package com.app.whosnextapp.pictures;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.GetAllLikesByPostIdModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.LikeHolder> {
    public Context context;
    private ArrayList<GetAllLikesByPostIdModel.CustomerList> LikePostList;

    LikesAdapter(Context context) {
        this.context = context;
    }

    public void doRefresh(ArrayList<GetAllLikesByPostIdModel.CustomerList> LikePostList) {
        this.LikePostList = LikePostList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LikeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_like_list_layout, viewGroup, false);
        return new LikesAdapter.LikeHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LikeHolder likeHolder, int i) {
        likeHolder.setLikeData(LikePostList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return LikePostList.size();
    }

    class LikeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_profile_pic)
        CircleImageView iv_profile_pic;
        @BindView(R.id.tv_like_name)
        AppCompatTextView tv_like_name;
        @BindView(R.id.tv_follow)
        AppCompatTextView tv_follow;

        LikeHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setLikeData(GetAllLikesByPostIdModel.CustomerList LikePostList, int i) {
            Glide.with(context)
                    .load(LikePostList.getProfilePicUrl())
                    .into(iv_profile_pic);
            tv_like_name.setText(LikePostList.getUserName());
            if (LikePostList.getIsFollowing()) {
                tv_follow.setText(R.string.following);
                tv_follow.setVisibility(View.VISIBLE);
            } else {
                tv_follow.setTextColor(context.getResources().getColor(R.color.sky));
                tv_follow.setBackgroundResource(R.drawable.border_follow);
                tv_follow.setText(R.string.follow);
                tv_follow.setVisibility(View.VISIBLE);
            }
        }
    }
}
