package com.cbat.cbat.ui.navigation;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cbat.cbat.R;
import com.cbat.cbat.adapter.ListPopupWindowAdapter;
import com.cbat.cbat.model.ListPopupItem;
import com.cbat.cbat.ui.services.ManagmentFragment;
import com.cbat.cbat.ui.services.sales.details_sales.CustomerDetailFragment;
import com.cbat.cbat.util.Constants;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.Utility;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.itextpdf.text.DocumentException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
//import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.shockwave.pdfium.PdfDocument;

//import org.androidannotations.annotations.AfterViews;
//import org.androidannotations.annotations.EActivity;
//import org.androidannotations.annotations.NonConfigurationInstance;
//import org.androidannotations.annotations.OnActivityResult;
//import org.androidannotations.annotations.OptionsItem;
//import org.androidannotations.annotations.OptionsMenu;
//import org.androidannotations.annotations.ViewById;

import jp.gr.java_conf.androtaku.geomap.GeoMapView;
import jp.gr.java_conf.androtaku.geomap.OnInitializedListener;

public class CustomerDetailActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {

    public static String TAG = "CustomerDetailActivity";
    String backFrgment;
    String currentFrgment;
    String title;
    String currentFrgmentMain;
    String stateName;
    Integer pageNumber = 0;
    PDFView pdfView;
    String pdfFileName;
    private ProgressDialog progressDialog;
    Map<String, Double> cutomerSalesMap;
    Map<String, JSONArray> customerFullList;

    private ImageView share;
    public Toolbar toolbar;
    public RelativeLayout imageLayout;
    public RelativeLayout navigationLayout;

    public ImageView productLogo;
    public TextView navigationTitle;
    public TextView navigationCurrent;
    public TextView navigationHome;
    // public TextView navigationCurrentMain;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    public ImageView navigatonShare;
    public ImageView navigatonTimeFilter;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    ////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        customerFullList = new HashMap<>();
        // setContentView(R.layout.activity_main);
//        LayoutInflater inflater = (LayoutInflater) this
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View contentView = inflater.inflate(R.layout.details_app_bar, null, false);
        // drawer.addView(contentView, 0);
//        toolbar.setTitle("");
        //super.onCreate(savedInstanceState);
        setContentView(R.layout.cutomer_detail);

        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.details_toolbar);

        //setting the title
        //toolbar.setTitle("My Toolbar");


        navigationCurrent = (TextView) findViewById(R.id.navigationCurrent);
        //  navigationHome = (TextView) findViewById(R.id.navigationHome);
        navigationTitle = (TextView) findViewById(R.id.navigationTitle);
        // navigationCurrentMain = (TextView) findViewById(R.id.navigationCurrentMain);
        navigationHome = (TextView) findViewById(R.id.navigationHome);
        navigationTitle.setText(GlobalClass.title);
        // toolbar.setTitle(title);
        navigationHome.setText(GlobalClass.backFrgment);
        navigationCurrent.setText(GlobalClass.currentFrgment);
        Bundle fregmentInfo = getIntent().getExtras();
        navigatonShare = (ImageView) findViewById(R.id.navigatonShare);
        navigatonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),
                        "Share your screen as pdf or Image first part",
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
//        if (fregmentInfo != null) {
//            backFrgment = fregmentInfo.getString("BackFrgment");
//            currentFrgment = fregmentInfo.getString("CurrentFrgment");
//            currentFrgmentMain = fregmentInfo.getString("CustomerList");
//            stateName=fregmentInfo.getString("StateName");
//            title = fregmentInfo.getString("Title");
//             navigationTitle.setText(GlobalClass.title);
//           // toolbar.setTitle(title);
//            navigationHome.setText(GlobalClass.backFrgment);
//            navigationCurrent.setText(GlobalClass.currentFrgment);
//         //   navigationCurrentMain.setText(currentFrgmentMain);
//           // productLogo.setVisibility(View.GONE);
//        } else if (fregmentInfo == null) {
//            Toast.makeText(this, "Bundle is null", Toast.LENGTH_SHORT).show();
//        }
//        Fragment cutomerFragment=new CustomerDetailFragment();
//        cutomerFragment.setMap()
//        Bundle bundle = new Bundle();
//        bundle.putString("stateName", stateName);
//        cutomerFragment.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction()
//                .replace(R.id.layout_service_fragment_content1, cutomerFragment)
//                .commit();

        navigationHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), ServicesDetailActivity.class);
                Bundle bundle = new Bundle();

                if (GlobalClass.backFrgment.equalsIgnoreCase("Product Wise Sales")) {
                    GlobalClass.selectedTabPosition = "1";
                }else{
                    GlobalClass.selectedTabPosition = "0";
                }
                GlobalClass.backFrgment = "Services";
                GlobalClass.currentFrgment = "Sales";
                myIntent.putExtras(bundle);
                startActivity(myIntent);
            }
        });
        //if (backFrgment.equalsIgnoreCase("TotalSale")) {
        navigationCurrent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                List<ListPopupItem> listPopupItems = new ArrayList<>();
                if (GlobalClass.backFrgment.equalsIgnoreCase("Product Wise Sales")) {
                    for (String product : GlobalClass.productsList) {
                        listPopupItems.add(new ListPopupItem(product, R.mipmap.ic_launcher));
                    }
                } else {
                    for (String state : GlobalClass.salesState) {
                        listPopupItems.add(new ListPopupItem(state, R.mipmap.ic_launcher));
                    }
                }
//                    listPopupItems.add(new ListPopupItem("Uttarkhand", R.mipmap.ic_launcher));
//                    listPopupItems.add(new ListPopupItem("UttarPradesh", R.mipmap.ic_launcher));
//                    listPopupItems.add(new ListPopupItem("Rajeshthan", R.mipmap.ic_launcher));
//                    listPopupItems.add(new ListPopupItem("Maharastra", R.mipmap.ic_launcher));
                //Creating the instance of PopupMenu
                showListServicePopupWindow(v, listPopupItems);
            }
        });
        // }
        if (GlobalClass.backFrgment.equalsIgnoreCase("Product Wise Sales") || GlobalClass.backFrgment.equalsIgnoreCase("Total Sales")) {
           // if (GlobalClass.customerFullList == null || GlobalClass.customerFullList.isEmpty()) {
                getTotalSaleCutomerWise();
           // }
        }

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
               String fileName= pdfCreator.createPdf();
                try {
                    Log.d("File Name >>>>",fileName);
                    File docsFolder = new File(Environment.getExternalStorageDirectory() + "/CBatDownload");
                    File pdfFile = new File(docsFolder.getAbsolutePath(), fileName);
                    Uri uri=Uri.fromFile(pdfFile);
                    View view = getLayoutInflater().inflate(R.layout.pdf_view_detail, null, false);
                    Button closeBtn = (Button) view.findViewById(R.id.closeWin);
                    TextView nameText = (TextView) view.findViewById(R.id.nameText);
                    pdfView=(PDFView) view.findViewById(R.id.pdfView);
                   // nameText.setText(item.data[column + 1]);
                    TextView filePath = (TextView) view.findViewById(R.id.filePath);
                    filePath.setText( pdfFile.getAbsolutePath());
                    Button deleteFile = (Button) view.findViewById(R.id.deleteFile);
//                    pdfView.fromAsset(pdfFile.getAbsolutePath())
//                            .defaultPage(pageNumber)
//                            .enableSwipe(true)
//                            .swipeHorizontal(false)
//                            .onPageChange(this)
//                            .enableAnnotationRendering(true)
//                            .onLoad(this)
//                            .scrollHandle(new DefaultScrollHandle(this))
//                            .load();
                    pdfView.fromUri(uri).load();
                    PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                    closeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Finish the registration screen and return to the Login activity
                            popupWindow.dismiss();
                            //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }
                    });
                    deleteFile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialog myQuittingDialogBox = new AlertDialog.Builder(v.getContext(),R.style.MyDialogTheme)
                                    .setTitle("Title")
                                    .setMessage("Message")
                                    // set message, title, and icon
                                    .setTitle("Delete File")
                                    .setMessage("Do you want to Delete : " +uri.getPath())
                                    //.setIcon(R.drawable.)

                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            //your deleting code

                                            File fdelete = new File(uri.getPath());
                                            if (fdelete.exists()) {
                                                if (fdelete.delete()) {
                                                    Log.d("file Deleted :" , uri.getPath());
                                                    Snackbar.make(view, "File Deleted : "+fileName, Snackbar.LENGTH_SHORT).show();

                                                } else {
                                                    Log.d("file not Deleted :" ,uri.getPath());
                                                }

                                            }

                                            dialog.dismiss();
                                            popupWindow.dismiss();
                                        }
                                         ;

                                    })
                                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();

                                        }
                                    })
                                    .create();

                             myQuittingDialogBox.show();
                            // Finish the registration screen and return to the Login activity

                            //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }
                    });

//                    Intent intent = new Intent(Intent.ACTION_VIEW,
//                            Uri.parse(fileName));
//                    intent.setType("application/pdf");
//                    PackageManager pm = getPackageManager();
//                    List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
//                    if (activities.size() > 0) {
//                        startActivity(intent);
//                    } else {
//                        // Do something else here. Maybe pop up a Dialog or Toast
//                    }
                    //displayFromAsset(pdfFileName);
                }catch (Exception e){
                    e.printStackTrace();
                }
                //File file = new File(fileName);
//                Uri path = Uri.fromFile(file);
//                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
//                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                pdfOpenintent.setDataAndType(path, "application/pdf");
//                try {
//                    startActivity(pdfOpenintent);
//                }
//                catch (ActivityNotFoundException e) {
//
//                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CODE_ASK_PERMISSIONS:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission Granted
//                    try {
//                        createPdfWrapper();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (DocumentException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    // Permission Denied
//                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT)
//                            .show();
//                }
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
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
                Intent myIntent = new Intent(getBaseContext(), CustomerDetailActivity.class);
              //  GlobalClass.customerFullList.clear();
                if (GlobalClass.backFrgment.equalsIgnoreCase("Product Wise Sales")) {
                    GlobalClass.productName = adapter.getTvTitle().getText().toString();
                    // GlobalClass.backFrgment="TotalSale";
                    GlobalClass.backFrgment="Product Wise Sales";
                    GlobalClass.currentFrgment = adapter.getTvTitle().getText().toString();
                     GlobalClass.currentFrgmentMain = adapter.getTvTitle().getText().toString();
                    GlobalClass.title = "Customer Wise List of " + adapter.getTvTitle().getText().toString();

                } else {

                    String StateNameT = Utility.getStateName(adapter.getTvTitle().getText().toString());
                    GlobalClass.stateName = StateNameT;
                     GlobalClass.backFrgment="Total Sales";
                    GlobalClass.currentFrgment = adapter.getTvTitle().getText().toString();
                    GlobalClass.currentFrgmentMain = "CustomerList";
                    GlobalClass.title = "Customer Wise Total Cost in " + adapter.getTvTitle().getText().toString();
                }
                startActivity(myIntent);

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
        FragmentManager fm = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        //fragmentTransaction.add(R.id.layout_service_fragment_content1, fragment);
        fragmentTransaction.replace(R.id.layout_service_fragment_content1, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void getTotalSaleCutomerWise() {
        //svgLoader.startAnimation();
        if (progressDialog != null) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
        }
        String url;

        if (GlobalClass.backFrgment.equalsIgnoreCase("Product Wise Sales")) {
            if(GlobalClass.month!=null && !GlobalClass.month.isEmpty()) {
                url = Constants.BASE_LOCAL__URL + "getCustomerListByProductAndDateAndMonth/sales/" + GlobalClass.productName + "/customer/"+GlobalClass.startDate+"/"+GlobalClass.endDate+ "/" + GlobalClass.month;
            }else{
                url = Constants.BASE_LOCAL__URL + "getCustomerListByProductAndDate/sales/" + GlobalClass.productName + "/customer/"+GlobalClass.startDate+"/"+GlobalClass.endDate;

            }
           // url = Constants.BASE_LOCAL__URL + "getCustomerListByProductAndDate/sales/" + GlobalClass.productName + "/customer/"+GlobalClass.startDate+"/"+GlobalClass.endDate;
            //getCustomerListByProductAndDateAndMonth
        } else {
            url = Constants.BASE_LOCAL__URL + "getListStateWiseByDate/sales/" + GlobalClass.stateName + "/customer/"+GlobalClass.startDate+"/"+GlobalClass.endDate;


        }
        JSONObject paramsJsonObj = new JSONObject();
        try {

            paramsJsonObj.put("id", GlobalClass.comapnyId);


            Log.d("URL :- ", url);
            Log.d("paramsJsonObj :- ", paramsJsonObj.toString());
            JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, url, paramsJsonObj, this.createRequestSuccessListener(0), this.createRequestErrorListener(System.currentTimeMillis()));
            jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT_TIME,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Log.d("No resul :- ", paramsJsonObj.toString());
            Volley.newRequestQueue(getBaseContext()).add(jsonObjRequest);
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Adding request to request queue

        // Cancelling request
//        AppController.getInstance().getRequestQueue().cancelAll(TAG);
    }


    private Response.Listener<JSONObject> createRequestSuccessListener(final int responseCode) {
        Response.Listener<JSONObject> listenerObj = null;
        //    if (requestCode == REQUEST_LOGIN) {

        listenerObj = new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (responseCode == 0) {
                        Log.d("Res", response.toString());
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        if (response.getString("code").equals(Integer.toString(206))) {

                            // GlobalClass.lastName = response.optString("userrole");
                            //Save to prefrence
                            JSONArray contentList = response.getJSONArray("contentList");
                            cutomerSalesMap = new HashMap<>();
                            GlobalClass.customerFullList = new HashMap<>();

                            JSONObject customerMap = contentList.getJSONObject(0);
                            Iterator<String> keys = customerMap.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                try {
                                    JSONArray value = customerMap.getJSONArray(key);
                                    Log.d(TAG, "key " + key);
                                    GlobalClass.customerFullList.put(key, value);
                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                            }

                            for (Map.Entry<String, JSONArray> set : GlobalClass.customerFullList.entrySet()) {
                                Log.d("Calling1", "Calling1");
                                // for(Map.Entry<String, JSONObject> element:GlobalClass.totalSales.entrySet()){
                                try {
                                    if (!set.getKey().equalsIgnoreCase("totalStateSales")) {
                                        JSONArray dataMonthly = set.getValue().getJSONArray(1);
                                        JSONArray dataYearly = set.getValue().getJSONArray(3);
                                        JSONArray contriData = set.getValue().getJSONArray(4);
                                        JSONArray info = set.getValue().getJSONArray(5);
                                        //  JSONArray dataCustomer =  set.getValue().getJSONArray(5);
                                        //[] dataTable = new String[dataMonthly.length() + 4];
                                        JSONObject elementYearly;
                                        boolean yearFlag = true;
                                        boolean contriFlag = true;
                                        float totalSales = 0;
                                        int j = 0;

                                        for (int i = 0; i < dataMonthly.length(); i++) {
                                            if (yearFlag) {
                                                elementYearly = dataYearly.getJSONObject(i);
                                                yearFlag = false;
                                                int yVal = elementYearly.getInt("value");
                                      //          time=elementYearly.getString("name");
                                                BigDecimal number1 = new BigDecimal(yVal);
                                                //totalSales = number1.floatValue() / 10000000;
                                                totalSales = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                                Log.d(TAG, "set.getKey() >" + set.getKey());
                                            //    Log.d(TAG, "totalSales 123>" + totalSales+" "+time);
                                                GlobalClass.CustomerName=set.getKey();
                                                Log.d("Calling1", "Calling2");
                                         //       value.add((double) totalSales);
                                                int val= (int) totalSales;
                                           //     total= total+val;
                                         //       Log.d(TAG, "totalSales 123>" + totalSales+" "+time+" "+total+" "+val);
                                            }
                                            if (contriFlag) {
                                                elementYearly = contriData.getJSONObject(i);
                                                contriFlag = false;
                                                double yVal = elementYearly.getDouble("value");
                                                BigDecimal number1 = new BigDecimal(yVal);
                                                //   totalSales = number1.floatValue() / 10000000;
                                                totalSales = number1.floatValue(); // GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                                Log.d(TAG, "set.getKey() >" + set.getKey());
                                                Log.d(TAG, "yVal >" + yVal);
                                             //   contri.add(yVal);
                                                // dataTable[j++] = String.valueOf(set.getKey());

                                            }

                                            JSONObject element = dataMonthly.getJSONObject(i);
                                            int yVal = element.getInt("value");
                                            BigDecimal number1 = new BigDecimal(yVal);
                                            // float debitFloat = number1.floatValue() / 10000000;
                                            float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);


                                        }

                                        for(int k=0;k<info.length();k++){
                                            Log.d("state1@1", info.toString());
                                            JSONObject element = info.getJSONObject(k);
                                            JSONObject obj=element.getJSONObject("customer");
                                            GlobalClass.CustomerState=obj.getString("state");
                                            GlobalClass.CustomerPincode=obj.getString("pincode");


                                        }
//                        dataTable[j++]=dataCustomer.getJSONObject(0).getJSONObject("customer").getString("address");
//                        items.add(new NexusWithImage(type, dataTable));
//                        Log.d(TAG, "dataTable >" + dataTable.toString());
//                        Log.d(TAG, "dataTable length >" + dataTable.length);
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            JSONArray totalStateSales = contentList.getJSONArray(1);
                            GlobalClass.customerFullList.put("totalStateSales", totalStateSales);
//                            for (int i = 0; i < contentList.length(); i++) {
//                                JSONObject element = (JSONObject) contentList.get(i);
//                                cutomerSalesMap.put(element.getString("name"), (Math.abs(element.getDouble("value") / 10000000)));
//                            }
                            Log.d("Globalclass12", GlobalClass.CustomerName+""+GlobalClass.CustomerPincode+" "+GlobalClass.CustomerState);
                            Fragment cutomerFragment = new CustomerDetailFragment();
                            //((CustomerDetailFragment) cutomerFragment).setMap(cutomerSalesMap);
                            //((CustomerDetailFragment) cutomerFragment).setFullMap(customerFullList);
                            // GlobalClass.customerFullList=customerFullList;
                            //Bundle bundle = new Bundle();
                            // bundle.putString("stateName", stateName);
                            //  GlobalClass.stateName=stateName;
                            // cutomerFragment.setArguments(bundle);
                            GlobalClass.sortingOn = -1;
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.layout_service_fragment_content1, cutomerFragment)
                                    .commit();

                            // sliderPagerAdapter.notifyDataSetChanged();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        };

        return listenerObj;
    }

    private Response.ErrorListener createRequestErrorListener(final long mRequestStartTime) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Log.d("Error :- ", error.toString());

                    if (error instanceof NetworkError) {
                        Toast.makeText(getBaseContext(), "Network Error.", Toast.LENGTH_LONG).show();

                    } else if (error instanceof ServerError) {
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(getBaseContext(), "Authentication fail.", Toast.LENGTH_LONG).show();

                    } else if (error instanceof ParseError) {
                        Toast.makeText(getBaseContext(), "We are not able to process login request due to technical issue.", Toast.LENGTH_LONG).show();

                    } else if (error instanceof NoConnectionError) {
                        Toast.makeText(getBaseContext(), "No connection available to connect with Server.", Toast.LENGTH_LONG).show();

                    } else if (error instanceof TimeoutError) {

                        long totalRequestTime = System.currentTimeMillis() - mRequestStartTime;
                        final String timeString =
                                new SimpleDateFormat("HH:mm:ss").format(mRequestStartTime);
                        Log.d("Err", error.toString());
                        Log.d("error.getClass :", error.getClass().toString());
                        Log.d("StartTime", timeString);
                        Log.d("EndTime", Utility.getCurrentTimeStamp());
                        Log.d("error total Time :", String.valueOf(totalRequestTime));
                        StringBuffer strBuff = new StringBuffer();
                        strBuff.append("The request timed out");
                        // strBuff.append(GlobalClass.userId);
                        strBuff.append(Utility.getCurrentTimeStamp());
                        Toast.makeText(getBaseContext(), strBuff.toString(), Toast.LENGTH_LONG).show();


                    }
                    //  onRequestFailed();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
    }
   // @OnActivityResult(REQUEST_CODE)
    public void onResult(int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            //uri = intent.getData();
            //displayFromUri(uri);
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

//    public String getFileName(Uri uri) {
//        String result = null;
//        if (uri.getScheme().equals("content")) {
//            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//            try {
//                if (cursor != null && cursor.moveToFirst()) {
//                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                }
//            } finally {
//                if (cursor != null) {
//                    cursor.close();
//                }
//            }
//        }
//        if (result == null) {
//            result = uri.getLastPathSegment();
//        }
//        return result;
//    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
//        Log.e(TAG, "title = " + meta.getTitle());
//        Log.e(TAG, "author = " + meta.getAuthor());
//        Log.e(TAG, "subject = " + meta.getSubject());
//        Log.e(TAG, "keywords = " + meta.getKeywords());
//        Log.e(TAG, "creator = " + meta.getCreator());
//        Log.e(TAG, "producer = " + meta.getProducer());
//        Log.e(TAG, "creationDate = " + meta.getCreationDate());
//        Log.e(TAG, "modDate = " + meta.getModDate());

                //(pdfView.getTableOfContents(), "-");

    }
    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;

        pdfView.fromAsset(pdfFileName)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                //.pageFitPolicy(FitPolicy.BOTH)
                .load();
    }
    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    /**
     * Listener for response to user permission request
     *
     * @param requestCode  Check that permission request code matches
     * @param permissions  Permissions that requested
     * @param grantResults Whether permissions granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[],
                                            int[] grantResults) {
//        if (requestCode == PERMISSION_CODE) {
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                launchPicker();
//            }
//        }
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.e(TAG, "Cannot load page " + page);
    }

}
