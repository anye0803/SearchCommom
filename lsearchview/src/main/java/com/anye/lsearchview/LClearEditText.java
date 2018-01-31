package com.anye.lsearchview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义带清除功能的EditText
 * Created by anye on 18-1-30.
 */

public class LClearEditText extends AppCompatEditText {

    /**
     * 是否显示左边的搜索icon
     */
    private boolean showLeftSearch;

    /**
     * 搜索Drawable
     */
    private Drawable mSearchDrawable;

    /**
     * 清除Drawable
     */
    private Drawable mClearDrawable;

    public LClearEditText(Context context) {
        super(context);
        initView();
    }

    public LClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initView();
    }

    public LClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LSearchClear);
        showLeftSearch = typedArray.getBoolean(R.styleable.LSearchClear_showLeftSearch, true);
        typedArray.recycle();
    }

    private void initView() {
        mSearchDrawable = getResources().getDrawable(R.mipmap.l_search_icon);
        mClearDrawable = getResources().getDrawable(R.mipmap.l_search_clear);
        setCompoundDrawablesWithIntrinsicBounds(showLeftSearch ? mSearchDrawable : null, null, null, null);
    }

    /**
     * 通过监听输入文字，来判断是否显示清除图标
     *
     * @param text
     * @param start
     * @param lengthBefore
     * @param lengthAfter
     */
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        showClearIcon(hasFocus() && text.length() > 0);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        showClearIcon(focused && length() > 0);
    }

    /**
     * 显示清除图标的操作
     */
    private void showClearIcon(boolean showClear) {
        setCompoundDrawablesWithIntrinsicBounds(showLeftSearch ? mSearchDrawable : null, null,
                showClear ? mClearDrawable : null, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                //计算清除图标的位置
                if (mClearDrawable != null && event.getX() >= getWidth() - getPaddingRight() * 2 - mClearDrawable.getBounds().width()
                        && event.getX() <= getWidth()) {
                    setText("");
                }
                break;
        }
        return super.onTouchEvent(event);
    }

}
