/*
 * Copyright 2011 woozzu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.zq.youjoin.widget.sidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;

import me.zq.youjoin.utils.GlobalUtils;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/11/29.
 */
public class IndexableListView extends ListView {

    private boolean mIsFastScrollEnabled = true;
    private IndexScroller mScroller = null;

    public IndexableListView(Context context) {
        super(context);
    }

    public IndexableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFastScrollEnabled() {
        return mIsFastScrollEnabled;
    }

    @Override
    public void setFastScrollEnabled(boolean enabled) {
        mIsFastScrollEnabled = enabled;
        if (mIsFastScrollEnabled) {
            if (mScroller == null) {
                mScroller = new IndexScroller(getContext(), this);
                mScroller.show();
            }

        } else {
            if (mScroller != null) {
                mScroller.hide();
                mScroller = null;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Overlay index bar
        if (mScroller != null)
            mScroller.draw(canvas);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mScroller != null && mScroller.onTouchEvent(ev))
            return true;

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if (mScroller != null)
            mScroller.setAdapter(adapter);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        final int minHeight = GlobalUtils.dpToPx(400); // listview 只有400dp 估计键盘弹出来了

        if (h < minHeight) {
            mScroller.hide();
        } else {
            mScroller.show();
        }

        if (mScroller != null)
            mScroller.onSizeChanged(w, h, oldw, oldh);
    }

}

