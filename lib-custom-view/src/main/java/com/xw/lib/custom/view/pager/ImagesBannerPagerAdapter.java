package com.xw.lib.custom.view.pager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.xw.lib.custom.view.R;
import com.xw.lib.custom.view.util.PxUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XWCHQ on 2017/8/10-15:10
 */

public class ImagesBannerPagerAdapter extends PagerAdapter implements BannerViewPager.TabAdapter{

    private Context context;
    private List<Object> imagePaths = new ArrayList<>();
    private LoadImageStrategy loadImageStrategy;

    public ImagesBannerPagerAdapter(Context context, List<? extends Object> imagePaths,LoadImageStrategy strategy) {
        this.context = context;
        if(imagePaths != null) {
            this.imagePaths.addAll(imagePaths);
        }
        this.loadImageStrategy = strategy;
    }

    public ImagesBannerPagerAdapter(Context context,LoadImageStrategy strategy) {
        this(context,null, strategy);
    }
    public ImagesBannerPagerAdapter(Context context,List<? extends Object> imagePaths) {
        this(context,imagePaths, null);
    }

    public ImagesBannerPagerAdapter(Context context) {
        this(context,null, null);
    }

    public void setImagePaths(List<? extends Object> paths){
        imagePaths.clear();
        imagePaths.addAll(paths);
        notifyDataSetChanged();
    }

    public void addImage(Object path){
        imagePaths.add(path);
        notifyDataSetChanged();
    }

    public void replace(int index,Object path){
        imagePaths.set(index,path);
        notifyDataSetChanged();
    }

    public void clear(){
        imagePaths.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imagePaths.size();
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
    public Object instantiateItem(ViewGroup viewGroup, final int position) {
        final Object vehicleMedia = getItem(position);
        View view = View.inflate(context, R.layout.custom_view_media_view_page, null);
        loadIntoImageView(vehicleMedia, (ImageView) view.findViewById(R.id.media_iv));
        viewGroup.addView(view);
        if (mOnItemClickListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position);
                }
            });
        }
        return view;
    }

    public Object getItem(int position) {
        if(position >= 0 && position < imagePaths.size()) {
            return imagePaths.get(position);
        }else{
            return null;
        }
    }

    private void loadIntoImageView(Object image, ImageView imageView) {
        LoadImageStrategy strategy = loadImageStrategy == null?new SimpleLoadImageStrategy():loadImageStrategy;
        if(image instanceof Integer){
            strategy.loadFromResource(imageView, (Integer) image);
        }else if(image instanceof String){
            if(image.toString().matches("\\d+")){
                strategy.loadFromResource(imageView, Integer.parseInt(image.toString()));
            }else if(((String) image).startsWith("http://") || ((String) image).startsWith("https://")){
                strategy.loadFromWebUrl(imageView, (String) image);
            }else {
                strategy.loadFromLocalPath(imageView, (String) image);
            }
        }else if(image instanceof Bitmap){
            strategy.loadFromBitmap(imageView, (Bitmap) image);
        }else if(image instanceof Drawable){
            strategy.loadFromDrawable(imageView, (Drawable) image);
        }else if(image instanceof Uri){
            strategy.loadFromURI(imageView, (Uri) image);
        }else{
            strategy.loadFromOther(imageView,image);
        }
    }

    @Override
    public View getTabView(Context context, int position) {
        ImageView imageView = new ImageView(context);
        Drawable drawable = context.getResources().getDrawable(R.drawable.custom_tab_point);
        imageView.setImageDrawable(drawable);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(PxUtil.dip2px(context,5f),PxUtil.dip2px(context,5f)));
        return imageView;
    }

    public LoadImageStrategy getLoadImageStrategy() {
        return loadImageStrategy;
    }

    public ImagesBannerPagerAdapter setLoadImageStrategy(LoadImageStrategy loadImageStrategy) {
        this.loadImageStrategy = loadImageStrategy;
        return this;
    }

    public static interface LoadImageStrategy{
        void loadFromResource(ImageView imageView, int resId);
        void loadFromLocalPath(ImageView imageView, String path);
        void loadFromWebUrl(ImageView imageView, String url);
        void loadFromBitmap(ImageView imageView, Bitmap bmp);
        void loadFromDrawable(ImageView imageView, Drawable drawable);
        void loadFromURI(ImageView imageView, Uri uri);
        void loadFromOther(ImageView imageView, Object obj);
    }

    public static class SimpleLoadImageStrategy implements LoadImageStrategy{

        @Override
        public void loadFromResource(ImageView imageView, int resId) {
            imageView.setImageResource(resId);
        }

        @Override
        public void loadFromLocalPath(ImageView imageView, String path) {
            imageView.setImageURI(Uri.fromFile(new File(path)));
        }

        @Override
        public void loadFromWebUrl(ImageView imageView, String url) {

        }

        @Override
        public void loadFromBitmap(ImageView imageView, Bitmap bmp) {
            imageView.setImageBitmap(bmp);
        }

        @Override
        public void loadFromDrawable(ImageView imageView, Drawable drawable) {
            imageView.setImageDrawable(drawable);
        }

        @Override
        public void loadFromURI(ImageView imageView, Uri uri) {
            imageView.setImageURI(uri);
        }

        @Override
        public void loadFromOther(ImageView imageView, Object obj) {

        }
    }

    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
