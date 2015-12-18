package me.zq.youjoin.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.R;

public class WelcomeActivity extends BaseActivity
        implements View.OnClickListener {

    @Bind(R.id.choose_signin_button)
    Button chooseSigninButton;
    @Bind(R.id.choose_signup_button)
    Button chooseSignupButton;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private List<View> list = new ArrayList<>();
    private ImageView[] img = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yj_activity_welcome);
        ButterKnife.bind(this);

        chooseSigninButton.setOnClickListener(this);
        chooseSignupButton.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        list.add(getLayoutInflater().inflate(R.layout.welcome_tab1, null));
        list.add(getLayoutInflater().inflate(R.layout.welcome_tab2, null));
        list.add(getLayoutInflater().inflate(R.layout.welcome_tab3, null));

        img = new ImageView[list.size()];
        LinearLayout layout = (LinearLayout) findViewById(R.id.viewGroup);
        for (int i = 0; i < list.size(); i++) {
            img[i] = new ImageView(WelcomeActivity.this);
            if (0 == i) {
                img[i].setBackgroundResource(R.drawable.ic_point_select);
            } else {
                img[i].setBackgroundResource(R.drawable.ic_point_normal);
            }
            img[i].setPadding(10, 10, 10, 10);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMarginStart(10);
            lp.setMarginEnd(10);
            img[i].setLayoutParams(lp);
            layout.addView(img[i]);
        }
        viewPager.setAdapter(new ViewPagerAdapter(list));
        viewPager.setOnPageChangeListener(new ViewPagerPageChangeListener());
    }

    class ViewPagerPageChangeListener implements ViewPager.OnPageChangeListener {

        /*
         * state：网上通常说法：1的时候表示正在滑动，2的时候表示滑动完毕了，0的时候表示什么都没做，就是停在那；
         * 我的认为：1是按下时，0是松开，2则是新的标签页的是否滑动了
         * (例如：当前页是第一页，如果你向右滑不会打印出2，如果向左滑直到看到了第二页，那么就会打印出2了)；
         * 个人认为一般情况下是不会重写这个方法的
         */
        @Override
        public void onPageScrollStateChanged(int state) {
        }

        /*
         * page：看名称就看得出，当前页； positionOffset：位置偏移量，范围[0,1]；
         * positionoffsetPixels：位置像素，范围[0,屏幕宽度)； 个人认为一般情况下是不会重写这个方法的
         */
        @Override
        public void onPageScrolled(int page, float positionOffset,
                                   int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int page) {
            //更新图标
            for (int i = 0; i < list.size(); i++) {
                if (page == i) {
                    img[i].setBackgroundResource(R.drawable.ic_point_select);
                } else {
                    img[i].setBackgroundResource(R.drawable.ic_point_normal);
                }
            }
        }
    }

    class ViewPagerAdapter extends PagerAdapter {

        private List<View> list = null;

        public ViewPagerAdapter(List<View> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    @Override
    public void onClick(View view) {
        boolean isSignIn = false;
        switch (view.getId()) {
            case R.id.choose_signin_button:
                isSignIn = true;
                break;
            case R.id.choose_signup_button:
                isSignIn = false;
                break;
            default:
                break;
        }

        SignInUpActivity.actionStart(this, isSignIn);
    }
}
