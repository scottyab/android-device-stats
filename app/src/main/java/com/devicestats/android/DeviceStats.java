package com.devicestats.android;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 1andbarb on 12/01/2016.
 */
public class DeviceStats implements Parcelable {

    public static final String HAS_HARDWARE = "Yes";
    public static final String NO_HARDWARE = "No";

    private String date;
    private String timeZone;

    private String manufacturer;
    private String model;
    private String product;
    private String apiLevel;
    private String release;

    private String brand;
    private String imei;
    private String serialNumber;
    private String secureId;

    private boolean hasNFC;
    private boolean hasBluetooth;
    private boolean hasAccel;
    private boolean hasBarom;
    private boolean hasCompass;
    private boolean hasGyro;
    private boolean hasCamera;
    private boolean hasFlash;
    private boolean hasCameraFront;
    private boolean hasGps;
    private boolean hasNetworkLoc;

    private LinkedHashMap<String, String> deviceInfoLabelMap;
    private LinkedHashMap<String, String> hardwareLabelMap;
    private List<String> ciphersList;
    private List<String> sslCiphersList;
    private List<String> sslCiphersProtocolsList;
    private List<String> protocolsList;
    private List<String> algorithmsList;
    private List<String> providerList;
    private String[] protocolsDefault;
    private String[] cipherSuitesDefault;
    private String[] protocolsSupport;
    private String[] cipherSuitesSupport;
    private Map<String, List<String>> cryptoAlgorithmsAesMap;
    private Map<String, List<String>> cryptoAlgorithmsEcMap;
    private int density;
    private int widthDpi;
    private int heightDpi;
    private int width;
    private int height;
    private String sizeQualifier;
    private String dpiQualifier;
    private String screenClass;

    public interface StatsLabels
    {
        String DATE_LABEL = "TIME";
        String TIME_ZONE_LABEL = "TIMEZONE";
        String MANUFACTURER_LABEL = "MANUFACTURER";
        String MODEL_LABEL = "MODEL";
        String API_LEVEL_LABEL = "API LEVEL";
        String RELEASE_LABEL = "RELEASE";
        String BRAND_LABEL = "BRAND";
        String IEMI_LABEL = "IEMI";
        String SERIAL_LABEL = "SERIAL NO";
        String SECURE_ID_LABEL = "SECURE ID";

        String IS_NFC_LABEL = "NFC";
        String IS_BLUETOOTH_LABEL = "BLUETOOTH";
        String IS_ACCELEROMETER_LABEL = "ACCELEROMETER";
        String IS_BAROMETER_LABEL = "BAROMETER";
        String IS_COMPASS_LABEL = "COMPASS";
        String IS_GYROSCOPE_LABEL = "GYROSCOPE";
        String IS_CAMERA_LABEL = "CAMERA";
        String IS_FLASH_LABEL = "FLASH";
        String IS_CAMERA_FRONT_LABEL = "CAMERA FRONT";
        String IS_GPS_LABEL = "GPS";
        String IS_NETWORK_LOCATION_LABEL = "NETWORK LOCATION";
    }

    // convenience methods to set data

    public void setScreenInfo(int density, int widthDpi, int heightDpi, int width, int height,
                              String sizeQualifier, String dpiQualifier, String screenClass) {

        this.density = density;
        this.widthDpi = widthDpi;
        this.heightDpi = heightDpi;
        this.width = width;
        this.height = height;
        this.sizeQualifier = sizeQualifier;
        this.dpiQualifier = dpiQualifier;
        this.screenClass = screenClass;
    }

    public void setDeviceInfo(String manufacturer, String model, String product, String apiLevel,
                              String release, String date, String displayName) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.product = product;
        this.apiLevel = apiLevel;
        this.release = release;
        this.date = date;
    }

    public void setDeviceId(String serial, String secureId) {
        this.serialNumber = serial;
        this.secureId = secureId;
    }

    public void setHardwareInfo(boolean hasNFC, boolean hasBluetooth, boolean hasAccel,
             boolean hasBarom, boolean hasCompass, boolean hasGyro, boolean hasCamera,
             boolean hasFlash, boolean hasCameraFront, boolean hasGps, boolean hasNetworkLoc) {

        this.hasNFC = hasNFC;
        this.hasBluetooth = hasBluetooth;
        this.hasAccel = hasAccel;
        this.hasBarom = hasBarom;
        this.hasCompass = hasCompass;
        this.hasGyro = hasGyro;
        this.hasCamera = hasCamera;
        this.hasFlash = hasFlash;
        this.hasCameraFront = hasCameraFront;
        this.hasGps = hasGps;
        this.hasNetworkLoc = hasNetworkLoc;
    }

    public void createDeviceInfoLabelMap()
    {
        deviceInfoLabelMap = new LinkedHashMap<String, String>();
        deviceInfoLabelMap.put(StatsLabels.DATE_LABEL, date);
        deviceInfoLabelMap.put(StatsLabels.TIME_ZONE_LABEL, timeZone);

        deviceInfoLabelMap.put(StatsLabels.MANUFACTURER_LABEL, manufacturer);
        deviceInfoLabelMap.put(StatsLabels.MODEL_LABEL, model);
        deviceInfoLabelMap.put(StatsLabels.API_LEVEL_LABEL, apiLevel);
        deviceInfoLabelMap.put(StatsLabels.RELEASE_LABEL, release);
        deviceInfoLabelMap.put(StatsLabels.BRAND_LABEL, brand);

        deviceInfoLabelMap.put(StatsLabels.SERIAL_LABEL, serialNumber);
        deviceInfoLabelMap.put(StatsLabels.SECURE_ID_LABEL, secureId);

        setDeviceInfoLabelMap(deviceInfoLabelMap);
    }

    public void createHardwareInfoLabelMap()
    {
        hardwareLabelMap = new LinkedHashMap<String, String>();
        hardwareLabelMap.put(StatsLabels.IS_NFC_LABEL, hasNFC ? HAS_HARDWARE : NO_HARDWARE);
        hardwareLabelMap.put(StatsLabels.IS_BLUETOOTH_LABEL, hasBluetooth ? HAS_HARDWARE : NO_HARDWARE);
        hardwareLabelMap.put(StatsLabels.IS_ACCELEROMETER_LABEL, hasAccel ? HAS_HARDWARE : NO_HARDWARE);
        hardwareLabelMap.put(StatsLabels.IS_BAROMETER_LABEL, hasBluetooth ? HAS_HARDWARE : NO_HARDWARE);
        hardwareLabelMap.put(StatsLabels.IS_COMPASS_LABEL, hasBluetooth ? HAS_HARDWARE : NO_HARDWARE);
        hardwareLabelMap.put(StatsLabels.IS_GYROSCOPE_LABEL, hasBluetooth ? HAS_HARDWARE : NO_HARDWARE);
        hardwareLabelMap.put(StatsLabels.IS_CAMERA_LABEL, hasBluetooth ? HAS_HARDWARE : NO_HARDWARE);
        hardwareLabelMap.put(StatsLabels.IS_FLASH_LABEL, hasBluetooth ? HAS_HARDWARE : NO_HARDWARE);
        hardwareLabelMap.put(StatsLabels.IS_CAMERA_FRONT_LABEL, hasBluetooth ? HAS_HARDWARE : NO_HARDWARE);
        hardwareLabelMap.put(StatsLabels.IS_GPS_LABEL, hasBluetooth ? HAS_HARDWARE : NO_HARDWARE);
        hardwareLabelMap.put(StatsLabels.IS_NETWORK_LOCATION_LABEL, hasBluetooth ? HAS_HARDWARE : NO_HARDWARE);
        setHardwareLabelMap(hardwareLabelMap);
    }

    public void setSslCipherInfo(String[] protocolsDefault, String[] cipherSuitesDefault,
                                 String[] protocolsSupport, String[] cipherSuitesSupport) {

        this.protocolsDefault = protocolsDefault;
        this.cipherSuitesDefault = cipherSuitesDefault;
        this.protocolsSupport = protocolsSupport;
        this.cipherSuitesSupport = cipherSuitesSupport;
    }

    public String[] getProtocolsDefault() {
        return protocolsDefault;
    }

    public String[] getCipherSuitesDefault() {
        return cipherSuitesDefault;
    }

    public String[] getProtocolsSupport() {
        return protocolsSupport;
    }

    public String[] getCipherSuitesSupport() {
        return cipherSuitesSupport;
    }

    public void setCryptoAlgorithmsAesMap(Map<String, List<String>> cryptoAlgorithmsAesMap) {

        this.cryptoAlgorithmsAesMap = cryptoAlgorithmsAesMap;
    }

    public void setCryptoAlgorithmsEcMap(Map<String, List<String>> cryptoAlgorithmsEcMap) {

        this.cryptoAlgorithmsEcMap = cryptoAlgorithmsEcMap;
    }

    // getters & setters

    public LinkedHashMap<String, String> getDeviceInfoLabelMap() {
        return deviceInfoLabelMap;
    }

    public void setDeviceInfoLabelMap(LinkedHashMap<String, String> deviceInfoLabelMap) {
        this.deviceInfoLabelMap = deviceInfoLabelMap;
    }

    public LinkedHashMap<String, String> getHardwareLabelMap() {
        return hardwareLabelMap;
    }

    public void setHardwareLabelMap(LinkedHashMap<String, String> hardwareLabelMap) {
        this.hardwareLabelMap = hardwareLabelMap;
    }

    public List<String> getAlgorithmsList() {
        return algorithmsList;
    }

    public void setAlgorithmsList(List<String> algorithmsList) {
        this.algorithmsList = algorithmsList;
    }

    public Map<String, List<String>> getCryptoAlgorithmsAesMap() {
        return cryptoAlgorithmsAesMap;
    }

    public Map<String, List<String>> getCryptoAlgorithmsEcMap() {
        return cryptoAlgorithmsEcMap;
    }

    public int getDensity() {
        return density;
    }

    public int getWidthDpi() {
        return widthDpi;
    }

    public int getHeightDpi() {
        return heightDpi;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getSizeQualifier() {
        return sizeQualifier;
    }

    public String getDpiQualifier() {
        return dpiQualifier;
    }

    public String getScreenClass() {
        return screenClass;
    }

    public String getModel() {
        return model;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.timeZone);
        dest.writeString(this.manufacturer);
        dest.writeString(this.model);
        dest.writeString(this.product);
        dest.writeString(this.apiLevel);
        dest.writeString(this.release);
        dest.writeString(this.brand);
        dest.writeString(this.serialNumber);
        dest.writeString(this.secureId);
        dest.writeByte(hasNFC ? (byte) 1 : (byte) 0);
        dest.writeByte(hasBluetooth ? (byte) 1 : (byte) 0);
        dest.writeByte(hasAccel ? (byte) 1 : (byte) 0);
        dest.writeByte(hasBarom ? (byte) 1 : (byte) 0);
        dest.writeByte(hasCompass ? (byte) 1 : (byte) 0);
        dest.writeByte(hasGyro ? (byte) 1 : (byte) 0);
        dest.writeByte(hasCamera ? (byte) 1 : (byte) 0);
        dest.writeByte(hasFlash ? (byte) 1 : (byte) 0);
        dest.writeByte(hasCameraFront ? (byte) 1 : (byte) 0);
        dest.writeByte(hasGps ? (byte) 1 : (byte) 0);
        dest.writeByte(hasNetworkLoc ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.deviceInfoLabelMap);
        dest.writeSerializable(this.hardwareLabelMap);
        dest.writeStringList(this.ciphersList);
        dest.writeStringList(this.sslCiphersList);
        dest.writeStringList(this.sslCiphersProtocolsList);
        dest.writeStringList(this.protocolsList);
        dest.writeStringList(this.algorithmsList);
        dest.writeStringList(this.providerList);
        dest.writeStringArray(this.protocolsDefault);
        dest.writeStringArray(this.cipherSuitesDefault);
        dest.writeStringArray(this.protocolsSupport);
        dest.writeStringArray(this.cipherSuitesSupport);
        //TODO - couldn't add this to parcelable config but still gets carried through? look at if issues
        /*dest.writeSerializable(this.cryptoAlgorithmsAesMap);
        dest.writeSerializable(this.cryptoAlgorithmsEcMap);*/
        dest.writeInt(this.density);
        dest.writeInt(this.widthDpi);
        dest.writeInt(this.heightDpi);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.sizeQualifier);
        dest.writeString(this.dpiQualifier);
        dest.writeString(this.screenClass);
    }

    public DeviceStats() {
    }

    protected DeviceStats(Parcel in) {
        this.date = in.readString();
        this.timeZone = in.readString();
        this.manufacturer = in.readString();
        this.model = in.readString();
        this.product = in.readString();
        this.apiLevel = in.readString();
        this.release = in.readString();
        this.brand = in.readString();
        this.serialNumber = in.readString();
        this.secureId = in.readString();
        this.hasNFC = in.readByte() != 0;
        this.hasBluetooth = in.readByte() != 0;
        this.hasAccel = in.readByte() != 0;
        this.hasBarom = in.readByte() != 0;
        this.hasCompass = in.readByte() != 0;
        this.hasGyro = in.readByte() != 0;
        this.hasCamera = in.readByte() != 0;
        this.hasFlash = in.readByte() != 0;
        this.hasCameraFront = in.readByte() != 0;
        this.hasGps = in.readByte() != 0;
        this.hasNetworkLoc = in.readByte() != 0;
        this.deviceInfoLabelMap = (LinkedHashMap<String, String>) in.readSerializable();
        this.hardwareLabelMap = (LinkedHashMap<String, String>) in.readSerializable();
        this.ciphersList = in.createStringArrayList();
        this.sslCiphersList = in.createStringArrayList();
        this.sslCiphersProtocolsList = in.createStringArrayList();
        this.protocolsList = in.createStringArrayList();
        this.algorithmsList = in.createStringArrayList();
        this.providerList = in.createStringArrayList();
        this.protocolsDefault = in.createStringArray();
        this.cipherSuitesDefault = in.createStringArray();
        this.protocolsSupport = in.createStringArray();
        this.cipherSuitesSupport = in.createStringArray();
        //TODO - couldn't add this to parcelable config but still gets carried through? look at if issues
        /*this.cryptoAlgorithmsAesMap = in.readParcelable(Map<String, List<String>>.class.getClassLoader());
        this.cryptoAlgorithmsEcMap = in.readParcelable(Map<String, List<String>>.class.getClassLoader());*/
        this.density = in.readInt();
        this.widthDpi = in.readInt();
        this.heightDpi = in.readInt();
        this.width = in.readInt();
        this.height = in.readInt();
        this.sizeQualifier = in.readString();
        this.dpiQualifier = in.readString();
        this.screenClass = in.readString();
    }

    public static final Parcelable.Creator<DeviceStats> CREATOR = new Parcelable.Creator<DeviceStats>() {
        public DeviceStats createFromParcel(Parcel source) {
            return new DeviceStats(source);
        }

        public DeviceStats[] newArray(int size) {
            return new DeviceStats[size];
        }
    };
}
