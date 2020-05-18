package com.app.whosnextapp.loginregistration;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.app.whosnextapp.BuildConstants;
import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.APIService;
import com.app.whosnextapp.apis.HttpRequestHandler;
import com.app.whosnextapp.apis.ProgressRequestBody;
import com.app.whosnextapp.apis.ProgressUtil;
import com.app.whosnextapp.apis.RetrofitClient;
import com.app.whosnextapp.drawer.HomePageActivity;
import com.app.whosnextapp.messaging.quickblox.helper.RegisterNewUserTOQB;
import com.app.whosnextapp.model.UserDetailModel;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.app.whosnextapp.utility.Utility;
import com.app.whosnextapp.videos.ShareToActivity;
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
import cc.cloudist.acplibrary.ACProgressFlower;
import life.knowledge4.videotrimmer.utils.FileUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends BaseAppCompatActivity implements ProgressRequestBody.UploadCallbacks {

    @BindView(R.id.tv_category)
    AppCompatTextView tv_category;
    @BindView(R.id.et_first_name)
    AppCompatEditText et_first_name;
    @BindView(R.id.et_last_name)
    AppCompatEditText et_last_name;
    @BindView(R.id.et_username)
    AppCompatEditText et_username;
    @BindView(R.id.et_email)
    AppCompatEditText et_email;
    @BindView(R.id.et_confirm_email)
    AppCompatEditText et_confirm_email;
    @BindView(R.id.et_password)
    AppCompatEditText et_password;
    @BindView(R.id.et_confirm_password)
    AppCompatEditText et_confirm_password;
    @BindView(R.id.chk_term_condition)
    AppCompatCheckBox chk_term_condition;
    @BindView(R.id.btn_register)
    AppCompatButton btn_register;
    @BindView(R.id.iv_upload_video)
    AppCompatImageView iv_upload_video;
    @BindView(R.id.tv_terms_condition)
    AppCompatTextView tv_terms_condition;

    String videoPath;
    File videoThumbnailFile;
    String category_id = "";
    Globals globals;
    Handler handler;
    private ACProgressFlower dialog;
    private FFmpeg ffmpeg;

    private Activity getActivity() {
        return RegistrationActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
    }

    public void init() {
        ButterKnife.bind(this);
        setTermAndPrivacy();
        globals = (Globals) getApplicationContext();
        dialog = ProgressUtil.initProgressBar(getActivity());
        ffmpeg = globals.loadFFMpegBinary(getActivity());
    }

    private void setTermAndPrivacy() {
        // Terms & Condition Span
        final SpannableStringBuilder termsCondition = new SpannableStringBuilder(getString(R.string.text_terms_conditions_privacy_policy));
        termsCondition.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 8, 0);

        termsCondition.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.red)), 9, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsCondition.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View v) {
                Intent intent = new Intent(getActivity(), TermConditionActivity.class);
                intent.putExtra(Constants.WN_TERMS_CONDITION, getString(R.string.url_privacy_policy));
                intent.putExtra(Constants.WN_POLICY, Constants.WN_PRIVACY_POLICY);
                startActivity(intent);
            }
        }, 9, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Privacy Policy Span
        termsCondition.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.red)), 24, 41, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsCondition.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(getActivity(), TermConditionActivity.class);
                intent.putExtra(Constants.WN_TERMS_CONDITION, getString(R.string.url_term_condition));
                intent.putExtra(Constants.WN_POLICY, Constants.WN_TERMS_CONDITION);
                startActivity(intent);
            }
        }, 24, 41, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_terms_condition.setText(termsCondition);
        tv_terms_condition.setClickable(true);
        tv_terms_condition.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @OnClick(R.id.btn_register)
    public void onRegister() {
        if (isValid()) {
            ProgressUtil.getInstance();
            dialog.show();
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doRequestForRegister(videoPath);
                    // compressVideo(videoPath);
                }
            }, Constants.WN_SPLASH_TIME);
        }
    }

    //Registration

    private void doRequestForRegister(String path) {

        int imgHeight = iv_upload_video.getHeight();
        int imgWidth = iv_upload_video.getWidth();

        APIService mApiService = RetrofitClient.getClient(BuildConstants.BASE_URL).create(APIService.class);
        File selected_video = new File(path);

        RequestBody requestFile = RequestBody.create(MediaType.parse(Constants.WN_MEDIA_TYPE_IMAGE), videoThumbnailFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData(Constants.WN_PROFILE_PIC, videoThumbnailFile.getName(), requestFile);

        ProgressRequestBody requestBodyVideo = new ProgressRequestBody(selected_video, Constants.WN_MEDIA_TYPE_VIDEO, this);
        MultipartBody.Part bodyVideo = MultipartBody.Part.createFormData(Constants.WN_BIO_VIDEO, selected_video.getName(), requestBodyVideo);


        try {
            String requestURL = String.format(getString(R.string.get_registration_url));
            Call<UserDetailModel> call = mApiService.GetRegistration(requestURL, bodyVideo, body,
                    RequestBody.create(MultipartBody.FORM, String.valueOf((HttpRequestHandler.getInstance().getRegisterJson(category_id, tv_category.getText().toString(),
                            "", "", et_last_name.getText().toString(), Constants.WN_ANDROID, et_password.getText().toString(),
                            et_username.getText().toString(), globals.getFCM_DeviceToken(), et_email.getText().toString(), et_first_name.getText().toString(),(int) Globals.dpFromPx(getActivity(), imgWidth), Globals.dpFromPx(getActivity(), (float) (imgHeight/1.01))).toString()))));

            call.enqueue(new Callback<UserDetailModel>() {
                @Override
                public void onResponse(Call<UserDetailModel> call, Response<UserDetailModel> response) {
                    UserDetailModel userDetailModel = response.body();

                    if (userDetailModel != null) {
                        if (userDetailModel.getStatus().getResult().equals(Constants.WN_SUCCESS)) {
                            userDetailModel.getStatus().setPassword(et_password.getText().toString());
                            globals.setUserDetails(userDetailModel);
                            if (globals.getUserDetails().getStatus().getChatId() == 0) {
                                RegisterNewUserTOQB.getInstance().doRequestForRefereshUser(getActivity(), globals.getUserDetails());
                            }
                            Intent intent = new Intent(getActivity(), HomePageActivity.class);
                            intent.putExtra(Constants.WN_USERNAME, userDetailModel.getStatus().getUserName());
                            intent.putExtra(Constants.WN_USER_ID, userDetailModel.getStatus().getUserId());
                            intent.putExtra(Constants.WN_CUSTOMER_ID, userDetailModel.getStatus().getCustomerId());
                            Globals.showToast(getActivity(), getString(R.string.registration_success));
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Globals.showToast(getActivity(), userDetailModel.getStatus().getResult());
                        }
                    } else {
                        Globals.showToast(getActivity(), getString(R.string.wrong_data_content));
                    }
                    // dialog.dismiss();
                }

                @Override
                public void onFailure(Call<UserDetailModel> call, Throwable t) {
                    Globals.showToast(getActivity(), getString(R.string.msg_server_error));
                }
            });
        } catch (Exception e) {

        }
    }


    @OnClick(R.id.tv_category)
    public void onSelectTalentClick() {
        Intent i = new Intent(getActivity(), SelectTalentActivity.class);
        i.putExtra(Constants.WN_SELECTED_TALENT, category_id);
        startActivityForResult(i, Constants.WN_REQUEST_SELECT_TALENT);
    }

    @OnClick(R.id.iv_upload_video)
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
                Globals.showToast(getActivity(), getString(R.string.permission_denied) + deniedPermissions.toString());
            }
        };
        TedPermission.with(getActivity())
                .setPermissionListener(permissionlistener)
                .setRationaleMessage(getString(R.string.request_camera_permission))
                .setDeniedMessage(getString(R.string.on_denied_permission))
                .setGotoSettingButtonText(getString(R.string.setting))
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void showDialog() {
        CharSequence[] options = new CharSequence[]{getResources().getString(R.string.video_option), getResources().getString(R.string.text_pick_video)};
        globals.show_dialog(getActivity(), getString(R.string.select_video), "", options, true, new Globals.OptionDialogClickHanderListener() {
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
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
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
                        category_id = catId;
                    } else {
                        category_id = "";
                        tv_category.setText(R.string.select_talent);
                    }

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

            if (requestCode == Constants.WN_VIDEO_TRIM) {
                if (data != null && data.getExtras() != null) {
                    videoPath = data.getExtras().getString(Constants.WN_INTENT_VIDEO_FILE);
                    afterVideoReturned(new File(videoPath));
                }
            }

        }
    }

    private void compressVideo(String path) {
        String dstFile = Utility.createFile(Constants.WN_VIDEO_FOLDER_PATH, Constants.WN_VIDEO_TRIM_FILE +
                System.currentTimeMillis(), Constants.WN_VIDEO_MP4);

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

        String[] complexCommand = {"-y", "-i", path, "-s", width + "*" + height, "-r", "40", "-vcodec", "mpeg4", "-b:v",
                "850k", "-b:a", "48000", "-ac", "2", "-ar", "22050",
                (new File(dstFile)).getAbsolutePath()};
        execFFmpegBinary(complexCommand, dstFile);
    }

    private void execFFmpegBinary(final String[] command, String dstFile) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    super.onSuccess(message);
                    doRequestForRegister(dstFile);
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
                    //dialog.show();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    //dialog.dismiss();

                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }

    private void startTrimActivity(@NonNull Uri uri) {

        startActivityForResult(new Intent(RegistrationActivity.this, VideoTrimmerActivity.class)
                        .putExtra(Constants.WN_EXTRA_VIDEO_PATH, FileUtils.getPath(this, uri)),
                Constants.WN_VIDEO_TRIM);
        overridePendingTransition(0, 0);

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


    private boolean isValid() {
        if (et_first_name.getText().toString().trim().isEmpty()) {
            Globals.showToast(this, getString(R.string.err_empty_first_name));
            return false;
        }
        if (et_last_name.getText().toString().trim().isEmpty()) {
            Globals.showToast(this, getString(R.string.err_empty_last_name));
            return false;
        }
        if (et_username.getText().toString().trim().isEmpty()) {
            Globals.showToast(this, getString(R.string.err_empty_user_name));
            return false;
        }
        if (et_username.getText().length() < 4) {
            Globals.showToast(this, getString(R.string.err_valid_username));
            return false;
        }
        if (et_email.getText().toString().trim().isEmpty()) {
            Globals.showToast(this, getString(R.string.err_empty_emailId));
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString().trim()).matches()) {
            Globals.showToast(this, getString(R.string.err_valid_emailId));
            return false;
        }
        if (et_confirm_email.getText().toString().trim().isEmpty()) {
            Globals.showToast(this, getString(R.string.err_empty_Confirm_new_email));
            return false;
        }
        if (!et_confirm_email.getText().toString().trim().equals(et_email.getText().toString().trim())) {
            Globals.showToast(this, getString(R.string.your_confirm_email_id_does_not_match));
            return false;
        }
        if (et_password.getText().toString().trim().isEmpty()) {
            Globals.showToast(this, getString(R.string.err_empty_password));
            return false;
        }
        if (et_password.length() < 6) {
            Globals.showToast(this, getString(R.string.err_min_length_password));
            return false;
        }
        if (et_confirm_password.getText().toString().trim().isEmpty()) {
            Globals.showToast(this, getString(R.string.err_check_confirm_new_password));
            return false;
        }
        if (!et_password.getText().toString().trim().equals(et_confirm_password.getText().toString().trim())) {
            Globals.showToast(this, getString(R.string.err_password_not_equals));
            return false;
        }
        if (category_id.isEmpty()) {
            Globals.showToast(this, getString(R.string.err_required_category));
            return false;
        }
        if (videoThumbnailFile == null) {
            Globals.showToast(this, getString(R.string.err_required_upload));
            return false;
        }
        if (!chk_term_condition.isChecked()) {
            Globals.showToast(this, getString(R.string.err_required_terms_policy));
            return false;
        }
        return true;
    }

    @OnClick(R.id.iv_back)
    public void onBackClick(View v) {
        onBackPressed();
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }

    @Override
    public void onError() {
        Globals.showToast(getActivity(), getString(R.string.msg_server_error));
    }

    @Override
    public void onFinish() {

    }
}
