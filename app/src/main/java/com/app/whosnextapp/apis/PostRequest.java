package com.app.whosnextapp.apis;

import android.app.Activity;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.app.whosnextapp.BuildConstants;
import com.app.whosnextapp.R;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.IOException;

import cc.cloudist.acplibrary.ACProgressFlower;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostRequest {
    private APIService mApiService;
    private Activity activity;
    private JSONObject postData;
    private String requestUrl;
    private boolean isLoaderRequired, isNoInternetDialog = true;
    private ResponseListener listener;
    private ProgressUtil progressUtil;
    private ACProgressFlower dialog = null;

    public PostRequest(Activity activity, JSONObject postData, String requestUrl, boolean isLoaderRequired,
                       boolean isNoInternetDialog, ResponseListener listener) {
        this.activity = activity;
        this.postData = postData;
        this.requestUrl = requestUrl;
        this.isLoaderRequired = isLoaderRequired;
        this.isNoInternetDialog = isNoInternetDialog;
        this.listener = listener;
        progressUtil = ProgressUtil.getInstance();

        mApiService = RetrofitClient.getClient(BuildConstants.BASE_URL).create(APIService.class);
    }

    private RequestBody prepareRequestBody() {
        if (postData == null) {
            return null;
        }
        return RequestBody.create(MediaType.parse("application/json; charset=" + Util.UTF_8), (postData).toString());
    }

    public void execute(String header) {
        if (!ConnectionDetector.internetCheck(activity, isNoInternetDialog)) {
            return;
        }

        if (isLoaderRequired) {
            dialog = ProgressUtil.initProgressBar(activity);
        }

        if (mApiService != null) {
            progressUtil.showDialog(dialog, new ProgressBar(activity), isLoaderRequired);

            Call<ResponseBody> bodyCall = null;
            if (prepareRequestBody() != null) {
                if (!header.isEmpty())
                    bodyCall = mApiService.PostRequest(header, requestUrl, prepareRequestBody());
                else
                    bodyCall = mApiService.PostRequest(requestUrl, prepareRequestBody());
            } else {
                if (!header.isEmpty())
                    bodyCall = mApiService.PostRequest(header, requestUrl);
                else
                    bodyCall = mApiService.PostRequest(requestUrl);

            }

            bodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    try {
                        progressUtil.hideDialog(dialog, new ProgressBar(activity));
                        if (listener != null) {
                            if (response.isSuccessful() && response.body() != null) {
                                String data = response.body().string();
                                Logger.json(data);
                                listener.onSucceedToPostCall(data);
                            } else if (response.code() == 401 && response.errorBody() != null) {
                                ResponseBody responseBody = response.errorBody();
                                String errData = responseBody.string();
                                listener.onFailedToPostCall(response.code(), activity.getString(R.string.msg_server_error));
                                Logger.json(errData);
                            } else {
                                listener.onFailedToPostCall(response.code(), activity.getString(R.string.msg_server_error));
                            }
                        }
                    } catch (IOException e) {
                        progressUtil.hideDialog(dialog, new ProgressBar(activity));
                        listener.onFailedToPostCall(response.code(), activity.getString(R.string.msg_server_error));
                        e.printStackTrace();
                        Logger.json(e.getMessage());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    progressUtil.hideDialog(dialog, new ProgressBar(activity));
                    Logger.json(t.getMessage());
                    if (listener != null)
                        listener.onFailedToPostCall(400, activity.getString(R.string.error_msg_connection_timeout));
                }
            });
        }
    }

    public void execute() {
        execute("");
    }

    public void cancelRequest() {
        mApiService.PostRequest(requestUrl, prepareRequestBody()).cancel();
    }

}