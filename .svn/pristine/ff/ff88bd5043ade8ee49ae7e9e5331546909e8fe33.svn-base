package com.app.whosnextapp.settings;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.AdCommentListModel;
import com.app.whosnextapp.pictures.MentionHelper;
import com.linkedin.android.spyglass.ui.MentionsEditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewAdCommentAdapter extends RecyclerView.Adapter<ViewAdCommentAdapter.viewAllCommentHolder> {
    public Context context;
    private ArrayList<AdCommentListModel.AdCommentList> adCommentLists;

    ViewAdCommentAdapter(Context context) {
        this.context = context;
    }

    public void doRefresh(ArrayList<AdCommentListModel.AdCommentList> adCommentLists) {
        this.adCommentLists = adCommentLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewAllCommentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_view_comments_layout, viewGroup, false);
        return new ViewAdCommentAdapter.viewAllCommentHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewAllCommentHolder viewAllCommentHolder, int i) {
        viewAllCommentHolder.setCommentData(adCommentLists.get(i), i);
    }

    @Override
    public int getItemCount() {
        return adCommentLists == null ? 0 : adCommentLists.size();
    }

    class viewAllCommentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_comment)
        MentionsEditText tv_comment;
        @BindView(R.id.tv_user)
        AppCompatTextView tv_user;

        viewAllCommentHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setCommentData(AdCommentListModel.AdCommentList adCommentLists, int i) {
            tv_comment.clearFocus();
            tv_comment.setText(MentionHelper.decodeComments((Activity) context, adCommentLists.getComment(), context.getResources().getColor(R.color.red)));
            tv_user.setText(adCommentLists.getUserName());
        }
    }
}
