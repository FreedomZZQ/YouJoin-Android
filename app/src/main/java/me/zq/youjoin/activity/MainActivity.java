package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.R;

public class MainActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler_list)
    RecyclerView recyclerList;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.navigation)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yj_activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                PublishActivity.actionStart(MainActivity.this);
            }
        });

        initDrawer();


    }

    private void initDrawer(){
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close){
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);

                fab.show();
            }

            public void onDrawerOpened(View view){
                super.onDrawerOpened(view);
                fab.hide();
            }
        };
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        setupNavigationAction(navigationView);
    }


    private void setupNavigationAction(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
               new NavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem){
                       switch (menuItem.getItemId()){

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

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

}
