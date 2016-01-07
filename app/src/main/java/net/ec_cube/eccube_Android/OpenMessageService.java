package net.ec_cube.eccube_Android;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.appiaries.baas.sdk.AB;
import com.appiaries.baas.sdk.ABDevice;
import com.appiaries.baas.sdk.ABException;
import com.appiaries.baas.sdk.ABPushMessage;
import com.appiaries.baas.sdk.ABResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 非同期で開封通知を送信するクラスです。
 * pushId（Config.KEY_PUSH_ID）をIntent経由で渡してください。
 */
public class OpenMessageService extends IntentService {

    /**
     * コンストラクタ
     */
    public OpenMessageService() {
        super("OpenMessageService");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("openMessageの開始");

        // アピアリーズ初期化
        AB.Config.setDatastoreID(Config.DATASTORE_ID);
        AB.Config.setApplicationID(Config.APPLICATION_ID);
        AB.Config.setApplicationToken(Config.APPLICATION_TOKEN);
        AB.Config.Push.setSenderID(Config.SENDER_ID);
        // 開封通知のイベント送信準備
        AB.Config.Push.setOpenMessage(true);
        AB.activate(this);

        try {
            // RegistrationIDを取得
            final SharedPreferences preferences = getSharedPreferences(Config.GCM_PREFERENCE, Context.MODE_PRIVATE);
            final String regId = preferences.getString(Config.PROPERTY_REG_ID, "");
            final long pushId = Long.parseLong(intent.getStringExtra(Config.KEY_PUSH_ID));
            // ABDeviceにRegistrationIDを登録
            final ABDevice device = new ABDevice();
            device.setID(regId);
            // ABPushMessageにdevice情報をセット
            final Map<String, Object> map = new HashMap<>();
            map.put("device", device);
            final ABPushMessage pushMessage = new ABPushMessage();
            pushMessage.setDevice(device);
            pushMessage.setPushId(pushId);
            // 開封通知を発行して結果を取得
            final ABResult<Void> result = AB.PushService.openMessageSynchronously(pushMessage);
            System.out.println("pushId:" + pushId);
            System.out.println("regId:" + regId);
            System.out.println(result.getCode());
        } catch (ABException e) {
            System.out.println("error:" + e);
        }
    }
}