package com.app.whosnextapp.breastCancerLegacies;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.GetAllCommentsByBreastCancerLegacyModel;
import com.app.whosnextapp.utility.TimeUtils;
import com.linkedin.android.spyglass.ui.MentionsEditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BreastCommentAdapter extends RecyclerView.Adapter<BreastCommentAdapter.breastCommentHolder> {
    public Context context;
    private ArrayList<GetAllCommentsByBreastCancerLegacyModel.BCLCommentList> BCLCommentList;
    private ArrayList<SpannableString> spannableStrings;

    BreastCommentAdapter(Context context) {
        this.context = context;
    }

    public void doRefresh(ArrayList<GetAllCommentsByBreastCancerLegacyModel.BCLCommentList> BCLCommentList, ArrayList<SpannableString> spannableStrings) {
        this.BCLCommentList = BCLCommentList;
        this.spannableStrings = spannableStrings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public breastCommentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_comment_layout, viewGroup, false);
        return new BreastCommentAdapter.breastCommentHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull breastCommentHolder breastCommentHolder, int i) {
        breastCommentHolder.setCommentData(BCLCommentList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return BCLCommentList.size();
    }

    class breastCommentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_comment)
        MentionsEditText tv_comment;
        @BindView(R.id.tv_user)
        AppCompatTextView tv_user;
        @BindView(R.id.tv_comment_time)
        AppCompatTextView tv_comment_time;

        breastCommentHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setCommentData(final GetAllCommentsByBreastCancerLegacyModel.BCLCommentList BCLCommentList, int i) {
            tv_comment.deselectAllSpans();
            tv_comment.clearFocus();
            if (spannableStrings != null && spannableStrings.get(i) != null) {
                tv_comment.setText(spannableStrings.get(i));
            }
            tv_user.setText(BCLCommentList.getCustomerName());
            tv_user.setTextColor(context.getResources().getColor(R.color.pink));
            tv_comment_time.setText(TimeUtils.getTimeDiff(context, Long.parseLong(BCLCommentList.getTimeDiff())));
        }
    }
}
