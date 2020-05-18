package com.app.whosnextapp.pictures;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.model.GetAllLikesByPostIdModel;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LikePostActivity extends BaseAppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;
    @BindView(R.id.rv_follower)
    RecyclerView rv_like;

    Globals globals;
    LikesAdapter likesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        globals = (Globals) getApplicationContext();
        setActionbar();
        if (getIntent() != null && getIntent().hasExtra(Constants.WN_POST_ID)) {
            doRequestForGetLikes();
        } else {
            doRequestForGetAdsLikes();
        }
    }

    public void setActionbar() {
        toolbar.setBackgroundResource(R.drawable.header_bg);
        setSupportActionBar(toolbar);
        toolbar_title.setText(R.string.text_likes);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.header_refresh) {
            setActionbar();
            doRequestForGetLikes();
        }
        return false;
    }

    public void setLikeAdapter(ArrayList<GetAllLikesByPostIdModel.CustomerList> posts) {
        if (likesAdapter == null) {
            likesAdapter = new LikesAdapter(this);
        }
        if (rv_like.getAdapter() == null) {
            rv_like.setHasFixedSize(true);
            rv_like.setLayoutManager(new LinearLayoutManager(this));
            rv_like.setAdapter(likesAdapter);
        }
        likesAdapter.doRefresh(posts);
    }

    public void doRequestForGetLikes() {
        String postId = getIntent().getStringExtra(Constants.WN_POST_ID);
        final String requestURL = String.format(getString(R.string.get_all_likeBy_post_ID), postId, "1");
        new PostRequest(this, null, requestURL, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                GetAllLikesByPostIdModel getAllLikesByPostIdModel = new Gson().fromJson(response, GetAllLikesByPostIdModel.class);
                if (getAllLikesByPostIdModel != null && getAllLikesByPostIdModel.getCustomerList() != null && !getAllLikesByPostIdModel.getCustomerList().isEmpty()) {
                    setLikeAdapter(getAllLikesByPostIdModel.getCustomerList());
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(LikePostActivity.this, msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    public void doRequestForGetAdsLikes() {
        String adId = getIntent().getStringExtra(Constants.WN_AD_ID);
        final String requestURL = String.format(getString(R.string.get_all_like_by_ad_id), adId);
        new PostRequest(this, null, requestURL, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                GetAllLikesByPostIdModel getAllLikesByPostIdModel = new Gson().fromJson(response, GetAllLikesByPostIdModel.class);
                if (getAllLikesByPostIdModel != null && getAllLikesByPostIdModel.getCustomerList() != null && !getAllLikesByPostIdModel.getCustomerList().isEmpty()) {
                    setLikeAdapter(getAllLikesByPostIdModel.getCustomerList());
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(LikePostActivity.this, msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
