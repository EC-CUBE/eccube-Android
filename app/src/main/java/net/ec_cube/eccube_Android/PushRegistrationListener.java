package net.ec_cube.eccube_Android;

/**
 * PUSH通知の登録に成功した場合のコールバックです。
 */
public interface PushRegistrationListener {

    /**
     * 成功した場合に通知されます。
     */
    void onSuccess();

    /**
     * 失敗した場合に通知されます。
     */
    void onFailure();

}
