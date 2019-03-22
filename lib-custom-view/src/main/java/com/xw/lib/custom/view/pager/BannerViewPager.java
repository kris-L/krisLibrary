package com.xw.lib.custom.view.pager;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xw.lib.custom.view.R;
import com.xw.lib.custom.view.util.PxUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by XWCHQ on 2017/8/10-14:07
 */

public class BannerViewPager extends FrameLayout {

    /**
     * 重新开始
     */
    public static final int REPEAT_MODE_RESTART = 0;
    /**
     * 一轮后回转方向
     */
    public static final int REPEAT_MODE_REVERSE = 1;
    /**
     * 单方向轮循,使用类似{@link RoundImagesPagerAdapter}的adapter
     */
    public static final int REPEAT_MODE_ROUND = 2;
    private static final int DEFAULT_INTERVAL = 3000;
    private boolean isRunning = false;
    private int repeatMode;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private boolean isFlipping = false;
    private int mFlipInterval = DEFAULT_INTERVAL;
    private DataSetObserver tabObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            initTabLayout();
            if(!isRunning) {
                updateFlipping();
            }
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    };
    private ViewPager.OnAdapterChangeListener onPagerAdapterChangeListener = new ViewPager.OnAdapterChangeListener() {
        @Override
        public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
            if (oldAdapter != null) {
                oldAdapter.unregisterDataSetObserver(tabObserver);
            }
            if (newAdapter != null) {
                newAdapter.registerDataSetObserver(tabObserver);
            }
            updateFlipping();
        }
    };
    private int offset = 1;
    private Runnable mFlipRunnable = null;

    private class FlipRunnable implements Runnable{
        @Override
        public void run() {
            if (isRunning) {
                showNext();
                postDelayed(mFlipRunnable, mFlipInterval);
            }
        }
    }

    private ViewPager.OnPageChangeListener roundPageChangelistener;

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BannerViewPager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context, attrs, defStyleAttr);
    }

    private void initViews(Context context, AttributeSet attrs, int defStyleAttr) {
        inflate(getContext(), R.layout.custom__banner_pager, this);
        viewPager = (ViewPager) findViewById(R.id.bannerPager);
        viewPager.addOnAdapterChangeListener(onPagerAdapterChangeListener);
        tabLayout = (TabLayout) findViewById(R.id.bannerTabs);
        tabLayout.setupWithViewPager(viewPager);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.BannerViewPager);
        int repeatMode = attributes.getInt(R.styleable.BannerViewPager_repeatMode, REPEAT_MODE_RESTART);
        //noinspection WrongConstant
        setRepeatMode(repeatMode);
        isFlipping = attributes.getBoolean(R.styleable.BannerViewPager_flipping, false);
        int mFlipInterval = attributes.getInt(R.styleable.BannerViewPager_flipInterval, DEFAULT_INTERVAL);
        setFlipInterval(mFlipInterval);
        attributes.recycle();
        updateFlipping();
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setBannerAdapter(PagerAdapter adapter) {
        viewPager.setAdapter(adapter);
    }

    private void initTabLayout() {
        if (viewPager.getAdapter() == null) {
            tabLayout.setVisibility(View.INVISIBLE);
            return;
        }
        if (viewPager.getAdapter().getCount() <= 1) {
            tabLayout.setVisibility(View.INVISIBLE);
        } else if(isTabVisible){
            tabLayout.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View tabView = getTabCustomView(tabLayout.getContext(), i);
                if (tabView != null) {
                    if (repeatMode == REPEAT_MODE_ROUND
                            && (i == 0 || i == viewPager.getAdapter().getCount() - 1)) {
                        tabView.setVisibility(INVISIBLE);
                    } else {
                        tabView.setVisibility(VISIBLE);
                    }
                    tab.setCustomView(tabView);
                }
            }
        }
        resetTabViews();
    }

    private boolean isTabVisible;
    public void setTabVisible(boolean isVisible){
        this.isTabVisible = isVisible;
        tabLayout.setVisibility(isVisible?View.VISIBLE:View.INVISIBLE);
    }

    /**
     * 重设间隔
     */
    private void resetTabViews() {
        post(new Runnable() {
            @Override
            public void run() {
                LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);
                int padding = PxUtil.dip2px(getContext(), 2);
                for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                    View tabView = mTabStrip.getChildAt(i);
                    tabView.setPadding(padding, padding, padding, padding);
                    tabView.setClickable(false);
                    tabView.invalidate();
                }

            }
        });
    }

    private View getTabCustomView(Context context, int position) {
        if (viewPager.getAdapter() != null && viewPager.getAdapter() instanceof TabAdapter) {
            return ((TabAdapter) viewPager.getAdapter()).getTabView(context, position);
        }
        return null;
    }

    public void startFlipping() {
        isFlipping = true;
        updateFlipping();
    }

    public void stopFlipping() {
        isFlipping = false;
        updateFlipping();
    }

    private synchronized void updateFlipping() {
        boolean flip = isFlipping && viewPager != null && getPageCount() > 1;
        if (flip != isRunning) {
            if (flip) {
                startFlip();
            } else {
                boolean isRemove = stopFlip();
            }
            isRunning = flip;
        }
    }

    private void startFlip() {
        if(mFlipRunnable == null){
            mFlipRunnable = new FlipRunnable();
            postDelayed(mFlipRunnable, mFlipInterval);
        }
    }

    private boolean stopFlip() {
        if(mFlipRunnable != null) {
            boolean isRemove = removeCallbacks(mFlipRunnable);
            if(isRemove) {
                isRunning = false;
                mFlipRunnable = null;
            }
            return isRemove;
        }else{
            return true;
        }
    }

    private void showNext() {
        int pageCount = getPageCount();
        if (pageCount < 1) {
            return;
        }
        if (viewPager.getCurrentItem() == pageCount - 1) {
            if (repeatMode == REPEAT_MODE_REVERSE) {
                offset = -offset;
            } else {
                offset = 1;
            }
        } else if (repeatMode == REPEAT_MODE_RESTART || repeatMode == REPEAT_MODE_ROUND) {
            offset = 1;
        }
        int nextIndex = (offset + viewPager.getCurrentItem() + pageCount) % pageCount;
//        viewPager.setCurrentItem(nextIndex);
        setCurrentItem(nextIndex,true);
    }

    /**
     *  使用手动滑时的翻页速度(默认的setCurrentItem(int position) 的速度200ms太快
     *  )
     */
    public void setCurrentItem(int nextIndex,boolean isSmooth) {
        Class<?>[] paramTypes = new Class[4];
        paramTypes[0] = Integer.TYPE;
        paramTypes[1] = Boolean.TYPE;
        paramTypes[2] = Boolean.TYPE;
        paramTypes[3] = Integer.TYPE;
        try {
            Method method = ViewPager.class.getDeclaredMethod("setCurrentItemInternal", paramTypes);
            if(method != null){
                Log.d("Reflect","Success");
                method.setAccessible(true);
                method.invoke(viewPager,nextIndex,isSmooth,true,1);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setRepeatMode(@RepeatMode int repeatMode) {
        this.repeatMode = repeatMode;
        if(repeatMode == REPEAT_MODE_ROUND){
             viewPager.addOnPageChangeListener(roundPageChangeListener);
        }else{
            viewPager.removeOnPageChangeListener(roundPageChangeListener);
        }
    }

    private ViewPager.OnPageChangeListener roundPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(state == ViewPager.SCROLL_STATE_IDLE && getPageCount() > 1) {
                int position = viewPager.getCurrentItem();
                if (position == 0) {
                    setCurrentItem(getPageCount() - 2, false);
                } else if (position ==getPageCount() - 1) {
                    setCurrentItem(1, false);
                }
            }
        }
    };

    public int getPageCount() {
        if (viewPager.getAdapter() != null) {
            return viewPager.getAdapter().getCount();
        } else {
            return 0;
        }
    }

    public void setFlipInterval(int interval) {
        this.mFlipInterval = interval;
    }

    public boolean isTabVisible() {
        return isTabVisible;
    }

    public static interface TabAdapter {
        public abstract View getTabView(Context context, int position);
    }

    @IntDef({REPEAT_MODE_RESTART, REPEAT_MODE_REVERSE,REPEAT_MODE_ROUND})
    @interface RepeatMode {

    }
}
