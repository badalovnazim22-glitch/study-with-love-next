package com.studywithlove.next;

import android.app.*;
import android.content.*;
import android.os.Build;

public class ReminderReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        String channel="study_with_love_reminders";
        NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=26){
            NotificationChannel c=new NotificationChannel(channel,"Напоминания об учёбе",NotificationManager.IMPORTANCE_DEFAULT);
            c.setDescription("Мягкие напоминания уделить время себе и учёбе");
            manager.createNotificationChannel(c);
        }
        Intent open=new Intent(context,MainActivity.class);
        PendingIntent content=PendingIntent.getActivity(context,78,open,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        Notification.Builder b=Build.VERSION.SDK_INT>=26?new Notification.Builder(context,channel):new Notification.Builder(context);
        Notification n=b.setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle("Время для себя ✨").setContentText("Сделай маленький шаг к своей цели. Ты справишься!").setContentIntent(content).setAutoCancel(true).build();
        manager.notify(78,n);
    }
}