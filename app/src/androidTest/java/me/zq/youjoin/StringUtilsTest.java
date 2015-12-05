package me.zq.youjoin;

import android.test.AndroidTestCase;

import me.zq.youjoin.utils.StringUtils;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/11/19.
 */
public class StringUtilsTest extends AndroidTestCase {
    /**
     * 测试Json字符串修复
     */
    public void testFixJsonString(){
        String jsonString = StringUtils.FixJsonString("123{ \"\":\"\" }");
        assertEquals(jsonString, "{ \"\":\"\" }");
        jsonString = StringUtils.FixJsonString("{ \"\":\"\" }123");
        assertEquals(jsonString, "{ \"\":\"\" }");
        jsonString = StringUtils.FixJsonString("123{ \"\":\"\" }456");
        assertEquals(jsonString, "{ \"\":\"\" }");
    }


    /**
     * 测试参数类型判断
     */
    public void testGetParamType(){
        assertEquals(StringUtils.getParamType("11111"), "1");
        assertEquals(StringUtils.getParamType("a123"), "2");
        assertEquals(StringUtils.getParamType("zzq@test.com"), "3");
        assertEquals(StringUtils.getParamType("1a23"), "invalid");
    }
}
