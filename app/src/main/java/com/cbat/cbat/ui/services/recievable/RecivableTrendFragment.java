package com.cbat.cbat.ui.services.recievable;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.cbat.cbat.ui.services.sales.details_sales.CustomerDetailFragment;
import com.cbat.cbat.util.Constants;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.Utility;
import com.github.mikephil.charting.charts.LineChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.cbat.cbat.ui.navigation.ProductDetailActivity.TAG;

public class RecivableTrendFragment extends Fragment {

    private ProgressDialog progressDialog;
    Map<String,Double> productSalesMap;
    Map<String,JSONArray> customerFullList;
    public static RecivableTrendFragment newInstance() {
        return new RecivableTrendFragment();
    }

    //MPLine
    private LineChart lineChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.services_sales_trendwisesales_fragment, container, false);
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
            progressDialog.setMessage("Sales Trend data...");
            progressDialog.show();
        }
        String url;

            url = Constants.BASE_LOCAL__URL + "getListStateWiseByDate/"+GlobalClass.apiKey+"/all/customer/"+GlobalClass.startDate+"/"+GlobalClass.endDate;


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
                            JSONArray contentList = response.getJSONArray("contentList");
                           // cutomerSalesMap=new HashMap<>();
                            GlobalClass.customerSalesTrendFullList=new HashMap<>();
                           // GlobalClass.customerFullList=new HashMap<>();
                            JSONObject customerMap=  contentList.getJSONObject(0);
                            Iterator<String> keys= customerMap.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                try {
                                    JSONArray value = customerMap.getJSONArray(key);
                                    Log.d(TAG,"key "+key);
                                    GlobalClass.customerSalesTrendFullList.put(key,value);
                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                            }
                            JSONArray totalStateSales=  contentList.getJSONArray(1);
                            GlobalClass.customerSalesTrendFullList.put("totalStateSales",totalStateSales);
                            Fragment cutomerFragment=new CustomerDetailFragment();

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.layout_service_fragment_content_trend, cutomerFragment)
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