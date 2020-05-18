package com.app.whosnextapp.videos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.app.whosnextapp.BuildConstants;
import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.APIService;
import com.app.whosnextapp.apis.HttpRequestHandler;
import com.app.whosnextapp.apis.ProgressRequestBody;
import com.app.whosnextapp.apis.ProgressUtil;
import com.app.whosnextapp.apis.RetrofitClient;
import com.app.whosnextapp.drawer.HomePageActivity;
import com.app.whosnextapp.model.ShareToModel;
import com.app.whosnextapp.model.TagListModel;
import com.app.whosnextapp.notification.NotificationActivity;
import com.app.whosnextapp.pictures.CaptionActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.app.whosnextapp.utility.Utility;
import com.bumptech.glide.Glide;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.orhanobut.logger.Logger;
import com.vincent.videocompressor.VideoCompress;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.cloudist.acplibrary.ACProgressFlower;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class FollowersFragment extends Fragment implements ProgressRequestBody.UploadCallbacks {
    @BindView(R.id.iv_video_thumbnail)
    AppCompatImageView iv_video_thumbnail;
    @BindView(R.id.tv_write_caption)
    AppCompatTextView tv_write_caption;
    @BindView(R.id.btn_share)
    AppCompatButton btn_share;
    @BindView(R.id.ll_share_video_progress)
    LinearLayout ll_share_video_progress;
    @BindView(R.id.progress_uploading)
    ProgressBar progress_uploading;
    ArrayList<TagListModel> tagListModels = new ArrayList<>();
    String camera_picture, tag_list, caption;
    private String selectedVideoPath;
    private ShareToActivity mContext;
    private Globals globals;
    private FFmpeg ffmpeg;
    private ACProgressFlower dialog;

    public static FollowersFragment newInstance() {
        return new FollowersFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (ShareToActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_followers, container, false);
        ButterKnife.bind(this, v);
        globals = (Globals) mContext.getApplicationContext();
        dialog = ProgressUtil.initProgressBar(getActivity());
        ffmpeg = globals.loadFFMpegBinary(mContext);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progress_uploading.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        ll_share_video_progress.setVisibility(View.GONE);
        if (mContext.getIntent() != null) {
            Bundle bundleData = mContext.getIntent().getBundleExtra(Constants.WN_EXTRA_VIDEO_TRIM_VIDEO);
            selectedVideoPath = bundleData.getString(Constants.WN_INTENT_VIDEO_FILE);
            onVideoReturned(new File(selectedVideoPath));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_page_menu, menu);
    }

    @OnClick(R.id.tv_write_caption)
    public void onCaptionClick() {
        Intent i = new Intent(getContext(), CaptionActivity.class);
        i.putExtra(Constants.WN_SHARED_VIDEO_CAPTION, caption);
        startActivityForResult(i, Constants.WN_CAPTION);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.header_bell).setVisible(true);
        menu.findItem(R.id.header_search).setVisible(false);
        menu.findItem(R.id.header_refresh).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.header_bell) {
            startActivity(new Intent(getActivity(), NotificationActivity.class));
        }
        return false;
    }


    @OnClick(R.id.btn_share)
    public void btnShareClick() {
        if (ll_share_video_progress.getVisibility() != View.VISIBLE) {
            compressVideo(selectedVideoPath);
        }
    }

    private void onVideoReturned(File photoFile) {
        Glide.with(mContext).load(photoFile).into(iv_video_thumbnail);
    }

    private void compressVideo(String path) {
        String dstFile = Utility.createFile(Constants.WN_VIDEO_FOLDER_PATH,
                Constants.WN_VIDEO_TRIM_FILE +
                        System.currentTimeMillis(), Constants.WN_VIDEO_MP4);

        VideoCompress.compressVideoMedium(path, dstFile, new VideoCompress.CompressListener() {
            @Override
            public void onStart() {

                ll_share_video_progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess() {

                uploadVideo(new File(dstFile));
            }

            @Override
            public void onFail() {
                ll_share_video_progress.setVisibility(View.GONE);

            }

            @Override
            public void onProgress(float percent) {
            }
        });

    }


//    private void compressVideo(String path) {
//        String dstFile = Utility.createFile(Constants.WN_VIDEO_FOLDER_PATH,
//                Constants.WN_VIDEO_TRIM_FILE +
//                        System.currentTimeMillis(), Constants.WN_VIDEO_MP4);
//
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(path);
//        int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
//        int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
//
//
//        float maxWidth = 720;
//        float maxHeight = 1080;
//
//        float imgRatio = width / height;
//        float maxRatio = maxWidth / maxHeight;
////      width and height values are set maintaining the aspect ratio of the image
//        if (height > maxHeight || width > maxWidth) {
//            if (imgRatio < maxRatio) {
//                imgRatio = maxHeight / height;
//                width = (int) (imgRatio * width);
//                height = (int) maxHeight;
//            } else if (imgRatio > maxRatio) {
//                imgRatio = maxWidth / width;
//                height = (int) (imgRatio * height);
//                width = (int) maxWidth;
//            } else {
//                height = (int) maxHeight;
//                width = (int) maxWidth;
//
//            }
//        }
//
//        //-b:a audio bitrate
//        //-b:v specificies video bitrate
//        //-i input file
//        //-b:v videobitrate of output video in kilobytes
//        //-vcodec videocodec (use ffmpeg -codecs to list all available codecs)
//        //-ac arithmetic coder
//
//
//
////        String[] complexCommand = {"-y", "-i", path, "-s", width + "*" + height, "-r", "40", "-vcodec", "mpeg4", "-b:v",
////                "850k", "-b:a", "48000", "-ac", "2", "-ar", "22050",
////                (new File(dstFile)).getAbsolutePath()};
//
//        String[] complexCommand = {"-y", "-i", path, "-s", width + "*" + height, "-r", "40", "-vcodec", "mpeg4", "-b:v",
//                "850k", "-b:a", "48000", "-ac", "2", "-ar", "22050",
//                (new File(dstFile)).getAbsolutePath()};
//
//
//        execFFmpegBinary(complexCommand, dstFile);
//    }

//    private void execFFmpegBinary(final String[] command, String dstFile) {
//        try {
//            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
//                @Override
//                public void onSuccess(String message) {
//                    super.onSuccess(message);
//                    uploadVideo(new File(dstFile));
//                }
//
//                @Override
//                public void onFailure(String message) {
//                    ll_share_video_progress.setVisibility(View.GONE);
//                    super.onFailure(message);
//                }
//
//                @Override
//                public void onProgress(String message) {
//                    super.onProgress(message);
//                }
//
//                @Override
//                public void onStart() {
//                    ll_share_video_progress.setVisibility(View.VISIBLE);
//                    super.onStart();
//                    dialog.show();
//                }
//
//                @Override
//                public void onFinish() {
//                    super.onFinish();
//                    dialog.dismiss();
//                }
//            });
//        } catch (FFmpegCommandAlreadyRunningException e) {
//            e.printStackTrace();
//        }
//    }

    private void uploadVideo(File selectedVideo) {
        int imgHeight = iv_video_thumbnail.getHeight();
        int imgWidth = iv_video_thumbnail.getWidth();

        APIService mApiService = RetrofitClient.getClient(BuildConstants.BASE_URL).create(APIService.class);

//        JSONObject postData = HttpRequestHandler.getInstance().getShareVideoJson(tv_write_caption.getText().toString(), 1, 0,
//                0, "", "", 0, 0, globals.getUserDetails().getStatus().getCustomerId(),
//                globals.getUserDetails().getStatus().getUserName(), (int) Globals.dpFromPx(mContext, imgWidth), Globals.dpFromPx(mContext, imgHeight));

        JSONObject postData = HttpRequestHandler.getInstance().getShareVideoJson(tv_write_caption.getText().toString(), 1, 0,
                0, "", "", 0, 0, globals.getUserDetails().getStatus().getCustomerId(),
                globals.getUserDetails().getStatus().getUserName(), (int) Globals.dpFromPx(mContext, imgWidth), Globals.dpFromPx(mContext, (float) (imgHeight/1.05)));

//        JSONObject postData = HttpRequestHandler.getInstance().getShareVideoJson(tv_write_caption.getText().toString(), 1, 0,
//                0, "", "", 0, 0, globals.getUserDetails().getStatus().getCustomerId(),
//                globals.getUserDetails().getStatus().getUserName(), imgWidth,imgHeight);


        ProgressRequestBody fileBody = new ProgressRequestBody(selectedVideo, "video", this);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("", selectedVideo.getName(), fileBody);

        Call<ShareToModel> call = mApiService.GetSharePost(getString(R.string.add_post_v5),
                filePart, RequestBody.create(MediaType.parse(Constants.WN_MEDIA_TYPE_TEXT), postData.toString()));

        call.enqueue(new Callback<ShareToModel>() {
            @Override
            public void onResponse
                    (@NonNull Call<ShareToModel> call, @NonNull Response<ShareToModel> response) {

                ll_share_video_progress.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    Globals.showToast(mContext, getString(R.string.msg_feed_shared));
                    startActivity(new Intent(mContext, HomePageActivity.class));
                    mContext.finish();
                } else {
                    Logger.e("ShareTo Response Else condition Fail =>", "" + response.isSuccessful() + " , " + response.body());
                    Globals.showToast(mContext, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ShareToModel> call, @NonNull Throwable t) {
                ll_share_video_progress.setVisibility(View.GONE);
                Logger.e("ShareTo Response =>", "onFailure, " + t.getMessage());
                Globals.showToast(getActivity(), getString(R.string.msg_server_error));
            }
        });
    }

    /*Retrofit Video Upload Progress*/
    @Override
    public void onProgressUpdate(int percentage) {
        // set current progress
        progress_uploading.setProgress(percentage);
    }

    @Override
    public void onError() {
        Globals.showToast(mContext, getString(R.string.msg_server_error));
    }

    @Override
    public void onFinish() {
        progress_uploading.setProgress(100);
    }

    /*Handle unCertain BackButton Press or App Close*/
    @Override
    public void onPause() {
        if (ll_share_video_progress.getVisibility() == View.VISIBLE) {
            RetrofitClient.cancelAllRequest();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (ll_share_video_progress.getVisibility() == View.VISIBLE) {
            RetrofitClient.cancelAllRequest();
        }
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.WN_CAPTION && data != null) {
                caption = data.getStringExtra(Constants.WN_SHARED_VIDEO_CAPTION);
                tv_write_caption.setText(caption);
            }
        }
    }
}
