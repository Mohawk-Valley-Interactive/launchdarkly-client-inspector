package com.launchdarkly.android.clientinspector;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.launchdarkly.android.clientinspector.model.LdClientModel;
import com.launchdarkly.android.clientinspector.ui.main.SectionsPagerAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LdClientModel.registerActivity(this);
        LdClientModel.registerApplication(getApplication());
        LdClientModel.setDefaultMobileKey(getDefaultMobileKey());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    private String getDefaultMobileKey() {
        String defaultValue = "No default set; enter mobile key ...";

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("mobile-key.txt")));
            return reader.readLine();
        } catch (IOException e) {
            return defaultValue;
        }
    }
}