package com.xw.lib.custom.view.popup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xw.lib.custom.view.R;

/**
 * Created by ms on 2017/7/3.
 */

public class PopupWindowAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mDatas;
    private int state;

    /**
     * @param context
     * @param datas
     * @param state   如果要设置图标，可更改此状态，0是默认
     */
    public PopupWindowAdapter(Context context, String[] datas, int state) {
        this.mContext = context;
        this.mDatas = datas;
        this.state = state;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDatas.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mDatas[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.custom_pop_item, null);
            viewHolder.textItem = (TextView) convertView.findViewById(R.id.pop_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textItem.setText(mDatas[position]);
//        if (state != 0) {
//            //设置图标..
//        }
        return convertView;
    }

    class ViewHolder {
        TextView textItem;
    }
}
