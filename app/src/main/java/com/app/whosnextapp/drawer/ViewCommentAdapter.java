package com.app.whosnextapp.drawer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.PostCommentListModel;
import com.app.whosnextapp.pictures.MentionHelper;
import com.app.whosnextapp.utility.Constants;
import com.linkedin.android.spyglass.ui.MentionsEditText;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewCommentAdapter extends RecyclerView.Adapter<ViewCommentAdapter.viewAllCommentHolder> {
    public Context context;
    private ArrayList<PostCommentListModel.PostCommentList> CommentList;
    private String post_type = "";

    public ViewCommentAdapter(Context context) {
        this.context = context;
    }

    public ViewCommentAdapter(Context context, String post_type) {
        this.context = context;
        this.post_type = post_type;
    }

    public void doRefresh(ArrayList<PostCommentListModel.PostCommentList> CommentList) {
        this.CommentList = CommentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewAllCommentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_view_comments_layout, viewGroup, false);
        return new ViewCommentAdapter.viewAllCommentHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewAllCommentHolder viewAllCommentHolder, int i) {
        viewAllCommentHolder.setCommentData(CommentList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return CommentList == null ? 0 : CommentList.size();
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

        void setCommentData(PostCommentListModel.PostCommentList CommentList, int i) {
            tv_comment.clearFocus();
            tv_comment.setText(MentionHelper.decodeComments((Activity) context, StringEscapeUtils.unescapeJava(CommentList.getComment()),
                    context.getResources().getColor(post_type.equalsIgnoreCase(Constants.WN_POST_TYPE_Featured_Profile) ? R.color.sky : R.color.red)));
            tv_user.setText(CommentList.getUserName());
            tv_user.setTextColor(context.getResources().getColor(post_type.equalsIgnoreCase(Constants.WN_POST_TYPE_Featured_Profile) ? R.color.sky : R.color.red));
        }
    }
}