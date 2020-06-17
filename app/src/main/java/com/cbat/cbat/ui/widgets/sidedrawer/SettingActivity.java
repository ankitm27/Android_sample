package com.cbat.cbat.ui.widgets.sidedrawer;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


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
import com.cbat.cbat.adapter.MonthlyExpandableListAdapter;
import com.cbat.cbat.adapter.ProductExpandableListAdapter;
import com.cbat.cbat.adapter.SliderPagerAdapter;
import com.cbat.cbat.adapter.TableFixHeadersAdapterFactory;
import com.cbat.cbat.ui.home.HomeFragment;
import com.cbat.cbat.ui.navigation.ServicesDetailActivity;
import com.cbat.cbat.ui.widgets.DrawerActivity;
import com.cbat.cbat.util.Constants;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.PichartMarkerView;
import com.cbat.cbat.util.SalesVsCollectionMarkerView;
import com.cbat.cbat.util.Utility;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class SettingActivity extends DrawerActivity implements SlyCalendarDialog.Callback {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    ExpandableListView expandableListView1;
    ProductExpandableListAdapter expandableListAdapter1;
    List<String> expandableListTitle1;
    HashMap<String, List<String>> expandableListDetail1;
    ImageView crImage;
    ImageView lakhImage;

    ImageView thImage;

    EditText periodText;
    protected ProgressDialog progressDialog = null;
    private String TAG="SettingActivity";

    ////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getBaseContext(),
                R.style.AppTheme_Dark_Dialog);
       // setContentView(R.layout.activity_main);
         LayoutInflater inflater = (LayoutInflater) this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View contentView = inflater.inflate(R.layout.activity_setting, null, false);
                drawer.addView(contentView, 0);
        share.setVisibility(View.GONE);
        timeFilter.setVisibility(View.GONE);

        headerTitle.setVisibility(View.VISIBLE);
        headerTitle.setText("Setting");
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new MonthlyExpandableListAdapter(this, expandableListTitle, expandableListDetail,getVarData());
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Collapsed.",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        expandableListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));



        crImage = findViewById(R.id.cr);
        crImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.currecyPrefix = "CR";

                resetCurrencyImage();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        lakhImage = findViewById(R.id.lakh);
        lakhImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.currecyPrefix = "LH";

                resetCurrencyImage();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        thImage = findViewById(R.id.th);
        thImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.currecyPrefix = "TH";

                resetCurrencyImage();
//                Toast.makeText(getContext(),
//
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        resetCurrencyImage();

        periodText = (EditText) findViewById(R.id.periodText);
        String peroidVal="'"+GlobalClass.startDate+"' : '"+GlobalClass.endDate+"'";
        periodText.setText(peroidVal);
        periodText.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                new SlyCalendarDialog()
                        .setSingle(false)
                        .setHeaderColor(getColor(R.color.colorPrimary))
                        .setSelectedColor(getColor(R.color.chart5))
                        .setFirstMonday(false)
                        .setCallback(SettingActivity.this)
                        .show(getSupportFragmentManager(), "TAG_CUSTOM_CALENDAR");


            }
        });
//
//        expandableListView1 = (ExpandableListView) findViewById(R.id.ProductListView);
//        expandableListDetail1 = getProductData();
//        expandableListTitle1 = new ArrayList<String>(expandableListDetail1.keySet());
//        expandableListAdapter1 = new ProductExpandableListAdapter(this, expandableListTitle1, expandableListDetail1,getVarData());
//        expandableListView1.setAdapter(expandableListAdapter1);
//        expandableListView1.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
//        expandableListView1.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//
//
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle1.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        expandableListView1.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//
//            @Override
//            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle1.get(groupPosition) + " List Collapsed.",
//                        Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        expandableListView1.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v,
//                                        int groupPosition, int childPosition, long id) {
//                Toast.makeText(
//                        getApplicationContext(),
//                        expandableListTitle1.get(groupPosition)
//                                + " -> "
//                                + expandableListDetail1.get(
//                                expandableListTitle1.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT
//                ).show();
//                return false;
//            }
//        });



    }

    public void resetCurrencyImage() {
        if (GlobalClass.currecyPrefix.equalsIgnoreCase("CR")) {
            crImage.setImageDrawable(getResources().getDrawable(R.drawable.cr_orange_icon));
            thImage.setImageDrawable(getResources().getDrawable(R.drawable.th_icon));
            lakhImage.setImageDrawable(getResources().getDrawable(R.drawable.lakh));

        } else if (GlobalClass.currecyPrefix.equalsIgnoreCase("LH")) {
            crImage.setImageDrawable(getResources().getDrawable(R.drawable.cr_icon));
            thImage.setImageDrawable(getResources().getDrawable(R.drawable.th_icon));
            lakhImage.setImageDrawable(getResources().getDrawable(R.drawable.lakh_icon));

        } else if (GlobalClass.currecyPrefix.equalsIgnoreCase("TH")) {
            crImage.setImageDrawable(getResources().getDrawable(R.drawable.cr_icon));
            thImage.setImageDrawable(getResources().getDrawable(R.drawable.th));
            lakhImage.setImageDrawable(getResources().getDrawable(R.drawable.lakh));
        }
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    public  HashMap<String,Integer> getVarData() {



        HashMap<String,Integer> monthTarget = new HashMap<>();
        monthTarget.put("jan",200000);
        monthTarget.put("feb",490000);
        monthTarget.put("mar",4990000);
        monthTarget.put("apr",590000);
        monthTarget.put("may",3982900);
        monthTarget.put("jun",200000);
        monthTarget.put("jul",293903);
        monthTarget.put("aug",399020);
        monthTarget.put("sep",92003);
        monthTarget.put("oct",399001);
        monthTarget.put("nov",293903);
        monthTarget.put("dec",239930);



        return monthTarget;
    }
        public  HashMap<String, List<String>> getData() {
            HashMap<String, List<String>> expandableListDetail = new HashMap<String,List<String>>();

            List<String> cricket = new ArrayList<>();
            cricket.add("jan");
            cricket.add("feb");
            cricket.add("mar");
            cricket.add("apr");
            cricket.add("may");
            cricket.add("jun");
            cricket.add("jul");
            cricket.add("aug");
            cricket.add("sep");
            cricket.add("oct");
            cricket.add("nov");
            cricket.add("dec");

            Map<String,Integer> monthTarget = new HashMap<>();
            monthTarget.put("jan",200000);
            monthTarget.put("feb",490000);
            monthTarget.put("mar",4990000);
            monthTarget.put("apr",590000);
            monthTarget.put("may",3982900);
            monthTarget.put("jun",200000);
            monthTarget.put("jul",293903);
            monthTarget.put("aug",399020);
            monthTarget.put("sep",92003);
            monthTarget.put("oct",399001);
            monthTarget.put("nov",293903);
            monthTarget.put("dec",239930);





//            List<String> football = new ArrayList<String>();
//            football.add("Brazil");
//            football.add("Spain");
//            football.add("Germany");
//            football.add("Netherlands");
//            football.add("Italy");
//
//            List<String> basketball = new ArrayList<String>();
//            basketball.add("United States");
//            basketball.add("Spain");
//            basketball.add("Argentina");
//            basketball.add("France");
//            basketball.add("Russia");

            expandableListDetail.put("Monthly Target", cricket);
//            expandableListDetail.put("FOOTBALL TEAMS", football);
//            expandableListDetail.put("BASKETBALL TEAMS", basketball);
            return expandableListDetail;
        }

    public  HashMap<String, List<String>> getProductData() {
        HashMap<String, List<String>> expandableListDetail1 = new HashMap<String,List<String>>();

        List<String> cricket = new ArrayList<>();
        cricket.add("jan");
        cricket.add("feb");
        cricket.add("mar");
        cricket.add("apr");
        cricket.add("may");
        cricket.add("jun");
        cricket.add("jul");
        cricket.add("aug");
        cricket.add("sep");
        cricket.add("oct");
        cricket.add("nov");
        cricket.add("dec");




//            List<String> football = new ArrayList<String>();
//            football.add("Brazil");
//            football.add("Spain");
//            football.add("Germany");
//            football.add("Netherlands");
//            football.add("Italy");
//
//            List<String> basketball = new ArrayList<String>();
//            basketball.add("United States");
//            basketball.add("Spain");
//            basketball.add("Argentina");
//            basketball.add("France");
//            basketball.add("Russia");

        expandableListDetail1.put("Product Colour", cricket);
//            expandableListDetail.put("FOOTBALL TEAMS", football);
//            expandableListDetail.put("BASKETBALL TEAMS", basketball);
        return expandableListDetail1;
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
                String peroidVal="'"+GlobalClass.startDate+"' : '"+GlobalClass.endDate+"'";
                periodText.setText(peroidVal);
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
               // Intent myIntent = new Intent(getBaseContext(), ServicesDetailActivity.class);
                //GlobalClass.backFrgment="Services";
               // startActivity(myIntent);
            }
        }
    }


    private void getProductList() {
        if (progressDialog != null) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }
        String url = Constants.BASE_LOCAL__URL + "getProductList";
        JSONObject paramsJsonObj = new JSONObject();
        try {

            paramsJsonObj.put("id", GlobalClass.comapnyId);



            Log.d("URL :- ", url);
            Log.d("paramsJsonObj :- ", paramsJsonObj.toString());
            JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, url, paramsJsonObj, this.createRequestSuccessListener(5), this.createRequestErrorListener(System.currentTimeMillis()));
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

    }


    private Response.Listener<JSONObject> createRequestSuccessListener(final int responseCode) throws Exception {
        Response.Listener<JSONObject> listenerObj = null;
        //    if (requestCode == REQUEST_LOGIN) {

        listenerObj = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (responseCode == 5) {
                        Log.d("Res", response.toString());
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        if (response.getString("code").equals(Integer.toString(206))) {

                            // GlobalClass.lastName = response.optString("userrole");
                            //Save to prefrence
                            JSONArray contentList = response.getJSONArray("contentList");
                            for (int i = 0; i < contentList.length(); i++) {
                                JSONObject element = contentList.getJSONObject(i);
                                String colourCode = element.getString("colourCode");
                                String productName = element.getString("productName");


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
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
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
