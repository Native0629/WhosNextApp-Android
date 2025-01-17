package com.app.whosnextapp.pictures;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.breastCancerLegacies.AddNewActivity;
import com.app.whosnextapp.drawer.HomePageActivity;
import com.app.whosnextapp.drawer.MyProfileFragment;
import com.app.whosnextapp.drawer.ViewCommentAdapter;
import com.app.whosnextapp.drawer.homepage.EditShareToPictureActivity;
import com.app.whosnextapp.drawer.homepage.SharePostActivity;
import com.app.whosnextapp.model.LikeUnlikePostModel;
import com.app.whosnextapp.model.PostCommentListModel;
import com.app.whosnextapp.model.PostDetailsModel;
import com.app.whosnextapp.navigationmenu.OtherUserProfileActivity;
import com.app.whosnextapp.notification.NotificationActivity;
import com.app.whosnextapp.notification.ProfileVideoPlayerActivity;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.ConstantEnum;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.app.whosnextapp.utility.TimeUtils;
import com.bumptech.glide.Glide;
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
import com.google.gson.Gson;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.helper.ToroPlayerHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;

public class SelectImageActivity extends BaseAppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;
    @BindView(R.id.iv_selectImage)
    AppCompatImageView iv_selectImage;
    @BindView(R.id.tv_username)
    AppCompatTextView tv_username;
    @BindView(R.id.tv_time)
    AppCompatTextView tv_time;
    @BindView(R.id.profile_image)
    CircleImageView profile_image;
    @BindView(R.id.tv_likeCount)
    AppCompatTextView tv_likeCount;
    @BindView(R.id.tv_total_view)
    AppCompatTextView tv_total_view;
    @BindView(R.id.btn_likes)
    AppCompatCheckBox btn_likes;
    @BindView(R.id.rv_pictures)
    RecyclerView rv_pictures;
    @BindView(R.id.pro_image)
    CircleImageView pro_image;
    @BindView(R.id.tv_name)
    AppCompatTextView tv_name;
    @BindView(R.id.lin_viewAllComment)
    LinearLayout lin_view_allComment;
    @BindView(R.id.exoplayer)
    PlayerView player;
    @BindView(R.id.fl_select_video)
    FrameLayout fl_select_video;

    Globals globals;
    DataSource.Factory dataSourceFactory;
    SimpleExoPlayer exoPlayer;
    ViewCommentAdapter viewCommentAdapter;
    PostDetailsModel postDetailsModel;
    String customerID, postId;

    int likeCount;
    Boolean IsLikeValue;
    private Uri mediaUri;
    int width, height;

    private Activity getActivity() {
        return SelectImageActivity.this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        init();
    }

    public void init() {
        ButterKnife.bind(this);
        globals = (Globals) getApplicationContext();
        setActionbar();
        postId = getIntent().getStringExtra(Constants.WN_POST_ID);
        doRequestForPost();
        doRequestForDisplayComment();
    }


    @Override
    protected void onResume() {
        super.onResume();
        doRequestForPost();
        doRequestForDisplayComment();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.header_refresh:
                doRequestForPost();
                doRequestForDisplayComment();
                break;
            case R.id.header_bell:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
        }
        return false;


    }

    public void setActionbar() {
        toolbar.setBackgroundResource(R.drawable.header_bg);
        setSupportActionBar(toolbar);
        toolbar_title.setText(R.string.post);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        menu.findItem(R.id.header_refresh).setVisible(true);
        menu.findItem(R.id.header_bell).setVisible(true);
        final MenuItem menuItem = menu.findItem(R.id.header_bell);
        View actionView = MenuItemCompat.getActionView(menuItem);
        // tv_noitfication_count = actionView.findViewById(R.id.tv_noitfication_count);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @OnClick(R.id.iv_option)
    public void onShowOptionClick() {
        if (globals.getUserDetails().getStatus().getCustomerId().equalsIgnoreCase(String.valueOf(postDetailsModel.getPostDetail().getCustomerId()))) {
            showEditAndDeleteDialog(postDetailsModel.getPostDetail().getId());
        } else {
            showTurnOnAndOffNotification();
        }
    }


    private void showEditAndDeleteDialog(int i) {
        CharSequence[] options = new CharSequence[]{getString(R.string.text_edit_post), getString(R.string.text_delete_post)};
        globals.show_dialog(getActivity(), "",
                "", options, true,
                (DialogInterface dialog, int item) -> {
                    switch (item) {
                        case 0:
                            Intent intent = new Intent(getActivity(), EditShareToPictureActivity.class);
                            intent.putExtra(Constants.WN_PICTURE_PATH, postDetailsModel.getPostDetail().getPostUrl());
                            intent.putExtra(Constants.WN_POST_ID, postId);
                            intent.putExtra(Constants.CAPTION, postDetailsModel.getPostDetail().getCaption());
                            //intent.putExtra(Constants.WN_IS_IMAGE, postDetailsModel.getPostDetail().getIsImage());
                            intent.putExtra(Constants.WN_IS_IMAGE, postDetailsModel.getPostDetail().getIsImage() ? 1 : 0);
                            //intent.putExtra(Constants.WN_IS_VIDEO, postDetailsModel.getPostDetail().getIsVideo());
                            startActivity(intent);

                            break;
                        case 1:
                            doRequestForDeletePost(i);
                            break;
                    }
                });
    }

    private void doRequestForDeletePost(int i) {
        String requestUrl = String.format(getString(R.string.delete_post), i);
        new PostRequest(getActivity(), null, requestUrl, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                LikeUnlikePostModel likeUnlikePostModel = new Gson().fromJson(response, LikeUnlikePostModel.class);
                if (likeUnlikePostModel.getResult().equalsIgnoreCase(Constants.WN_SUCCESS)) {
                    startActivity(new Intent(getActivity(), HomePageActivity.class));
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {

            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    private void showTurnOnAndOffNotification() {
        String option;
        int status;
        if (!postDetailsModel.getPostDetail().getNotificationStatus()) {
            option = getString(R.string.text_turn_on_notification);
            status = 1;
        } else {
            option = getString(R.string.text_turn_off_notification);
            status = 0;
        }
        CharSequence[] options = new CharSequence[]{option};
        globals.show_dialog(getActivity(), "",
                "", options, true,
                (dialog, item) -> doRequestToTurnOnAndOffNotification(status, postDetailsModel.getPostDetail().getId()));
    }

    private void doRequestToTurnOnAndOffNotification(int status, int postId) {
        String requestURL = String.format(getString(R.string.turn_on_and_off_notification), postId, status);
        new PostRequest(getActivity(), null, requestURL, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                LikeUnlikePostModel likeUnlikePostModel = new Gson().fromJson(response, LikeUnlikePostModel.class);
                if (likeUnlikePostModel.getResult().equalsIgnoreCase(Constants.WN_SUCCESS)) {
                    startActivity(new Intent(getActivity(), HomePageActivity.class));
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {

            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    @OnClick(R.id.tv_username)
    public void onGoToProfileClick() {
//        Intent intent = new Intent(getActivity(), MyProfileFragment.class);
//        intent.putExtra(Constants.WN_CUSTOMER_ID, postDetailsModel.getPostDetail().getCustomerId());
//        startActivity(intent);

        Intent intent = new Intent(getActivity(), HomePageActivity.class);
        intent.putExtra(Constants.WN_CUSTOMER_ID, postDetailsModel.getPostDetail().getCustomerId());
        intent.putExtra(Constants.WN_START_FRAGMENT, Constants.WN_MY_PROFILE);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.profile_image)
    public void goToVideo() {
        Intent intent = new Intent(getActivity(), ProfileVideoPlayerActivity.class);
        intent.putExtra(Constants.WN_CUSTOMER_ID, postDetailsModel.getPostDetail().getCustomerId());
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.tv_view_all_comments)
    public void viewComment() {
        Intent intent = new Intent(getActivity(), CommentActivity.class);
        intent.putExtra(Constants.WN_POST_ID, postId);
        intent.putExtra(Constants.WN_CUSTOMER_ID, customerID);
        startActivity(intent);
    }

    @OnClick(R.id.iv_shared)
    public void onSharedClick() {
        String path;
        if (postDetailsModel.getPostDetail().getIsImage()) {
            path = postDetailsModel.getPostDetail().getPostUrl();
        } else {
            path = postDetailsModel.getPostDetail().getVideoThumbnailUrl();
        }
        Intent intent = new Intent(getActivity(), SharePostActivity.class);
        intent.putExtra(Constants.WN_PICTURE_PATH, path);
        intent.putExtra(Constants.WN_POST_ID, postId);
        intent.putExtra(Constants.WN_Post_Type, ConstantEnum.PostType.NORMAL.getId());
        startActivity(intent);
    }

    @OnClick(R.id.iv_mike)
    public void onMikeClick() {
        Intent intent = new Intent(getActivity(), CommentActivity.class);
        intent.putExtra(Constants.WN_POST_ID, postId);
        intent.putExtra(Constants.WN_CUSTOMER_ID, customerID);
        startActivity(intent);
    }

    @OnClick(R.id.tv_addComment)
    public void onAddCommentClick() {
        Intent intent = new Intent(getActivity(), CommentActivity.class);
        intent.putExtra(Constants.WN_POST_ID, postId);
        intent.putExtra(Constants.WN_CUSTOMER_ID, customerID);
        startActivity(intent);
    }

    @OnClick(R.id.tv_likes)
    public void OnShowLikesClick() {
        Intent intent = new Intent(getActivity(), LikePostActivity.class);
        intent.putExtra(Constants.WN_POST_ID, postId);
        startActivity(intent);
    }

    @OnClick(R.id.btn_likes)
    public void OnLikeUnlikeClick() {
        doRequestForLikeUnlikePost();
    }

    public void doRequestForLikeUnlikePost() {
        String postId = getIntent().getStringExtra(Constants.WN_POST_ID);
        final String requestURL = String.format(getString(R.string.like_unlike_post), postId, IsLikeValue ? 0 : 1);
        new PostRequest(getActivity(), null, requestURL, false, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                LikeUnlikePostModel likeUnlikePostModel = new Gson().fromJson(response, LikeUnlikePostModel.class);
                if (likeUnlikePostModel != null) {
                    if (likeUnlikePostModel.getResult().equals(Constants.WN_SUCCESS)) {
                        if (IsLikeValue) {
                            likeCount = likeCount - 1;
                        } else {
                            likeCount = likeCount + 1;
                        }
                        tv_likeCount.setText(String.valueOf(likeCount));
                        IsLikeValue = !IsLikeValue;
                    }
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(getActivity(), msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    public void doRequestForDisplayComment() {
        String postId = getIntent().getStringExtra(Constants.WN_POST_ID);
        final String requestURL = String.format(getString(R.string.get_all_comment_by_postId), postId, "1");
        new PostRequest(getActivity(), null, requestURL, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                PostCommentListModel postCommentListModel = new Gson().fromJson(response, PostCommentListModel.class);
                if (postCommentListModel != null && postCommentListModel.getPostCommentList() != null && !postCommentListModel.getPostCommentList().isEmpty()) {
                    setViewCommentAdapter(postCommentListModel.getPostCommentList());
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(getActivity(), msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    public void setViewCommentAdapter(ArrayList<PostCommentListModel.PostCommentList> posts) {
        if (viewCommentAdapter == null) {
            viewCommentAdapter = new ViewCommentAdapter(getActivity());
        }
        if (rv_pictures.getAdapter() == null) {
            rv_pictures.setHasFixedSize(true);
            rv_pictures.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv_pictures.setAdapter(viewCommentAdapter);
        }

        Collections.reverse(posts);
        ArrayList<PostCommentListModel.PostCommentList> post = new ArrayList<>();
        if (posts.size() == 0) {
            lin_view_allComment.setVisibility(View.GONE);
        } else if (posts.size() > 2) {
            lin_view_allComment.setVisibility(View.VISIBLE);
            for (int i = 0; i < 2; i++) {
                post.add(posts.get(i));
            }
        } else {
            post.addAll(posts);
        }
        viewCommentAdapter.doRefresh(post);
    }

    public void doRequestForPost() {
        final String requestURL = String.format(getString(R.string.get_postDetails_by_PostID), postId);
        new PostRequest(getActivity(), null, requestURL, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                postDetailsModel = new Gson().fromJson(response, PostDetailsModel.class);
                if (postDetailsModel != null) {
                    setPostData(postDetailsModel);
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(getActivity(), msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    private void setPostData(PostDetailsModel postDetailsModel) {
        Glide.with(getActivity())
                .load(postDetailsModel.getPostDetail().getProfilePicUrl())
                .into(profile_image);

        Glide.with(getActivity())
                .load(postDetailsModel.getPostDetail().getProfilePicUrl())
                .into(pro_image);

        if (postDetailsModel.getPostDetail().getPostCommentList().isEmpty()) {
            lin_view_allComment.setVisibility(View.GONE);
        } else {
            lin_view_allComment.setVisibility(View.VISIBLE);
        }
        tv_time.setText(TimeUtils.getTimeDiff(getActivity(), Long.parseLong(postDetailsModel.getPostDetail().getTimeDiff())));
        tv_total_view.setText(String.format(getString(R.string.text_views), postDetailsModel.getPostDetail().getTotalViews()));
        customerID = String.valueOf(postDetailsModel.getPostDetail().getCustomerId());
        likeCount = postDetailsModel.getPostDetail().getTotalLikes();
        tv_likeCount.setText(String.valueOf(likeCount));

        SpannableString ss1 = new SpannableString(postDetailsModel.getPostDetail().getUserName() + " " + StringEscapeUtils.unescapeJava(postDetailsModel.getPostDetail().getCaption()));
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), 0, (postDetailsModel.getPostDetail().getUserName().length()), 0);// set color
        tv_username.setText(postDetailsModel.getPostDetail().getUserName());
        tv_name.setText(ss1);
        IsLikeValue = postDetailsModel.getPostDetail().getIsLiked();
        btn_likes.setChecked(IsLikeValue);
        width = postDetailsModel.getPostDetail().getPostWidth();
        height = postDetailsModel.getPostDetail().getPostHeight();

        if (postDetailsModel.getPostDetail().getIsImage()) {
            player.setVisibility(View.GONE);
            iv_selectImage.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(postDetailsModel.getPostDetail().getPostUrl())
                    .into(iv_selectImage);

//            // ((display.getHeight()*30)/100)
//            FrameLayout.LayoutParams parms = new FrameLayout.LayoutParams(width, height);
//            iv_selectImage.setLayoutParams(parms);
        } else {
            iv_selectImage.setVisibility(View.GONE);
            player.setVisibility(View.VISIBLE);

            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
            exoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            player.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            player.setPlayer(exoPlayer);
            dataSourceFactory = new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), getString(R.string.app_name)), bandwidthMeter);

            mediaUri = Uri.parse(postDetailsModel.getPostDetail().getPostUrl());
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            LoopingMediaSource loopingMediaSource = new LoopingMediaSource(videoSource);
            exoPlayer.prepare(loopingMediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }


    @Override
    protected void onStop() {
        super.onStop();
//        exoPlayer.stop();
    }
}
