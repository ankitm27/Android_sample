package com.cbat.cbat.ui.services.recievable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.cbat.cbat.ui.services.recievable.details_receivable.InvoiceDetailActivity_R;
import com.cbat.cbat.ui.services.recievable.details_receivable.RecivableAgingDetailFragment;
import com.cbat.cbat.ui.services.sales.details_sales.ProductDetailFragment;
import com.cbat.cbat.util.Constants;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.MyValueFormatter;
import com.cbat.cbat.util.PichartMarkerView;
import com.cbat.cbat.util.Utility;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.inqbarna.tablefixheaders.TableFixHeaders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.cbat.cbat.ui.navigation.ProductDetailActivity.TAG;

public class RecivableAgingFragment extends Fragment {

    private ProgressDialog progressDialog;
    Map<String,JSONObject> customerAgingFullList;
    protected Typeface tfLight;

    public static RecivableAgingFragment newInstance() {
        return new RecivableAgingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.services_receivable_againg_fragment, container, false);
        // final ProgressBar progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);

        if(GlobalClass.customerAgingFullList!=null && !GlobalClass.customerAgingFullList.isEmpty()) {
            Fragment agingDetailFragment=new RecivableAgingDetailFragment();

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout_service_fragment_content_receivable_againg,agingDetailFragment )
                    .commit();
        }else{
            getTotalReceivableCutomerWise();
        }

        return v;
    }

    private void getTotalReceivableCutomerWise() {
        //svgLoader.startAnimation();
        if(progressDialog!=null) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
        }
        String url;

            url = Constants.BASE_LOCAL__URL + "getReceivableCustomerAging/"+GlobalClass.startDate+"/"+GlobalClass.endDate;


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
                    Log.d(TAG, "responseCode >> " + responseCode);
                    if (responseCode == 0) {
                        Log.d("Res", response.toString());
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Log.d(TAG, "response >> " + response.toString());
                        if (response.getString("code").equals(Integer.toString(206))) {
                            GlobalClass.salesStateCustomer=new ArrayList<>();//.clear();
                            // GlobalClass.lastName = response.optString("userrole");
                            //Save to prefrence
                            JSONArray contentList = response.getJSONArray("contentList");
                            // cutomerSalesMap=new HashMap<>();
                            GlobalClass.customerAgingFullList = new HashMap<>();
                            customerAgingFullList = new HashMap<>();
                            // GlobalClass.customerFullList=new HashMap<>();
                           // for (int i = 0; i < contentList.length(); i++) {
                            if(contentList.length()>0){
                            for (int i = 0; i < 1; i++) {
                                JSONObject customerMap = contentList.getJSONObject(i);
                                Iterator<String> keys = customerMap.keys();
                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    try {
                                        JSONObject value = customerMap.getJSONObject(key);
                                        Log.d(TAG, "key >> " + key);
                                        // value.getInt("noDue");
                                        GlobalClass.customerAgingFullList.put(key, value);
                                        customerAgingFullList.put(key, value);
                                        if(!key.equalsIgnoreCase("Total Receivable")) {
                                            GlobalClass.salesStateCustomer.add(key);
                                        }
                                    } catch (JSONException e) {
                                        // Something went wrong!
                                    }
                                }
                            }
                            }
                            Log.d(TAG, "GlobalClass.salesStateCustomer Size>> " + GlobalClass.salesStateCustomer.size());
                            Log.d(TAG, "GlobalClass.salesStateCustomer >> " + GlobalClass.salesStateCustomer.toString());
                            Fragment agingDetailFragment=new RecivableAgingDetailFragment();

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.layout_service_fragment_content_receivable_againg,agingDetailFragment )
                                    .commit();
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