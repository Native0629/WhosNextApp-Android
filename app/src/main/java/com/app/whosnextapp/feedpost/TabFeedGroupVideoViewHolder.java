package com.app.whosnextapp.feedpost;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.drawer.ViewCommentAdapter;
import com.app.whosnextapp.model.GetAllPostByCustomerIDModel;
import com.app.whosnextapp.model.GroupVideoModel;
import com.app.whosnextapp.model.PostCommentListModel;
import com.app.whosnextapp.utility.Globals;
import com.app.whosnextapp.utility.TimeUtils;
import com.bumptech.glide.Glide;
import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerDispatcher;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;
import im.ene.toro.widget.PressablePlayerSelector;

public class TabFeedGroupVideoViewHolder extends RecyclerView.ViewHolder
        implements ToroPlayer, ToroPlayer.EventListener, TabFeedGroupVideoAdapter.OnGroupVideoClickListener, View.OnClickListener {

    /*TopBar*/
    @BindView(R.id.profile_image)
    CircleImageView profile_image;
    @BindView(R.id.tv_username)
    AppCompatTextView tv_username;
    @BindView(R.id.tv_time)
    AppCompatTextView tv_time;
    @BindView(R.id.tv_total_view)
    AppCompatTextView tv_total_view;
    @BindView(R.id.iv_group_video_thumbnail)
    AppCompatImageView iv_group_video_thumbnail;
    @BindView(R.id.rv_group_video)
    RecyclerView rv_group_video;
    @BindView(R.id.btn_likes)
    AppCompatCheckBox btn_likes;
    @BindView(R.id.tv_likeCount)
    AppCompatTextView tv_likeCount;
    @BindView(R.id.iv_mike)
    AppCompatImageView iv_mike;
    @BindView(R.id.iv_shared)
    AppCompatImageView iv_shared;
    @BindView(R.id.iv_option)
    AppCompatImageView iv_option;
    @BindView(R.id.tv_likes)
    AppCompatTextView tv_likes;
    /*Comments*/
    @BindView(R.id.rv_comment)
    RecyclerView rv_comment;
    @BindView(R.id.tv_view_all_comments)
    AppCompatTextView tv_view_all_comments;
    @BindView(R.id.tv_addComment)
    AppCompatTextView tv_addComment;
    @BindView(R.id.fl_view)
    FrameLayout fl_view;
    @BindView(R.id.player)
    PlayerView playerView;

    private TabFeedGroupVideoAdapter tabFeedGroupVideoAdapter;
    private ExoPlayerViewHelper helper;
    private Player.EventListener eventListener;
    private ArrayList<Uri> mediaUris;
    private int video_width;
    private int video_height;
    private String TAG = TabFeedGroupVideoViewHolder.class.getSimpleName();
    private Context context;
    private ArrayList<GroupVideoModel> groupVideoModels;
    private TabFeedAdapter.OnItemClick onItemClick;
    private GetAllPostByCustomerIDModel.PostList postList;
    private ViewCommentAdapter viewCommentAdapter;
    private Activity activity;

    TabFeedGroupVideoViewHolder(Activity activity, Context context, View itemView, PressablePlayerSelector selector, TabFeedAdapter.OnItemClick onItemClick) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.onItemClick = onItemClick;
        this.context = context;
        this.activity = activity;
        if (selector != null) {
            playerView.setControlDispatcher(new ExoPlayerDispatcher(selector, this));
            playerView.removeView(iv_group_video_thumbnail);
            playerView.getOverlayFrameLayout().addView(iv_group_video_thumbnail);
        }
    }

    @NonNull
    @Override
    public View getPlayerView() {
        return playerView;
    }

    @NonNull
    @Override
    public PlaybackInfo getCurrentPlaybackInfo() {
        return helper != null ? helper.getLatestPlaybackInfo() : new PlaybackInfo();
    }

    @Override
    public void initialize(@NonNull Container container, @NonNull PlaybackInfo playbackInfo) {
        if (helper == null) {
            helper = new ExoPlayerViewHelper(this, Uri.parse(""), mediaUris);
            helper.addPlayerEventListener(this);
        }
        helper.initialize(container, playbackInfo);

        if (eventListener == null) {
            eventListener = new ExoPlayer.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                }

                @Override
                public void onLoadingChanged(boolean isLoading) {
                    Log.e(TAG, "onLoadingChanged");
                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {
                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                }

                @Override
                public void onPositionDiscontinuity(int reason) {
                    int p = playerView.getPlayer().getCurrentWindowIndex();
                    if (p >= mediaUris.size()) {
                        p = p % mediaUris.size();
                    }
                    setSelector(p);
                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                }

                @Override
                public void onSeekProcessed() {
                }
            };
        }
    }

    @Override
    public void play() {
        if (helper != null) helper.play();
        int p = playerView.getPlayer().getCurrentWindowIndex();
        if (p >= mediaUris.size()) {
            p = p % mediaUris.size();
        }
        setSelector(p);
        if (playerView.getPlayer() != null) {
            playerView.getPlayer().addListener(eventListener);
        }
    }

    private void setSelector(int pos) {
        for (int i = 0; i < groupVideoModels.size(); i++) {
            if (groupVideoModels.get(i).isSelected) {
                groupVideoModels.get(i).isSelected = false;
                tabFeedGroupVideoAdapter.notifyItemChanged(i);
            }
        }
        groupVideoModels.get(pos).isSelected = true;
        tabFeedGroupVideoAdapter.notifyItemChanged(pos);
    }

    @Override
    public void pause() {
        if (helper != null) helper.pause();
    }

    @Override
    public boolean isPlaying() {
        return helper != null && helper.isPlaying();
    }

    @Override
    public void release() {
        if (playerView.getPlayer() != null) {
            playerView.getPlayer().removeListener(eventListener);
        }
        if (helper != null) {
            helper.removePlayerEventListener(this);
            helper.release();
            helper = null;
        }
        this.eventListener = null;
    }

    @Override
    public boolean wantsToPlay() {
        return ToroUtil.visibleAreaOffset(this, itemView.getParent()) >= 0.85;
    }

    @Override
    public int getPlayerOrder() {
        return getAdapterPosition();
    }

    @Override
    public void onFirstFrameRendered() {
        iv_group_video_thumbnail.setVisibility(View.GONE);
    }

    @Override
    public void onBuffering() {
    }

    @Override
    public void onPlaying() {
        iv_group_video_thumbnail.setVisibility(View.GONE);
    }

    @Override
    public void onPaused() {
        iv_group_video_thumbnail.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCompleted() {
    }

    public void bind(GetAllPostByCustomerIDModel.PostList postListSingleData) {
        postList = postListSingleData;
        mediaUris = new ArrayList<>();
        groupVideoModels = postListSingleData.getGroupVideoList();
        tabFeedGroupVideoAdapter = new TabFeedGroupVideoAdapter(context, groupVideoModels, this);

        for (int p = 0; p < groupVideoModels.size(); p++) {
            HttpProxyCacheServer proxy = Globals.getProxy(context);
            String proxyUrl = proxy.getProxyUrl(groupVideoModels.get(p).getPostUrl());
            mediaUris.add(Uri.parse(proxyUrl));
        }

        if (postList.getPostCommentList().isEmpty()) {
            tv_view_all_comments.setVisibility(View.GONE);
        } else {
            tv_view_all_comments.setVisibility(View.VISIBLE);
        }

        this.video_height = groupVideoModels.get(0).getPostHeight();
        this.video_width = groupVideoModels.get(0).getPostWidth();

        setItemVideoHeight();

        Glide.with(context).load(postListSingleData.getProfilePicUrl()).into(profile_image);
        tv_username.setText(postListSingleData.getUserName());
        tv_time.setText(TimeUtils.getTimeDiff(context, Long.parseLong(postListSingleData.getTimeDiff())));
        tv_total_view.setText(String.format(context.getString(R.string.text_views), postListSingleData.getTotalViews()));

        btn_likes.setChecked(postListSingleData.getIsLiked());
        tv_likeCount.setText(String.valueOf(postListSingleData.getTotalLikes()));

        /*Video Thumbnail*/
        Glide.with(context).load(groupVideoModels.get(0).getVideoThumbnailUrl()).thumbnail(0.15f).into(iv_group_video_thumbnail);

        rv_group_video.setHasFixedSize(true);
        rv_group_video.setItemAnimator(null);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 5);
        rv_group_video.setLayoutManager(gridLayoutManager);
        rv_group_video.setAdapter(tabFeedGroupVideoAdapter);

        setCommentData(postListSingleData.getPostCommentList());

        profile_image.setOnClickListener(this);
        tv_username.setOnClickListener(this);
        btn_likes.setOnClickListener(this);
        iv_mike.setOnClickListener(this);
        iv_shared.setOnClickListener(this);
        iv_option.setOnClickListener(this);
        tv_likes.setOnClickListener(this);
        tv_view_all_comments.setOnClickListener(this);
        tv_addComment.setOnClickListener(this);
    }

    private void setItemVideoHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int req_height = (video_height * width) / video_width;
        fl_view.setLayoutParams(new LinearLayout.LayoutParams(width, req_height));
    }

    @Override
    public void onUserVideoClick(int position) {
        Glide.with(context).load(groupVideoModels.get(position).getVideoThumbnailUrl()).thumbnail(0.15f).into(iv_group_video_thumbnail);
        iv_group_video_thumbnail.setVisibility(View.VISIBLE);
        playerView.getPlayer().seekTo(position, C.TIME_UNSET);
    }

    private void setCommentData(ArrayList<PostCommentListModel.PostCommentList> commentData) {
        if (viewCommentAdapter == null) {
            viewCommentAdapter = new ViewCommentAdapter(context);
        }
        if (commentData != null) {
            //Collections.reverse(commentData);
            ArrayList<PostCommentListModel.PostCommentList> post = new ArrayList<>();

            if (commentData.size() > 2) {
                for (int i = 0; i < 2; i++) {
                    post.add(commentData.get(i));
                }
            } else {
                post.addAll(commentData);
            }
            if (rv_comment.getAdapter() == null) {
                rv_comment.setHasFixedSize(false);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                rv_comment.setLayoutManager(layoutManager);
                rv_comment.setAdapter(viewCommentAdapter);
            }
            viewCommentAdapter.doRefresh(post);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_image:
                if (onItemClick != null)
                    onItemClick.onClickProfile(getAdapterPosition());
                break;
            case R.id.tv_username:
                if (onItemClick != null)
                    onItemClick.onClickUsername(getAdapterPosition());
                break;
            case R.id.btn_likes:
                if (onItemClick != null)
                    onItemClick.onClickLike(postList, getAdapterPosition());
                break;
            case R.id.iv_mike:
                if (onItemClick != null)
                    onItemClick.displayComment(getAdapterPosition());
                break;
            case R.id.iv_shared:
                Globals.showToast(context, context.getString(R.string.text_coming_soon));
                break;
            case R.id.iv_option:
                Globals.showToast(context, context.getString(R.string.text_coming_soon));
                break;
            case R.id.tv_likes:
                if (onItemClick != null)
                    onItemClick.likeDisplay(getAdapterPosition());
                break;
            case R.id.tv_view_all_comments:
                if (onItemClick != null)
                    onItemClick.displayAllComments(getAdapterPosition());
                break;
            case R.id.tv_addComment:
                if (onItemClick != null)
                    onItemClick.onClickAddComment(getAdapterPosition());
                break;
            case R.id.tv_name:
                if (onItemClick != null)
                    onItemClick.onClickSmallUsername(getAdapterPosition());
                break;
            case R.id.pro_image:
                if (onItemClick != null)
                    onItemClick.onClickSmallProfile(getAdapterPosition());
                break;
        }
    }
}