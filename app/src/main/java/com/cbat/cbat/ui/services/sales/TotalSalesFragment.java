package com.cbat.cbat.ui.services.sales;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.agrawalsuneet.svgloaderspack.loaders.SVGLoader;
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
import com.cbat.cbat.adapter.TestViewHolder;
import com.cbat.cbat.adapter.original_sortable.ItemSortable;
import com.cbat.cbat.adapter.original_sortable.NexusWithImage;
import com.cbat.cbat.adapter.original_sortable.OriginalBodyCellViewGroup;
import com.cbat.cbat.adapter.original_sortable.OriginalFirstBodyCellViewGroup;
import com.cbat.cbat.adapter.original_sortable.OriginalSortableTableFixHeader;
import com.cbat.cbat.ui.navigation.CustomerDetailActivity;
import com.cbat.cbat.util.Constants;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.TooltipWindow;
import com.cbat.cbat.util.Utility;
import com.github.florent37.materialleanback.MaterialLeanBack;
import com.github.mikephil.charting.charts.LineChart;
//import com.github.naz013.colorslider.ColorSlider;
import com.inqbarna.tablefixheaders.TableFixHeaders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.gr.java_conf.androtaku.geomap.GeoMapView;
import jp.gr.java_conf.androtaku.geomap.OnInitializedListener;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class TotalSalesFragment extends Fragment {
    String TAG = "TotalSalesFragment";

    private TableFixHeaders tableFixHeaders;
    private TableFixHeadersAdapterFactory tableFixHeadersAdapterFactory;
    GeoMapView geoMapView;
    ArrayList mapSlectedState = new ArrayList();
    Map<String, Float> salesMap = new HashMap<>();
    Map<String, Float> finalMap = new HashMap<>();

    LinearLayout totalSalesLayout;
    SVGLoader svgLoader;
    private ProgressDialog progressDialog;
    String[] hexColors;
    // ColorSlider colorSlider;
    LinearLayout stateList;
    TooltipWindow tipWindow;
    Animation animZoomOut;
    MaterialLeanBack materialLeanBack;
LinearLayout mapLegend;
    LinearLayout stateSales;

    public static TotalSalesFragment newInstance() {
        return new TotalSalesFragment();
    }

    //MPLine
    private LineChart lineChart;
    Map<String, String> stateMapCode = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.services_sales_totalsales_fragment, container, false);
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
       // materialLeanBack=v.findViewById(R.id.materialLeanBack);
        mapLegend=v.findViewById(R.id.mapLegend);
        stateSales=v.findViewById(R.id.casts_container);
       GlobalClass.apiKey="Sales";
        final FrameLayout totalSalesContainer = (FrameLayout) v.findViewById(R.id.totalSalesContainer);
        totalSalesLayout = (LinearLayout) v.findViewById(R.id.totalSalesLayout);

        geoMapView = (GeoMapView) v.findViewById(R.id.totalSalesGeoMap);
        getTotalSaleStateWise(geoMapView);

        return v;
    }




    private void loadFragment(String stateName) {
        Log.d("@#@#@#",stateName );
        Intent myIntent = new Intent(getActivity(), CustomerDetailActivity.class);
        GlobalClass.stateName = stateName;
        GlobalClass.backFrgment="Total Sales";
        GlobalClass.currentFrgment = Utility.getStateCode(stateName);
        GlobalClass.currentFrgmentMain = "CustomerList";
        GlobalClass.title = "Customer Wise " + GlobalClass.backFrgment + " in " + Utility.getStateCode(stateName);
        startActivity(myIntent);

    }

    private void getTotalSaleStateWise(GeoMapView geoMapView) {
        //svgLoader.startAnimation();

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
        String url = Constants.BASE_LOCAL__URL + "getListByDate/" + GlobalClass.apiKey + "/state/"+GlobalClass.startDate+"/"+GlobalClass.endDate;
        JSONObject paramsJsonObj = new JSONObject();
        try {

            paramsJsonObj.put("id", GlobalClass.comapnyId);


            Log.d("URL :- ", url);
            Log.d("paramsJsonObj :- ", paramsJsonObj.toString());
            JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, url, paramsJsonObj, this.createRequestSuccessListener(0, geoMapView), this.createRequestErrorListener(System.currentTimeMillis()));
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


    private Response.Listener<JSONObject> createRequestSuccessListener(final int responseCode, final GeoMapView geoMapView) {
        Response.Listener<JSONObject> listenerObj = null;
        //    if (requestCode == REQUEST_LOGIN) {

        listenerObj = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (responseCode == 0) {
                        Log.d("Res", response.toString());
//                        if (progressDialog != null) {
//                            progressDialog.dismiss();
//                        }
                        if (response.getString("code").equals(Integer.toString(206))) {

                            // GlobalClass.lastName = response.optString("userrole");
                            //Save to prefrence
                            JSONArray contentList = response.getJSONArray("contentList");
                            GlobalClass.totalSales= new HashMap<>();
                            for (int i = 0; i < contentList.length(); i++) {
                                JSONObject element = (JSONObject) contentList.get(i);
                                salesMap.put(element.getString("name"), Utility.decimal2PalceAsInput(Math.abs(element.getLong("value"))));
                                GlobalClass.totalSales.put(element.getString("name"),element);
                            }

                            geoMapView.setOnInitializedListener(new OnInitializedListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onInitialized(GeoMapView geoMapView) {
//                                    svgLoader.setVisibility(View.INVISIBLE);
//                                    svgLoader.endAnimation();
                                    if (progressDialog != null) {
                                        progressDialog.dismiss();
                                    }
                                    GlobalClass.salesState.clear();
                                    hexColors = new String[salesMap.size()];
                                    int i = 0;

                                    for (Map.Entry<String, Float> set : salesMap.entrySet()) {
                                       // View stateView = getLayoutInflater().inflate(R.layout.state_list, null);
                                        String stateCode = Utility.getStateCode(set.getKey());
                                        // GlobalClass.salesState.add(set.getKey());
                                        GlobalClass.salesState.add(stateCode);
                                        mapSlectedState.add(stateCode.trim());
                                        finalMap.put(stateCode,set.getValue());
                                        geoMapView.setCountryColor(stateCode, Utility.getSalesColour(set.getValue()), String.valueOf(set.getValue()) +  GlobalClass.currecyPrefix );
                                        hexColors[i] = Utility.getSalesColour(set.getValue());
                                        i++;
                                    }

                                    //create LayoutInflator class
//                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
//                                    int size = casts.size();
//                                    for (int i = 0; i < size; i++) {
//                                        Cast cast = casts.get(i);
//// create dynamic LinearLayout and set Image on it.
//                                        if (cast != null) {
//                                            LinearLayout clickableColumn = (LinearLayout) inflater.inflate(
//                                                    R.layout.clickable_column, null);
//                                            ImageView thumbnailImage = (ImageView) clickableColumn
//                                                    .findViewById(R.id.thumbnail_image);
//                                            TextView titleView = (TextView) clickableColumn
//                                                    .findViewById(R.id.title_view);
//                                            TextView subTitleView = (TextView) clickableColumn
//                                                    .findViewById(R.id.subtitle_view);
//                                            thumbnailImage.setImageResource(R.drawable.ic_launcher);

                                    stateSales.removeAllViews();
                                    int count=0;
                                    int count1=2;

                                    for (Map.Entry<String, Float> val:finalMap.entrySet()) {
                                        if(val.getKey()!=null && !val.getKey().isEmpty()) {
                                            LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            View contentView = inflater1.inflate(R.layout.cell_test, null, false);
                                            TextView tv = contentView.findViewById(R.id.saleAmount);
                                            TextView tvState = contentView.findViewById(R.id.textView);
                                            RelativeLayout frame = contentView.findViewById(R.id.cardFrame);
                                            LinearLayout ll = contentView.findViewById(R.id.textLayout);
                                            // mapLegend.addView(contentView, 0);
                                            //dots[count] = new TextView(getContext());
                                            String text = String.valueOf(val.getValue()) + " %";
                                            tv.setText(text);
                                            tvState.setText(val.getKey());
                                            Log.d(TAG, "State >>>>>>>>>>>>" + val.getKey() + " VAlue % >>>>>>>>> " + val.getValue());
                                            //dots[count].setTextSize(15);
                                            Log.d(TAG, "State >>>>>>>>>>>>" + tvState.getText().toString() + " VAlue % >>>>>>>>> " + tv.getText().toString());

                                            frame.setBackgroundColor(Color.parseColor(Utility.getSalesColour(val.getValue())));

                                            View contentV = inflater1.inflate(R.layout.horizontal_divider, null, false);
                                            ///  if(val.getKey()!=null && !val.getKey().isEmpty()) {
                                            stateSales.addView(contentView, count);
                                            stateSales.addView(contentV, count);
                                            //  }
                                            contentView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    TextView cast = (TextView) v.findViewById(R.id.saleAmount);
                                                    TextView castTextView = (TextView) v.findViewById(R.id.textView);
                                                    // Toast.makeText(getContext(), castTextView.getText()+" >> "+Utility.getStateName(castTextView.getText().toString()), Toast.LENGTH_SHORT).show();
                                                    loadFragment(Utility.getStateName(castTextView.getText().toString()));
                                                }
                                            });

                                            count++;
                                        }
                                    }

                                    geoMapView.refresh();
                                }
                            });

                            geoMapView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {

                                    // have same code as onTouchEvent() (for the Activity) above

                                    switch (event.getAction()) {
                                        case MotionEvent.ACTION_DOWN:

                                            //screen touch get x of the touch event
                                            float x = event.getX();
                                            //screen touch get y of the touch event
                                            float y = event.getY();

                                            //check if touch point intersects the path bounds
                                            for (Path p : geoMapView.getPaths()) {
                                                RectF pBounds = new RectF();
                                                p.computeBounds(pBounds, true);
                                                Log.d("touched", "  pBounds.centerX : " + pBounds.centerX() + "centerY :  " + pBounds.centerY());

                                                if (pBounds.contains(x, y)) {
                                                    //select path
                                                    Path selectedPath = p;// where selectedPath is assumed declared.
                                                    Log.d("touched", " isConvex : " + selectedPath.isConvex());
                                                    String stateCode = geoMapView.getPathsMap().get(selectedPath);
                                                    Log.d("touched", "X : " + x + " Y : " + y);
                                                    Log.d("stateCode##", stateCode);

                                                    // totalSalesLayout.removeAllViews();
                                                    // totalSalesLayout.addView(tableFixHeaders);
                                                    if (mapSlectedState.contains(stateCode.trim())) {
                                                        Log.d("State Code##", stateCode.trim());
                                                        Log.d("State Name##", Utility.getStateName(stateCode.trim()));
                                                        // ManagmentFragment fragment = new ManagmentFragment();
                                                         loadFragment(Utility.getStateName(stateCode.trim()));
                                                    }
                                                    break;
                                                }
                                            }
                                            //  dv.invalidate();
                                            break;

//                 case MotionEvent.ACTION_UP:
//                        //screen touch get x of the touch event
//                        x = event.getX();
//                        //screen touch get y of the touch event
//                        y = event.getY();
//                        break;
//
//                case MotionEvent.ACTION_MOVE:
//                        //screen touch get x of the touch event
//                        x = event.getX();
//                        //screen touch get y of the touch event
//                        y = event.getY();
//                        break;
//                }
                                    }
                                    return true;
                                }
                            });
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