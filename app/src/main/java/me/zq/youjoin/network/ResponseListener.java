package me.zq.youjoin.network;

import com.android.volley.Response;

/**
 * Created by ZQ on 2015/11/12.
 */
public interface ResponseListener<T> extends Response.ErrorListener,Response.Listener<T> {

}

