package top.itmp.jiandan.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import top.itmp.jiandan.BuildConfig;
import top.itmp.jiandan.R;
import top.itmp.jiandan.cache.BaseCache;
import top.itmp.jiandan.utils.StrictModeUtil;
import top.itmp.jiandan.utils.logger.LogLevel;
import top.itmp.jiandan.utils.logger.Logger;
import top.itmp.jiandan.view.imageloader.ImageLoadProxy;
import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.DisplayLeakService;
import com.squareup.leakcanary.ExcludedRefs;
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
    private static RequestQueue mRequestQueue;

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        StrictModeUtil.init(); //StrictMode 线程监控， VM监控
        super.onCreate();

        ExcludedRefs excludedRefs = AndroidExcludedRefs.createAppDefaults()
                //.instanceField("android.support.v7.widget.RecyclerView", "mContext")
                // inv
                .staticField("android.view.inputmethod.InputMethodManager", "sInstance")
                .instanceField("android.support.v7.widget.RecyclerView", "mContext")
                .instanceField("top.itmp.uidemo.ui.MainActivity", "instance")
                .reason("recyclerView leak")
                .build();
        refWatcher = LeakCanary.install(this, DisplayLeakService.class, excludedRefs); // init LeakCanary
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


    public static DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(mContext, BaseCache.DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public static RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }
}