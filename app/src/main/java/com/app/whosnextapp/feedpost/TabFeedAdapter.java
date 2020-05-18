package com.app.whosnextapp.feedpost;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.GetAllPostByCustomerIDModel;
import com.app.whosnextapp.utility.Constants;

import java.util.ArrayList;

import im.ene.toro.widget.PressablePlayerSelector;

public class TabFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private OnItemClick onItemClick;
    private ArrayList<GetAllPostByCustomerIDModel.PostList> postLists;
    private PressablePlayerSelector selector;
    private Activity activity;

    TabFeedAdapter(Activity activity, Context context, OnItemClick onItemClick, PressablePlayerSelector selector) {
        this.context = context;
        this.activity = activity;
        this.onItemClick = onItemClick;
        this.selector = selector;
    }

    public void doRefresh(ArrayList<GetAllPostByCustomerIDModel.PostList> postLists) {
        this.postLists = postLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (i == Constants.LAYOUT_GROUP_FEED) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_group_video, viewGroup, false);
            return new TabFeedGroupVideoViewHolder(activity, context, view, selector, onItemClick);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_tabfeed_recyclerview, viewGroup, false);
            return new TabFeedViewHolder(context, view, selector, onItemClick);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder.getItemViewType() == Constants.LAYOUT_FEED) {
            TabFeedViewHolder holder = (TabFeedViewHolder) viewHolder;
            holder.bind(postLists.get(i));
        } else if (viewHolder.getItemViewType() == Constants.LAYOUT_GROUP_FEED) {
            TabFeedGroupVideoViewHolder holder = (TabFeedGroupVideoViewHolder) viewHolder;
            holder.bind(postLists.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return postLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (postLists.get(position).getIsGroupVideo()) {
            return Constants.LAYOUT_GROUP_FEED;
        } else {
            return Constants.LAYOUT_FEED;
        }
    }

    interface OnItemClick {
        void onClickAddComment(int i);

        void likeDisplay(int i);

        void displayAllComments(int i);

        void displayComment(int i);

        void onClickLike(GetAllPostByCustomerIDModel.PostList postList, int i);

        void onClickShareIcon(int i);

        void onClickOptionIcon(int i);

        void onClickUsername(int i);

        void onClickProfile(int i);

        void onClickSmallProfile(int i);

        void onClickSmallUsername(int i);

        /*void requestForView(GetAllPostByCustomerIDModel.PostList postList, int i);*/
    }
}