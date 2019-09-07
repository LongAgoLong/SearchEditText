package com.leo.searchedittextlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by LEO
 * On 2019/9/7
 * Description:优化了字符串变化回调的EditText，为优化搜索功能打造
 */
public class SearchEditText extends EditText {
    private long mLimitMillis = 1000;
    private String mLastResult;
    private boolean mIsCompareResult;
    private OnSearhTextChangeCallback onSearhTextChangeCallback;

    public SearchEditText(Context context) {
        super(context);
        init(context, null);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchEditText);
            mLimitMillis = a.getInt(R.styleable.SearchEditText_searchET_intervalsMills, 1000);
            mIsCompareResult = a.getBoolean(R.styleable.SearchEditText_searchET_isCompareResult, false);
            a.recycle();
        } else {
            mLimitMillis = 1000;
            mIsCompareResult = false;
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        removeCallbacks(runnable);
        postDelayed(runnable, mLimitMillis);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(runnable);
    }

    public void setLimitMillis(long limitMillis) {
        this.mLimitMillis = limitMillis;
        removeCallbacks(runnable);
    }

    public void setOnSearhTextChangeCallback(OnSearhTextChangeCallback onSearhTextChangeCallback) {
        this.onSearhTextChangeCallback = onSearhTextChangeCallback;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (null == onSearhTextChangeCallback) {
                return;
            }
            String currentStr = getText().toString();
            if (mIsCompareResult && TextUtils.equals(mLastResult, currentStr)) {
                return;
            }
            mLastResult = currentStr;
            onSearhTextChangeCallback.onTextChange(getText().toString());
        }
    };
}
