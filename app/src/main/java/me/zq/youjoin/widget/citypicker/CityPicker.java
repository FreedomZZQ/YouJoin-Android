package me.zq.youjoin.widget.citypicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.zq.youjoin.R;
import me.zq.youjoin.utils.FileUtils;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/15.
 */
public class CityPicker extends LinearLayout {

    private Spinner provinceSpinner;
    private Spinner citySpinner;
    private Spinner countySpinner;
    private ArrayAdapter<String> provinceAdapter = null;  //省级适配器
    private ArrayAdapter<String> cityAdapter = null;    //地级适配器
    private ArrayAdapter<String> countyAdapter = null;    //县级适配器
    static int provincePosition = 0;

    private Context context;
    private CitycodeUtil citycodeUtil;

    private List<Cityinfo> province_list = new ArrayList<Cityinfo>();
    private List<Cityinfo> city_list = new ArrayList<Cityinfo>();
    private List<Cityinfo> county_list = new ArrayList<Cityinfo>();

    private HashMap<String, List<Cityinfo>> city_map = new HashMap<String, List<Cityinfo>>();
    private HashMap<String, List<Cityinfo>> county_map = new HashMap<String, List<Cityinfo>>();

    private List<String> province_string_list = new ArrayList<String>();
    private List<String> city_string_list = new ArrayList<String>();
    private List<String> county_string_list = new ArrayList<String>();

    public String GetAddress(){
        return provinceSpinner.getItemAtPosition(provinceSpinner.getSelectedItemPosition()).toString()
                + citySpinner.getItemAtPosition(citySpinner.getSelectedItemPosition()).toString()
                + countySpinner.getItemAtPosition(countySpinner.getSelectedItemPosition()).toString();

    }

    public String GetProvince(){
        return provinceSpinner.getItemAtPosition(provinceSpinner.getSelectedItemPosition()).toString();

    }

    public String GetCity(){
        return citySpinner.getItemAtPosition(citySpinner.getSelectedItemPosition()).toString();

    }

    public String GetCounty(){
        return countySpinner.getItemAtPosition(countySpinner.getSelectedItemPosition()).toString();

    }

    public CityPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        getAddressInfo();
    }

    public CityPicker(Context context){
        super(context);
        this.context = context;
        getAddressInfo();

    }

    private void getAddressInfo(){
        // 读取城市信息string
        JSONParser parser = new JSONParser();
        String area_str = FileUtils.readAssets(context, "area.json");
        province_list = parser.getJSONParserResult(area_str, "area0");

        city_map = parser.getJSONParserResultArray(area_str, "area1");

        county_map = parser.getJSONParserResultArray(area_str, "area2");

        for(Cityinfo info : province_list){
            province_string_list.add(info.getCity_name());
        }

    }

    public static class JSONParser {
        public ArrayList<String> province_list_code = new ArrayList<String>();
        public ArrayList<String> city_list_code = new ArrayList<String>();

        public List<Cityinfo> getJSONParserResult(String JSONString, String key) {
            List<Cityinfo> list = new ArrayList<Cityinfo>();
            JsonObject result = new JsonParser().parse(JSONString)
                    .getAsJsonObject().getAsJsonObject(key);

            Iterator iterator = result.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) iterator.next();
                Cityinfo cityinfo = new Cityinfo();

                cityinfo.setCity_name(entry.getValue().getAsString());
                cityinfo.setId(entry.getKey());
                province_list_code.add(entry.getKey());
                list.add(cityinfo);
            }
            System.out.println(province_list_code.size());
            return list;
        }
        public HashMap<String, List<Cityinfo>> getJSONParserResultArray(
                String JSONString, String key) {
            HashMap<String, List<Cityinfo>> hashMap = new HashMap<String, List<Cityinfo>>();
            JsonObject result = new JsonParser().parse(JSONString)
                    .getAsJsonObject().getAsJsonObject(key);

            Iterator iterator = result.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) iterator.next();
                List<Cityinfo> list = new ArrayList<Cityinfo>();
                JsonArray array = entry.getValue().getAsJsonArray();
                for (int i = 0; i < array.size(); i++) {
                    Cityinfo cityinfo = new Cityinfo();
                    cityinfo.setCity_name(array.get(i).getAsJsonArray().get(0)
                            .getAsString());
                    cityinfo.setId(array.get(i).getAsJsonArray().get(1)
                            .getAsString());
                    city_list_code.add(array.get(i).getAsJsonArray().get(1)
                            .getAsString());
                    list.add(cityinfo);
                }
                hashMap.put(entry.getKey(), list);
            }
            return hashMap;
        }
    }

    /*
     * 设置下拉框
     */
    private void setSpinner(){

        //绑定适配器和值
        provinceAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, province_string_list);
        provinceSpinner.setAdapter(provinceAdapter);
        provinceSpinner.setSelection(0,true);

        cityAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, city_string_list);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setSelection(0,true);  //默认选中第0个

        countyAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, county_string_list);
        countySpinner.setAdapter(countyAdapter);
        countySpinner.setSelection(0, true);


        //省级下拉框监听
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                //position为当前省级选中的值的序号

                String id = province_list.get(position).getId();
                city_list = city_map.get(id);
                city_string_list.clear();
                for(Cityinfo info : city_list){
                    city_string_list.add(info.getCity_name());
                }

                cityAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, city_string_list);
                // 设置二级下拉列表的选项内容适配器
                citySpinner.setAdapter(cityAdapter);
                provincePosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }

        });


        //地级下拉监听
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3)
            {
                String id = city_list.get(position).getId();
                county_list = county_map.get(id);
                county_string_list.clear();
                for(Cityinfo info : county_list){
                    county_string_list.add(info.getCity_name());
                }

                countyAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, county_string_list);
                countySpinner.setAdapter(countyAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.city_spinner, this);
        citycodeUtil = CitycodeUtil.getSingleton();
        // 获取控件引用
        provinceSpinner = (Spinner) findViewById(R.id.address_province);
        citySpinner = (Spinner) findViewById(R.id.address_city);
        countySpinner = (Spinner) findViewById(R.id.address_county);

        setSpinner();
    }

}

