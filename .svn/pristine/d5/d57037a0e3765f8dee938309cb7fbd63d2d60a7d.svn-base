package com.app.whosnextapp.notification;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.NotificationModel;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.TimeUtils;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

class NormalNotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.iv_profile)
    CircleImageView iv_profile;
    @BindView(R.id.tv_name)
    AppCompatTextView tv_name;
    @BindView(R.id.tv_time)
    AppCompatTextView tv_time;
    @BindView(R.id.tv_message)
    AppCompatTextView tv_message;
    @BindView(R.id.tv_follow)
    AppCompatTextView tv_follow;
    @BindView(R.id.iv_post)
    AppCompatImageView iv_post;

    private Context context;
    private NotificationAdapter.OnCustomClickListener onCustomClickListener;

    NormalNotificationViewHolder(@NonNull View itemView, Context context, NotificationAdapter.OnCustomClickListener onCustomClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        this.onCustomClickListener = onCustomClickListener;
        iv_profile.setOnClickListener(this);
        tv_name.setOnClickListener(this);
        tv_follow.setOnClickListener(this);
    }

    public void bind(NotificationModel.ActivityList activityList) {
        Glide.with(context)
                .load(activityList.getProfilePicUrl())
                .into(iv_profile);

        tv_name.setText(activityList.getUserName());
        tv_message.setText(activityList.getActivityMessage());
        tv_time.setText(TimeUtils.getTimeDiff(context, Long.parseLong(activityList.getTimeDiff())));
        if (!activityList.getUrl().isEmpty()) {
            Glide.with(context)
                    .load(activityList.getUrl())
                    .into(iv_post);
            iv_post.setVisibility(View.VISIBLE);
            tv_follow.setVisibility(View.GONE);
            return;
        } else {
            iv_post.setVisibility(View.GONE);
        }
        if (activityList.getIsFollowing()) {
            tv_follow.setTextColor(context.getResources().getColor(R.color.red));
            tv_follow.setBackgroundResource(R.drawable.border_following);
            tv_follow.setText(R.string.following);
            tv_follow.setVisibility(View.VISIBLE);
        } else {
            tv_follow.setTextColor(context.getResources().getColor(R.color.sky));
            tv_follow.setBackgroundResource(R.drawable.border_follow);
            tv_follow.setText(R.string.follow);
            tv_follow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_profile:
                onCustomClickListener.onClickProfile(getAdapterPosition(), v);
                break;
            case R.id.tv_name:
                onCustomClickListener.onClickUserName(getAdapterPosition(), v);
                break;
            case R.id.tv_follow:
                if (tv_follow.getText().toString().equals(context.getResources().getString(R.string.follow)))
                    onCustomClickListener.onClickFollowButton(getAdapterPosition(), v);
                else if (tv_follow.getText().toString().equals(context.getResources().getString(R.string.following)))
                    onCustomClickListener.onClickFollowingButton(getAdapterPosition(), v);
                break;
        }
    }
}
