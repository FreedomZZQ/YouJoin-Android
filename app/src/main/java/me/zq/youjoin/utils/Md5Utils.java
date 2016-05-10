package me.zq.youjoin.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 * YouJoin-Android
 * Created by ZQ on 2015/11/14.
 */
public class Md5Utils {

    public static String MD5_secure(String toSecure){
        String s = null;
        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(toSecure.getBytes());
            byte tmp[] = md.digest();          // MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            char str[] = new char[16 * 2];   // 每个字节用 16 进制表示的话，使用两个字符，
            // 所以表示成 16 进制需要 32 个字符
            int k = 0;                                // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) {    // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i];  // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];  // 取字节中高 4 位的数字转换,
                // >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf];   // 取字节中低 4 位的数字转换
            }
            s = new String(str);  // 换后的结果转换为字符串

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * @param val 需要加密的String串
     * @return MD5加密后的String串
     */
//    public static String getMd5(String val){
//        try{
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            md5.update(val.getBytes());
//            byte[] m = md5.digest();
//            return getString(m);
//        }catch(NoSuchAlgorithmException e){
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    private static String getString(byte[] b){
//        StringBuilder stringBuilder = new StringBuilder();
//        for(int i = 0; i < b.length; i++){
//            stringBuilder.append(b[i]);
//        }
//        return stringBuilder.toString();
//    }

    private Md5Utils(){}
}
