package com.app.whosnextapp.drawer.homepage;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.HttpRequestHandler;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.model.LikeUnlikePostModel;
import com.app.whosnextapp.model.TagListModel;
import com.app.whosnextapp.pictures.CaptionActivity;
import com.app.whosnextapp.pictures.TagPeopleActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPictureFragment extends Fragment {

    @BindView(R.id.iv_picture_thumbnail)
    AppCompatImageView iv_picture_thumbnail;
    @BindView(R.id.tv_write_caption)
    AppCompatTextView tv_write_caption;
    @BindView(R.id.tv_tag_people)
    TextView tv_tag_people;
    @BindView(R.id.ll_upload_progress)
    LinearLayout ll_upload_progress;
    @BindView(R.id.progress_uploading)
    ProgressBar progress_uploading;

    String isImage, isVideo;
    ArrayList<TagListModel> tagListModels = new ArrayList<>();
    String camera_picture, tag_list, caption;
    EditShareToPictureActivity editShareToPictureActivity;
    Globals globals;

    public static EditPictureFragment newInstance() {
        return new EditPictureFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        editShareToPictureActivity = (EditShareToPictureActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_picture, container, false);
        ButterKnife.bind(this, v);
        init();
        return v;
    }

    private void init() {
        globals = (Globals) editShareToPictureActivity.getApplicationContext();
        tv_write_caption.setText(editShareToPictureActivity.getIntent().getStringExtra(Constants.CAPTION));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progress_uploading.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        ll_upload_progress.setVisibility(View.GONE);
        if (editShareToPictureActivity.getIntent() != null) {
            camera_picture = editShareToPictureActivity.getIntent().getStringExtra(Constants.WN_PICTURE_PATH);
            if (camera_picture != null) {
                Glide.with(editShareToPictureActivity).load(camera_picture).into(iv_picture_thumbnail);
            }
        }
    }

    @OnClick(R.id.btn_share)
    public void onShareClick() {
        if (ll_upload_progress.getVisibility() != View.VISIBLE) {
            doRequestForEditPicture();
        }
    }

    @OnClick(R.id.tv_tag_people)
    public void onTagPeopleClick() {
        Intent i = new Intent(getContext(), TagPeopleActivity.class);
        i.putExtra(Constants.WN_PICTURE_PATH, camera_picture);
        i.putExtra(Constants.WN_POST_TAG_LIST, tagListModels);
        startActivityForResult(i, Constants.WN_UPLOAD_PICTURE);
    }

    @OnClick(R.id.tv_write_caption)
    public void onCaptionClick() {
        Intent i = new Intent(getContext(), CaptionActivity.class);
        i.putExtra(Constants.WN_SHARED_VIDEO_CAPTION, caption);
        startActivityForResult(i, Constants.WN_CAPTION);
    }

    // Edit 

    public void doRequestForEditPicture() {
        isImage = String.valueOf(editShareToPictureActivity.getIntent().getExtras().getInt(Constants.WN_IS_IMAGE));
        isVideo = isImage.equals("0") ? "1" : "0";

        JSONObject postData = HttpRequestHandler.getInstance().editPostJson(editShareToPictureActivity.getIntent().getStringExtra(Constants.WN_POST_ID), "", isImage, tv_write_caption.getText().toString(), "XYZ", "120.000000", "112.000000", "", isVideo, "");

        new PostRequest(getActivity(), postData, getString(R.string.edit_post), true, true, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                LikeUnlikePostModel updateCustomerProfileModel = new Gson().fromJson(response, LikeUnlikePostModel.class);
                if (updateCustomerProfileModel != null) {
                    if (updateCustomerProfileModel.getResult().equals(Constants.WN_SUCCESS)) {
                        Globals.showToast(getActivity(), getString(R.string.msg_update_profile));
                        editShareToPictureActivity.finish();
                    } else {
                        Globals.showToast(getActivity(), getString(R.string.msg_server_error));
                    }
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {

            }
        }).execute(globals.getUserDetails().getStatus().getUserId());


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.WN_UPLOAD_PICTURE && data != null) {
                tagListModels = (ArrayList<TagListModel>) data.getSerializableExtra(Constants.WN_X);
                List<String> list = new ArrayList<>();
                for (int i = 0; i < tagListModels.size(); i++) {
                    list.add(tagListModels.get(i).getUsername());
                }
                tag_list = TextUtils.join(", ", list);
                if (tag_list != null) {
                    tv_tag_people.setText(tag_list);
                } else {
                    tv_tag_people.setText(R.string.text_tap_tag_people);
                }
            }
            if (requestCode == Constants.WN_CAPTION && data != null) {
                caption = data.getStringExtra(Constants.WN_SHARED_VIDEO_CAPTION);
                tv_write_caption.setText(caption);
            }
        }
    }
}
