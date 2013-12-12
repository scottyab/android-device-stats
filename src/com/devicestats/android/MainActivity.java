package com.devicestats.android;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.crittercism.app.CrittercismConfig;

/**
 * 
 * @author scottab
 * 
 */
public class MainActivity extends Activity {

	private static final String TAG = null;
	private String results;
	private String filename = "devicestats.txt";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initCrashReporting();

		setContentView(R.layout.main);

		StatsGatherer gatherer = new StatsGatherer(this);
		results = gatherer.printStatsToString();

		((TextView) findViewById(R.id.infoText)).setText(results);
	}

	private void initCrashReporting() {
		CrittercismConfig config = new CrittercismConfig();
		config.setVersionCodeToBeIncludedInVersionString(true);
		Crittercism.initialize(getApplicationContext(),
				"527b7b6a97c8f25ea9000003", config);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.default_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		long id = item.getItemId();
		if (R.id.menu_share == id) {
			Intent emailIntent = newEmailIntent(getApplicationContext(), null,
					"DeviceStats " + getAppVersion(), results, true);
			try {
				startActivity(emailIntent);
			} catch (ActivityNotFoundException e) {
				Intent shareIntent = newEmailIntent(getApplicationContext(),
						null, "DeviceStats " + getAppVersion(), results, false);
				startActivity(Intent.createChooser(shareIntent, "Share via"));
			}
			return true;
		} else if (R.id.menu_save_to_sd == id) {
			try {
				dumpDataToSD();
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

	public static final String MIME_TYPE_EMAIL = "message/rfc822";

	public static Intent newEmailIntent(final Context context,
			final String address, final String subject, final String body,
			boolean useEmailMime) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
		intent.putExtra(Intent.EXTRA_TEXT, body);
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		if (useEmailMime) {
			intent.setType(MIME_TYPE_EMAIL);
		}

		return intent;
	}

	private String getAppVersion() {
		String versionName = null;
		int versionCode = -1;
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			versionName = pInfo.versionName;
			versionCode = pInfo.versionCode;
		} catch (NameNotFoundException ex) {
			versionName = null;
		}
		return getString(R.string.about_version, versionName, versionCode);
	}

	private void dumpDataToSD() throws Exception {
		File log = new File(Environment.getExternalStorageDirectory(), filename);

		if (log.exists()) {
			log.delete();
		}

		Log.d(TAG, "Wrting devicestats to " + filename);

		BufferedWriter out = new BufferedWriter(new FileWriter(
				log.getAbsolutePath(), log.exists()));
		out.write(results);
		out.close();
	}

}