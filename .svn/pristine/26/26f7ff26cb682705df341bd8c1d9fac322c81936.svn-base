package com.app.whosnextapp.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.HttpRequestHandler;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.drawer.HomePageActivity;
import com.app.whosnextapp.model.FollowUnfollowModel;
import com.app.whosnextapp.model.NotificationModel;
import com.app.whosnextapp.navigationmenu.OtherUserProfileActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.app.whosnextapp.utility.TimeUtils;
import com.app.whosnextapp.videos.groupvideo.AddGroupVideoByUserActivity;
import com.app.whosnextapp.videos.groupvideo.InvitePeopleActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

class GroupNotificationViewHolder extends RecyclerView.ViewHolder implements NotificationGroupVideoAdapter.OnCustomClickListener {

    @BindView(R.id.iv_profile)
    CircleImageView iv_profile;
    @BindView(R.id.tv_name)
    AppCompatTextView tv_name;
    @BindView(R.id.tv_time)
    AppCompatTextView tv_time;
    @BindView(R.id.tv_message)
    AppCompatTextView tv_message;
    @BindView(R.id.iv_post)
    AppCompatImageView iv_post;
    @BindView(R.id.rv_group_video)
    RecyclerView rv_group_video;

    private Context context;
    private Activity activity;
    private NotificationGroupVideoAdapter notificationGroupVideoAdapter;
    private Globals globals;

    GroupNotificationViewHolder(@NonNull View itemView, Context context, Activity activity) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        this.activity = activity;
        globals = (Globals) activity.getApplicationContext();
    }

    public void bind(ArrayList<NotificationModel.ActivityList.GroupVideoInvitedList> activityLists, int i) {
        if (activityLists != null) {
            iv_profile.setOnClickListener(v -> {
                Intent intent = new Intent(context, ProfileVideoPlayerActivity.class);
                intent.putExtra(Constants.WN_CUSTOMER_ID, Integer.valueOf(globals.getUserDetails().getStatus().getCustomerId()));
                activity.startActivity(intent);
            });

            tv_name.setOnClickListener(v -> {
                Intent intent = new Intent(context, OtherUserProfileActivity.class);
                intent.putExtra(Constants.WN_CUSTOMER_ID, Integer.valueOf(globals.getUserDetails().getStatus().getCustomerId()));
                activity.startActivity(intent);
            });
            boolean isAdmin = false;
            for (int j = 0; j < activityLists.size(); j++) {
                if (activityLists.get(j).getIsAdmin()) {
                    Glide.with(context).load(activityLists.get(j).getProfilePicUrl()).into(iv_profile);
                    Glide.with(context).load(activityLists.get(j).getUrl()).into(iv_post);
                    tv_name.setText(activityLists.get(j).getUserName());
                    tv_time.setText(TimeUtils.getTimeDiff(context, Long.parseLong(activityLists.get(j).getTimeDiff())));
                    isAdmin = activityLists.get(j).getIsAdmin();
                    tv_message.setText(activity.getString(R.string.msg_group_invited_user));
                    activityLists.remove(j);
                    break;
                }
            }
            setGroupUserListAdapter(activityLists, isAdmin);
        }
    }

    private void doRequestToUpdateNotification(ArrayList<NotificationModel.ActivityList.GroupVideoInvitedList> activityList, int i, String result) {
        JSONObject postData = HttpRequestHandler.getInstance().getUpdateGroupNotification
                (activityList.get(i).getId(), globals.getUserDetails().getStatus().getCustomerId(),
                        activityList.get(i).getActionUserId(), result);

        new PostRequest(activity, postData, context.getString(R.string.update_user_in_group_notification), true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                FollowUnfollowModel followUnfollowModel = new Gson().fromJson(response, FollowUnfollowModel.class);
                if (followUnfollowModel.getResult() == 1) {
                    activity.startActivity(new Intent(activity, AddGroupVideoByUserActivity.class)
                            .putExtra(Constants.WN_GROUP_POST_ID, activityList.get(i).getId()));
                    activity.finish();
                } else if (followUnfollowModel.getResult() == 2) {
                    activity.startActivity(new Intent(activity, HomePageActivity.class));
                    activity.finish();
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(context, msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    private void setGroupUserListAdapter(ArrayList<NotificationModel.ActivityList.GroupVideoInvitedList> groupVideoInvitedList, boolean isAdmin) {
        if (notificationGroupVideoAdapter == null) {
            notificationGroupVideoAdapter = new NotificationGroupVideoAdapter(groupVideoInvitedList, context, this, isAdmin);
        }
        if (rv_group_video.getAdapter() == null) {
            rv_group_video.setLayoutManager(new LinearLayoutManager(context));
            rv_group_video.setAdapter(notificationGroupVideoAdapter);
        }
        notificationGroupVideoAdapter.doRefresh(groupVideoInvitedList);
    }

    @Override
    public void onClickProfile(int position, View v) {
        Intent intent = new Intent(activity, ProfileVideoPlayerActivity.class);
        intent.putExtra(Constants.WN_CUSTOMER_ID, Integer.valueOf(notificationGroupVideoAdapter.getItems().get(position).getCustomerId()));
        activity.startActivity(intent);
    }

    @Override
    public void onClickUsername(int position, View v) {
        Intent intent = new Intent(activity, OtherUserProfileActivity.class);
        intent.putExtra(Constants.WN_CUSTOMER_ID, Integer.valueOf(notificationGroupVideoAdapter.getItems().get(position).getCustomerId()));
        activity.startActivity(intent);
    }

    @Override
    public void onClickRemove(int position, View v) {
        Intent i = new Intent(activity, InvitePeopleActivity.class);
        i.putExtra(Constants.WN_CUSTOMER_ID, notificationGroupVideoAdapter.getItems().get(position).getCustomerId());
        i.putExtra(Constants.WN_POST_ID, notificationGroupVideoAdapter.getItems().get(position).getId());
        i.putExtra(Constants.WN_IS_REMOVE_GROUP, true);
        activity.startActivity(i);
    }

    @Override
    public void onClickAccept(ArrayList<NotificationModel.ActivityList.GroupVideoInvitedList> activityLists, int position) {
        doRequestToUpdateNotification(activityLists, position, "1");
    }

    @Override
    public void onClickDecline(ArrayList<NotificationModel.ActivityList.GroupVideoInvitedList> activityLists, int i) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(activity.getResources().getString(R.string.are_you_sure_want_to_decline));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(activity.getResources().getString(R.string.yes),
                (arg0, arg1) -> {
                    doRequestToUpdateNotification(activityLists, i, "2");
                });
        alertDialogBuilder.setNegativeButton(activity.getResources().getString(R.string.no), (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
