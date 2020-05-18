package com.app.whosnextapp.settings;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.app.whosnextapp.BuildConstants;
import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.APIService;
import com.app.whosnextapp.apis.ProgressRequestBody;
import com.app.whosnextapp.apis.ProgressUtil;
import com.app.whosnextapp.apis.RetrofitClient;
import com.app.whosnextapp.drawer.HomePageActivity;
import com.app.whosnextapp.loginregistration.VideoTrimmerActivity;
import com.app.whosnextapp.model.SnippetModel;
import com.app.whosnextapp.utility.AudioTrimBackground;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.cloudist.acplibrary.ACProgressFlower;
import life.knowledge4.videotrimmer.utils.FileUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSnippetActivity extends BaseAppCompatActivity implements ProgressRequestBody.UploadCallbacks, View.OnClickListener {

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

    Globals globals;
    String imagePath, videoPath, audioPath, trimmedAudioPath;
    File videoThumbnailFile, audioThumbnailFile;
    MediaMetadataRetriever mediaMetadataRetriever;
    Bitmap audioImage;
    private FFmpeg ffmpeg;
    private ACProgressFlower dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_snippet);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        globals = (Globals) getApplicationContext();
        setActionBar();
        dialog = ProgressUtil.initProgressBar(getActivity());
        ffmpeg = globals.loadFFMpegBinary(getActivity());
        Bundle bundle = getIntent().getBundleExtra(Constants.WN_EXTRA_VIDEO_TRIM_VIDEO);
        if (bundle != null) {
            videoPath = bundle.getString(Constants.WN_INTENT_VIDEO_FILE);
            afterVideoReturned(new File(videoPath));
        }
        progress_uploading.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
    }

    private void afterVideoReturned(File photoFile) {
        Glide.with(getActivity()).load(photoFile).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                iv_snippet_image.setImageDrawable(resource);
                videoThumbnailFile = Utility.createFileFromResource(getActivity(), resource, Constants.WN_VIDEO_THUMBNAIL);
            }
        });
    }

    private void setActionBar() {
        toolbar.setBackgroundResource(R.drawable.header_bg);
        setSupportActionBar(toolbar);
        toolbar_title.setText(R.string.setting);
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private Activity getActivity() {
        return AddSnippetActivity.this;
    }

    @OnClick(R.id.iv_snippet_image)
    public void onSnippetImageClick() {
        selectSnippetType();
    }

    private void selectSnippetType() {
        CharSequence[] options = new CharSequence[]{getResources().getString(R.string.text_image), getResources().getString(R.string.text_video_simple), getResources().getString(R.string.text_audio)};
        globals.show_dialog(getActivity(), getString(R.string.text_select_snippet), "", options, true, new Globals.OptionDialogClickHanderListener() {
            @Override
            public void OnItemClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        getPermissionForImage();
                        break;
                    case 1:
                        getPermissionForVideo();
                        break;
                    case 2:
                        getPermissionForAudio();
                        break;
                }
            }
        });
    }

    private void getPermissionForAudio() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                showAudioDialog();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Globals.showToast(getActivity(), getString(R.string.permission_denied) + deniedPermissions.toString());
            }
        };
        TedPermission.with(getActivity())
                .setPermissionListener(permissionlistener)
                .setRationaleMessage(getString(R.string.request_audio_permission))
                .setDeniedMessage(getString(R.string.on_denied_permission))
                .setGotoSettingButtonText(getString(R.string.setting))
                .setPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void showAudioDialog() {
        CharSequence[] options = new CharSequence[]{getResources().getString(R.string.text_record), getResources().getString(R.string.text_pick_from_library)};
        globals.show_dialog(getActivity(), getString(R.string.text_browse_audio), "", options, true, new Globals.OptionDialogClickHanderListener() {
            @Override
            public void OnItemClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        startActivity(new Intent(getActivity(), AudioRecorderActivity.class));
                        break;
                    case 1:
                        Intent intent = new Intent();
                        intent.setType(Constants.WN_MEDIA_TYPE_AUDIO);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, Constants.WN_SELECT_AUDIO);
                        break;
                }
            }
        });
    }

    private void getPermissionForImage() {
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
        globals.show_dialog(getActivity(), getString(R.string.text_browse_image), "", options, true, new Globals.OptionDialogClickHanderListener() {
            @Override
            public void OnItemClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        openCameraForImage();
                        break;
                    case 1:
                        openGalleryForImage();
                        break;
                }
            }
        });
    }


    private void getPermissionForVideo() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                showVideoDialog();
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

    private void showVideoDialog() {
        CharSequence[] options = new CharSequence[]{getResources().getString(R.string.text_camera), getResources().getString(R.string.text_gallery)};
        globals.show_dialog(getActivity(), getString(R.string.text_browse_video), "", options, true, new Globals.OptionDialogClickHanderListener() {
            @Override
            public void OnItemClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        openCameraForVideo();
                        break;
                    case 1:
                        openGalleryForVideo();
                        break;
                }
            }
        });
    }

    private void openGalleryForImage() {
        EasyImage.openGallery(getActivity(), 0);
    }

    private void openCameraForImage() {
        EasyImage.openCameraForImage(getActivity(), 0);
    }

    private void openGalleryForVideo() {
        Intent intent = new Intent();
        intent.setTypeAndNormalize(Constants.WN_MEDIA_TYPE_VIDEO);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_video)), Constants.WN_REQUEST_VIDEO_TRIMMER);
    }

    private void openCameraForVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, Constants.WN_VIDEOS_DURATION);
        startActivityForResult(intent, Constants.WN_REQUEST_CAMERA);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                if (!imageFiles.isEmpty()) {
                    imagePath = imageFiles.get(0).getPath();
                    Glide.with(getActivity())
                            .load(imagePath)
                            .into(iv_snippet_image);
                    if (audioPath != null) {
                        audioThumbnailFile = new File(imagePath);
                    }
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

        if (resultCode == RESULT_OK) {
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
            if (requestCode == Constants.WN_SELECT_AUDIO) {
                if (data != null) {
                    Uri selectedAudioUri = data.getData();
                    if (selectedAudioUri != null) {
                        audioPath = FileUtils.getPath(getActivity(), selectedAudioUri);
                        mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(audioPath);
                        String durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        int millSecond = Integer.parseInt(durationStr);
                        if (millSecond > 60000) {
                            new AudioTrimBackground(getActivity(), new AudioTrimBackground.OnResult() {
                                @Override
                                public void success(String trimAudioPath) {
                                    trimmedAudioPath = trimAudioPath;
                                }

                                @Override
                                public void error() {

                                }
                            }).execute(audioPath);
                        } else {
                            trimmedAudioPath = audioPath;
                        }
                        try {
                            mediaMetadataRetriever = new MediaMetadataRetriever();
                            mediaMetadataRetriever.setDataSource(audioPath);
                            byte[] art = mediaMetadataRetriever.getEmbeddedPicture();
                            audioImage = BitmapFactory.decodeByteArray(art, 0, art.length);
                            Drawable drawable = new BitmapDrawable(getResources(), audioImage);
                            audioThumbnailFile = Utility.createFileFromResource(getActivity(), drawable, Constants.WN_AUDIO_THUMBNAIL);
                            Glide.with(getActivity()).load(audioThumbnailFile).into(iv_snippet_image);
                        } catch (Exception e) {
                            audioThumbnailFile = Utility.createFileFromResource(getActivity(), getResources().getDrawable(R.drawable.whosnextapp), Constants.WN_AUDIO_THUMBNAIL);
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void startTrimActivity(@NonNull Uri uri) {
        startActivityForResult(new Intent(getActivity(), VideoTrimmerActivity.class)
                .putExtra(Constants.WN_EXTRA_VIDEO_PATH, FileUtils.getPath(getActivity(), uri))
                .putExtra(Constants.WN_REQUEST_CODE, Constants.WN_VIDEO_TRIM_SNIPPET)
                .putExtra(Constants.WN_VIDEO_DURATION, Constants.WN_VIDEOS_DURATION), Constants.WN_VIDEO_TRIM_SNIPPET);
        finish();
    }

    @OnClick(R.id.btn_save)
    public void onSnippetUpload() {

        if (audioPath != null && ll_upload_progress.getVisibility() != View.VISIBLE) {
            doRequestToUploadSnippetAudio();
        } else if (videoPath != null && ll_upload_progress.getVisibility() != View.VISIBLE) {
            compressVideo(videoPath);
        } else if (imagePath != null && imagePath.length() > 0 && ll_upload_progress.getVisibility() != View.VISIBLE) {
            doRequestToUploadSnippetImage();
        }else{
            Globals.showToast(getActivity(), getString(R.string.err_empty_add_at_least_one_snippet));
        }
    }


    private void compressVideo(String path) {
        String dstFile = Utility.createFile(Constants.WN_VIDEO_FOLDER_PATH, Constants.WN_VIDEO_TRIM_FILE +
                System.currentTimeMillis(), Constants.WN_VIDEO_MP4);

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

        float maxWidth = 720;
        float maxHeight = 1080;

        float imgRatio = width / height;
        float maxRatio = maxWidth / maxHeight;
//      width and height values are set maintaining the aspect ratio of the image
        if (height > maxHeight || width > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / height;
                width = (int) (imgRatio * width);
                height = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / width;
                height = (int) (imgRatio * height);
                width = (int) maxWidth;
            } else {
                height = (int) maxHeight;
                width = (int) maxWidth;

            }
        }

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
                    doRequestToUploadSnippetVideo(dstFile);
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

    private void doRequestToUploadSnippetAudio() {
        ll_upload_progress.setVisibility(View.VISIBLE);
        APIService mApiService = RetrofitClient.getClient(BuildConstants.BASE_URL).create(APIService.class);

        File selected_audio = new File(trimmedAudioPath);

        RequestBody requestFile = RequestBody.create(MediaType.parse(Constants.WN_MEDIA_TYPE_IMAGE), audioThumbnailFile);
        MultipartBody.Part bodyImage = MultipartBody.Part.createFormData(Constants.WN_SNIPPET_IMAGE, audioThumbnailFile.getName(), requestFile);

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

    private void doRequestToUploadSnippetVideo(String path) {
        ll_upload_progress.setVisibility(View.VISIBLE);
        APIService mApiService = RetrofitClient.getClient(BuildConstants.BASE_URL).create(APIService.class);

        File selected_video = new File(path);

        RequestBody requestFile = RequestBody.create(MediaType.parse(Constants.WN_MEDIA_TYPE_IMAGE), videoThumbnailFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData(Constants.WN_SNIPPET_IMAGE, videoThumbnailFile.getName(), requestFile);

        ProgressRequestBody requestBodyVideo = new ProgressRequestBody(selected_video, Constants.WN_MEDIA_TYPE_VIDEO, this);
        MultipartBody.Part bodyVideo = MultipartBody.Part.createFormData(Constants.WN_SNIPPET_VIDEO, selected_video.getName(), requestBodyVideo);

        RequestBody SnippetsCaption = RequestBody.create(MediaType.parse(Constants.WN_MEDIA_TYPE_TEXT), et_snippet_details.getText().toString());

        String requestURL = String.format(getString(R.string.upload_snippet), globals.getUserDetails().getStatus().getCustomerId(), 2);

        Call<SnippetModel> call = mApiService.GetSnippetVideoPost(globals.getUserDetails().getStatus().getUserId(),
                requestURL, bodyVideo, body, SnippetsCaption);

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

    private void doRequestToUploadSnippetImage() {
        ll_upload_progress.setVisibility(View.VISIBLE);
        APIService mApiService = RetrofitClient.getClient(BuildConstants.BASE_URL).create(APIService.class);

        File selected_image = new File(imagePath);

        ProgressRequestBody requestFile = new ProgressRequestBody(selected_image, Constants.WN_MEDIA_TYPE_IMAGE, this);

        MultipartBody.Part body = MultipartBody.Part.createFormData(Constants.WN_SNIPPET_IMAGE, selected_image.getName(), requestFile);

        RequestBody SnippetsCaption = RequestBody.create(MediaType.parse(Constants.WN_MEDIA_TYPE_TEXT), et_snippet_details.getText().toString());

        String requestURL = String.format(getString(R.string.upload_snippet), globals.getUserDetails().getStatus().getCustomerId(), 1);

        Call<SnippetModel> call = mApiService.GetSnippetPicturePost(globals.getUserDetails().getStatus().getUserId(),
                requestURL, body, SnippetsCaption);

        call.enqueue(new Callback<SnippetModel>() {
            @Override
            public void onResponse(@NonNull Call<SnippetModel> call, @NonNull Response<SnippetModel> response) {
                ll_upload_progress.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Globals.showToast(getActivity(), getString(R.string.msg_snippet_picture_shared));
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
    public void onClick(View v) {
        onBackPressed();
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
