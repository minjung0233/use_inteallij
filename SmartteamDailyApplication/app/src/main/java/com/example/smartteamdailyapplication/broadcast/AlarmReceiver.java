package com.example.smartteamdailyapplication.broadcast;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.smartteamdailyapplication.MainActivity;
import com.example.smartteamdailyapplication.R;

public class AlarmReceiver extends BroadcastReceiver {
    private int code;
    private String title, desc;

    @Override
    public void onReceive(Context context, Intent intent) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        title = intent.getStringExtra("TITLE");
        desc = intent.getStringExtra("DESC");

        code = intent.getIntExtra("CODE", 0);


        Intent i = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE);

        Intent cancelIntent = new Intent(context, CancelAlarmBroadcast.class);
        cancelIntent.putExtra("CODE", code);
        PendingIntent actionIntent = PendingIntent.getBroadcast(context, code, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "plannerAlarm")
                .setSmallIcon(R.drawable.ic_clock)
                .setContentTitle(title)
                .setContentText(desc)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_cancel, context.getString(R.string.cancel), actionIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123,builder.build());


        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        MediaPlayer mp = MediaPlayer.create(context, alert);
        if(mp !=null) {
            mp.setVolume(100, 100);
            mp.start();
            mp.setOnCompletionListener(MediaPlayer::release);
        }

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
    }
}
