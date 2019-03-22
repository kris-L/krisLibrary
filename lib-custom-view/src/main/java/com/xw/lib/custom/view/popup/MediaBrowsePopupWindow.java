package com.xw.lib.custom.view.popup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xw.lib.custom.view.R;

import java.io.File;
import java.util.List;


public class MediaBrowsePopupWindow extends PopupWindow {
    private ViewPager mViewPager;
    private CirclePageIndicatorForViewPager circlePageIndicator;
    private TextView tv_page_num;
    private Context context;

    public MediaBrowsePopupWindow(Context context) {
        this.context = context;
        initViews();
    }

    private void initViews() {
        View view = View.inflate(context, R.layout.custom_popup_window_media_browse, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable());
        setOutsideTouchable(true);
        mViewPager = (ViewPager) view.findViewById(R.id.media_view_pager);
        circlePageIndicator = (CirclePageIndicatorForViewPager) view.findViewById(R.id.flipper_Indicator);
        mViewPager.setOffscreenPageLimit(2);

        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_page_num = (TextView) view.findViewById(R.id.tv_page_num);
    }

    public void show(View view, List<? extends Object> vehicleMediaList, int position) {
        show(view, vehicleMediaList, position, true, false);
    }

    public void show(View view, List<? extends Object> vehicleMediaList, int position, boolean showIndicator, boolean showPageNum) {
        PagerAdapter adapter = new ImageBrowseAdapter(context, vehicleMediaList);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(position);
        showAtLocation(view, Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
        if (showIndicator && adapter.getCount() > 1) {
            circlePageIndicator.setViewPager(mViewPager);
        } else {
            circlePageIndicator.close();
        }

        final int size = vehicleMediaList.size();
        if (showPageNum) {
            tv_page_num.setVisibility(View.VISIBLE);
            tv_page_num.setText((position+1) + "/" + size);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    tv_page_num.setText((position+1) + "/" + size);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        } else {
            tv_page_num.setVisibility(View.GONE);
        }
    }

    protected void loadIntoImageView(Object vehicleMedia, ImageView imageView) {
        try {
            if (vehicleMedia instanceof Integer) {
                imageView.setImageResource((Integer) vehicleMedia);
            } else if (vehicleMedia instanceof String) {
                String path = (String) vehicleMedia;
                if (path.startsWith(File.separator)) {
                    imageView.setImageBitmap(BitmapFactory.decodeFile((String) vehicleMedia));
                }
            } else if (vehicleMedia instanceof Bitmap) {
                imageView.setImageBitmap((Bitmap) vehicleMedia);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Context getContext() {
        return context;
    }

    public class ImageBrowseAdapter extends PagerAdapter {
        private Context context;
        private List<? extends Object> vehicleMediaList;

        public ImageBrowseAdapter(Context context, List<? extends Object> vehicleMediaList) {
            this.context = context;
            this.vehicleMediaList = vehicleMediaList;
        }

        @Override
        public int getCount() {
            return vehicleMediaList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup, int position) {
            final Object vehicleMedia = vehicleMediaList.get(position);
            View view = View.inflate(context, R.layout.custom_view_media_view_page, null);
            loadIntoImageView(vehicleMedia, (ImageView) view.findViewById(R.id.media_iv));
            viewGroup.addView(view);
            return view;
        }
    }
}
