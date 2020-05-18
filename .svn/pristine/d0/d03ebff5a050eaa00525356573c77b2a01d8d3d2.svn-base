package com.app.whosnextapp.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.PersonModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linkedin.android.spyglass.suggestions.interfaces.Suggestible;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchInFollowingFollowerAdapter extends RecyclerView.Adapter<SearchInFollowingFollowerAdapter.followerHolder> {

    public Context context;
    private List<? extends Suggestible> customerList;
    private OnCommentSuggestionEvent onCommentSuggestionEvent;

    public SearchInFollowingFollowerAdapter(Context context, OnCommentSuggestionEvent onCommentSuggestionEvent) {
        this.context = context;
        this.onCommentSuggestionEvent = onCommentSuggestionEvent;
    }

    public void doRefresh(List<? extends Suggestible> customerList) {
        this.customerList = customerList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public followerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.discover_people_item, viewGroup, false);
        return new SearchInFollowingFollowerAdapter.followerHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull followerHolder followerHolder, int i) {
        followerHolder.setDataToView(customerList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public interface OnCommentSuggestionEvent {
        void onSuggestionClick(PersonModel customerList);
    }

    class followerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_profile_pic)
        CircleImageView iv_profile_pic;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_username)
        TextView tv_username;

        followerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setDataToView(final Suggestible suggestion, final int i) {
            if (!(suggestion instanceof PersonModel)) {
                return;
            }
            final PersonModel customerList = (PersonModel) suggestion;

            itemView.setOnClickListener(v -> onCommentSuggestionEvent.onSuggestionClick(customerList));

            tv_name.setText(customerList.getFirstName() + " " + customerList.getLastName());
            tv_username.setText(customerList.getUserName());
            Glide.with(context)
                    .load(customerList.getProfilePicUrl())
                    .apply(RequestOptions.placeholderOf(R.drawable.user_profile))
                    .into(iv_profile_pic);
        }
    }
}
