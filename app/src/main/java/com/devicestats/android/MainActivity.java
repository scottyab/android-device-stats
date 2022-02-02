package com.devicestats.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private static String filename = "devicestats.txt";

    private DeviceStats mDeviceStats;
    private String mDeviceStatsString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initStats();
        initViews();
    }

    private void initStats() {
        //TODO - move to background thread
        StatsGatherer gatherer = new StatsGatherer(this);
        mDeviceStatsString = gatherer.printStatsToString();
        mDeviceStats = gatherer.getDeviceStats();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        DeviceStatsAdapter adapter = new DeviceStatsAdapter(this, getSupportFragmentManager(), mDeviceStats);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);

        tabLayout.setTabsFromPagerAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //TODO - maybe move this share back to action bar menu as does get in the way of data
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareAllDeviceStatsText();
            }
        });
    }

    private void shareAllDeviceStatsText() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mDeviceStatsString);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

}
