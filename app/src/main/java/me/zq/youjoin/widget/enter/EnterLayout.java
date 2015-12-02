package me.zq.youjoin.widget.enter;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Build;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.reflect.Method;

import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.utils.GlobalUtils;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/1.
 *
 * Created by chaochen on 14-10-28.
 * @notice common_enter_emoji必须以EnterLayoutAnimSupportContainer作父容器,且该容器只有两个子布局
 */
public abstract class EnterLayout {
    public TextView sendText;
    public ImageButton send;
    public EditText content;
    private Activity mActivity;
    protected ViewGroup commonEnterRoot;
    protected Type mType = Type.Default;
    protected int inputBoxHeight = 0;
    protected int screenHeight;
    protected int panelHeight;
    protected EnterLayoutAnimSupportContainer mEnterLayoutAnimSupportContainer;
    protected boolean mEnterLayoutStatus;
    private TextWatcher restoreWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            Object tag = content.getTag();
            if (tag == null) {
                return;
            }

            //CommentBackup.getInstance().save(CommentBackup.BackupParam.create(tag), s.toString());
        }
    };

    public EnterLayoutAnimSupportContainer getEnterLayoutAnimSupportContainer(){
        return mEnterLayoutAnimSupportContainer;
    }
    public EnterLayout(Activity activity, View.OnClickListener sendTextOnClick, Type type) {
        mType = type;

        mActivity = activity;

        panelHeight =  GlobalUtils.dpToPx(200);
        inputBoxHeight = GlobalUtils.dpToPx(48);
        screenHeight = YouJoinApplication.sHeightPix;

        commonEnterRoot = (ViewGroup)mActivity.findViewById(R.id.commonEnterRoot);
        if(commonEnterRoot!=null && commonEnterRoot.getParent() instanceof EnterLayoutAnimSupportContainer){
            mEnterLayoutAnimSupportContainer = (EnterLayoutAnimSupportContainer) commonEnterRoot.getParent();
            if(activity instanceof EnterLayoutAnimSupportContainer.OnEnterLayoutBottomMarginChanagedCallBack){
                mEnterLayoutAnimSupportContainer.setOnEnterLayoutBottomMarginChanagedCallBack((EnterLayoutAnimSupportContainer.OnEnterLayoutBottomMarginChanagedCallBack) activity);
            }
        }


        sendText = (TextView) activity.findViewById(R.id.sendText);
        sendText.setOnClickListener(sendTextOnClick);


        if (mType == Type.TextOnly) {
            sendText.setVisibility(View.VISIBLE);
        }

        send = (ImageButton) activity.findViewById(R.id.send);
        if (mType == Type.Default) {
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActivity instanceof CameraAndPhoto) {
                        CameraAndPhoto cameraAndPhoto = (CameraAndPhoto) mActivity;
                        cameraAndPhoto.photo();
                    }
                }
            });
        } else {
            send.setVisibility(View.GONE);
        }

        content = (EditText) activity.findViewById(R.id.comment);
        //拦截输入法 通过点击事件触发输入法
//        if (mType != Type.TextOnly) {
//            interceptInputMethod(content);
//        }

        content.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                updateSendButtonStyle();
            }
        });
        content.setText("");

        content.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1 || count == 2) {
                    String newString = s.subSequence(start, start + count).toString();
                    String imgName = EmojiTranslate.sEmojiMap.get(newString);
                    if (imgName != null) {
                        final String format = ":%s:";
                        String replaced = String.format(format, imgName);

                        Editable editable = content.getText();
                        editable.replace(start, start + count, replaced);
                        editable.setSpan(new EmojiconSpan(mActivity, imgName), start
                                , start + replaced.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } else {
                    int emojiStart = 0;
                    int emojiEnd;
                    boolean startFinded = false;
                    int end = start + count;
                    for (int i = start; i < end; ++i) {
                        if (s.charAt(i) == ':') {
                            if (!startFinded) {
                                emojiStart = i;
                                startFinded = true;
                            } else {
                                emojiEnd = i;
                                if (emojiEnd - emojiStart < 2) {
                                // 指示的是两个：的位置，如果是表情的话，间距肯定大于1
                                    emojiStart = emojiEnd;
                                } else {
                                    String newString = s.subSequence(emojiStart + 1, emojiEnd).toString();
                                    EmojiconSpan emojiSpan = new EmojiconSpan(mActivity, newString);
                                    if (emojiSpan.getDrawable() != null) {
                                        Editable editable = content.getText();
                                        editable.setSpan(emojiSpan, emojiStart
                                                , emojiEnd + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        startFinded = false;
                                    } else {
                                        emojiStart = emojiEnd;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void interceptInputMethod(EditText et){
        // Android.edittext点击时,隐藏系统弹出的键盘,显示出光标
        // 3.0以下版本可以用editText.setInputType(InputType.TYPE_NULL)来实现。
        // 3.0以上版本除了调用隐藏方法:setShowSoftInputOnFocus(false)
        int sdkInt = Build.VERSION.SDK_INT;// 16 -- 4.1系统
        if (sdkInt >= 11)
        {
            Class<EditText> cls = EditText.class;
            try
            {
                Method setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(et, false);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            et.setInputType(EditorInfo.TYPE_NULL);
        }
    }

    public EnterLayout(Activity activity, View.OnClickListener sendTextOnClick) {
        this(activity, sendTextOnClick, Type.Default);
    }

    public static void insertText(EditText edit, String s) {
        edit.requestFocus();
        int insertPos = edit.getSelectionStart();

        String insertString = s + " ";
        Editable editable = edit.getText();
        editable.insert(insertPos, insertString);
    }

    public void animEnterLayoutStatusChanaged(final boolean isOpen){
        if(mEnterLayoutStatus == isOpen){
            return;
        }
        mEnterLayoutStatus = isOpen;
        if(commonEnterRoot!=null && mEnterLayoutAnimSupportContainer!=null){
            ValueAnimator va = ValueAnimator.ofInt(isOpen?new int[]{-panelHeight,0}:new int[]{0,-panelHeight});
            va.setDuration(300);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int v = (int) animation.getAnimatedValue();
                    mEnterLayoutAnimSupportContainer.setEnterLayoutBottomMargin(v);
                    if (isOpen) {
                        onEnterLayoutPopUp(v);
                    } else {
                        onEnterLayoutDropDown(v);
                    }
                }
            });
            va.start();
        }
    }

    protected void onEnterLayoutPopUp(int bottom){

    }

    protected void onEnterLayoutDropDown(int bottom){

    }

    protected void updateEnterLayoutBottom(int bottom){
        if(bottom == 0){
            mEnterLayoutStatus = true;
        }else if(bottom == -panelHeight){
            mEnterLayoutStatus = false;
        }
        if(mEnterLayoutAnimSupportContainer!=null){
            mEnterLayoutAnimSupportContainer.setEnterLayoutBottomMargin(bottom);
        }
    }

    public void updateSendButtonStyle() {
        if (mType == Type.Default) {
            if (sendButtonEnable()) {
                sendText.setVisibility(View.VISIBLE);
                send.setVisibility(View.GONE);
            } else {
                sendText.setVisibility(View.GONE);
                send.setVisibility(View.VISIBLE);
            }
        }

        if (sendButtonEnable()) {
            sendText.setBackgroundResource(R.drawable.edit_send_green);
            sendText.setTextColor(0xffffffff);
        } else {
            sendText.setBackgroundResource(R.drawable.edit_send);
            sendText.setTextColor(0xff999999);
        }
    }

    protected boolean sendButtonEnable() {
        return content.getText().length() > 0;
    }

    public void hideKeyboard() {
        GlobalUtils.popSoftkeyboard(mActivity, content, false);
    }

    public void popKeyboard() {
        content.requestFocus();
        GlobalUtils.popSoftkeyboard(mActivity, content, true);
    }

    public void insertText(String s) {
        insertText(content, s);
    }

    public void setText(String s) {
        content.requestFocus();
        Editable editable = content.getText();
        editable.clear();
        editable.insert(0, s);
    }

    public void insertEmoji(String s) {
        int insertPos = content.getSelectionStart();
        final String format = ":%s:";
        String replaced = String.format(format, s);

        Editable editable = content.getText();
        editable.insert(insertPos, String.format(format, s));
        editable.setSpan(new EmojiconSpan(mActivity, s), insertPos, insertPos + replaced.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void deleteOneChar() {
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        content.dispatchKeyEvent(event);
    }

    public void clearContent() {
        content.setText("");
    }

    public String getContent() {
        return content.getText().toString();
    }

    public void hide() {
        if(commonEnterRoot!=null){
            commonEnterRoot.setVisibility(View.GONE);
        }

    }

    public void show() {
        if(commonEnterRoot!=null){
            commonEnterRoot.setVisibility(View.VISIBLE);
        }

    }

    public boolean isShow() {
        return commonEnterRoot!=null && commonEnterRoot.getVisibility() == View.VISIBLE;
    }

    public void restoreSaveStart() {
        content.addTextChangedListener(restoreWatcher);
    }

    public void restoreSaveStop() {
        content.removeTextChangedListener(restoreWatcher);
    }

//    public void restoreDelete(Object comment) {
//        CommentBackup.getInstance().delete(CommentBackup.BackupParam.create(comment));
//    }

//    public void restoreLoad(final Object object) {
//        if (object == null) {
//            return;
//        }
//        if(commonEnterRoot!=null && mEnterLayoutAnimSupportContainer!=null && !mEnterLayoutAnimSupportContainer.isAdjustResize()){
//            //common_enter_emoji控件由于在某些情况下第一次进入activity恢复数据时会出现显示不正常现象，因此先让控件以空文本形式正常显示出来
//            //之后再恢复数据
//            clearContent();
//            content.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    restoreSaveStop();
//                    String lastInput = CommentBackup.getInstance().load(CommentBackup.BackupParam.create(object));
//                    content.getText().append(lastInput);
//                    restoreSaveStart();
//                }
//            }, 100);
//        }else{
//            restoreSaveStop();
//            clearContent();
//            String lastInput = CommentBackup.getInstance().load(CommentBackup.BackupParam.create(object));
//            content.getText().append(lastInput);
//            restoreSaveStart();
//        }
//
//    }

    public enum Type {
        Default, TextOnly
    }

    public enum InputType{
        Text,Voice,Emoji
    }


    public interface CameraAndPhoto {
        void photo();
    }

}
