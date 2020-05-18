package com.app.whosnextapp.messaging.videocall.utils;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.app.whosnextapp.messaging.quickblox.configs.CoreApp;


public class ResourceUtils {

    public static String getString(@StringRes int stringId) {
        return CoreApp.getInstance().getString(stringId);
    }

    public static Drawable getDrawable(@DrawableRes int drawableId) {
        return CoreApp.getInstance().getResources().getDrawable(drawableId);
    }

    public static int getColor(@ColorRes int colorId) {
        return CoreApp.getInstance().getResources().getColor(colorId);
    }

}
