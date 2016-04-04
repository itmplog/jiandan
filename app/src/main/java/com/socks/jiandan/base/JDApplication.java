package com.socks.jiandan.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.socks.jiandan.BuildConfig;
import com.socks.jiandan.R;
import com.socks.jiandan.cache.BaseCache;
import com.socks.jiandan.utils.StrictModeUtil;
import com.socks.jiandan.utils.logger.LogLevel;
import com.socks.jiandan.utils.logger.Logger;
import com.socks.jiandan.view.imageloader.ImageLoadProxy;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import okhttp3.OkHttpClient;
import top.itmp.greendao.DaoMaster;
import top.itmp.greendao.DaoSession;

public class JDApplication extends Application {

    public static int COLOR_OF_DIALOG = R.color.primary;
    public static int COLOR_OF_DIALOG_CONTENT = Color.WHITE;

    private static Context mContext;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        StrictModeUtil.init(); //StrictMode 线程监控， VM监控
        super.onCreate();
        refWatcher = LeakCanary.install(this); // init LeakCanary
        mContext = this;
        ImageLoadProxy.initImageLoader(this); //universalImageLoader init

        if (BuildConfig.DEBUG) {
            Logger.init().setMethodCount(1).setLogLevel(LogLevel.FULL); //Log init
            //hideThreadInfo().setMethodCount(1).setLogLevel(LogLevel.FULL);
        }

        Stetho.initialize( //Stetho init
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();


    }

    public static Context getContext() {
        return mContext;
    }

    public static RefWatcher getRefWatcher(Context context) {
        JDApplication application = (JDApplication) context.getApplicationContext();
        return application.refWatcher;
    }


    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, BaseCache.DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

}