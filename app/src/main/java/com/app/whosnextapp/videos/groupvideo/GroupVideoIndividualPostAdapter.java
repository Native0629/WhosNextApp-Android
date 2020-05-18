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
import com.app.whosnextapp.model.GetGroupVideoIndividualModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupVideoIndividualPostAdapter extends RecyclerView.Adapter<GroupVideoIndividualPostAdapter.ViewHolder> {
    private Context context;
    private ArrayList<GetGroupVideoIndividualModel.Result> groupVideoIndividualPost = new ArrayList<>();

    GroupVideoIndividualPostAdapter(Context context) {
        this.context = context;
    }

    void doRefresh(ArrayList<GetGroupVideoIndividualModel.Result> groupVideoIndividualPost) {
        this.groupVideoIndividualPost = groupVideoIndividualPost;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GroupVideoIndividualPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.child_individual_post, viewGroup, false);
        return new GroupVideoIndividualPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupVideoIndividualPostAdapter.ViewHolder viewHolder, int i) {
        try {
            viewHolder.setDataToView(groupVideoIndividualPost.get(i), i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return groupVideoIndividualPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_profile)
        AppCompatImageView iv_profile;
        @BindView(R.id.tv_name)
        AppCompatTextView tv_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setDataToView(GetGroupVideoIndividualModel.Result groupIndividualPost, int i) {
            Glide.with(context).load(groupIndividualPost.getVideoThumbnailUrl()).into(iv_profile);
            tv_name.setText(groupIndividualPost.getCustomerName());
        }
    }
}
