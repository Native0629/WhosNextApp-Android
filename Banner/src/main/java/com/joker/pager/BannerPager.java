package com.joker.pager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.joker.pager.adapter.BannerViewPagerAdapter;
import com.joker.pager.holder.ViewHolderCreator;
import com.joker.pager.listener.OnItemClickListener;

import java.lang.reflect.Field;
import java.util.List;

public class BannerPager<T> extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private static final String TAG = BannerPager.class.getSimpleName();

    private BannerViewPager mViewPager;
    private BannerViewPagerAdapter<T> mAdapter;
    private LinearLayout mIndicatorLayout;
    private ImageView mCurrentIndicator;
    private PagerOptions mPagerOptions;
    private boolean isStartLoop;
    private boolean isDetached2StopLoop;
    private ViewPagerScroller mScroller;
    private ViewPager.OnPageChangeListener mPageChangeListener;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int currentItem = mViewPager.getCurrentItem();
            if (currentItem < mAdapter.getCount() - 1) {
                currentItem++;
            } else {
                currentItem = 0;
            }
            mViewPager.setCurrentItem(currentItem);
            sendHandlerEmptyMessage();
            trace("切换轮播图");
            return true;
        }
    });
    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            performClick();
            return mViewPager.onTouchEvent(event);
        }
    };

    public BannerPager(Context context) {
        this(context, null);
    }

    public BannerPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.widget_banner_pager, this);
        mViewPager = findViewById(R.id.widget_view_pager);
        mIndicatorLayout = findViewById(R.id.widget_indicator_container);
        mPagerOptions = new PagerOptions.Builder(context).build();
        initViewPagerScroll();
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(this);
        setOnTouchListener(mOnTouchListener);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isDetached2StopLoop && !isStartLoop) {
            startTurning();
            isDetached2StopLoop = false;
        }
        trace("onAttachedToWindow");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!isDetached2StopLoop && isStartLoop) {
            stopTurning();
            isDetached2StopLoop = true;
        }
        trace("onDetachedFromWindow");
    }

    /**
     * 设置ViewPager的滑动速度
     */
    private void initViewPagerScroll() {
        try {
            final Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            mScroller = new ViewPagerScroller(mViewPager.getContext());
            scroller.set(mViewPager, mScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        // 切换indicator
        final int realPosition = position % mIndicatorLayout.getChildCount();
        setIndicatorSelected(realPosition);
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageSelected(realPosition);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        handleEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void handleEvent(MotionEvent ev) {
        //没有使能loop 或者 没有调用start
        if (!mPagerOptions.mLoopEnable || !isStartLoop) {
            return;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_OUTSIDE:
                trace("dispatchTouchEvent: " + ev.getX());
                removeHandlerMessages();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                trace("dispatchTouchEvent: " + ev.getX());
                removeHandlerMessages();
                sendHandlerEmptyMessage();
                break;
            default:
                trace("未知事件: " + ev.getAction());
                break;
        }
    }

    private void sendHandlerEmptyMessage() {
        if (mHandler != null && mPagerOptions.mLoopEnable) {
            mHandler.sendEmptyMessageDelayed(0, mPagerOptions.mDelayedTime);
        }
    }

    private void removeHandlerMessages() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    private void setIndicatorSelected(int currentItem) {
        if (mCurrentIndicator != null) {
            mCurrentIndicator.setImageDrawable(mPagerOptions.mIndicatorDrawable[0]);
            mCurrentIndicator.setSelected(false);
        }
        final ImageView indicator = (ImageView) mIndicatorLayout.getChildAt(currentItem);
        indicator.setSelected(true);
        indicator.setImageDrawable(mPagerOptions.mIndicatorDrawable[1]);
        mCurrentIndicator = indicator;
    }

    public BannerPager<T> setPagerOptions(PagerOptions options) {
        mPagerOptions = options;
        return this;
    }

    public void setPages(@NonNull List<T> data, @NonNull ViewHolderCreator creator) {
        mAdapter = new BannerViewPagerAdapter<>(data, creator);
        mViewPager.setAdapter(mAdapter);
        mAdapter.setLoopEnable(mPagerOptions.mLoopEnable);
        mAdapter.setViewPager(mViewPager);
        initIndicator();
        handlePagerOptions();
    }

    private void initIndicator() {
        final int count = mAdapter.getRealCount();
        mIndicatorLayout.removeAllViews();
        IndicatorView indicator;
        for (int i = 0; i < count; i++) {
            indicator = new IndicatorView(getContext());
            mIndicatorLayout.addView(indicator);
            indicator.setPagerOptions(mPagerOptions);
        }
        setIndicatorSelected(mViewPager.getCurrentItem());
        trace("indicator count : " + mIndicatorLayout.getChildCount());
    }

    private void handlePagerOptions() {
        mViewPager.setPageMargin(mPagerOptions.mPagePadding);
        final ViewGroup.MarginLayoutParams mp = (MarginLayoutParams) mViewPager.getLayoutParams();
        mp.leftMargin = mp.rightMargin = mPagerOptions.mPrePagerWidth;
        mViewPager.setLayoutParams(mp);
        LayoutParams lp = (LayoutParams) mIndicatorLayout.getLayoutParams();
        lp.addRule(mPagerOptions.mIndicatorAlign);
        if (mPagerOptions.mIndicatorMarginBottom != -1) {
            lp.bottomMargin = mPagerOptions.mIndicatorMarginBottom;
        }
        mIndicatorLayout.setLayoutParams(lp);
        mViewPager.setPageTransformer(true, mPagerOptions.mPageTransformer);
        mScroller.setScrollDuration(mPagerOptions.mScrollDuration);
        mIndicatorLayout.setVisibility(mPagerOptions.mIndicatorVisibility);
    }

    public void clearPages() {
        mAdapter.clearData();
        initIndicator();
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    private void setCurrentItem(int position) {
        if (mPagerOptions.mLoopEnable) {
            final int realCount = mAdapter.getRealCount();
            final int multiple = mViewPager.getCurrentItem() / realCount;
            position = position + multiple * realCount;
        }
        mViewPager.setCurrentItem(position, false);
        trace("new position: " + position);
    }

    public boolean isStartLoop() {
        return isStartLoop;
    }

    public void startTurning() {
        if (mAdapter == null) {
            return;
        }
        if (isStartLoop) {
            stopTurning();
        }
        mPagerOptions.mLoopEnable = true;
        mAdapter.setLoopEnable(true);
        mViewPager.addOnPageChangeListener(this);
        sendHandlerEmptyMessage();
        isStartLoop = true;
        isDetached2StopLoop = false;
        trace("startTurning ");
    }

    public void stopTurning() {
        removeHandlerMessages();
        mViewPager.removeOnPageChangeListener(this);
        isStartLoop = false;
        isDetached2StopLoop = true;
        trace("stopTurning ");
    }

    private void trace(String msg) {
        if (mPagerOptions.mDebug) {
            Log.d(TAG, "===> " + msg);
        }
    }


    public int getScrollDuration() {
        return mScroller.getScrollDuration();
    }


    public BannerPager<T> setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mPageChangeListener = listener;
        return this;
    }

    /**
     * 设置 Item点击监听
     *
     * @param listener OnItemClickListener
     * @return BannerPager
     */
    public BannerPager<T> setOnItemClickListener(OnItemClickListener listener) {
        mViewPager.setOnItemClickListener(listener);
        return this;
    }
}
