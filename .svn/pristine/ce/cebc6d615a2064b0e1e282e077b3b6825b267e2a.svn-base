package com.app.whosnextapp.notification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.model.DiscoverModel;
import com.app.whosnextapp.model.FollowUnfollowModel;
import com.app.whosnextapp.model.NotificationModel;
import com.app.whosnextapp.navigationmenu.OtherUserProfileActivity;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationActivity extends BaseAppCompatActivity implements View.OnClickListener, NotificationAdapter.OnCustomClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;
    @BindView(R.id.rv_notification)
    RecyclerView rv_notification;
    @BindView(R.id.ll_follower_request)
    LinearLayout ll_follower_request;
    @BindView(R.id.tv_follower_username)
    TextView tv_follower_username;
    @BindView(R.id.line_view)
    View line;

    boolean isLoaderRequire = true;
    Globals globals;
    NotificationAdapter notificationAdapter;
    ArrayList<NotificationModel.ActivityList> activityLists = new ArrayList<>();

    private Activity getActivity() {
        return NotificationActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        globals = (Globals) getApplicationContext();
        setActionBar();
//        doRequestForNotification();
//        doRequestForFollowRequest();
    }

    private void setActionBar() {
        toolbar.setBackgroundResource(R.drawable.header_bg);
        setSupportActionBar(toolbar);
        toolbar_title.setText(R.string.notification);
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
            doRequestForNotification();
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        //todo remove extra calls
        doRequestForNotification();
        doRequestForFollowRequest();
        isLoaderRequire = false;
    }

    private void doRequestForFollowRequest() {
        new PostRequest(this, null, getString(R.string.get_follow_request_url), isLoaderRequire, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                DiscoverModel discoverModel = new Gson().fromJson(response, DiscoverModel.class);
                if (discoverModel != null && !discoverModel.getCustomerList().isEmpty()) {
                    ll_follower_request.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    for (int i = 0; i < discoverModel.getCustomerList().size(); i++) {
                        tv_follower_username.setText(discoverModel.getCustomerList().get(i).getFirstName() + " " + discoverModel.getCustomerList().get(i).getLastName());
                    }
                } else {
                    ll_follower_request.setVisibility(View.GONE);
                    line.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(getActivity(), msg);

            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    private void doRequestForNotification() {
        new PostRequest(this, null, getString(R.string.get_notification_url), isLoaderRequire, isLoaderRequire, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                NotificationModel notificationModel = new Gson().fromJson(response, NotificationModel.class);
                if (notificationModel != null) {
                    activityLists = notificationModel.getActivityList();
                    setNotificationAdapter();
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(getActivity(), msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    private void setNotificationAdapter() {
        if (notificationAdapter == null) {
            notificationAdapter = new NotificationAdapter(getApplicationContext(), this, getActivity());
        }
        if (rv_notification.getAdapter() == null) {
            rv_notification.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv_notification.setAdapter(notificationAdapter);
        }
        notificationAdapter.doRefresh(activityLists);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @OnClick(R.id.ll_follower_request)
    public void onFollowRequest() {
        startActivity(new Intent(getActivity(), FollowRequestActivity.class));
    }

    @Override
    public void onClickProfile(int position, View v) {
        Intent intent = new Intent(getActivity(), ProfileVideoPlayerActivity.class);
        intent.putExtra(Constants.WN_CUSTOMER_ID, activityLists.get(position).getActionUserId());
        startActivity(intent);
    }

    @Override
    public void onClickUserName(int position, View v) {
        Intent intent = new Intent(getActivity(), OtherUserProfileActivity.class);
        intent.putExtra(Constants.WN_CUSTOMER_ID, activityLists.get(position).getActionUserId());
        startActivity(intent);
    }

    @Override
    public void onClickFollowButton(int position, View v) {
        doRequestToFollowUserProfile(activityLists.get(position).getActionUserId(), 1);
    }

    @Override
    public void onClickFollowingButton(int position, View v) {
        doRequestToFollowUserProfile(activityLists.get(position).getActionUserId(), 0);
    }

    private void doRequestToFollowUserProfile(int customer_id, int result) {

        final String requestURL = String.format(getString(R.string.get_follow_user_url), customer_id, result);
        new PostRequest(this, null, requestURL, false, false, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                FollowUnfollowModel followUnfollowModel = new Gson().fromJson(response, FollowUnfollowModel.class);
                if (followUnfollowModel != null) {
                    doRequestForNotification();
                } else {
                    Globals.showToast(getActivity(), getString(R.string.msg_server_error));
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(getActivity(), msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }
}
