package top.itmp.jiandan.utils;

import android.app.Activity;
import android.graphics.Point;

/**
 * Created by zhaokaiqiang on 15/4/9.
 */
public class ScreenSizeUtil {

	public static int getScreenWidth(Activity activity) {
		Point point = new Point();
		activity.getWindowManager().getDefaultDisplay().getSize(point);
		return point.x;
	}

	public static int getScreenHeight(Activity activity) {
		Point point = new Point();
		activity.getWindowManager().getDefaultDisplay().getSize(point);
		return point.y;
	}

}
