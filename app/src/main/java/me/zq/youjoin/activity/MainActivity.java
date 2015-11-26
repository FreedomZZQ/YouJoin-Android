package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.fragment.TweetsFragment;
import me.zq.youjoin.model.UserInfo;

public class MainActivity extends BaseActivity  {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.navigation)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    ImageView ivUserPhoto;
    TextView tvUserName;
    TextView tvUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yj_activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        initView();

        switchToTweets();
    }



    private void initView(){
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close){
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);

                //fab.show();
            }

            public void onDrawerOpened(View view){
                super.onDrawerOpened(view);
                //fab.hide();
            }
        };
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        setupNavigationAction(navigationView);

        View navigationHeadView = navigationView.getHeaderView(0);
        if(navigationView == null) return;

        ivUserPhoto = (ImageView) navigationHeadView.findViewById(R.id.navigation_avatar);
        tvUserName = (TextView) navigationHeadView.findViewById(R.id.navigation_username);
        tvUserEmail = (TextView) navigationHeadView.findViewById(R.id.navigation_email);

        UserInfo userInfo = YouJoinApplication.getCurrUser();
//        Picasso.with(MainActivity.this)
//                .load(userInfo.getAvatarUrl())
//                .centerCrop()
//                .into(ivUserPhoto);
        tvUserName.setText(userInfo.getUsername());
        tvUserEmail.setText(userInfo.getEmail());

        ivUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.actionStart(MainActivity.this);
            }
        });

    }


    private void setupNavigationAction(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_item_friends:

                                break;
                            case R.id.navigation_item_messages:

                                break;
                            case R.id.navigation_item_tweets:
                                switchToTweets();
                                break;
                            case R.id.navigation_item_settings:

                                break;
                            case R.id.navigation_item_exit:
                                ActivityManager.finishAll();
                                break;

                            default:
                                break;
                        }
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }


    private void switchToTweets(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new TweetsFragment()).commit();
        toolbar.setTitle(getString(R.string.title_tweets));
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

}
