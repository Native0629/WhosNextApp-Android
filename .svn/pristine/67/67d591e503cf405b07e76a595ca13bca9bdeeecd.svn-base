package com.app.whosnextapp.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.NotificationModel;
import com.app.whosnextapp.utility.ConstantEnum;
import com.app.whosnextapp.utility.Globals;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationGroupVideoAdapter extends RecyclerView.Adapter<NotificationGroupVideoAdapter.ViewHolder> {
    Globals globals;
    private ArrayList<NotificationModel.ActivityList.GroupVideoInvitedList> groupVideoInvitedLists;
    private Context context;
    private NotificationGroupVideoAdapter.OnCustomClickListener onCustomClickListener;
    private boolean isAdmin;

    NotificationGroupVideoAdapter(ArrayList<NotificationModel.ActivityList.GroupVideoInvitedList> groupVideoInvitedLists, Context context, NotificationGroupVideoAdapter.OnCustomClickListener onCustomClickListener, boolean isAdmin) {
        this.groupVideoInvitedLists = groupVideoInvitedLists;
        this.context = context;
        this.onCustomClickListener = onCustomClickListener;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public NotificationGroupVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_group_user_list, null);
        globals = (Globals) context.getApplicationContext();
        return new NotificationGroupVideoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationGroupVideoAdapter.ViewHolder holder, int i) {
        try {
            holder.setDataToView(groupVideoInvitedLists.get(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return groupVideoInvitedLists.size();
    }

    public void doRefresh(ArrayList<NotificationModel.ActivityList.GroupVideoInvitedList> groupVideoInvitedList) {
        this.groupVideoInvitedLists = groupVideoInvitedList;
        notifyDataSetChanged();
    }

    ArrayList<NotificationModel.ActivityList.GroupVideoInvitedList> getItems() {
        return groupVideoInvitedLists;
    }

    public interface OnCustomClickListener {
        void onClickProfile(int position, View v);

        void onClickUsername(int position, View v);

        void onClickRemove(int position, View v);

        void onClickAccept(ArrayList<NotificationModel.ActivityList.GroupVideoInvitedList> activityLists, int position);

        void onClickDecline(ArrayList<NotificationModel.ActivityList.GroupVideoInvitedList> activityLists, int i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_profile)
        CircleImageView iv_profile;
        @BindView(R.id.tv_name)
        AppCompatTextView tv_name;
        @BindView(R.id.tv_status)
        AppCompatTextView tv_status;
        @BindView(R.id.iv_post)
        AppCompatImageView iv_post;
        @BindView(R.id.iv_remove)
        AppCompatImageView iv_remove;
        @BindView(R.id.btn_decline)
        AppCompatButton btn_decline;
        @BindView(R.id.btn_accept)
        AppCompatButton btn_accept;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            iv_profile.setOnClickListener(this);
            tv_name.setOnClickListener(this);
            btn_decline.setOnClickListener(this);
            btn_accept.setOnClickListener(this);
            iv_remove.setOnClickListener(this);
        }

        public void setDataToView(NotificationModel.ActivityList.GroupVideoInvitedList groupVideoInvitedList) {
            Glide.with(context).load(groupVideoInvitedList.getProfilePicUrl()).into(iv_profile);
            tv_name.setText(groupVideoInvitedList.getUserName());
            if (groupVideoInvitedList.getGroupStatus().equals(ConstantEnum.GroupInviteStatus.UPLOADED.getId())) {
                tv_status.setText(R.string.text_uploaded);
            } else if (groupVideoInvitedList.getGroupStatus().equals(ConstantEnum.GroupInviteStatus.REJECT.getId())) {
                tv_status.setText(R.string.text_status_rejected);
            } else if (groupVideoInvitedList.getGroupStatus().equals(ConstantEnum.GroupInviteStatus.PENDING.getId())) {
                btn_accept.setVisibility(View.GONE);
                btn_decline.setVisibility(View.GONE);
                if (groupVideoInvitedList.getIsAdmin()) {
                    tv_status.setText(R.string.text_uploaded);
                } else {
                    tv_status.setText(R.string.opponent_pending);
                }
            }
            Glide.with(context).load(groupVideoInvitedList.getUrl()).placeholder(R.drawable.question).into(iv_post);

            if (isAdmin && !groupVideoInvitedList.getGroupStatus().equals(ConstantEnum.GroupInviteStatus.UPLOADED.getId())) {
                iv_remove.setVisibility(View.VISIBLE);
            } else {
                iv_remove.setVisibility(View.GONE);
            }


            if (groupVideoInvitedList.getUserName().equalsIgnoreCase(globals.getUserDetails().getStatus().getUserName())
                    && !groupVideoInvitedList.getGroupStatus().equals(ConstantEnum.GroupInviteStatus.UPLOADED.getId())) {
                btn_accept.setVisibility(View.VISIBLE);
                btn_decline.setVisibility(View.VISIBLE);
                tv_status.setVisibility(View.GONE);

            } else {
                btn_accept.setVisibility(View.GONE);
                btn_decline.setVisibility(View.GONE);
                tv_status.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_profile:
                    onCustomClickListener.onClickProfile(getAdapterPosition(), v);
                    break;
                case R.id.tv_name:
                    onCustomClickListener.onClickUsername(getAdapterPosition(), v);
                    break;
                case R.id.iv_remove:
                    onCustomClickListener.onClickRemove(getAdapterPosition(), v);
                    break;
                case R.id.btn_accept:
                    onCustomClickListener.onClickAccept(groupVideoInvitedLists, getAdapterPosition());
                    break;
                case R.id.btn_decline:
                    onCustomClickListener.onClickDecline(groupVideoInvitedLists, getAdapterPosition());

            }
        }
    }
}
