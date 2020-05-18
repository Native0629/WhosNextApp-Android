package com.app.whosnextapp.drawer.homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.GroupVideoModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupVideoAdapter extends RecyclerView.Adapter<GroupVideoAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GroupVideoModel> groupVideoModels;
    private OnGroupVideoClickListener clickListener;

    GroupVideoAdapter(Context context, ArrayList<GroupVideoModel> groupVideoModels, OnGroupVideoClickListener clickListener) {
        this.context = context;
        this.groupVideoModels = groupVideoModels;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_group_video_single, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.setDataToView(groupVideoModels.get(position));
    }

    @Override
    public int getItemCount() {
        return groupVideoModels == null ? 0 : groupVideoModels.size();
    }

    public interface OnGroupVideoClickListener {
        void onUserVideoClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_thumbnail)
        AppCompatImageView iv_thumbnail;
        @BindView(R.id.tv_group_video_username)
        AppCompatTextView tv_group_video_username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            iv_thumbnail.setOnClickListener(this);
        }

        void setDataToView(GroupVideoModel groupVideoModel) {
            Glide.with(context).load(groupVideoModel.getVideoThumbnailUrl()).into(iv_thumbnail);
            tv_group_video_username.setText(groupVideoModel.getCustomerName());
            if (groupVideoModel.isSelected) {
                iv_thumbnail.setPadding(10, 10, 10, 10);
                iv_thumbnail.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_video_frame));
            } else {
                iv_thumbnail.setPadding(0, 0, 0, 0);
                iv_thumbnail.setBackground(null);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.iv_thumbnail) {
                handleSelection(getAdapterPosition());
                clickListener.onUserVideoClick(getAdapterPosition());
            }
        }

        void handleSelection(int position) {
            for (int i = 0; i < groupVideoModels.size(); i++) {
                if (groupVideoModels.get(i).isSelected) {
                    groupVideoModels.get(i).isSelected = false;
                    notifyItemChanged(i);
                }
            }
            groupVideoModels.get(position).isSelected = true;
            notifyItemChanged(position);
        }
    }
}
