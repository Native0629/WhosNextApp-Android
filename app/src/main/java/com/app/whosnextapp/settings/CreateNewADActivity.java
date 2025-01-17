package com.app.whosnextapp.settings;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.BuildConstants;
import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.APIService;
import com.app.whosnextapp.apis.HttpRequestHandler;
import com.app.whosnextapp.apis.ProgressRequestBody;
import com.app.whosnextapp.apis.ProgressUtil;
import com.app.whosnextapp.apis.RetrofitClient;
import com.app.whosnextapp.loginregistration.VideoTrimmerActivity;
import com.app.whosnextapp.model.LikeUnlikePostModel;
import com.app.whosnextapp.notification.NotificationActivity;
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
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewADActivity extends BaseAppCompatActivity implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;
    @BindView(R.id.iv_image)
    AppCompatImageView iv_image;
    @BindView(R.id.iv_upload)
    AppCompatImageView iv_upload;
    @BindView(R.id.et_description)
    AppCompatEditText et_description;
    @BindView(R.id.et_button_name)
    AppCompatEditText et_button_name;
    @BindView(R.id.et_button_link)
    AppCompatEditText et_button_link;
    @BindView(R.id.btn_submit)
    AppCompatTextView btn_submit;
    @BindView(R.id.rv_ads)
    RecyclerView rv_ads;

    Globals globals;
    String imagePath;
    String profile_pic_base;
    String videoPath;
    File videoThumbnailFile;
    int IsImage, IsVideo;
    List<File> imageslist = new ArrayList<>();
    private FFmpeg ffmpeg;
    private ACProgressFlower dialog;
    private SelectMultipleImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_ad);
        ButterKnife.bind(this);
        init();
    }

    private Activity getActivity() {
        return CreateNewADActivity.this;
    }

    private void init() {
        globals = (Globals) getApplicationContext();
        dialog = ProgressUtil.initProgressBar(getActivity());
        toolbar.setBackgroundResource(R.drawable.header_bg);
        setSupportActionBar(toolbar);
        toolbar_title.setText(getString(R.string.create_new));
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.backarrow);
        ffmpeg = globals.loadFFMpegBinary(this);
        toolbar.setNavigationOnClickListener(this);
    }

    @OnClick(R.id.ll_camera)
    public void onCameraClick() {
        getPermissionForImage();
    }

    @OnClick(R.id.ll_video)
    public void onVideoClick() {
        getPermissionForVideo();
    }

    @OnClick(R.id.btn_submit)
    public void onSubmitClick() {
        if (isValid()) {
            doRequestForCreateAd();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        menu.findItem(R.id.header_bell).setVisible(true);
        final MenuItem menuItem = menu.findItem(R.id.header_bell);
        View actionView = MenuItemCompat.getActionView(menuItem);
        // tv_noitfication_count = actionView.findViewById(R.id.tv_noitfication_count);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.header_bell) {
            startActivity(new Intent(this, NotificationActivity.class));
        }
        return false;
    }

    private void showDialog() {
        CharSequence[] options = new CharSequence[]{getResources().getString(R.string.take_from_camera), getResources().getString(R.string.select_from_library)};
        globals.show_dialog(this, getString(R.string.text_browse_image), "", options, true, new Globals.OptionDialogClickHanderListener() {
            @Override
            public void OnItemClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        openCamera();
                        break;
                    case 1:
                        openGallery();
                        break;
                }
            }
        });
    }

    private void openCamera() {
        EasyImage.openCameraForImage(CreateNewADActivity.this, 0);
    }

    private void openGallery() {
        EasyImage.clearConfiguration(this);
        EasyImage.configuration(this).setAllowMultiplePickInGallery(true);

        EasyImage.openGallery(CreateNewADActivity.this, 0);

    }

    private void getPermissionForImage() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                showDialog();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Globals.showToast(CreateNewADActivity.this, getString(R.string.permission_denied) + deniedPermissions.toString());
            }
        };
        TedPermission.with(CreateNewADActivity.this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage(getString(R.string.request_camera_permission))
                .setDeniedMessage(getString(R.string.on_denied_permission))
                .setGotoSettingButtonText(getString(R.string.setting))
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                imageslist = new ArrayList<>();
                if (!imageFiles.isEmpty()) {
                    imageslist.addAll(imageFiles);
                    if (EasyImage.configuration(getActivity()).allowsMultiplePickingInGallery()) {
                        iv_image.setVisibility(View.GONE);
                        iv_upload.setVisibility(View.GONE);
                        rv_ads.setVisibility(View.VISIBLE);
                        if (imageAdapter == null) {
                            imageAdapter = new SelectMultipleImageAdapter(getActivity());
                        }

                        imageAdapter.doRefresh(new ArrayList<File>(imageFiles));

                        if (rv_ads.getAdapter() == null) {
                            rv_ads.setHasFixedSize(false);
                            GalleryLayoutManager multiplePickingImage = new GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL);
                            multiplePickingImage.setItemTransformer(new ScaleTransformer(true));
                            multiplePickingImage.attach(rv_ads, 0);
                            multiplePickingImage.setCallbackInFling(true);
                            //  rv_ads.setLayoutManager(new GridLayoutManager(CreateNewADActivity.this,3));
                            rv_ads.setLayoutManager(multiplePickingImage);
                            rv_ads.setAdapter(imageAdapter);
                            IsImage = 1;
                            IsVideo = 0;
                        }


                    } else {
                        rv_ads.setVisibility(View.GONE);
                        iv_image.setVisibility(View.GONE);
                        imagePath = imageFiles.get(0).getPath();
                        Glide.with(getActivity())
                                .load(imagePath)
                                .into(iv_upload);
                        IsImage = 1;
                        IsVideo = 0;

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

    private void afterVideoReturned(File photoFile) {
        Glide.with(getActivity()).load(photoFile).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                iv_image.setVisibility(View.GONE);
                iv_upload.setImageDrawable(resource);
                videoThumbnailFile = Utility.createFileFromResource(getActivity(), resource, Constants.WN_VIDEO_THUMBNAIL);
                IsImage = 0;
                IsVideo = 1;
            }
        });
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
                    //path = Uri.parse(dstFile);
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
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }


    private void startTrimActivity(@NonNull Uri uri) {
        startActivityForResult(new Intent(CreateNewADActivity
                        .this, VideoTrimmerActivity.class)
                        .putExtra(Constants.WN_EXTRA_VIDEO_PATH, FileUtils.getPath(this, uri)),
                Constants.WN_VIDEO_TRIM);
    }

    private void getPermissionForVideo() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                showVideoDialog();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Globals.showToast(getApplicationContext(), getString(R.string.permission_denied) + deniedPermissions.toString());
            }
        };
        TedPermission.with(CreateNewADActivity.this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage(getString(R.string.request_camera_permission))
                .setDeniedMessage(getString(R.string.on_denied_permission))
                .setGotoSettingButtonText(getString(R.string.setting))
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void showVideoDialog() {
        CharSequence[] options = new CharSequence[]{getResources().getString(R.string.video_option), getResources().getString(R.string.text_pick_video)};
        globals.show_dialog(this, getString(R.string.text_browse_video), "", options, true, new Globals.OptionDialogClickHanderListener() {
            @Override
            public void OnItemClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        openCameraForVideo();
                        break;
                    case 1:
                        openVideoGallery();
                        break;
                }
            }
        });
    }

    private void openVideoGallery() {
        Intent intent = new Intent();
        intent.setTypeAndNormalize(Constants.WN_MEDIA_TYPE_VIDEO);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_video)), Constants.WN_REQUEST_VIDEO_TRIMMER);
    }

    private void openCameraForVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, Constants.WN_BIO_VIDEO_DURATION);
        startActivityForResult(intent, Constants.WN_REQUEST_CAMERA);
    }

//    public void doRequestForCreateAd() {
//        JSONObject postData = HttpRequestHandler.getInstance()
//                .getAddNewAdJSON(et_button_link.getText().toString(), et_button_name.getText().toString(), 320, 320, et_description.getText().toString(), profile_pic_base64, IsImage, IsVideo, 320, video_base64, profile_pic_base, 320);
//        new PostRequest(this, postData, getString(R.string.add_new_ads), true, true, new ResponseListener() {
//            @Override
//            public void onSucceedToPostCall(String response) {
//                startActivity(new Intent(CreateNewADActivity.this, AdsActivity.class));
//            }
//
//            @Override
//            public void onFailedToPostCall(int statusCode, String msg) {
//
//            }
//        }).execute(globals.getUserDetails().getStatus().getUserId());
//    }


    public void doRequestForCreateAd() {
        Call<LikeUnlikePostModel> call = null;
        APIService mApiService = RetrofitClient.getClient(BuildConstants.BASE_URL).create(APIService.class);
        try {

            String requestURL = getString(R.string.add_new_ads);

            if (IsImage == 1) {
                if (imageslist.size() == 0) {
                    File selected_image = new File(imagePath);
                    ProgressRequestBody requestFile = new ProgressRequestBody(selected_image, Constants.WN_MEDIA_TYPE_IMAGE, this);
                    MultipartBody.Part body = MultipartBody.Part.createFormData(Constants.WN_ADVERTISE_PIC, selected_image.getName(), requestFile);
                    call = mApiService.AddNewAdsImagePost(globals.getUserDetails().getStatus().getUserId(), requestURL, body, RequestBody.create(MultipartBody.FORM, (
                            HttpRequestHandler.getInstance()
                                    .getAddNewAdJSON(et_button_link.getText().toString(), et_button_name.getText().toString(), 320, 320, et_description.getText().toString(), "", IsImage, IsVideo, 320, "", profile_pic_base, 320).toString())));

                } else {
                    if (!imageslist.isEmpty()) {
                        for (int i = 0; i < imageslist.size(); i++) {
                            File selected_image = new File(String.valueOf(imageslist.get(i)));
                            ProgressRequestBody requestFile = new ProgressRequestBody(selected_image, Constants.WN_MEDIA_TYPE_IMAGE, this);
                            MultipartBody.Part body = MultipartBody.Part.createFormData(Constants.WN_ADVERTISE_PIC, selected_image.getName(), requestFile);
                            call = mApiService.AddNewAdsImagePost(globals.getUserDetails().getStatus().getUserId(), requestURL, body, RequestBody.create(MultipartBody.FORM, (
                                    HttpRequestHandler.getInstance()
                                            .getAddNewAdJSON(et_button_link.getText().toString(), et_button_name.getText().toString(), 320, 320, et_description.getText().toString(), "", IsImage, IsVideo, 320, "", profile_pic_base, 320).toString())));
                        }
                    }
                }
            } else {
                File selected_video = new File(videoPath);
                ProgressRequestBody requestBodyVideo = new ProgressRequestBody(selected_video, Constants.WN_MEDIA_TYPE_VIDEO, this);
                MultipartBody.Part bodyVideo = MultipartBody.Part.createFormData(Constants.WN_ADVERTISE_VIDEO, selected_video.getName(), requestBodyVideo);
                call = mApiService.AddNewAdsImagePost(globals.getUserDetails().getStatus().getUserId(), requestURL, bodyVideo, RequestBody.create(MultipartBody.FORM, (
                        HttpRequestHandler.getInstance()
                                .getAddNewAdJSON(et_button_link.getText().toString(), et_button_name.getText().toString(), 320, 320, et_description.getText().toString(), "", IsImage, IsVideo, 320, "", profile_pic_base, 320).toString())));

            }

            dialog.show();
            call.enqueue(new Callback<LikeUnlikePostModel>() {
                @Override
                public void onResponse(Call<LikeUnlikePostModel> call, Response<LikeUnlikePostModel> response) {
                    LikeUnlikePostModel likeUnlikePostModel = response.body();
                    if (likeUnlikePostModel.getResult().equals(Constants.WN_SUCCESS)) {
                        startActivity(new Intent(CreateNewADActivity.this, AdsActivity.class));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<LikeUnlikePostModel> call, Throwable t) {

                }
            });


        } catch (Exception e) {

        }
    }


    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    private boolean isValid() {
        if (imageslist == null) {
            Globals.showToast(this, getString(R.string.text_Please_upload_image));
        }
        if (et_description.getText().toString().trim().isEmpty()) {
            Globals.showToast(this, getString(R.string.err_empty_description));
            return false;
        }
        if (et_button_name.getText().toString().trim().isEmpty()) {
            Globals.showToast(this, getString(R.string.err_empty_button_name));
            return false;
        }
        if (et_button_link.getText().toString().trim().isEmpty()) {
            Globals.showToast(this, getString(R.string.err_empty_button_link));
            return false;
        }

        return true;
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
}
