package top.itmp.jiandan.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import top.itmp.jiandan.R;
import top.itmp.jiandan.base.ConstantString;
import top.itmp.jiandan.cache.FreshNewsCache;
import top.itmp.jiandan.callback.LoadFinishCallBack;
import top.itmp.jiandan.callback.LoadResultCallBack;
import top.itmp.jiandan.model.FreshNews;
import top.itmp.jiandan.net.JSONParser;
import top.itmp.jiandan.net.Request4FreshNews;
import top.itmp.jiandan.net.RequestManager;
import top.itmp.jiandan.ui.FreshNewsDetailActivity;
import top.itmp.jiandan.utils.NetWorkUtil;
import top.itmp.jiandan.utils.ShareUtil;
import top.itmp.jiandan.utils.ShowToast;
import top.itmp.jiandan.utils.String2TimeUtil;
import top.itmp.jiandan.view.imageloader.ImageLoadProxy;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FreshNewsAdapter extends RecyclerView.Adapter<FreshNewsAdapter.ViewHolder> {

    private int page;
    private int lastPosition;
    private boolean isLargeMode;
    private Activity mActivity;
    private DisplayImageOptions options;
    private ArrayList<FreshNews> mFreshNews;
    private LoadFinishCallBack mLoadFinisCallBack;
    private LoadResultCallBack mLoadResultCallBack;

    public FreshNewsAdapter(Activity activity, LoadFinishCallBack loadFinisCallBack, LoadResultCallBack loadResultCallBack, boolean isLargeMode) {
        this.mActivity = activity;
        this.isLargeMode = isLargeMode;
        this.mLoadFinisCallBack = loadFinisCallBack;
        this.mLoadResultCallBack = loadResultCallBack;
        mFreshNews = new ArrayList<>();

        int loadingResource = isLargeMode ? R.drawable.ic_loading_large : R.drawable.ic_loading_small;
        options = ImageLoadProxy.getOptions4PictureList(loadingResource);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        lastPosition = sp.getInt("lastPosition", -1);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R
                    .anim.item_bottom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (isLargeMode) {
            holder.card.clearAnimation();
        } else {
            holder.ll_content.clearAnimation();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = isLargeMode ? R.layout.item_fresh_news : R.layout.item_fresh_news_small;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final FreshNews freshNews = mFreshNews.get(position);

        ImageLoadProxy.displayImage(freshNews.getCustomFields().getThumb_m(), holder.img, options);
        holder.tv_title.setText(freshNews.getTitle());
        holder.tv_info.setText(freshNews.getAuthor().getName() + "@" + freshNews.getTags()
                .getTitle());
        //holder.tv_views.setText("浏览" + freshNews.getCustomFields().getViews() + "次");
        holder.tv_views.setText(freshNews.getComment_count() + "评论");
        holder.tv_date.setText(String2TimeUtil
                .dateString2GoodExperienceFormat(freshNews.getDate()));

        if (isLargeMode) {

            holder.tv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareUtil.shareText(mActivity, freshNews.getTitle() + " " + freshNews.getUrl());
                }
            });

            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toDetailActivity(position);
                }
            });

            setAnimation(holder.card, position);
        } else {
            holder.ll_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toDetailActivity(position);
                }
            });
            setAnimation(holder.ll_content, position);
        }

    }

    private void toDetailActivity(int position) {
        Intent intent = new Intent(mActivity, FreshNewsDetailActivity.class);
        intent.putExtra(FreshNewsDetailActivity.DATA_FRESH_NEWS, mFreshNews);
        intent.putExtra(FreshNewsDetailActivity.DATA_POSITION, position);
        mActivity.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mFreshNews.size();
    }

    public void loadFirst() {
        page = 1;
        loadDataByNetworkType();
    }

    public void loadNextPage() {
        page++;
        loadDataByNetworkType();
    }

    private void loadDataByNetworkType() {

        if (NetWorkUtil.isNetWorkConnected()) {
            RequestManager.addRequest(new Request4FreshNews(FreshNews.getUrlFreshNews(page),
                    new Response.Listener<ArrayList<FreshNews>>() {
                        @Override
                        public void onResponse(ArrayList<FreshNews> response) {

                            mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
                            mLoadFinisCallBack.loadFinish(null);

                            if (page == 1) {
                                mFreshNews.clear();
                                FreshNewsCache.getInstance().clearAllCache();
                                lastPosition = -1;
                            }

                            mFreshNews.addAll(response);
                            notifyDataSetChanged();

                            FreshNewsCache.getInstance().addResultCache(JSONParser.toString(response),
                                    page);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mLoadResultCallBack.onError(LoadResultCallBack.ERROR_NET, error.getMessage());
                    mLoadFinisCallBack.loadFinish(null);
                }
            }), mActivity);
        } else {
            mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
            mLoadFinisCallBack.loadFinish(null);

            if (page == 1) {
                mFreshNews.clear();
                ShowToast.Short(ConstantString.LOAD_NO_NETWORK);
            }

            mFreshNews.addAll(FreshNewsCache.getInstance().getCacheByPage(page));
            notifyDataSetChanged();
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_title)
        TextView tv_title;
        @Bind(R.id.tv_info)
        TextView tv_info;
        @Bind(R.id.tv_views)
        TextView tv_views;
        @Bind(R.id.tv_date)
        TextView tv_date;
        @Nullable
        @Bind(R.id.tv_share)
        TextView tv_share;
        @Bind(R.id.img)
        ImageView img;
        @Nullable
        @Bind(R.id.card)
        CardView card;
        @Nullable
        @Bind(R.id.ll_content)
        LinearLayout ll_content;

        public ViewHolder(View contentView) {
            super(contentView);
            ButterKnife.bind(this, contentView);
        }
    }

    public void setLastPosition(){
        PreferenceManager.getDefaultSharedPreferences(mActivity).edit().putInt("lastPosition", lastPosition)
                .commit();
    }

}