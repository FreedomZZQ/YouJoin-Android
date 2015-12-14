/**
 * Copyright 2014 Zhenguo Jin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.zq.youjoin.utils;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * 文件操作工具类
 * <p/>
 * <ul>
 * Read or write file
 * <li>{@link #readFile(String)} read file</li>
 * <li>{@link #readFileToList(String)} read file to string list</li>
 * <li>{@link #writeFile(String, String, boolean)} write file from String</li>
 * <li>{@link #writeFile(String, String)} write file from String</li>
 * <li>{@link #writeFile(String, List, boolean)} write file from String List</li>
 * <li>{@link #writeFile(String, List)} write file from String List</li>
 * <li>{@link #writeFile(String, InputStream)} write file</li>
 * <li>{@link #writeFile(String, InputStream, boolean)} write file</li>
 * <li>{@link #writeFile(File, InputStream)} write file</li>
 * <li>{@link #writeFile(File, InputStream, boolean)} write file</li>
 * </ul>
 * <ul>
 * Operate file
 * <li>{@link #copyFile(String, String)}</li>
 * <li>{@link #getFileExtension(String)}</li>
 * <li>{@link #getFileName(String)}</li>
 * <li>{@link #getFileNameWithoutExtension(String)}</li>
 * <li>{@link #getFileSize(String)}</li>
 * <li>{@link #deleteFile(String)}</li>
 * <li>{@link #isFileExist(String)}</li>
 * <li>{@link #isFolderExist(String)}</li>
 * <li>{@link #makeFolders(String)}</li>
 * <li>{@link #makeDirs(String)}</li>
 * </ul>
 *
 * @author jingle1267@163.com
 */

/**
 * YouJoin-Android
 * Created by ZQ on 2015/11/19.
 */
public final class FileUtils {

    public final static String FILE_EXTENSION_SEPARATOR = ".";

    /**
     * get file name from path, include suffix
     * <p/>
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     *
     * @param filePath 文件路径
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }


    /**
     * get suffix of file from path
     * <p/>
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     *
     * @param filePath 文件路径
     * @return
     */
    public static String getFileExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * 删除文件
     *
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名，要在系统内保持唯一
     * @return boolean 存储成功的标志
     */
    public static boolean deleteFile(Context context, String fileName)
    {
        return context.deleteFile(fileName);
    }

    /**
     * 文件是否存在
     *
     * @param context
     * @param fileName
     * @return
     */
    public static boolean exists(Context context, String fileName)
    {
        return new File(context.getFilesDir(), fileName).exists();
    }

    /**
     * 存储文本数据
     *
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名，要在系统内保持唯一
     * @param content
     *            文本内容
     * @return boolean 存储成功的标志
     */
    public static boolean writeFile(Context context, String fileName, String content)
    {
        boolean success = false;
        FileOutputStream fos = null;
        try
        {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            byte[] byteContent = content.getBytes();
            fos.write(byteContent);

            success = true;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fos != null) fos.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }

        return success;
    }

    /**
     * 存储文本数据
     *
     * @param filePath
     *
     * @param content
     *            文本内容
     * @return boolean 存储成功的标志
     */
    public static boolean writeFile(String filePath, String content)
    {
        boolean success = false;
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(filePath);
            byte[] byteContent = content.getBytes();
            fos.write(byteContent);

            success = true;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fos != null) fos.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }

        return success;
    }

    /**
     * 读取文本数据
     *
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名
     * @return String, 读取到的文本内容，失败返回null
     */
    public static String readFile(Context context, String fileName)
    {
        if (!exists(context, fileName)) { return null; }
        FileInputStream fis = null;
        String content = null;
        try
        {
            fis = context.openFileInput(fileName);
            if (fis != null)
            {

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true)
                {
                    int readLength = fis.read(buffer);
                    if (readLength == -1) break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                fis.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());

            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            content = null;
        }
        finally
        {
            try
            {
                if (fis != null) fis.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 读取文本数据
     *
     * @param filePath
     *
     * @return String, 读取到的文本内容，失败返回null
     */
    public static String readFile(String filePath)
    {
        if (filePath == null || !new File(filePath).exists()) { return null; }
        FileInputStream fis = null;
        String content = null;
        try
        {
            fis = new FileInputStream(filePath);
            if (fis != null)
            {

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true)
                {
                    int readLength = fis.read(buffer);
                    if (readLength == -1) break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                fis.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());

            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            content = null;
        }
        finally
        {
            try
            {
                if (fis != null) fis.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 读取文本数据
     *
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名
     * @return String, 读取到的文本内容，失败返回null
     */
    public static String readAssets(Context context, String fileName)
    {
        InputStream is = null;
        String content = null;
        try
        {
            is = context.getAssets().open(fileName);
            if (is != null)
            {

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true)
                {
                    int readLength = is.read(buffer);
                    if (readLength == -1) break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                is.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());

            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            content = null;
        }
        finally
        {
            try
            {
                if (is != null) is.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 存储单个Parcelable对象
     *
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名，要在系统内保持唯一
     * @param parcelObject
     *            对象必须实现Parcelable
     * @return boolean 存储成功的标志
     */
    public static boolean writeParcelable(Context context, String fileName, Parcelable parcelObject)
    {
        boolean success = false;
        FileOutputStream fos = null;
        try
        {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            Parcel parcel = Parcel.obtain();
            parcel.writeParcelable(parcelObject, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
            byte[] data = parcel.marshall();
            fos.write(data);

            success = true;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }

        return success;
    }

    /**
     * 存储List对象
     *
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名，要在系统内保持唯一
     * @param list
     *            对象数组集合，对象必须实现Parcelable
     * @return boolean 存储成功的标志
     */
    public static boolean writeParcelableList(Context context, String fileName, List<Parcelable> list)
    {
        boolean success = false;
        FileOutputStream fos = null;
        try
        {
            if (list instanceof List)
            {
                fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                Parcel parcel = Parcel.obtain();
                parcel.writeList(list);
                byte[] data = parcel.marshall();
                fos.write(data);

                success = true;
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }

        return success;
    }

    /**
     * 读取单个数据对象
     *
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名
     * @return Parcelable, 读取到的Parcelable对象，失败返回null
     */
    @SuppressWarnings("unchecked")
    public static Parcelable readParcelable(Context context, String fileName, ClassLoader classLoader)
    {
        Parcelable parcelable = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try
        {
            fis = context.openFileInput(fileName);
            if (fis != null)
            {
                bos = new ByteArrayOutputStream();
                byte[] b = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(b)) != -1)
                {
                    bos.write(b, 0, bytesRead);
                }

                byte[] data = bos.toByteArray();

                Parcel parcel = Parcel.obtain();
                parcel.unmarshall(data, 0, data.length);
                parcel.setDataPosition(0);
                parcelable = parcel.readParcelable(classLoader);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            parcelable = null;
        }
        finally
        {
            if (fis != null) try
            {
                fis.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if (bos != null) try
            {
                bos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return parcelable;
    }

    /**
     * 读取数据对象列表
     *
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名
     * @return List, 读取到的对象数组，失败返回null
     */
    @SuppressWarnings("unchecked")
    public static List<Parcelable> readParcelableList(Context context, String fileName, ClassLoader classLoader)
    {
        List<Parcelable> results = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try
        {
            fis = context.openFileInput(fileName);
            if (fis != null)
            {
                bos = new ByteArrayOutputStream();
                byte[] b = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(b)) != -1)
                {
                    bos.write(b, 0, bytesRead);
                }

                byte[] data = bos.toByteArray();

                Parcel parcel = Parcel.obtain();
                parcel.unmarshall(data, 0, data.length);
                parcel.setDataPosition(0);
                results = parcel.readArrayList(classLoader);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            results = null;
        }
        finally
        {
            if (fis != null) try
            {
                fis.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if (bos != null) try
            {
                bos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return results;
    }

    public static boolean saveSerializable(Context context, String fileName, Serializable data)
    {
        boolean success = false;
        ObjectOutputStream oos = null;
        try
        {
            oos = new ObjectOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            oos.writeObject(data);
            success = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (oos != null)
            {
                try
                {
                    oos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    public static Serializable readSerialLizable(Context context, String fileName)
    {
        Serializable data = null;
        ObjectInputStream ois = null;
        try
        {
            ois = new ObjectInputStream(context.openFileInput(fileName));
            data = (Serializable) ois.readObject();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (ois != null)
            {
                try
                {
                    ois.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return data;
    }

    /**
     * 从assets里边读取字符串
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getFromAssets(Context context, String fileName)
    {
        try
        {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 复制文件
     *
     * @param srcFile
     * @param dstFile
     * @return
     */
    public static boolean copy(String srcFile, String dstFile)
    {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try
        {

            File dst = new File(dstFile);
            if (!dst.getParentFile().exists())
            {
                dst.getParentFile().mkdirs();
            }

            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(dstFile);

            byte[] buffer = new byte[1024];
            int len = 0;

            while ((len = fis.read(buffer)) != -1)
            {
                fos.write(buffer, 0, len);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }
        return true;
    }

}