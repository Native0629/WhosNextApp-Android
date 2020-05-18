package com.app.whosnextapp.videos.groupvideo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.GroupListModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {
    private ArrayList<GroupListModel.UserList> userLists;
    private Context context;
    private GroupListAdapter.OnCustomClickListener onCustomClickListener;


    GroupListAdapter(Context context, GroupListAdapter.OnCustomClickListener onCustomClickListener) {
        this.context = context;
        this.onCustomClickListener = onCustomClickListener;
    }

    void doRefresh(ArrayList<GroupListModel.UserList> groupListModels) {
        this.userLists = groupListModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.child_invite_people_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        try {
            viewHolder.setDataToView(userLists.get(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ArrayList<GroupListModel.UserList> getItems() {
        return userLists;
    }

    @Override
    public int getItemCount() {
        return userLists.size();
    }

    public interface OnCustomClickListener {
        void onClickRemove(int position, View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_profile_pic)
        CircleImageView iv_profile_pic;
        @BindView(R.id.tv_username)
        AppCompatTextView tv_username;
        @BindView(R.id.iv_remove)
        AppCompatImageView iv_remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            iv_remove.setOnClickListener(this);
        }

        public void setDataToView(GroupListModel.UserList groupListModel) {
            tv_username.setText(groupListModel.getUsername());
            Glide.with(context).load(groupListModel.getProfilePic()).into(iv_profile_pic);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.iv_remove) {
                onCustomClickListener.onClickRemove(getAdapterPosition(), v);
            }
        }
    }
}
