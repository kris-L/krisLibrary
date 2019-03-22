package com.xw.lib.custom.view.text;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by XWCHQ on 2017/6/16-14:03
 */

public class InputFilterExt {
    public static class LengthFilter implements InputFilter {
        private final int mMax;
        private OnOverFilterListener overFilterListener;
        public LengthFilter(int max) {
            mMax = max;
        }

        public LengthFilter(int mMax, OnOverFilterListener overFilterListener) {
            this.mMax = mMax;
            this.overFilterListener = overFilterListener;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart, int dend) {
            int keep = mMax - (dest.length() - (dend - dstart));//剩余可输入长度
            if (keep <= 0) {//已满
                onOver(dest,source);
                return "";
            } else if (keep >= end - start) {//未满，且一次性输入后仍未超出
                return null; // keep original
            } else {//未满，但一次性输入多个字符时超出，截取部分
                keep += start;
                if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                    --keep;
                    if (keep == start) {
                        return "";
                    }
                }
                CharSequence keepSource = source.subSequence(start, keep);
                CharSequence abandon = source.subSequence(keep,source.length());
                onOver(dest.toString()+keepSource+toString(),abandon);
                return keepSource;
            }
        }

        private void onOver(CharSequence keep, CharSequence abandon) {
            if(overFilterListener != null){
                overFilterListener.onOver(keep,abandon);
            }
        }

        /**
         * @return the maximum length enforced by this input filter
         */
        public int getMax() {
            return mMax;
        }
    }

    public static class RegexInputFilter implements InputFilter{

        public final String regex;
        private OnOverFilterListener overFilterListener;

        public RegexInputFilter(String regex) {
            this.regex = regex;
        }

        public RegexInputFilter(String regex, OnOverFilterListener overFilterListener) {
            this.regex = regex;
            this.overFilterListener = overFilterListener;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String sourceStr = ""+source;
            if(sourceStr.matches(regex)){
                return null;
            }else {
                onOver(dest,source);
                return "";
            }
        }

        private void onOver(CharSequence keep, CharSequence abandon) {
            if(overFilterListener != null){
                overFilterListener.onOver(keep,abandon);
            }
        }
    }

    public static interface OnOverFilterListener{
        void onOver(CharSequence keep,CharSequence over);
    }

    public static class NumberRangeFilter implements InputFilter {
        private final Number mMax;
        private final Number mMin;
        private OnOverFilterListener overFilterListener;
        public NumberRangeFilter(Number min,Number max) {
            this(min,max,null);
        }

        public NumberRangeFilter(Number min,Number max, OnOverFilterListener overFilterListener) {
            this.mMin = min;
            this.mMax = max;
            if(mMax.doubleValue() < mMin.doubleValue()){
                throw new IllegalArgumentException("最大值不能小于最小值");
            }
            this.overFilterListener = overFilterListener;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart, int dend) {
            int maxLen;
            if((mMax instanceof Integer || mMax instanceof Long)&&(mMin instanceof Integer || mMin instanceof Long)) {
                maxLen = Math.max(String.valueOf(mMax).length(), String.valueOf(mMin).length());
            }else if(mMax instanceof Float || mMin instanceof Float){
                maxLen = String.valueOf(Float.MIN_VALUE).length();
            }else{
                maxLen = String.valueOf(Double.MIN_VALUE).length();
            }
            int keep = maxLen - (dest.length() - (dend - dstart));//剩余可输入长度
            if (keep <= 0) {//已满
                onOver(dest,source);
                return "";
            } else if (keep >= end - start) {//未满，且一次性输入后刚好
                return isInRange(getPreDestText(source,start,end,dest,dstart,dend))?null:""; // keep original
            }else {//未满，但一次性输入多个字符时超出，截取部分
                keep += start;
                if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                    --keep;
                    if (keep == start) {
                        return "";
                    }
                }
                CharSequence keepSource = source.subSequence(start, keep);
                if(isInRange(getPreDestText(source,start,keep,dest,dstart,dend))){
                    CharSequence abandon = source.subSequence(keep,source.length());
                    onOver(dest.toString()+keepSource+toString(),abandon);
                    return keepSource;
                }else if(keep - start > 1){
                    CharSequence abandon = source.subSequence(keep - 1,source.length());
                    onOver(dest.toString()+keepSource+toString(),abandon);
                    return keepSource.subSequence(start,keep - 1);
                }else{
                    onOver(dest,source);
                    return "";
                }
            }
        }

        private String getPreDestText(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            StringBuffer sb = new StringBuffer();
            sb.append(dest.subSequence(0,dstart));
            if(end <= source.length()) {
                sb.append(source.subSequence(start, end));
            }
            if(dend < dest.length()) {
                sb.append(dest.subSequence(dend, dest.length()));
            }
            return sb.toString();
        }

        /**
         * 判断文本是否在范围内
         */
        private boolean isInRange(String after) {
            boolean result = false;
            if(mMin.doubleValue() < 0 && "-".equals(after)){//可以为负数
                result = true;
            }else{
                try {
                    double afterDouble = Double.parseDouble(after);
                    if (afterDouble <= mMax.doubleValue() && afterDouble >= mMin.doubleValue()) {
                        result = true;
                    }else if(afterDouble <= mMax.doubleValue() && mMin.doubleValue() >= 0){
                        result = true;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return result;
        }

        private void onOver(CharSequence keep, CharSequence abandon) {
            if(overFilterListener != null){
                overFilterListener.onOver(keep,abandon);
            }
        }

        /**
         * @return the maximum length enforced by this input filter
         */
        public Number getMax() {
            return mMax;
        }

        public Number getMin() {
            return mMax;
        }
    }
}
