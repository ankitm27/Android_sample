package com.cbat.cbat.ui.services.sales.details_sales;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cbat.cbat.R;
import com.cbat.cbat.adapter.TableFixHeaderAdapter;
import com.cbat.cbat.adapter.TableFixHeadersAdapterFactory;
import com.cbat.cbat.adapter.original_sortable.ItemSortable;
import com.cbat.cbat.adapter.original_sortable.NexusWithImage;
import com.cbat.cbat.adapter.original_sortable.OriginalBodyCellViewGroup;
import com.cbat.cbat.adapter.original_sortable.OriginalFirstBodyCellViewGroup;
import com.cbat.cbat.adapter.original_sortable.OriginalSortableTableFixHeader;
import com.cbat.cbat.ui.navigation.InvoiceDetailActivity;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.Utility;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.inqbarna.tablefixheaders.TableFixHeaders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jp.gr.java_conf.androtaku.geomap.GeoMapView;


public class SalesTrendCustomerDetailFragment extends Fragment {
    String TAG="SalesTrendCustomerDetailFragment";
    private LayoutInflater layoutInflater;
    private TableFixHeaders tableFixHeaders;
    private TableFixHeadersAdapterFactory tableFixHeadersAdapterFactory;
    GeoMapView geoMapView;
    LinearLayout totalSalesLayout;
    LinearLayout customerChartLayout;
    LinearLayout barchats;
    View chartLayout;
    private LineChart chart;
    protected Typeface tfLight;
    String stateName;
    private ProgressDialog progressDialog;
    Map<String,Double> cutomerSalesMap;
    Map<String,JSONArray> customerFullList;
    JSONArray fullSaleData=new JSONArray();
     TabLayout tabLayout;
    ImageView crImage;
    ImageView lakhImage;

    ImageView thImage;
    ImageView top5Image;
    ImageView top10Image;
    ImageView top15Image;
    ImageView allImage;
    Map<String,JSONArray> customerDetailMap= new HashMap<>();
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
    public static SalesTrendCustomerDetailFragment newInstance() {
        return new SalesTrendCustomerDetailFragment();
    }


    //MPLine
    private LineChart lineChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        //stateName = getArguments().getString("stateName");
        //GlobalClass.stateName = getArguments().getString("stateName");
        if (GlobalClass.customerSalesTrendFullList != null && !GlobalClass.customerSalesTrendFullList.isEmpty()) {
            Log.d(TAG, "Other Size >> " + GlobalClass.customerSalesTrendFullList.size());
            customerFullList = GlobalClass.customerSalesTrendFullList;

        } else {
            customerFullList = new HashMap<>();
        }
        tfLight = Typeface.createFromAsset(getActivity().getAssets(), "arial.ttf");

        View v = inflater.inflate(R.layout.sales_trend_customer_detail_fragment, container, false);
        //heighSetting( v );
        tableFixHeaders = (TableFixHeaders) v.findViewById(R.id.tableTrendfixheaders);
       // tableFixHeaders = new TableFixHeaders(getContext());
       tableFixHeadersAdapterFactory = new TableFixHeadersAdapterFactory(getContext());
        createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE,getCustomerSixMBody());
       // final ProgressBar progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
       // final FrameLayout totalSalesContainer = (FrameLayout)v.findViewById(R.id.totalSalesContainer);
        totalSalesLayout=(LinearLayout)v.findViewById(R.id.totalSalesTrendLayout);
        customerChartLayout=(LinearLayout)v.findViewById(R.id.customerChartTrendLayout);
        chartLayout = layoutInflater.inflate(R.layout.chart_tab_layout, customerChartLayout, false);
         tabLayout = (TabLayout) chartLayout.findViewById(R.id.chart_filter_tabs);
        barchats = (LinearLayout) chartLayout.findViewById(R.id.chart_container);
         //fullSaleData= customerFullList.get((customerFullList.size()-1));
        if(!GlobalClass.customerSalesTrendFullList.isEmpty()) {
            fullSaleData = GlobalClass.customerSalesTrendFullList.get("totalStateSales");
        }else{
            fullSaleData= new JSONArray();
        }
        barChartCreate(tabLayout);

        crImage = v.findViewById(R.id.cr);
        crImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.currecyPrefix = "CR";
                if (getFragmentManager() != null) {
                    //  Fragment  frm=getFragmentManager().findFragmentByTag(TAG);
                    Fragment cutomerFragment = new SalesTrendCustomerDetailFragment();
                    //((CustomerDetailFragment) cutomerFragment).setMap(cutomerSalesMap);
                    //((CustomerDetailFragment) cutomerFragment).setFullMap(customerFullList);
                    // GlobalClass.customerFullList=customerFullList;
                    //Bundle bundle = new Bundle();
                    // bundle.putString("stateName", stateName);
                    //  GlobalClass.stateName=stateName;
                    // cutomerFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.layout_service_fragment_content_trend, cutomerFragment)
                            .commit();
                } else {
                    createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                    tabLayout.removeAllTabs();
                    barChartCreate(tabLayout);
                    resetCurrencyImage();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
                }

//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        lakhImage = v.findViewById(R.id.lakh);
        lakhImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.currecyPrefix = "LH";
                if (getFragmentManager() != null) {
                    //  Fragment  frm=getFragmentManager().findFragmentByTag(TAG);
                    Fragment cutomerFragment = new SalesTrendCustomerDetailFragment();
                    //((CustomerDetailFragment) cutomerFragment).setMap(cutomerSalesMap);
                    //((CustomerDetailFragment) cutomerFragment).setFullMap(customerFullList);
                    // GlobalClass.customerFullList=customerFullList;
                    //Bundle bundle = new Bundle();
                    // bundle.putString("stateName", stateName);
                    //  GlobalClass.stateName=stateName;
                    // cutomerFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.layout_service_fragment_content_trend, cutomerFragment)
                            .commit();
                } else {
                    createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                    tabLayout.removeAllTabs();
                    barChartCreate(tabLayout);
                    resetCurrencyImage();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
                }

//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        thImage = v.findViewById(R.id.th);
        thImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.currecyPrefix = "TH";
                if (getFragmentManager() != null) {
                    //  Fragment  frm=getFragmentManager().findFragmentByTag(TAG);
                    Fragment cutomerFragment = new SalesTrendCustomerDetailFragment();
                    //((CustomerDetailFragment) cutomerFragment).setMap(cutomerSalesMap);
                    //((CustomerDetailFragment) cutomerFragment).setFullMap(customerFullList);
                    // GlobalClass.customerFullList=customerFullList;
                    //Bundle bundle = new Bundle();
                    // bundle.putString("stateName", stateName);
                    //  GlobalClass.stateName=stateName;
                    // cutomerFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.layout_service_fragment_content_trend, cutomerFragment)
                            .commit();
                } else {
                    createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                    tabLayout.removeAllTabs();
                    barChartCreate(tabLayout);
                    resetCurrencyImage();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
                }

//                Toast.makeText(getContext(),
//
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        top5Image = v.findViewById(R.id.top5);
        top5Image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.sortingOn = 5;
                if (getFragmentManager() != null) {
                    //  Fragment  frm=getFragmentManager().findFragmentByTag(TAG);
                    Fragment cutomerFragment = new SalesTrendCustomerDetailFragment();
                    //((CustomerDetailFragment) cutomerFragment).setMap(cutomerSalesMap);
                    //((CustomerDetailFragment) cutomerFragment).setFullMap(customerFullList);
                    // GlobalClass.customerFullList=customerFullList;
                    //Bundle bundle = new Bundle();
                    // bundle.putString("stateName", stateName);
                    //  GlobalClass.stateName=stateName;
                    // cutomerFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.layout_service_fragment_content_trend, cutomerFragment)
                            .commit();
                } else {
                    createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                    fullSaleData = customerFullList.get(0);
                    //  barChartCreate(tabLayout);
                    resetShortingImage();
                    tabLayout.getTabAt(1).select();
                    setSalesTrendLineData(1, fullSaleData);
                    lineChart.animateX(2000);
                    lineChart.refreshDrawableState();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
                }

//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        top10Image = v.findViewById(R.id.top10);
        top10Image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.sortingOn = 10;
                if (getFragmentManager() != null) {
                    //  Fragment  frm=getFragmentManager().findFragmentByTag(TAG);
                    Fragment cutomerFragment = new SalesTrendCustomerDetailFragment();
                    //((CustomerDetailFragment) cutomerFragment).setMap(cutomerSalesMap);
                    //((CustomerDetailFragment) cutomerFragment).setFullMap(customerFullList);
                    // GlobalClass.customerFullList=customerFullList;
                    //Bundle bundle = new Bundle();
                    // bundle.putString("stateName", stateName);
                    //  GlobalClass.stateName=stateName;
                    // cutomerFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.layout_service_fragment_content_trend, cutomerFragment)
                            .commit();
                } else {
                    fullSaleData = customerFullList.get(0);
                    createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                    // barChartCreate(tabLayout);
                    resetShortingImage();
                    tabLayout.getTabAt(1).select();
                    setSalesTrendLineData(1, fullSaleData);
                    lineChart.animateX(2000);
                    lineChart.refreshDrawableState();
//                Toast.makeText(getContext(),
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
                }

//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        top15Image = v.findViewById(R.id.top15);
        top15Image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.sortingOn = 15;
                if (getFragmentManager() != null) {
                    //  Fragment  frm=getFragmentManager().findFragmentByTag(TAG);
                    Fragment cutomerFragment = new SalesTrendCustomerDetailFragment();
                    //((CustomerDetailFragment) cutomerFragment).setMap(cutomerSalesMap);
                    //((CustomerDetailFragment) cutomerFragment).setFullMap(customerFullList);
                    // GlobalClass.customerFullList=customerFullList;
                    //Bundle bundle = new Bundle();
                    // bundle.putString("stateName", stateName);
                    //  GlobalClass.stateName=stateName;
                    // cutomerFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.layout_service_fragment_content_trend, cutomerFragment)
                            .commit();
                } else {
                    fullSaleData = customerFullList.get(0);
                    createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                    // barChartCreate(tabLayout);
                    resetShortingImage();
                    tabLayout.getTabAt(1).select();
                    setSalesTrendLineData(1, fullSaleData);
                    lineChart.animateX(2000);
                    lineChart.refreshDrawableState();
//                Toast.makeText(getContext(),
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
                }

//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        allImage = v.findViewById(R.id.alltop);
        allImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.sortingOn = -1;
                if (getFragmentManager() != null) {
                    //  Fragment  frm=getFragmentManager().findFragmentByTag(TAG);
                    Fragment cutomerFragment = new SalesTrendCustomerDetailFragment();
                    //((CustomerDetailFragment) cutomerFragment).setMap(cutomerSalesMap);
                    //((CustomerDetailFragment) cutomerFragment).setFullMap(customerFullList);
                    // GlobalClass.customerFullList=customerFullList;
                    //Bundle bundle = new Bundle();
                    // bundle.putString("stateName", stateName);
                    //  GlobalClass.stateName=stateName;
                    // cutomerFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.layout_service_fragment_content_trend, cutomerFragment)
                            .commit();
                } else {
                    fullSaleData = customerFullList.get("totalStateSales");
                    createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                    // barChartCreate(tabLayout);
                    resetShortingImage();
                    tabLayout.getTabAt(1).select();
                    setSalesTrendLineData(1, fullSaleData);
                    lineChart.animateX(2000);
                    lineChart.refreshDrawableState();
//                Toast.makeText(getContext(),
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
                }

//                //Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        resetCurrencyImage();
        resetShortingImage();
        return v;
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

    public void resetShortingImage() {
        if (GlobalClass.sortingOn <= -1) {
            top5Image.setImageDrawable(getResources().getDrawable(R.drawable.top_5_white));
            top10Image.setImageDrawable(getResources().getDrawable(R.drawable.top_10_white));
            top15Image.setImageDrawable(getResources().getDrawable(R.drawable.top_15_white));
            allImage.setImageDrawable(getResources().getDrawable(R.drawable.top_all));

        } else if (GlobalClass.sortingOn == 5) {
            top5Image.setImageDrawable(getResources().getDrawable(R.drawable.top_5));
            top10Image.setImageDrawable(getResources().getDrawable(R.drawable.top_10_white));
            top15Image.setImageDrawable(getResources().getDrawable(R.drawable.top_15_white));
            allImage.setImageDrawable(getResources().getDrawable(R.drawable.top_all_white));

        } else if (GlobalClass.sortingOn == 10) {
            top5Image.setImageDrawable(getResources().getDrawable(R.drawable.top_5_white));
            top10Image.setImageDrawable(getResources().getDrawable(R.drawable.top_10));
            top15Image.setImageDrawable(getResources().getDrawable(R.drawable.top_15_white));
            allImage.setImageDrawable(getResources().getDrawable(R.drawable.top_all_white));
        } else if (GlobalClass.sortingOn == 15) {
            top5Image.setImageDrawable(getResources().getDrawable(R.drawable.top_5_white));
            top10Image.setImageDrawable(getResources().getDrawable(R.drawable.top_10_white));
            top15Image.setImageDrawable(getResources().getDrawable(R.drawable.top_15));
            allImage.setImageDrawable(getResources().getDrawable(R.drawable.top_all_white));
        }
    }

    public void barChartCreate(final TabLayout tabLayout){
        try{
            customerChartLayout.removeAllViews();

            //final TabLayout tabLayout = (TabLayout) chartLayout.findViewById(R.id.chart_filter_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Day"));
        tabLayout.addTab(tabLayout.newTab().setText("Mth"));
        tabLayout.addTab(tabLayout.newTab().setText("Qtr"));
       // tabLayout.addTab(tabLayout.newTab().setText("YTD"));
       // tabLayout.addTab(tabLayout.newTab().setText("LTM"));

       // tabLayout.addTab(tabLayout.newTab().setText("5Y"));
        lineChart=new LineChart(getActivity());
        ViewGroup.LayoutParams lp= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        lineChart.setLayoutParams(lp);
        lineChart.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        lineChart.setMinimumHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabLayout.getSelectedTabPosition()==2) {
                    setSalesTrendLineData(tabLayout.getSelectedTabPosition()+1,fullSaleData);
                }else{
                    setSalesTrendLineData(tabLayout.getSelectedTabPosition(),fullSaleData);
                }
                lineChart.animateX(2000);
                lineChart.refreshDrawableState();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

            lineChartSetup();
            tabLayout.getTabAt(1).select();
            setSalesTrendLineData(1,fullSaleData);
            barchats.addView(lineChart);
        customerChartLayout.addView(chartLayout);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void lineChartSetup() throws JSONException {
//        lineChart = v.findViewById(R.id.lineChart);
//        lineChart.setOnChartValueSelectedListener(this);
        //lineChart.setDrawGridBackground(true);

        // no description text
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        lineChart.getDescription().setEnabled(true);
        lineChart.getDescription().setPosition(1f,0f);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getXAxis().setEnabled(true);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        // lineChart.getAxisLeft().setTextColor(activity.getResources().getColor(R.color.gr));
        lineChart.getAxisLeft().setAxisLineColor(getResources().getColor(R.color.gray));
        lineChart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            // private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
            @Override
            public String getFormattedValue(float value) {
                return value+" "+GlobalClass.currecyPrefix;
            }
        });
//        lineChart.setTouchEnabled(false);
//
//        // enable scaling and dragging
//        lineChart.setDragEnabled(true);
//        lineChart.setScaleEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);
        lineChart.setBorderColor(getResources().getColor(R.color.textColorSecondary));
        // set an alternative background color
        //lineChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it

        // // restrain the maximum scale-out factor
        // chart.setScaleMinima(3f, 3f);
        //
        // // center the view to a specific position inside the chart
        // chart.centerViewPort(10, 50);
        //LineDataSet
        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();
        l.setEnabled(false);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(true);

//        YAxis leftAxis = lineChart.getAxisLeft();
//        leftAxis.setAxisMaxValue(200f);
//        leftAxis.setLabelCount(6,false);
//        leftAxis.setAxisMinValue(0f);
//        leftAxis.setDrawAxisLine(false);
//        leftAxis.setDrawZeroLine(false);
//        leftAxis.setDrawGridLines(false);
        // l.setEnabled(false);
        // don't forget to refresh the drawing
        lineChart.invalidate();

        // setSalesTrendLineData((JSONArray) salesTrendLineData.get("day"));
        lineChart.animateXY(2000, 2000);
    }

    private void setSalesTrendLineData(int tabType,JSONArray fulldata){
        if (fulldata!=null && fulldata.length()>0) {
        try {

                JSONArray data = fulldata.getJSONArray(tabType);

                ArrayList<Entry> entries = new ArrayList<>();
                final Map<Float, String> xAxisLable = new HashMap<>();
                float limitNumber = 0;
                float yaxisMax = 0;
                xAxisLable.clear();
                Log.d(TAG, "data.length() Trend> " + data.length());
                for (int i = 0; i < data.length(); i++) {
                    JSONObject element = (JSONObject) data.get(i);
                    float xVal = (float) (i);
                    int yVal = element.getInt("value");
                    String lableName = element.getString("name");

                    BigDecimal number1 = new BigDecimal(yVal);
                    xAxisLable.put(xVal, lableName);
                    float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                    if (yaxisMax < debitFloat) {
                        yaxisMax = debitFloat;
                    }
                    if (i == data.length()) {
                        limitNumber = Math.round(debitFloat) % 2 == 0 ? +Math.round(debitFloat) + 2 : Math.round(debitFloat) + 1;
                    }
                    // Log.d(TAG,"lableName > "+lableName);
                    // Log.d(TAG,"debitFloat > "+debitFloat);

                    entries.add(new Entry(xVal, Utility.decimal2PalceAsInput(Math.abs(debitFloat))));
                }
                yaxisMax = Math.round(yaxisMax) % 2 == 0 ? +Math.round(yaxisMax) + 2 : Math.round(yaxisMax) + 1;
                yaxisMax = yaxisMax * 2;
                //Log.d(TAG,"yaxisMax >> "+yaxisMax);

                // sort by x-value
                Collections.sort(entries, new EntryXComparator());

//        // create a dat aset and give it a type
                LineDataSet set1 = new LineDataSet(entries, "");
                if (entries.size() <= 1) {
                    set1.setDrawCircles(true);
                }
//        set1.setDrawCircles(false);
//        set1.setDrawCircleHole(false);
                set1.setLineWidth(.5f);
//        set1.setDrawHorizontalHighlightIndicator(true);
//        set1.setDrawHighlightIndicators(true);
                set1.setCubicIntensity(0.2f);
                //  set1.setColor(Color.rgb(255, 241, 46));
//        set1.setColor(activity.getResources().getColor(R.color.black));
//        set1.setFillAlpha(255);
//        set1.setValueTextColor(activity.getResources().getColor(R.color.chart5));
//        set1.setDrawValues(false);
                set1.setDrawIcons(false);
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                // draw dashed line
                //  set1.enableDashedLine(10f, 5f, 0f);
                set1.disableDashedLine();
                // black lines and points
                set1.setColor(Color.BLACK);
                // set1.setCircleColor(Color.BLACK);

                // line thickness and point size
                // set1.setLineWidth(0f);
                // set1.setCircleRadius(0f);

                // draw points as solid circles
                set1.setDrawCircleHole(false);
                set1.setDrawCircles(false);

                // customize legend entry
                set1.setFormLineWidth(0f);
                //set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                // set1.setFormSize(15.f);

                // text size of values
                set1.setValueTextSize(9f);

                // draw selection line as dashed
                //set1.enableDashedHighlightLine(10f, 5f, 0f);

                // set the filled area
                set1.setDrawFilled(true);
                set1.setDrawValues(false);

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1); // add the data sets
                //set1.setFillColor(Color.GREEN);
                //set1.setHighLightColor(getResources().getColor(R.color.chart5));
                //set1.setCircleRadius(4f);

                // create a data object with the data sets
                LineData lineData = new LineData(set1);

                // set data
                lineChart.setData(lineData);
                List<ILineDataSet> sets = lineChart.getData().getDataSets();
                //lineChart.getAxisLeft().setAxisMaximum(0);
                //lineChart.getAxisLeft().setAxisMinimum(limitNumber);
                YAxis leftAxis = lineChart.getAxisLeft();
                //leftAxis.setAxisMaxValue(yaxisMax);
                leftAxis.setLabelCount(6, false);
                leftAxis.setAxisMinValue(0f);
                leftAxis.setDrawAxisLine(false);
                leftAxis.setDrawZeroLine(false);
                leftAxis.setDrawGridLines(true);
                leftAxis.setGridColor(getResources().getColor(R.color.gray));
                leftAxis.setValueFormatter(new ValueFormatter() {
                // private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
                @Override
                public String getFormattedValue(float value) {
                    DecimalFormat mFormat = new DecimalFormat("###.#");
                    System.out.println("value >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+value);
                    //float debitFloat = value / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                    System.out.println("GlobalClass.currecyPrefix >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>."+GlobalClass.currecyPrefix);
                    return mFormat.format(value) + " " + GlobalClass.currecyPrefix;
                }
            });
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setValueFormatter(new ValueFormatter() {
                    // private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);

                    @Override
                    public String getFormattedValue(float value) {
                        String lable = xAxisLable.get(value);
                        //Log.d(TAG, "lable >><< " + lable);
                        //long millis = TimeUnit.HOURS.toMillis((long) value);
                        // return mFormat.format(new Date(millis));
                        return lable != null ? lable : "";

                    }
                });
                xAxis.setLabelCount(6);
                set1.setDrawFilled(false);
                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
//            if (set.isDrawFilledEnabled())
//                set.setDrawFilled(false);
//            else
//                set.setDrawFilled(true);
//        }
                    set1.setDrawFilled(true);
                    if (Utils.getSDKInt() >= 18) {
                        // fill drawable only supported on api level 18 and above
                        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_graph);
                        set1.setFillDrawable(drawable);
                    } else {
                        set1.setFillColor(Color.BLACK);
                    }
                }
                lineChart.invalidate();
                lineChart.fitScreen();
                lineChart.setScaleEnabled(false);

                Log.d(TAG, "data.length():" + data.length());
                if (data.length() > 12) {
                    lineChart.setVisibleXRangeMaximum(30); // allow 10 values to be displayed at once on the x-axis, not more
                    lineChart.moveViewToX(30);
                    xAxis.setLabelCount(4);
                    lineChart.setTouchEnabled(true);
                    lineChart.setDragEnabled(true);
                    // enable scaling and dragging

                } else {
                    xAxis.setLabelCount(6);
                    lineChart.setTouchEnabled(false);
                    lineChart.setDragEnabled(false);
                }
            }catch(JSONException ej){
                ej.printStackTrace();
            }
        catch(Exception e){
                e.printStackTrace();
            }
        }
        //return data;
    }



    private void createTable(int type,List<NexusWithImage> customerBody) {

       //BaseTableAdapter originalSortableTableFixHeader=tableFixHeadersAdapterFactory.getAdapter(type);
        OriginalSortableTableFixHeader originalSortableTableFixHeader=new OriginalSortableTableFixHeader(getContext());
        originalSortableTableFixHeader.setHeader(getCustomerHeader());
        originalSortableTableFixHeader.setBody(customerBody);
        originalSortableTableFixHeader.setClickListenerBody(setClickListenerBody);
        originalSortableTableFixHeader.setClickListenerFirstBody(setClickListenerFirstBody);
        tableFixHeaders.setAdapter(originalSortableTableFixHeader.getInstance());
        originalSortableTableFixHeader.getInstance().notifyDataSetChanged();


        //tableFixHeaders.setAdapter(tableFixHeadersAdapterFactory.getAdapter(type));
    }

    public ItemSortable[] getCustomerHeader(){
         ItemSortable headers[] = {
            new ItemSortable("Total("+GlobalClass.currecyPrefix+")"),
                 new ItemSortable("% Contri."),
                 new ItemSortable("Apr"),
                 new ItemSortable("May"),
                 new ItemSortable("Jun"),
                 new ItemSortable("Jul"),
                 new ItemSortable("Aug"),
                 new ItemSortable("Sep"),
                 new ItemSortable("Oct"),
                 new ItemSortable("Nov"),
                 new ItemSortable("Dec"),
                 new ItemSortable("Jan"),
                 new ItemSortable("Feb"),
                 new ItemSortable("Mar"),
                 new ItemSortable("Customer"),

        };
         return headers;
    }

    public List<NexusWithImage> getCustomerSixMBody()  {
        List<Integer> resImages = Arrays.asList();

        List<NexusWithImage> items = new ArrayList<>();
        String type = "Total Sales";
       // items.add(new NexusWithImage(type, resImages));
      //  GlobalClass.salesStateCustomer.clear();
//        if(GlobalClass.customerFullList!=null && !GlobalClass.customerFullList.isEmpty()) {
//            customerFullList = GlobalClass.customerFullList;
//        }
        if(GlobalClass.customerSalesTrendFullList!=null && !GlobalClass.customerSalesTrendFullList.isEmpty()){
        int count = 0;
            Float[] dataTableTotal = null;
            String[] dataTableT = null;
            boolean totalFlag = true;
        for(Map.Entry<String, JSONArray> set:GlobalClass.customerSalesTrendFullList.entrySet()) {
            try {
                if (count < GlobalClass.sortingOn || GlobalClass.sortingOn == -1) {
                    Log.d(TAG, "Key >" + set.getKey());
                    if (!set.getKey().equalsIgnoreCase("totalStateSales")) {
                        JSONArray dataEveryMonthly = set.getValue().getJSONArray(1);
                        JSONArray dataYearly = set.getValue().getJSONArray(3);
                        JSONArray contriData = set.getValue().getJSONArray(4);
                        JSONArray dataCustomer =  set.getValue().getJSONArray(5);
                        JSONArray dataMonthly = set.getValue().getJSONArray(6);
                        String[] dataTable = new String[dataMonthly.length() + 4];
                        if(totalFlag) {
                            dataTableTotal = new Float[dataMonthly.length() + 4];
                        }
                        dataTableT= new String[dataMonthly.length() + 4];
                        JSONObject elementYearly;
                        boolean yearFlag = true;
                        boolean contriFlag = true;

                        float totalSales = 0;
                        int j = 0;
                        int k = 2;
                        for (int i = 0; i < dataMonthly.length(); i++) {
                            if (yearFlag) {
                                elementYearly = dataYearly.getJSONObject(i);
                                yearFlag = false;
                                int yVal = elementYearly.getInt("value");
                                BigDecimal number1 = new BigDecimal(yVal);
                                totalSales = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                Log.d(TAG, "set.getKey() >" + set.getKey());
                                Log.d(TAG, "totalSales >" + totalSales);
                                dataTable[j++] = String.valueOf(set.getKey());
                                //dataTable[j++] = Utility.decimal2Palce(Math.abs(totalSales));
                                String moneyString = formatter.format(Math.abs(totalSales));
                                dataTableTotal[0] =(dataTableTotal[0]!=null?dataTableTotal[0]:new Float(0))+new Float(totalSales);
                                dataTable[j++]=moneyString;
                                //  GlobalClass.salesStateCustomer.add(set.getKey());
                            }
                            if (contriFlag) {
                                elementYearly = contriData.getJSONObject(i);
                                contriFlag = false;
                                double yVal = elementYearly.getDouble("value");
                                //BigDecimal number1 = new BigDecimal(yVal);
                                //totalSales = number1.floatValue() / 10000000;
                                //totalSales = number1.floatValue(); // GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                Log.d(TAG, "set.getKey() >" + set.getKey());
                                Log.d(TAG, "yVal >" + yVal);
                                // dataTable[j++] = String.valueOf(set.getKey());
                                dataTableTotal[1] =(dataTableTotal[1]!=null?dataTableTotal[1]:new Float(0))+new Float(yVal);
                                dataTable[j++] = Utility.decimal2Palce(Math.abs(yVal));
                            }

                            JSONObject element = dataMonthly.getJSONObject(i);
                            int yVal = element.getInt("value");
                            BigDecimal number1 = new BigDecimal(yVal);
                            float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);;
                           // dataTable[j++] = Utility.decimal2Palce(Math.abs(debitFloat));
                            Log.d(TAG, dataTableTotal[k]+" <> "+k+" dataTableTotla[k] >##########" + number1.floatValue());
                            dataTableTotal[k] =(dataTableTotal[k]!=null?dataTableTotal[k]:new Float(0))+number1.floatValue();
                            k++;
                            String moneyString = formatter.format(Math.abs(debitFloat));
                            dataTable[j++] =moneyString;

                        }
                        customerDetailMap.put(dataTable[0],dataCustomer);
                        dataTable[j++]=dataCustomer.getJSONObject(0).getJSONObject("customer").getString("address");
                        items.add(new NexusWithImage(type, dataTable));
                        Log.d(TAG, "dataTable >" + dataTable.toString());
                        Log.d(TAG, "dataTable length >" + dataTable.length);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            count++;
            totalFlag=false;

        }

            Log.d(TAG, "dataTableTotla >" + dataTableTotal.toString());
            Log.d(TAG, "dataTable length >" + dataTableTotal.length);
            if( GlobalClass.sortingOn >0) {
                int l=1;
                dataTableT[0]="Total";
//                dataTableT[1]="100";
//                dataTableT[2]="2";
                for(Float f:dataTableTotal){
                    if(l<dataTableTotal.length && l>2) {
                        dataTableT[l] = formatter.format(Utility.decimal2PalceAsInput(Math.abs((f!=null?f:0)/ GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix))));//String.valueOf(f);
                        l++;
                    }else if(l==1) {
                        Log.d(TAG, "F>>>>>>" + f);
                        dataTableT[l]=formatter.format(Utility.decimal2PalceAsInput(Math.abs(f!=null?f:0)));
                        l++;
                    }else if(l==2) {
                        Log.d(TAG, "F>>>>>>" + f);
                        dataTableT[l]=String.valueOf(Utility.decimal2PalceAsInput(Math.abs(f!=null?f:0)));
                        l++;
                    }

                }
                items.add(new NexusWithImage(type, dataTableT));
            }
        if (GlobalClass.sortingOn == -1) {
            try {
                JSONArray setVal = GlobalClass.customerSalesTrendFullList.get("totalStateSales");
                JSONArray dataMonthly = setVal.getJSONArray(2);
                JSONArray dataYearly = setVal.getJSONArray(4);
                String[] dataTable = new String[dataMonthly.length() + 4];
                JSONObject elementYearly;
                boolean yearFlag = true;
                float totalSales = 0;
                int j = 0;
                for (int i = 0; i < dataMonthly.length(); i++) {
                    if (yearFlag) {
                        elementYearly = dataYearly.getJSONObject(i);
                        yearFlag = false;
                        int yVal = elementYearly.getInt("value");
                        BigDecimal number1 = new BigDecimal(yVal);
                        totalSales = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);;
                        //Log.d(TAG, "set.getKey() >" + totalStateSales);
                        Log.d(TAG, "totalSales >" + totalSales);
                        dataTable[j++] = String.valueOf("Total Sales");
                       // dataTable[j++] = Utility.decimal2Palce(Math.abs(totalSales));
                        String moneyString = formatter.format(Math.abs(totalSales));
                        dataTable[j++] =moneyString;
                        dataTable[j++] = String.valueOf("100");
                    }

                    JSONObject element = dataMonthly.getJSONObject(i);
                    int yVal = element.getInt("value");
                    BigDecimal number1 = new BigDecimal(yVal);
                    float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);;
                    Log.d(TAG, "debitFloat >" + debitFloat);
                   // dataTable[j++] = Utility.decimal2Palce(Math.abs(debitFloat));
                    String moneyString = formatter.format(Math.abs(debitFloat));
                    dataTable[j++] =moneyString;

                }
                items.add(new NexusWithImage(type, dataTable));
                Log.d(TAG, "dataTable >" + dataTable.toString());
                Log.d(TAG, "dataTable length >" + dataTable.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        }
        return items;
    }



    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup> setClickListenerBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup>() {
            @Override
            public void onClickItem(NexusWithImage item, OriginalBodyCellViewGroup viewGroup, int row, int column) {

                //Snackbar.make(viewGroup, "Yes we do it " + item.data[0] + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();

                //Snackbar.make(viewGroup, "Yes we do it " + item.data[column + 1] + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();
                Intent myIntent = new Intent(getActivity(), InvoiceDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("StateName", stateName);
//                bundle.putString("BackFrgment", "TotalSale");
//                bundle.putString("CurrentFrgment", stateName);
//                bundle.putString("CurrentFrgmentMain", item.data[0]);
//                bundle.putString("CutomerName", item.data[0]);
//                bundle.putString("Title", "Invoices for "+item.data[0]);
//                myIntent.putExtras(bundle);
                GlobalClass.stateName="all";
                GlobalClass.currentFrgmentMain=item.data[0];
                GlobalClass.cutomerName=item.data[0];
               // GlobalClass.title="Invoices for "+item.data[0];
                //set month
                System.out.println("Month >>>"+ GlobalClass.month);
                System.out.println("column>>>>>>>> >>>"+ column);
                if(column>=2) {
                    GlobalClass.month = GlobalClass.monthList.get(column - 2);
                    GlobalClass.title = "Invoices of " +GlobalClass.month+ " for " + item.data[0];
                    System.out.println("Month >>>"+ GlobalClass.month);
                }else{
                    GlobalClass.month="";
                    GlobalClass.title = "Invoices for " + item.data[0];
                }
                startActivity(myIntent);
            }
        };

    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup> setClickListenerFirstBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup>() {
        @Override
        public void onClickItem(NexusWithImage item, OriginalFirstBodyCellViewGroup viewGroup, int row, int column) {


            if(item.data[column + 1].equalsIgnoreCase("Total Sales")) {
                fullSaleData = GlobalClass.customerSalesTrendFullList.get("totalStateSales");
                Snackbar snackbar = Snackbar.make(viewGroup,   item.data[column + 1] , Snackbar.LENGTH_LONG).setAction("Action", null);
                //  customerFullList.get(item.data[column + 1] );
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.chart4));
               // snackbar.show();
            }else{
                fullSaleData = GlobalClass.customerSalesTrendFullList.get(item.data[column + 1]);
                Snackbar snackbar = Snackbar.make(viewGroup, "Name: " + item.data[column + 1]+"\nAddress: "+item.data[15], Snackbar.LENGTH_LONG).setAction("Action", null);
                //  customerFullList.get(item.data[column + 1] );
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                textView.setMaxLines(5);
                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.chart4));
                sbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
               // snackbar.show();

                JSONArray dataCustomer=customerDetailMap.get(item.data[column + 1]);
                try {

                    View view = getLayoutInflater().inflate(R.layout.cutomer_profile_detail, null, false);
                    Button closeBtn = (Button) view.findViewById(R.id.closeWin);
                    TextView nameText = (TextView) view.findViewById(R.id.nameText);
                    nameText.setText(item.data[column + 1]);
                    TextView addressText = (TextView) view.findViewById(R.id.addressText);
                    addressText.setText( dataCustomer.getJSONObject(0).getJSONObject("customer").getString("address"));
                    TextView stateText = (TextView) view.findViewById(R.id.stateText);
                    stateText.setText(dataCustomer.getJSONObject(0).getJSONObject("customer").getString("state"));
                    TextView pincodeText = (TextView) view.findViewById(R.id.pincodeText);
                    pincodeText.setText(dataCustomer.getJSONObject(0).getJSONObject("customer").getString("pincode"));
                    TextView panText = (TextView) view.findViewById(R.id.panText);
                    panText.setText(dataCustomer.getJSONObject(0).getJSONObject("customer").getString("panapplicablefrom"));
                 //   TextView registrationTypeText = (TextView) view.findViewById(R.id.registrationTypeText);
                 //   registrationTypeText.setText(dataCustomer.getJSONObject(0).getJSONObject("customer").getString("gstregistrationtype"));
                    TextView gstText = (TextView) view.findViewById(R.id.gstText);
                    gstText.setText(dataCustomer.getJSONObject(0).getJSONObject("customer").getString("partygstin"));
                    TextView creditPeroidText = (TextView) view.findViewById(R.id.creditPeroidText);
                    String creditPeroidTxt=dataCustomer.getJSONObject(0).getJSONObject("customer").getString("creditPeroid");
                    creditPeroidText.setText(creditPeroidTxt!=null && !creditPeroidTxt.isEmpty()?creditPeroidTxt +"Days":"");
                    TextView creditLimitText = (TextView) view.findViewById(R.id.creditLimitText);
                    String creditLimitTxt=dataCustomer.getJSONObject(0).getJSONObject("customer").getString("creditAmount");
                    //String moneyString = formatter.format(creditLimitText);
                    creditLimitText.setText(creditLimitTxt!=null && !creditLimitTxt.isEmpty()?formatter.format(Math.abs(Integer.parseInt(creditLimitTxt))):"");

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
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            tabLayout.getTabAt(1).select();

                setSalesTrendLineData(1,fullSaleData);

            lineChart.animateX(2000);
            lineChart.refreshDrawableState();

        }
    };


//    private void setSalesData(int tabType, JSONArray fullSaleData) {
//        final  Map<Float,String> xAxisLable=new HashMap<>();
//
//       //JSONArray fullSaleData= customerFullList.get("totalStateSales");
//        //JSONArray fullSaleData= customerFullList.get(position);
//        try {
//            JSONArray tabData=fullSaleData.getJSONArray(tabType);
//
//        ArrayList<BarEntry> values = new ArrayList<>();
//
//        for (int i = 0; i < tabData.length(); i++) {
//            JSONObject element=tabData.getJSONObject(i);
//            float xVal = (float) (i);
//            int yVal = element.getInt("value");
//            String lableName=element.getString("name");
//
//            BigDecimal number1 = new BigDecimal(yVal);
//            xAxisLable.put(xVal,lableName);
//            float debitFloat = number1.floatValue() / 10000000;
//
//            Log.d(TAG,"lableName > "+lableName);
//            Log.d(TAG,"debitFloat > "+debitFloat);
//
//             values.add(new BarEntry(xVal,Math.abs(debitFloat)));
//
//        }
//
//        BarDataSet set1;
//
//
//            set1 = new BarDataSet(values, "");
//            set1.setDrawIcons(false);
//            set1.setColor(getActivity().getResources().getColor(R.color.chart1));
//            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//            dataSets.add(set1);
//            BarData data = new BarData(dataSets);
//            data.setValueTextSize(10f);
//            data.setValueTypeface(tfLight);
//            data.setBarWidth(.5f);
//            data.setDrawValues(false);
//            XAxis xAxis = chart.getXAxis();
//            xAxis.setValueFormatter(new ValueFormatter() {
//                // private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
//
//                @Override
//                public String getFormattedValue(float value) {
//                    String lable = xAxisLable.get(value);
//                    Log.d(TAG, "lable >><< " + lable);
//                    //  long millis = TimeUnit.HOURS.toMillis((long) value);
//                    //  return mFormat.format(new Date(millis));
//                    return lable!=null?lable:"";
//                    // return value+"";
//
//                }
//            });
//            chart.setData(data);
//        //}
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//    private void loadFragment(Fragment fragment) {
//        Intent myIntent = new Intent(getActivity(), CustomerDetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("BackFrgment", "Services");
//        bundle.putString("CurrentFrgment", "Sales");
//        bundle.putString("Title", "Sales");
//        myIntent.putExtras(bundle);
//        startActivity(myIntent);
//
//    }



}