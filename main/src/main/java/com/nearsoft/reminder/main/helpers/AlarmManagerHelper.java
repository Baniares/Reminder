package com.nearsoft.reminder.main.helpers;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nearsoft.reminder.main.models.Reminder;
import com.nearsoft.reminder.main.service.AlarmService;

import java.util.List;

/**
 * Created by Baniares on 4/23/14.
 */
public class AlarmManagerHelper extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        setAlarms(context);
    }

    public static void setAlarms(Context context){
        cancelAlarms(context);
        DBHelper db = new DBHelper(context);
        List<Reminder> reminders = db.getNotStartedReminders();
        if(reminders!=null){
            for(Reminder reminder : reminders){
                PendingIntent pIntent = createPendingIntent(context, reminder);
                setAlarm(context,reminder.getStartDateToMillis(),pIntent);
            }
        }
    }

    @SuppressLint("NewApi")
    private static void setAlarm(Context context,long time,PendingIntent pIntent){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT){
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pIntent);
        }
    }

    public static void cancelAlarms(Context context){
        DBHelper db = new DBHelper(context);
        List<Reminder> reminders = db.getNotStartedReminders();

        if(reminders!=null){
            for(Reminder reminder : reminders){
                PendingIntent pIntent = createPendingIntent(context, reminder);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pIntent);
            }
        }
    }

    private static PendingIntent createPendingIntent(Context context, Reminder reminder){
        Intent intent = new Intent(context, AlarmService.class);
        intent.putExtra("id",reminder.getId());
        intent.putExtra("userId",reminder.getUserID());
        intent.putExtra("title",reminder.getTitle());
        intent.putExtra("description",reminder.getDescription());
        intent.putExtra("startDate",reminder.getStartDateToMillis());
        intent.putExtra("endDate",reminder.getEndDateToMillis());
        return PendingIntent.getService(context, reminder.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
