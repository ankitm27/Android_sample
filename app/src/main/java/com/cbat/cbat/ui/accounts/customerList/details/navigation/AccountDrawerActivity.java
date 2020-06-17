package com.cbat.cbat.ui.accounts.customerList.details.navigation;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.cbat.cbat.R;
import com.cbat.cbat.adapter.DrawerAdapter;
import com.cbat.cbat.adapter.ListPopupWindowAdapter;
import com.cbat.cbat.model.DrawerModel;
import com.cbat.cbat.model.ListPopupItem;
import com.cbat.cbat.ui.home.HomeFragment;
import com.cbat.cbat.ui.login.ChangePassActivity;
import com.cbat.cbat.ui.login.LoginActivity;
import com.cbat.cbat.ui.login.ProfileActivity;
import com.cbat.cbat.ui.navigation.MainActivity;
import com.cbat.cbat.ui.navigation.PdfCreatorActivity;
import com.cbat.cbat.ui.navigation.ServicesDetailActivity;
import com.cbat.cbat.ui.widgets.sidedrawer.SettingActivity;
import com.cbat.cbat.util.GlobalClass;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class AccountDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SlyCalendarDialog.Callback {
    private static final int REQUEST = 0;
    private static final String TAG ="AccountDrawerActivity" ;
    protected DrawerLayout drawer;
    private static final int REQUEST_SIGNUP = 0;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    public ImageView share;
    public ImageView timeFilter;
    public Toolbar toolbar;
    public RelativeLayout imageLayout;
    public RelativeLayout navigationLayout;
    public RelativeLayout screenLayout;
    public TextView screenTitle;

    public ImageView productLogo;
    public TextView navigationTitle;
    public TextView headerTitle;

    public TextView navigationBack;
    public TextView navigationHome;
    public TextView navigationCurrentMain;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    public ImageView navigatonShare;
    public ImageView navigatonTimeFilter;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;


    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_drawer);
        toolbar = (Toolbar) findViewById(R.id.accounttoolbar);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        productLogo = (ImageView) findViewById(R.id.productLogo);
        //this.setTitle(GlobalClass.firstName);
        imageLayout = (RelativeLayout) findViewById(R.id.imageLayout);
        navigationLayout = (RelativeLayout) findViewById(R.id.navigationLayout);
        navigationBack = (TextView) findViewById(R.id.navigationCurrent);
        //  navigationHome = (TextView) findViewById(R.id.navigationHome);
        // navigationTitle=(TextView)findViewById(R.id.navigationTitle);
        navigationCurrentMain = (TextView) findViewById(R.id.navigationCurrentMain);
        navigationHome = (TextView) findViewById(R.id.navigationHome);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.frame_container);
        headerTitle= (TextView) findViewById(R.id.headerTitle);
        navigatonShare = (ImageView) findViewById(R.id.navigatonShare);
        navigatonTimeFilter = (ImageView) findViewById(R.id.navigatonTimeFilter);
        timeFilter = (ImageView) findViewById(R.id.calFilter);

        screenLayout = (RelativeLayout) findViewById(R.id.screen);
        screenTitle = (TextView) findViewById(R.id.screenTitle);


        navigatonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),
                        "Share your screen as pdf or Image",
                        Toast.LENGTH_LONG).show();
                try {
                    createPdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

            }
        });
        timeFilter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("cal","calender");
                List<ListPopupItem> listPopupItems = new ArrayList<>();
                listPopupItems.add(new ListPopupItem("MTD", R.mipmap.ic_launcher));
                listPopupItems.add(new ListPopupItem("QTD", R.mipmap.ic_launcher));
                listPopupItems.add(new ListPopupItem("YTD", R.mipmap.ic_launcher));
                listPopupItems.add(new ListPopupItem("LY", R.mipmap.ic_launcher));
                listPopupItems.add(new ListPopupItem("Custom", R.mipmap.ic_launcher));

                //Creating the instance of PopupMenu
                Log.d("cal","calender");
                showTimeFilterListServicePopupWindow(v, listPopupItems);
            }
        });

        share = (ImageView) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),
                        "Share >>>>>> your screen as pdf or Image",
                        Toast.LENGTH_LONG).show();
            }
        });
        TextView userNameHeader = (TextView) findViewById(R.id.textUserName);
//        if (userNameHeader != null) {
//            userNameHeader.setText(GlobalClass.firstName);
//        }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if (item != null && item.getItemId() == android.R.id.home) {
                    if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                        drawer.closeDrawer(Gravity.RIGHT);
                    } else {
                        drawer.openDrawer(Gravity.LEFT);
                    }
                }
                return false;
            }
        };
        drawer.addDrawerListener(toggle);
        drawer.getRootView().findViewById(R.id.textUserName);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        ListView lst = (ListView) navigationView.findViewById(R.id.lst_menu_items);
        DrawerAdapter adapter = new DrawerAdapter(this, generateData());

        // if extending Activity 2. Get ListView from activity_main.xml
        //ListView listView = (ListView) findViewById(R.id.listview);

        // 3. setListAdapter
        lst.setAdapter(adapter);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getApplicationContext(), "Selected: " + position+" Id : "+id,Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Selected: " + position + " Id : " + id);
                Log.d(TAG, "Logout: " + position + " Id : " +  R.id.nav_logout);

                if (id == 0) {

                    Log.d(TAG,"Log Out..Thanks You");
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                }else if (id == 1) {

                    Log.d(TAG,"Log Out..Thanks You");
                    Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(intent);
                }else if (id == 2) {

                    Log.d(TAG,"Log Out..Thanks You");
                    Intent intent = new Intent(getApplicationContext(), ChangePassActivity.class);
                    startActivity(intent);
                }else if (id == 3) {

                    Log.d(TAG,"Log Out..Thanks You");
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    Log.d(TAG, "Selected: " + position + " Id : " + id);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        //if extending Activity
        //setListAdapter(adapter);

//        if(GlobalClass.userRole!=null && !GlobalClass.userRole.isEmpty() && GlobalClass.userRole.equalsIgnoreCase("admin")) {
//            navigationView.getMenu().clear();
//            navigationView.inflateMenu(R.menu.activity_drawer_drawer);
//        } else{
//            if(GlobalClass.userRole!=null && !GlobalClass.userRole.isEmpty() && GlobalClass.userRole.equalsIgnoreCase("guest")) {
//                navigationView.getMenu().clear();
//                navigationView.inflateMenu(R.menu.activity_guest_drawer);
//            }else {
//                navigationView.getMenu().clear();
//                navigationView.inflateMenu(R.menu.activity_user_drawer);
//            }
//        }
        // navigationView.getMenu().getItem(0).setChecked(true);
        //navigationView.callOnClick();
//        if (savedInstanceState == null && !navigationView.getMenu().hasVisibleItems()) {
//            this.onNavigationItemSelected(navigationView.getMenu().getItem(0));
//            // navigationView.getMenu().performIdentifierAction(R.id.nav_formCreation, 0);
//        }
        // View header = navigationView.getHeaderView(0);
//        TextView text = (TextView) header.findViewById(R.id.textUserName);
        //text.setText(GlobalClass.firstName);
        //TextView textRole = (TextView) header.findViewById(R.id.textRole);
        //textRole.setText(GlobalClass.userRole);

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {
//           // mClss = clss;
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
//        }


    }

    private void createPdfWrapper() throws FileNotFoundException,DocumentException{

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }else {
            PdfCreatorActivity pdfCreator= new PdfCreatorActivity(getApplicationContext());
            try {
                pdfCreator.createPdf();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private ArrayList<DrawerModel> generateData() {
        ArrayList<DrawerModel> models = new ArrayList<DrawerModel>();
        // models.add(new DrawerModel("Group Title"));
        models.add(new DrawerModel(R.drawable.ic_nev_profile, "Profile", "1"));
        models.add(new DrawerModel(R.drawable.ic_nev_setting, "Setting", "2"));
        models.add(new DrawerModel(R.drawable.ic_nev_password, "Password", "12"));
        models.add(new DrawerModel(R.drawable.ic_nev_logout, "Logout", "12"));


        return models;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.closeDrawer(GravityCompat.START);
            // super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d(TAG,String.valueOf(id));
        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_logout) {

            Log.d(TAG,"Log Out..Thanks You");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_formCreation) {

            // Intent intent = new Intent(getApplicationContext(), CollapsingDemoActivity.class);
            // startActivity(intent);
            Intent intent = new Intent(getApplicationContext(), HomeFragment.class);
            startActivity(intent);

        }
//        else if (id == R.id.nav_userCreation) {
//            Intent intent = new Intent(getApplicationContext(), UserCreationActivity.class);
//            startActivity(intent);
//        }
//        else if (id == R.id.nav_machineCreation) {
//            launchActivity(SimpleScannerActivity.class,2);
//
//        }

//        else if (id == R.id.nav_zoneCreation) {
//            Intent intent = new Intent(getApplicationContext(), ZoneCreationActivity.class);
//            startActivity(intent);
//
//        }
//        else if (id == R.id.nav_machineCreation1) {
//            Intent intent = new Intent(getApplicationContext(), MachineCreationActivity .class);
//            startActivity(intent);
//
//        }

//        else if (id == R.id.nav_zoneFormMap) {
//            Intent intent = new Intent(getApplicationContext(), ZoneFormMapActivity.class);
//            startActivity(intent);
//
//        }


//        else if (id == R.id.nav_gps) {
//                Intent intent = new Intent(getApplicationContext(), GPSACtivity.class);
//                startActivity(intent);
//
//    }
//        else if (id == R.id.nav_machine) {
//            Intent intent = new Intent(getApplicationContext(), MachineListActivity.class);
//            startActivity(intent);
//
//        }

        if (id == R.id.nav_logout) {

            Log.d(TAG,"Log Out..Thanks You");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        //else if (id == R.id.nav_userZoneMap) {
//            Intent intent = new Intent(getApplicationContext(), UserZoneMapActivity.class);
//
//            startActivity(intent);
//
//        }


        //  nav_zoneFormList
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }

    public void launchActivity(Class<?> clss, int formId, String formNameVar, int formUserId, String QR_VALUE, int activityTime, String qrBypass) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent iBus = new Intent(this, clss);

            iBus.putExtra("formId", formId);
            iBus.putExtra("formName", formNameVar);
            iBus.putExtra("formUserId", formUserId);
            iBus.putExtra("type", 0);
            iBus.putExtra("activityTime", activityTime);
            iBus.putExtra("qrBypass", qrBypass);
            iBus.putExtra("QR_VALUE", QR_VALUE);

            startActivity(iBus);
        }
    }

    public void launchActivity(Class<?> clss, String api, String data, int formId, String formNameVar, int formUserId, String QR_VALUE, int activityTime, String qrBypass) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent iBus = new Intent(this, clss);

            iBus.putExtra("api", api);
            iBus.putExtra("data", data);
            iBus.putExtra("type", 1);
            iBus.putExtra("formId", formId);
            iBus.putExtra("formName", formNameVar);
            iBus.putExtra("formUserId", formUserId);
            iBus.putExtra("activityTime", activityTime);
            iBus.putExtra("qrBypass", qrBypass);
            iBus.putExtra("QR_VALUE", QR_VALUE);
            startActivity(iBus);
        }
    }

    public void launchActivity(Class<?> clss, int type) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent iBus = new Intent(this, clss);
            iBus.putExtra("type", type);
            startActivity(iBus);
        }
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
                    Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
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
                    Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
                    //GlobalClass.backFrgment="Services";

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
                    Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
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
                            .setCallback(AccountDrawerActivity.this)
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
                Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
                // GlobalClass.backFrgment="Services";
                startActivity(myIntent);
            }
        }
    }
}

