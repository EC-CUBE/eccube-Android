package net.ec_cube.eccube_Android;

/**
 * 設定クラスです。
 */
public class Config {

    /**
     * スプラッシュを表示する時間
     */
    public static final long SPLASH_TIME_MILLIS = 2000L;

    /**
     * タブのTOPに対応するURL
     */
    public static final String BASE_URL = "http://example.com/";

    /**
     * タブのTOPに対応するURL
     */
    public static final String TAB_URL_TOP = BASE_URL + "";

    /**
     * タブのマイページに対応するURL
     */
    public static final String TAB_URL_MYPAGE = BASE_URL + "mypage";

    /**
     * タブの商品一覧に対応するURL
     */
    public static final String TAB_URL_ALL_ITEMS = BASE_URL + "products/list";

    /**
     * タブのカートに対応するURL
     */
    public static final String TAB_URL_CART = BASE_URL + "cart";

    /**
     * タブのお気に入りに対応するURL
     */
    public static final String TAB_URL_FAV = BASE_URL + "mypage/favorite";

    /**
     * ログインの対象URL
     */
    public static final String[] LOGIN_TARGET_URLS = {BASE_URL + "mypage/login", TAB_URL_MYPAGE};

    /**
     * Registration IDのキー
     */
    public static final String PROPERTY_REG_ID = "registration_id";

    /**
     * アプリバージョン用のキー
     */
    public static final String PROPERTY_APP_VERSION = "appVersion";

    /**
     * GCMのRegistrationIDを保持しておくためのプリファレンスキー
     */
    public static final String GCM_PREFERENCE = "eccube3demo";

    /**
     * GCMのSENDER ID(プロジェクト毎に異なります)
     */
    public static final String SENDER_ID = "";

    /**
     * アピアリーズのデータストアID
     */
    public static final String DATASTORE_ID = "";

    /**
     * アピアリーズのアプリケーションID
     */
    public static final String APPLICATION_ID = "";

    /**
     * アピアリーズのアプリケーショントークン
     */
    public static final String APPLICATION_TOKEN = "";

    /**
     * 通知タイトル
     */
    public static final String NOTIFICATION_KEY_TITLE = "title";

    /**
     * 通知メッセージ
     */
    public static final String NOTIFICATION_KEY_MESSAGE = "message";

    /**
     * Push通知IDのキー(アピアリーズから受け取る値)
     */
    public static final String KEY_PUSH_ID = "pushId";
}
