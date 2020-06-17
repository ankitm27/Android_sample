package com.cbat.cbat.ui.services.recievable.details_receivable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import com.cbat.cbat.adapter.TableFixHeaderAdapter;
import com.cbat.cbat.adapter.TableFixHeadersAdapterFactory;
import com.cbat.cbat.adapter.original_sortable.ItemSortable;
import com.cbat.cbat.adapter.original_sortable.NexusWithImage;
import com.cbat.cbat.adapter.original_sortable.OriginalBodyCellViewGroup;
import com.cbat.cbat.adapter.original_sortable.OriginalFirstBodyCellViewGroup;
import com.cbat.cbat.adapter.original_sortable.OriginalSortableTableFixHeader;
import com.cbat.cbat.ui.services.sales.details_sales.CustomerDetailFragment;
import com.cbat.cbat.ui.services.sales.details_sales.ProductDetailFragment;
import com.cbat.cbat.util.Constants;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.Utility;
import com.github.mikephil.charting.charts.LineChart;
import com.inqbarna.tablefixheaders.TableFixHeaders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RecivableTop5Fragment_R extends Fragment {
    String TAG="RecivableTop5Fragment_R";
    private ProgressDialog progressDialog;
    Map<String,Double> productSalesMap;
    Map<String,JSONArray> customerFullList;
    private TableFixHeaders tableFixHeadersCustomer;
    private TableFixHeaders tableFixHeadersProduct;
    private TableFixHeaders tableFixHeadersAging;
    private TableFixHeadersAdapterFactory tableFixHeadersAdapterFactoryCustomer;
    private TableFixHeadersAdapterFactory tableFixHeadersAdapterFactoryProduct;
    private TableFixHeadersAdapterFactory tableFixHeadersAdapterFactoryAging;

    public static RecivableTop5Fragment_R newInstance() {
        return new RecivableTop5Fragment_R();
    }

    //MPLine
    private LineChart lineChart;
   public  View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         v = inflater.inflate(R.layout.services_receivable_top5_fragment, container, false);
       // final ProgressBar progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        Log.d("URL :- ","Test");
        getTotalSaleCutomerWise();


        return v;
    }



    private void getTotalSaleCutomerWise() {
        //svgLoader.startAnimation();
        if(progressDialog!=null) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
        }
        String url;

            url = Constants.BASE_LOCAL__URL + "getReceivableTop/all/5/"+GlobalClass.startDate+"/"+GlobalClass.endDate;


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
            Volley.newRequestQueue(getContext()).add(jsonObjRequest);
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
                            JSONObject contentList = response.getJSONObject("contentList");
                           // cutomerSalesMap=new HashMap<>();
                            GlobalClass.customerSalesTrendFullList=new HashMap<>();
                           // GlobalClass.customerFullList=new HashMap<>();
                            //JSONObject customerMap=  contentList.getJSONObject(0);
                            Iterator<String> keys= contentList.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                try {
                                    JSONArray value = contentList.getJSONArray(key);
                                    Log.d(TAG,"key "+key);
                                    if(key.equalsIgnoreCase("Customer")) {
                                        GlobalClass.customerTop5=new HashMap<>();
                                        //GlobalClass.customerSalesTrendFullList.put(key,value);
                                        JSONObject customerMap = value.getJSONObject(0);
                                        Iterator<String> keys1 = customerMap.keys();
                                        Map<String, JSONArray> finalList = new HashMap<>();
                                        while (keys1.hasNext()) {
                                            String keyT = keys1.next();
                                            JSONArray valueT = customerMap.getJSONArray(keyT);

                                            GlobalClass.customerTop5.put(keyT, valueT);

                                        }
                                    }else  if(key.equalsIgnoreCase("Product")) {
                                        GlobalClass.productTop5=new HashMap<>();
                                        //GlobalClass.customerSalesTrendFullList.put(key,value);
                                        JSONObject customerMap = value.getJSONObject(0);
                                        Iterator<String> keys1 = customerMap.keys();
                                        Map<String, JSONArray> finalList = new HashMap<>();
                                        while (keys1.hasNext()) {
                                            String keyT = keys1.next();
                                            JSONArray valueT = customerMap.getJSONArray(keyT);

                                            GlobalClass.productTop5.put(keyT, valueT);

                                        }
                                    }else  if(key.equalsIgnoreCase("Aging")) {
                                        GlobalClass.agingTop5=new HashMap<>();
                                        //GlobalClass.customerSalesTrendFullList.put(key,value);
                                        JSONObject customerMap = value.getJSONObject(0);
                                        Iterator<String> keys1 = customerMap.keys();
                                        Map<String, JSONObject> finalList = new HashMap<>();
                                        while (keys1.hasNext()) {
                                            String keyT = keys1.next();
                                            JSONObject valueT = customerMap.getJSONObject(keyT);

                                            GlobalClass.agingTop5.put(keyT, valueT);

                                        }
                                    }
                                    Fragment productFragment = new RecivableTop5DetailFragment();

                                  //  GlobalClass.title = "Customer List  " + GlobalClass.backFrgment;

                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.layout_service_fragment_content_top5, productFragment)
                                            .commit();

                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                            }
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
                        Toast.makeText(getContext(), "Network Error.", Toast.LENGTH_LONG).show();

                    } else if (error instanceof ServerError) {
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(getContext(), "Authentication fail.", Toast.LENGTH_LONG).show();

                    } else if (error instanceof ParseError) {
                        Toast.makeText(getContext(), "We are not able to process login request due to technical issue.", Toast.LENGTH_LONG).show();

                    } else if (error instanceof NoConnectionError) {
                        Toast.makeText(getContext(), "No connection available to connect with Server.", Toast.LENGTH_LONG).show();

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
                        Toast.makeText(getContext(), strBuff.toString(), Toast.LENGTH_LONG).show();


                    }
                    //  onRequestFailed();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
    }


}