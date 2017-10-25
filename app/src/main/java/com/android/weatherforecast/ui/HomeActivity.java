package com.android.weatherforecast.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.weatherforecast.R;
import com.android.weatherforecast.fragments.DashboardFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        DashboardFragment homeFragment = new DashboardFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, homeFragment).disallowAddToBackStack().commit();
    }
}
