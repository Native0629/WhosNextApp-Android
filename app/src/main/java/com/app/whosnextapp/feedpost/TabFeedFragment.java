package com.app.whosnextapp.feedpost;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.drawer.HeaderAllPostActivity;
import com.app.whosnextapp.drawer.homepage.SharePostActivity;
import com.app.whosnextapp.model.GetAllPostByCustomerIDModel;
import com.app.whosnextapp.model.LikeUnlikePostModel;
import com.app.whosnextapp.navigationmenu.OtherUserProfileActivity;
import com.app.whosnextapp.notification.NotificationActivity;
import com.app.whosnextapp.notification.ProfileVideoPlayerActivity;
import com.app.whosnextapp.pictures.CommentActivity;
import com.app.whosnextapp.pictures.LikePostActivity;
import com.app.whosnextapp.utility.BaseFragment;
import com.app.whosnextapp.utility.ConstantEnum;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.toro.widget.Container;
import im.ene.toro.widget.PressablePlayerSelector;

public class TabFeedFragment extends BaseFragment implements TabFeedAdapter.OnItemClick {
    @BindView(R.id.player_container)
    Container container;

    PressablePlayerSelector selector;
    Globals globals;
    TabFeedAdapter tabFeedAdapter;
    int likeCount, customerId, IsLike;
    String requestURL;
    boolean isLoaderRequire = true;

    private ArrayList<GetAllPostByCustomerIDModel.PostList> postLists = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_feed, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        if (getActivity() != null) {
            globals = (Globals) getActivity().getApplicationContext();
            if (getActivity().getIntent() != null && getActivity().getIntent().hasExtra(Constants.WN_CUSTOMER_ID)) {
                customerId = getActivity().getIntent().getIntExtra(Constants.WN_CUSTOMER_ID, 0);
                requestURL = String.format(getString(R.string.get_all_post_by_other_customerId), 1, customerId);
            } else {
                requestURL = String.format(getString(R.string.get_all_postBy_CustomerID), 1);
            }
            doRequestForGetFeed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isLoaderRequire = false;
        doRequestForGetFeed();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_page_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.header_bell).setVisible(true);
        menu.findItem(R.id.header_refresh).setVisible(true);
        menu.findItem(R.id.header_search).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.header_refresh:
                doRequestForGetFeed();
                break;
            case R.id.header_bell:
                startActivity(new Intent(getContext(), NotificationActivity.class));
                break;
        }
        return false;
    }

    private void doRequestForGetFeed() {
        new PostRequest(getActivity(), null, requestURL, isLoaderRequire, isLoaderRequire, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                GetAllPostByCustomerIDModel getAllPostByCustomerIDModel = new Gson().fromJson(response, GetAllPostByCustomerIDModel.class);
                if (getAllPostByCustomerIDModel != null && getAllPostByCustomerIDModel.getPostLists() != null && !getAllPostByCustomerIDModel.getPostLists().isEmpty()) {
                    postLists = getAllPostByCustomerIDModel.getPostLists();
                    setFeedPostAdapter();
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {

            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    private void setFeedPostAdapter() {
        if (tabFeedAdapter == null && getContext() != null) {
            selector = new PressablePlayerSelector(container);
            tabFeedAdapter = new TabFeedAdapter(getActivity(), getContext(), this, selector);
        }
        if (container.getAdapter() == null) {
            container.setHasFixedSize(false);
            container.setLayoutManager(new LinearLayoutManager(getActivity()));
            container.setPlayerSelector(selector);
            container.setAdapter(tabFeedAdapter);
        }
        tabFeedAdapter.doRefresh(postLists);
    }

    @Override
    public void onClickShareIcon(int i) {
        String postUrl;
        if (postLists.get(i).getIsImage()) {
            postUrl = postLists.get(i).getPostUrl();
        } else {
            postUrl = postLists.get(i).getVideoThumbnailUrl();
        }
        Intent intent = new Intent(getContext(), SharePostActivity.class);
        intent.putExtra(Constants.WN_PICTURE_PATH, postUrl);
        intent.putExtra(Constants.WN_POST_ID, String.valueOf(postLists.get(i).getId()));
        intent.putExtra(Constants.WN_Post_Type, ConstantEnum.PostType.NORMAL.getId());
        startActivity(intent);
    }

    @Override
    public void onClickOptionIcon(int i) {
        if (globals.getUserDetails().getStatus().getCustomerId().equalsIgnoreCase(String.valueOf(postLists.get(i).getCustomerId()))) {
            showEditAndDeleteDialog(i);
        } else {
            showTurnOnAndOffNotification(i);
        }
    }

    private void showEditAndDeleteDialog(int i) {
        CharSequence[] options = new CharSequence[]{getString(R.string.text_edit_post), getString(R.string.text_delete_post)};
        globals.show_dialog(getContext(), getString(R.string.text_select_option),
                "", options, true,
                (DialogInterface dialog, int item) -> {
                    switch (item) {
                        case 0:
                            Globals.showToast(getActivity(), getString(R.string.text_coming_soon));
                            break;
                        case 1:
                            doRequestForDeletePost(i);
                            break;
                    }
                });
    }

    private void doRequestForDeletePost(int i) {
        String requestUrl = String.format(getString(R.string.delete_post), postLists.get(i).getId());
        new PostRequest(getActivity(), null, requestUrl, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                LikeUnlikePostModel likeUnlikePostModel = new Gson().fromJson(response, LikeUnlikePostModel.class);
                if (likeUnlikePostModel.getResult().equalsIgnoreCase(Constants.WN_SUCCESS)) {
                    startActivity(new Intent(getActivity(), HeaderAllPostActivity.class));
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {

            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    private void showTurnOnAndOffNotification(int i) {
        String option;
        int status;
        if (!postLists.get(i).getNotificationStatus()) {
            option = getString(R.string.text_turn_on_notification);
            status = 1;
        } else {
            option = getString(R.string.text_turn_off_notification);
            status = 0;
        }
        CharSequence[] options = new CharSequence[]{option};
        globals.show_dialog(getContext(), getString(R.string.text_select_option),
                "", options, true,
                (dialog, item) -> doRequestToTurnOnAndOffNotification(status, postLists.get(i).getId()));
    }

    private void doRequestToTurnOnAndOffNotification(int status, int postId) {
        String requestURL = String.format(getString(R.string.turn_on_and_off_notification), postId, status);
        new PostRequest(getActivity(), null, requestURL, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                LikeUnlikePostModel likeUnlikePostModel = new Gson().fromJson(response, LikeUnlikePostModel.class);
                if (likeUnlikePostModel.getResult().equalsIgnoreCase(Constants.WN_SUCCESS)) {
                    startActivity(new Intent(getActivity(), HeaderAllPostActivity.class));
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {

            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }


    @Override
    public void likeDisplay(int i) {
        Intent intent = new Intent(getContext(), LikePostActivity.class);
        intent.putExtra(Constants.WN_POST_ID, String.valueOf(postLists.get(i).getId()));
        startActivity(intent);
    }

    @Override
    public void displayAllComments(int i) {
        navigateToComment(i);
    }

    @Override
    public void onClickAddComment(int i) {
        navigateToComment(i);
    }

    @Override
    public void displayComment(int i) {
        navigateToComment(i);
    }

    public void navigateToComment(int i) {
        Intent intent = new Intent(getContext(), CommentActivity.class);
        intent.putExtra(Constants.WN_POST_ID, String.valueOf(postLists.get(i).getId()));
        intent.putExtra(Constants.WN_CUSTOMER_ID, postLists.get(i).getCustomerId());
        startActivity(intent);
    }

    @Override
    public void onClickUsername(int i) {
        navigateToProfile(i);
    }

    @Override
    public void onClickSmallUsername(int i) {
        navigateToProfile(i);
    }

    private void navigateToProfile(int i) {
        Intent intent = new Intent(getContext(), OtherUserProfileActivity.class);
        intent.putExtra(Constants.WN_CUSTOMER_ID, postLists.get(i).getCustomerId());
        startActivity(intent);
    }

    @Override
    public void onClickProfile(int i) {
        navigateToProfileVideo(i);
    }

    @Override
    public void onClickSmallProfile(int i) {
        navigateToProfileVideo(i);
    }

    private void navigateToProfileVideo(int i) {
        Intent intent = new Intent(getContext(), ProfileVideoPlayerActivity.class);
        intent.putExtra(Constants.WN_CUSTOMER_ID, postLists.get(i).getCustomerId());
        startActivity(intent);
    }

   /* @Override
    public void requestForView(final GetAllPostByCustomerIDModel.PostList postList, int i) {
        String requestURL = String.format(getString(R.string.view_post), postList.getCustomerId());
        new PostRequest(getActivity(), null, requestURL, false, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                ViewPostModel viewPostModel = new Gson().fromJson(response, ViewPostModel.class);
                if (viewPostModel != null) {
                    if (viewPostModel.getResult().getResult()) {
                        postList.setTotalViews(viewPostModel.getResult().getTotalViews());
                    }
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {

            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }*/

    @Override
    public void onClickLike(final GetAllPostByCustomerIDModel.PostList postList, final int i) {
        likeCount = postLists.get(i).getTotalLikes();
        if (postLists.get(i).getIsLiked() && likeCount != 0) {
            likeCount = likeCount - 1;
            IsLike = 0;
        } else {
            likeCount = likeCount + 1;
            IsLike = 1;
        }
        String requestURL = String.format(getString(R.string.like_unlike_post), String.valueOf(postLists.get(i).getId()), IsLike);
        new PostRequest(getActivity(), null, requestURL, false, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                LikeUnlikePostModel likeUnlikePostModel = new Gson().fromJson(response, LikeUnlikePostModel.class);
                if (likeUnlikePostModel != null) {
                    if (likeUnlikePostModel.getResult().equals(Constants.WN_SUCCESS)) {
                        postLists.get(i).setTotalLikes(likeCount);
                        postLists.get(i).setIsLiked(IsLike == 1);
                        tabFeedAdapter.notifyItemChanged(i, postLists.get(i));
                    }
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(getContext(), msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }
}


