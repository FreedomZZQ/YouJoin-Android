package me.zq.youjoin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.R;
import me.zq.youjoin.widget.citypicker.CityPicker;

public class ChooseLocationActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.city_picker)
    CityPicker cityPicker;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yj_activity_choose_location);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = cityPicker.GetProvince()
                        + " " + cityPicker.GetCity()
                        + " " + cityPicker.GetCounty();
                Intent intent = new Intent();
                intent.putExtra("location", location);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public static void actionStart(Activity activity, int resultCode){
        Intent intent = new Intent(activity, ChooseLocationActivity.class);
        activity.startActivityForResult(intent, resultCode);
    }



}
