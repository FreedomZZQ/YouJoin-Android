package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
//    @Bind(R.id.navigation)
//    NavigationView navigationView;
//    @Bind(R.id.drawer_layout)
//    DrawerLayout drawerLayout;

//    ImageView ivUserPhoto;
//    TextView tvUserName;
//    TextView tvUserEmail;

    public static final int DRAWER_FRIEND = 1;
    public static final int DRAWER_MSG = 2;
    public static final int DRAWER_TWEETS = 3;
    public static final int DRAWER_SETTING = 4;
    public static final int DRAWER_EXIT = 5;

    private AccountHeader drawerHeader = null;
    private Drawer drawer = null;
    private boolean opened = false;

    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yj_activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        initView(savedInstanceState);

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

    private void initView(Bundle savedInstanceState){
//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
//                this, drawerLayout, toolbar,
//                R.string.drawer_open, R.string.drawer_close){
//            public void onDrawerClosed(View view){
//                super.onDrawerClosed(view);
//            }
//
//            public void onDrawerOpened(View view){
//                super.onDrawerOpened(view);
//            }
//        };
//        actionBarDrawerToggle.syncState();
//        drawerLayout.setDrawerListener(actionBarDrawerToggle);
//        setupNavigationAction(navigationView);
//
//        View navigationHeadView = navigationView.getHeaderView(0);
//        if(navigationView == null) return;

//        ivUserPhoto = (ImageView) navigationHeadView.findViewById(R.id.navigation_avatar);
//        tvUserName = (TextView) navigationHeadView.findViewById(R.id.navigation_username);
//        tvUserEmail = (TextView) navigationHeadView.findViewById(R.id.navigation_email);

        UserInfo userInfo = YouJoinApplication.getCurrUser();

        final IProfile profile = new ProfileDrawerItem()
                .withName(userInfo.getNickname())
                .withEmail(userInfo.getEmail())
                .withIcon(R.mipmap.ic_account_circle_white_48dp)
                .withIdentifier(100);

        drawerHeader = new AccountHeaderBuilder()
                .withActivity(MainActivity.this)
                .withHeaderBackground(R.drawable.header2)
                .addProfiles(profile)
                .withSavedInstance(savedInstanceState)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(MainActivity.this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withAccountHeader(drawerHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(getString(R.string.navigation_friends))
                        .withIcon(R.drawable.ic_group_black_48dp).withIdentifier(DRAWER_FRIEND)
                        .withSelectable(true),
                        new PrimaryDrawerItem().withName(getString(R.string.navigation_msg))
                        .withIcon(R.drawable.ic_textsms_black_48dp).withIdentifier(DRAWER_MSG)
                        .withSelectable(true).withBadgeStyle(new BadgeStyle()
                                .withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)),
                        new PrimaryDrawerItem().withName(getString(R.string.navigation_tweets))
                        .withIcon(R.drawable.ic_home_black_48dp).withIdentifier(DRAWER_TWEETS)
                        .withSelectable(true),
                        new PrimaryDrawerItem().withName(getString(R.string.navigation_setting))
                        .withIcon(R.drawable.ic_settings_black_48dp).withIdentifier(DRAWER_SETTING)
                        .withSelectable(true),
                        new PrimaryDrawerItem().withName(getString(R.string.navigation_exit))
                        .withIcon(R.drawable.ic_assignment_return_black_48dp).withIdentifier(DRAWER_EXIT)
                        .withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem){
                        if(drawerItem != null){
                            switch(drawerItem.getIdentifier()){
                                case DRAWER_FRIEND:
                                    switchToFriends();
                                    break;
                                case DRAWER_MSG:
                                    switchToMessage();
                                    break;
                                case DRAWER_TWEETS:
                                    switchToTweets();
                                    break;
                                case DRAWER_SETTING:
                                    switchToSetting();
                                    break;
                                case DRAWER_EXIT:
                                    ActivityManager.finishAll();
                                    break;
                                default:
                                    break;
                            }
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();

        refreshUserInfo();
        drawer.updateBadge(DRAWER_MSG, new StringHolder(10 + ""));
//        ivUserPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UserInfoActivity.actionStart(MainActivity.this, UserInfoActivity.TYPE_CURR_USER,
//                        YouJoinApplication.getCurrUser().getId());
//            }
//        });

    }

    private void refreshUserInfo() {
        final UserInfo userInfo = YouJoinApplication.getCurrUser();



        if(userInfo.getImg_url() != null){
            Picasso.with(MainActivity.this)
                    .load(StringUtils.getPicUrlList(userInfo.getImg_url()).get(0))
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            IProfile profile = new ProfileDrawerItem()
                                    .withName(userInfo.getNickname())
                                    .withEmail(userInfo.getEmail())
                                    .withIcon(bitmap)
                                    .withIdentifier(100);
                            drawerHeader.updateProfile(profile);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        }else{
            IProfile profile = new ProfileDrawerItem()
                    .withName(userInfo.getNickname())
                    .withEmail(userInfo.getEmail())
                    .withIcon(R.mipmap.ic_account_circle_white_48dp)
                    .withIdentifier(100);
            drawerHeader.updateProfile(profile);
        }



//        if(userInfo.getImg_url() != null && !userInfo.getImg_url().equals("")){
//            Picasso.with(MainActivity.this)
//                    .load(StringUtils.getPicUrlList(userInfo.getImg_url()).get(0))
//                    .resize(200, 200)
//                    .centerCrop()
//                    .into(ivUserPhoto);
//        }
//        tvUserName.setText(userInfo.getUsername());
//        tvUserEmail.setText(userInfo.getEmail());
    }


//    private void setupNavigationAction(NavigationView navigationView){
//        navigationView.setNavigationItemSelectedListener(
//                new NavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(MenuItem menuItem) {
//                        switch (menuItem.getItemId()) {
//                            case R.id.navigation_item_friends:
//                                switchToFriends();
//                                break;
//                            case R.id.navigation_item_messages:
//                                switchToMessage();
//                                break;
//                            case R.id.navigation_item_tweets:
//                                switchToTweets();
//                                break;
//                            case R.id.navigation_item_settings:
//
//                                break;
//                            case R.id.navigation_item_exit:
//                                ActivityManager.finishAll();
//                                break;
//
//                            default:
//                                break;
//                        }
//                        menuItem.setChecked(true);
//                        drawerLayout.closeDrawers();
//                        return true;
//                    }
//                });
//    }

    private void switchToMessage() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new MessageFragment()).commit();
        toolbar.setTitle(getString(R.string.title_message));
        drawer.updateBadge(DRAWER_MSG, null);
    }

    private void switchToTweets(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new TweetsFragment()).commit();
        toolbar.setTitle(getString(R.string.title_tweets));
    }

    private void switchToFriends(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new FriendFragment()).commit();
        toolbar.setTitle(getString(R.string.title_friends));
    }

    private void switchToSetting(){
        SettingActivity.actionStart(MainActivity.this);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
//            if(drawerLayout.isDrawerOpen(navigationView)){
//                drawerLayout.closeDrawers();
//                return true;
//            }
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
