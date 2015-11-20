package me.zq.youjoin;

import android.test.AndroidTestCase;

import me.zq.youjoin.model.ImageInfo;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/11/19.
 */
public class ImageInfoTest extends AndroidTestCase {
    public void testConstructor(){
        String path = "/storage/emulated/0/Images/123.jpg";
        ImageInfo info = new ImageInfo(path);
        assertEquals(info.getFileName(), "123.jpg");
        assertEquals(info.getImagePath(), path);
        assertEquals(info.getMime(), "image/jpeg");

        path = "/storage/emulated/0/Images/234.png";
        info = new ImageInfo(path);
        assertEquals(info.getFileName(), "234.png");
        assertEquals(info.getImagePath(), path);
        assertEquals(info.getMime(), "image/png");
    }
}
