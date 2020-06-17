package com.cbat.cbat.ui.navigation;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
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
import com.cbat.cbat.ui.services.sales.details_sales.CustomerDetailFragment;
import com.cbat.cbat.ui.services.sales.details_sales.InvoiceCollectionDetailFragment;
import com.cbat.cbat.ui.services.sales.details_sales.InvoiceDetailFragment;
import com.cbat.cbat.util.Constants;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.Utility;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.itextpdf.text.DocumentException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InvoiceDetailActivity extends AppCompatActivity {

    PDFView pdfView;
    String backFrgment;
    String currentFrgment;
    String title,stateName,cutomerName;
    String currentFrgmentMain;
    String navigatonTital;
    private ImageView share;
    public Toolbar toolbar;
    public RelativeLayout imageLayout;
    public RelativeLayout navigationLayout;

    public ImageView productLogo,arrow2;
    public TextView navigationTitle;
    public TextView navigationCurrent;
    public TextView navigationHome;
    public TextView navigationCurrentMain;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    public ImageView navigatonShare;
    public ImageView navigatonTimeFilter;
    private ProgressDialog progressDialog;
    List<JSONObject> customerInvoiceFullList;
    JSONArray chartData = new JSONArray();
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;

    ////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        // setContentView(R.layout.activity_main);
//        LayoutInflater inflater = (LayoutInflater) this
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View contentView = inflater.inflate(R.layout.details_app_bar, null, false);
       // drawer.addView(contentView, 0);
//        toolbar.setTitle("");
        //super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_detail);
        customerInvoiceFullList=new ArrayList<>();
        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.invoice_details_toolbar);

        //setting the title
        //toolbar.setTitle("My Toolbar");
Log.d("third", "gate invoice 1");

        navigationCurrent = (TextView) findViewById(R.id.navigationInvoiceCurrent);
        //  navigationHome = (TextView) findViewById(R.id.navigationHome);
         navigationTitle=(TextView)findViewById(R.id.navigationInvoiceTitle);
        navigationCurrentMain = (TextView) findViewById(R.id.navigationInvoiceCurrentMain);
        navigationHome = (TextView) findViewById(R.id.navigationInvoiceHome);
        arrow2=(ImageView)findViewById(R.id.arrow2);
        Bundle fregmentInfo = getIntent().getExtras();
      //  if (fregmentInfo != null) {
//            backFrgment = fregmentInfo.getString("BackFrgment");
//            currentFrgment = fregmentInfo.getString("CurrentFrgment");
//            currentFrgmentMain = fregmentInfo.getString("CurrentFrgmentMain");
//            title = fregmentInfo.getString("Title");
//            stateName= fregmentInfo.getString("StateName");
//            cutomerName= fregmentInfo.getString("CutomerName");
           navigationTitle.setText(GlobalClass.title);
           // toolbar.setTitle(title);
            navigationHome.setText(GlobalClass.backFrgment);
            navigationCurrent.setText(GlobalClass.currentFrgment);
            navigationCurrentMain.setText(GlobalClass.currentFrgmentMain);
           // productLogo.setVisibility(View.GONE);
//        } else if (fregmentInfo == null) {
//            Toast.makeText(this, "Bundle is null", Toast.LENGTH_SHORT).show();
//        }
        if(GlobalClass.backFrgment.equalsIgnoreCase(getResources().getString(R.string.sales_trend)) || GlobalClass.backFrgment.equalsIgnoreCase(getResources().getString(R.string.collection_trend)) ){
            navigationCurrent.setVisibility(View.GONE);
            arrow2.setVisibility(View.GONE);

        }
        if(GlobalClass.backFrgment.equalsIgnoreCase(getResources().getString(R.string.collection_trend)) ){

           // GlobalClass.apiKey="credit note";
            GlobalClass.apiKey="Receipt";
        }else{
            GlobalClass.apiKey="sales";
        }
        navigationHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), ServicesDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("BackFrgment", "Services");
//                bundle.putString("CurrentFrgment", "Sales");
//                bundle.putString("Title", "Sales");
//                bundle.putString("selectedTabPosition", "1");

//                myIntent.putExtras(bundle);

                GlobalClass.currentFrgment="Sales";
                if(GlobalClass.backFrgment.equalsIgnoreCase("Total Sale")){
                    GlobalClass.selectedTabPosition="0";

                }else if(GlobalClass.backFrgment.equalsIgnoreCase("Sales Trend")){
                    GlobalClass.selectedTabPosition="2";
                }else if(GlobalClass.backFrgment.equalsIgnoreCase("Collection Trend")){
                    GlobalClass.selectedTabPosition="3";
                }
                GlobalClass.backFrgment="Services";
                startActivity(myIntent);
            }
        });
        navigationCurrent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), CustomerDetailActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putString("StateName", stateName);
//                bundle.putString("BackFrgment", "TotalSale");
//                bundle.putString("CurrentFrgment", stateName);
//                bundle.putString("CurrentFrgmentMain", "CustomerList");
//                bundle.putString("Title", "Customer Wise TotalSale in "+stateName);
//                myIntent.putExtras(bundle);

                GlobalClass.title="Customer List of "+GlobalClass.backFrgment;
                startActivity(myIntent);
            }
        });
       // if (backFrgment.equalsIgnoreCase("TotalSale")) {
            navigationCurrentMain.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    List<ListPopupItem> listPopupItems = new ArrayList<>();
    for(String cutomer:GlobalClass.salesStateCustomer) {
    listPopupItems.add(new ListPopupItem(cutomer, R.mipmap.ic_launcher));
}
//                    listPopupItems.add(new ListPopupItem("Balaji Crushers", R.mipmap.ic_launcher));
//                    listPopupItems.add(new ListPopupItem("Bayer", R.mipmap.ic_launcher));
//                    listPopupItems.add(new ListPopupItem("Bharat Petroliam", R.mipmap.ic_launcher));
//                    listPopupItems.add(new ListPopupItem("Bharti Airtel", R.mipmap.ic_launcher));
//                    //Creating the instance of PopupMenu
                    showListServicePopupWindow(v, listPopupItems);
                }
            });
       // }

        navigatonShare = (ImageView) findViewById(R.id.navigatonShare);


        navigatonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),
                        "Share your screen as pdf or Image part 2",
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
        getTotalSaleCutomerInvoiceWise();
    }

    private void showListServicePopupWindow(View anchor, List<ListPopupItem> listPopup) {
        List<ListPopupItem> listPopupItems = new ArrayList<>();
       listPopupItems=listPopup;

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
                ListPopupWindowAdapter.ViewHolder  adapter =(ListPopupWindowAdapter.ViewHolder) view.getTag();
                Intent myIntent = new Intent(getApplicationContext(), InvoiceDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("StateName", stateName);
//                bundle.putString("BackFrgment", "TotalSale");
//                bundle.putString("CurrentFrgment", stateName);
//                bundle.putString("CurrentFrgmentMain", adapter.getTvTitle().getText().toString());
//                bundle.putString("CutomerName", adapter.getTvTitle().getText().toString());
//                bundle.putString("Title", "Invoices for "+adapter.getTvTitle().getText().toString());
//                myIntent.putExtras(bundle);
                GlobalClass.currentFrgmentMain=adapter.getTvTitle().getText().toString();
                GlobalClass.cutomerName=adapter.getTvTitle().getText().toString();
                GlobalClass.title="Invoices "+(GlobalClass.month!=null && !GlobalClass.month.isEmpty()?"of "+GlobalClass.month:"")+" for "+adapter.getTvTitle().getText().toString();
                startActivity(myIntent);
//                 Toast.makeText(getApplicationContext(), "clicked at " +adapter.getTvTitle().getText(), Toast.LENGTH_SHORT)
//                         .show();
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

    private void getTotalSaleCutomerInvoiceWise() {
        //svgLoader.startAnimation();
        if(progressDialog!=null) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
        }
        String url;

        if(GlobalClass.backFrgment.equalsIgnoreCase("Product Wise Sales")) {
            if(GlobalClass.month!=null && !GlobalClass.month.isEmpty()) {
                    url = Constants.BASE_LOCAL__URL + "getInvoiceListByProductAndCutomerAndDateAndMonth/sales/" + GlobalClass.productName + "/"+GlobalClass.cutomerName+"/"+GlobalClass.startDate+"/"+GlobalClass.endDate+ "/" + GlobalClass.month;
            }else {
                url = Constants.BASE_LOCAL__URL + "getInvoiceListByProductAndCutomerAndDate/sales/" + GlobalClass.productName + "/" + GlobalClass.cutomerName + "/" + GlobalClass.startDate + "/" + GlobalClass.endDate;
            }
            }else  if(GlobalClass.backFrgment.equalsIgnoreCase(getResources().getString(R.string.collection_trend)) ){
            if(GlobalClass.day!=null && !GlobalClass.day.isEmpty()) {
                url = Constants.BASE_LOCAL__URL + "getVoucherListByCutomerNdByDateNdDay/" + GlobalClass.apiKey + "/" + GlobalClass.stateName + "/" + GlobalClass.cutomerName + "/" + GlobalClass.startDate + "/" + GlobalClass.endDate + "/" + GlobalClass.day;
            }else{
                url = Constants.BASE_LOCAL__URL + "getVoucherListByCutomerNdByDate/" + GlobalClass.apiKey + "/" + GlobalClass.stateName + "/" + GlobalClass.cutomerName + "/" + GlobalClass.startDate + "/" + GlobalClass.endDate;

            }
        }else {
            if (GlobalClass.month != null && !GlobalClass.month.isEmpty()) {
                url = Constants.BASE_LOCAL__URL + "getVoucherListByCutomerNdByDateNdMonth/" + GlobalClass.apiKey + "/" + GlobalClass.stateName + "/" + GlobalClass.cutomerName + "/" + GlobalClass.startDate + "/" + GlobalClass.endDate + "/" + GlobalClass.month;
            } else {
                url = Constants.BASE_LOCAL__URL + "getVoucherListByCutomerNdByDate/" + GlobalClass.apiKey + "/" + GlobalClass.stateName + "/" + GlobalClass.cutomerName + "/" + GlobalClass.startDate + "/" + GlobalClass.endDate;

            }
        }
        //http://node16321-env-3353070.mj.milesweb.cloudgetVoucherListByCutomerNdByDate/sales/Maharashtra/ABC Fruits Pvt Ltd/2018-04-01/2019-03-31
        //String url = Constants.BASE_LOCAL__URL + "getVoucherListByCutomerNdByDate/"+GlobalClass.apiKey+"/"+GlobalClass.stateName+"/"+GlobalClass.cutomerName+"/"+GlobalClass.startDate+"/"+GlobalClass.endDate;
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
                            GlobalClass.StateCustomerInvoice.clear();
                        }
                        if (response.getString("code").equals(Integer.toString(206))) {

                            // GlobalClass.lastName = response.optString("userrole");
                            //Save to prefrence
                            JSONArray contentList = response.getJSONObject("contentList").getJSONArray("tableData");
                             chartData = response.getJSONObject("contentList").getJSONArray("chatData");

                            for(int i=0;i<contentList.length();i++){
                                customerInvoiceFullList.add(contentList.getJSONObject(i));
                                JSONObject element= contentList.getJSONObject(i);
                                GlobalClass.StateCustomerInvoice.put(element.getString("name"),element.getString("voucherGuid"));

                            }
//                            for (int i = 0; i < contentList.length(); i++) {
//                                JSONObject element = (JSONObject) contentList.get(i);
//                                cutomerSalesMap.put(element.getString("name"), (Math.abs(element.getDouble("value") / 10000000)));
//                            }

                           // GlobalClass.apiKey="sales";
                            Log.d("Api Key", " >>> "+GlobalClass.apiKey);
                            Log.d("GlobalClass.backFrgment", " >>> "+GlobalClass.backFrgment);
                            if(GlobalClass.backFrgment.equalsIgnoreCase(getResources().getString(R.string.collection_trend))) {
                                Fragment invoiceDetailFragment = new InvoiceCollectionDetailFragment();
                                GlobalClass.customerInvoiceFullList = customerInvoiceFullList;
                                GlobalClass.customerInvoiceChartData=chartData;
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.layout_invoice_content, invoiceDetailFragment)
                                        .commit();
                            }else{
                                Fragment invoiceDetailFragment = new InvoiceDetailFragment();
                                GlobalClass.customerInvoiceFullList = customerInvoiceFullList;
                                GlobalClass.customerInvoiceChartData=chartData;
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.layout_invoice_content, invoiceDetailFragment)
                                        .commit();
                            }

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
                String fileName=pdfCreator.createPdf();
                try{
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
                }catch (Exception e){
                    e.printStackTrace();
                }
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

}
