package me.zq.youjoin.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/11/19.
 */
public class StringUtils {

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
