package com.app.whosnextapp.navigationmenu;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.ConnectionDetector;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.model.FollowUnfollowModel;
import com.app.whosnextapp.model.GetFollowersListModel;
import com.app.whosnextapp.notification.ProfileVideoPlayerActivity;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowerActivity extends BaseAppCompatActivity implements View.OnClickListener, FollowerAdapter.OnCustomClickListener, TextWatcher {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;
    @BindView(R.id.rv_follower)
    RecyclerView rv_follower;
    @BindView(R.id.tv_no_user_found)
    TextView tv_no_user_found;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.et_search_people)
    AppCompatEditText et_search_people;

    Globals globals;
    Integer customer_id;
    String requestURL;
    FollowerAdapter followerAdapter;
    private ArrayList<GetFollowersListModel.CustomerList> customerLists;

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
        if (getIntent().hasExtra(Constants.WN_CUSTOMER_ID)) {
            customer_id = getIntent().getIntExtra(Constants.WN_CUSTOMER_ID, 0);
            requestURL = String.format(getString(R.string.get_followers_list_by_customerId), 1, customer_id);
        } else {
            requestURL = String.format(getString(R.string.get_followers_list), 1);
        }
        doRequestForFollowerList();
        et_search_people.addTextChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        menu.findItem(R.id.header_refresh).setVisible(true);
        menu.findItem(R.id.header_search).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.header_search:
                if (ll_search.getVisibility() == View.VISIBLE) {
                    ll_search.setVisibility(View.GONE);
                } else {
                    ll_search.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.header_refresh:
                doRequestForFollowerList();
                break;
        }
        return false;
    }

    private void setActionBar() {
        toolbar.setBackgroundResource(R.drawable.header_bg);
        setSupportActionBar(toolbar);
        toolbar_title.setText(Constants.WN_FOLLOWERS);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(this);
    }

    private void doRequestForFollowerList() {
        if (!ConnectionDetector.internetCheck(FollowerActivity.this, false))
            return;

        new PostRequest(this, null, requestURL, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                GetFollowersListModel getFollowersListModel = new Gson().fromJson(response, GetFollowersListModel.class);
                if (getFollowersListModel != null && !getFollowersListModel.getCustomerList().isEmpty()) {
                    customerLists = getFollowersListModel.getCustomerList();
                    setFollowerAdapter();
                } else {
                    tv_no_user_found.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(FollowerActivity.this, msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    private void setFollowerAdapter() {
        if (followerAdapter == null) {
            followerAdapter = new FollowerAdapter(getApplicationContext(), this);
        }
        if (rv_follower.getAdapter() == null) {
            rv_follower.setLayoutManager(new LinearLayoutManager(this));
            rv_follower.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
            rv_follower.setAdapter(followerAdapter);
        }
        followerAdapter.doRefresh(customerLists);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    public void onClickProfile(int position, View v) {
        Intent intent = new Intent(this, ProfileVideoPlayerActivity.class);
        intent.putExtra(Constants.WN_CUSTOMER_ID, customerLists.get(position).getCustomerId());
        startActivity(intent);
    }

    @Override
    public void onClickUsername(int position, View v) {
        Intent intent = new Intent(this, OtherUserProfileActivity.class);
        intent.putExtra(Constants.WN_CUSTOMER_ID, customerLists.get(position).getCustomerId());
        startActivity(intent);
    }

    @Override
    public void onClickFollowButton(int position, View v) {
        doRequestToFollowUserProfile(customerLists.get(position).getCustomerId(), 1);
    }

    @Override
    public void onClickRequestedButton(int position, View v) {
        doRequestToFollowUserProfile(customerLists.get(position).getCustomerId(), 0);
    }

    @Override
    public void onClickFollowingButton(int position, View v) {
        doRequestToFollowUserProfile(customerLists.get(position).getCustomerId(), 0);
    }

    @Override
    public void onFilter(boolean IsShow) {

    }

    private void doRequestToFollowUserProfile(int customer_id, int result) {
        final String requestURL = String.format(getString(R.string.get_follow_user_url), customer_id, result);
        new PostRequest(this, null, requestURL, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                FollowUnfollowModel followUnfollowModel = new Gson().fromJson(response, FollowUnfollowModel.class);
                if (followUnfollowModel != null) {
                    doRequestForFollowerList();
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


    private void searchPeople(String text) {
        if (followerAdapter != null) {
            followerAdapter.getFilter().filter(text);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        searchPeople(s.toString().trim());
    }
}
