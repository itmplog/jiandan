package com.socks.jiandan.cache;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import top.itmp.greendao.VideoCacheDao;
import com.socks.jiandan.base.JDApplication;
import com.socks.jiandan.model.Video;
import com.socks.jiandan.net.JSONParser;

import java.util.ArrayList;

import de.greenrobot.dao.query.QueryBuilder;

public class VideoCache extends BaseCache {

    private static VideoCache instance;
    private static VideoCacheDao mVideoCacheDao;

    private VideoCache() {
    }

    public static VideoCache getInstance(Context context) {

        if (instance == null) {

            synchronized (VideoCache.class) {
                if (instance == null) {
                    instance = new VideoCache();
                }
            }

            mDaoSession = JDApplication.getDaoSession();
            mVideoCacheDao = mDaoSession.getVideoCacheDao();
        }
        return instance;
    }

    public void clearAllCache() {
        mVideoCacheDao.deleteAll();
    }

    @Override
    public ArrayList<Video> getCacheByPage(int page) {

        QueryBuilder<top.itmp.greendao.VideoCache> query = mVideoCacheDao.queryBuilder().where(VideoCacheDao.Properties.Page.eq("" + page));
        if (query.list().size() > 0) {
            return (ArrayList<Video>) JSONParser.toObject(query.list().get(0).getResult(),
                    new TypeToken<ArrayList<Video>>() {
                    }.getType());
        } else {
            return new ArrayList<>();
        }

    }

    @Override
    public void addResultCache(String result, int page) {
        top.itmp.greendao.VideoCache jokeCache = new top.itmp.greendao.VideoCache();
        jokeCache.setResult(result);
        jokeCache.setPage(page);
        jokeCache.setTime(System.currentTimeMillis());
        mVideoCacheDao.insert(jokeCache);
    }

}
