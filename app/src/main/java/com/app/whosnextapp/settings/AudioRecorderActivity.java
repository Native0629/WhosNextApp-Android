package com.app.whosnextapp.settings;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.app.whosnextapp.BuildConstants;
import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.APIService;
import com.app.whosnextapp.apis.ProgressRequestBody;
import com.app.whosnextapp.apis.RetrofitClient;
import com.app.whosnextapp.drawer.HomePageActivity;
import com.app.whosnextapp.model.SnippetModel;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.app.whosnextapp.utility.Utility;
import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AudioRecorderActivity extends BaseAppCompatActivity implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;
    @BindView(R.id.et_snippet_details)
    AppCompatEditText et_snippet_details;
    @BindView(R.id.iv_snippet_image)
    AppCompatImageView iv_snippet_image;
    @BindView(R.id.ll_upload_progress)
    LinearLayout ll_upload_progress;
    @BindView(R.id.progress_uploading)
    ProgressBar progress_uploading;
    @BindView(R.id.iv_record)
    AppCompatImageView iv_record;
    @BindView(R.id.chronometer)
    Chronometer chronometer;

    MediaRecorder mediaRecorder = null;
    String imageFile, selectedAudioPath;
    Globals globals;
    File selected_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        ButterKnife.bind(this);
        setActionBar();
        globals = (Globals) getApplicationContext();
        Globals.setAnimationToView(chronometer);
        iv_record.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startRecording();
                    return true;
                case MotionEvent.ACTION_UP:
                    stopRecording();
                    break;
            }
            return false;
            /*switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startRecording();
                    break;
                case MotionEvent.ACTION_UP:
                    stopRecording();
                    break;
                default:
                    return false;
            }
            return true;*/
        });
        chronometer.setOnChronometerTickListener(chronometer -> {
            long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
            if (elapsedMillis > Constants.WN_AUDIO_MAX_DURATION) {
                stopRecording();
            }
        });
    }

    private void setActionBar() {
        toolbar.setBackgroundResource(R.drawable.header_bg);
        setSupportActionBar(toolbar);
        toolbar_title.setText(R.string.setting);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(this);
    }

    private Activity getActivity() {
        return AudioRecorderActivity.this;
    }

    @OnClick(R.id.iv_snippet_image)
    public void onSnippetImageClick() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                showImageDialog();
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

    private void showImageDialog() {
        CharSequence[] options = new CharSequence[]{getResources().getString(R.string.text_camera), getResources().getString(R.string.text_gallery)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        builder.setTitle(R.string.text_browse_image);
        builder.setNeutralButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.setItems(options, (dialog, item) -> {
            switch (item) {
                case 0:
                    openCameraForImage();
                    break;
                case 1:
                    openGalleryForImage();
                    break;
            }
        });
        builder.show();
    }

    private void openGalleryForImage() {
        EasyImage.openGallery(getActivity(), 0);
    }

    private void openCameraForImage() {
        EasyImage.openCameraForImage(getActivity(), 0);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), HomePageActivity.class));
    }

    private void stopRecording() {
        try {
            if (mediaRecorder != null) {
                chronometer.stop();
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder.reset();
                mediaRecorder = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startRecording() {
        chronometer.setVisibility(View.VISIBLE);
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setMaxDuration(Constants.WN_AUDIO_MAX_DURATION);
        selectedAudioPath = Utility.createFile(Constants.WN_AUDIO_FOLDER_PATH, Constants.WN_AUDIO_RECORD_FILE + System.currentTimeMillis(), Constants.WN_MP3);
        mediaRecorder.setOutputFile(selectedAudioPath);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_save)
    public void onSnippetUpload() {
        if (selected_image == null) {
            selected_image = Utility.createFileFromResource(getActivity(), getResources().getDrawable(R.drawable.whosnextapp), Constants.WN_AUDIO_THUMBNAIL);
        }
        if (selectedAudioPath != null && selected_image != null && ll_upload_progress.getVisibility() != View.VISIBLE) {
            doRequestToUploadSnippetAudio();
        }
    }

    private void doRequestToUploadSnippetAudio() {
        ll_upload_progress.setVisibility(View.VISIBLE);
        APIService mApiService = RetrofitClient.getClient(BuildConstants.BASE_URL).create(APIService.class);

        final File selected_audio = new File(selectedAudioPath);

        RequestBody requestFile = RequestBody.create(MediaType.parse(Constants.WN_MEDIA_TYPE_IMAGE), selected_image);
        MultipartBody.Part bodyImage = MultipartBody.Part.createFormData(Constants.WN_SNIPPET_IMAGE, selected_image.getName(), requestFile);

        ProgressRequestBody requestBodyAudio = new ProgressRequestBody(selected_audio, Constants.WN_MEDIA_TYPE_AUDIO, this);
        MultipartBody.Part bodyAudio = MultipartBody.Part.createFormData(Constants.WN_SNIPPET_AUDIO, selected_audio.getName(), requestBodyAudio);

        RequestBody SnippetsCaption = RequestBody.create(MediaType.parse(Constants.WN_MEDIA_TYPE_TEXT), et_snippet_details.getText().toString());

        String requestURL = String.format(getString(R.string.upload_snippet), globals.getUserDetails().getStatus().getCustomerId(), 3);

        Call<SnippetModel> call = mApiService.GetSnippetAudioPost(globals.getUserDetails().getStatus().getUserId(), requestURL, bodyAudio, bodyImage, SnippetsCaption);

        call.enqueue(new Callback<SnippetModel>() {
            @Override
            public void onResponse(@NonNull Call<SnippetModel> call, @NonNull Response<SnippetModel> response) {
                ll_upload_progress.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Globals.showToast(getActivity(), getString(R.string.msg_snippet_picture_shared));
                    Utility.deleteInternalCacheFiles(getActivity());
                    startActivity(new Intent(getActivity(), HomePageActivity.class));
                    getActivity().finish();
                } else {
                    Globals.showToast(getActivity(), response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SnippetModel> call, @NonNull Throwable t) {
                ll_upload_progress.setVisibility(View.GONE);
                Globals.showToast(getActivity(), getString(R.string.msg_server_error));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                if (!imageFiles.isEmpty()) {
                    imageFile = imageFiles.get(0).getPath();
                    Glide.with(getActivity())
                            .load(imageFile)
                            .into(iv_snippet_image);
                    selected_image = new File(imageFile);
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA_IMAGE) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getActivity());
                    if (photoFile != null) {
                        photoFile.delete();
                    }
                }
            }
        });
    }

    @Override
    public void onProgressUpdate(int percentage) {
        progress_uploading.setProgress(percentage);
    }

    @Override
    public void onError() {
        Globals.showToast(getActivity(), getString(R.string.msg_server_error));
    }

    @Override
    public void onFinish() {
        progress_uploading.setProgress(100);
    }
}
