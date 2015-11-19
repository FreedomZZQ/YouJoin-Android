package me.zq.youjoin;

import android.test.AndroidTestCase;

import me.zq.youjoin.utils.StringUtils;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/11/19.
 */
public class StringUtilsTest extends AndroidTestCase {
    public void testFixJsonString(){
        String jsonString = StringUtils.FixJsonString("123{ \"\":\"\" }");
        assertEquals(jsonString, "{ \"\":\"\" }");
        jsonString = StringUtils.FixJsonString("{ \"\":\"\" }123");
        assertEquals(jsonString, "{ \"\":\"\" }");
        jsonString = StringUtils.FixJsonString("123{ \"\":\"\" }456");
        assertEquals(jsonString, "{ \"\":\"\" }");
    }
}
