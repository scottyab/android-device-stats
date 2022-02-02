package com.devicestats.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class DimensionsFragment extends Fragment {

    private static final String TAG = "DimensionsFragment";
    private static final String ARG_PARAM1 = "param1";

    private TextView mLabelArrowWidthTextView;
    private TextView mLabelArrowHeightTextView;
    private TextView mModelTextView;
    private TextView mScreenDimensionDipsTextView;
    private TextView mScreenDimensionPixelsTextView;

    private DeviceStats mDeviceStats;

    public static DimensionsFragment newInstance(DeviceStats deviceStats) {
        DimensionsFragment fragment = new DimensionsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, deviceStats);
        fragment.setArguments(args);
        return fragment;
    }

    public DimensionsFragment()
    {
        
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDeviceStats = (DeviceStats) getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dimensions, container, false);

        getViews(view);
        setData();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //need to delay this as dimensions aren't available initially
        view.post(new Runnable() {
            @Override
            public void run() {
                showViewDimensions();
            }
        });
    }

    private void getViews(View view) {
        mScreenDimensionPixelsTextView = (TextView) view.findViewById(R.id.screenDimensionsPixels);
        mScreenDimensionDipsTextView = (TextView) view.findViewById(R.id.screenDimensionsDips);
        mModelTextView = (TextView) view.findViewById(R.id.model);
        mLabelArrowWidthTextView = (TextView) view.findViewById(R.id.horizontal_label);
        mLabelArrowHeightTextView = (TextView) view.findViewById(R.id.vertical_label);
    }

    private void setData() {
        mScreenDimensionPixelsTextView.setText(getString(R.string.screen_dimensions_pixels,
                mDeviceStats.getHeight(), mDeviceStats.getWidth()));
        mScreenDimensionDipsTextView.setText(getString(R.string.screen_dimensions_dips,
                mDeviceStats.getHeightDpi(), mDeviceStats.getWidthDpi()));
        mModelTextView.setText(mDeviceStats.getModel());
    }

    protected void showViewDimensions() {
        /*DisplayMetrics metrics = getResources().getDisplayMetrics();
        mWidthDp = mWidth/metrics.density;
        mHeightDp = mHeight/metrics.density;*/

        mLabelArrowWidthTextView.setText(getString(R.string.view_width, mDeviceStats.getWidthDpi(),
                mDeviceStats.getWidth()));
        mLabelArrowHeightTextView.setText(getString(R.string.view_height, mDeviceStats.getHeightDpi(),
                mDeviceStats.getHeight()));
    }

}
