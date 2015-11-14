package me.zq.youjoin.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 * YouJoin-Android
 * Created by ZQ on 2015/11/14.
 */
public class Md5Utils {

    /**
     * @param val 需要加密的String串
     * @return MD5加密后的String串
     */
    public static String getMd5(String val){
        try{
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(val.getBytes());
            byte[] m = md5.digest();
            return getString(m);
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return "";
    }

    private static String getString(byte[] b){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < b.length; i++){
            stringBuilder.append(b[i]);
        }
        return stringBuilder.toString();
    }


}
