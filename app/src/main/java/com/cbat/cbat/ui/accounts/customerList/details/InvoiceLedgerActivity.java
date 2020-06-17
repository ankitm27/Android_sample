package com.cbat.cbat.ui.accounts.customerList.details;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.cbat.cbat.ui.accounts.customerList.details.fragment.InvoiceLedgerFragment;
import com.cbat.cbat.ui.accounts.customerList.details.navigation.AccountDrawerActivity;
import com.cbat.cbat.ui.navigation.MainActivity;
import com.cbat.cbat.ui.navigation.PdfCreatorActivity;
import com.cbat.cbat.ui.services.recievable.details_receivable.InvoiceDetailActivity_R;
import com.cbat.cbat.ui.services.recievable.details_receivable.LedgerDetailFragment_R;
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
import java.util.List;
import java.util.Map;

public class InvoiceLedgerActivity extends AccountDrawerActivity {

    PDFView pdfView;
    String backFrgment;
    String currentFrgment;
    String title,stateName,cutomerName;
    String currentFrgmentMain;
    String currentFrgmentFinal;
    String voucherNo;

    String navigatonTital;
    private ImageView share;
   // public Toolbar toolbar;
   // public RelativeLayout imageLayout;
  //  public RelativeLayout navigationLayout;

    //public ImageView productLogo;
    //public TextView navigationTitle;
  //  public TextView navigationCurrent;
    //public TextView navigationHome;
   // public TextView navigationCurrentMain;
   // public TextView navigationCurrentFinal;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    //public ImageView navigatonShare;
    public ImageView navigatonTimeFilter;
    private ProgressDialog progressDialog;
    public static List<JSONObject> customerInvoiceSummaryList=new ArrayList<>();
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;

    ////

    String[] Customer ={
            "Akaash","Bajaj Finance Ltd",
            "Cash at Bank Accounts","Order Booked Account",
            "Sales Account","Purchase Account",
            "Open Purchase Account", "Stock Account",
            "Top Accounts"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
//        LayoutInflater inflater = (LayoutInflater) this
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View contentView = inflater.inflate(R.layout.details_app_bar, null, false);
        // drawer.addView(contentView, 0);
//        toolbar.setTitle("");
        //super.onCreate(savedInstanceState);
       //setContentView(R.layout.invoice_ladger_activity);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.invoice_ladger_activity, null, false);
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        toolbar.setTitle("Customer List");
        navigationLayout.setVisibility(View.VISIBLE);
        imageLayout.setVisibility(View.GONE);
        navigationHome.setText("a/c");
        navigationBack.setText(GlobalClass.CustomerAccountName);
       // navigationCurrentMain.setText("Cust.a/c.");
        navigationCurrentMain.setText("9");



        navigationHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                List<ListPopupItem> listPopupItems = new ArrayList<>();
                for(String cutomer:Customer) {
                    listPopupItems.add(new ListPopupItem(cutomer, R.mipmap.ic_launcher));

                }

                showListServicePopupWindow(v, listPopupItems);

            }
        });

        navigationBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                Intent intent = new Intent(getBaseContext(), CustomerLedgerActivity.class);


                startActivity(intent);
            }
        });
      //  navigationCurrent = (TextView) findViewById(R.id.navigationCurrent);
        //  navigationHome = (TextView) findViewById(R.id.navigationHome);
   //     navigationTitle=(TextView)findViewById(R.id.navigationTitle);
        // navigationCurrentMain = (TextView) findViewById(R.id.navigationCurrentMain);
    //    navigationHome = (TextView) findViewById(R.id.navigationHome);
        // navigationCurrentFinal = (TextView) findViewById(R.id.navigationCurrentFinal);
      //  navigatonShare = (ImageView) findViewById(R.id.navigatonShare);


//        navigatonShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getBaseContext(),
//                        "Share your screen as pdf or Image",
//                        Toast.LENGTH_LONG).show();
//                try {
//                    createPdfWrapper();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (DocumentException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });

        Bundle fregmentInfo = getIntent().getExtras();
        // if (fregmentInfo != null) {
//            backFrgment = fregmentInfo.getString("BackFrgment");
//            currentFrgment = fregmentInfo.getString("CurrentFrgment");
//            currentFrgmentMain = fregmentInfo.getString("CurrentFrgmentMain");
//            currentFrgmentFinal= fregmentInfo.getString("CurrentFrgmentFinal");
//            title = fregmentInfo.getString("Title");
//            stateName= fregmentInfo.getString("StateName");
//            cutomerName= fregmentInfo.getString("CutomerName");
//            voucherNo= fregmentInfo.getString("voucherNo");
      //  navigationTitle.setText(GlobalClass.title);
        // toolbar.setTitle(title);
      //  navigationHome.setText(GlobalClass.cutomerName);
       // navigationCurrent.setText(GlobalClass.currentFrgmentFinal);
        //  navigationCurrentMain.setText(currentFrgmentMain);
        // navigationCurrentFinal.setText(currentFrgmentFinal);

        // productLogo.setVisibility(View.GONE);
//        } else if (fregmentInfo == null) {
//            Toast.makeText(this, "Bundle is null", Toast.LENGTH_SHORT).show();
//        }

//        navigationHome.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent myIntent = new Intent(getBaseContext(), InvoiceDetailActivity_R.class);
////                Bundle bundle = new Bundle();
////                bundle.putString("StateName", stateName);
////                bundle.putString("BackFrgment", "TotalSale");
////                bundle.putString("CurrentFrgment", currentFrgment);
////                bundle.putString("CurrentFrgmentMain", currentFrgmentMain);
////                bundle.putString("CutomerName", cutomerName);
////                bundle.putString("Title", "Invoices for "+cutomerName);
//
//                // myIntent.putExtras(bundle);
//
//                GlobalClass.title="Invoices for "+GlobalClass.cutomerName;
//                startActivity(myIntent);
//            }
//        });
//        navigationCurrentMain.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent myIntent = new Intent(getBaseContext(), CustomerDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("BackFrgment", "TotalSale");
//                bundle.putString("CurrentFrgment", "J&K");
//                bundle.putString("CurrentFrgmentMain", "CustomerList");
//                bundle.putString("Title", "Customer Wise TotalSale in J&K");
//                myIntent.putExtras(bundle);
//                startActivity(myIntent);
//            }
//        });
//        if (backFrgment.equalsIgnoreCase("TotalSale")) {
//
//        navigationCurrent.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                List<ListPopupItem> listPopupItems = new ArrayList<>();
//                for(Map.Entry<String, String> set: GlobalClass.StateCustomerInvoice.entrySet()){
//                    listPopupItems.add(new ListPopupItem(set.getKey(), R.mipmap.ic_launcher));
//                }
//
////                    listPopupItems.add(new ListPopupItem("507", R.mipmap.ic_launcher));
////                    listPopupItems.add(new ListPopupItem("612", R.mipmap.ic_launcher));
////                    listPopupItems.add(new ListPopupItem("715", R.mipmap.ic_launcher));
////                    listPopupItems.add(new ListPopupItem("450", R.mipmap.ic_launcher));
////                    listPopupItems.add(new ListPopupItem("1098", R.mipmap.ic_launcher));
//                //Creating the instance of PopupMenu
//                showListServicePopupWindow(v, listPopupItems);
//            }
//        });

        getCutomerInvoiceSummaryWise();
        drawer.addView(contentView, 0);
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
                Intent intent = new Intent(getBaseContext(), CustomerListActivity.class);
                startActivity(intent);
             //   Intent myIntent = new Intent(getBaseContext(), com.cbat.cbat.ui.services.recievable.details_receivable.LedgerDetailActivity_R.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("StateName", stateName);
//                bundle.putString("BackFrgment", stateName);
//                bundle.putString("CurrentFrgment", currentFrgment);
//                bundle.putString("CurrentFrgmentMain", currentFrgmentMain);
//                bundle.putString("CutomerName", cutomerName);
//                bundle.putString("CurrentFrgmentFinal", adapter.getTvTitle().getText().toString());
//                bundle.putString("Title", "Summay of Voucher "+adapter.getTvTitle().getText().toString());
//                bundle.putString("voucherNo",  GlobalClass.StateCustomerInvoice.get(adapter.getTvTitle().getText().toString()));
//                GlobalClass.currentFrgmentFinal=adapter.getTvTitle().getText().toString();
//                GlobalClass.title="Summay of Voucher "+adapter.getTvTitle().getText().toString();
//                GlobalClass.voucherNo= GlobalClass.StateCustomerInvoice.get(adapter.getTvTitle().getText().toString());
//                //myIntent.putExtras(bundle);
//                startActivity(myIntent);

//                Toast.makeText(getApplicationContext(), "clicked at " +adapter.getTvTitle().getText(), Toast.LENGTH_SHORT)
//                         .show();
//                 if(adapter.getTvTitle().getText().equals("Payable")){
//                     Intent myIntent = new Intent(getApplicationContext(), CustomerDetailActivity.class);
//                     Bundle bundle = new Bundle();
//                     bundle.putString("BackFrgment", "Services");
//                     bundle.putString("CurrentFrgment", "Payable");
//                     bundle.putString("Title", "Payable");
//                     myIntent.putExtras(bundle);
//                     startActivity(myIntent);
//
//                 }else if(adapter.getTvTitle().getText().equals("Sales")){
//                    Intent myIntent = new Intent(getApplicationContext(), CustomerDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("BackFrgment", "Services");
//                    bundle.putString("CurrentFrgment", "Sales");
//                    bundle.putString("Title", "Sales");
//                    myIntent.putExtras(bundle);
//                    startActivity(myIntent);
//
//                }else if(adapter.getTvTitle().getText().equals("Recievable")){
//                     Intent myIntent = new Intent(getApplicationContext(), CustomerDetailActivity.class);
//                     Bundle bundle = new Bundle();
//                     bundle.putString("BackFrgment", "Services");
//                     bundle.putString("CurrentFrgment", "Recievable");
//                     bundle.putString("Title", "Recievable");
//                     myIntent.putExtras(bundle);
//                     startActivity(myIntent);
//
//                 }else if(adapter.getTvTitle().getText().equals("Inventory")){
//                     Intent myIntent = new Intent(getApplicationContext(), CustomerDetailActivity.class);
//                     Bundle bundle = new Bundle();
//                     bundle.putString("BackFrgment", "Services");
//                     bundle.putString("CurrentFrgment", "Inventory");
//                     bundle.putString("Title", "Inventory");
//                     myIntent.putExtras(bundle);
//                     startActivity(myIntent);
//
//                 }else if(adapter.getTvTitle().getText().equals("Managment")){
//                     Intent myIntent = new Intent(getApplicationContext(), CustomerDetailActivity.class);
//                     Bundle bundle = new Bundle();
//                     bundle.putString("BackFrgment", "Services");
//                     bundle.putString("CurrentFrgment", "Managment");
//                     bundle.putString("Title", "Managment");
//                     myIntent.putExtras(bundle);
//                     startActivity(myIntent);
//
//                 }
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

//    private void loadFragment(Fragment fragment) {
//// create a FragmentManager
//        FragmentManager fm = getSupportFragmentManager();
//// create a FragmentTransaction to begin the transaction and replace the Fragment
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//// replace the FrameLayout with new Fragment
//        //fragmentTransaction.add(R.id.layout_service_fragment_content1, fragment);
//        fragmentTransaction.replace(R.id.layout_service_fragment_content1, fragment);
//        fragmentTransaction.commit(); // save the changes
//    }

//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//    }

    private void getCutomerInvoiceSummaryWise() {
        //svgLoader.startAnimation();
        if(progressDialog!=null) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
        }
        //http://node16321-env-3353070.mj.milesweb.cloudgetVoucherListByCutomerNdByDate/sales/Maharashtra/ABC Fruits Pvt Ltd/2018-04-01/2019-03-31
        String url = Constants.BASE_LOCAL__URL + "getVoucherDetailsByID/"+GlobalClass.voucherNo;
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
                            GlobalClass.customerInvoiceSummaryList=new ArrayList<>();
                            JSONArray contentList = response.getJSONArray("contentList");
                            for(int i=0;i<contentList.length();i++){
                                GlobalClass.customerInvoiceSummaryList.add(contentList.getJSONObject(i));


                            }
                            Log.d("check12@",GlobalClass.customerInvoiceSummaryList.toString());
//                            for (int i = 0; i < contentList.length(); i++) {
//                                JSONObject element = (JSONObject) contentList.get(i);
//                                cutomerSalesMap.put(element.getString("name"), (Math.abs(element.getDouble("value") / 10000000)));
//                            }
                            Fragment ledgerDetailFragment=new InvoiceLedgerFragment();
                            // GlobalClass.customerInvoiceSummaryList=customerInvoiceSummaryList;
                            //((InvoiceDetailFragment) invoiceDetailFragment).setFullList(customerInvoiceFullList);
//GlobalClass.backFrgment=GlobalClass.mainNavigation;
//                            Bundle bundle = new Bundle();
//                            bundle.putString("StateName", stateName);
//                            bundle.putString("BackFrgment", GlobalClass.mainNavigation);
//                            bundle.putString("CurrentFrgment", stateName);
//                            bundle.putString("CurrentFrgmentMain", currentFrgmentMain);
//                            bundle.putString("CutomerName", cutomerName);
//                            // bundle.putString("Title", "Invoices for "+cutomerName);
//                            ledgerDetailFragment.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.ledger_fragment_content, ledgerDetailFragment)
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


    private void createPdfWrapper() throws FileNotFoundException, DocumentException {

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


}


