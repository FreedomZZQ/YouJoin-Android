package me.zq.youjoin.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

import me.zq.youjoin.utils.FileUtils;
import me.zq.youjoin.utils.LogUtils;
import me.zq.youjoin.utils.MimeUtils;

/**
 * YouJoin
 * Created by ZQ on 2015/11/12.
 */
public class ImageInfo {

    //文件名
    private String fileName ;
    //文件的 mime，需要根据文档查询
    private String mime ;
    //文件的路径
    private String imagePath;

    public ImageInfo(String filePath){
        this.imagePath = filePath;
        this.fileName = FileUtils.getFileName(filePath);
        this.mime = MimeUtils.guessMimeTypeFromExtension(FileUtils.getFileExtension(filePath));
    }

    //对图片进行二进制转换
    public byte[] getValue() {
        if(imagePath == null){
            return new ByteArrayOutputStream().toByteArray();
        }
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        if(bitmap == null){
            LogUtils.e(imagePath);
            return null;
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos) ;
        return bos.toByteArray();
    }

    public String getFileName() {
        return fileName;
    }

    public String getMime() {
        return mime;
    }

    public String getImagePath() {
        return imagePath;
    }

}
