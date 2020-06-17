package com.cbat.cbat.ui.services.recievable.details_receivable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
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
import com.cbat.cbat.util.Constants;
import com.cbat.cbat.util.DayAxisValueFormatter;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.MyValueFormatter;
import com.cbat.cbat.util.PichartMarkerView;
import com.cbat.cbat.util.Utility;
import com.github.mikephil.charting.charts.BarChart;
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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jp.gr.java_conf.androtaku.geomap.GeoMapView;


public class CustomerDetailFragment_R extends Fragment {
    String TAG="CustomerDetailFragment_R";
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
    ImageView crImage;
    ImageView lakhImage;

    ImageView thImage;
    ImageView top5Image;
    ImageView top10Image;
    ImageView top15Image;
    ImageView allImage;
    Map<String,JSONArray> customerDetailMap= new HashMap<>();
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
    public static CustomerDetailFragment_R newInstance() {
        return new CustomerDetailFragment_R();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        //stateName = getArguments().getString("stateName");
        //GlobalClass.stateName = getArguments().getString("stateName");
        Log.d(TAG, "Tab >> " + GlobalClass.backFrgment);
//        if(GlobalClass.backFrgment.equalsIgnoreCase("Sales Trend")) {
//            Log.d(TAG,"Sales Trend Size >> "+GlobalClass.customerSalesTrendFullList.size());
//            customerFullList= GlobalClass.customerSalesTrendFullList;
//        }else
        if (GlobalClass.customerReceivableFullList != null && !GlobalClass.customerReceivableFullList.isEmpty()) {
            Log.d(TAG, "Other Size >> " + GlobalClass.customerReceivableFullList.size());
            customerFullList = GlobalClass.customerReceivableFullList;

        } else {
            customerFullList = new HashMap<>();
        }
        GlobalClass.sortingOn = -1;
        GlobalClass.pdfDataTab="customerReceivableDetails";

        tfLight = Typeface.createFromAsset(getActivity().getAssets(), "arial.ttf");

        View v = inflater.inflate(R.layout.customer_detail_fragment, container, false);
        //heighSetting( v );
        tableFixHeaders = (TableFixHeaders) v.findViewById(R.id.tablefixheaders);
        // tableFixHeaders = new TableFixHeaders(getContext());
        tableFixHeadersAdapterFactory = new TableFixHeadersAdapterFactory(getContext());
        createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
        // final ProgressBar progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        final FrameLayout totalSalesContainer = (FrameLayout) v.findViewById(R.id.totalSalesContainer);
        totalSalesLayout = (LinearLayout) v.findViewById(R.id.totalSalesLayout);
        customerChartLayout = (LinearLayout) v.findViewById(R.id.customerChartLayout);
        chartLayout = layoutInflater.inflate(R.layout.chart_tab_layout, customerChartLayout, false);
        tabLayout = (TabLayout) chartLayout.findViewById(R.id.chart_filter_tabs);
        barchats = (LinearLayout) chartLayout.findViewById(R.id.chart_container);
        //fullSaleData= customerFullList.get((customerFullList.size()-1));

        if (!customerFullList.isEmpty()) {
            Log.d(TAG, "Cusomter size " + customerFullList.size());
            fullSaleData = customerFullList.get("totalStateSales");
        } else {
            fullSaleData = new JSONArray();
        }

        barChartCreate(tabLayout);
        // getTotalSaleCutomerWise();
        crImage = v.findViewById(R.id.cr);
        crImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.currecyPrefix = "CR";

                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                barChartCreate(tabLayout);
                resetCurrencyImage();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        lakhImage = v.findViewById(R.id.lakh);
        lakhImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.currecyPrefix = "LH";
                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                barChartCreate(tabLayout);
                resetCurrencyImage();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        thImage = v.findViewById(R.id.th);
        thImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.currecyPrefix = "TH";
                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                barChartCreate(tabLayout);
                resetCurrencyImage();
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
                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                fullSaleData = customerFullList.get(0);
                //  barChartCreate(tabLayout);
                resetShortingImage();
                tabLayout.getTabAt(1).select();
                setSalesData(1, fullSaleData);
                chart.animateX(2000);
                chart.refreshDrawableState();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        top10Image = v.findViewById(R.id.top10);
        top10Image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.sortingOn = 10;
                fullSaleData = customerFullList.get(0);
                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                // barChartCreate(tabLayout);
                resetShortingImage();
                tabLayout.getTabAt(1).select();
                setSalesData(1, fullSaleData);
                chart.animateX(2000);
                chart.refreshDrawableState();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        top15Image = v.findViewById(R.id.top15);
        top15Image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.sortingOn = 15;
                fullSaleData = customerFullList.get(0);
                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                // barChartCreate(tabLayout);
                resetShortingImage();
                tabLayout.getTabAt(1).select();
                setSalesData(1, fullSaleData);
                chart.animateX(2000);
                chart.refreshDrawableState();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        allImage = v.findViewById(R.id.alltop);
        allImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.sortingOn = -1;
                fullSaleData = customerFullList.get("totalStateSales");
                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                // barChartCreate(tabLayout);
                resetShortingImage();
                tabLayout.getTabAt(1).select();
                setSalesData(1, fullSaleData);
                chart.animateX(2000);
                chart.refreshDrawableState();
//                Toast.makeText(getContext(),
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

//   // @OnClick({R.id.cr,R.id.lakh,R.id.th,R.id.top5,R.id.top10,R.id.top15,R.id.all})
//    @OnClick(R.id.th)
//    public void submit() {
//        Toast.makeText(getContext(),
//                "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void barChartCreate(final TabLayout tabLayout) {
        customerChartLayout.removeAllViews();
        //final TabLayout tabLayout = (TabLayout) chartLayout.findViewById(R.id.chart_filter_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("1D"));
        tabLayout.addTab(tabLayout.newTab().setText("1M"));
        tabLayout.addTab(tabLayout.newTab().setText("3M"));
        tabLayout.addTab(tabLayout.newTab().setText("YTD"));
       // tabLayout.addTab(tabLayout.newTab().setText("3YTD"));

        chart = new BarChart(getActivity());
        ViewGroup.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
        //ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart, 2019);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(6);

        ValueFormatter custom = new MyValueFormatter("");

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setLabelCount(6, false);
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
                DecimalFormat mFormat = new DecimalFormat("###.#");
                return mFormat.format(value) + " " + GlobalClass.currecyPrefix;
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
                setSalesData(tabLayout.getSelectedTabPosition(), fullSaleData);
                chart.animateX(2000);
                chart.refreshDrawableState();

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
        Log.d("selectedtab", String.valueOf(tabLayout.getSelectedTabPosition()));

        //setData(10, 80);
        tabLayout.getTabAt(1).select();
        setSalesData(1, fullSaleData);
        chart.animateX(2000);
        barchats.addView(chart);
        customerChartLayout.addView(chartLayout);
    }


    private void createTable(int type, List<NexusWithImage> customerBody) {

        //BaseTableAdapter originalSortableTableFixHeader=tableFixHeadersAdapterFactory.getAdapter(type);
        OriginalSortableTableFixHeader originalSortableTableFixHeader = new OriginalSortableTableFixHeader(getContext());
        originalSortableTableFixHeader.setHeader(getCustomerHeader());
        originalSortableTableFixHeader.setBody(customerBody);
        originalSortableTableFixHeader.setClickListenerBody(setClickListenerBody);
        originalSortableTableFixHeader.setClickListenerFirstBody(setClickListenerFirstBody);
        tableFixHeaders.setAdapter(originalSortableTableFixHeader.getInstance());
        originalSortableTableFixHeader.getInstance().notifyDataSetChanged();


        //tableFixHeaders.setAdapter(tableFixHeadersAdapterFactory.getAdapter(type));
    }

    public ItemSortable[] getCustomerHeader() {
        ItemSortable headers[] = {
                new ItemSortable("Total"),
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

    public List<NexusWithImage> getCustomerSixMBody() {
        List<Integer> resImages = Arrays.asList();

        List<NexusWithImage> items = new ArrayList<>();
        String type = "Total Sales";
        // items.add(new NexusWithImage(type, resImages));
        if( GlobalClass.salesStateCustomer!=null &&  !GlobalClass.salesStateCustomer.isEmpty()) {
            GlobalClass.salesStateCustomer.clear();
        }else{
            GlobalClass.salesStateCustomer= new ArrayList<>();
        }
//        if(GlobalClass.customerFullList!=null && !GlobalClass.customerFullList.isEmpty()) {
//            customerFullList = GlobalClass.customerFullList;
//        }

        int count = 0;
        String moneyString="";
        for (Map.Entry<String, JSONArray> set : customerFullList.entrySet()) {
            try {
                if (count < GlobalClass.sortingOn || GlobalClass.sortingOn == -1) {
                    Log.d(TAG, "Key >" + set.getKey());
                    if (!set.getKey().equalsIgnoreCase("totalStateSales")) {
                        JSONArray dataMonthly = set.getValue().getJSONArray(1);
                        JSONArray dataYearly = set.getValue().getJSONArray(3);
                        JSONArray contriData = set.getValue().getJSONArray(4);
                       // JSONArray dataCustomer =  set.getValue().getJSONArray(5);
                        String[] dataTable = new String[dataMonthly.length() + 4];
                        JSONObject elementYearly;
                        boolean yearFlag = true;
                        boolean contriFlag = true;
                        float totalSales = 0;
                        int j = 0;

                        for (int i = 0; i < dataMonthly.length(); i++) {
                            if (yearFlag) {
                                elementYearly = dataYearly.getJSONObject(i);
                                yearFlag = false;
                                int yVal = elementYearly.getInt("value");
                                BigDecimal number1 = new BigDecimal(yVal);
                                //totalSales = number1.floatValue() / 10000000;
                                totalSales = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                Log.d(TAG, "set.getKey() >" + set.getKey());
                                Log.d(TAG, "totalSales >" + totalSales);
                                moneyString = formatter.format(Math.abs(totalSales));
                                dataTable[j++] = String.valueOf(set.getKey());
                                dataTable[j++] = moneyString; // Utility.decimal2Palce(Math.abs(totalSales));
                                GlobalClass.salesStateCustomer.add(set.getKey());
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
                                moneyString = formatter.format(Math.abs(yVal));
                                dataTable[j++] = moneyString; //Utility.decimal2Palce(Math.abs(yVal));
                            }

                            JSONObject element = dataMonthly.getJSONObject(i);
                            int yVal = element.getInt("value");
                            BigDecimal number1 = new BigDecimal(yVal);
                            // float debitFloat = number1.floatValue() / 10000000;
                            float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            moneyString = formatter.format(Math.abs(debitFloat));
                            dataTable[j++] = moneyString; //Utility.decimal2Palce(Math.abs(debitFloat));

                        }
                   //     customerDetailMap.put(dataTable[0],dataCustomer);
                        items.add(new NexusWithImage(type, dataTable));
                        Log.d(TAG, "dataTable >" + dataTable.toString());
                        Log.d(TAG, "dataTable length >" + dataTable.length);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            count++;
        }
        if (GlobalClass.sortingOn == -1) {
            try {
                if (customerFullList != null && !customerFullList.isEmpty() && customerFullList.containsKey("totalStateSales")) {
                    JSONArray setVal = customerFullList.get("totalStateSales");
                    JSONArray dataMonthly = setVal.getJSONArray(1);
                    JSONArray dataYearly = setVal.getJSONArray(3);
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
                            //totalSales = number1.floatValue() / 10000000;
                            totalSales = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            //Log.d(TAG, "set.getKey() >" + totalStateSales);
                            Log.d(TAG, "totalSales >" + totalSales);
                            dataTable[j++] = "Total Sales";
                            moneyString = formatter.format(Math.abs(totalSales));
                            dataTable[j++] = moneyString;//Utility.decimal2Palce(Math.abs(totalSales));
                            dataTable[j++] = "100";
                        }

                        JSONObject element = dataMonthly.getJSONObject(i);
                        int yVal = element.getInt("value");
                        BigDecimal number1 = new BigDecimal(yVal);
                        // float debitFloat = number1.floatValue() / 10000000;
                        float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                        Log.d(TAG, "debitFloat >" + debitFloat);
                        moneyString = formatter.format(Math.abs(debitFloat));
                        dataTable[j++] = moneyString; //Utility.decimal2Palce(Math.abs(debitFloat));

                    }
                    items.add(new NexusWithImage(type, dataTable));
                    Log.d(TAG, "dataTable >" + dataTable.toString());
                    Log.d(TAG, "dataTable length >" + dataTable.length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // }
        return items;
    }



    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup> setClickListenerBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup>() {
        @Override
        public void onClickItem(NexusWithImage item, OriginalBodyCellViewGroup viewGroup, int row, int column) {

            //Snackbar.make(viewGroup, "Yes we do it " + item.data[0] + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();

            //Snackbar.make(viewGroup, "Yes we do it " + item.data[column + 1] + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();
            Intent myIntent = new Intent(getActivity(), InvoiceDetailActivity_R.class);
//
            GlobalClass.currentFrgmentMain = item.data[0];
            GlobalClass.cutomerName = item.data[0];
            GlobalClass.title = "Invoices for " + item.data[0];
            startActivity(myIntent);
        }
    };

    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup> setClickListenerFirstBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup>() {
        @Override
        public void onClickItem(NexusWithImage item, OriginalFirstBodyCellViewGroup viewGroup, int row, int column) {
            Log.d("@@@@@@", "customerFullList"+customerFullList.toString());
            Log.d("@@@@@@", "customerDetailMap"+customerDetailMap.toString());

            if (item.data[column + 1].equalsIgnoreCase("Total Sales")) {
                fullSaleData = customerFullList.get("totalStateSales");

                Snackbar snackbar = Snackbar.make(viewGroup, item.data[column + 1], Snackbar.LENGTH_LONG).setAction("Action", null);
                //  customerFullList.get(item.data[column + 1] );
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.chart4));
                //snackbar.show();
            }
//            else {
//                fullSaleData = customerFullList.get(item.data[column + 1]);
//                Snackbar snackbar = Snackbar.make(viewGroup, " Name: " + item.data[column + 1]+"\nAddress: "+item.data[item.data.length-1],
//                        Snackbar.LENGTH_LONG).setAction("Action", null);
//                //  customerFullList.get(item.data[column + 1] );
//                View sbView = snackbar.getView();
//                TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
//                textView.setMaxLines(5);
//                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.chart4));
//                //snackbar.show();
//                JSONArray dataCustomer=customerDetailMap.get(item.data[column + 1]);
//                Log.d("check gurav",dataCustomer.toString());
//                try {
//                    View view = getLayoutInflater().inflate(R.layout.customer_profile_detail_new, null, false);
//                    Button closeBtn = (Button) view.findViewById(R.id.close);
//
//                    Button profile = (Button) view.findViewById(R.id.profile);
//                    profile.setText("Profile");
//                    TextView name = (TextView) view.findViewById(R.id.name);
//                    name.setText(item.data[column + 1]);
//                    GlobalClass.CusName=item.data[column + 1];
//                    TextView address = (TextView) view.findViewById(R.id.address);
//                    address.setText( dataCustomer.getJSONObject(0).getJSONObject("customer").getString("address"));
//
//                    TextView sate = (TextView) view.findViewById(R.id.sate);
//                    sate.setText(dataCustomer.getJSONObject(0).getJSONObject("customer").getString("state"));
//
//                    TextView pinCode = (TextView) view.findViewById(R.id.pinCode);
//                    pinCode.setText(dataCustomer.getJSONObject(0).getJSONObject("customer").getString("pincode"));
//
//                    Button credit = (Button) view.findViewById(R.id.credit);
//
//                    TextView pan = (TextView) view.findViewById(R.id.panID);
//                    String panTxt=(dataCustomer.getJSONObject(0).getJSONObject("customer").getString("panapplicablefrom"));
//                    if(panTxt!=null && !panTxt.isEmpty()){
//                        if(panTxt!="null"){
//                            pan.setText(panTxt);
//                        }else{
//                            pan.setText("");
//                        }
//
//                    }else{
//                        pan.setText("");
//                    }
//                    //   pan.setText(panTxt!=null && !panTxt.isEmpty() && panTxt.equalsIgnoreCase("null")?panTxt:" ");
//
//                    Log.d("bank",pan.getText().toString() );
//
//                    TextView gst = (TextView) view.findViewById(R.id.gst);
//                    String gstTxt=(dataCustomer.getJSONObject(0).getJSONObject("customer").getString("partygstin"));
//                    //  gst.setText(gstTxt!=null && !gstTxt.isEmpty() && gstTxt.equalsIgnoreCase("null")?gstTxt:" ");
//                    if(gstTxt!=null && !gstTxt.isEmpty()){
//                        if(gstTxt!="null"){
//                            gst.setText(gstTxt);
//                        }else{
//                            gst.setText("");
//                        }
//
//                    }else{
//                        gst.setText("");
//                    }
//
//                    TextView creditPeriod = (TextView) view.findViewById(R.id.creditPeriod);
//                    String creditPeroidTxt=dataCustomer.getJSONObject(0).getJSONObject("customer").getString("creditPeroid");
//                    creditPeriod.setText(creditPeroidTxt!=null && !creditPeroidTxt.isEmpty()?creditPeroidTxt +" "+"Days":"");
//
//                    TextView creditLimit = (TextView) view.findViewById(R.id.creditLimit);
//                    String creditLimitTxt=dataCustomer.getJSONObject(0).getJSONObject("customer").getString("creditAmount");
//                    creditLimit.setText(creditLimitTxt!=null && !creditLimitTxt.isEmpty()?formatter.format(Math.abs(Integer.parseInt(creditLimitTxt))):"");
//                    try{
//                        TextView accNo = (TextView) view.findViewById(R.id.accNo);
//                        String accountNoTxt=dataCustomer.getJSONObject(0).getJSONObject("customer").getString("accountNo");
//                        accNo.setText(accountNoTxt!=null && !accountNoTxt.isEmpty() && accountNoTxt!="0" && Integer.parseInt(accountNoTxt)>0 ?accountNoTxt:"");
//
//                        TextView ifscode = (TextView) view.findViewById(R.id.ifscode);
//                        String ifscCodeTxt=dataCustomer.getJSONObject(0).getJSONObject("customer").getString("ifsccode");
//                        //ifscode.setText(ifscCodeTxt!=null && !ifscCodeTxt.isEmpty() && ifscCodeTxt.equalsIgnoreCase("null")?ifscCodeTxt:"");
//                        if(ifscCodeTxt!=null && !ifscCodeTxt.isEmpty()){
//                            if(ifscCodeTxt!="null"){
//                                ifscode.setText(ifscCodeTxt);
//                            }else{
//                                ifscode.setText("");
//                            }
//
//                        }else{
//                            ifscode.setText("");
//                        }
//
//
//
//
//                        TextView bankName = (TextView) view.findViewById(R.id.bankName);
//                        String bankNameTxt=dataCustomer.getJSONObject(0).getJSONObject("customer").getString("bankName");
//                        //  bankName.setText(bankNameTxt!=null && !bankNameTxt.isEmpty() && bankNameTxt.equalsIgnoreCase("null")?bankNameTxt:"");
//                        if(bankNameTxt!=null && !bankNameTxt.isEmpty()){
//                            if(bankNameTxt!="null"){
//                                bankName.setText(bankNameTxt);
//                            }else{
//                                bankName.setText("");
//                            }
//
//                        }else{
//                            bankName.setText("");
//                        }
//
//
//                        TextView branch = (TextView) view.findViewById(R.id.branch);
//                        String branchNameTxt=dataCustomer.getJSONObject(0).getJSONObject("customer").getString("branchName");
//                        //  branch.setText(branchNameTxt!=null && !branchNameTxt.isEmpty() && branchNameTxt.equalsIgnoreCase("null")?branchNameTxt:"");
//
//                        if(branchNameTxt!=null && !branchNameTxt.isEmpty()){
//                            if(branchNameTxt!="null"){
//                                branch.setText(branchNameTxt);
//                            }else{
//                                branch.setText("");
//                            }
//
//                        }else{
//                            branch.setText("");
//                        }
//
//                        TextView BankAddress = (TextView) view.findViewById(R.id.BankAddress);
//                        String bankAddressTxt=dataCustomer.getJSONObject(0).getJSONObject("customer").getString("bankAddress");
//                        //   BankAddress.setText(bankAddressTxt!=null && !bankAddressTxt.isEmpty() &&  bankAddressTxt.equalsIgnoreCase("null")?bankAddressTxt:"");
//
//                        if(bankAddressTxt!=null && !bankAddressTxt.isEmpty()){
//                            if(bankAddressTxt!="null"){
//                                BankAddress.setText(bankAddressTxt);
//                            }else{
//                                BankAddress.setText("");
//                            }
//
//                        }else{
//                            BankAddress.setText("");
//                        }
//
//                    }catch(Exception e){
//                        Log.d("tag", e.toString());
//                    }
//
//
//                    PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//                    closeBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            popupWindow.dismiss();
//                        }
//                    });
//                    credit.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            popupWindow.dismiss();
//                            creditBricks();
//                        }
//                    });
//
//
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//
//            tabLayout.getTabAt(1).select();
//            setSalesData(1, fullSaleData);
//            chart.animateX(2000);
//            chart.refreshDrawableState();

        }



//        @Override
//        public void onClickItem(NexusWithImage item, OriginalFirstBodyCellViewGroup viewGroup, int row, int column) {
//
//
////            if (item.data[column + 1].equalsIgnoreCase("Total Sales")) {
////                fullSaleData = customerFullList.get("totalStateSales");
////                Snackbar snackbar = Snackbar.make(viewGroup, item.data[column + 1], Snackbar.LENGTH_LONG).setAction("Action", null);
////                //  customerFullList.get(item.data[column + 1] );
////                View sbView = snackbar.getView();
////                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.chart4));
////                snackbar.show();
////            } else {
////                fullSaleData = customerFullList.get(item.data[column + 1]);
////                Snackbar snackbar = Snackbar.make(viewGroup, "Cutomer Name is " + item.data[column + 1], Snackbar.LENGTH_LONG).setAction("Action", null);
////                //  customerFullList.get(item.data[column + 1] );
////                View sbView = snackbar.getView();
////                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.chart4));
////                snackbar.show();
////            }
//            if (item.data[column + 1].equalsIgnoreCase("Total Sales")) {
//                fullSaleData = customerFullList.get("totalStateSales");
//
//                Snackbar snackbar = Snackbar.make(viewGroup, item.data[column + 1], Snackbar.LENGTH_LONG).setAction("Action", null);
//                //  customerFullList.get(item.data[column + 1] );
//                View sbView = snackbar.getView();
//                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.chart4));
//                snackbar.show();
//            } else {
//                fullSaleData = customerFullList.get(item.data[column + 1]);
//                Snackbar snackbar = Snackbar.make(viewGroup, "Name: " + item.data[column + 1]+"\nAddress: "+item.data[15], Snackbar.LENGTH_LONG).setAction("Action", null);
//                //  customerFullList.get(item.data[column + 1] );
//                View sbView = snackbar.getView();
//                TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
//                textView.setMaxLines(5);
//                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.chart4));
//                snackbar.show();
//            }
//            tabLayout.getTabAt(1).select();
//            setSalesData(1, fullSaleData);
//            chart.animateX(2000);
//            chart.refreshDrawableState();
//
//        }


    };


    private void setSalesData(int tabType, JSONArray fullSaleData) {
        final Map<Float, String> xAxisLable = new HashMap<>();

        //JSONArray fullSaleData= customerFullList.get("totalStateSales");
        //JSONArray fullSaleData= customerFullList.get(position);
        try {
            if (fullSaleData != null && fullSaleData.length() > 0) {
                JSONArray tabData = fullSaleData.getJSONArray(tabType);

                ArrayList<BarEntry> values = new ArrayList<>();

                for (int i = 0; i < tabData.length(); i++) {
                    JSONObject element = tabData.getJSONObject(i);
                    float xVal = (float) (i);
                    int yVal = element.getInt("value");
                    String lableName = element.getString("name");

                    BigDecimal number1 = new BigDecimal(yVal);
                    xAxisLable.put(xVal, lableName);
                    //float debitFloat = number1.floatValue() / 10000000;
                    float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                    ;


                    // Log.d(TAG, "lableName > " + lableName);
                    //Log.d(TAG, "debitFloat > " + debitFloat);

                    values.add(new BarEntry(xVal, Utility.decimal2PalceAsInput(Math.abs(debitFloat))));

                }

                BarDataSet set1;


                set1 = new BarDataSet(values, "");
                set1.setDrawIcons(false);
                set1.setColor(getActivity().getResources().getColor(R.color.chart3));
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
                        //Log.d(TAG, "lable >><< " + lable);
                        //  long millis = TimeUnit.HOURS.toMillis((long) value);
                        //  return mFormat.format(new Date(millis));
                        return lable != null ? lable : "";
                        // return value+"";

                    }
                });
                chart.setData(data);
            }
            //}
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void creditBricks(){
        View view = getLayoutInflater().inflate(R.layout.customer_query_activity, null, false);
        Button closeBtn = (Button) view.findViewById(R.id.close);
        EditText subject = (EditText) view.findViewById(R.id.subject);


        EditText textArea = (EditText) view.findViewById(R.id.textArea);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button send = (Button) view.findViewById(R.id.send);
        PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        //  subject.clearFocus();


        popupWindow.setFocusable(true);
        popupWindow.update();
        //   subject.requestFocus();

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sub=subject.getText().toString();
                String message=textArea.getText().toString();
                saveQuery(sub,message);
                popupWindow.dismiss();

            }
        });
    }

    private void saveQuery(String sub, String mess) {
        if (progressDialog != null) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }
        String url = Constants.BASE_LOCAL__URL + "sendCreditLink";
        JSONObject paramsJsonObj = new JSONObject();
        try {
            String message =GlobalClass.userName+" "+"from"+" "+ GlobalClass.comapnyName+" "+"wants CreditLink of"+" "+GlobalClass.CusName +":"+"  "+ mess;
            paramsJsonObj.put("id", GlobalClass.userId);
            paramsJsonObj.put("mailtoemail", "ekank.rana@gmail.com");
            paramsJsonObj.put("mailSubject",sub );
            paramsJsonObj.put("mailMessage", message);



            Log.d("URL :- ", url);
            Log.d("paramsJsonObj :- ", paramsJsonObj.toString());
            JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, url, paramsJsonObj, this.createRequestSuccessListener(1), this.createRequestErrorListener(System.currentTimeMillis()));
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
                    else if (responseCode == 6) {
                        Log.d("Res", response.toString());
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        if (response.getString("code").equals(Integer.toString(206))) {

                            // GlobalClass.lastName = response.optString("userrole");
                            //Save to prefrence
                            JSONObject contentList = response.getJSONObject("contentList");
                            JSONObject setting = contentList.getJSONObject("setting");
                            GlobalClass.currecyPrefix=setting.getString("currencyType");
                            GlobalClass.startDate=setting.getString("startDate");
                            GlobalClass.endDate=setting.getString("endDate");





                            //   Log.d("setting", "settingRespinse"+contentList);


                        }
                    }
                    else if (responseCode == 1) {
                        Log.d("Res", response.toString());
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        if (response.getString("code").equals(Integer.toString(206))) {

                            // GlobalClass.lastName = response.optString("userrole");
                            //Save to prefrence


                            Log.d("resone query", response.toString());


                            //   Log.d("setting", "settingRespinse"+contentList);


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