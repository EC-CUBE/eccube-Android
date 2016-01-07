package net.ec_cube.eccube_Android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * Webを表示する画面です。
 * ※制限事項：ページを完全に読み込む前にタブを切り替えると履歴が残る場合があります。
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, HtmlInjectionListener, WebViewLoadingListener {

    /**
     * WebView
     */
    private WebView mWebView;

    /**
     * 通知情報
     */
    private View mNotificationInfoView;

    /**
     * WebViewの読み込み状態表示
     */
    private View mProgressBar;

    /**
     * CustomWebViewClient
     */
    private CustomWebViewClient mWebViewClient;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 今回はToolBarを全て非表示にしていますが、Toolbarを使用する場合は以下のコードを有効にしてください
        //setSupportActionBar(toolbar);

        // ActionBarの設定
        //final ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setDisplayHomeAsUpEnabled(true);

        // タブの設定
        findViewById(R.id.tab_top).setOnClickListener(this);
        findViewById(R.id.tab_mypage).setOnClickListener(this);
        findViewById(R.id.tab_all_items).setOnClickListener(this);
        findViewById(R.id.tab_cart).setOnClickListener(this);
        findViewById(R.id.tab_fav).setOnClickListener(this);

        // 通知情報View
        mNotificationInfoView = findViewById(R.id.notification_info);

        // 読み込み状態表示
        mProgressBar = findViewById(R.id.progress);

        // WebViewの設定
        mWebView = (WebView) findViewById(R.id.webview);
        final WebSettings webSettings = mWebView.getSettings();
        // WebViewの初期化
        webSettings.setJavaScriptEnabled(true);
        mWebViewClient = new CustomWebViewClient(this);
        mWebView.setWebViewClient(mWebViewClient);
        // RegistrationID
        final SharedPreferences preferences = getSharedPreferences(Config.GCM_PREFERENCE, Context.MODE_PRIVATE);
        final String regId = preferences.getString(Config.PROPERTY_REG_ID, "");
        mWebView.addJavascriptInterface(new JavaScriptInterface(regId, this), "HtmlViewer");

        // 通知から来た場合は通知の内容を表示
        final Intent intent = getIntent();
        if (intent != null && PushBroadcastReceiver.ACTION_NOTIFICATION_OPEN.equals(intent.getAction())) {
            mWebView.setVisibility(View.GONE);
            mNotificationInfoView.setVisibility(View.VISIBLE);
            final String title = intent.getStringExtra(Config.NOTIFICATION_KEY_TITLE);
            final String message = intent.getStringExtra(Config.NOTIFICATION_KEY_MESSAGE);
            ((TextView) findViewById(R.id.notification_title)).setText(title);
            ((TextView) findViewById(R.id.notification_message)).setText(message);
        } else {
            mWebView.loadUrl(Config.TAB_URL_TOP);
            mWebView.setVisibility(View.VISIBLE);
            mNotificationInfoView.setVisibility(View.GONE);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPause() {
        mWebView.onPause();
        super.onPause();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy() {
        mWebView.destroy();
        super.onDestroy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        // ActonBarのホーム
        if (id == android.R.id.home) {
            onBack();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
        final int id = v.getId();
        mWebView.setVisibility(View.VISIBLE);
        mNotificationInfoView.setVisibility(View.GONE);
        // トップ
        if (id == R.id.tab_top) {
            mWebView.loadUrl(Config.TAB_URL_TOP);
        }
        // マイページ
        else if (id == R.id.tab_mypage) {
            mWebView.loadUrl(Config.TAB_URL_MYPAGE);
        }
        // 全商品
        else if (id == R.id.tab_all_items) {
            mWebView.loadUrl(Config.TAB_URL_ALL_ITEMS);
        }
        // カート
        else if (id == R.id.tab_cart) {
            mWebView.loadUrl(Config.TAB_URL_CART);
        }
        // お気に入り
        else if (id == R.id.tab_fav) {
            mWebView.loadUrl(Config.TAB_URL_FAV);
        }

        // ページ読み込み後。履歴を削除する
        mWebViewClient.setClearHistory(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        onBack();
    }

    /**
     * バックキーもしくはバックと同等の処理が行われた際に呼び出されます。
     */
    private void onBack() {
        // WebViewが表示されていて、履歴をもつ場合
        if (mWebView.getVisibility() == View.VISIBLE && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    /**
     * {@inheritDoc}
     */
    @JavascriptInterface
    @Override
    public void onInjectionFinish(final String newHtml) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.loadDataWithBaseURL(Config.BASE_URL, newHtml, "text/html; charset=utf-8;", "utf-8",null);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStartLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFinishLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * EC Cube 3用のWebViewClientです。
     */
    static class CustomWebViewClient extends WebViewClient {

        /**
         * ページ履歴を消す場合の処理
         */
        private boolean mClearHistory;

        /**
         * Progress表示のWeakReference
         */
        private final WebViewLoadingListener mListener;

        /**
         * コンストラクタ
         *
         * @param listener WebViewLoadingListener
         */
        CustomWebViewClient(WebViewLoadingListener listener) {
            mListener = listener;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mListener.onStartLoading();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mClearHistory) {
                view.clearHistory();
                mClearHistory = false;
            }

            // URLが完全一致した場合にJavaScriptの処理をかける
            for (String targetUrl : Config.LOGIN_TARGET_URLS) {
                if (url.startsWith(targetUrl)) {
                    view.loadUrl("javascript:HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                    break;
                }
            }

            mListener.onFinishLoading();
        }

        /**
         * ページの履歴削除フラグを変更します。
         *
         * @param clearHistory ページ履歴を消す場合はtrue
         */
        public void setClearHistory(boolean clearHistory) {
            mClearHistory = clearHistory;
        }

    }

    /**
     * JavaScriptの処理
     */
    static class JavaScriptInterface {

        /**
         * RegistrationID
         */
        private final String mRegId;

        /**
         * HtmlInjectionListener
         */
        private final HtmlInjectionListener mListener;

        /**
         * コンストラクタ
         *
         * @param regId    RegistrationID
         * @param listener HtmlInjectionListener
         */
        JavaScriptInterface(String regId, HtmlInjectionListener listener) {
            mRegId = regId;
            mListener = listener;
        }

        @JavascriptInterface
        public void showHTML(String html) {
            // 挿入するHTMLの作成
            String insertStr = "<input type=\"hidden\" name=\"login_device_id\" value=\"" + mRegId + "\"/>";
            insertStr += "<input type=\"hidden\" name=\"login_os\" value=\"android\">";

            // 簡易HTML解析(指定文字の後ろに挿入する)
            final String targetText = "<form name=\"login_mypage\"";
            final int index = html.indexOf(targetText);

            // 見つかった場合
            if (index != -1) {
                // 閉じタグを見つける
                final String closeTag = "</form>";
                final int closeTagIndex = html.indexOf(closeTag, index);
                final StringBuilder builder = new StringBuilder(html);
                builder.insert(closeTagIndex - closeTag.length(), insertStr);
                // 完了を通知
                mListener.onInjectionFinish(builder.toString());
            }
        }
    }
}
