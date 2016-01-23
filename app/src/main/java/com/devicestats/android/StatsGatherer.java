package com.devicestats.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.devicestats.android.R;

import java.security.Provider;
import java.security.Provider.Service;
import java.security.SecureRandom;
import java.security.Security;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class StatsGatherer {

	private static final String BR = "\n";
	private static final Object SEPERATOR = ":\t";
	private static final Object HEADING_SYM = " *** ";
	private static final String ISO8601_FULL_DATE_STRING = "yyyy-MM-dd HH:mm:ss";

	private Activity context;
	private StringBuilder builder;
	private DeviceStats deviceStats;

	public StatsGatherer(Activity context) {
		this.context = context;
		builder = new StringBuilder();
		getStats();
	}

	private void getStats() {

		deviceStats = new DeviceStats();
		recordDeviceInfo();
		recordDeviceId();
		recordHardwareFeatures();
		recordScreenInfo();
		recordSupportedHTTPSCipherSuites();
		recordAlgorithms();
		recordCryptoAlgorithms();
		deviceStats.createDeviceInfoLabelMap();
		deviceStats.createHardwareInfoLabelMap();
	}

	public DeviceStats getDeviceStats() {
		return deviceStats;
	}

	private void recordDeviceId() {

		String deviceId = "", serial = "";
		printHeading("Device Id info");

		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		// might be a wifi only tablet
		if (telephonyManager != null) {
			deviceId = telephonyManager.getDeviceId();
			printLine("imei-meid", deviceId);
		}

		// We're using the Reflection API because Build.SERIAL is only available
		// since API Level 9 (Gingerbread, Android 2.3).
		try {
			serial = (String) Build.class.getField("SERIAL").get(null);
			printLine("serial-number", serial);
		} catch (Exception ignored) {
		}

		String secureId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		printLine("secure-id", secureId);

		deviceStats.setDeviceId(deviceId, serial, secureId);
	}

	private void recordHardwareFeatures() {
		printHeading("Hardware features");

		PackageManager pm = context.getPackageManager();
		boolean hasNfc = pm.hasSystemFeature("android.hardware.nfc");
		boolean hasBluetooth = pm.hasSystemFeature("android.hardware.bluetooth");
		boolean hasAccel = pm.hasSystemFeature("android.hardware.sensor.accelerometer");
		boolean hasBarom = pm.hasSystemFeature("android.hardware.sensor.barometer");
		boolean hasCompass = pm.hasSystemFeature("android.hardware.sensor.compass");
		boolean hasGyro = pm.hasSystemFeature("android.hardware.sensor.gyroscope");
		boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
		boolean hasFlash = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		boolean hasCameraFront = pm.hasSystemFeature("android.hardware.camera.front");
		boolean hasGps = pm.hasSystemFeature("android.hardware.location.gps");
		boolean hasNetworkLoc = pm.hasSystemFeature("android.hardware.location.network");

		deviceStats.setHardwareInfo(hasNfc, hasBluetooth, hasAccel, hasBarom, hasCompass,
				hasGyro, hasCamera, hasFlash, hasCameraFront, hasGps, hasNetworkLoc);

		printLine("Has NFC", hasNfc);
		printLine("Has bluetooth", hasBluetooth);
		printLine("Has accelerometer", hasAccel);
		printLine("Has barometer", hasBarom);
		printLine("Has compass", hasCompass);
		printLine("Has gyroscope", hasGyro);
		printLine("Has camera", hasCamera);
		printLine("Has flash", hasFlash);
		printLine("Has front camera", hasCameraFront);
		printLine("Has GPS location", hasGps);
		printLine("Has network location", hasNetworkLoc);

	}

	private void recordScreenInfo() {

		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		final int density = metrics.densityDpi;
		final int xdpi = (int) (metrics.widthPixels/metrics.density);
		final int ydpi = (int) (metrics.heightPixels/metrics.density);
		final int width = metrics.widthPixels;
		final int height = metrics.heightPixels;

		final float widthInches = width / xdpi;
		final float heightInches = height / ydpi;
		final double diagInches = Math.sqrt(widthInches * widthInches
				+ heightInches * heightInches);
		String sizeQualifier = context.getResources().getString(R.string.size_qualifier);
		String dpiQualifier = context.getResources().getString(R.string.dpi_qualifier);
		String screenClass = context.getResources().getString(R.string.screen_width_class);

		deviceStats.setScreenInfo(density, xdpi, ydpi, width, height, sizeQualifier,
				dpiQualifier, screenClass);

		//re-factored to use resource directories :-)
		printHeading("Screen Info");
		StringBuilder infoStr = new StringBuilder(density + " dpi");
		printLine("Screen density", infoStr.toString());
		printLine("Actual dpi values",
				String.format(context.getString(R.string.dpi_string), xdpi, ydpi));
		printLine("Averaged dpi: ", Math.round((xdpi + ydpi) / 2));
		printLine(
				"Screen size",
				String.format("%s (%.2f\")",
						context.getString(R.string.label_screen_size), diagInches));
		printLine("Screen dimensions", String.format("(%dx%d)", width, height));
	}

	private void recordDeviceInfo() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(ISO8601_FULL_DATE_STRING);
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		String product = Build.PRODUCT;
		String apiLevel = String.valueOf(Build.VERSION.SDK_INT);
		String release = Build.VERSION.RELEASE;
		String date = dateFormat.format(new Date(System.currentTimeMillis()));
		String displayName = TimeZone.getDefault().getDisplayName();
		deviceStats.setDeviceInfo(manufacturer, model, product, apiLevel,
									release, date, displayName);

		printHeading("Device info");
		printLine("Manufacturer", manufacturer);
		printLine("Model", model);
		//printLine("Product", product);
		printLine("API Level", apiLevel);
		printLine("Release", release);
		printLine("System Time", date);
		printLine("TimeZone", displayName);
	}

	public String printStatsToString() {
		return builder.toString();
	}

	private String printArray(String[] array, boolean newlinePerEntry) {
		if (array != null) {
			StringBuilder b = new StringBuilder();
			for (String value : array) {
				b.append(value);
				if (newlinePerEntry) {
					b.append(BR);
				} else {
					b.append(",");
				}
			}
			return b.toString();
		}
		return "";
	}

	private String printArray(List<String> array, boolean newlinePerEntry) {
		if (array != null) {
			StringBuilder b = new StringBuilder();
			for (String value : array) {
				b.append(value);
				if (newlinePerEntry) {
					b.append(BR);
				} else {
					b.append(",");
				}
			}
			return b.toString();
		}
		return "";
	}

	private void printLine(String label, int value) {
		printLine(label, value + "");
	}

	private void printLine(String label, boolean value) {
		printLine(label, value ? "true" : "false");
	}

	private void printLine(String label, String value) {
		builder.append(label);
		builder.append(SEPERATOR);
		builder.append(value);
		builder.append(BR);
	}

	private void printHeading(String label) {
		builder.append(BR);
		builder.append(HEADING_SYM);
		builder.append(label);
		builder.append(HEADING_SYM);
		builder.append(BR);
	}

	/**
	 * API 9+
	 * 
	 * @return
	 */
	@SuppressLint("NewApi")
	private void recordSupportedHTTPSCipherSuites() {
		printHeading("SSL CipherSuites");
		try {

			SSLContext sslContext = SSLContext.getInstance("TLS");
			/*
			 * KeyManagerFactory kmf = KeyManagerFactory
			 * .getInstance(KeyManagerFactory.getDefaultAlgorithm());
			 * kmf.init(null, null);
			 * 
			 * TrustManagerFactory tmf = TrustManagerFactory
			 * .getInstance(TrustManagerFactory.getDefaultAlgorithm());
			 * 
			 * sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new
			 * SecureRandom());
			 */
			sslContext.init(null, null, new SecureRandom());

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
				SSLParameters defaultParams = sslContext.getDefaultSSLParameters();
				String[] protocolsDefault = defaultParams.getProtocols();
				String[] cipherSuitesDefault = defaultParams.getCipherSuites();

				SSLParameters supportedParams = sslContext.getSupportedSSLParameters();
				String[] protocolsSupport = supportedParams.getProtocols();
				String[] cipherSuitesSupport = supportedParams.getCipherSuites();

				deviceStats.setSslCipherInfo(protocolsDefault, cipherSuitesDefault, protocolsSupport, cipherSuitesSupport);

				printLine("Default Protocols", printArray(protocolsDefault, false));
				printLine("Default CipherSuites", printArray(cipherSuitesDefault, true));
				printLine("Supported Protocols", printArray(protocolsSupport, false));
				printLine("Supported CipherSuites", printArray(cipherSuitesSupport, true));
			} else {
				printLine("Limited data pre API9", ":(");
				String protocolSslContext = sslContext.getProtocol();
				printLine("Protocol", protocolSslContext);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private X509TrustManager findX509TrustManager(TrustManagerFactory tmf) {
		TrustManager trustManagers[] = tmf.getTrustManagers();
		for (int i = 0; i < trustManagers.length; i++) {
			if (trustManagers[i] instanceof X509TrustManager) {
				return (X509TrustManager) trustManagers[i];
			}
		}
		return null;
	}

	private void recordCryptoAlgorithms() {
		printHeading("Crypto Algorithms");
		Map<String, List<String>> cryptoAlgorithmsAesMap = listAlg("AES");
		Map<String, List<String>> cryptoAlgorithmsEcMap = listAlg("EC");
		deviceStats.setCryptoAlgorithmsAesMap(cryptoAlgorithmsAesMap);
		deviceStats.setCryptoAlgorithmsEcMap(cryptoAlgorithmsEcMap);
	}

	private Map<String, List<String>> listAlg(String algFilter) {
		Map<String, List<String>> algorithmsMap = new LinkedHashMap<>();

		printLine("Algorithm Filter", algFilter);
		Provider[] providers = Security.getProviders();
		for (Provider p : providers) {

			String providerStr = String.format("%s/%s/%f\n", p.getName(),
					p.getInfo(), p.getVersion());
			Set<Service> services = p.getServices();
			List<String> algs = new ArrayList<String>();
			for (Service s : services) {
				boolean match = true;
				if (algFilter != null) {
					match = s.getAlgorithm().toLowerCase()
							.contains(algFilter.toLowerCase());
				}

				if (match) {

					String algStr = String.format("%s/%s/%s", s.getType(),
							s.getAlgorithm(), s.getClassName());
					algs.add(algStr);
				}
			}
			if (!algs.isEmpty()) {
				Collections.sort(algs);
				printLine("Provider", "");
				printLine(providerStr, printArray(algs, true));
				algorithmsMap.put(providerStr, algs);
			}
		}
		return algorithmsMap;
	}

	private void recordAlgorithms() {
		List<String> algorithmsList = new ArrayList<>();
		Set<String> algs = Security.getAlgorithms("cipher");
		printHeading("Ciphers");
		for (String alg : algs) {
			printLine(alg, "");
			algorithmsList.add(alg);
		}
		deviceStats.setAlgorithmsList(algorithmsList);
	}
}
