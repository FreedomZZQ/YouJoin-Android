package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.DataPresenter;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.event.ImTypeMessageEvent;
import me.zq.youjoin.fragment.AboutFragment;
import me.zq.youjoin.fragment.AroundFragment;
import me.zq.youjoin.fragment.FriendFragment;
import me.zq.youjoin.fragment.MessageFragment;
import me.zq.youjoin.fragment.PluginFragment;
import me.zq.youjoin.fragment.TweetsFragment;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.utils.StringUtils;

public class MainActivity extends BaseActivity
implements DataPresenter.GetUserInfo{

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    public static final int DRAWER_FRIEND = 1;
    public static final int DRAWER_MSG = 2;
    public static final int DRAWER_TWEETS = 3;
    public static final int DRAWER_ABOUT = 4;
    public static final int DRAWER_EXIT = 5;
    public static final int DRAWER_PLUGIN = 6;
    public static final int DRAWER_AROUND = 7;

    private AccountHeader drawerHeader = null;
    private Drawer drawer = null;

    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yj_activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        setSupportActionBar(toolbar);
        initView(savedInstanceState);

        DataPresenter.requestUserInfoById(YouJoinApplication.getCurrUser().getId(), MainActivity.this);

        switchToTweets();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        }

        //PullManager.startPullService(MainActivity.this, 5, PullService.class, PullService.ACTION);
        Log.d(TAG, "onCreate");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImTypeMessageEvent event){
        drawer.updateBadge(DRAWER_MSG, new StringHolder(1 + ""));
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //PullManager.stopPullService(MainActivity.this, PullService.class, PullService.ACTION);
    }

    @Override
    public void onGetUserInfo(UserInfo info){
        if(info.getResult().equals(NetworkManager.SUCCESS)){
            YouJoinApplication.setCurrUser(info);
            refreshUserInfo();
        }
    }

    private void initView(Bundle savedInstanceState){

        UserInfo userInfo = YouJoinApplication.getCurrUser();

        final IProfile profile = new ProfileDrawerItem()
                .withName(userInfo.getNickname())
                .withEmail(userInfo.getEmail())
                .withIcon(R.mipmap.ic_account_circle_white_48dp)
                .withIdentifier(100);

        drawerHeader = new AccountHeaderBuilder()
                .withActivity(MainActivity.this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(profile)
                .withDividerBelowHeader(false)
                .withPaddingBelowHeader(true)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        UserInfoActivity.actionStart(MainActivity.this, UserInfoActivity.TYPE_CURR_USER,
                        YouJoinApplication.getCurrUser().getId());
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(MainActivity.this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withAccountHeader(drawerHeader)
                .withHeaderDivider(false)
                .withHeaderPadding(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(getString(R.string.navigation_tweets))
                                .withIcon(R.drawable.ic_home_black_48dp).withIdentifier(DRAWER_TWEETS)
                                .withSelectable(true),
                        new PrimaryDrawerItem().withName(getString(R.string.navigation_msg))
                                .withIcon(R.drawable.ic_textsms_black_48dp).withIdentifier(DRAWER_MSG)
                                .withSelectable(true).withBadgeStyle(new BadgeStyle()
                                .withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)),
                        new PrimaryDrawerItem().withName(getString(R.string.navigation_friends))
                                .withIcon(R.drawable.ic_group_black_48dp).withIdentifier(DRAWER_FRIEND)
                                .withSelectable(true),
                        new PrimaryDrawerItem().withName(getString(R.string.navigation_around))
                                .withIcon(R.drawable.ic_location_on_black_48dp).withIdentifier(DRAWER_AROUND)
                                .withSelectable(true),
                        new PrimaryDrawerItem().withName(getString(R.string.navigation_plugin))
                                .withIcon(R.drawable.ic_apps_black_48dp).withIdentifier(DRAWER_PLUGIN)
                                .withSelectable(true),
                        new PrimaryDrawerItem().withName(getString(R.string.navigation_about))
                                .withIcon(R.drawable.ic_settings_black_48dp).withIdentifier(DRAWER_ABOUT)
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
                                case DRAWER_AROUND:
                                    switchToAround();
                                    break;
                                case DRAWER_PLUGIN:
                                    switchToPlugin();
                                    break;
                                case DRAWER_ABOUT:
                                    switchToAbout();
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
        // TODO: 2016/4/20  
        //drawer.updateBadge(DRAWER_MSG, new StringHolder(10 + ""));
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

    private void switchToMessage() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new MessageFragment()).commit();
        toolbar.setTitle(getString(R.string.title_message));
        drawer.updateBadge(DRAWER_MSG, null);
    }

    private void switchToAround(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new AroundFragment()).commit();
        toolbar.setTitle(getString(R.string.title_around));
    }

    private void switchToTweets(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new TweetsFragment()).commit();
        toolbar.setTitle(getString(R.string.title_tweets));
    }

    private void switchToFriends(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new FriendFragment()).commit();
        toolbar.setTitle(getString(R.string.title_friends));
    }

    private void switchToPlugin(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new PluginFragment()).commit();
        toolbar.setTitle(getString(R.string.title_plugin));
    }

    private void switchToAbout(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new AboutFragment()).commit();
        toolbar.setTitle(getString(R.string.title_about));
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(drawer != null && drawer.isDrawerOpen()){
                drawer.closeDrawer();
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
