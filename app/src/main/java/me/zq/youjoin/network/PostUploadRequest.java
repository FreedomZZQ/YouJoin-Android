package me.zq.youjoin.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import me.zq.youjoin.model.ImageInfo;
import me.zq.youjoin.utils.StringUtils;

/**
 * YouJoin
 * Created by ZQ on 2015/11/12.
 */
public class PostUploadRequest<T> extends Request<T> {

    /**
     * 正确数据的时候回掉用
     */
    private ResponseListener mListener ;
    /*请求 数据通过参数的形式传入*/
    private List<ImageInfo> mListItem ;

    private static final String BOUNDARY = "--------------520-13-14"; //数据分隔线
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private static final String TAG = "YouJoin_upload_request";
    private static final String PARAM = "uploadedfile";

    private Map<String, String> params;
    private Gson gson;
    private Type clazz;

    public PostUploadRequest(String url, List<ImageInfo> listItem,
                             Map<String, String> params,
                             Type type, ResponseListener listener) {
        super(Method.POST, url, listener);
        this.mListener = listener ;
        this.params = params;
        this.gson = new Gson();
        this.clazz = type;
        setShouldCache(false);
        mListItem = listItem ;
        //设置请求的响应事件，因为文件上传需要较长的时间，所以在这里加大了，设为5秒
        setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * 这里开始解析数据
     * @param response Response from the network
     * @return
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String mString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.v(TAG, "mString = " + mString);
            T result = gson.fromJson(StringUtils.FixJsonString(mString), clazz);
            return Response.success(result,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    /**
     * 回调正确的数据
     * @param response The parsed response returned by
     */
    @Override
    protected void deliverResponse(T response) {

        mListener.onResponse(response);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (mListItem == null||mListItem.size() == 0){
            return super.getBody() ;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;

        if(!params.isEmpty()) {
            StringBuilder sbParams = new StringBuilder();

            for (Map.Entry<String, String> o : params.entrySet()) {
                Map.Entry entry = (Map.Entry) o;

                /*第一行*/
                //`"--" + BOUNDARY + "\r\n"`
                sbParams.append("--" + BOUNDARY);
                sbParams.append("\r\n");
                /*第二行*/
                //Content-Disposition: form-data; name="参数的名称"; + 参数 + "\r\n"
                sbParams.append("Content-Disposition: form-data;");
                sbParams.append(" name=\"");
                sbParams.append((String) entry.getKey());
                sbParams.append("\"");
                sbParams.append("\r\n");
                sbParams.append("\r\n");
                sbParams.append((String) entry.getValue());
                sbParams.append("\r\n");
            }
            try {
                bos.write(sbParams.toString().getBytes("utf-8"));
//                bos.write("\r\n".getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        int N = mListItem.size() ;
        ImageInfo imageInfo ;
        for (int i = 0; i < N ;i++){
            imageInfo = mListItem.get(i) ;
            StringBuilder sb= new StringBuilder() ;
            /*第一行*/
            //`"--" + BOUNDARY + "\r\n"`
            sb.append("--"+BOUNDARY);
            sb.append("\r\n") ;
            /*第二行*/
            //Content-Disposition: form-data; name="参数的名称"; filename="上传的文件名" + "\r\n"
            sb.append("Content-Disposition: form-data;");
            sb.append(" name=\"");
            sb.append(PARAM + "[" + Integer.toString(i) + "]");
            sb.append("\"") ;
            sb.append("; filename=\"") ;
            sb.append(imageInfo.getFileName()) ;
            sb.append("\"");
            sb.append("\r\n") ;
            /*第三行*/
            //Content-Type: 文件的 mime 类型 + "\r\n"
            sb.append("Content-Type: ");
            sb.append(imageInfo.getMime()) ;
            sb.append("\r\n") ;
            /*第四行*/
            //"\r\n"
            sb.append("\r\n") ;
            try {
                bos.write(sb.toString().getBytes("utf-8"));
                /*第五行*/
                //文件的二进制数据 + "\r\n"
                bos.write(imageInfo.getValue());
                bos.write("\r\n".getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        /*结尾行*/
        //`"--" + BOUNDARY + "--" + "\r\n"`
        String endLine = "--" + BOUNDARY + "--" + "\r\n" ;
        try {
            bos.write(endLine.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
       // Log.v(TAG,"imageInfo =\n"+bos.toString()) ;
        return bos.toByteArray();
    }
    //Content-Type: multipart/form-data; boundary=----------8888888888888
    @Override
    public String getBodyContentType() {
        return MULTIPART_FORM_DATA+"; boundary="+BOUNDARY;
    }

//    @Override
//    protected Map<String, String> getParams() throws AuthFailureError{
//        return params;
//    }
}