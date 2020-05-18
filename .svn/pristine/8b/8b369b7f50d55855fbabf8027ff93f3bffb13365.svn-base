package com.app.whosnextapp.featuredProfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.GetFeaturesProfileModel;

import java.util.ArrayList;

import im.ene.toro.widget.PressablePlayerSelector;

public class FeaturedProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int LAYOUT_FEED = 1;
    private static final int LAYOUT_GROUP_FEED = 2;
    private Context context;
    private ArrayList<GetFeaturesProfileModel.DashboardList> dashboardLists;
    private OnItemClick onItemClick;
    private PressablePlayerSelector selector;

    FeaturedProfileAdapter(Context context, OnItemClick onItemClick, PressablePlayerSelector selector) {
        this.context = context;
        this.onItemClick = onItemClick;
        this.selector = selector;
    }

    public void doRefresh(ArrayList<GetFeaturesProfileModel.DashboardList> dashboardLists) {
        this.dashboardLists = dashboardLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        if (i == LAYOUT_GROUP_FEED) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_group_video, viewGroup, false);
            viewHolder = new FeaturedProfileViewHolder(context, view, selector, onItemClick);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_featured_profile_layout, viewGroup, false);
            viewHolder = new FeaturedProfileViewHolder(context, view, selector, onItemClick);
            if (this.selector != null) viewHolder.itemView.setOnLongClickListener(this.selector);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder.getItemViewType() == LAYOUT_FEED) {
            FeaturedProfileViewHolder holder = (FeaturedProfileViewHolder) viewHolder;
            holder.bind(dashboardLists.get(i));
        } else if (viewHolder.getItemViewType() == LAYOUT_GROUP_FEED) {
            FeaturedProfileGroupViewHolder holder = (FeaturedProfileGroupViewHolder) viewHolder;
            holder.bind(dashboardLists.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return dashboardLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (dashboardLists.get(position).getIsGroupVideo()) {
            return LAYOUT_GROUP_FEED;
        } else {
            return LAYOUT_FEED;
        }
    }


    interface OnItemClick {
        void onClickAddComment(int i);

        void likeDisplay(int i);

        void displayAllComments(int i);

        void displayComment(int i);

        void onLikeButtonClick(GetFeaturesProfileModel.DashboardList dashboardLists, int i);

        void onClickShareIcon();

        void onClickOptionIcon();

        void onClickUsername(int i);

        void onClickProfile(int i);

        void onClickSmallProfile(int i);

        void onClickSmallUsername(int i);
    }
}
