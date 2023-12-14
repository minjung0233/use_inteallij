package com.example.smartteamdailyapplication.broadcast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.smartteamdailyapplication.R;

public class CancelAlarmBroadcast extends BroadcastReceiver {
    AlarmManager mAlarmManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        int code = intent.getIntExtra("CODE",0);

        Intent i = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,code,intent,0);

        if(mAlarmManager== null){
            mAlarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        }
        mAlarmManager.cancel(pendingIntent);
        pendingIntent.cancel();

        Toast.makeText(context,context.getString(R.string.alarm_disabled),Toast.LENGTH_SHORT).show();
    }
}
