package me.zq.youjoin.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/11/29.
 */
public class GlobalUtils {
    public static int dpToPx(int dpValue) {
        return (int) (dpValue * YouJoinApplication.sScale + 0.5f);
    }

    public static void popSoftkeyboard(Context ctx, View view, boolean wantPop) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (wantPop) {
            view.requestFocus();
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } else {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void zoomDrwable(Drawable drawable/*, boolean isMonkey*/) {
        //int width = isMonkey ? MyApp.sEmojiMonkey : MyApp.sEmojiNormal;
        int width = YouJoinApplication.getAppContext().getResources()
                .getDimensionPixelSize(R.dimen.emoji_normal);
        drawable.setBounds(0, 0, width, width);
    }
}
