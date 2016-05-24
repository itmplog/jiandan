package top.itmp.jiandan.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import top.itmp.jiandan.R;
import top.itmp.jiandan.base.BaseActivity;

import butterknife.ButterKnife;
    import butterknife.BindView;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.title_activity_setting);
        mToolbar.setNavigationIcon(R.drawable.ic_actionbar_back);
    }

    @Override
    protected void initData() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}