package com.cbat.cbat.ui.navigation;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;

import androidx.fragment.app.FragmentManager;

import com.cbat.cbat.R;
import com.cbat.cbat.ui.accounts.AccountsFragment;
import com.cbat.cbat.ui.home.HomeFragment;
import com.cbat.cbat.ui.more.MoreFragment;
import com.cbat.cbat.ui.notification.NotificationsFragment;
import com.cbat.cbat.ui.services.ServicesFragment;
import com.cbat.cbat.ui.widgets.DrawerActivity;
import com.cbat.cbat.ui.widgets.UIFragmentTabHost;
import com.cbat.cbat.ui.widgets.UITabView;
import com.cbat.cbat.util.GlobalClass;

import java.util.ArrayList;

public class MainActivity extends DrawerActivity  {



    /////////
    private FragmentManager fragmentManager;
    private UIFragmentTabHost fragmentTabHost;
    int selectedMainTab=-1;
    private ArrayList<TabItem> tabItemArrayList;

    private class TabItem {
        Class<?> tabClass;
        String tabName;
        int tabDrawable;
        int tabSelectDrawable;
    }

    ////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
         LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View contentView = inflater.inflate(R.layout.activity_main, null, false);
                drawer.addView(contentView, 0);
//if(getIntent().hasExtra("selectedMainTab")) {
//    selectedMainTab = getIntent().getIntExtra("selectedMainTab",0);
//}

        Log.d("selectedMainTab", String.valueOf(GlobalClass.selectedMainTab));
       if(GlobalClass.selectedMainTab==2){
           timeFilter.setVisibility(View.GONE);
           share.setVisibility(View.GONE);
       }else{
           timeFilter.setVisibility(View.VISIBLE);
           share.setVisibility(View.VISIBLE);
       }



        initializeTabHost();
    }


    private void initializeTabHost() {
        fragmentManager = this.getSupportFragmentManager();
       // fragmentManager.addOnBackStackChangedListener(backStackChangedListener);

        tabItemArrayList = new ArrayList<>();

        // Sports

        // Yoga video tutorial
        TabItem yogaTabItem = new TabItem();
        yogaTabItem.tabClass =MoreFragment.class;
        //yogaTabItem.tabClass = YogaMain.class;
        //yogaTabItem.tabClass = YogaMainFragment.class;
        yogaTabItem.tabName = getString(R.string.tab_more);
        yogaTabItem.tabDrawable = R.drawable.ic_tab_more_select;
        yogaTabItem.tabSelectDrawable = R.drawable.ic_tab_more_active;

        tabItemArrayList.add(yogaTabItem);

        // Profile
        TabItem profileTabItem = new TabItem();
        //profileTabItem.tabClass = ProfileFragment.class;
        profileTabItem.tabClass = NotificationsFragment.class;
        profileTabItem.tabName = getString(R.string.tab_notification);
        profileTabItem.tabDrawable = R.drawable.ic_tab_notification_select;
        profileTabItem.tabSelectDrawable = R.drawable.ic_tab_notifications_active;

        tabItemArrayList.add(profileTabItem);
        // Settings
        TabItem settingsTabItem = new TabItem();
        //settingsTabItem.tabClass = SettingsFragment.class;
        settingsTabItem.tabClass = AccountsFragment.class;
        settingsTabItem.tabName = getString(R.string.tab_accounts);
        settingsTabItem.tabDrawable = R.drawable.ic_tab_account_select;
        settingsTabItem.tabSelectDrawable = R.drawable.ic_tab_account_active;
        tabItemArrayList.add(settingsTabItem);
        // Alarms
        TabItem alarmsTabItem = new TabItem();
        //alarmsTabItem.tabClass = AlarmsFragment.class;
        alarmsTabItem.tabClass = ServicesFragment.class;
        alarmsTabItem.tabName = getString(R.string.tab_services);
        alarmsTabItem.tabDrawable = R.drawable.ic_tab_services_select;
        alarmsTabItem.tabSelectDrawable = R.drawable.ic_tab_services_active;

        tabItemArrayList.add(alarmsTabItem);

        TabItem sportTabItem = new TabItem();
        // sportTabItem.tabClass = SportsFragment.class;
        sportTabItem.tabClass = HomeFragment.class;
        sportTabItem.tabName = getString(R.string.tab_home);
        sportTabItem.tabDrawable = R.drawable.ic_tab_dashboard_select;
//        sportTabItem.tabDrawable = R.drawable.ic_tab_dashboard_unactive;
        sportTabItem.tabSelectDrawable = R.drawable.ic_tab_dashboard_active;

        tabItemArrayList.add(sportTabItem);

        // Yoga video tutorial
//        TabItem analyticTabItem = new TabItem();
//        // yogaTabItem.tabClass = YogaMain.class;
//        analyticTabItem.tabClass = HeartRateAnalyticTabFragment.class;
//        analyticTabItem.tabName = getString(R.string.main_tabname_Analytic);
//        analyticTabItem.tabDrawable = R.drawable.selector_tabbar_analytic;
//        analyticTabItem.tabSelectDrawable = R.drawable.selector_tabbar_select;
      //  tabItemArrayList.add(analyticTabItem);

        fragmentTabHost = findViewById(R.id.dw_tabhost);
        fragmentTabHost.setup(this, fragmentManager, R.id.layout_fragment_content);

        for (int i = 0, size = tabItemArrayList.size(); i < size; i++) {
            TabItem tabItem = tabItemArrayList.get(i);
            if (null != tabItem) {
//                if(tabItem.tabName.equals("Accounts")){
//                    timeFilter.setVisibility(View.GONE);
//                    share.setVisibility(View.GONE);
//                }else{
//                    timeFilter.setVisibility(View.VISIBLE);
//                    share.setVisibility(View.VISIBLE);
//
//                }
                TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(tabItem.tabName);
                tabSpec.setIndicator(UITabView.createTabView(LayoutInflater.from(this),tabItem
                        .tabSelectDrawable, tabItem.tabDrawable, tabItem.tabName));
                fragmentTabHost.addTab(tabSpec, tabItem.tabClass, null);
            }
        }
        if(GlobalClass.selectedMainTab>0) {

                Log.d("account Tab", "off nav");
                Log.d("account Tab", String.valueOf(GlobalClass.selectedMainTab));



            Log.d("account Tab1", String.valueOf(GlobalClass.selectedMainTab));

            fragmentTabHost.setCurrentTab(GlobalClass.selectedMainTab);
        }else{
            timeFilter.setVisibility(View.VISIBLE);
            share.setVisibility(View.VISIBLE);
            fragmentTabHost.setCurrentTab(tabItemArrayList.size() - 1);
        }
        fragmentTabHost.getTabWidget().setDividerDrawable(null);
    }

    public void setTabVisibility(int visibility) {

        fragmentTabHost.setVisibility(visibility);
    }


//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        mPackageName = getApplicationInfo().packageName;
//
//       // initBnbDefault();
//        initBnbDrawableMode();
//      //  initBnbMixMode();
//    }

//    private void initBnbDefault() {
//        mBnbDefaultList = new ArrayList<>();
//        mTvBnbDefault = (TextView) findViewById(R.id.tv_bnb_default);
//        BottomNavigationBar mBnbDefault = (BottomNavigationBar) findViewById(R.id.bnb_default);
//        mBnbDefault.addOnSelectedListener(new BottomNavigationBar.OnSelectedListener() {
//            @Override
//            public void OnSelected(int oldPosition, int newPosition) {
//                mTvBnbDefault.setText("Default Tint Mode : " + mBnbDefaultList.get(newPosition).getText());
//            }
//        });
//
//        BottomItem item1 = new BottomItem();
//        item1.setText("Bag");
//        item1.setIconResID(getResources().getIdentifier("ic_tint_bag", "drawable", mPackageName));
//        item1.setActiveBgResID(R.drawable.ic_home_orange_24dp);
//        item1.setInactiveBgResID(R.drawable.ic_home_black_24dp);
//        mBnbDefault.addItem(item1);
//        mBnbDefaultList.add(item1);
//
//        BottomItem item2 = new BottomItem();
//        item2.setText("Book");
//        item2.setIconResID(getResources().getIdentifier("ic_tint_book", "drawable", mPackageName));
//        item2.setActiveBgResID(R.drawable.ic_home_orange_24dp);
//        item2.setInactiveBgResID(R.drawable.ic_home_black_24dp);
//        mBnbDefault.addItem(item2);
//        mBnbDefaultList.add(item2);
//
//        BottomItem item3 = new BottomItem();
//        item3.setText("Cart");
//        item3.setIconResID(getResources().getIdentifier("ic_tint_cart", "drawable", mPackageName));
//        item3.setActiveBgResID(R.drawable.ic_home_orange_24dp);
//        item3.setInactiveBgResID(R.drawable.ic_home_black_24dp);
//        mBnbDefault.addItem(item3);
//        mBnbDefaultList.add(item3);
//
//        BottomItem item4 = new BottomItem();
//        item4.setText("List");
//        item4.setIconResID(getResources().getIdentifier("ic_tint_list", "drawable", mPackageName));
//        item4.setActiveBgResID(R.drawable.ic_home_orange_24dp);
//        item4.setInactiveBgResID(R.drawable.ic_home_black_24dp);
//        mBnbDefault.addItem(item4);
//        mBnbDefaultList.add(item4);
//
//        BottomItem item5 = new BottomItem();
//        item5.setText("Rocket");
//        item5.setIconResID(getResources().getIdentifier("ic_tint_rocket", "drawable", mPackageName));
//        item5.setActiveBgResID(R.drawable.ic_home_orange_24dp);
//        item5.setInactiveBgResID(R.drawable.ic_home_black_24dp);
//        mBnbDefault.addItem(item5);
//        mBnbDefaultList.add(item5);
//
//        mBnbDefault.setSelectedPosition(2); //Set default selected item
//        mTvBnbDefault.setText("Default Tint Mode : " + mBnbDefaultList.get(2).getText());
//        mBnbDefault.initialize();
//
//        mBnbDefault.setBadgeNumber(0, 68);
//        mBnbDefault.setBadgeNumber(1, -1);
//        mBnbDefault.setBadgeNumber(2, 6);
//        mBnbDefault.setBadgeNumber(3, 0);
//        mBnbDefault.setBadgeNumber(4, 10);
//    }

//    private void initBnbDrawableMode() {
//        mBnbDrawableModeList = new ArrayList<>();
//        mTvBnbDrawableMode = (TextView) findViewById(R.id.tv_bnb_drawable);
//        BottomNavigationBar mBnbDrawable = (BottomNavigationBar) findViewById(R.id.bnb_drawable);
//        mBnbDrawable.addOnSelectedListener(new BottomNavigationBar.OnSelectedListener() {
//            @Override
//            public void OnSelected(int oldPosition, int newPosition) {
//                mTvBnbDrawableMode.setText("Drawable Mode : " + mBnbDrawableModeList.get(newPosition).getText());
//            }
//        });
//        BottomItem item1 = new BottomItem();
//        item1.setMode(BottomItem.DRAWABLE_MODE);
//        item1.setText("Home");
//        item1.setActiveIconResID(getResources().getIdentifier("ic_tab_dashboard_active", "drawable",
//                getApplicationInfo().packageName));
//        item1.setInactiveIconResID(getResources().getIdentifier("ic_tab_dashboard_unactive", "drawable",
//                getApplicationInfo().packageName));
//        item1.setActiveTextColor(Color.parseColor("#E64B4E"));
//        mBnbDrawable.addItem(item1);
//        mBnbDrawableModeList.add(item1);
//
//        BottomItem item2 = new BottomItem();
//        item2.setMode(BottomItem.DRAWABLE_MODE);
//        item2.setText("Services");
//        item2.setActiveIconResID(getResources().getIdentifier("ic_tab_services_active", "drawable",
//                getApplicationInfo().packageName));
//        item2.setInactiveIconResID(getResources().getIdentifier("ic_tab_services_unactive", "drawable",
//                getApplicationInfo().packageName));
//        item2.setActiveTextColor(Color.parseColor("#E64B4E"));
//        mBnbDrawable.addItem(item2);
//        mBnbDrawableModeList.add(item2);
//
//        BottomItem item3 = new BottomItem();
//        item3.setMode(BottomItem.DRAWABLE_MODE);
//        item3.setText("Accounts");
//        item3.setActiveIconResID(getResources().getIdentifier("ic_tab_account_active", "drawable",
//                getApplicationInfo().packageName));
//        item3.setInactiveIconResID(getResources().getIdentifier("ic_tab_account_unactive", "drawable",
//                getApplicationInfo().packageName));
//        item3.setActiveTextColor(Color.parseColor("#E64B4E"));
//        mBnbDrawable.addItem(item3);
//        mBnbDrawableModeList.add(item3);
//
//        BottomItem item4 = new BottomItem();
//        item4.setMode(BottomItem.DRAWABLE_MODE);
//        item4.setText("Notification");
//        item4.setActiveIconResID(getResources().getIdentifier("ic_tab_notifications_active", "drawable",
//                getApplicationInfo().packageName));
//        item4.setInactiveIconResID(getResources().getIdentifier("ic_tab_notifications_unactive", "drawable",
//                getApplicationInfo().packageName));
//        item4.setActiveTextColor(Color.parseColor("#E64B4E"));
//        mBnbDrawable.addItem(item4);
//        mBnbDrawableModeList.add(item4);
//
//        BottomItem item5 = new BottomItem();
//        item5.setMode(BottomItem.DRAWABLE_MODE);
//        item5.setText("More");
//        item5.setActiveIconResID(getResources().getIdentifier("ic_tab_more_active", "drawable",
//                getApplicationInfo().packageName));
//        item5.setInactiveIconResID(getResources().getIdentifier("ic_tab_more_unactive", "drawable",
//                getApplicationInfo().packageName));
//        item5.setActiveTextColor(Color.parseColor("#E64B4E"));
//        mBnbDrawable.addItem(item5);
//        mBnbDrawableModeList.add(item5);
//
//        mBnbDrawable.setSelectedPosition(2); //Set default selected item
//        mTvBnbDrawableMode.setText("Drawable Mode : " + mBnbDrawableModeList.get(2).getText());
//        mBnbDrawable.initialize();
//
//        mBnbDrawable.setBadgeText(0, "WOW");
//        mBnbDrawable.setBadgeText(1, "");
//        mBnbDrawable.setBadgeText(2, "赞");
//        mBnbDrawable.setBadgeText(3, "");
//        mBnbDrawable.setBadgeText(4, "紧急");
//    }

//    private void initBnbMixMode() {
//        mBnbMixModeList = new ArrayList<>();
//        mTvBnbMixMode = (TextView) findViewById(R.id.tv_bnb_mix);
//        BottomNavigationBar mBnbMix = (BottomNavigationBar) findViewById(R.id.bnb_mix);
//        mBnbMix.addOnSelectedListener(new BottomNavigationBar.OnSelectedListener() {
//            @Override
//            public void OnSelected(int oldPosition, int newPosition) {
//                mTvBnbMixMode.setText("Mix : " + mBnbMixModeList.get(newPosition).getText());
//            }
//        });
//
//        BottomItem item1 = new BottomItem();
//        item1.setText("Bag");
//        item1.setIconResID(getResources().getIdentifier("ic_tint_bag", "drawable", mPackageName));
//        item1.setActiveIconColor(Color.parseColor("#E55D87"));
//        item1.setInactiveIconColor(Color.parseColor("#5FC3E4"));
//        item1.setActiveTextColor(Color.parseColor("#43CEA2"));
//        item1.setInactiveTextColor(Color.parseColor("#D38312"));
//        mBnbMix.addItem(item1);
//        mBnbMixModeList.add(item1);
//
//        BottomItem item2 = new BottomItem();
//        item2.setMode(BottomItem.DRAWABLE_MODE);
//        item2.setText("Friend");
//        item2.setActiveIconResID(getResources().getIdentifier("ic_drawable_friend_add_fill", "drawable",
//                getApplicationInfo().packageName));
//        item2.setInactiveIconResID(getResources().getIdentifier("ic_drawable_friend_add", "drawable",
//                getApplicationInfo().packageName));
//        item2.setActiveTextColor(Color.parseColor("#E64B4E"));
//        mBnbMix.addItem(item2);
//        mBnbMixModeList.add(item2);
//
//        BottomItem item3 = new BottomItem();
//        item3.setText("Cart");
//        item3.setIconResID(getResources().getIdentifier("ic_tint_cart", "drawable", mPackageName));
//        item3.setActiveIconColor(Color.parseColor("#E55D87"));
//        item3.setInactiveIconColor(Color.parseColor("#5FC3E4"));
//        item3.setActiveTextColor(Color.parseColor("#43CEA2"));
//        item3.setInactiveTextColor(Color.parseColor("#D38312"));
//        mBnbMix.addItem(item3);
//        mBnbMixModeList.add(item3);
//
//        BottomItem item4 = new BottomItem();
//        item4.setMode(BottomItem.DRAWABLE_MODE);
//        item4.setText("Recharge");
//        item4.setActiveIconResID(getResources().getIdentifier("ic_drawable_recharge_fill", "drawable",
//                getApplicationInfo().packageName));
//        item4.setInactiveIconResID(getResources().getIdentifier("ic_drawable_recharge", "drawable",
//                getApplicationInfo().packageName));
//        item4.setActiveTextColor(Color.parseColor("#E64B4E"));
//        mBnbMix.addItem(item4);
//        mBnbMixModeList.add(item4);
//
//        BottomItem item5 = new BottomItem();
//        item5.setText("Rocket");
//        item5.setIconResID(getResources().getIdentifier("ic_tint_rocket", "drawable", mPackageName));
//        item5.setActiveIconColor(Color.parseColor("#E55D87"));
//        item5.setInactiveIconColor(Color.parseColor("#5FC3E4"));
//        item5.setActiveTextColor(Color.parseColor("#43CEA2"));
//        item5.setInactiveTextColor(Color.parseColor("#D38312"));
//        mBnbMix.addItem(item5);
//        mBnbMixModeList.add(item5);
//
//        mBnbMix.setSelectedPosition(2); //Set default selected item
//        mTvBnbMixMode.setText("Mix : " + mBnbMixModeList.get(2).getText());
//        mBnbMix.initialize();
//    }
}
