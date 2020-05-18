package com.app.whosnextapp.utility;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.ProgressUtil;
import com.app.whosnextapp.audiotrimmer.SoundFile;

import java.io.File;

import cc.cloudist.acplibrary.ACProgressFlower;
import life.knowledge4.videotrimmer.utils.FileUtils;

public class AudioTrimBackground extends AsyncTask<String, Void, String> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private OnResult onResult;
    private String resultString;
    private SoundFile mSoundFile;
    private ACProgressFlower dialog;

    public AudioTrimBackground(Context context, OnResult onResult) {
        this.context = context;
        this.onResult = onResult;
        resultString = "";
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            return saveTrimAudio(strings[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ProgressUtil.getInstance();
        dialog = ProgressUtil.initProgressBar(context);
        dialog.show();
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

    private String saveTrimAudio(String audioPath) {
        final SoundFile.ProgressListener listener = fractionComplete -> true;
        try {
            mSoundFile = SoundFile.create(audioPath, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final int startFrame = 0;
        final int endFrame = 2600;
        String outPath = Utility.createFile(Constants.WN_AUDIO_FOLDER_PATH, Constants.WN_AUDIO_RECORD_FILE + System.currentTimeMillis(), Constants.WN_MP3);
        File outFile = new File(outPath);
        boolean fallbackToWAV = false;
        try {
            mSoundFile.WriteFile(outFile, startFrame, endFrame - startFrame);
        } catch (Exception e) {
            if (outFile.exists()) {
                outFile.delete();
            }
            e.printStackTrace();
            fallbackToWAV = true;
        }
        // Try to create a .wav file if creating a .mp3 file failed.
        if (fallbackToWAV) {
            outPath = Utility.createFile(Constants.WN_AUDIO_FOLDER_PATH, Constants.WN_AUDIO_RECORD_FILE + System.currentTimeMillis(), Constants.WN_WAV);
            outFile = new File(outPath);
            try {
                mSoundFile.WriteWAVFile(outFile, startFrame, endFrame - startFrame);
            } catch (Exception e) {
                if (outFile.exists()) {
                    outFile.delete();
                }
                e.printStackTrace();
            }
        }
        return afterSavingAudioTrim(outPath);
    }

    private String afterSavingAudioTrim(String outPath) {
        File outFile = new File(outPath);
        long fileSize = outFile.length();
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, outPath);
        values.put(MediaStore.MediaColumns.TITLE, R.string.text_who_s_next_app);
        values.put(MediaStore.MediaColumns.SIZE, fileSize);
        values.put(MediaStore.MediaColumns.MIME_TYPE, Constants.WN_AUDIO_MP3);
        values.put(MediaStore.Audio.Media.ARTIST, R.string.text_who_s_next_app);
        values.put(MediaStore.Audio.Media.DURATION, 60 * 1000);
        values.put(MediaStore.Audio.Media.IS_MUSIC, true);
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(outPath);
        Uri AudioUri = context.getContentResolver().insert(uri, values);
        assert AudioUri != null;
        return FileUtils.getPath(context, AudioUri);
    }

    public interface OnResult {
        void success(String trimAudioPath);

        void error();
    }
}
