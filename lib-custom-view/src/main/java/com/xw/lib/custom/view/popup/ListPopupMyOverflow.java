package com.xw.lib.custom.view.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.xw.lib.custom.view.datePicker.utils.ConvertUtils;

/**
 * 只适合列表形式的PopupWindow
 * Created by ms on 2017/7/3.
 */

public class ListPopupMyOverflow extends PopupWindow {
    private Context mContext;
    private ListView listView;
    private String[] itemArray;
    private View rootView;// 自身的布局
    private int xOffset, yOffset;
    private ItemClickCallBack mCallBack;
    private PopupWindowAdapter mAdapter;

    /**
     * @param context
     * @param layoutId          listView布局Id
     * @param viewIds           listView控件id
     * @param array             数据源
     * @param state             控制布局 用于扩展
     * @param itemClickCallBack item点击回调
     */
    public ListPopupMyOverflow(Context context, int layoutId, int viewIds, String[] array, int state, ItemClickCallBack itemClickCallBack) {
        this.mContext = context;
        this.mCallBack = itemClickCallBack;
        this.itemArray = array;
        initPopUpMyOverflow(layoutId, viewIds, state);
    }

    private void initPopUpMyOverflow(int layoutId, int viewIds, int state) {
        // 获取状态栏高度
        Rect frame = new Rect();
        ((Activity) mContext).getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(frame);
        // 获取屏幕信息
        DisplayMetrics dm = new DisplayMetrics();// 获取屏幕的信息
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        /////初始化listView
        rootView = LayoutInflater.from(mContext).inflate(layoutId, null);
        listView = (ListView) rootView.findViewById(viewIds);
        mAdapter = new PopupWindowAdapter(mContext, itemArray, 0);
        listView.setAdapter(mAdapter);
        //////
        setContentView(rootView);
        setWidth((int) (dm.widthPixels * 0.3));// 设置宽度
        setHeight(RadioGroup.LayoutParams.WRAP_CONTENT);// 设置高度
        setFocusable(true);// 聚焦
        setOutsideTouchable(true); // 点击外部关闭。
        setAnimationStyle(android.R.style.Animation_Dialog);// 设置一个动画。
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if (mCallBack != null) {
                    mCallBack.callBack(position);
                    dismiss();
                }
            }
        });

        // 让pop可以点击外面消失掉
        setBackgroundDrawable(new ColorDrawable(0));
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                dismiss();
            }
        });

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                if (arg1.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }

        });
    }

    public void showPop(View view) {
        showAsDropDown(view, ConvertUtils.toPx(mContext, -60f), ConvertUtils.toPx(mContext, 5f));
        backgroundAlpha(0.5f);
    }

    public void changeData() {
        mAdapter.notifyDataSetChanged();
    }

    public interface ItemClickCallBack {
        void callBack(int position);
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
    }
}
