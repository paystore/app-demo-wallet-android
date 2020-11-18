package com.phoebus.appdemowallet.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.phoebus.appdemowallet.R;
import com.phoebus.appdemowallet.activities.MainActivity;
import com.phoebus.libwallet.utils.Constants;


public class AppDemoWalletFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "Firebase message";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(Constants.TAG, TAG + " | from: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(Constants.TAG, TAG + " | data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Long now = System.currentTimeMillis();
            int id = now.intValue();
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Log.d(Constants.TAG, TAG + " | notification: {"
                    + "\n  id: " + id
                    + "\n  title: " + title
                    + "\n  body: " + body
            + "\n}");

            showNotification(id, title, body);
        }

    }


    @Override
    public void onNewToken(String token) {
        Log.d(Constants.TAG, TAG + " | refreshed token: " + token);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param id generated notification id.
     * @param title FCM message title received.
     * @param body FCM message body received.
     */
    private void showNotification(int id, String title, String body) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.firebase_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_phoebus_default_foreground)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(body));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    getString(R.string.firebase_notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(id, notificationBuilder.build());
    }
}