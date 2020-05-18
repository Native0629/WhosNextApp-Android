package com.app.whosnextapp.settings;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.AdCommentListModel;
import com.app.whosnextapp.model.GetAllAdsByCustomerIdModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.AdsViewHolder> {
    private Context context;
    private OnItemClick onItemClick;
    private ArrayList<GetAllAdsByCustomerIdModel.AdsList> adsLists;

    AdsAdapter(Context context, OnItemClick onItemClick) {
        this.context = context;
        this.onItemClick = onItemClick;
    }

    public void doRefresh(ArrayList<GetAllAdsByCustomerIdModel.AdsList> adsLists) {
        this.adsLists = adsLists;
        notifyDataSetChanged();
    }

    void setAdsLists(ArrayList<GetAllAdsByCustomerIdModel.AdsList> adsLists) {
        this.adsLists = adsLists;
    }

    @NonNull
    @Override
    public AdsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_all_ads_list_layout, viewGroup, false);
        return new AdsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdsViewHolder adsViewHolder, int i) {
        adsViewHolder.setData(adsLists.get(i), i);
        adsViewHolder.setViewCommentAdapter(adsLists.get(i).getAdCommentList());
    }

    @Override
    public void onBindViewHolder(@NonNull AdsViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            if (payloads.get(0) instanceof Integer) {
                holder.tv_likeCount.setText(String.valueOf(adsLists.get(position).getTotalLikes()));
                holder.btn_likes.setChecked(adsLists.get(position).getIsLiked());
            }
        } else
            super.onBindViewHolder(holder, position, payloads);

    }

    @Override
    public int getItemCount() {
        return adsLists.size();
    }

    interface OnItemClick {

        void viewAllComments(GetAllAdsByCustomerIdModel.AdsList adsLists, int position);

        void likeDisplay(int i);

        void onClickLike(int i);

        void onClickAddComment(int i);

        void onClickOptionMenu(int i);

        void doRequestForAdsPublish(int i);

        void onClickButton(int i);

    }

    class AdsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.profile_image)
        CircleImageView profile_image;
        @BindView(R.id.tv_username)
        AppCompatTextView tv_username;
        @BindView(R.id.tv_name)
        AppCompatTextView tv_name;
        @BindView(R.id.Image_view)
        AppCompatImageView Image_view;
        @BindView(R.id.pro_image)
        CircleImageView pro_image;
        @BindView(R.id.tv_likeCount)
        AppCompatTextView tv_likeCount;
        @BindView(R.id.btn_likes)
        AppCompatCheckBox btn_likes;
        @BindView(R.id.iv_mike)
        AppCompatImageView iv_mike;
        @BindView(R.id.tv_likes)
        AppCompatTextView tv_likes;
        @BindView(R.id.tv_addComment)
        AppCompatTextView tv_addComment;
        @BindView(R.id.tv_description)
        AppCompatTextView tv_description;
        @BindView(R.id.tv_button)
        AppCompatTextView tv_button;
        @BindView(R.id.iv_option)
        AppCompatImageView iv_option;
        @BindView(R.id.tv_publish)
        AppCompatTextView tv_publish;
        @BindView(R.id.tv_view_all_comments)
        AppCompatTextView tv_view_all_comments;
        @BindView(R.id.rv_comment)
        RecyclerView rv_comment;
        @BindView(R.id.ll_view_allComment)
        LinearLayout ll_view_allComment;

        ViewAdCommentAdapter viewAdCommentAdapter;
        @BindView(R.id.exoplayer)
        PlayerView player;
        DataSource.Factory dataSourceFactory;
        SimpleExoPlayer exoPlayer;

        AdsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(GetAllAdsByCustomerIdModel.AdsList ads, int i) {
            tv_username.setText(ads.getUserName());
            Glide.with(context).load(ads.getProfilePicUrl()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)).into(profile_image);
            Glide.with(context).load(ads.getProfilePicUrl()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)).into(pro_image);
            tv_name.setText(ads.getUserName());
            if (ads.getIsImage()) {
                player.setVisibility(View.GONE);
                if (ads.getImageList() != null && !ads.getImageList().isEmpty())
                    Glide.with(context).load(ads.getImageList().get(0).getImageUrl()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)).into(Image_view);
            } else {
                Image_view.setVisibility(View.GONE);

                DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
                TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
                exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
                exoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                player.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
                player.setPlayer(exoPlayer);

                dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, context.getString(R.string.app_name)), bandwidthMeter);

                MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(ads.getVideoUrl()));
                LoopingMediaSource loopingMediaSource = new LoopingMediaSource(videoSource);
                exoPlayer.prepare(loopingMediaSource);
                exoPlayer.setPlayWhenReady(true);
            }
            tv_description.setText(ads.getDescription());
            tv_likeCount.setText(String.valueOf(ads.getTotalLikes()));
            tv_button.setText(ads.getButtonName());
            btn_likes.setChecked(ads.getIsLiked());

            btn_likes.setOnClickListener(this);
            tv_likes.setOnClickListener(this);
            tv_addComment.setOnClickListener(this);
            tv_view_all_comments.setOnClickListener(this);
            iv_option.setOnClickListener(this);
            tv_publish.setOnClickListener(this);
            tv_button.setOnClickListener(this);
            onItemClick.viewAllComments(adsLists.get(getAdapterPosition()), i);
        }

        void setViewCommentAdapter(ArrayList<AdCommentListModel.AdCommentList> adCommentLists) {
            if (viewAdCommentAdapter == null) {
                viewAdCommentAdapter = new ViewAdCommentAdapter(context);
            }
            if (rv_comment.getAdapter() == null) {
                rv_comment.setHasFixedSize(false);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                rv_comment.setLayoutManager(layoutManager);
                rv_comment.setAdapter(viewAdCommentAdapter);
            }

            if (adCommentLists != null) {
                Collections.reverse(adCommentLists);

                ArrayList<AdCommentListModel.AdCommentList> comment = new ArrayList<>();
                if (adCommentLists.size() == 0) {
                    ll_view_allComment.setVisibility(View.GONE);
                } else if (adCommentLists.size() > 2) {
                    ll_view_allComment.setVisibility(View.VISIBLE);
                    for (int i = 0; i < 2; i++) {
                        comment.add(adCommentLists.get(i));
                    }
                } else {
                    ll_view_allComment.setVisibility(View.VISIBLE);
                    comment.addAll(adCommentLists);
                }
                viewAdCommentAdapter.doRefresh(comment);
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_likes:
                    onItemClick.onClickLike(getAdapterPosition());
                    break;

                case R.id.tv_likes:
                    onItemClick.likeDisplay(getAdapterPosition());
                    break;

                case R.id.tv_addComment:
                    onItemClick.onClickAddComment(getAdapterPosition());
                    break;

                case R.id.iv_option:
                    onItemClick.onClickOptionMenu(getAdapterPosition());
                    break;

                case R.id.tv_publish:
                    onItemClick.doRequestForAdsPublish(getAdapterPosition());
                    break;

                case R.id.tv_button:
                    onItemClick.onClickButton(getAdapterPosition());
                    break;

                case R.id.tv_view_all_comments:
                    onItemClick.onClickAddComment(getAdapterPosition());
                    break;
            }
        }
    }
}
