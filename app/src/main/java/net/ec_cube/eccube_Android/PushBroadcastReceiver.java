package net.ec_cube.eccube_Android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * PUSH通知を受け取るためのBroadcastReceiverです。
 */
public class PushBroadcastReceiver extends WakefulBroadcastReceiver {

    /**
     * 通知の開封アクション
     */
    public static final String ACTION_NOTIFICATION_OPEN = "appiaries.intent.action.NOTIFICATION_OPEN";

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // Notificationの設定
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        // アイコン
        builder.setSmallIcon(R.mipmap.ic_launcher);
        final String title = intent.getStringExtra(Config.NOTIFICATION_KEY_TITLE);
        final String message = intent.getStringExtra(Config.NOTIFICATION_KEY_MESSAGE);
        // Tickerテキスト(ステータスバーに表示される文字列)
        builder.setTicker(title);
        // 通知内容のタイトル
        builder.setContentTitle(title);
        // 通知内容のテキスト
        builder.setContentText(message);
        // タップすれば消える設定
        builder.setAutoCancel(true);

        // タップされた際に発行するIntent
        final Intent newIntent = new Intent(context, NotificationHelperActivity.class);
        newIntent.setAction(ACTION_NOTIFICATION_OPEN);
        // 渡されたデータを全て入れる
        newIntent.putExtras(intent.getExtras());
        final PendingIntent contentIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        // 音とバイブレーション
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        // NotificationManagerの生成
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

}
