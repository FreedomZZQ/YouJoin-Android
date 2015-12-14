package me.zq.youjoin.widget.citypicker;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/15.
 */
public class CitycodeUtil {

    private ArrayList<String> province_list = new ArrayList<String>();
    private ArrayList<String> city_list = new ArrayList<String>();
    private ArrayList<String> couny_list = new ArrayList<String>();
    public ArrayList<String> province_list_code = new ArrayList<String>();
    public ArrayList<String> city_list_code = new ArrayList<String>();
    public ArrayList<String> couny_list_code = new ArrayList<String>();
    /** 单例 */
    public static CitycodeUtil model;
    private Context context;

    private CitycodeUtil() {
    }

    public ArrayList<String> getProvince_list_code() {
        return province_list_code;
    }

    public ArrayList<String> getCity_list_code() {
        return city_list_code;
    }

    public void setCity_list_code(ArrayList<String> city_list_code) {
        this.city_list_code = city_list_code;
    }

    public ArrayList<String> getCouny_list_code() {
        return couny_list_code;
    }

    public void setCouny_list_code(ArrayList<String> couny_list_code) {
        this.couny_list_code = couny_list_code;
    }

    public void setProvince_list_code(ArrayList<String> province_list_code) {

        this.province_list_code = province_list_code;
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static CitycodeUtil getSingleton() {
        if (null == model) {
            model = new CitycodeUtil();
        }
        return model;
    }

    public ArrayList<String> getProvince(List<Cityinfo> provice) {
        if (province_list_code.size() > 0) {
            province_list_code.clear();
        }
        if (province_list.size() > 0) {
            province_list.clear();
        }
        for (int i = 0; i < provice.size(); i++) {
            province_list.add(provice.get(i).getCity_name());
            province_list_code.add(provice.get(i).getId());
        }
        return province_list;

    }

    public ArrayList<String> getCity(
            HashMap<String, List<Cityinfo>> cityHashMap, String provicecode) {
        if (city_list_code.size() > 0) {
            city_list_code.clear();
        }
        if (city_list.size() > 0) {
            city_list.clear();
        }
        List<Cityinfo> city = new ArrayList<Cityinfo>();
        city = cityHashMap.get(provicecode);
        System.out.println("city--->" + city.toString());
        for (int i = 0; i < city.size(); i++) {
            city_list.add(city.get(i).getCity_name());
            city_list_code.add(city.get(i).getId());
        }
        return city_list;

    }

    public ArrayList<String> getCouny(
            HashMap<String, List<Cityinfo>> cityHashMap, String citycode) {
        System.out.println("citycode" + citycode);
        List<Cityinfo> couny = null;
        if (couny_list_code.size() > 0) {
            couny_list_code.clear();

        }
        if (couny_list.size() > 0) {
            couny_list.clear();
        }
        if (couny == null) {
            couny = new ArrayList<Cityinfo>();
        } else {
            couny.clear();
        }

        couny = cityHashMap.get(citycode);
        System.out.println("couny--->" + couny.toString());
        for (int i = 0; i < couny.size(); i++) {
            couny_list.add(couny.get(i).getCity_name());
            couny_list_code.add(couny.get(i).getId());
        }
        return couny_list;

    }
}

