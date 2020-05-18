package com.app.whosnextapp.notification;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.model.DiscoverModel;
import com.app.whosnextapp.model.LikeUnlikePostModel;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowRequestActivity extends BaseAppCompatActivity implements View.OnClickListener, FollowRequestAdapter.OnCustomClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;
    @BindView(R.id.rv_follower)
    RecyclerView rv_follow_request;

    ArrayList<DiscoverModel.CustomerList> customerLists;
    Globals globals;
    FollowRequestAdapter followRequestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        globals = (Globals) getApplicationContext();
        setActionBar();
        doRequestForFollowRequest();
    }

    private void setActionBar() {
        toolbar.setBackgroundResource(R.drawable.header_bg);
        setSupportActionBar(toolbar);
        toolbar_title.setText(R.string.text_follow_request);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        menu.findItem(R.id.header_refresh).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.header_refresh) {
            doRequestForFollowRequest();
        }
        return false;
    }

    private void doRequestForFollowRequest() {
        new PostRequest(this, null, getString(R.string.get_follow_request_url), true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                DiscoverModel discoverModel = new Gson().fromJson(response, DiscoverModel.class);
                if (discoverModel != null && discoverModel.getCustomerList() != null) {
                    customerLists = discoverModel.getCustomerList();
                    setFollowerRequestAdapter();
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {

            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    private void setFollowerRequestAdapter() {
        if (followRequestAdapter == null) {
            followRequestAdapter = new FollowRequestAdapter(getApplicationContext(), this);
        }
        if (rv_follow_request.getAdapter() == null) {
            rv_follow_request.setLayoutManager(new LinearLayoutManager(this));
            rv_follow_request.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            rv_follow_request.setAdapter(followRequestAdapter);
        }
        followRequestAdapter.doRefresh(customerLists);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    public void onApproveClick(int position, View v) {
        doRequestToFollowUserProfile(customerLists.get(position).getFollowerRequestId(), 1);
    }

    @Override
    public void onDisapproveClick(int position, View v) {
        doRequestToFollowUserProfile(customerLists.get(position).getFollowerRequestId(), 0);
    }

    private void doRequestToFollowUserProfile(int customer_id, int result) {
        final String requestURL = String.format(getString(R.string.get_approve_disapprove_follower_request_url), customer_id, result);
        new PostRequest(this, null, requestURL, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                LikeUnlikePostModel likeUnlikePostModel = new Gson().fromJson(response, LikeUnlikePostModel.class);
                if (likeUnlikePostModel != null) {
                    if (likeUnlikePostModel.getResult().equals(Constants.WN_SUCCESS)) {
                        doRequestForFollowRequest();
                    }
                } else {
                    Globals.showToast(getApplicationContext(), getString(R.string.msg_server_error));
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(getApplicationContext(), msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }
}
