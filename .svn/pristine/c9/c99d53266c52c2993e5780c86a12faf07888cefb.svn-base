package com.app.whosnextapp.snippets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.app.whosnextapp.R;
import com.app.whosnextapp.drawer.HomePageActivity;
import com.app.whosnextapp.model.GetAllDashboardData;
import com.app.whosnextapp.navigationmenu.OtherUserProfileActivity;
import com.app.whosnextapp.notification.ProfileVideoPlayerActivity;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.ConstantEnum;
import com.app.whosnextapp.utility.Constants;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class SnippetProfileActivity extends BaseAppCompatActivity implements View.OnClickListener {
    @BindView(R.id.iv_profile)
    CircleImageView iv_profile;
    @BindView(R.id.tv_username)
    TextView tv_username;
    @BindView(R.id.iv_snippet_image)
    ImageView iv_snippet_image;
    @BindView(R.id.tv_snippet_details)
    TextView tv_snippet_details;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;

    int customer_id;
    GetAllDashboardData.SnippetsList snippetsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snippet_profile);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        setActionBar();
        setData();
    }

    private void setActionBar() {
        toolbar.setBackgroundResource(R.drawable.header_bg);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar_title.setText(R.string.text_home);
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(this);
    }

    private void setData() {
        if (getIntent() != null)
            snippetsList = (GetAllDashboardData.SnippetsList) getIntent().getSerializableExtra(Constants.WN_GET_SNIPPET_MODEL);
        String imageUrl = "";
        if (snippetsList.getSnippetsType() == ConstantEnum.SnippetType.AUDIO.getId())
            imageUrl = snippetsList.getSnippetsAudioImagePath();
        else if (snippetsList.getSnippetsType() == ConstantEnum.SnippetType.VIDEO.getId())
            imageUrl = snippetsList.getSnippetsThumbPath();
        else if (snippetsList.getSnippetsType() == ConstantEnum.SnippetType.IMAGE.getId()) {
            imageUrl = snippetsList.getSnippetsPath();
        }
        tv_username.setText(snippetsList.getUserName());
        tv_snippet_details.setText(snippetsList.getSnippetsCaption());
        Glide.with(this)
                .load(snippetsList.getSnippetsProfileUrl())
                .into(iv_profile);

        Glide.with(this)
                .load(imageUrl)
                .into(iv_snippet_image);
        customer_id = snippetsList.getCustomerId();
    }

    @OnClick(R.id.iv_profile)
    public void onProfileClick() {
        Intent intent = new Intent(this, ProfileVideoPlayerActivity.class);
        intent.putExtra(Constants.WN_CUSTOMER_ID, customer_id);
        startActivity(intent);
    }

    @OnClick(R.id.tv_username)
    public void onUsernameClick() {
        Intent intent = new Intent(this, OtherUserProfileActivity.class);
        intent.putExtra(Constants.WN_CUSTOMER_ID, customer_id);
        startActivity(intent);
    }

    @OnClick(R.id.iv_snippet_image)
    public void onTapToPlayClick() {
        Intent intent = new Intent(this, SnippetVideoActivity.class);
        intent.putExtra(Constants.WN_GET_SNIPPET_MODEL, snippetsList);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, HomePageActivity.class));
    }
}
