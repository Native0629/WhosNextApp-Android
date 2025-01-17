package com.app.whosnextapp.drawer.homepage;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.GetAllDashboardData;
import com.app.whosnextapp.navigationmenu.OtherUserProfileActivity;
import com.app.whosnextapp.notification.ProfileVideoPlayerActivity;
import com.app.whosnextapp.snippets.SnippetVideoActivity;
import com.app.whosnextapp.utility.ConstantEnum;
import com.app.whosnextapp.utility.Constants;
import com.bumptech.glide.Glide;
import com.joker.pager.holder.ViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class BannerPagerHolder extends ViewHolder<GetAllDashboardData.SnippetsList> {
    @BindView(R.id.iv_profile)
    CircleImageView iv_profile;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.tv_username)
    TextView tv_username;
    @BindView(R.id.tv_views)
    TextView tv_views;

    private Context context;
    private int customerId;

    BannerPagerHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBindView(View view, final GetAllDashboardData.SnippetsList data, final int position) {

        String imageUrl = "";
        if (data.getSnippetsType() == ConstantEnum.SnippetType.AUDIO.getId()) {
            imageUrl = data.getSnippetsAudioImagePath();
        } else if (data.getSnippetsType() == ConstantEnum.SnippetType.VIDEO.getId()) {
            imageUrl = data.getSnippetsThumbPath();
        } else if (data.getSnippetsType() == ConstantEnum.SnippetType.IMAGE.getId()) {
            imageUrl = data.getSnippetsPath();
        }
        Glide.with(image.getContext())
                .load(imageUrl)
                .into(image);

        Glide.with(iv_profile.getContext())
                .load(data.getSnippetsProfileUrl())
                .into(iv_profile);

        tv_username.setText(data.getUserName());
        tv_views.setText(String.format(context.getString(R.string.text_views), data.getTotalViews()));
        customerId = data.getCustomerId();
        image.setOnClickListener(v -> {
            Intent intent = new Intent(context, SnippetVideoActivity.class);
            intent.putExtra(Constants.WN_GET_SNIPPET_MODEL, data);
            intent.putExtra(Constants.WN_POSITION, position);
            context.startActivity(intent);
        });
    }

    @OnClick(R.id.iv_profile)
    void onProfileClick() {
        Intent intent = new Intent(context, ProfileVideoPlayerActivity.class);
        intent.putExtra(Constants.WN_CUSTOMER_ID, customerId);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_username)
    void onUsernameClick() {
        Intent intent = new Intent(context, OtherUserProfileActivity.class);
        intent.putExtra(Constants.WN_CUSTOMER_ID, customerId);
        context.startActivity(intent);
    }
}
