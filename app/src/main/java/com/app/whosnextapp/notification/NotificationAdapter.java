package com.app.whosnextapp.notification;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.NotificationModel;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_NORMAL_NOTIFICATION = 1;
    private static final int TYPE_GROUP_NOTIFICATION = 2;
    private Context context;
    private ArrayList<NotificationModel.ActivityList> notificationModels;
    private OnCustomClickListener onCustomClickListener;
    private Activity activity;

    NotificationAdapter(Context context, OnCustomClickListener onCustomClickListener, Activity activity) {
        this.onCustomClickListener = onCustomClickListener;
        this.context = context;
        this.activity = activity;
    }

    void doRefresh(ArrayList<NotificationModel.ActivityList> notificationModel) {
        notificationModels = notificationModel;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_NORMAL_NOTIFICATION) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item_list, parent, false);
            return new NormalNotificationViewHolder(view, context, onCustomClickListener);
        } else if (viewType == TYPE_GROUP_NOTIFICATION) {
            view = LayoutInflater.from(context).inflate(R.layout.notification_group_video_admin, parent, false);
            return new GroupNotificationViewHolder(view, context, activity);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder.getItemViewType() == TYPE_NORMAL_NOTIFICATION) {
            NormalNotificationViewHolder holder = (NormalNotificationViewHolder) viewHolder;
            holder.bind(notificationModels.get(i));
        } else if (viewHolder.getItemViewType() == TYPE_GROUP_NOTIFICATION) {
            GroupNotificationViewHolder holder = (GroupNotificationViewHolder) viewHolder;
            holder.bind(notificationModels.get(i).getGroupVideoInvitedList(), i);
        }
    }

    public NotificationModel.ActivityList getItem(int position) {
        return notificationModels.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (notificationModels.get(position).getPostType().equals(13) && notificationModels.get(position).getGroupVideoInvitedList() != null) {
            return TYPE_GROUP_NOTIFICATION;
        } else {
            return TYPE_NORMAL_NOTIFICATION;
        }
    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    public interface OnCustomClickListener {
        void onClickProfile(int position, View v);

        void onClickUserName(int position, View v);

        void onClickFollowButton(int position, View v);

        void onClickFollowingButton(int position, View v);
    }
}
