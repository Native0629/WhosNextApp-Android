package com.app.whosnextapp.videos;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;

import com.app.whosnextapp.R;
import com.app.whosnextapp.notification.NotificationActivity;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareToActivity extends BaseAppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;
    @BindView(R.id.tb_category)
    TabLayout tb_category;
    @BindView(R.id.vp_category)
    ViewPager vp_category;

    ShareAdapter shareAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_to);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        toolbar.setBackgroundResource(R.drawable.header_bg);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar_title.setText(getResources().getString(R.string.text_share_to));
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(this);
        setViewPager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        menu.findItem(R.id.header_bell).setVisible(true);
        final MenuItem menuItem = menu.findItem(R.id.header_bell);
        View actionView = MenuItemCompat.getActionView(menuItem);
        // tv_noitfication_count = actionView.findViewById(R.id.tv_noitfication_count);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.header_bell) {
            startActivity(new Intent(this, NotificationActivity.class));
        }
        return false;
    }

    public void setViewPager() {
        shareAdapter = new ShareAdapter(getSupportFragmentManager());
        shareAdapter.addFragment(FollowersFragment.newInstance(), getString(R.string.followers));
        shareAdapter.addFragment(VideoMessageFragment.newInstance(), getString(R.string.text_message));
        vp_category.setAdapter(shareAdapter);
        tb_category.setupWithViewPager(vp_category);
        vp_category.setOffscreenPageLimit(shareAdapter.getCount() - 1);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
