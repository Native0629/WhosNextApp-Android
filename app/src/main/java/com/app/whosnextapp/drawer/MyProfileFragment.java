package com.app.whosnextapp.drawer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.feedpost.AllPostAdapter;
import com.app.whosnextapp.loginregistration.LoginActivity;
import com.app.whosnextapp.model.GetAllPostByCustomerIDModel;
import com.app.whosnextapp.model.GetCustomerInfoModel;
import com.app.whosnextapp.model.GetCustomerProfileModel;
import com.app.whosnextapp.model.LikeUnlikePostModel;
import com.app.whosnextapp.navigationmenu.EditProfileActivity;
import com.app.whosnextapp.navigationmenu.FollowerActivity;
import com.app.whosnextapp.navigationmenu.FollowingActivity;
import com.app.whosnextapp.pictures.OtherSelectImageActivity;
import com.app.whosnextapp.pictures.SelectImageActivity;
import com.app.whosnextapp.pictures.SelectVideoActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyProfileFragment extends Fragment implements AllPostAdapter.OnItemClick {

    @BindView(R.id.tv_count_post)
    AppCompatTextView tv_count_post;
    @BindView(R.id.tv_username)
    AppCompatTextView tv_username;
    @BindView(R.id.tv_count_followers)
    AppCompatTextView tv_count_followers;
    @BindView(R.id.tv_count_following)
    AppCompatTextView tv_count_following;
    @BindView(R.id.tv_views)
    AppCompatTextView tv_views;
    @BindView(R.id.tv_about_self)
    AppCompatTextView tv_about_self;
    @BindView(R.id.tv_city)
    AppCompatTextView tv_city;
    @BindView(R.id.tv_category)
    AppCompatTextView tv_category;
    @BindView(R.id.checkbox)
    AppCompatCheckBox checkbox;
    @BindView(R.id.tv_website1)
    AppCompatTextView tv_website1;
    @BindView(R.id.tv_website2)
    AppCompatTextView tv_website2;
    @BindView(R.id.tv_website3)
    AppCompatTextView tv_website3;
    @BindView(R.id.tv_website4)
    AppCompatTextView tv_website4;
    @BindView(R.id.tv_website5)
    AppCompatTextView tv_website5;
    @BindView(R.id.exoplayer)
    PlayerView player;
    @BindView(R.id.bio_image)
    AppCompatImageView bio_image;
    @BindView(R.id.rv_pictures)
    RecyclerView rv_Pictures;
    @BindView(R.id.iv_volume)
    AppCompatImageView iv_volume;

    DataSource.Factory dataSourceFactory;
    SimpleExoPlayer exoPlayer;
    Boolean IsPrivate;
    Globals globals;
    String website;
    AllPostAdapter allPostAdapter;
    MediaMetadataRetriever retriever;
    boolean isLoaderRequire = true;
    GetCustomerProfileModel getCustomerProfileModel;
    private int video_width;
    private int video_height;
    Runnable mRunnable;
    Handler mHandler;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();

        mRunnable = () -> {
            iv_volume.setVisibility(View.GONE); //This will remove the View. and free s the space occupied by the View
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.myprofile_fragment, container, false);
        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        init();
        return v;
    }


    @OnClick(R.id.checkbox)
    public void onclickPrivate() {
        doRequestForProfile();
    }

    @OnClick(R.id.tv_edit_profile)
    public void onEditProfileClick() {
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        intent.putExtra(Constants.WN_GET_CUSTOMER_INFO_MODEL, getCustomerProfileModel);
        startActivity(intent);
    }

    @OnClick(R.id.tv_website1)
    public void onWebsiteClick() {

        Uri uri = Uri.parse(tv_website1.getText().toString().trim());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @OnClick(R.id.tv_website2)
    public void onWebsite2Click() {

        Uri uri = Uri.parse(tv_website2.getText().toString().trim());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @OnClick(R.id.tv_website3)
    public void onWebsite3Click() {

        Uri uri = Uri.parse(tv_website3.getText().toString().trim());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @OnClick(R.id.tv_website4)
    public void onWebsite4Click() {

        Uri uri = Uri.parse(tv_website4.getText().toString().trim());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @OnClick(R.id.tv_website5)
    public void onWebsite5Click() {

        Uri uri = Uri.parse(tv_website5.getText().toString().trim());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @OnClick(R.id.ll_Posts)
    public void onPostClick() {
        Intent intent = new Intent(getActivity(), HeaderAllPostActivity.class);
        startActivity(intent);
    }

    public void init() {
        if (getActivity() != null) {
            globals = (Globals) getActivity().getApplicationContext();
            tv_username.setText(globals.getUserDetails().getStatus().getUserName());
            doRequestForgetPost();
            retriever = new MediaMetadataRetriever();
        }
    }

    @OnClick(R.id.tv_de_active_account)
    public void AccountClick() {
        showDialog();
    }

    public void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(getResources().getString(R.string.de_active_your_profile));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                (arg0, arg1) -> doRequestForDeActiveAccount());
        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_page_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.header_refresh).setVisible(true);
        menu.findItem(R.id.header_bell).setVisible(false);
        menu.findItem(R.id.header_search).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.header_refresh) {
            doRequestForgetPost();
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isLoaderRequire = false;
        countPostsFollowerFollowing();
        doRequestForGetMyProfile();
    }

    @OnClick(R.id.fl_select_video)
    public void onSelectVideoClick() {
        if (exoPlayer.getVolume() == 0f) {
            exoPlayer.setVolume(1f);
            iv_volume.setImageResource(R.drawable.volume_unmute);
            iv_volume.setVisibility(View.VISIBLE);
            mHandler.removeCallbacks(mRunnable);
            mHandler.postDelayed(mRunnable, 1000);

        } else {
            exoPlayer.setVolume(0f);
            iv_volume.setImageResource(R.drawable.volume_mute);
            iv_volume.setVisibility(View.VISIBLE);
            mHandler.removeCallbacks(mRunnable);
            mHandler.postDelayed(mRunnable, 1000);

        }

    }

    public void countPostsFollowerFollowing() {
        new PostRequest(getActivity(), null, getString(R.string.get_CustomerInfo), isLoaderRequire, isLoaderRequire, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                GetCustomerInfoModel CustomerInfoModel = new Gson().fromJson(response, GetCustomerInfoModel.class);
                if (CustomerInfoModel != null) {
                    tv_count_post.setText(String.valueOf(CustomerInfoModel.getStatus().getTotalPost()));
                    tv_count_followers.setText(String.valueOf(CustomerInfoModel.getStatus().getTotalFollowers()));
                    tv_count_following.setText(String.valueOf(CustomerInfoModel.getStatus().getTotalFollowings()));
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(getActivity(), msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    //Display Customer profile

    private void doRequestForGetMyProfile() {
        new PostRequest(getActivity(), null, getString(R.string.get_customer_profile), true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                getCustomerProfileModel = new Gson().fromJson(response, GetCustomerProfileModel.class);
                if (getCustomerProfileModel != null) {
                    setData();
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(getActivity(), msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    private void setData() {


        tv_views.setText(String.valueOf(getCustomerProfileModel.getCustomerDetail().getTotalProfileVideoViews()));
        tv_about_self.setText(String.valueOf(getCustomerProfileModel.getCustomerDetail().getAboutSelf()));
        tv_city.setText(String.valueOf(getCustomerProfileModel.getCustomerDetail().getCity()));
        setCategory(getCustomerProfileModel.getCustomerDetail().getCategoryList(), getCustomerProfileModel.getCustomerDetail().getCategoryIds());
        checkbox.setChecked(Boolean.parseBoolean(String.valueOf(getCustomerProfileModel.getCustomerDetail().getIsPrivate())));
        website = getCustomerProfileModel.getCustomerDetail().getWebsite();


        // this.video_height = getCustomerProfileModel.getCustomerDetail().getPostHeight();
        //this.video_width = dashboardList.getPostWidth();


        Glide.with(getContext().getApplicationContext())
                .asBitmap()
                .load(getCustomerProfileModel.getCustomerDetail().getProfilePicUrl())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap,
                                                Transition<? super Bitmap> transition) {
                        video_width = bitmap.getWidth();
                        video_height = (int) (bitmap.getHeight() / 1.01);
                        setItemVideoHeight();
                        //bio_image.setImageBitmap(bitmap);
                    }
                });


        String videoUrl = getCustomerProfileModel.getCustomerDetail().getBioVideoUrl();


        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
        //  exoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        player.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        player.setPlayer(exoPlayer);
        dataSourceFactory = new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), getString(R.string.app_name)), bandwidthMeter);

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(videoUrl));
        LoopingMediaSource loopingMediaSource = new LoopingMediaSource(videoSource);
        exoPlayer.prepare(loopingMediaSource);
        exoPlayer.setVolume(0f);
        exoPlayer.setPlayWhenReady(true);

        AppCompatTextView[] websiteText = new AppCompatTextView[]{tv_website1, tv_website2, tv_website3, tv_website4, tv_website5};
        website = getCustomerProfileModel.getCustomerDetail().getWebsite();
        String[] separated = website.split(",");

        for (int i = 0; i < separated.length; i++) {
            websiteText[i].setText(separated[i]);
        }
    }


    private void setCategory(List<GetCustomerProfileModel.CategoryList> categoryList, String categoryIds) {
        if (categoryIds.contains(",")) {
            String[] catID = categoryIds.split(",");
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < catID.length; i++) {
                for (int j = 0; j < categoryList.size(); j++) {
                    if (catID[i].equalsIgnoreCase(String.valueOf(categoryList.get(j).getCategoryId()))) {
                        s.append(categoryList.get(j).getCategoryName());
                        if (i != catID.length - 1) {
                            s.append(",");
                        }
                        break;
                    }
                }
            }

            tv_category.setText(s.toString());
        } else {
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryIds.equalsIgnoreCase(String.valueOf(categoryList.get(i).getCategoryId()))) {
                    tv_category.setText(String.valueOf(categoryList.get(i).getCategoryName()));
                    break;
                }
            }
        }
    }

    private void setCommentAdapter(ArrayList<GetAllPostByCustomerIDModel.PostList> posts) {
        if (allPostAdapter == null) {
            allPostAdapter = new AllPostAdapter(getActivity(), this);
        }
        allPostAdapter.doRefresh(posts);
        if (rv_Pictures.getAdapter() == null) {
            rv_Pictures.setHasFixedSize(true);
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
            rv_Pictures.setLayoutManager(layoutManager);
            rv_Pictures.setAdapter(allPostAdapter);
        }
    }

    // DeActive Account

    private void doRequestForDeActiveAccount() {
        final String requestURL = String.format(getString(R.string.make_profile_private), 1, globals.getUserDetails().getStatus().getCustomerId());
        new PostRequest(getActivity(), null, requestURL, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                LikeUnlikePostModel likeUnlikePostModel = new Gson().fromJson(response, LikeUnlikePostModel.class);
                if (likeUnlikePostModel != null) {
                    if (likeUnlikePostModel.getResult().equals(Constants.WN_SUCCESS)) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {

            }
        }).execute(globals.getUserDetails().getStatus().getUserId());

    }

    public int getType() {
        IsPrivate = Boolean.parseBoolean(String.valueOf(getCustomerProfileModel.getCustomerDetail().getIsPrivate()));
        if (IsPrivate.equals(true)) {
            return 0;
        } else {
            return 1;
        }
    }

    public void doRequestForProfile() {
        final String requestURL = String.format(getString(R.string.make_profile_private), getType(), globals.getUserDetails().getStatus().getCustomerId());
        new PostRequest(getActivity(), null, requestURL, true, true, null).execute(globals.getUserDetails().getStatus().getUserId());
    }

    public void doRequestForgetPost() {
        final String requestURL = String.format(getString(R.string.get_all_postBy_CustomerID), 1);
        new PostRequest(getActivity(), null, requestURL, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                GetAllPostByCustomerIDModel getAllPostByCustomerIDModel = new Gson().fromJson(response, GetAllPostByCustomerIDModel.class);
                if (getAllPostByCustomerIDModel != null && getAllPostByCustomerIDModel.getPostLists() != null && !getAllPostByCustomerIDModel.getPostLists().isEmpty()) {
                    setCommentAdapter(getAllPostByCustomerIDModel.getPostLists());
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(getActivity(), msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }


    @OnClick(R.id.ll_followers)
    public void onFollowerClick() {
        Intent intent = new Intent(getContext(), FollowerActivity.class);
        intent.putExtra(Constants.WN_USER_ID, globals.getUserDetails().getStatus().getUserId());
        startActivity(intent);
    }

    @OnClick(R.id.ll_followings)
    public void onFollowingClick() {
        Intent intent = new Intent(getContext(), FollowingActivity.class);
        intent.putExtra(Constants.WN_USER_ID, globals.getUserDetails().getStatus().getUserId());
        startActivity(intent);
    }

    @Override
    public void selectImage(GetAllPostByCustomerIDModel.PostList postList) {
        if (postList.getIsImage()) {
            Intent intent = new Intent(getActivity(), SelectImageActivity.class);
            intent.putExtra(Constants.WN_POSTS_URL, postList.getPostUrl());
            intent.putExtra(Constants.WN_POST_ID, String.valueOf(postList.getPostId()));
            intent.putExtra(Constants.WN_IS_IMAGE, postList.getIsImage());
            intent.putExtra(Constants.WN_TOTAL_LIKES, postList.getTotalLikes());
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), SelectVideoActivity.class);
            intent.putExtra(Constants.WN_POSTS_URL, postList.getPostUrl());
            intent.putExtra(Constants.WN_POST_ID, String.valueOf(postList.getPostId()));
            intent.putExtra(Constants.WN_IS_IMAGE, postList.getIsImage());
            intent.putExtra(Constants.WN_TOTAL_LIKES, postList.getTotalLikes());
            startActivity(intent);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.getPlaybackState();
        }
    }

    @Override
    public void onStop() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
        super.onStop();
    }

    private void setItemVideoHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        (getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int req_height = ((video_height * width) / video_width);
        player.setLayoutParams(new FrameLayout.LayoutParams(width, req_height));
    }
}
