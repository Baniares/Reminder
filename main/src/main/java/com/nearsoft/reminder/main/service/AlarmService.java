package com.nearsoft.reminder.main.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.nearsoft.reminder.main.R;
import com.nearsoft.reminder.main.ReminderFragmentActivity;
import com.nearsoft.reminder.main.Reminders;
import com.nearsoft.reminder.main.helpers.AlarmManagerHelper;

/**
 * Created by Baniares on 4/23/14.
 */
public class AlarmService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(intent.getExtras().getString("title"))
                        .setContentText(intent.getExtras().getString("description"));
        SharedPreferences prefs = getSharedPreferences("user", 0);
        int idUser = prefs.getInt("id",-200);
        if(idUser!=-200&&idUser!=-6){
            Intent resultIntent = new Intent(this, ReminderFragmentActivity.class);
            resultIntent.putExtra("ReminderId",intent.getExtras().getInt("id"));
            resultIntent.putExtra("mode",2);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(Reminders.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.notify(34567654, mBuilder.build());
        }
        AlarmManagerHelper.setAlarms(this);
        return super.onStartCommand(intent, flags, startId);
    }
}
