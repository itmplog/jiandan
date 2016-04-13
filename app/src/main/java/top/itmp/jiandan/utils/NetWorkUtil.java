package top.itmp.jiandan.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import top.itmp.jiandan.base.JDApplication;

/**
 * Created by zhaokaiqiang on 15/4/22.
 */
public class NetWorkUtil {

	/**
	 * 判断当前网络是否已连接
	 *
	 * @return
	 */
	public static boolean isNetWorkConnected() {
		ConnectivityManager cm = (ConnectivityManager) JDApplication.getContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnected();
	}


	/**
	 * 判断当前的网络连接方式是否为WIFI
	 *
	 * @return
	 */
	public static boolean isWifiConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) JDApplication.getContext().getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return  activeNetworkInfo != null && activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
	}

}
