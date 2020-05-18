package com.app.whosnextapp.videos.groupvideo;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.BuildConstants;
import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.APIService;
import com.app.whosnextapp.apis.HttpRequestHandler;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ProgressRequestBody;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.apis.RetrofitClient;
import com.app.whosnextapp.drawer.HomePageActivity;
import com.app.whosnextapp.loginregistration.VideoTrimmerActivity;
import com.app.whosnextapp.model.GetGroupVideoIndividualModel;
import com.app.whosnextapp.model.UploadPictureModel;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.app.whosnextapp.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.vincent.videocompressor.VideoCompress;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.knowledge4.videotrimmer.utils.FileUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddGroupVideoByUserActivity extends BaseAppCompatActivity implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;
    @BindView(R.id.rv_post)
    RecyclerView rv_post;
    @BindView(R.id.btn_submit)
    AppCompatButton btn_submit;
    @BindView(R.id.iv_group_video_thumbnail_image)
    AppCompatImageView iv_group_video_thumbnail_image;
    @BindView(R.id.et_video_details)
    AppCompatEditText et_video_details;
    @BindView(R.id.ll_upload_progress)
    LinearLayout ll_upload_progress;
    @BindView(R.id.progress_uploading)
    ProgressBar progress_uploading;

    String postId, videoPath;
    Globals globals;
    GroupVideoIndividualPostAdapter groupVideoIndividualPostAdapter;
    private ArrayList<GetGroupVideoIndividualModel.Result> groupVideoPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_video_by_user);
        init();
    }

    private Activity getActivity() {
        return AddGroupVideoByUserActivity.this;
    }

    private void init() {
        ButterKnife.bind(this);
        globals = (Globals) getApplicationContext();
        setActionBar();
        if (getIntent().getExtras() != null) {
            postId = getIntent().getExtras().getString(Constants.WN_GROUP_POST_ID);
        }
        doRequestForGroupVideoPost();
    }

    private void afterVideoReturned(File photoFile) {
        Glide.with(getActivity()).load(photoFile).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                iv_group_video_thumbnail_image.setImageDrawable(resource);
            }
        });
    }

    private void setActionBar() {
        toolbar.setBackgroundResource(R.drawable.header_bg);
        setSupportActionBar(toolbar);
        toolbar_title.setText(R.string.text_videos);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(this);
    }

    private void doRequestForGroupVideoPost() {
        String requestUrl = String.format(getString(R.string.get_individual_group_video_by_postId), postId);
        new PostRequest(this, null, requestUrl, false, false, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                GetGroupVideoIndividualModel getGroupVideoIndividualModel = new Gson().fromJson(response, GetGroupVideoIndividualModel.class);
                if (getGroupVideoIndividualModel != null && getGroupVideoIndividualModel.getResult() != null) {
                    groupVideoPost = getGroupVideoIndividualModel.getResult();
                    setGroupVideoPostAdapter(groupVideoPost);
                }
            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {

            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    private void setGroupVideoPostAdapter(ArrayList<GetGroupVideoIndividualModel.Result> groupVideoPost) {
        if (groupVideoIndividualPostAdapter == null) {
            groupVideoIndividualPostAdapter = new GroupVideoIndividualPostAdapter(getActivity());
        }
        if (rv_post.getAdapter() == null) {
            rv_post.setHasFixedSize(true);
            rv_post.setLayoutManager(new GridLayoutManager(getActivity(), 5));
            rv_post.setAdapter(groupVideoIndividualPostAdapter);
        }
        groupVideoIndividualPostAdapter.doRefresh(groupVideoPost);
    }

    @OnClick(R.id.iv_group_video_thumbnail_image)
    public void onGroupVideoImageClick() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                groupVideoSelectionDialog();
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

    private void groupVideoSelectionDialog() {
        CharSequence[] options = new CharSequence[]{getResources().getString(R.string.text_capture_video), getResources().getString(R.string.text_pick_video)};
        globals.show_dialog(getActivity(), getString(R.string.select_video), "", options, true, new Globals.OptionDialogClickHanderListener() {
            @Override
            public void OnItemClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        openCameraForGroupVideo();
                        break;
                    case 1:
                        openGalleryForGroupVideo();
                        break;
                }
            }
        });
    }

    private void openGalleryForGroupVideo() {
        Intent intent = new Intent();
        intent.setTypeAndNormalize(Constants.WN_MEDIA_TYPE_VIDEO);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_video)), Constants.WN_REQUEST_VIDEO_TRIMMER_GROUP);
    }

    private void openCameraForGroupVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, Constants.WN_BIO_VIDEO_DURATION);
        startActivityForResult(intent, Constants.WN_REQUEST_CAMERA_GROUP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.WN_REQUEST_CAMERA_GROUP) {
                if (data != null) {
                    Uri selectedMediaUri = data.getData();
                    if (selectedMediaUri != null) {
                        startGroupVideoTrimActivity(selectedMediaUri);
                    } else {
                        Globals.showToast(getActivity(), getString(R.string.toast_cannot_retrieve_selected_video));
                    }
                }
            }

            if (requestCode == Constants.WN_REQUEST_VIDEO_TRIMMER_GROUP) {
                if (data != null) {
                    final Uri selectedUri = data.getData();
                    if (selectedUri != null) {
                        startGroupVideoTrimActivity(selectedUri);
                    } else {
                        Globals.showToast(getActivity(), getString(R.string.toast_cannot_retrieve_selected_video));
                    }
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

    private void startGroupVideoTrimActivity(Uri selectedMediaUri) {
        startActivityForResult(new Intent(getActivity(), VideoTrimmerActivity.class)
                .putExtra(Constants.WN_EXTRA_VIDEO_PATH, FileUtils.getPath(getActivity(), selectedMediaUri)), Constants.WN_VIDEO_TRIM);
    }

    @OnClick(R.id.btn_submit)
    public void onSubmitClick() {
        if (videoPath != null)
            compressVideo(videoPath);
    }

    private void doRequestToUploadPost(File videoPath) {
        int imgHeight = iv_group_video_thumbnail_image.getHeight();
        int imgWidth = iv_group_video_thumbnail_image.getWidth();

        ll_upload_progress.setVisibility(View.VISIBLE);

        APIService mApiService = RetrofitClient.getClient(BuildConstants.BASE_URL).create(APIService.class);

        // File selected_video = new File(videoPath);

        JSONObject postData = HttpRequestHandler.getInstance().getUploadGroupPostJson
                (String.valueOf((int) Globals.dpFromPx(getActivity(), imgWidth)), "", "0", Integer.valueOf(postId),
                        String.valueOf(Globals.dpFromPx(getActivity(), imgHeight)),
                        et_video_details.getText().toString(), "", "",
                        globals.getUserDetails().getStatus().getCustomerId(), "", "");

        ProgressRequestBody requestBodyVideo = new ProgressRequestBody(videoPath, Constants.WN_MEDIA_TYPE_VIDEO, this);
        MultipartBody.Part bodyVideo = MultipartBody.Part.createFormData(Constants.WN_SNIPPET_VIDEO, videoPath.getName(), requestBodyVideo);

        Call<UploadPictureModel.GroupVideoList> call = mApiService.GetPicturePost(getString(R.string.add_individual_group_video),
                bodyVideo, RequestBody.create(MediaType.parse(Constants.WN_MEDIA_TYPE_TEXT), postData.toString()), globals.getUserDetails().getStatus().getUserId());

        call.enqueue(new Callback<UploadPictureModel.GroupVideoList>() {
            @Override
            public void onResponse(Call<UploadPictureModel.GroupVideoList> call, Response<UploadPictureModel.GroupVideoList> response) {
                ll_upload_progress.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    startActivity(new Intent(getActivity(), HomePageActivity.class));
                    getActivity().finish();
                } else {
                    Globals.showToast(getActivity(), response.message());
                }
            }

            @Override
            public void onFailure(Call<UploadPictureModel.GroupVideoList> call, Throwable t) {
                ll_upload_progress.setVisibility(View.GONE);
                Globals.showToast(getActivity(), getString(R.string.msg_server_error));
            }
        });
    }

    private void compressVideo(String path) {
        String dstFile = Utility.createFile(Constants.WN_VIDEO_FOLDER_PATH,
                Constants.WN_VIDEO_TRIM_FILE +
                        System.currentTimeMillis(), Constants.WN_VIDEO_MP4);

        VideoCompress.compressVideoMedium(path, dstFile, new VideoCompress.CompressListener() {
            @Override
            public void onStart() {
                ll_upload_progress.setVisibility(View.VISIBLE);

            }

            @Override
            public void onSuccess() {

                doRequestToUploadPost(new File(dstFile));
            }

            @Override
            public void onFail() {
                ll_upload_progress.setVisibility(View.GONE);

            }

            @Override
            public void onProgress(float percent) {
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
