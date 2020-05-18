package com.app.whosnextapp.drawer;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.app.whosnextapp.R;
import com.app.whosnextapp.feedpost.TabFeedFragment;
import com.app.whosnextapp.feedpost.TabMediaFragment;
import com.app.whosnextapp.pictures.PagerAdapter;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Globals;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeaderAllPostActivity extends BaseAppCompatActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;
    @BindView(R.id.vp_pictures)
    ViewPager vp_pictures;
    @BindView(R.id.tb_pictures)
    TabLayout tb_pictures;

    PagerAdapter pagerAdapter;
    Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_all_post);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        globals = (Globals) getApplicationContext();
        setActionbar();
        setViewPager();
    }

    public void setActionbar() {
        toolbar.setBackgroundResource(R.drawable.header_bg);
        setSupportActionBar(toolbar);
        toolbar_title.setText(R.string.text_posts);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(this);
    }

    void setViewPager() {
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new TabMediaFragment(), getString(R.string.text_media));
        pagerAdapter.addFragment(new TabFeedFragment(), getString(R.string.feed));
        vp_pictures.setAdapter(pagerAdapter);
        tb_pictures.setupWithViewPager(vp_pictures);
        vp_pictures.setOffscreenPageLimit(pagerAdapter.getCount() - 1);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
