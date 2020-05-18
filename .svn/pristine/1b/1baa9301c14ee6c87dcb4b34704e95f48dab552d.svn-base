package com.app.whosnextapp.settings;

import android.view.View;


public class ScaleTransformer implements GalleryLayoutManager.ItemTransformer {

    private boolean isEnable;

    public ScaleTransformer(boolean isEnable) {
        this.isEnable = isEnable;
    }

    @Override
    public void transformItem(GalleryLayoutManager layoutManager, View item, float fraction) {
        item.setPivotX(item.getWidth() / 2.0f);
        item.setPivotY(item.getHeight() / 2.0f);
        float scale = 1 - 0.3f * Math.abs(fraction);
        item.setScaleX(scale);
        item.setScaleY(scale);
        item.setAlpha(fraction == 0 ? (isEnable ? 1.0f : 0.3f) : (isEnable ? 0.5f : 0.2f));
    }
}
