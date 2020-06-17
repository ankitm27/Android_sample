package com.cbat.cbat.ui.navigation;

import android.content.Intent;
import android.os.Build;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cbat.cbat.R;
import com.cbat.cbat.adapter.ListPopupWindowAdapter;
import com.cbat.cbat.adapter.ViewPagerAdapter;
import com.cbat.cbat.model.ListPopupItem;
import com.cbat.cbat.ui.services.InventoryFragment;
import com.cbat.cbat.ui.services.ManagmentFragment;
import com.cbat.cbat.ui.services.PayableFragment;
import com.cbat.cbat.ui.services.recievable.ProductWiseRecivableFragment;
import com.cbat.cbat.ui.services.recievable.RecievableFragment;
import com.cbat.cbat.ui.services.recievable.RecivableAgingFragment;
import com.cbat.cbat.ui.services.recievable.TotalRecivableFragment;
import com.cbat.cbat.ui.services.recievable.details_receivable.RecivableTop5Fragment_R;
import com.cbat.cbat.ui.services.sales.CollectionTrendFragment;
import com.cbat.cbat.ui.services.sales.CreditUtilReportFragment;
import com.cbat.cbat.ui.services.sales.OpenOrderFragment;
import com.cbat.cbat.ui.services.sales.ProductWiseSalesFragment;
import com.cbat.cbat.ui.services.sales.SalesTrendFragment;
import com.cbat.cbat.ui.services.sales.SalesVsAchievmentFragment;
import com.cbat.cbat.ui.services.sales.TotalSalesFragment;
import com.cbat.cbat.ui.widgets.DrawerActivity;
import com.cbat.cbat.util.GlobalClass;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class ServicesDetailActivity extends DrawerActivity implements SlyCalendarDialog.Callback {


    private static final String TAG = "ServicesDetailActivity";
    String backFrgment;
    String currentFrgment;
    String title;
    String selectedTabPosition;

    ////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.services_detail, null, false);
        drawer.addView(contentView, 0);
        toolbar.setTitle("");
        //   tabLayout = (TabLayout) findViewById(R.id.tabs);

        navigationLayout.setVisibility(View.VISIBLE);
        imageLayout.setVisibility(View.GONE);
        tabLayout.setVisibility(View.VISIBLE);

        Bundle fregmentInfo = getIntent().getExtras();
        // if (fregmentInfo != null) {
        //  backFrgment = fregmentInfo.getString("BackFrgment");
        //  currentFrgment = fregmentInfo.getString("CurrentFrgment");
        // title = fregmentInfo.getString("Title");
        // selectedTabPosition = fregmentInfo.getString("selectedTabPosition");
        if (selectedTabPosition != null && !selectedTabPosition.isEmpty()) {
            Log.d("selectedTabPosition", selectedTabPosition);
        }

        // navigationTitle.setText(title);
        toolbar.setTitle(GlobalClass.title);
        navigationBack.setText(GlobalClass.backFrgment);
        navigationCurrentMain.setText(GlobalClass.currentFrgment);
        productLogo.setVisibility(View.GONE);

        navigatonTimeFilter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                List<ListPopupItem> listPopupItems = new ArrayList<>();
                listPopupItems.add(new ListPopupItem("MTD", R.mipmap.ic_launcher));
                listPopupItems.add(new ListPopupItem("QTD", R.mipmap.ic_launcher));
                listPopupItems.add(new ListPopupItem("YTD", R.mipmap.ic_launcher));
                listPopupItems.add(new ListPopupItem("LY", R.mipmap.ic_launcher));
                listPopupItems.add(new ListPopupItem("Custom", R.mipmap.ic_launcher));

                //Creating the instance of PopupMenu
                showTimeFilterListServicePopupWindow(v, listPopupItems);
            }
        });
        navigationHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                // intent.putExtra("selectedMainTab", 3);
                GlobalClass.selectedMainTab = 4;
                startActivity(intent);
            }
        });//closing the setOnClickListener method
        if (GlobalClass.currentFrgment.equalsIgnoreCase("Sales")) {
            GlobalClass.apiKey = "sales";
            navigationBack.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    List<ListPopupItem> listPopupItems = new ArrayList<>();
                    listPopupItems.add(new ListPopupItem("Payable", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("Receivable", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("Inventrory", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("GST", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("Managment", R.mipmap.ic_launcher));
                    //Creating the instance of PopupMenu
                    showListServicePopupWindow(v, listPopupItems);
                }
            });

            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            //adapter.addFragment(new HomeFragment(), "Overview");
            adapter.addFragment(new TotalSalesFragment(), getResources().getString(R.string.total_sales));
            adapter.addFragment(new ProductWiseSalesFragment(), getResources().getString(R.string.product_wise_sales));
            adapter.addFragment(new SalesTrendFragment(), getResources().getString(R.string.sales_trend));
            adapter.addFragment(new CollectionTrendFragment(), getResources().getString(R.string.collection_trend));
            adapter.addFragment(new SalesVsAchievmentFragment(), getResources().getString(R.string.target_sales_vs_achievment));
          adapter.addFragment(new OpenOrderFragment(), getResources().getString(R.string.open_order));

            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);

            tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    Log.d("Tab Name", tab.getText().toString());
                    GlobalClass.backFrgment = tab.getText().toString();
                    GlobalClass.pdfDataTab=tab.getText().toString();
                    if(GlobalClass.backFrgment.equalsIgnoreCase(getResources().getString(R.string.target_sales_vs_achievment)))
                    {
                        navigatonShare.setVisibility(View.GONE);
                    }else{
                        navigatonShare.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

            if (GlobalClass.selectedTabPosition != null && !GlobalClass.selectedTabPosition.isEmpty()) {
                Log.d("selected inside", GlobalClass.selectedTabPosition);
                GlobalClass.mainNavigation = tabLayout.getTabAt(Integer.parseInt(GlobalClass.selectedTabPosition)).getText().toString();
                tabLayout.getTabAt(Integer.parseInt(GlobalClass.selectedTabPosition)).getText().toString();
                tabLayout.getTabAt(Integer.parseInt(GlobalClass.selectedTabPosition)).select();
                GlobalClass.pdfDataTab = tabLayout.getTabAt(Integer.parseInt(GlobalClass.selectedTabPosition)).getText().toString();

                // SalesFragment fragment = new SalesFragment();
                // loadFragment(fragment);
            }else{
                GlobalClass.mainNavigation = tabLayout.getTabAt(0).getText().toString();
                GlobalClass.pdfDataTab=tabLayout.getTabAt(0).getText().toString();
            }

        }
        else if (GlobalClass.currentFrgment.equalsIgnoreCase("Payable")) {
            navigationBack.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    List<ListPopupItem> listPopupItems = new ArrayList<>();
                    listPopupItems.add(new ListPopupItem("Sales", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("Receivable", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("Inventrory", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("GST", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("Managment", R.mipmap.ic_launcher));

                    //Creating the instance of PopupMenu
                    showListServicePopupWindow(v, listPopupItems);
                }
            });//closing the setOnClickListener method
            tabLayout.addTab(tabLayout.newTab().setText("Overview"));
            tabLayout.addTab(tabLayout.newTab().setText("Total Payables"));
            tabLayout.addTab(tabLayout.newTab().setText("Payables Aging Report"));
            tabLayout.addTab(tabLayout.newTab().setText("Overdue Report"));
            tabLayout.addTab(tabLayout.newTab().setText("Top 5"));


            PayableFragment fragment = new PayableFragment();
            loadFragment(fragment);

        }
        else if (GlobalClass.currentFrgment.equalsIgnoreCase("Receivable")) {
           // GlobalClass.apiKey = "credit note";
            GlobalClass.apiKey="Receipt";
            navigationBack.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    List<ListPopupItem> listPopupItems = new ArrayList<>();
                    listPopupItems.add(new ListPopupItem("Sales", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("Payable", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("Inventrory", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("GST", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("Managment", R.mipmap.ic_launcher));

                    //Creating the instance of PopupMenu
                    showListServicePopupWindow(v, listPopupItems);
                }
            });//closing the setOnClickListener method
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new RecievableFragment(), getResources().getString(R.string.overview));
            adapter.addFragment(new TotalRecivableFragment(), getResources().getString(R.string.total_receivable));
           // adapter.addFragment(new ProductWiseRecivableFragment(), getResources().getString(R.string.product_wise_receivable));
            // adapter.addFragment(new RecivableTrendFragment(), "Receivable Trend");
            adapter.addFragment(new RecivableAgingFragment(), getResources().getString(R.string.receivable_aging));
            adapter.addFragment(new RecivableTop5Fragment_R(), getResources().getString(R.string.top_5));
            //GlobalClass.selectedTabPosition="0";
//            adapter.addFragment(new ProductWiseSalesFragment(), "Target Sales Vs Achievment");
//            adapter.addFragment(new ProductWiseSalesFragment(), "Credit Utilization Report");
//            adapter.addFragment(new ProductWiseSalesFragment(), "Total Collection Report");
//            adapter.addFragment(new ProductWiseSalesFragment(), "Sales vs Collection Report");
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);

            tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    Log.d("Tab Name", tab.getText().toString());
                    GlobalClass.backFrgment = tab.getText().toString();
                    GlobalClass.pdfDataTab=tab.getText().toString();
                    if(GlobalClass.backFrgment.equalsIgnoreCase(getResources().getString(R.string.top_5))){
                        navigatonShare.setVisibility(View.GONE);
                    }else{
                        navigatonShare.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

            if (GlobalClass.selectedTabPosition != null && !GlobalClass.selectedTabPosition.isEmpty()) {
                Log.d("selected inside", GlobalClass.selectedTabPosition);
                GlobalClass.mainNavigation = tabLayout.getTabAt(Integer.parseInt(GlobalClass.selectedTabPosition)).getText().toString();
                tabLayout.getTabAt(Integer.parseInt(GlobalClass.selectedTabPosition)).getText().toString();
                tabLayout.getTabAt(Integer.parseInt(GlobalClass.selectedTabPosition)).select();
                GlobalClass.pdfDataTab = tabLayout.getTabAt(Integer.parseInt(GlobalClass.selectedTabPosition)).getText().toString();


            }
           // RecievableFragment fragment = new RecievableFragment();
           // loadFragment(fragment);

        }
        else if (GlobalClass.currentFrgment.equalsIgnoreCase("Inventory")) {
            navigationBack.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    List<ListPopupItem> listPopupItems = new ArrayList<>();
                    listPopupItems.add(new ListPopupItem("Sales", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("Payable", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("Recievable", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("GST", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("Managment", R.mipmap.ic_launcher));

                    //Creating the instance of PopupMenu
                    showListServicePopupWindow(v, listPopupItems);
                }
            });//closing the setOnClickListener method

            InventoryFragment fragment = new InventoryFragment();
            loadFragment(fragment);

        }
        else if (GlobalClass.currentFrgment.equalsIgnoreCase("Managment")) {
            navigationBack.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    List<ListPopupItem> listPopupItems = new ArrayList<>();
                    listPopupItems.add(new ListPopupItem("Sales", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("Payable", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("Recievable", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("GST", R.mipmap.ic_launcher));
                    listPopupItems.add(new ListPopupItem("Inventory", R.mipmap.ic_launcher));

                    //Creating the instance of PopupMenu
                    showListServicePopupWindow(v, listPopupItems);
                }
            });//closing the setOnClickListener method

            ManagmentFragment fragment = new ManagmentFragment();
            loadFragment(fragment);

        }


    }

    private void showListServicePopupWindow(View anchor, List<ListPopupItem> listPopup) {
        List<ListPopupItem> listPopupItems = new ArrayList<>();
        listPopupItems = listPopup;

        //listPopupItems.add(new ListPopupItem("Crore", R.mipmap.ic_launcher));

        final ListPopupWindow listPopupWindow =
                createListPopupWindow(anchor, 200, listPopupItems);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();
                // float amount = 100000;
                // NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

                // String moneyString = formatter.format(amount);
                //  totalSalesVal.setText(moneyString);
                ListPopupWindowAdapter.ViewHolder adapter = (ListPopupWindowAdapter.ViewHolder) view.getTag();
                Toast.makeText(getApplicationContext(), "clicked at " + adapter.getTvTitle().getText(), Toast.LENGTH_SHORT)
                        .show();
                if (adapter.getTvTitle().getText().equals("Payable")) {
                    Intent myIntent = new Intent(getApplicationContext(), ServicesDetailActivity.class);
//                     Bundle bundle = new Bundle();
//                     bundle.putString("BackFrgment", "Services");
//                     bundle.putString("CurrentFrgment", "Payable");
//                     bundle.putString("Title", "Payable");
//                     myIntent.putExtras(bundle);
                    GlobalClass.backFrgment = "Services";
                    GlobalClass.currentFrgment = "Payable";
                    GlobalClass.title = "Payable";
                    startActivity(myIntent);

                } else if (adapter.getTvTitle().getText().equals("Sales")) {
                    Intent myIntent = new Intent(getApplicationContext(), ServicesDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("BackFrgment", "Services");
//                    bundle.putString("CurrentFrgment", "Sales");
//                    bundle.putString("Title", "Sales");
//                    myIntent.putExtras(bundle);
                    GlobalClass.backFrgment = "Services";
                    GlobalClass.currentFrgment = "Sales";
                    GlobalClass.title = "Sales";
                    startActivity(myIntent);

                } else if (adapter.getTvTitle().getText().equals("Receivable")) {
                    Intent myIntent = new Intent(getApplicationContext(), ServicesDetailActivity.class);
//                     Bundle bundle = new Bundle();
//                     bundle.putString("BackFrgment", "Services");
//                     bundle.putString("CurrentFrgment", "Recievable");
//                     bundle.putString("Title", "Recievable");
//                     myIntent.putExtras(bundle);
                    GlobalClass.backFrgment = "Services";
                    GlobalClass.currentFrgment = "Receivable";
                    GlobalClass.title = "Receivable";
                    startActivity(myIntent);

                } else if (adapter.getTvTitle().getText().equals("Inventory")) {
                    Intent myIntent = new Intent(getApplicationContext(), ServicesDetailActivity.class);
//                     Bundle bundle = new Bundle();
//                     bundle.putString("BackFrgment", "Services");
//                     bundle.putString("CurrentFrgment", "Inventory");
//                     bundle.putString("Title", "Inventory");
//                     myIntent.putExtras(bundle);
                    GlobalClass.backFrgment = "Services";
                    GlobalClass.currentFrgment = "Inventory";
                    GlobalClass.title = "Inventory";
                    startActivity(myIntent);

                } else if (adapter.getTvTitle().getText().equals("Managment")) {
                    Intent myIntent = new Intent(getApplicationContext(), ServicesDetailActivity.class);
//                     Bundle bundle = new Bundle();
//                     bundle.putString("BackFrgment", "Services");
//                     bundle.putString("CurrentFrgment", "Managment");
//                     bundle.putString("Title", "Managment");
//                     myIntent.putExtras(bundle);
                    GlobalClass.backFrgment = "Services";
                    GlobalClass.currentFrgment = "Managment";
                    GlobalClass.title = "Managment";
                    startActivity(myIntent);

                }
            }
        });
        listPopupWindow.show();
    }

    private void showTimeFilterListServicePopupWindow(View anchor, List<ListPopupItem> listPopup) {
        List<ListPopupItem> listPopupItems = new ArrayList<>();
        listPopupItems = listPopup;

        //listPopupItems.add(new ListPopupItem("Crore", R.mipmap.ic_launcher));
//        listPopupItems.add(new ListPopupItem("MTD", R.mipmap.ic_launcher));
//        listPopupItems.add(new ListPopupItem("QTD", R.mipmap.ic_launcher));
//        listPopupItems.add(new ListPopupItem("YTD", R.mipmap.ic_launcher));
//        listPopupItems.add(new ListPopupItem("LY", R.mipmap.ic_launcher));
//        listPopupItems.add(new ListPopupItem("Custom", R.mipmap.ic_launcher));
        final ListPopupWindow listPopupWindow =
                createListPopupWindow(anchor, 200, listPopupItems);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();
                // float amount = 100000;
                // NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

                // String moneyString = formatter.format(amount);
                //  totalSalesVal.setText(moneyString);
                ListPopupWindowAdapter.ViewHolder adapter = (ListPopupWindowAdapter.ViewHolder) view.getTag();
                Toast.makeText(getApplicationContext(), "clicked at " + adapter.getTvTitle().getText(), Toast.LENGTH_SHORT)
                        .show();
                if (adapter.getTvTitle().getText().equals("MTD")) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    GlobalClass.startDate = format.format(new Date());
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MONTH, -1);
                    System.out.println(format.format(cal.getTime()));
                    GlobalClass.endDate = format.format(cal.getTime());
                    Log.d(TAG, "GlobalClass.startDate " + GlobalClass.startDate);
                    Log.d(TAG, "GlobalClass.endDate " + GlobalClass.endDate);
                    Intent myIntent = new Intent(getBaseContext(), ServicesDetailActivity.class);
                    GlobalClass.backFrgment="Services";

                    startActivity(myIntent);
                } else if (adapter.getTvTitle().getText().equals("QTD")) {
                    LocalDate localDate = LocalDate.now();
                    LocalDate firstDayOfQuarter = localDate.with(localDate.getMonth().firstMonthOfQuarter())
                            .with(TemporalAdjusters.firstDayOfMonth());

                    LocalDate lastDayOfQuarter = firstDayOfQuarter.plusMonths(2)
                            .with(TemporalAdjusters.lastDayOfMonth());

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                    System.out.println("firstDayOfQuarter " + firstDayOfQuarter);
                    System.out.println("lastDayOfQuarter " + lastDayOfQuarter);

                    GlobalClass.startDate = firstDayOfQuarter.toString();
                    //GlobalClass.endDate=lastDayOfQuarter.toString();
                    GlobalClass.endDate = localDate.toString();

                    Log.d(TAG, "GlobalClass.startDate " + GlobalClass.startDate);
                    Log.d(TAG, "GlobalClass.endDate " + GlobalClass.endDate);
                    Intent myIntent = new Intent(getBaseContext(), ServicesDetailActivity.class);
                    GlobalClass.backFrgment="Services";

                    startActivity(myIntent);

                } else if (adapter.getTvTitle().getText().equals("YTD")) {
                    LocalDate localDate = LocalDate.now();
//                    LocalDate firstDayOfQuarter = localDate.with(localDate.getMonth().firstMonthOfQuarter())
//                            .with(TemporalAdjusters.firstDayOfMonth());
//
//                    LocalDate lastDayOfQuarter = firstDayOfQuarter.plusMonths(2)
//                            .with(TemporalAdjusters.lastDayOfMonth());

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

//                    System.out.println("firstDayOfQuarter "+firstDayOfQuarter);
//                    System.out.println("lastDayOfQuarter "+ lastDayOfQuarter);

                    GlobalClass.startDate = localDate.getYear() + "-04-01";//firstDayOfQuarter.toString();
                    GlobalClass.endDate = localDate.toString();

                    Log.d(TAG, "GlobalClass.startDate " + GlobalClass.startDate);
                    Log.d(TAG, "GlobalClass.endDate " + GlobalClass.endDate);
                    Intent myIntent = new Intent(getBaseContext(), ServicesDetailActivity.class);
                    GlobalClass.backFrgment="Services";

                    startActivity(myIntent);

                } else if (adapter.getTvTitle().getText().equals("LY")) {
                    LocalDate localDate = LocalDate.now();
                    LocalDate firstDayOfQuarter = localDate.with(localDate.getMonth().firstMonthOfQuarter())
                            .with(TemporalAdjusters.firstDayOfMonth());
//
//                    LocalDate lastDayOfQuarter = firstDayOfQuarter.plusMonths(2)
//                            .with(TemporalAdjusters.lastDayOfMonth());

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

//                    System.out.println("firstDayOfQuarter "+firstDayOfQuarter);
//                    System.out.println("lastDayOfQuarter "+ lastDayOfQuarter);

                    GlobalClass.startDate = (localDate.getYear() - 1) + "-04-01";//firstDayOfQuarter.toString();
                    GlobalClass.endDate = (localDate.getYear()) + "-03-31";

                    Log.d(TAG, "GlobalClass.startDate " + GlobalClass.startDate);
                    Log.d(TAG, "GlobalClass.endDate " + GlobalClass.endDate);
                    Intent myIntent = new Intent(getBaseContext(), ServicesDetailActivity.class);
                    GlobalClass.backFrgment="Services";
                    //GlobalClass.currentFrgment="Receivable";
                   // GlobalClass.title="Receivable";
                    //GlobalClass.selectedTabPosition="0";
                    startActivity(myIntent);

                } else if (adapter.getTvTitle().getText().equals("Custom")) {

                    new SlyCalendarDialog()
                            .setSingle(false)
                            .setHeaderColor(getColor(R.color.colorPrimary))
                            .setSelectedColor(getColor(R.color.chart5))
                            .setFirstMonday(false)
                            .setCallback(ServicesDetailActivity.this)
                            .show(getSupportFragmentManager(), "TAG_CUSTOM_CALENDAR");

//                    Intent myIntent = new Intent(getApplicationContext(), ServicesDetailActivity.class);
////                     Bundle bundle = new Bundle();
////                     bundle.putString("BackFrgment", "Services");
////                     bundle.putString("CurrentFrgment", "Managment");
////                     bundle.putString("Title", "Managment");
////                     myIntent.putExtras(bundle);
//                    GlobalClass.backFrgment = "Services";
//                    //GlobalClass.currentFrgment = "Managment";
//                    //GlobalClass.title = "Managment";
//                    startActivity(myIntent);

                }
            }
        });
        listPopupWindow.show();
    }


    private ListPopupWindow createListPopupWindow(View anchor, int width,
                                                  List<ListPopupItem> items) {
        final ListPopupWindow popup = new ListPopupWindow(getBaseContext());
        ListAdapter adapter = new ListPopupWindowAdapter(items);
        popup.setAnchorView(anchor);
        popup.setWidth(width);
        popup.setAdapter(adapter);
        return popup;
    }

    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        // Log.d(TAG,"loadFragment >> "+fragment.getTag());
        Log.d(TAG, "loadFragment >> " + fragment.getClass());
        FragmentManager fm = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.layout_service_fragment_content, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public void onCancelled() {
        //Nothing
    }

    @Override
    public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {
        if (firstDate != null) {
            if (secondDate == null) {
                firstDate.set(Calendar.HOUR_OF_DAY, hours);
                firstDate.set(Calendar.MINUTE, minutes);
//                Toast.makeText(
//                        this,
//                        new SimpleDateFormat(getString(R.string.timeFormat), Locale.getDefault()).format(firstDate.getTime()),
//                        Toast.LENGTH_LONG
//
//                ).show();

                Toast.makeText(
                        this,
                        "Select Start and End Date.",
                        Toast.LENGTH_LONG

                ).show();
            } else {
                GlobalClass.startDate =new SimpleDateFormat(getString(R.string.dateFormat), Locale.getDefault()).format(firstDate.getTime());//firstDayOfQuarter.toString();
                GlobalClass.endDate = new SimpleDateFormat(getString(R.string.dateFormat), Locale.getDefault()).format(secondDate.getTime());
//                Toast.makeText(
//                        this,
//                        getString(
//                                R.string.period,
//                                new SimpleDateFormat(getString(R.string.dateFormat), Locale.getDefault()).format(firstDate.getTime()),
//                                new SimpleDateFormat(getString(R.string.dateFormat), Locale.getDefault()).format(secondDate.getTime())
//                        ),
//                        Toast.LENGTH_LONG
//
//                ).show();

                Toast.makeText(
                        this,
                        getString(
                                R.string.period,
                                new SimpleDateFormat(getString(R.string.dateFormat), Locale.getDefault()).format(firstDate.getTime()),
                                new SimpleDateFormat(getString(R.string.dateFormat), Locale.getDefault()).format(secondDate.getTime())
                        ),
                        Toast.LENGTH_LONG

                ).show();
                Intent myIntent = new Intent(getBaseContext(), ServicesDetailActivity.class);
                GlobalClass.backFrgment="Services";
                startActivity(myIntent);
            }
        }
    }

}
