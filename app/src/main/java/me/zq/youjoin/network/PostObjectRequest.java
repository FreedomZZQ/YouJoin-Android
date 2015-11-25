package me.zq.youjoin.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

import me.zq.youjoin.utils.LogUtils;
import me.zq.youjoin.utils.StringUtils;

/**
 * Created by ZQ on 2015/11/12.
 */
public class PostObjectRequest<T> extends Request<T> {

    private ResponseListener listener;
    private Gson gson;
    private Type clazz;
    private Map<String, String> params;

    public PostObjectRequest(String url, Map<String, String> params, Type type,
                             ResponseListener listener){
        super(Method.POST, url, listener);
        this.listener = listener;
        this.clazz = type;
        this.params = params;
        this.gson = new Gson();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response){
        try{
            T result;
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            LogUtils.d("hehe", jsonString);
            result = gson.fromJson(StringUtils.FixJsonString(jsonString), clazz);
            return Response.success(result,
                    HttpHeaderParser.parseCacheHeaders(response));
        }catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response){
        listener.onResponse(response);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return params;
    }

}
