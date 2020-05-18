package com.app.whosnextapp.apis;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {

    private static final int DEFAULT_BUFFER_SIZE = 2048;
    private File mFile;
    private UploadCallbacks mListener;
    private String content_type;

    public ProgressRequestBody(final File file, String content_type, final UploadCallbacks listener) {
        this.content_type = content_type;
        mFile = file;
        mListener = listener;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse(content_type + "/*");
    }

    @Override
    public long contentLength() throws IOException {
        Logger.e("contentLength: " + mFile.length());
        return mFile.length();
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        long fileLength = mFile.length();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(mFile);
        long uploaded = 0;

        try {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {

                // update progress on UI thread
                handler.post(new ProgressUpdater(uploaded, fileLength));

                uploaded += read;
                Logger.e("uploaded: " + uploaded);
                sink.write(buffer, 0, read);
            }
        } finally {
            in.close();
        }
    }

    public interface UploadCallbacks {
        void onProgressUpdate(int percentage);

        void onError();

        void onFinish();
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;

        ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;

            Logger.e("mUploaded: " + mUploaded + ", mTotal: " + mTotal);
        }

        @Override
        public void run() {
            mListener.onProgressUpdate((int) (100 * mUploaded / mTotal));
            Logger.e("onProgressUpdater Run(): " + (int) (100 * mUploaded / mTotal));
        }
    }

}
