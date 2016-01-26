package com.devicestats.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.devicestats.android.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by andyb129 on 22/01/2016.
 */
public class Utils {

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

    public static String getAppVersion(Context ctx) {
        String versionName = null;
        int versionCode = -1;
        try {
            PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), 0);
            versionName = pInfo.versionName;
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            versionName = null;
        }
        return ctx.getString(R.string.about_version, versionName, versionCode);
    }

    public static void dumpDataToSD(String filename, String results) throws Exception {
        File log = new File(Environment.getExternalStorageDirectory(), filename);

        if (log.exists()) {
            log.delete();
        }

        BufferedWriter out = new BufferedWriter(new FileWriter(
                log.getAbsolutePath(), log.exists()));
        out.write(results);
        out.close();
    }

}
