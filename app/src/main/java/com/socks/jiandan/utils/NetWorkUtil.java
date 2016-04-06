package com.socks.jiandan.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by zhaokaiqiang on 15/4/22.
 */
public class NetWorkUtil {

	/**
	 * 判断当前网络是否已连接
	 *
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		boolean result;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnected();
	}


	/**
	 * 判断当前的网络连接方式是否为WIFI
	 *
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return  activeNetworkInfo == null ? false : activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
	}

}
