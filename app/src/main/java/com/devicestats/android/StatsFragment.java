package com.devicestats.android;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devicestats.android.R;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.prototypes.CardWithList;
import it.gmariotti.cardslib.library.view.CardView;*/


/**
 * device stats fragment
 */
public class StatsFragment extends Fragment {

    private static final String TAG = "StatsFragment";
    private static final String ARG_DEVICE_STATS = TAG + "_device_info_type";
    private static final String ARG_DEVICE_INFO_TYPE = TAG + "_display_hardware";
    private static final String EXTRA_DEVICE_STATS = TAG + "_extra_device_stats";
    private static final String EXTRA_DEVICE_INFO_TYPE = TAG + "_extra_device_info_type";

    private LinearLayout mCardParentLayout;

    public enum DeviceInfoType
    {
        DEVICE_INFO,
        HARDWARE,
        CIPHERS,
        ALGORITHMS,
        ALGORITHMS_CRYPTO
    }

    private DeviceStats mDeviceStats;
    private CardView mCardView;
    private RecyclerView mRecyclerView;
    private ImageView mCardHeaderImageView;
    private TextView mCardHeaderTextView;
    private DeviceInfoType mDeviceInfoType;

    public StatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param deviceStats
     * @return A new instance of fragment StatsFragment.
     */
    public static StatsFragment newInstance(DeviceStats deviceStats, DeviceInfoType deviceInfoType) {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DEVICE_STATS, deviceStats);
        args.putSerializable(ARG_DEVICE_INFO_TYPE, deviceInfoType);
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        if (getArguments() != null) {
            mDeviceStats = (DeviceStats) getArguments().getParcelable(ARG_DEVICE_STATS);
            mDeviceInfoType = (DeviceInfoType) getArguments().getSerializable(ARG_DEVICE_INFO_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_stats, container, false);
        mCardParentLayout = (LinearLayout) view.findViewById(R.id.fragment_stats_card_parent_layout);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        //needed to add cards here as onViewCreated doesn't get called on rotation of device
        addCards();
    }

    private void addCards() {
        Log.i(TAG, "addCards");
        mCardParentLayout.removeAllViews();
        Map<String, Integer> valueImageMap = new HashMap<>();
        valueImageMap.put(DeviceStats.HAS_HARDWARE, R.drawable.ic_check_circle_green_24dp);
        valueImageMap.put(DeviceStats.NO_HARDWARE, R.drawable.ic_cancel_red_24dp);

        //add specified data from device stats
        switch (mDeviceInfoType)
        {
            case DEVICE_INFO:
                addRowsToCard(mDeviceStats.getDeviceInfoLabelMap(), null);
                break;
            case HARDWARE:
                addRowsToCard(mDeviceStats.getHardwareLabelMap(), valueImageMap);
                break;
            case CIPHERS:
                boolean isCollapseContent = true;
                addRowsToCardSingle("Default Protocols", null, new String[] {TextUtils.join(", ",mDeviceStats.getProtocolsDefault())}, isCollapseContent);
                addRowsToCardSingle("Default Cipher Suites", null, mDeviceStats.getCipherSuitesDefault(), isCollapseContent);
                addRowsToCardSingle("Supported Protocols", null, new String[] {TextUtils.join(", ",mDeviceStats.getProtocolsSupport())}, isCollapseContent);
                addRowsToCardSingle("Supported Cipher Suites", null, mDeviceStats.getCipherSuitesSupport(), isCollapseContent);
                break;
            case ALGORITHMS:
                addRowsToCardSingle("Algorithms", null, mDeviceStats.getAlgorithmsList(), false);
                break;
            case ALGORITHMS_CRYPTO:
                addRowsToCardSingle("Algorithm Filter: AES", "Provider: ", mDeviceStats.getCryptoAlgorithmsAesMap(), true);
                addRowsToCardSingle("Algorithm Filter: EC", "Provider: ", mDeviceStats.getCryptoAlgorithmsAesMap(), true);
                break;
            default:

        }
    }

    private void addRowsToCard(LinkedHashMap<String, String> labelMap, Map<String, Integer> valueImageMap) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        FrameLayout cardView = (FrameLayout) inflater.inflate(R.layout.element_card_view, null, false);
        LinearLayout cardBodyLayout = (LinearLayout) cardView.findViewById(R.id.fragment_stats_card_body_layout);
        for (Map.Entry<String, String> entry : labelMap.entrySet())
        {
            View rowView = inflater.inflate(R.layout.element_card_double_column_row, null, false);
            TextView textViewLabel = (TextView) rowView.findViewById(R.id.element_card_row_label);
            ImageView imageViewValue = (ImageView) rowView.findViewById(R.id.element_card_row_value_image);
            TextView textViewValue = (TextView) rowView.findViewById(R.id.element_card_row_value);
            textViewLabel.setText(entry.getKey());
            if (valueImageMap!=null) {
                imageViewValue.setImageDrawable(getResources()
                        .getDrawable(valueImageMap.get(entry.getValue())));
                setImageColumnVisibility(imageViewValue, textViewValue, true);
            } else {
                textViewValue.setText(entry.getValue());
                setImageColumnVisibility(imageViewValue, textViewValue, false);
            }

            cardBodyLayout.addView(rowView);
        }
        mCardParentLayout.addView(cardView);
    }

    private void setImageColumnVisibility(ImageView imageViewValue, TextView textViewValue,
                                          boolean isVisible) {
        imageViewValue.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        textViewValue.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }

    private void addRowsToCardSingle(String title, String subTitle, Map<String,
            List<String>> valuesMap, boolean isCollapseContent) {
        for (Map.Entry<String, List<String>> entry : valuesMap.entrySet())
        {
            addRowsToCardSingle(title, subTitle.concat(entry.getKey()), entry.getValue(), isCollapseContent);
        }
    }

    private void addRowsToCardSingle(String title, String subTitle, List<String> valuesList, boolean isCollapseContent) {
        addRowsToCardSingle(title, subTitle, valuesList.toArray(new String[0]), isCollapseContent);
    }

    private void addRowsToCardSingle(String title, String subTitle, String[] valuesList, boolean isCollapseContent) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        FrameLayout cardView = (FrameLayout) inflater.inflate(R.layout.element_card_view, null, false);
        final LinearLayout cardBodyLayout = (LinearLayout) cardView.findViewById(R.id.fragment_stats_card_body_layout);
        //add title if required
        if (title!=null)
        {
            LinearLayout headerParentLayout = (LinearLayout) cardView.findViewById(R.id.fragment_stats_header_parent_layout);
            TextView textViewTitle = (TextView) cardView.findViewById(R.id.fragment_stats_header_text);
            textViewTitle.setText(title);
            if (subTitle!=null)
            {
                TextView textViewSubTitle = (TextView) cardView.findViewById(R.id.fragment_stats_header_subtitle_text);
                textViewSubTitle.setText(subTitle);
                textViewSubTitle.setVisibility(View.VISIBLE);
            }
            if (isCollapseContent)
            {
                cardBodyLayout.setVisibility(View.GONE);
                headerParentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cardBodyLayout.getVisibility()==View.GONE) {
                            cardBodyLayout.setVisibility(View.VISIBLE);
                        } else {
                            cardBodyLayout.setVisibility(View.GONE);
                        }
                    }
                });
            }
            headerParentLayout.setVisibility(View.VISIBLE);
        }
        for (String value : valuesList)
        {
            View rowView = inflater.inflate(R.layout.element_card_single_column_row, null, false);
            TextView textViewValue = (TextView) rowView.findViewById(R.id.element_card_row_value);
            textViewValue.setText(value);
            cardBodyLayout.addView(rowView);
        }
        mCardParentLayout.addView(cardView);
    }

    /*public class StatsCardwithList extends CardWithList {

        private final Context mContext;
        private int mHeaderImageId;
        private final String mHeaderText;
        private Map<String, String> mKeyValueList;

        public StatsCardwithList(Context context, @DrawableRes int headerImageId, String headerText, Map<String, String> keyValueList) {
            super(context);

            mContext = context;
            mHeaderImageId = headerImageId;
            mHeaderText = headerText;
            mKeyValueList = keyValueList;
        }

        @Override
        protected CardHeader initCardHeader() {
            //Add Header
            CardHeader header = new CardHeader(getContext(), R.layout.element_card_inner_header) {
                @Override
                public void setupInnerViewElements(ViewGroup parent, View view) {
                    super.setupInnerViewElements(parent, view);
                    TextView title = (TextView) view.findViewById(R.id.element_card_inner_header_text);
                    ImageView image = (ImageView) view.findViewById(R.id.element_card_inner_header_image);
                    if (title != null) {
                        title.setText(mHeaderText);
                    }
                    if (image != null) {
                        image.setImageDrawable(getResources().getDrawable(mHeaderImageId));
                    }
                }
            };
            header.setTitle(mHeaderText);
            return header;
        }

        @Override
        protected void initCard() {

        }

        @Override
        protected List<ListObject> initChildren() {
            //Init the list
            List<ListObject> objectsList = new ArrayList<ListObject>();
            StatObject statObject;
            for (Map.Entry<String, String> entry : mKeyValueList.entrySet())
            {
                statObject = new StatObject(this, entry.getKey(), entry.getValue());
                objectsList.add(statObject);
            }
            return objectsList;
        }

        @Override
        public View setupChildView(int childPosition, ListObject object, View convertView, ViewGroup parent) {
            //Setup the ui elements inside the item
            TextView textViewLabel = (TextView) convertView.findViewById(R.id.element_card_inner_row_label);
            TextView textViewValue = (TextView) convertView.findViewById(R.id.element_card_inner_row_value);

            //Retrieve the values from the object
            StatObject stockObject = (StatObject) object;
            textViewLabel.setText(stockObject.mLabel);
            textViewValue.setText(stockObject.mValue);

            return convertView;
        }

        @Override
        public int getChildLayoutId() {
            return R.layout.element_card_inner_row;
        }

        public class StatObject extends DefaultListObject {

            private final String mLabel;
            private final String mValue;

            public StatObject(Card parentCard, String label, String value) {
                super(parentCard);
                mLabel = label;
                mValue = value;
            }
        }
    }*/
}
