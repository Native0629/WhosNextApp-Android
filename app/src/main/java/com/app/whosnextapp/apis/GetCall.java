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

public class GetCall {

    private ResponseListener listener;
    private JSONObject postData;
    private Activity activity;
    private String url;
    private boolean isLoaderRequired;
    private boolean isNoInternetDialog;
    private ACProgressFlower dialog;
    private ProgressUtil progressUtil;
    private APIService mApiService;

    public GetCall(Activity activity, String url, JSONObject postData, boolean isLoaderRequired, boolean isNoInternetDialog, ResponseListener listener) {
        this.listener = listener;
        this.postData = postData;
        this.activity = activity;
        this.url = url;
        this.isLoaderRequired = isLoaderRequired;
        this.isNoInternetDialog = isNoInternetDialog;
        progressUtil = ProgressUtil.getInstance();

        // get retrofit client for making api call
        mApiService = RetrofitClient.getClient(BuildConstants.BASE_URL).create(APIService.class);
    }

    private RequestBody prepareRequestBody() {
        if (postData == null) {
            return null;
        }
        return RequestBody.create(MediaType.parse("application/json; charset=" + Util.UTF_8), (postData).toString());
    }

    public void execute(String header) {
        if (header == null) {
            execute();
            return;
        }

        if (!ConnectionDetector.internetCheck(activity, isNoInternetDialog)) {
            if (listener != null)
                return;
        }
        Call<ResponseBody> bodyCall = null;
        if (isLoaderRequired) {
            dialog = ProgressUtil.initProgressBar(activity);
        }

        if (prepareRequestBody() != null) {
            if (!header.isEmpty())
                bodyCall = mApiService.GetRequest(header, url, prepareRequestBody());
            else
                bodyCall = mApiService.GetRequest(url, prepareRequestBody());
        } else {
            if (!header.isEmpty())
                bodyCall = mApiService.GetRequest(header, url);
            else
                bodyCall = mApiService.GetRequest(url);

        }
        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    progressUtil.hideDialog(dialog, new ProgressBar(activity));
                    if (listener != null) {
                        if (response.isSuccessful() && response.body() != null) {
                            String data = response.body().string();
                            Logger.e(data);
                            listener.onSucceedToPostCall(data);
                        } else if (response.code() == 401 && response.errorBody() != null) {
                            ResponseBody responseBody = response.errorBody();
                            String errData = responseBody.string();
                            listener.onFailedToPostCall(response.code(), activity.getString(R.string.msg_server_error));
                            Logger.e(errData);
                        } else {
                            listener.onFailedToPostCall(response.code(), activity.getString(R.string.msg_server_error));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Logger.e(e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressUtil.hideDialog(dialog, new ProgressBar(activity));
                Logger.e(t.getMessage());
                if (listener != null)
                    listener.onFailedToPostCall(400, t.getMessage());
            }
        });

    }

    public void execute() {
        execute("");
    }

    public interface OnGetServiceCallListener {
        public void onSucceedToGetCall(JSONObject response);

        public void onFailedToGetCall();
    }
}
