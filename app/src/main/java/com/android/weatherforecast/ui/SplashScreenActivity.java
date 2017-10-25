package com.android.weatherforecast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.android.weatherforecast.R;
import com.android.weatherforecast.services.LocationUpdateService;


public class SplashScreenActivity extends AppCompatActivity {


    Runnable mShowloginScreen = new Runnable() {
        @Override
        public void run() {
            Intent homeIntent = new Intent(SplashScreenActivity.this,
                    HomeActivity.class);
            startActivity(homeIntent);
            finish();
            }
        };

    private long DELAY_LOGIN = 2000;
    private String IS_ICON_CREATED = "IS_ICON_CREATED";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         startService(new Intent(this, LocationUpdateService.class));
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {

            finish();
            return;
        }
        setContentView(R.layout.activity_splash_screen);

    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(mShowloginScreen, DELAY_LOGIN);
    }

    private void addShortcut() {
        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(),
                SplashScreenActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "WeatherForecast");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        R.mipmap.ic_launcher));

        addIntent
                .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }
}