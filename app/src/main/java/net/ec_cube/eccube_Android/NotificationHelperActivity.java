package net.ec_cube.eccube_Android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Notificationを扱うためのActivityです。
 * 開封通知処理のコードを簡易化するためにこのActivityを挟んでいます。
 */
public class NotificationHelperActivity extends Activity {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent baseIntent = getIntent();
        final String action = baseIntent.getAction();
        // 開封通知のアクション
        if (PushBroadcastReceiver.ACTION_NOTIFICATION_OPEN.equals(action)) {
            // 開封通知用のサービスを開始
            final Intent openMessageIntent = new Intent(this, OpenMessageService.class);
            openMessageIntent.putExtra(Config.KEY_PUSH_ID, baseIntent.getStringExtra(Config.KEY_PUSH_ID));
            startService(openMessageIntent);
        }

        // アプリ起動
        final Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(PushBroadcastReceiver.ACTION_NOTIFICATION_OPEN);
        intent.putExtra(Config.NOTIFICATION_KEY_TITLE, baseIntent.getStringExtra(Config.NOTIFICATION_KEY_TITLE));
        intent.putExtra(Config.NOTIFICATION_KEY_MESSAGE, baseIntent.getStringExtra(Config.NOTIFICATION_KEY_MESSAGE));
        startActivity(intent);

        finish();
    }


}
