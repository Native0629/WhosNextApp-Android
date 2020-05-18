package com.app.whosnextapp.breastCancerLegacies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.GetAllCommentsByBreastCancerLegacyModel;
import com.app.whosnextapp.pictures.MentionHelper;
import com.linkedin.android.spyglass.ui.MentionsEditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewAllBCLCommentAdapter extends RecyclerView.Adapter<ViewAllBCLCommentAdapter.viewAllBCLCommentHolder> {
    public Context context;
    private ArrayList<GetAllCommentsByBreastCancerLegacyModel.BCLCommentList> BCLCommentList;

    ViewAllBCLCommentAdapter(Context context) {
        this.context = context;
    }

    public void doRefresh(ArrayList<GetAllCommentsByBreastCancerLegacyModel.BCLCommentList> BCLCommentList) {
        this.BCLCommentList = BCLCommentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewAllBCLCommentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_view_bcl_comment_layout, viewGroup, false);
        return new ViewAllBCLCommentAdapter.viewAllBCLCommentHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewAllBCLCommentHolder viewAllBCLCommentHolder, int i) {
        viewAllBCLCommentHolder.setBCLCommentData(BCLCommentList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return BCLCommentList == null ? 0 : BCLCommentList.size();
    }

    class viewAllBCLCommentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_bcl_comment)
        MentionsEditText tv_bcl_comment;

        viewAllBCLCommentHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setBCLCommentData(GetAllCommentsByBreastCancerLegacyModel.BCLCommentList BCLCommentList, int i) {
            tv_bcl_comment.setText(MentionHelper.decodeComment((Activity) context, BCLCommentList.getComment(), BCLCommentList.getUserName(), context.getResources().getColor(R.color.pink)), TextView.BufferType.SPANNABLE);
        }
    }
}
