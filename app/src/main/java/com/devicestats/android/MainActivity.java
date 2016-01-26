package com.devicestats.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.default_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long id = item.getItemId();
        //TODO - maybe reitegrate this but replaced with simple share above
        /*if (R.id.menu_share == id) {
            Intent emailIntent = Utils.newEmailIntent(getApplicationContext(), null,
                    "DeviceStats " + Utils.getAppVersion(this), mDeviceStatsString, true);
            try {
                startActivity(emailIntent);
            } catch (ActivityNotFoundException e) {
                Intent shareIntent = Utils.newEmailIntent(getApplicationContext(),
                        null, "DeviceStats " + Utils.getAppVersion(this), mDeviceStatsString, false);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
            return true;
        } else*/
            if (R.id.menu_save_to_sd == id) {
            try {
                Utils.dumpDataToSD(filename, mDeviceStatsString);
                Toast.makeText(getApplicationContext(), "Saved to " + filename,
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to save.",
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
