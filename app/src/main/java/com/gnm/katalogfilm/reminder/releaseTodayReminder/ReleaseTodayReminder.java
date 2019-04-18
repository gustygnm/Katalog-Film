package com.gnm.katalogfilm.reminder.releaseTodayReminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.gnm.katalogfilm.R;
import com.gnm.katalogfilm.activity.DetailActivity;
import com.gnm.katalogfilm.model.DetailFilm;

import java.util.Calendar;
import java.util.List;

public class ReleaseTodayReminder extends BroadcastReceiver {

    private static int NOTIFICATION_ID = 1000;
    private static String CHANNEL_ID = "channel_01";
    private static CharSequence CHANNEL_NAME = "gnm channel";

    private final int REMINDER_HOUR = 8;
    private final int REMINDER_MINUTE = 00;
    private final int REMINDER_SECOND = 00;

    @Override
    public void onReceive(Context context, Intent intent) {
        String filmTitle = intent.getStringExtra("filmtitle");
        int id = intent.getIntExtra("id", 0);
        int filmId = intent.getIntExtra("filmid", 0);
        String poster = intent.getStringExtra("filmposter");
        String back = intent.getStringExtra("filmback");
        String date = intent.getStringExtra("filmdate");
        Double rate = intent.getDoubleExtra("filmrating", 0);
        String ovr = intent.getStringExtra("filmover");

        DetailFilm filmResult = new DetailFilm(filmId, filmTitle, poster, back, date, rate, ovr);
        String desc = String.valueOf(String.format(context.getString(R.string.message_release_today_reminder), filmTitle));
        sendNotification(context, filmTitle, desc, id, filmResult);
    }

    private void sendNotification(Context context, String title, String desc, int id, DetailFilm filmResult) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("id", filmResult.getmId().toString());
        intent.putExtra("img", filmResult.getmBackdropPath());
        intent.putExtra("judul", filmResult.getmOriginalTitle());

        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(desc)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);

            builder.setChannelId(CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(id, builder.build());
    }

    public void setReminder(Context context, List<DetailFilm> filmResults) {
        int delay = 0;

        for (DetailFilm film : filmResults) {
            cancelReminder(context);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, ReleaseTodayReminder.class);
            intent.putExtra("filmtitle", film.getmOriginalTitle());
            intent.putExtra("filmid", film.getmId());
            intent.putExtra("filmposter", film.getmPosterPath());
            intent.putExtra("filmback", film.getmBackdropPath());
            intent.putExtra("filmdate", film.getmReleaseDate());
            intent.putExtra("filmrating", film.getmVoteAverage());
            intent.putExtra("filmover", film.getmOverview());
            intent.putExtra("id", NOTIFICATION_ID);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, REMINDER_HOUR);
            calendar.set(Calendar.MINUTE, REMINDER_MINUTE);
            calendar.set(Calendar.SECOND, REMINDER_SECOND);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis() + delay,
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                );
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis() + delay, pendingIntent);
            }
            NOTIFICATION_ID += 1;
            delay += 3000;
        }
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, ReleaseTodayReminder.class);
        return PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void cancelReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context));
    }


}
