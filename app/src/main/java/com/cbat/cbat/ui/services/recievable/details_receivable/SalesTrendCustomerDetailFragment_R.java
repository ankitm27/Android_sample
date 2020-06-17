package com.cbat.cbat.ui.services.recievable.details_receivable;

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
import android.widget.LinearLayout;

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
import com.cbat.cbat.ui.navigation.CustomerDetailActivity;
import com.cbat.cbat.ui.navigation.InvoiceDetailActivity;
import com.cbat.cbat.util.DayAxisValueFormatter;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.MyValueFormatter;
import com.cbat.cbat.util.PichartMarkerView;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.inqbarna.tablefixheaders.TableFixHeaders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.androtaku.geomap.GeoMapView;


public class SalesTrendCustomerDetailFragment_R extends Fragment {
    String TAG="SalesTrendCustomerDetailFragment_R";
    private LayoutInflater layoutInflater;
    private TableFixHeaders tableFixHeaders;
    private TableFixHeadersAdapterFactory tableFixHeadersAdapterFactory;
    GeoMapView geoMapView;
    LinearLayout totalSalesLayout;
    LinearLayout customerChartLayout;
    LinearLayout barchats;
    View chartLayout;
    private BarChart chart;
    protected Typeface tfLight;
    String stateName;
    private ProgressDialog progressDialog;
    Map<String,Double> cutomerSalesMap;
    Map<String,JSONArray> customerFullList;
    JSONArray fullSaleData=new JSONArray();
     TabLayout tabLayout;
    public static SalesTrendCustomerDetailFragment_R newInstance() {
        return new SalesTrendCustomerDetailFragment_R();
    }


    //MPLine
    private LineChart lineChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        //stateName = getArguments().getString("stateName");
        //GlobalClass.stateName = getArguments().getString("stateName");

        tfLight = Typeface.createFromAsset(getActivity().getAssets(), "arial.ttf");

        View v = inflater.inflate(R.layout.customer_detail_fragment, container, false);
        //heighSetting( v );
        tableFixHeaders = (TableFixHeaders) v.findViewById(R.id.tablefixheaders);
       // tableFixHeaders = new TableFixHeaders(getContext());
       tableFixHeadersAdapterFactory = new TableFixHeadersAdapterFactory(getContext());
        createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE,getCustomerSixMBody());
       // final ProgressBar progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        final FrameLayout totalSalesContainer = (FrameLayout)v.findViewById(R.id.totalSalesContainer);
        totalSalesLayout=(LinearLayout)v.findViewById(R.id.totalSalesLayout);
        customerChartLayout=(LinearLayout)v.findViewById(R.id.customerChartLayout);
        chartLayout = layoutInflater.inflate(R.layout.chart_tab_layout, customerChartLayout, false);
         tabLayout = (TabLayout) chartLayout.findViewById(R.id.chart_filter_tabs);
        barchats = (LinearLayout) chartLayout.findViewById(R.id.chart_container);
         //fullSaleData= customerFullList.get((customerFullList.size()-1));
        if(!GlobalClass.customerFullList.isEmpty()) {
            fullSaleData = GlobalClass.customerFullList.get("totalStateSales");
        }else{
            fullSaleData= new JSONArray();
        }
        barChartCreate(tabLayout);

       // getTotalSaleCutomerWise();

        return v;
    }


    public void barChartCreate(final TabLayout tabLayout){
        //final TabLayout tabLayout = (TabLayout) chartLayout.findViewById(R.id.chart_filter_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("1D"));
        tabLayout.addTab(tabLayout.newTab().setText("1M"));
        tabLayout.addTab(tabLayout.newTab().setText("3M"));
        tabLayout.addTab(tabLayout.newTab().setText("YTD"));
        tabLayout.addTab(tabLayout.newTab().setText("3YTD"));

        chart=new BarChart(getActivity());
        ViewGroup.LayoutParams lp= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        chart.setLayoutParams(lp);
        chart.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        chart.setMinimumHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(false);
        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setDoubleTapToZoomEnabled(false);
        chart.setPinchZoom(false);
        chart.setEnabled(false);
        chart.setDrawGridBackground(false);
        //chart.setDrawYLabels(false);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getXAxis().setDrawGridLines(true);
        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart,2019);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(6);
       // xAxis.setValueFormatter(xAxisFormatter);
//        xAxis.setValueFormatter(new ValueFormatter() {
//            // private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
//
//            @Override
//            public String getFormattedValue(float value) {
//                String lable = xAxisLable.get(value);
//                Log.d(TAG, "lable >><< " + lable);
//              //  long millis = TimeUnit.HOURS.toMillis((long) value);
//               //  return mFormat.format(new Date(millis));
//                return lable!=null?lable:"";
//               // return value+"";
//
//            }
//        });
        ValueFormatter custom = new MyValueFormatter("");

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setLabelCount(6,false);
        //leftAxis.setAxisMinValue(-1f);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setValueFormatter(custom);
        //leftAxis.setGranularity(2f);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(5f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setValueFormatter(new ValueFormatter() {
            // private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
            @Override
            public String getFormattedValue(float value) {
                DecimalFormat  mFormat = new DecimalFormat("###.#");
                return mFormat.format(value)+" Cr";
            }
        });

        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(getResources().getColor(R.color.gray));
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(tfLight);
        rightAxis.setLabelCount(10, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(5f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = chart.getLegend();
        l.setEnabled(false);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(8f);
        l.setXEntrySpace(10f);

        //XYMarkerView mv = new XYMarkerView(getActivity(), xAxisFormatter);
       // mv.setChartView(chart); // For bounds control
        //chart.setMarker(mv); // Set the marker to the chart
        PichartMarkerView pichartView = new PichartMarkerView(getContext(), R.layout.pi_chart_marker_view);
        chart.setMarker(pichartView);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setSalesData(tabLayout.getSelectedTabPosition(),fullSaleData);
                chart.animateX(2000);
                chart.refreshDrawableState();
//                if(tabLayout.getSelectedTabPosition() == 0){
//                    setSalesData(tabLayout.getSelectedTabPosition());
//                    chart.animateX(2000);
//                    chart.refreshDrawableState();
//                   // createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE,getCustomerSixMBody());
//                    // Toast.makeText(activity, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_SHORT).show();
//                }
//                else if(tabLayout.getSelectedTabPosition() == 1){
//                    setData(40, 80);
//                    chart.animateX(2000);
//                    chart.refreshDrawableState();
//                    createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE,getCustomerThreeMBody());
//                    // Toast.makeText(activity, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_SHORT).show();
//                }
//                else if(tabLayout.getSelectedTabPosition() == 2){
//                    setData(80, 80);
//                    chart.animateX(2000);
//                    chart.refreshDrawableState();
//                    createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE,getCustomerSixMBody());
//                    // Toast.makeText(activity, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_SHORT).show();
//                }
//                else if(tabLayout.getSelectedTabPosition() == 3){
//                    setData(400, 80);
//                    chart.animateX(2000);
//                    chart.refreshDrawableState();
//                    createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE,getCustomerSixMBody());
//                    // Toast.makeText(activity, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//            TabLayout.Tab tab = tabLayout.getTabAt(0);
//            tab.select();
        Log.d("selectedtab",String.valueOf(tabLayout.getSelectedTabPosition()));

        //setData(10, 80);
        tabLayout.getTabAt(1).select();
        setSalesData(1,fullSaleData);
        chart.animateX(2000);
        barchats.addView(chart);
        customerChartLayout.addView(chartLayout);
    }

//    private void setSalesBarData(JSONArray salesData,JSONArray collectData) throws JSONException {
//
//        ArrayList<Entry> entries = new ArrayList<>();
//        final  Map<Float,String> xAxisLable=new HashMap<>();
//        float limitNumber = 0;
//        float yaxisMax=0;
//        xAxisLable.clear();
//        Log.d(TAG,"data.length() > "+salesData.length());
//        for (int i = 0; i < salesData.length(); i++) {
//            JSONObject element= (JSONObject) salesData.get(i);
//            float xVal = (float) (i);
//            int yVal = element.getInt("value");
//            String lableName=element.getString("name");
//
//            BigDecimal number1 = new BigDecimal(yVal);
//            xAxisLable.put(xVal,lableName);
//            float debitFloat = number1.floatValue() / 10000000;
//            if(yaxisMax<debitFloat) {
//                yaxisMax = debitFloat;
//            }
//            if (i == salesData.length()) {
//                limitNumber = Math.round(debitFloat) % 2 == 0 ? +Math.round(debitFloat) + 2 : Math.round(debitFloat) + 1;
//            }
//            Log.d(TAG,"lableName > "+lableName);
//            Log.d(TAG,"debitFloat > "+debitFloat);
//
//            entries.add(new Entry(xVal, Math.abs(debitFloat)));
//        }
//
//        ArrayList<Entry> collectEntries = new ArrayList<>();
//        for (int i = 0; i < collectData.length(); i++) {
//            JSONObject element= (JSONObject) collectData.get(i);
//            float xVal = (float) (i);
//            int yVal = element.getInt("value");
//            String lableName=element.getString("name");
//
//            BigDecimal number1 = new BigDecimal(yVal);
//            // xAxisLable.put(xVal,lableName);
//            float debitFloat = number1.floatValue() / 10000000;
////            if(yaxisMax<debitFloat) {
////                yaxisMax = debitFloat;
////            }
////            if (i == collectData.length()) {
////                limitNumber = Math.round(debitFloat) % 2 == 0 ? +Math.round(debitFloat) + 2 : Math.round(debitFloat) + 1;
////            }
//            Log.d(TAG,"lableName > "+lableName);
//            Log.d(TAG,"debitFloat > "+debitFloat);
//
//            collectEntries.add(new Entry(xVal, Math.abs(debitFloat)));
//        }
//        yaxisMax = Math.round(yaxisMax) % 2 == 0 ? +Math.round(yaxisMax) + 2 : Math.round(yaxisMax) + 1;
//        yaxisMax=yaxisMax*2;
//        //Log.d(TAG,"yaxisMax >> "+yaxisMax);
//
//        // sort by x-value
//        Collections.sort(entries, new EntryXComparator());
//        ArrayList<LineDataSet> lines = new ArrayList<LineDataSet> ();
////        // create a dat aset and give it a type
//        LineDataSet set1 = new LineDataSet(entries, "");
//
//        if(entries.size()<=1){
//            set1.setDrawCircles(true);
//        }
////        set1.setDrawCircles(false);
////        set1.setDrawCircleHole(false);
//        set1.setLineWidth(.5f);
////        set1.setDrawHorizontalHighlightIndicator(true);
////        set1.setDrawHighlightIndicators(true);
//        set1.setCubicIntensity(0.2f);
//        //  set1.setColor(Color.rgb(255, 241, 46));
////        set1.setColor(activity.getResources().getColor(R.color.black));
////        set1.setFillAlpha(255);
////        set1.setValueTextColor(activity.getResources().getColor(R.color.chart5));
////        set1.setDrawValues(false);
//        set1.setDrawIcons(false);
//        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        // draw dashed line
//        //  set1.enableDashedLine(10f, 5f, 0f);
//        set1.disableDashedLine();
//        // black lines and points
//        set1.setColor(activity.getResources().getColor(R.color.chart4));
//        // set1.setCircleColor(Color.BLACK);
//
//        // line thickness and point size
//        // set1.setLineWidth(0f);
//        // set1.setCircleRadius(0f);
//
//        // draw points as solid circles
//        set1.setDrawCircleHole(false);
//        set1.setDrawCircles(false);
//
//        // customize legend entry
//        set1.setFormLineWidth(0f);
//        //set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//        // set1.setFormSize(15.f);
//
//        // text size of values
//        set1.setValueTextSize(9f);
//
//        // draw selection line as dashed
//        //set1.enableDashedHighlightLine(10f, 5f, 0f);
//
//        // set the filled area
//        set1.setDrawFilled(true);
//        set1.setDrawValues(false);
//
//        LineDataSet set2 = new LineDataSet(collectEntries, "");
//
//        if(entries.size()<=1){
//            set1.setDrawCircles(true);
//        }
////        set1.setDrawCircles(false);
////        set1.setDrawCircleHole(false);
//        set2.setLineWidth(.5f);
////        set1.setDrawHorizontalHighlightIndicator(true);
////        set1.setDrawHighlightIndicators(true);
//        set2.setCubicIntensity(0.2f);
//        //  set1.setColor(Color.rgb(255, 241, 46));
////        set1.setColor(activity.getResources().getColor(R.color.black));
////        set1.setFillAlpha(255);
////        set1.setValueTextColor(activity.getResources().getColor(R.color.chart5));
////        set1.setDrawValues(false);
//        set2.setDrawIcons(false);
//        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        // draw dashed line
//        //  set1.enableDashedLine(10f, 5f, 0f);
//        set2.disableDashedLine();
//        // black lines and points
//        set2.setColor(activity.getResources().getColor(R.color.chart4));
//        // set1.setCircleColor(Color.BLACK);
//
//        // line thickness and point size
//        // set1.setLineWidth(0f);
//        // set1.setCircleRadius(0f);
//
//        // draw points as solid circles
//        set2.setDrawCircleHole(false);
//        set2.setDrawCircles(false);
//
//        // customize legend entry
//        set2.setFormLineWidth(0f);
//        //set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//        // set1.setFormSize(15.f);
//
//        // text size of values
//        set2.setValueTextSize(9f);
//
//        // draw selection line as dashed
//        //set1.enableDashedHighlightLine(10f, 5f, 0f);
//
//        // set the filled area
//        set2.setDrawFilled(true);
//        set2.setDrawValues(false);
//
//
//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(set1); // add the data sets
//        dataSets.add(set2); // add the data sets
//        //set1.setFillColor(Color.GREEN);
//        //set1.setHighLightColor(getResources().getColor(R.color.chart5));
//        //set1.setCircleRadius(4f);
//
//        // create a data object with the data sets
//        lines.add(set1);
//        lines.add(set2);
//        LineData lineData = new LineData(dataSets);
//
//        // set data
//        saleVsCollecLineChart.setData(lineData);
//        List<ILineDataSet> sets = saleVsCollecLineChart.getData().getDataSets();
//        //lineChart.getAxisLeft().setAxisMaximum(0);
//        //lineChart.getAxisLeft().setAxisMinimum(limitNumber);
//        YAxis leftAxis = saleVsCollecLineChart.getAxisLeft();
//        //leftAxis.setAxisMaxValue(yaxisMax);
//        leftAxis.setLabelCount(6,false);
//        leftAxis.setAxisMinValue(-1f);
//        leftAxis.setDrawAxisLine(false);
//        leftAxis.setDrawZeroLine(false);
//        leftAxis.setDrawGridLines(true);
//        leftAxis.setGridColor(activity.getResources().getColor(R.color.gray));
//        XAxis xAxis = saleVsCollecLineChart.getXAxis();
//
//        //  xAxis.setValueFormatter(new DayAxisValueFormatter(saleVsCollecLineChart,2018));
//        xAxis.setValueFormatter(new ValueFormatter() {
//            // private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
//
//            @Override
//            public String getFormattedValue(float value) {
//                String lable = xAxisLable.get(value);
//                Log.d(TAG, "lable >><< " + lable);
//                //long millis = TimeUnit.HOURS.toMillis((long) value);
//                // return mFormat.format(new Date(millis));
//                return lable!=null?lable:"";
//
//            }
//        });
//        xAxis.setLabelCount(6);
//        set1.setDrawFilled(false);
//        set2.setDrawFilled(false);
//
//        for (ILineDataSet iSet : sets) {
//
//            LineDataSet set = (LineDataSet) iSet;
////            if (set.isDrawFilledEnabled())
////                set.setDrawFilled(false);
////            else
////                set.setDrawFilled(true);
////        }
//            set1.setDrawFilled(true);
//            if (Utils.getSDKInt() >= 18) {
//                // fill drawable only supported on api level 18 and above
//                Drawable drawable = ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.fade_graph);
//                set1.setFillDrawable(drawable);
//                Drawable drawable1 = ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.colleaction_fade_graph);
//                set2.setFillDrawable(drawable1);
//            } else {
//                set1.setFillColor(activity.getResources().getColor(R.color.chart1));
//                set1.setFillColor(activity.getResources().getColor(R.color.chart5));
//            }
//        }
//        //return data;
//    }


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
            new ItemSortable("Total"),
                 new ItemSortable("April"),
                 new ItemSortable("May"),
                 new ItemSortable("June"),
                 new ItemSortable("July"),
                 new ItemSortable("August"),
                 new ItemSortable("September"),
                 new ItemSortable("October"),
                 new ItemSortable("November"),
                 new ItemSortable("December"),
                 new ItemSortable("Januray"),
                 new ItemSortable("Febuary"),
                 new ItemSortable("March"),
                 new ItemSortable("Customer"),

        };
         return headers;
    }

    public List<NexusWithImage> getCustomerSixMBody()  {
        List<Integer> resImages = Arrays.asList();

        List<NexusWithImage> items = new ArrayList<>();
        String type = "Total Sales";
       // items.add(new NexusWithImage(type, resImages));
        GlobalClass.salesStateCustomer.clear();
//        if(GlobalClass.customerFullList!=null && !GlobalClass.customerFullList.isEmpty()) {
//            customerFullList = GlobalClass.customerFullList;
//        }
        for(Map.Entry<String, JSONArray> set:GlobalClass.customerFullList.entrySet()) {
            try {
                Log.d(TAG, "Key >" + set.getKey());
                if(!set.getKey().equalsIgnoreCase("totalStateSales")) {
                    JSONArray dataMonthly = set.getValue().getJSONArray(1);
                    JSONArray dataYearly = set.getValue().getJSONArray(3);
                    String[] dataTable = new String[dataMonthly.length() + 2];
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
                            totalSales = number1.floatValue() / 10000000;
                            Log.d(TAG, "set.getKey() >" + set.getKey());
                            Log.d(TAG, "totalSales >" + totalSales);
                            dataTable[j++] = String.valueOf(set.getKey());
                            dataTable[j++] = String.valueOf(Math.abs(totalSales));
                            GlobalClass.salesStateCustomer.add(set.getKey());
                        }

                        JSONObject element = dataMonthly.getJSONObject(i);
                        int yVal = element.getInt("value");
                        BigDecimal number1 = new BigDecimal(yVal);
                        float debitFloat = number1.floatValue() / 10000000;
                        dataTable[j++] = String.valueOf(Math.abs(debitFloat));

                    }
                    items.add(new NexusWithImage(type, dataTable));
                    Log.d(TAG, "dataTable >" + dataTable.toString());
                    Log.d(TAG, "dataTable length >" + dataTable.length);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
//        items.add(new NexusWithImage(type, "Auto Zone","Rs. 1,10,146", "20", "", "", "", ""));
//        items.add(new NexusWithImage(type, "Balaji Crushers","Rs. 3,50,146", "20", "", "", "", ""));
//        items.add(new NexusWithImage(type, "Bayer","Rs. 3,50,146", "20", "", "", "", ""));
//        items.add(new NexusWithImage(type, "Bharat Petroliam","Rs. 3,50,146", "20", "", "", "", ""));
//        items.add(new NexusWithImage(type, "Bharti Airtel","Rs. 3,50,146", "20", "", "", "", ""));


//        //items.add(new NexusWithImage(type, "Nexus S", "Samsung", "Gingerbread", "10", "16 GB", "4\"", "512 MB"));
//        items.add(new NexusWithImage(type, "Galaxy Nexus (16 GB)", "Samsung", "Ice cream Sandwich", "15", "16 GB", "4.65\"", "1 GB"));
//        items.add(new NexusWithImage(type, "Galaxy Nexus (32 GB)", "Samsung", "Ice cream Sandwich", "15", "32 GB", "4.65\"", "1 GB"));
//        items.add(new NexusWithImage(type, "Nexus 4 (8 GB)", "LG", "Jelly Bean", "17", "8 GB", "4.7\"", "2 GB"));
//        items.add(new NexusWithImage(type, "Nexus 4 (16 GB)", "LG", "Jelly Bean", "17", "16 GB", "4.7\"", "2 GB"));
//
//        type = "Tablets";
//        items.add(new NexusWithImage(type, resImages));
//        items.add(new NexusWithImage(type, "Nexus 7 (16 GB)", "Asus", "Jelly Bean", "16", "16 GB", "7\"", "1 GB"));
//        items.add(new NexusWithImage(type, "Nexus 7 (32 GB)", "Asus", "Jelly Bean", "16", "32 GB", "7\"", "1 GB"));
//        items.add(new NexusWithImage(type, "Nexus 10 (16 GB)", "Samsung", "Jelly Bean", "17", "16 GB", "10\"", "2 GB"));
//        items.add(new NexusWithImage(type, "Nexus 10 (32 GB)", "Samsung", "Jelly Bean", "17", "32 GB", "10\"", "2 GB"));
//
//        type = "Others";
//        items.add(new NexusWithImage(type, resImages));
//        items.add(new NexusWithImage(type, "Nexus Q", "--", "Honeycomb", "13", "--", "--", "--"));

      //  if(!set.getKey().equalsIgnoreCase("totalStateSales")) {
            try {
                JSONArray setVal = customerFullList.get("totalStateSales");
                JSONArray dataMonthly = setVal.getJSONArray(1);
                JSONArray dataYearly = setVal.getJSONArray(3);
                String[] dataTable = new String[dataMonthly.length() + 2];
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
                        totalSales = number1.floatValue() / 10000000;
                        //Log.d(TAG, "set.getKey() >" + totalStateSales);
                        Log.d(TAG, "totalSales >" + totalSales);
                        dataTable[j++] = String.valueOf("Total Sales");
                        dataTable[j++] = String.valueOf(Math.abs(totalSales));

                    }

                    JSONObject element = dataMonthly.getJSONObject(i);
                    int yVal = element.getInt("value");
                    BigDecimal number1 = new BigDecimal(yVal);
                    float debitFloat = number1.floatValue() / 10000000;
                    Log.d(TAG, "debitFloat >" + debitFloat);
                    dataTable[j++] = String.valueOf(Math.abs(debitFloat ));

                }
                items.add(new NexusWithImage(type, dataTable));
                Log.d(TAG, "dataTable >" + dataTable.toString());
                Log.d(TAG, "dataTable length >" + dataTable.length);
            }catch (Exception e){
                e.printStackTrace();
            }
       // }
        return items;
    }

    public List<NexusWithImage> getCustomerThreeMBody(){
        List<Integer> resImages = Arrays.asList();

        List<NexusWithImage> items = new ArrayList<>();
        String type = "Total Sales";
        // items.add(new NexusWithImage(type, resImages));
      //  items.add(new NexusWithImage(type, "Amazone","Rs. 1,30,546", "20", "", "", "", ""));



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
                GlobalClass.currentFrgmentMain=item.data[0];
                GlobalClass.cutomerName=item.data[0];
                GlobalClass.title="Invoices for "+item.data[0];
                startActivity(myIntent);
            }
        };

    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup> setClickListenerFirstBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup>() {
        @Override
        public void onClickItem(NexusWithImage item, OriginalFirstBodyCellViewGroup viewGroup, int row, int column) {


            if(item.data[column + 1].equalsIgnoreCase("Total Sales")) {
                fullSaleData = GlobalClass.customerFullList.get("totalStateSales");
                Snackbar snackbar = Snackbar.make(viewGroup,   item.data[column + 1] , Snackbar.LENGTH_LONG).setAction("Action", null);
                //  customerFullList.get(item.data[column + 1] );
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.chart4));
                snackbar.show();
            }else{
                fullSaleData = GlobalClass.customerFullList.get(item.data[column + 1]);
                Snackbar snackbar = Snackbar.make(viewGroup, "Cutomer Name is " + item.data[column + 1] , Snackbar.LENGTH_LONG).setAction("Action", null);
                //  customerFullList.get(item.data[column + 1] );
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.chart4));
                snackbar.show();
            }
            tabLayout.getTabAt(1).select();
            setSalesData(1,fullSaleData);
            chart.animateX(2000);
            chart.refreshDrawableState();

        }
    };


    private void setSalesData(int tabType, JSONArray fullSaleData) {
        final  Map<Float,String> xAxisLable=new HashMap<>();

       //JSONArray fullSaleData= customerFullList.get("totalStateSales");
        //JSONArray fullSaleData= customerFullList.get(position);
        try {
            JSONArray tabData=fullSaleData.getJSONArray(tabType);

        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < tabData.length(); i++) {
            JSONObject element=tabData.getJSONObject(i);
            float xVal = (float) (i);
            int yVal = element.getInt("value");
            String lableName=element.getString("name");

            BigDecimal number1 = new BigDecimal(yVal);
            xAxisLable.put(xVal,lableName);
            float debitFloat = number1.floatValue() / 10000000;

            Log.d(TAG,"lableName > "+lableName);
            Log.d(TAG,"debitFloat > "+debitFloat);

             values.add(new BarEntry(xVal,Math.abs(debitFloat)));

        }

        BarDataSet set1;


            set1 = new BarDataSet(values, "");
            set1.setDrawIcons(false);
            set1.setColor(getActivity().getResources().getColor(R.color.chart1));
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(tfLight);
            data.setBarWidth(.5f);
            data.setDrawValues(false);
            XAxis xAxis = chart.getXAxis();
            xAxis.setValueFormatter(new ValueFormatter() {
                // private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);

                @Override
                public String getFormattedValue(float value) {
                    String lable = xAxisLable.get(value);
                  //  Log.d(TAG, "lable >><< " + lable);
                    //  long millis = TimeUnit.HOURS.toMillis((long) value);
                    //  return mFormat.format(new Date(millis));
                    return lable!=null?lable:"";
                    // return value+"";

                }
            });
            chart.setData(data);
        //}
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void loadFragment(Fragment fragment) {
        Intent myIntent = new Intent(getActivity(), CustomerDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("BackFrgment", "Services");
        bundle.putString("CurrentFrgment", "Sales");
        bundle.putString("Title", "Sales");
        myIntent.putExtras(bundle);
        startActivity(myIntent);

    }



}