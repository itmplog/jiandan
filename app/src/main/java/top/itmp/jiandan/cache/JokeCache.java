package top.itmp.jiandan.cache;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import top.itmp.jiandan.base.JDApplication;
import top.itmp.jiandan.model.Joke;
import top.itmp.jiandan.net.JSONParser;

import java.util.ArrayList;

import de.greenrobot.dao.query.QueryBuilder;
import top.itmp.greendao.JokeCacheDao;


public class JokeCache extends BaseCache {

    private static JokeCache instance;
    private static JokeCacheDao mJokeCacheDao;

    private JokeCache() {
    }

    public static JokeCache getInstance(Context context) {

        if (instance == null) {

            synchronized (JokeCache.class) {
                if (instance == null) {
                    instance = new JokeCache();
                }
            }

            mDaoSession = JDApplication.getDaoSession();
            mJokeCacheDao = mDaoSession.getJokeCacheDao();
        }
        return instance;
    }

    public void clearAllCache() {
        mJokeCacheDao.deleteAll();
    }


    @Override
    public ArrayList<Joke> getCacheByPage(int page) {
        QueryBuilder<top.itmp.greendao.JokeCache> query = mJokeCacheDao.queryBuilder().where(JokeCacheDao.Properties.Page.eq("" + page));

        if (query.list().size() > 0) {
            return (ArrayList<Joke>) JSONParser.toObject(query.list().get(0).getResult(),
                    new TypeToken<ArrayList<Joke>>() {
                    }.getType());
        } else {
            return new ArrayList<>();
        }

    }

    @Override
    public void addResultCache(String result, int page) {
        top.itmp.greendao.JokeCache jokeCache = new top.itmp.greendao.JokeCache();
        jokeCache.setResult(result);
        jokeCache.setPage(page);
        jokeCache.setTime(System.currentTimeMillis());

        mJokeCacheDao.insert(jokeCache);
    }

}
