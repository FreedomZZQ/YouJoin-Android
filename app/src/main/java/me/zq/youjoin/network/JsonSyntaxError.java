package me.zq.youjoin.network;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

/**
 * YouJoin-Android
 * Created by ZQ on 2016/4/20.
 */
public class JsonSyntaxError extends VolleyError {
    public JsonSyntaxError() {
    }

    public JsonSyntaxError(NetworkResponse networkResponse) {
        super(networkResponse);
    }

    public JsonSyntaxError(Throwable cause) {
        super(cause);
    }
}
