package com.gnm.katalogfilm.reminder;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalData {

    private static final String APP_SHARED_PREFS = "RemindMePref";
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    private static final String prefDailyReminder = "dailyReminder";
    private static final String prefReleaseTodayReminder = "releaseTodayReminder";

    public LocalData(Context context) {
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public boolean getDailyReminderStatus() {
        return appSharedPrefs.getBoolean(prefDailyReminder, false);
    }

    public void setDailyReminderStatus(boolean status) {
        prefsEditor.putBoolean(prefDailyReminder, status);
        prefsEditor.commit();
    }

    public boolean getReleaseTodayReminderStatus() {
        return appSharedPrefs.getBoolean(prefReleaseTodayReminder, false);
    }

    public void setReleaseTodayReminderStatus(boolean status) {
        prefsEditor.putBoolean(prefReleaseTodayReminder, status);
        prefsEditor.commit();
    }
}
