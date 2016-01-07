package net.ec_cube.eccube_Android;

/**
 * HTMLの挿入を行うイベントを扱うリスナです。
 */
public interface HtmlInjectionListener {

    /**
     * 挿入が完了した場合に呼び出されます。
     *
     * @param newHtml 挿入済みのHTML
     */
    void onInjectionFinish(String newHtml);
}
