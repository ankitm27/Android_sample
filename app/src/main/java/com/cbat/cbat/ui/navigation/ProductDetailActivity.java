package com.cbat.cbat.ui.navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.cbat.cbat.ui.services.sales.details_sales.ProductDetailFragment;
import com.cbat.cbat.util.Constants;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.Utility;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {

    public static String TAG="ProductDetailActivity";
    String backFrgment;
    String currentFrgment;
    String title;
    String currentFrgmentMain;
    String stateName;
    private ProgressDialog progressDialog;
    Map<String,Double> productSalesMap;
    Map<String,JSONArray> customerFullList;

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
    ////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        customerFullList=new HashMap<>();
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
         navigationTitle=(TextView)findViewById(R.id.navigationTitle);
       // navigationCurrentMain = (TextView) findViewById(R.id.navigationCurrentMain);
        navigationHome = (TextView) findViewById(R.id.navigationHome);
        navigationTitle.setText(GlobalClass.title);
        // toolbar.setTitle(title);
        navigationHome.setText(GlobalClass.backFrgment);
        navigationCurrent.setText(GlobalClass.currentFrgment);
        Bundle fregmentInfo = getIntent().getExtras();
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
//                bundle.putString("BackFrgment", "Services");
//                bundle.putString("CurrentFrgment", "Sales");
//                bundle.putString("Title", "Sales");
//                bundle.putString("selectedTabPosition", "1");
                GlobalClass.selectedTabPosition="1";
                GlobalClass.backFrgment="Services";
                GlobalClass.currentFrgment="Sales";
                myIntent.putExtras(bundle);
                startActivity(myIntent);
            }
        });
        //if (backFrgment.equalsIgnoreCase("TotalSale")) {
            navigationCurrent.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    List<ListPopupItem> listPopupItems = new ArrayList<>();
                    for(String state:GlobalClass.salesState) {
                        listPopupItems.add(new ListPopupItem(state, R.mipmap.ic_launcher));
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
        Log.d(TAG,"Selected >> "+GlobalClass.backFrgment);
        if(GlobalClass.backFrgment.equalsIgnoreCase("Product Wise Sales")) {
            getTotalSaleProductWise();
        }

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
                Intent myIntent = new Intent(getBaseContext(), ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                String StateNameT=Utility.getStateName(adapter.getTvTitle().getText().toString());
//                bundle.putString("StateName", StateNameT);
//                bundle.putString("BackFrgment", "TotalSale");
//                bundle.putString("CurrentFrgment", adapter.getTvTitle().getText().toString());
//                bundle.putString("CurrentFrgmentMain", "CustomerList");
//                bundle.putString("Title", "Customer Wise TotalSale in "+adapter.getTvTitle().getText().toString());
//                myIntent.putExtras(bundle);
                GlobalClass.stateName=StateNameT;
                // GlobalClass.backFrgment="TotalSale";
                GlobalClass.currentFrgment=adapter.getTvTitle().getText().toString();
                GlobalClass.currentFrgmentMain="CustomerList";
                GlobalClass.title="Customer Wise Total Cost in "+adapter.getTvTitle().getText().toString();

                startActivity(myIntent);
//                 Toast.makeText(getApplicationContext(), "clicked at " +adapter.getTvTitle().getText().toString(), Toast.LENGTH_SHORT)
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

    private void getTotalSaleProductWise() {
        //svgLoader.startAnimation();
if(progressDialog!=null) {
    progressDialog.setIndeterminate(true);
    progressDialog.setMessage("Fetching data...");
    progressDialog.show();
}
        String url = Constants.BASE_LOCAL__URL + "getListStateWiseByDate/"+GlobalClass.apiKey+"/all/product/"+GlobalClass.startDate+"/"+GlobalClass.endDate;
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
                            productSalesMap=new HashMap<>();
                            GlobalClass.productFullList=new HashMap<>();

                          JSONObject customerMap=  contentList.getJSONObject(0);
                            Iterator<String> keys= customerMap.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                try {
                                    JSONArray value = customerMap.getJSONArray(key);
                                    Log.d(TAG,"key "+key);
                                   GlobalClass.productFullList.put(key,value);
                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                            }
                            JSONArray totalStateSales=  contentList.getJSONArray(1);
                            GlobalClass.productFullList.put("totalStateSales",totalStateSales);
//                            for (int i = 0; i < contentList.length(); i++) {
//                                JSONObject element = (JSONObject) contentList.get(i);
//                                cutomerSalesMap.put(element.getString("name"), (Math.abs(element.getDouble("value") / 10000000)));
//                            }
                            Fragment productFragment=new ProductDetailFragment();
                            //((CustomerDetailFragment) cutomerFragment).setMap(cutomerSalesMap);
                            //((CustomerDetailFragment) cutomerFragment).setFullMap(customerFullList);
                           // GlobalClass.customerFullList=customerFullList;
                            //Bundle bundle = new Bundle();
                           // bundle.putString("stateName", stateName);
                          //  GlobalClass.stateName=stateName;
                           // cutomerFragment.setArguments(bundle);
                           // GlobalClass.currentFrgment=Utility.getStateCode(stateName);
                         //   GlobalClass.currentFrgmentMain="CustomerList";
                            GlobalClass.title="Customer List  "+GlobalClass.backFrgment;

                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.layout_service_fragment_content1,productFragment )
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


}
