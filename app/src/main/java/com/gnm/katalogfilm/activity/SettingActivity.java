package com.gnm.katalogfilm.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.gnm.katalogfilm.BuildConfig;
import com.gnm.katalogfilm.R;
import com.gnm.katalogfilm.api.Api;
import com.gnm.katalogfilm.api.ApiInterface;
import com.gnm.katalogfilm.model.DetailFilm;
import com.gnm.katalogfilm.model.Film;
import com.gnm.katalogfilm.reminder.dailyReminder.DailyReminder;
import com.gnm.katalogfilm.reminder.LocalData;
import com.gnm.katalogfilm.reminder.releaseTodayReminder.ReleaseTodayReminder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {

    LocalData localData;

    DailyReminder dailyReminder = new DailyReminder();
    ReleaseTodayReminder releaseTodayReminder = new ReleaseTodayReminder();

    List<DetailFilm> filmList;
    List<DetailFilm> filmListOther;
    ApiInterface apiInterface;
    Call<Film> filmCall;

    AlertDialog.Builder alertDialogBuilder;

    @BindView(R.id.klikReleaseTodayReminder1)
    RelativeLayout onOFFReleaseTodayReminder;

    @BindView(R.id.klikDailyReminder1)
    RelativeLayout onOffDailyReminder;

    @BindView(R.id.klikLanguage)
    RelativeLayout klikLanguage;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.klikDailyReminder)
    Switch klikDailyReminder;

    @BindView(R.id.klikReleaseTodayReminder)
    Switch klikReleaseTodayReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        filmList = new ArrayList<>();
        filmListOther = new ArrayList<>();

        alertDialogBuilder = new AlertDialog.Builder(this);


        localData = new LocalData(getApplicationContext());

        klikDailyReminder.setChecked(localData.getDailyReminderStatus());

        if (klikDailyReminder.isChecked()) {
            onOffDailyReminder.setAlpha(1f);
        }
        klikReleaseTodayReminder.setChecked(localData.getReleaseTodayReminderStatus());

        if (klikReleaseTodayReminder.isChecked()) {
            onOFFReleaseTodayReminder.setAlpha(1f);
        }

        klikLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }
        });

        klikDailyReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                localData.setDailyReminderStatus(isChecked);
                if (isChecked) {
                    dailyReminder.setReminder(SettingActivity.this);
                    onOffDailyReminder.setAlpha(1f);
                } else {
                    alertDialogBuilder.setTitle(R.string.reminder);
                    alertDialogBuilder
                            .setMessage(R.string.message_dialog_daily)
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int id) {
                                            dailyReminder.cancelReminder(SettingActivity.this);
                                            onOffDailyReminder.setAlpha(0.4f);
                                        }
                                    })
                            .setNegativeButton(R.string.no,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                            klikDailyReminder.setChecked(true);
                                        }
                                    }).create().show();
                }
            }
        });
        klikReleaseTodayReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                localData.setReleaseTodayReminderStatus(isChecked);
                if (isChecked) {
                    setReleaseReminder();
                    onOFFReleaseTodayReminder.setAlpha(1f);
                } else {
                    alertDialogBuilder.setTitle(R.string.reminder);
                    alertDialogBuilder
                            .setMessage(R.string.message_dialog_release_today)
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int id) {
                                            releaseTodayReminder.cancelReminder(SettingActivity.this);
                                            onOFFReleaseTodayReminder.setAlpha(0.4f);
                                        }
                                    })
                            .setNegativeButton(R.string.no,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                            klikReleaseTodayReminder.setChecked(true);
                                        }
                                    }).create().show();
                }
            }
        });
    }


    private void setReleaseReminder() {
        apiInterface = Api.getApi().create(ApiInterface.class);
        filmCall = apiInterface.getUpComing(BuildConfig.TMDB_API_KEY);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        final String now = dateFormat.format(date);

        filmCall.enqueue(new Callback<Film>() {
            @Override
            public void onResponse(Call<Film> call, Response<Film> response) {
                filmList = response.body().getmFilmResults();
                for (DetailFilm detailFilm : filmList) {
                    if (detailFilm.getmReleaseDate().equals(now)) {
                        filmListOther.add(detailFilm);
                    }
                }
                releaseTodayReminder.setReminder(SettingActivity.this, filmListOther);

            }

            @Override
            public void onFailure(Call<Film> call, Throwable t) {
                Toast.makeText(SettingActivity.this, getString(R.string.stError)
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
