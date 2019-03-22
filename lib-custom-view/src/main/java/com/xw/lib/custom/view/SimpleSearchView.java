package com.xw.lib.custom.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xw.lib.custom.view.util.PxUtil;

/**
 * Created by Horris on 2016/12/15.
 */

public class SimpleSearchView extends LinearLayout {
    public SimpleSearchView(Context context) {
        super(context);
        inflateView();
    }

    public SimpleSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateView();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SimpleSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView();
    }

    RelativeLayout layBack;
    EditText search_content;
    ImageView clear_text;
    View view;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void inflateView() {
        view = View.inflate(getContext(), R.layout.view_simple_search, this);
        layBack = (RelativeLayout) view.findViewById(R.id.lay_back);
        search_content = (EditText) view.findViewById(R.id.search_content);
        clear_text = (ImageView) view.findViewById(R.id.clear_text);
        layBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                if(context instanceof Activity){
                    ((Activity) context).onBackPressed();
                }
            }
        });
        search_content.addTextChangedListener(textWatcher);
        search_content.setOnEditorActionListener(onEditorActionListener);
        clear_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                search_content.setText(null);
            }
        });
    }


    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {

        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId != EditorInfo.IME_ACTION_SEARCH) {
                return false;
            }
            final String queryString = v.getText().toString();
            if (queryString.length() <= 0) {
                Toast.makeText(getContext(), "请输入搜索关键词", Toast.LENGTH_SHORT).show();
                return true;
            }
//            handleSearch(queryString);
//            if (!TextUtils.isEmpty(queryString) && !"".equals(queryString.trim())) {
//                searchSuggestions.saveRecentQuery(queryString, null);
//            }
            return true;
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int before, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int after) {
            if (s.length() > 0) {
                clear_text.setVisibility(VISIBLE);
            } else {
                clear_text.setVisibility(GONE);

            }
            if (onTextChangedListener != null) {
                if(updateList) {
                    onTextChangedListener.onTextChanged(true, s);
                } else {
                    onTextChangedListener.onTextChanged(false, s);
                    updateList = true;
                }
            }
//            updateHistoryList(s.toString());
        }

        public void afterTextChanged(Editable s) {
        }
    };
    private boolean updateList;
    public void setEditTextStr(String str) {
        updateList = false;
        search_content.setText(str);
//        setTextDrawableLeft(getContext(), search_content, R.drawable.common_shape_half_white);
    }

    private void setTextDrawableLeft(Context context, EditText editText, int id) {

        Drawable drawable = context.getResources().getDrawable(id);
        if (drawable != null) {

            drawable.setBounds(0, 0, PxUtil.dip2px(context, 90f), PxUtil.dip2px(context, 28f));
        }
        editText.setCompoundDrawables(drawable, null, null , null);
        editText.setText("");
        editText.setHint("");
    }

    OnEditTextClickListener onEditTextClickListener;
    public void setEditTextOnClickListener(OnEditTextClickListener listener){
        this.onEditTextClickListener = listener;
        search_content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onEditTextClickListener != null) {
                    onEditTextClickListener.onClick(view, search_content.getText().toString());
                }
            }
        });
    }

    OnTextChangedListener onTextChangedListener;
    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
        this.onTextChangedListener = onTextChangedListener;
    }

    public interface OnTextChangedListener {
        void onTextChanged(boolean updateList, CharSequence s);
    }

    public interface OnEditTextClickListener {
        void onClick(View view, String s);
    }
}
