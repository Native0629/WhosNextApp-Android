package com.joker.pager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;


/**
 * Indicator
 *
 * @author joker
 * @date 2018/1/18.
 */

final class IndicatorView extends AppCompatImageView {
    public IndicatorView(Context context) {
        super(context);
        init(context);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

    }

    public void setPagerOptions(PagerOptions pagerOptions) {
        setImageDrawable(pagerOptions.mIndicatorDrawable[0]);

        final int padding = pagerOptions.mIndicatorDistance >> 1;
        setPadding(padding, padding, padding, padding);

        if (pagerOptions.mIndicatorSize != -1) {
            final ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.width = layoutParams.height = pagerOptions.mIndicatorSize + pagerOptions.mIndicatorDistance;
            setLayoutParams(layoutParams);
        }
        setScaleType(ScaleType.FIT_XY);
    }
}
