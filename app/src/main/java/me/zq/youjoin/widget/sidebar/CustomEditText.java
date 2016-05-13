/*
 * Copyright (c) 2015 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.zq.youjoin.widget.sidebar;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义文本输入框，增加清空按钮
 */
public class CustomEditText extends AppCompatEditText {

    private Drawable mLeft, mTop, mRight, mBottom;

    private Rect mBounds;

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setDrawable();
        // 增加文本监听器.
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
    }

    // 输入框右边的图标显示控制
    private void setDrawable() {
        if (length() == 0 || !isFocused()) {
            setCompoundDrawables(mLeft, mTop, null, mBottom);
        } else {
            setCompoundDrawables(mLeft, mTop, mRight, mBottom);
        }
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (mLeft == null) {
            this.mLeft = left;
        }
        if (mTop == null) {
            this.mTop = top;
        }
        if (mRight == null) {
            this.mRight = right;
        }
        if (mBottom == null) {
            this.mBottom = bottom;
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }

    // 输入事件处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mRight != null && event.getAction() == MotionEvent.ACTION_UP) {
            this.mBounds = mRight.getBounds();
            mRight.getIntrinsicWidth();
            int eventX = (int) event.getX();
            int width = mBounds.width();
            int right = getRight();
            int left = getLeft();
            if (eventX > (right - width * 2 - left)) {
                setText("");
                event.setAction(MotionEvent.ACTION_CANCEL);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.mLeft = null;
        this.mTop = null;
        this.mRight = null;
        this.mBottom = null;
        this.mBounds = null;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        setDrawable();
    }
}