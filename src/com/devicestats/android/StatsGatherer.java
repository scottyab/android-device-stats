package com.devicestats.android;

import java.security.Provider;
import java.security.Provider.Service;
import java.security.SecureRandom;
import java.security.Security;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class StatsGatherer {

	private static final String BR = "\n";
	private static final Object SEPERATOR = ":\t";
	private static final Object HEADING_SYM = " *** ";
	private Activity context;
	private StringBuilder builder;

	public StatsGatherer(Activity context) {
		this.context = context;
		builder = new StringBuilder();
		getStats();
	}

	private void getStats() {

		recordDeviceInfo();
		recordDeviceId();
		recordHardwareFeatures();
		recordScreenInfo();
		recordSupportedHTTPSCipherSuites();
		recordAlgorithms();
		recordCryptoAlgorithms();

	}

	private void recordDeviceId() {
		printHeading("Device Id info");
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		// might be a wifi only tablet
		if (telephonyManager != null) {
			printLine("imei-meid", telephonyManager.getDeviceId());
		}

		// We're using the Reflection API because Build.SERIAL is only available
		// since API Level 9 (Gingerbread, Android 2.3).
		try {
			printLine("serial-number", (String) Build.class.getField("SERIAL")
					.get(null));
		} catch (Exception ignored) {
		}

		printLine("secure-id", Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID));

	}

	private void recordHardwareFeatures() {
		printHeading("Hardware features");

		PackageManager pm = context.getPackageManager();
		printLine("Has NFC", pm.hasSystemFeature("android.hardware.nfc"));
		printLine("Has bluetooth",
				pm.hasSystemFeature("android.hardware.bluetooth"));

		printLine("Has accelerometer",
				pm.hasSystemFeature("android.hardware.sensor.accelerometer"));
		printLine("Has barometer",
				pm.hasSystemFeature("android.hardware.sensor.barometer"));
		printLine("Has compass",
				pm.hasSystemFeature("android.hardware.sensor.compass"));
		printLine("Has gyroscope",
				pm.hasSystemFeature("android.hardware.sensor.gyroscope"));

		printLine("Has camera",
				pm.hasSystemFeature(PackageManager.FEATURE_CAMERA));
		printLine("Has flash",
				pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH));
		printLine("Has front camera",
				pm.hasSystemFeature("android.hardware.camera.front"));

		printLine("Has GPS location",
				pm.hasSystemFeature("android.hardware.location.gps"));
		printLine("Has network location",
				pm.hasSystemFeature("android.hardware.location.network"));

	}

	private void recordScreenInfo() {
		printHeading("Screen Info");
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		final int density = metrics.densityDpi;
		final float xdpi = metrics.xdpi;
		final float ydpi = metrics.ydpi;
		final int width = metrics.widthPixels;
		final int height = metrics.heightPixels;

		final float widthInches = width / xdpi;
		final float heightInches = height / ydpi;
		final double diagInches = Math.sqrt(widthInches * widthInches
				+ heightInches * heightInches);

		StringBuilder infoStr = new StringBuilder(density + " dpi");
		switch (density) {
		case DisplayMetrics.DENSITY_LOW:
			infoStr.append("(low)");
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			infoStr.append("(medium)");
			break;
		case DisplayMetrics.DENSITY_HIGH:
			infoStr.append("(high)");
			break;
		case 320:
			// Uses 320 as DisplayMetrics.DENSITY_XHIGH is API 9
			infoStr.append("(xhigh)");
			break;
		case 480:
			infoStr.append("(xxhigh)");
			break;
		case 640:
			infoStr.append("(xxxhigh)");
			break;
		default:
			infoStr.append("(unknown)");
		}
		printLine("Screen density", infoStr.toString());

		printLine("Actual dpi values",
				String.format("(x,y): (%.2f,%.2f) dpi", xdpi, ydpi));

		printLine("Averaged dpi: ", Math.round((xdpi + ydpi) / 2));

		printLine(
				"Screen size",
				String.format("%s (%.2f\")",
						context.getString(R.string.screen_size), diagInches));

		printLine("Screen dimensions", String.format("(%dx%d)", width, height));
	}

	private void recordDeviceInfo() {
		printHeading("Device info");
		printLine("Manufacturer", android.os.Build.MANUFACTURER);
		printLine("Model", android.os.Build.MODEL);
		// printLine("Product", Build.PRODUCT);
		printLine("API Level", android.os.Build.VERSION.SDK_INT);
		printLine("Release", android.os.Build.VERSION.RELEASE);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				ISO8601_FULL_DATE_STRING);
		printLine("System Time",
				dateFormat.format(new Date(System.currentTimeMillis())));
		printLine("TimeZone", TimeZone.getDefault().getDisplayName());
	}

	public static final String ISO8601_FULL_DATE_STRING = "yyyy-MM-dd HH:mm:ss";

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
				SSLParameters defaultParams = sslContext
						.getDefaultSSLParameters();
				printLine("Default Protocols",
						printArray(defaultParams.getProtocols(), false));
				printLine("Default CipherSuites",
						printArray(defaultParams.getCipherSuites(), true));

				SSLParameters supportedParams = sslContext
						.getSupportedSSLParameters();
				printLine("Supported Protocols",
						printArray(supportedParams.getProtocols(), false));
				printLine("Supported CipherSuites",
						printArray(supportedParams.getCipherSuites(), true));
			} else {
				printLine("Limited data pre API9", ":(");
				printLine("Protocol", sslContext.getProtocol());
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
		listAlg("AES");
		listAlg("EC");

	}

	private void listAlg(String algFilter) {
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
			}
		}
	}

	private void recordAlgorithms() {
		Set<String> algs = Security.getAlgorithms("cipher");
		printHeading("Ciphers");
		for (String alg : algs) {
			printLine(alg, "");
		}
	}
}
