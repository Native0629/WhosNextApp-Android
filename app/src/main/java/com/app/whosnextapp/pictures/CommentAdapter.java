package com.app.whosnextapp.pictures;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.PostCommentListModel;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.TimeUtils;
import com.linkedin.android.spyglass.ui.MentionsEditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    public Context context;
    private String post_type;
    private ArrayList<PostCommentListModel.PostCommentList> commentList;
    private ArrayList<SpannableString> spannableStrings;

    CommentAdapter(Context context, String post_type) {
        this.context = context;
        this.post_type = post_type;
    }

    public void doRefresh(ArrayList<PostCommentListModel.PostCommentList> CommentList, ArrayList<SpannableString> spannableStrings) {
        this.commentList = CommentList;
        this.spannableStrings = spannableStrings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_comment_layout, viewGroup, false);
        return new CommentAdapter.CommentHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder commentHolder, int i) {
        commentHolder.setCommentData(commentList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_comment)
        MentionsEditText tv_comment;
        @BindView(R.id.tv_user)
        AppCompatTextView tv_user;
        @BindView(R.id.tv_comment_time)
        AppCompatTextView tv_comment_time;

        String Comment;

        CommentHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setCommentData(final PostCommentListModel.PostCommentList CommentList, int i) {
            tv_comment.deselectAllSpans();
            tv_comment.clearFocus();
            if (spannableStrings != null && spannableStrings.get(i) != null) {
                tv_comment.setText(spannableStrings.get(i));
            } else {
                tv_comment.setText(MentionHelper.decodeComments((CommentActivity) context,
                        Comment, context.getResources().getColor(post_type.equalsIgnoreCase(Constants.WN_POST_TYPE_Featured_Profile) ? R.color.sky : R.color.red)));
            }
            tv_user.setText(CommentList.getCustomerName());
            if (post_type.equalsIgnoreCase(Constants.WN_POST_TYPE_Featured_Profile)) {
                tv_user.setTextColor(context.getResources().getColor(R.color.sky));
            }
            tv_comment_time.setText(TimeUtils.getTimeDiff(context, Long.parseLong(CommentList.getTimeDiff())));

        }
    }
}
