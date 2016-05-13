package me.zq.youjoin.fragment;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.activity.UserInfoActivity;
import me.zq.youjoin.adapter.FriendListAdapter;
import me.zq.youjoin.model.FriendsInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.network.ResponseListener;
import me.zq.youjoin.utils.Md5Utils;
import me.zq.youjoin.utils.StringUtils;
import me.zq.youjoin.widget.sidebar.CustomEditText;
import me.zq.youjoin.widget.sidebar.SideBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AroundFragment extends BaseFragment implements SideBar.OnTouchingLetterChangedListener, TextWatcher {

    @Bind(R.id.refresher)
    SwipeRefreshLayout refresher;
    @Bind(R.id.search_input)
    CustomEditText searchInput;
    @Bind(R.id.friend_list)
    ListView friendList;
    @Bind(R.id.friend_dialog)
    TextView friendDialog;
    @Bind(R.id.friend_sidebar)
    SideBar friendSidebar;

    private FriendListAdapter adapter;
    private List<FriendsInfo.FriendsEntity> dataList = new ArrayList<>();

    private LocationManager locationManager;
    private String provider;

    private static String baseLocation = "";
    private static double size = 0.001;
    private static int len = 3;


    public AroundFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_around, container, false);
        ButterKnife.bind(this, view);


        initView();

        return view;
    }

    private void initView() {

        friendSidebar.setTextView(friendDialog);
        friendSidebar.setOnTouchingLetterChangedListener(this);

        searchInput.addTextChangedListener(this);

        adapter = new FriendListAdapter(friendList, dataList);
        friendList.setAdapter(adapter);

        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInfoActivity.actionStart(getActivity(),
                        UserInfoActivity.TYPE_OTHER_USER, dataList.get(position).getId());

            }
        });

        refresher.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLocation();
            }
        });

        getLocation();
    }

    private void getLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(getActivity(), "No Location Provider to Use", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                onGetLocation(location);
            }else{
                refresher.setRefreshing(false);
                Toast.makeText(getActivity(), "Can not get location", Toast.LENGTH_SHORT).show();
            }
            locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            //更新当前设备位置信息
            onGetLocation(location);
        }
    };

    private void onGetLocation(Location location) {

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        String[] locationString = {
                StringUtils.double2String(longitude, len) + StringUtils.double2String(latitude, len),
                StringUtils.double2String(longitude + size, len) + StringUtils.double2String(latitude, len),
                StringUtils.double2String(longitude, len) + StringUtils.double2String(latitude + size, len),
                StringUtils.double2String(longitude + size, len) + StringUtils.double2String(latitude + size, len)
        };

        if (locationString[0].equals(baseLocation)) {
            NetworkManager.postRequestAround(Integer.toString(YouJoinApplication.getCurrUser().getId()),
                    false,
                    null,
                    new ResponseListener<FriendsInfo>() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if(refresher != null){
                                refresher.setRefreshing(false);
                            }

                        }

                        @Override
                        public void onResponse(FriendsInfo info) {
                            if(refresher != null){
                                refresher.setRefreshing(false);

                                Log.d(TAG, info.getResult());
                                dataList.clear();
                                dataList.addAll(info.getFriends());
                                adapter.refresh(dataList);
                                searchInput.setText("");
                            }
                        }
                    });
        }else{

            baseLocation = locationString[0];
            List<String> locationKeyList = new ArrayList<>();
            for (String s : locationString) {
                locationKeyList.add(Md5Utils.MD5_secure(s));
            }

            NetworkManager.postRequestAround(Integer.toString(YouJoinApplication.getCurrUser().getId()),
                    true,
                    locationKeyList,
                    new ResponseListener<FriendsInfo>() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if(refresher != null){
                                refresher.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onResponse(FriendsInfo info) {
                            if(refresher != null){
                                refresher.setRefreshing(false);
                                Log.d(TAG, info.getResult());
                                dataList.clear();
                                dataList.addAll(info.getFriends());
                                adapter.refresh(dataList);
                                searchInput.setText("");
                            }
                        }
                    });
        }

    }

    @Override
    public void onTouchingLetterChanged(String s){
        int position = 0;
        //该字母首次出现的位置
        if(adapter != null){
            position = adapter.getPositionForSection(s.charAt(0));
        }
        if(position != -1){
            friendList.setSelection(position);
        }else if(s.contains("#")){
            friendList.setSelection(0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        List<FriendsInfo.FriendsEntity> temp = new ArrayList<>();
        temp.addAll(dataList);
        if(!searchInput.getText().toString().equals("")){
            for (FriendsInfo.FriendsEntity data : dataList) {
                if (data.getNickname().contains(s) || data.getPinyin().contains(s)) {
                } else {
                    temp.remove(data);
                }
            }
        }

        if (adapter != null) {
            adapter.refresh(temp);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
