package com.app.whosnextapp.navigationmenu;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.app.whosnextapp.BuildConstants;
import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.APIService;
import com.app.whosnextapp.apis.HttpRequestHandler;
import com.app.whosnextapp.apis.ProgressRequestBody;
import com.app.whosnextapp.apis.ProgressUtil;
import com.app.whosnextapp.apis.RetrofitClient;
import com.app.whosnextapp.loginregistration.SelectEditTalentActivity;
import com.app.whosnextapp.loginregistration.SelectTalentActivity;
import com.app.whosnextapp.loginregistration.VideoTrimmerActivity;
import com.app.whosnextapp.model.GetCustomerProfileModel;
import com.app.whosnextapp.model.LikeUnlikePostModel;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.app.whosnextapp.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import cc.cloudist.acplibrary.ACProgressFlower;
import life.knowledge4.videotrimmer.utils.FileUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends BaseAppCompatActivity implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;
    @BindView(R.id.tv_first_name)
    AppCompatEditText tv_first_name;
    @BindView(R.id.tv_last_name)
    AppCompatEditText tv_last_name;
    @BindView(R.id.tv_username)
    AppCompatEditText tv_username;
    @BindView(R.id.tv_email)
    AppCompatEditText tv_email;
    @BindView(R.id.tv_city)
    AppCompatEditText tv_city;
    @BindView(R.id.tv_WEBSITE1)
    AppCompatEditText tv_WEBSITE1;
    @BindView(R.id.tv_WEBSITE2)
    AppCompatEditText tv_WEBSITE2;
    @BindView(R.id.tv_WEBSITE3)
    AppCompatEditText tv_WEBSITE3;
    @BindView(R.id.tv_WEBSITE4)
    AppCompatEditText tv_WEBSITE4;
    @BindView(R.id.tv_WEBSITE5)
    AppCompatEditText tv_WEBSITE5;
    @BindView(R.id.tv_category)
    AppCompatTextView tv_category;
    @BindView(R.id.iv_upload_video)
    AppCompatImageView iv_upload_video;
    @BindView(R.id.tv_about_self)
    AppCompatEditText tv_about_self;

    Globals globals;
    String video_base64;
    String videoPath;
    String catID = "", website;
    String profile_pic_base64;
    String customerId, UID;
    GetCustomerProfileModel getCustomerProfileModel;
    Uri selectedMediaUri;
    File videoThumbnailFile;
    private FFmpeg ffmpeg;
    private ACProgressFlower dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        init();
    }

    private Activity getActivity() {
        return EditProfileActivity.this;
    }

    public void init() {
        globals = (Globals) getApplicationContext();
        dialog = ProgressUtil.initProgressBar(this);
        setActionbar(toolbar);
        ffmpeg = globals.loadFFMpegBinary(this);
        setData();
    }

    public void setActionbar(Toolbar toolbar) {
        toolbar.setBackgroundResource(R.drawable.header_bg);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar_title.setText(R.string.edit_profile);
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(this);
    }

    @OnFocusChange({R.id.tv_WEBSITE1, R.id.tv_WEBSITE2, R.id.tv_WEBSITE3, R.id.tv_WEBSITE4, R.id.tv_WEBSITE5})
    public void website1(View view, boolean hasFocus) {
        AppCompatEditText appCompatEditText = (AppCompatEditText) view;
        if (hasFocus && TextUtils.isEmpty(appCompatEditText.getText().toString())) {
            appCompatEditText.setText(R.string.http);
        }
    }

    private void setData() {
        if (getIntent() != null) {
            getCustomerProfileModel = (GetCustomerProfileModel) getIntent().getSerializableExtra(Constants.WN_GET_CUSTOMER_INFO_MODEL);
            tv_first_name.setText(getCustomerProfileModel.getCustomerDetail().getFirstName());
            tv_first_name.setSelection(getCustomerProfileModel.getCustomerDetail().getFirstName().length());
            tv_last_name.setText(getCustomerProfileModel.getCustomerDetail().getLastName());
            tv_last_name.setSelection(getCustomerProfileModel.getCustomerDetail().getLastName().length());
            tv_username.setText(globals.getUserDetails().getStatus().getUserName());
            tv_username.setSelection(getCustomerProfileModel.getCustomerDetail().getUserName().length());
            tv_email.setText(globals.getUserDetails().getStatus().getEmail());
            tv_email.setSelection(getCustomerProfileModel.getCustomerDetail().getEmail().length());
            tv_city.setText(getCustomerProfileModel.getCustomerDetail().getCity());
            tv_city.setSelection(getCustomerProfileModel.getCustomerDetail().getCity().length());


            catID = getCustomerProfileModel.getCustomerDetail().getCategoryIds();
            // ProfilePic = getCustomerProfileModel.getCustomerDetail().getProfilePicUrl();
            AppCompatEditText[] websiteText = new AppCompatEditText[]{tv_WEBSITE1, tv_WEBSITE2, tv_WEBSITE3, tv_WEBSITE4, tv_WEBSITE5};
            website = getCustomerProfileModel.getCustomerDetail().getWebsite();
            String[] separated = website.split(",");
            for (int i = 0; i < separated.length; i++) {
                websiteText[i].setText(separated[i]);
                websiteText[i].setSelection(separated[i].length());
            }

            tv_category.setText(getCustomerProfileModel.getCustomerDetail().getCategoryNames());
            tv_about_self.setText(getCustomerProfileModel.getCustomerDetail().getAboutSelf());
            tv_about_self.setSelection(getCustomerProfileModel.getCustomerDetail().getAboutSelf().length());

            videoPath = getCustomerProfileModel.getCustomerDetail().getProfilePicUrl();
            Glide.with(EditProfileActivity.this)
                    .load(globals.getUserDetails().getStatus().getProfilePicUrl())
                    .into(iv_upload_video);


            customerId = String.valueOf(getCustomerProfileModel.getCustomerDetail().getCustomerId());
            UID = getCustomerProfileModel.getCustomerDetail().getUserId();
            tv_WEBSITE1.setSelection(tv_WEBSITE1.getText().length());
            tv_WEBSITE2.setSelection(tv_WEBSITE2.getText().length());
            tv_WEBSITE3.setSelection(tv_WEBSITE3.getText().length());
            tv_WEBSITE4.setSelection(tv_WEBSITE4.getText().length());
            tv_WEBSITE5.setSelection(tv_WEBSITE5.getText().length());
        }
    }

    @OnClick(R.id.btn_select_talent)
    public void onSelectTalentClick() {
        Intent i = new Intent(EditProfileActivity.this, SelectEditTalentActivity.class);
        i.putExtra(Constants.WN_SELECTED_TALENT, catID);
        startActivityForResult(i, Constants.WN_REQUEST_SELECT_TALENT);
    }

    @OnClick(R.id.btn_update)
    public void onUpdateProfileClick() {
        dialog.show();
        AppCompatEditText[] websiteText = new AppCompatEditText[]{tv_WEBSITE1, tv_WEBSITE2, tv_WEBSITE3, tv_WEBSITE4, tv_WEBSITE5};
        StringBuilder web = new StringBuilder();
        for (AppCompatEditText aWebsiteText : websiteText) {
            if (!aWebsiteText.getText().toString().isEmpty()) {
                if (web.length() == 0)
                    web.append(aWebsiteText.getText().toString());
                else {
                    web.append(",");
                    web.append(aWebsiteText.getText().toString());
                }
            }
        }
        doRequestForEditProfile(web.toString(), videoPath);

    }

    @OnClick(R.id.tv_replace_video)
    public void onUploadVideoClick() {
        getPermissionForVideo();
    }

    private void getPermissionForVideo() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                showDialog();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Globals.showToast(getApplicationContext(), getString(R.string.permission_denied) + deniedPermissions.toString());
            }
        };
        TedPermission.with(EditProfileActivity.this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage(getString(R.string.request_camera_permission))
                .setDeniedMessage(getString(R.string.on_denied_permission))
                .setGotoSettingButtonText(getString(R.string.setting))
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void afterVideoReturned(File photoFile) {
        Glide.with(getActivity()).load(photoFile).into(new SimpleTarget<Drawable>() {
            @Override

            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                iv_upload_video.setImageDrawable(resource);
                videoThumbnailFile = Utility.createFileFromResource(getActivity(), resource, Constants.WN_VIDEO_THUMBNAIL);
            }
        });
    }

    private void showDialog() {
        CharSequence[] options = new CharSequence[]{getResources().getString(R.string.video_option), getResources().getString(R.string.text_pick_video)};
        globals.show_dialog(this, getString(R.string.text_browse_video), "", options, true, new Globals.OptionDialogClickHanderListener() {
            @Override
            public void OnItemClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        openCameraForVideo();
                        break;
                    case 1:
                        openGallery();
                        break;
                }
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setTypeAndNormalize(Constants.WN_MEDIA_TYPE_VIDEO);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_video)), Constants.WN_REQUEST_VIDEO_TRIMMER);
    }

    private void openCameraForVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, Constants.WN_BIO_VIDEO_DURATION);
        startActivityForResult(intent, Constants.WN_REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.WN_REQUEST_SELECT_TALENT) {
                if (data != null) {
                    String cat = data.getStringExtra(Constants.WN_CATEGORY_SEL);
                    String catId = data.getStringExtra(Constants.WN_CATEGORY_ID_SEL);
                    if (!cat.isEmpty()) {
                        tv_category.setText(cat);
                        catID = catId;
                    } else {
                        catID = "";
                        tv_category.setText(R.string.select_talent);
                    }

                }
            }

            if (requestCode == Constants.WN_VIDEO_TRIM) {
                if (data != null && data.getExtras() != null) {
                    videoPath = data.getExtras().getString(Constants.WN_INTENT_VIDEO_FILE);
                    afterVideoReturned(new File(videoPath));
                }
            }

            if (requestCode == Constants.WN_REQUEST_CAMERA) {
                if (data != null) {
                    Uri selectedMediaUri = data.getData();
                    if (selectedMediaUri != null) {
                        startTrimActivity(selectedMediaUri);
                    } else {
                        Globals.showToast(getActivity(), getString(R.string.toast_cannot_retrieve_selected_video));
                    }
                }
            }


            if (requestCode == Constants.WN_REQUEST_VIDEO_TRIMMER) {
                assert data != null;
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startTrimActivity(selectedUri);
                } else {
                    Globals.showToast(getActivity(), getString(R.string.toast_cannot_retrieve_selected_video));
                }
            }
        }
    }

    private void compressCaptureVideo(String path) {
        String dstFile = Utility.createFile(Constants.WN_VIDEO_FOLDER_PATH, Constants.WN_VIDEO_TRIM_FILE +
                System.currentTimeMillis(), Constants.WN_VIDEO_MP4);

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

        String[] complexCommand = {"-y", "-i", path, "-s", width + "*" + height, "-r", "40", "-vcodec", "mpeg4", "-b:v",
                "850k", "-b:a", "48000", "-ac", "2", "-ar", "22050",
                (new File(dstFile)).getAbsolutePath()};
        executeFFmpegBinary(complexCommand, dstFile);
    }

    private void executeFFmpegBinary(final String[] command, String dstFile) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    super.onSuccess(message);
                    selectedMediaUri = Uri.parse(dstFile);
                }

                @Override
                public void onFailure(String message) {
                    super.onFailure(message);
                }

                @Override
                public void onProgress(String message) {
                    super.onProgress(message);
                }

                @Override
                public void onStart() {
                    super.onStart();
                    dialog.show();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    dialog.dismiss();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }

    private void startTrimActivity(@NonNull Uri uri) {
        startActivityForResult(new Intent(EditProfileActivity.this, VideoTrimmerActivity.class)
                        .putExtra(Constants.WN_EXTRA_VIDEO_PATH, FileUtils.getPath(this, uri)),
                Constants.WN_VIDEO_TRIM);
        overridePendingTransition(0, 0);
    }

    //Edit Profile

    private void doRequestForEditProfile(String website, String path) {
        int imgHeight = iv_upload_video.getHeight();
        int imgWidth = iv_upload_video.getWidth();
        MultipartBody.Part body = null;
        MultipartBody.Part bodyVideo = null;
        APIService mApiService = RetrofitClient.getClient(BuildConstants.BASE_URL).create(APIService.class);
        if (!videoPath.isEmpty() && new File(videoPath).exists()) {
            File selected_video = new File(path);

            RequestBody requestFile = RequestBody.create(MediaType.parse(Constants.WN_MEDIA_TYPE_IMAGE), videoThumbnailFile);
            body = MultipartBody.Part.createFormData(Constants.WN_PROFILE_PIC, videoThumbnailFile.getName(), requestFile);

            ProgressRequestBody requestBodyVideo = new ProgressRequestBody(selected_video, Constants.WN_MEDIA_TYPE_VIDEO, this);
            bodyVideo = MultipartBody.Part.createFormData(Constants.WN_BIO_VIDEO, selected_video.getName(), requestBodyVideo);
        }
        try {
            String requestURL = String.format(getString(R.string.update_customer_profile));
            Call<LikeUnlikePostModel> call;
            if (body != null) {
                call = mApiService.GetUpdateProfile(globals.getUserDetails().getStatus().getUserId(), requestURL, bodyVideo, body,
                        RequestBody.create(MultipartBody.FORM, (HttpRequestHandler.getInstance().getUpdateProfileJson(catID, profile_pic_base64, tv_email.getText().toString(), tv_username.getText().toString(),
                                videoPath, tv_first_name.getText().toString(), customerId, video_base64, tv_category.getText().toString(), tv_about_self.getText().toString(),
                                tv_last_name.getText().toString(), tv_city.getText().toString(), website,(int) Globals.dpFromPx(getActivity(), imgWidth), Globals.dpFromPx(getActivity(), (float) (imgHeight/1.01))).toString())));
            } else {
                call = mApiService.GetUpdateWithoutProfile(globals.getUserDetails().getStatus().getUserId(), requestURL,
                        RequestBody.create(MultipartBody.FORM, (HttpRequestHandler.getInstance().getUpdateProfileJson(catID, profile_pic_base64, tv_email.getText().toString(), tv_username.getText().toString(),
                                videoPath, tv_first_name.getText().toString(), customerId, video_base64, tv_category.getText().toString(), tv_about_self.getText().toString(),
                                tv_last_name.getText().toString(), tv_city.getText().toString(), website,(int) Globals.dpFromPx(getActivity(), imgWidth), Globals.dpFromPx(getActivity(), (float) (imgHeight/1.01))).toString())));
            }


            call.enqueue(new Callback<LikeUnlikePostModel>() {
                @Override
                public void onResponse(Call<LikeUnlikePostModel> call, Response<LikeUnlikePostModel> response) {
                    LikeUnlikePostModel updateCustomerProfileModel = response.body();
                    if (updateCustomerProfileModel != null) {
                        if (updateCustomerProfileModel.getResult().equals(Constants.WN_SUCCESS)) {
                            Globals.showToast(EditProfileActivity.this, getString(R.string.msg_update_profile));
                            finish();

                        } else {
                            Globals.showToast(EditProfileActivity.this, getString(R.string.msg_server_error));
                        }
                    }
                }

                @Override
                public void onFailure(Call<LikeUnlikePostModel> call, Throwable t) {
                    Globals.showToast(getActivity(), getString(R.string.msg_server_error));
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
    }

    @Override
    public void onError() {
    }

    @Override
    public void onFinish() {
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
