package com.app.whosnextapp.apis;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ProgressBar;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class ProgressUtil {

    private static ProgressUtil mInstance = null;

    public static ProgressUtil getInstance() {
        if (mInstance == null) {
            mInstance = new ProgressUtil();
        }
        return mInstance;
    }

    public static ACProgressFlower initProgressBar(Context context) {
        final ACProgressFlower dialog = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .petalThickness(5)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        dialog.setCancelable(false);
        return dialog;
    }

    void showDialog(ACProgressFlower dialog, ProgressBar pb, boolean isLoaderRequired) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        } else if (pb != null && isLoaderRequired) {
            pb.setVisibility(View.VISIBLE);
        }
    }

    void hideDialog(ACProgressFlower dialog, ProgressBar pb) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        } else if (pb != null) {
            pb.setVisibility(View.GONE);
        }
    }
}
