package com.gnm.katalogfilm.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gnm.katalogfilm.R;
import com.gnm.katalogfilm.adapter.AdapterViewpager;
import com.gnm.katalogfilm.database.DatabaseContract;
import com.gnm.katalogfilm.fragment.NowFragment;
import com.gnm.katalogfilm.fragment.UpComingFragment;
import com.gnm.katalogfilm.model.Favorite;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tab)
    TabLayout tab;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        initViewpager(viewPager);
        tab.setupWithViewPager(viewPager);
    }

    void initViewpager(ViewPager viewPager) {
        AdapterViewpager adapter = new AdapterViewpager(getSupportFragmentManager());
        adapter.getFragment(new NowFragment(), getString(R.string.now));
        adapter.getFragment(new UpComingFragment(), getString(R.string.up_coming));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_setting) {
            Intent mIntent = new Intent(this, SettingActivity.class);
            startActivity(mIntent);
            return true;
        } else if (id == R.id.menu_about) {
            Intent mIntent = new Intent(this, AboutActivity.class);
            startActivity(mIntent);
            return true;
        } else if (id == R.id.menu_favorit) {
            Intent intent = new Intent(MainActivity.this, FavoritActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_search) {
            Intent mIntent = new Intent(this, SearchActivity.class);
            startActivity(mIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    boolean doubleBackToExit = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExit) {
            finishAffinity();
        } else {
            Toast.makeText(this, getString(R.string.message_exit), Toast.LENGTH_SHORT).show();
        }
        int timee = 2000;
        this.doubleBackToExit = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExit = false;
            }
        }, timee);
    }


}
