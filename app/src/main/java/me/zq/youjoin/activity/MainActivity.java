package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.DataPresenter;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.fragment.FriendFragment;
import me.zq.youjoin.fragment.MessageFragment;
import me.zq.youjoin.fragment.TweetsFragment;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.pullrequest.PullManager;
import me.zq.youjoin.pullrequest.PullService;
import me.zq.youjoin.utils.StringUtils;

public class MainActivity extends BaseActivity
implements DataPresenter.GetUserInfo{

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.navigation)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    ImageView ivUserPhoto;
    TextView tvUserName;
    TextView tvUserEmail;

    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yj_activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        initView();

        DataPresenter.requestUserInfoById(YouJoinApplication.getCurrUser().getId(), MainActivity.this);

        switchToTweets();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        }

        PullManager.startPullService(MainActivity.this, 5, PullService.class, PullService.ACTION);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        PullManager.stopPullService(MainActivity.this, PullService.class, PullService.ACTION);
    }

    @Override
    public void onGetUserInfo(UserInfo info){
        if(info.getResult().equals(NetworkManager.SUCCESS)){
            YouJoinApplication.setCurrUser(info);
            refreshUserInfo();
        }
    }

    private void initView(){
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close){
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View view){
                super.onDrawerOpened(view);
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

        refreshUserInfo();

        ivUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.actionStart(MainActivity.this, UserInfoActivity.TYPE_CURR_USER,
                        YouJoinApplication.getCurrUser().getId());
            }
        });

    }

    private void refreshUserInfo() {
        UserInfo userInfo = YouJoinApplication.getCurrUser();

        if(userInfo.getImg_url() != null && !userInfo.getImg_url().equals("")){
            Picasso.with(MainActivity.this)
                    .load(StringUtils.getPicUrlList(userInfo.getImg_url()).get(0))
                    .resize(200, 200)
                    .centerCrop()
                    .into(ivUserPhoto);
        }
        tvUserName.setText(userInfo.getUsername());
        tvUserEmail.setText(userInfo.getEmail());
    }


    private void setupNavigationAction(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_item_friends:
                                switchToFriends();
                                break;
                            case R.id.navigation_item_messages:
                                switchToMessage();
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

    private void switchToMessage() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new MessageFragment()).commit();
        toolbar.setTitle(getString(R.string.title_message));
    }

    private void switchToTweets(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new TweetsFragment()).commit();
        toolbar.setTitle(getString(R.string.title_tweets));
    }

    private void switchToFriends(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new FriendFragment()).commit();
        toolbar.setTitle(getString(R.string.title_friends));
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(drawerLayout.isDrawerOpen(navigationView)){
                drawerLayout.closeDrawers();
                return true;
            }
            if((System.currentTimeMillis() - exitTime) > 2000){

                Toast.makeText(MainActivity.this, getString(R.string.click_again_to_exit),
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else{
                ActivityManager.finishAll();
            }
            return true;


        }
        return super.onKeyDown(keyCode, event);
    }

}
