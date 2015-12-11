package me.zq.youjoin.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.zq.youjoin.widget.enter.MyImageGetter;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/11/19.
 */
public class StringUtils {

    public static SpannableString getEmotionContent(final Context context, final TextView tv, String source) {
        SpannableString spannableString = new SpannableString(source);
        Resources res = context.getResources();

        //String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]" ;
        String regexEmotion = ":\\w+:";
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);

        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            int length = key.length();
            key = key.substring(key.indexOf(":") + 1, key.lastIndexOf(":"));
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            // 利用表情名字获取到对应的图片
            Integer imgRes = MyImageGetter.getResourceId(key);
            if (imgRes != null) {
                // 压缩表情图片
                int size = (int) tv.getTextSize();
                Bitmap bitmap = BitmapFactory.decodeResource(res, imgRes);
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);

                ImageSpan span = new ImageSpan(context, scaleBitmap);
                spannableString.setSpan(span, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
            }
        }
        return spannableString;
    }

    public static List<String> getPicUrlList(String pics){
        List<String> picUrlList = new ArrayList<>();

        String[] picArray = pics.split(";");

        Collections.addAll(picUrlList, picArray);

        return picUrlList;
    }

    public static String getParamType(String param){
        if(isEmailAddress(param)){
            return "3";
        } else if(isLetter(param.charAt(0))){
            return "2";
        } else if(isAllNumber(param)){
            return "1";
        }else{
            return "invalid";
        }
    }

    public static boolean isAllNumber(String s){
        return s.matches("\\d+");
    }

    public static boolean isLetter(char a){
        return (a >= 'A' && a <= 'Z') || (a >= 'a' && a <= 'z');
    }

    public static boolean isEmailAddress(String email){
        return email.contains("@");
    }

    /**
     * 对接收到的json字符串进行处理，去掉可能的垃圾头和垃圾尾，使其符合json格式
     * @param jsonString 接收到的json字符串
     * @return 处理后的json字符串
     */
    public static String FixJsonString(String jsonString){

        int startIndex = jsonString.indexOf('{');
        int endIndex = jsonString.lastIndexOf('}');

        //LogUtils.d("hehe", jsonString.substring(startIndex, endIndex + 1));
        return jsonString.substring(startIndex, endIndex + 1);
    }

    public static String getFirstLetters(String chinese){
        StringBuilder pysb = new StringBuilder();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char anArr : arr) {
            if (anArr > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(anArr, defaultFormat);
                    if (temp != null) {
                        pysb.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pysb.append(anArr);
            }
        }
        return pysb.toString().replaceAll("\\W", "").trim();
    }



    private StringUtils(){}
}
