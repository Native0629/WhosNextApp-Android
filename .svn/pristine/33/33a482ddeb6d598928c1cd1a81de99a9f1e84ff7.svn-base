package com.app.whosnextapp.featuredProfile;

import android.content.Context;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.drawer.ViewCommentAdapter;
import com.app.whosnextapp.model.GetFeaturesProfileModel;
import com.app.whosnextapp.model.PostCommentListModel;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.app.whosnextapp.utility.TimeUtils;
import com.bumptech.glide.Glide;
import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerDispatcher;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.helper.ToroPlayerHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;
import im.ene.toro.widget.PressablePlayerSelector;

public class FeaturedProfileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ToroPlayer, ToroPlayer.EventListener {
    @BindView(R.id.profile_image)
    CircleImageView profile_image;
    @BindView(R.id.tv_username)
    AppCompatTextView tv_username;
    @BindView(R.id.tv_time)
    AppCompatTextView tv_time;
    @BindView(R.id.tv_name)
    AppCompatTextView tv_name;
    @BindView(R.id.iv_feed_post)
    AppCompatImageView iv_feed_post;
    @BindView(R.id.player)
    PlayerView playerView;
    @BindView(R.id.Image_view)
    AppCompatImageView Image_view;
    @BindView(R.id.fl_feed_image)
    FrameLayout fl_feed_image;
    @BindView(R.id.fl_feed_video)
    FrameLayout fl_feed_video;
    /*BottomLine*/
    @BindView(R.id.btn_likes)
    AppCompatCheckBox btn_likes;
    @BindView(R.id.tv_likeCount)
    AppCompatTextView tv_likeCount;
    @BindView(R.id.tv_total_view)
    AppCompatTextView tv_total_view;
    @BindView(R.id.iv_mike)
    AppCompatImageView iv_mike;
    @BindView(R.id.iv_shared)
    AppCompatImageView iv_shared;
    @BindView(R.id.iv_option)
    AppCompatImageView iv_option;
    @BindView(R.id.tv_likes)
    AppCompatTextView tv_likes;
    @BindView(R.id.pro_image)
    CircleImageView pro_image;

    /*Comments*/
    @BindView(R.id.rv_comment)
    RecyclerView rv_comment;
    @BindView(R.id.tv_view_all_comments)
    AppCompatTextView tv_view_all_comments;
    @BindView(R.id.tv_addComment)
    AppCompatTextView tv_addComment;
    private ToroPlayerHelper helper;
    private Uri mediaUri;
    private Context context;
    private FeaturedProfileAdapter.OnItemClick onItemClick;
    private GetFeaturesProfileModel.DashboardList dashboardLists;
    private ViewCommentAdapter viewCommentAdapter;

    FeaturedProfileViewHolder(Context context, View itemView, PressablePlayerSelector selector, FeaturedProfileAdapter.OnItemClick onItemClick) {
        super(itemView);
        this.onItemClick = onItemClick;
        ButterKnife.bind(this, itemView);
        this.context = context;
        if (selector != null) {
            playerView.setControlDispatcher(new ExoPlayerDispatcher(selector, this));
            playerView.removeView(Image_view);
            playerView.getOverlayFrameLayout().addView(Image_view);
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
            helper = new ExoPlayerViewHelper(this, mediaUri, new ArrayList<>());
            helper.addPlayerEventListener(this);
        }
        helper.initialize(container, playbackInfo);
    }

    @Override
    public void play() {
        if (helper != null) helper.play();

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
        if (helper != null) {
            helper.removePlayerEventListener(this);
            helper.release();
            helper = null;
        }
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
        Image_view.setVisibility(View.GONE);
    }

    @Override
    public void onBuffering() {

    }

    @Override
    public void onPlaying() {
        Image_view.setVisibility(View.GONE);
    }

    @Override
    public void onPaused() {
        Image_view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCompleted() {

    }

    public void bind(GetFeaturesProfileModel.DashboardList dashboardList) {
        dashboardLists = dashboardList;
        if (dashboardList.getPostUrl() != null) {
            HttpProxyCacheServer proxy = Globals.getProxy(context);
            String proxyUrl = proxy.getProxyUrl(dashboardList.getPostUrl());
            this.mediaUri = Uri.parse(proxyUrl);
        } else {
            this.mediaUri = Uri.parse("");
        }

        Glide.with(context).load(dashboardList.getProfilePicUrl()).into(profile_image);
        tv_username.setText(dashboardList.getUserName());
        tv_time.setText(TimeUtils.getTimeDiff(context, Long.parseLong(dashboardList.getTimeDiff())));

        /*Video Thumbnail*/
        Glide.with(context).load(dashboardList.getVideoThumbnailUrl()).thumbnail(0.15f).into(Image_view);

        if (dashboardList.getIsImage()) {
            Glide.with(context).load(dashboardList.getPostUrl()).into(iv_feed_post);
            fl_feed_image.setVisibility(View.VISIBLE);
            fl_feed_video.setVisibility(View.GONE);
        } else if (dashboardList.getIsVideo()) {
            fl_feed_image.setVisibility(View.GONE);
            fl_feed_video.setVisibility(View.VISIBLE);
        }

        btn_likes.setChecked(dashboardList.getIsLiked());
        tv_likeCount.setText(String.valueOf(dashboardList.getTotalLikes()));
        tv_total_view.setText(String.format(context.getString(R.string.text_views), dashboardList.getTotalViews()));
        Glide.with(context).load(dashboardList.getProfilePicUrl()).into(pro_image);

        String s = dashboardList.getUserName() + " " + dashboardList.getCaption();
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.sky)), 0, dashboardList.getUserName().length(), 0);
        tv_name.setText(ss1);

        setCommentData(dashboardList.getCommentList());

        /*Click Listeners*/
        profile_image.setOnClickListener(this);
        tv_username.setOnClickListener(this);
        iv_mike.setOnClickListener(this);
        iv_shared.setOnClickListener(this);
        iv_option.setOnClickListener(this);
        tv_likes.setOnClickListener(this);
        tv_view_all_comments.setOnClickListener(this);
        tv_addComment.setOnClickListener(this);
        btn_likes.setOnClickListener(this);
        pro_image.setOnClickListener(this);
        tv_name.setOnClickListener(this);
    }

    private void setCommentData(ArrayList<PostCommentListModel.PostCommentList> commentData) {
        if (viewCommentAdapter == null) {
            viewCommentAdapter = new ViewCommentAdapter(context, Constants.WN_POST_TYPE_Featured_Profile);
        }
        if (commentData != null) {
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
            case R.id.iv_mike:
                if (onItemClick != null)
                    onItemClick.displayComment(getAdapterPosition());
                break;
            case R.id.iv_shared:
                if (onItemClick != null)
                    onItemClick.onClickShareIcon();
                break;
            case R.id.iv_option:
                if (onItemClick != null)
                    onItemClick.onClickOptionIcon();
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
            case R.id.pro_image:
                if (onItemClick != null)
                    onItemClick.onClickSmallProfile(getAdapterPosition());
                break;
            case R.id.tv_name:
                if (onItemClick != null)
                    onItemClick.onClickSmallUsername(getAdapterPosition());
                break;
            case R.id.btn_likes:
                if (onItemClick != null)
                    onItemClick.onLikeButtonClick(dashboardLists, getAdapterPosition());
                break;
        }
    }
}
