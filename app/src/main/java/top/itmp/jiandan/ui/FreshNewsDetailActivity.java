package top.itmp.jiandan.ui;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import top.itmp.jiandan.R;
import top.itmp.jiandan.base.BaseActivity;
import top.itmp.jiandan.model.FreshNews;
import top.itmp.jiandan.ui.fragment.FreshNewsDetailFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;

public class FreshNewsDetailActivity extends BaseActivity {

    @Bind(R.id.vp)
    ViewPager viewPager;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresh_news_detail);
        initView();
        initData();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        final Drawable upArrow;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.abc_ic_ab_back_mtrl_am_alpha, getTheme());
            // ContextCompat.getDrawable()  is ok
        } else {
            upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha, getTheme());
        }

        // parse the UpArrow Color
        upArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        mToolbar.setNavigationIcon(upArrow);
    }

    @Override
    protected void initData() {
        ArrayList<FreshNews> FreshNews = (ArrayList<FreshNews>) getIntent().getSerializableExtra
                (DATA_FRESH_NEWS);
        int position = getIntent().getIntExtra(DATA_POSITION, 0);
        viewPager.setAdapter(new FreshNewsDetailAdapter(getSupportFragmentManager(), FreshNews));
        viewPager.setCurrentItem(position);
    }


    private class FreshNewsDetailAdapter extends FragmentPagerAdapter {

        private ArrayList<FreshNews> freshNewses;

        public FreshNewsDetailAdapter(FragmentManager fm, ArrayList<FreshNews> freshNewses) {
            super(fm);
            this.freshNewses = freshNewses;
        }

        @Override
        public Fragment getItem(int position) {
            return FreshNewsDetailFragment.getInstance(freshNewses.get(position));
        }

        @Override
        public int getCount() {
            return freshNewses.size();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
