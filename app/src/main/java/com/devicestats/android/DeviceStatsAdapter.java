package com.devicestats.android;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

/**
 * Created by 1andbarb on 12/01/2016.
 */
public class DeviceStatsAdapter extends FragmentStatePagerAdapter {

    private static final int ITEM_COUNT = 6;
    private final DeviceStats mDeviceStats;
    private final Context mContext;

    private int[] imageResId = {
            R.drawable.ic_developer_mode_white_24dp,
            R.drawable.ic_perm_device_information_white_24dp,
            R.drawable.ic_speaker_phone_white_24dp,
            R.drawable.ic_lock_white_24dp,
            R.drawable.ic_vpn_key_white_24dp,
            R.drawable.ic_phonelink_lock_white_24dp
    };

    public DeviceStatsAdapter(Context ctx, FragmentManager fm, DeviceStats deviceStats) {
        super(fm);
        mContext = ctx;
        mDeviceStats = deviceStats;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DimensionsFragment.newInstance(mDeviceStats);
            case 1:
                return StatsFragment.newInstance(mDeviceStats, StatsFragment.DeviceInfoType.DEVICE_INFO);
            case 2:
                return StatsFragment.newInstance(mDeviceStats, StatsFragment.DeviceInfoType.HARDWARE);
            case 3:
                return StatsFragment.newInstance(mDeviceStats, StatsFragment.DeviceInfoType.CIPHERS);
            case 4:
                return StatsFragment.newInstance(mDeviceStats, StatsFragment.DeviceInfoType.ALGORITHMS);
            default:
                return StatsFragment.newInstance(mDeviceStats, StatsFragment.DeviceInfoType.ALGORITHMS_CRYPTO);

        }
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getImageInSpannableString(imageResId[position]);
    }

    @NonNull
    private CharSequence getImageInSpannableString(int i) {
        Drawable image = ContextCompat.getDrawable(mContext, i);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
