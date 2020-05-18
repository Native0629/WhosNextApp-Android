package com.app.whosnextapp.pictures;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.app.whosnextapp.R;
import com.app.whosnextapp.notification.NotificationActivity;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareToPictureActivity extends BaseAppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;
    @BindView(R.id.tb_pictures)
    TabLayout tb_pictures;
    @BindView(R.id.vp_pictures)
    ViewPager vp_pictures;

    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_all_post);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setToolbar();
        setViewPager();
    }

    private void setToolbar() {
        toolbar.setBackgroundResource(R.drawable.header_bg);
        setSupportActionBar(toolbar);
        toolbar_title.setText(getResources().getString(R.string.text_share_to));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        menu.findItem(R.id.header_bell).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.header_bell) {
            startActivity(new Intent(this, NotificationActivity.class));
        }
        return false;
    }

    private void setViewPager() {
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(FollowerPictureFragment.newInstance(), getString(R.string.followers));
        pagerAdapter.addFragment(MessagePictureFragment.newInstance(), getString(R.string.text_message));
        vp_pictures.setAdapter(pagerAdapter);
        tb_pictures.setupWithViewPager(vp_pictures);
        vp_pictures.setOffscreenPageLimit(pagerAdapter.getCount() - 1);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
