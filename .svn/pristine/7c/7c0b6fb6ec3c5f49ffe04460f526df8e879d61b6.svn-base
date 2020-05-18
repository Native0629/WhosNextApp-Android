package com.app.whosnextapp.breastCancerLegacies;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.drawer.homepage.SharePostActivity;
import com.app.whosnextapp.model.GetAllBreastCancerLegaciesModel;
import com.app.whosnextapp.model.GetAllCommentsByBreastCancerLegacyModel;
import com.app.whosnextapp.model.LikeUnlikePostModel;
import com.app.whosnextapp.notification.NotificationActivity;
import com.app.whosnextapp.pictures.MentionHelper;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.ConstantEnum;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseAppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;
    @BindView(R.id.tv_name)
    AppCompatTextView tv_name;
    @BindView(R.id.tv_description)
    AppCompatTextView tv_description;
    @BindView(R.id.rv_bcl_comment)
    RecyclerView rv_bcl_comment;
    @BindView(R.id.tv_total_comments)
    AppCompatTextView tv_total_comments;
    @BindView(R.id.tv_likeCount)
    AppCompatTextView tv_likeCount;
    @BindView(R.id.tv_addComment)
    AppCompatTextView tv_addComment;
    @BindView(R.id.iv_selectImage)
    AppCompatImageView iv_selectImage;
    @BindView(R.id.ll_view_allComment)
    LinearLayout ll_view_allComment;
    @BindView(R.id.btn_likes)
    AppCompatCheckBox btn_likes;
    @BindView(R.id.iv_mike)
    AppCompatImageView iv_mike;
    @BindView(R.id.iv_image)
    CircleImageView iv_image;
    @BindView(R.id.iv_flower)
    AppCompatImageView iv_flower;

    Globals globals;
    Boolean IsLikeValue;
    ArrayList<SpannableString> spannableStrings = new ArrayList<>();
    int likeCount;
    ViewAllBCLCommentAdapter viewAllBCLCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }

    public void init() {
        ButterKnife.bind(this);
        globals = (Globals) getApplicationContext();
        setActionbar();
        doRequestForPost();
        doRequestForDisplayComment();
    }

    public void setActionbar() {
        toolbar.setBackgroundResource(R.color.pink);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(this);
        iv_flower.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        menu.findItem(R.id.header_bell).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.header_bell) {
            startActivity(new Intent(this, NotificationActivity.class));
        }
        return false;
    }

    public void doRequestForPost() {
        String Legacy_Id = getIntent().getStringExtra(Constants.WN_BREAST_CANCER_LEGACY_ID);
        final String requestURL = String.format(getString(R.string.get_all_sub_BCL), Legacy_Id, "1");
        new PostRequest(this, null, requestURL, true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                GetAllBreastCancerLegaciesModel getAllBreastCancerLegaciesModel = new Gson().fromJson(response, GetAllBreastCancerLegaciesModel.class);
                if (getAllBreastCancerLegaciesModel != null && !getAllBreastCancerLegaciesModel.getBreastCancerLegaciesListArrayList().isEmpty()) {
                    setPostData(getAllBreastCancerLegaciesModel, 0);
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(ProfileActivity.this, msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    private void setPostData(GetAllBreastCancerLegaciesModel getAllBreastCancerLegaciesModel, int i) {
        toolbar_title.setText(getAllBreastCancerLegaciesModel.getBreastCancerLegaciesListArrayList().get(i).getName());
        tv_description.setText(getAllBreastCancerLegaciesModel.getBreastCancerLegaciesListArrayList().get(i).getDescription());
        Glide.with(ProfileActivity.this)
                .load(getAllBreastCancerLegaciesModel.getBreastCancerLegaciesListArrayList().get(i).getImageUrl())
                .into(iv_selectImage);
        tv_total_comments.setText(String.valueOf(getAllBreastCancerLegaciesModel.getBreastCancerLegaciesListArrayList().get(i).getTotalComments()));
        tv_likeCount.setText(String.valueOf(getAllBreastCancerLegaciesModel.getBreastCancerLegaciesListArrayList().get(i).getTotalLikes()));
        tv_name.setText(getAllBreastCancerLegaciesModel.getBreastCancerLegaciesListArrayList().get(i).getName());
        likeCount = getAllBreastCancerLegaciesModel.getBreastCancerLegaciesListArrayList().get(i).getTotalLikes();
        IsLikeValue = getAllBreastCancerLegaciesModel.getBreastCancerLegaciesListArrayList().get(i).getIsLiked();
        btn_likes.setChecked(IsLikeValue);

        if (getAllBreastCancerLegaciesModel.getBreastCancerLegaciesListArrayList().get(i).getFlowerType() == 0) {
            Glide.with(ProfileActivity.this)
                    .load(getResources().getDrawable(R.drawable.flower1))
                    .into(iv_image);
        } else if (getAllBreastCancerLegaciesModel.getBreastCancerLegaciesListArrayList().get(i).getFlowerType() == 1) {
            Glide.with(ProfileActivity.this)
                    .load(getResources().getDrawable(R.drawable.flower2))
                    .into(iv_image);
        } else if (getAllBreastCancerLegaciesModel.getBreastCancerLegaciesListArrayList().get(i).getFlowerType() == 2) {
            Glide.with(ProfileActivity.this)
                    .load(getResources().getDrawable(R.drawable.flower2))
                    .into(iv_image);
        } else {
            iv_image.setVisibility(View.GONE);
        }
    }

    // Display last two comment

    public void doRequestForDisplayComment() {
        String Id = getIntent().getStringExtra(Constants.WN_BREAST_CANCER_LEGACY_ID);
        String requestURL = String.format(getString(R.string.get_all_comment_by_breast_cancer), Id, "1");
        new PostRequest(this, null, requestURL, false, false, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                GetAllCommentsByBreastCancerLegacyModel getAllCommentsByBreastCancerLegacyModel = new Gson().fromJson(response, GetAllCommentsByBreastCancerLegacyModel.class);
                if (getAllCommentsByBreastCancerLegacyModel != null && getAllCommentsByBreastCancerLegacyModel.getBCLCommentList() != null && !getAllCommentsByBreastCancerLegacyModel.getBCLCommentList().isEmpty()) {
                    spannableStrings.clear();
                    for (int i = 0; i < getAllCommentsByBreastCancerLegacyModel.getBCLCommentList().size(); i++) {
                        spannableStrings.add(MentionHelper.decodeComments(ProfileActivity.this,
                                getAllCommentsByBreastCancerLegacyModel.getBCLCommentList().get(i).getComment(), getResources().getColor(R.color.pink)));
                    }
                    setViewCommentAdapter(getAllCommentsByBreastCancerLegacyModel.getBCLCommentList());
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(ProfileActivity.this, msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }


    void setViewCommentAdapter(ArrayList<GetAllCommentsByBreastCancerLegacyModel.BCLCommentList> BCLCommentList) {
        if (viewAllBCLCommentAdapter == null) {
            viewAllBCLCommentAdapter = new ViewAllBCLCommentAdapter(this);
        }
        if (rv_bcl_comment.getAdapter() == null) {
            rv_bcl_comment.setHasFixedSize(false);
            rv_bcl_comment.setLayoutManager(new LinearLayoutManager(this));
            rv_bcl_comment.setAdapter(viewAllBCLCommentAdapter);
        }

        if (BCLCommentList != null) {
            Collections.reverse(BCLCommentList);
            ArrayList<GetAllCommentsByBreastCancerLegacyModel.BCLCommentList> comment = new ArrayList<>();
            if (BCLCommentList.size() == 0) {
                ll_view_allComment.setVisibility(View.GONE);
            } else if (BCLCommentList.size() > 2) {
                ll_view_allComment.setVisibility(View.VISIBLE);
                for (int i = 0; i < 2; i++) {
                    comment.add(BCLCommentList.get(i));
                }
            } else {
                ll_view_allComment.setVisibility(View.VISIBLE);
                comment.addAll(BCLCommentList);
            }
            viewAllBCLCommentAdapter.doRefresh(comment);
        }
    }

    @OnClick({R.id.iv_mike, R.id.ll_view_Comment, R.id.tv_addComment})
    public void onMikeClick() {
        Intent intent = new Intent(this, BreastCommentActivity.class);
        intent.putExtra(Constants.WN_BREAST_CANCER_LEGACY_ID, getIntent().getStringExtra(Constants.WN_BREAST_CANCER_LEGACY_ID));
        intent.putExtra(Constants.WN_CUSTOMER_ID, getIntent().getStringExtra(Constants.WN_CUSTOMER_ID));
        startActivity(intent);
    }

    @OnClick(R.id.iv_shared)
    public void onClickShare() {
        Intent intent = new Intent(this, SharePostActivity.class);
        intent.putExtra(Constants.WN_PICTURE_PATH, getIntent().getStringExtra(Constants.WN_BREAST_CANCER_IMAGE_URL));
        intent.putExtra(Constants.WN_POST_ID, getIntent().getStringExtra(Constants.WN_BREAST_CANCER_LEGACY_ID));
        intent.putExtra(Constants.WN_Post_Type, ConstantEnum.PostType.BCL.getId());
        startActivity(intent);
    }

    @OnClick(R.id.btn_likes)
    public void onLikeButtonClick() {
        doRequestForLikeUnlikePost();
    }

    //Like/UnLike BCL Post

    public void doRequestForLikeUnlikePost() {
        btn_likes.setEnabled(false);
        String Id = getIntent().getStringExtra(Constants.WN_BREAST_CANCER_LEGACY_ID);

        final String requestURL = String.format(getString(R.string.like_unlike_breast_cancer_legacy), Id, IsLikeValue ? 0 : 1);

        new PostRequest(this, null, requestURL, false, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                btn_likes.setEnabled(true);

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
                Globals.showToast(ProfileActivity.this, msg);
                btn_likes.setEnabled(true);

            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
