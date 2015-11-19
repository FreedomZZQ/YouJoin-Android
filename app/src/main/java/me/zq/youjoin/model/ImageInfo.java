package me.zq.youjoin.model;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * YouJoin
 * Created by ZQ on 2015/11/12.
 */
public class ImageInfo {

    //参数的名称
    private String paraName ;
    //文件名
    private String fileName ;
    //文件的 mime，需要根据文档查询
    private String mime ;
    //需要上传的图片资源，因为这里测试为了方便起见，直接把 bitmap 传进来
    //真正在项目中一般不会这般做，而是把图片的路径传过来，在这里对图片进行二进制转换
    private Bitmap bitmap;
    private String imagePath;

    public ImageInfo(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

//    public String getName() {
//        //        return mName;
//        //测试，把参数名称写死
//        return "uploadedfile" ;
//    }

    //对图片进行二进制转换
    public byte[] getValue() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos) ;
        return bos.toByteArray();
    }

    public String getFileName() {
        //测试，直接写死文件的名字
        //return "lufei.jpg";
        return fileName;
    }

    public String getMime() {
        //return "image/jpg";
        return mime;
    }

    public String getParaName() {
        return paraName;
    }

    public void setParaName(String paraName) {
        this.paraName = paraName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
