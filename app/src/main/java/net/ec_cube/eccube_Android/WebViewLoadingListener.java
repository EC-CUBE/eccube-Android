package net.ec_cube.eccube_Android;

/**
 * WebViewの読み込み状態を表すリスナです。
 */
public interface WebViewLoadingListener {

    /**
     * 読み込みを開始した時に通知します。
     */
    void onStartLoading();

    /**
     * 読み込みを終了した時に通知します。
     */
    void onFinishLoading();

}
