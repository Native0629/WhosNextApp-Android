package com.app.whosnextapp.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.app.whosnextapp.apis.ProgressUtil;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;

import cc.cloudist.acplibrary.ACProgressFlower;
import life.knowledge4.videotrimmer.utils.FileUtils;

public class ConvertImageBase64 extends AsyncTask<Uri, Void, String> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ACProgressFlower dialog;
    private OnResult onResult;
    private String resultString;

    public ConvertImageBase64(Context context, OnResult onResult) {
        this.context = context;
        this.onResult = onResult;
        resultString = "";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressUtil.initProgressBar(context);
        dialog.show();
    }

    @Override
    protected String doInBackground(Uri... uris) {
        try {
            return encodeFileToBase64Binary(FileUtils.getPath(context, uris[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null) {
            resultString = s;
            onResult.success(resultString);
        } else {
            onResult.error();
        }
        dialog.dismiss();

    }

    private String encodeFileToBase64Binary(String fileName) throws IOException {
        File file = new File(fileName);
        byte[] bytes = Utility.loadFile(file);
        byte[] encoded = Base64.encodeBase64(bytes);
        return new String(encoded);
    }

    public interface OnResult {
        void success(String stringBase64);

        void error();
    }
}
