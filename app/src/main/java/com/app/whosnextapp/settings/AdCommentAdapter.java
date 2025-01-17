package com.app.whosnextapp.settings;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.AdCommentListModel;
import com.app.whosnextapp.pictures.CommentActivity;
import com.app.whosnextapp.pictures.MentionHelper;
import com.app.whosnextapp.utility.TimeUtils;
import com.linkedin.android.spyglass.ui.MentionsEditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdCommentAdapter extends RecyclerView.Adapter<AdCommentAdapter.CommentHolder> {

    public Context context;
    private ArrayList<AdCommentListModel.AdCommentList> adCommentLists;
    private ArrayList<SpannableString> spannableStrings;

    public AdCommentAdapter(Context context) {
        this.context = context;
    }

    public void doRefresh(ArrayList<AdCommentListModel.AdCommentList> adCommentLists, ArrayList<SpannableString> spannableStrings) {
        this.adCommentLists = adCommentLists;
        this.spannableStrings = spannableStrings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_comment_layout, viewGroup, false);
        return new AdCommentAdapter.CommentHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder commentHolder, int i) {
        commentHolder.setCommentData(adCommentLists.get(i), i);
    }

    @Override
    public int getItemCount() {
        return adCommentLists.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_comment)
        MentionsEditText tv_comment;
        @BindView(R.id.tv_user)
        AppCompatTextView tv_user;
        @BindView(R.id.tv_comment_time)
        AppCompatTextView tv_comment_time;

        CommentHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setCommentData(final AdCommentListModel.AdCommentList adCommentLists, int i) {
            tv_comment.deselectAllSpans();
            tv_comment.clearFocus();

            if (spannableStrings != null && spannableStrings.get(i) != null) {
                tv_comment.setText(spannableStrings.get(i));
            } else {
                tv_comment.setText(MentionHelper.decodeComments((CommentActivity) context, "", context.getResources().getColor(R.color.red)));
            }
            tv_user.setText(adCommentLists.getCustomerName());
            tv_comment_time.setText(TimeUtils.getTimeDiff(context, Long.parseLong(adCommentLists.getTimeDiff())));
        }
    }
}
