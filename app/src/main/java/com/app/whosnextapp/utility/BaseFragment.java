package com.app.whosnextapp.utility;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.whosnextapp.BuildConfig;

public class BaseFragment extends Fragment {
    private static boolean D = BuildConfig.DEBUG;
    protected String TAG = "Toro:BaseActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = "Toro:" + getClass().getSimpleName();
        if (D)
            Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
    }


    @Override
    public void onStart() {
        super.onStart();
        if (D) Log.d(TAG, "onStart() called");
    }


    @Override
    public void onResume() {
        super.onResume();
        if (D) Log.d(TAG, "onResume() called");
    }


    @Override
    public void onPause() {
        super.onPause();
        if (D) Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (D) Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (D) Log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (D) Log.d(TAG, "onSaveInstanceState() called with: outState = [" + outState + "]");
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        if (D) {
            Log.d(TAG, "onMultiWindowModeChanged() called with: isInMultiWindowMode = ["
                    + isInMultiWindowMode
                    + "]");
        }
    }


    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        if (D) {
            Log.d(TAG, "onPictureInPictureModeChanged() called with: isInPictureInPictureMode = ["
                    + isInPictureInPictureMode
                    + "]");
        }
    }

}
