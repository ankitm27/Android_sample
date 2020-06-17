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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.cbat.cbat.ui.navigation.ProductDetailActivity.TAG;

public class RecivableAgingDetailFragment extends Fragment {

    private ProgressDialog progressDialog;
    Map<String,Double> productSalesMap;
    Map<String,JSONObject> customerAgingFullList;
    private LayoutInflater layoutInflater;
    private TableFixHeaders tableFixHeaders;
    private TableFixHeadersAdapterFactory tableFixHeadersAdapterFactory;
    LinearLayout totalSalesLayout;
    LinearLayout customerChartLayout;
    LinearLayout barchats;

    View chartLayout;
    private BarChart chart;
    protected Typeface tfLight;
    String stateName;
    Map<String,Double> cutomerSalesMap;
    JSONObject fullAgainData;
    TabLayout tabLayout;
    ImageView crImage;
    ImageView lakhImage;

    ImageView thImage;
    ImageView top5Image;
    ImageView top10Image;
    ImageView top15Image;
    ImageView allImage;
    Map<String,JSONArray> customerDetailMap= new HashMap<>();
    public static RecivableAgingDetailFragment newInstance() {
        return new RecivableAgingDetailFragment();
    }
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
    //MPLine
    private LineChart lineChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.aging_customer_detail_fragment, container, false);
       // final ProgressBar progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        Log.d("URL :- ","Test");
        GlobalClass.sortingOn = -1;
        tfLight = Typeface.createFromAsset(getActivity().getAssets(), "arial.ttf");
        if(GlobalClass.customerAgingFullList!=null && GlobalClass.customerAgingFullList.size()>0) {
            customerAgingFullList=GlobalClass.customerAgingFullList;
            Log.d(TAG,"customerAgingFullList > "+customerAgingFullList.size());
        }
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
     if(customerAgingFullList!=null && customerAgingFullList.size()>0) {
            for (Map.Entry<String, JSONObject> set : customerAgingFullList.entrySet()) {
                Log.d(TAG, "Key >" + set.getKey());
                fullAgainData = set.getValue();
            }
        Log.d(TAG,"fullCustomerData > "+fullAgainData.length());
    }else{
        Log.d(TAG,"customerAgingFullList > "+customerAgingFullList.size());
    }

        barChartCreate();


        // getTotalSaleCutomerWise();
        crImage = v.findViewById(R.id.cr);
        crImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.currecyPrefix = "CR";

                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                barChartCreate();
                resetCurrencyImage();
                Toast.makeText(getContext(),
                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        lakhImage = v.findViewById(R.id.lakh);
        lakhImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.currecyPrefix = "LH";
                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                barChartCreate();
                resetCurrencyImage();
                Toast.makeText(getContext(),
                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        thImage = v.findViewById(R.id.th);
        thImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.currecyPrefix = "TH";
                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                barChartCreate();
                resetCurrencyImage();
                Toast.makeText(getContext(),

                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        top5Image = v.findViewById(R.id.top5);
        top5Image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.sortingOn = 5;
                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                fullAgainData = customerAgingFullList.get(0);
                //  barChartCreate(tabLayout);
                resetShortingImage();
               // tabLayout.getTabAt(1).select();
                setSalesData(fullAgainData);
                chart.animateX(2000);
                chart.refreshDrawableState();
                Toast.makeText(getContext(),
                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        top10Image = v.findViewById(R.id.top10);
        top10Image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.sortingOn = 10;
                fullAgainData = customerAgingFullList.get(0);
                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                // barChartCreate(tabLayout);
                resetShortingImage();
               // tabLayout.getTabAt(1).select();
                setSalesData( fullAgainData);
                chart.animateX(2000);
                chart.refreshDrawableState();
                Toast.makeText(getContext(),
                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        top15Image = v.findViewById(R.id.top15);
        top15Image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.sortingOn = 15;
                fullAgainData = customerAgingFullList.get(0);
                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                // barChartCreate(tabLayout);
                resetShortingImage();
               // tabLayout.getTabAt(1).select();
                setSalesData( fullAgainData);
                chart.animateX(2000);
                chart.refreshDrawableState();
                Toast.makeText(getContext(),
                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        allImage = v.findViewById(R.id.alltop);
        allImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.sortingOn = -1;
                fullAgainData = customerAgingFullList.get(0);
                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                // barChartCreate(tabLayout);
                resetShortingImage();
               // tabLayout.getTabAt(1).select();
                setSalesData( fullAgainData);
                chart.animateX(2000);
                chart.refreshDrawableState();
                Toast.makeText(getContext(),
                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
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

    public void barChartCreate() {
        customerChartLayout.removeAllViews();
        //final TabLayout tabLayout = (TabLayout) chartLayout.findViewById(R.id.chart_filter_tabs);
       // tabLayout.addTab(tabLayout.newTab().setText("Aging"));
//        tabLayout.addTab(tabLayout.newTab().setText("1M"));
//        tabLayout.addTab(tabLayout.newTab().setText("3M"));
//        tabLayout.addTab(tabLayout.newTab().setText("YTD"));
//        tabLayout.addTab(tabLayout.newTab().setText("3YTD"));

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
                DecimalFormat mFormat = new DecimalFormat("###.##");
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

//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                setSalesData( fullCustomerData);
//                chart.animateX(2000);
//                chart.refreshDrawableState();
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
////            TabLayout.Tab tab = tabLayout.getTabAt(0);
////            tab.select();
//        Log.d("selectedtab", String.valueOf(tabLayout.getSelectedTabPosition()));

        //setData(10, 80);
      //  tabLayout.getTabAt(1).select();
        setSalesData( fullAgainData);
        chart.animateX(2000);
       // barchats.addView(chart);
        customerChartLayout.addView(chart);
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
                new ItemSortable("Receivable"),
                new ItemSortable("Overdue"),
                new ItemSortable("No Due"),
                new ItemSortable("0-15"),
                new ItemSortable("15-30"),
                new ItemSortable("30-60"),
                new ItemSortable("60-90"),
                new ItemSortable(">90"),
                new ItemSortable("Customer"),

        };
        return headers;
    }

    public List<NexusWithImage> getCustomerSixMBody() {
        List<Integer> resImages = Arrays.asList();

        List<NexusWithImage> items = new ArrayList<>();
        String type = "Total Sales";
        // items.add(new NexusWithImage(type, resImages));
       // GlobalClass.salesStateCustomer.clear();
//        if(GlobalClass.customerFullList!=null && !GlobalClass.customerFullList.isEmpty()) {
//            customerFullList = GlobalClass.customerFullList;
//        }


        int count = 0;
String moneyString="";
        Log.d(TAG, "customerAgingFullList size >" + customerAgingFullList.size());
        if(customerAgingFullList!=null && !customerAgingFullList.isEmpty() ) {
            for (Map.Entry<String, JSONObject> set : customerAgingFullList.entrySet()) {
                try {
                    if (count < GlobalClass.sortingOn || GlobalClass.sortingOn == -1) {
                        Log.d(TAG, "Key >" + set.getKey());
                        if (!set.getKey().equalsIgnoreCase("totalStateSales")) {
                            JSONObject fullAgaingData = set.getValue();

                            String[] dataTable = new String[9];
                            int j = 0;

                            double receivable = fullAgaingData.getDouble("receivable");
                            BigDecimal receivablenumber = new BigDecimal(receivable);
                            float receivableFinal = receivablenumber.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            dataTable[j++] = String.valueOf(set.getKey());
                           // dataTable[j++] = String.valueOf("Receivable");
                            moneyString = formatter.format(Math.abs(receivableFinal));
                            dataTable[j++] = moneyString;
                            //Utility.decimal2Palce(Math.abs(receivableFinal));

                            double overdue = fullAgaingData.getDouble("overdue");
                            BigDecimal number = new BigDecimal(overdue);
                            float overdueFinal = number.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            moneyString = formatter.format(Math.abs(overdueFinal));
                            dataTable[j++] = moneyString;
                            //Utility.decimal2Palce(Math.abs(overdueFinal));

                            double noDue = fullAgaingData.getDouble("noDue");
                            BigDecimal number1 = new BigDecimal(noDue);
                            float noDue_Final = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            moneyString = formatter.format(Math.abs(noDue_Final));
                            dataTable[j++] = moneyString;
                            //Utility.decimal2Palce(Math.abs(noDue_Final));


                            double day15 = fullAgaingData.getDouble("day15");
                            BigDecimal number2 = new BigDecimal(day15);
                            float day15Final = number2.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            moneyString = formatter.format(Math.abs(day15Final));
                            dataTable[j++] = moneyString;
                            //Utility.decimal2Palce(Math.abs(day15Final));

                            double day30 = fullAgaingData.getDouble("day30");
                            BigDecimal number3 = new BigDecimal(day30);
                            float day30Final = number3.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            moneyString = formatter.format(Math.abs(day30Final));

                            dataTable[j++] = moneyString;
                            //Utility.decimal2Palce(Math.abs(day30Final));

                            double day60 = fullAgaingData.getDouble("day60");
                            BigDecimal number4 = new BigDecimal(day60);
                            float day60Final = number4.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            moneyString = formatter.format(Math.abs(day60Final));
                            dataTable[j++] = moneyString;
                            //Utility.decimal2Palce(Math.abs(day60Final));


                            double day90 = fullAgaingData.getDouble("day90");
                            BigDecimal number5 = new BigDecimal(day90);
                            float day90Final = number5.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);

                            moneyString = formatter.format(Math.abs(day90Final));
                            dataTable[j++] = moneyString;
                            //Utility.decimal2Palce(Math.abs(day90Final));

                            double dayGreater90 = fullAgaingData.getDouble("dayGreater90");
                            BigDecimal number6 = new BigDecimal(dayGreater90);
                            float dayGreater90Final = number6.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            moneyString = formatter.format(Math.abs(dayGreater90Final));
                            dataTable[j++] = moneyString;
                            //Utility.decimal2Palce(Math.abs(dayGreater90Final));


                            items.add(new NexusWithImage(type, dataTable));
                            Log.d(TAG, "dataTable >" + dataTable.toString());
                            Log.d(TAG, "dataTable length >" + dataTable.length);

                            if(!set.getKey().equalsIgnoreCase("Total Receivable")) {
                                GlobalClass.salesStateCustomer.add(set.getKey());
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                count++;
            }
        }
//        if (GlobalClass.sortingOn == -1) {
//            try {
//                if (customerFullList != null && !customerFullList.isEmpty() && customerFullList.containsKey("totalStateSales")) {
//                    JSONArray setVal = customerFullList.get("totalStateSales");
//                    JSONArray dataMonthly = setVal.getJSONArray(1);
//                    JSONArray dataYearly = setVal.getJSONArray(3);
//                    String[] dataTable = new String[dataMonthly.length() + 3];
//                    JSONObject elementYearly;
//                    boolean yearFlag = true;
//                    float totalSales = 0;
//                    int j = 0;
//                    for (int i = 0; i < dataMonthly.length(); i++) {
//                        if (yearFlag) {
//                            elementYearly = dataYearly.getJSONObject(i);
//                            yearFlag = false;
//                            int yVal = elementYearly.getInt("value");
//                            BigDecimal number1 = new BigDecimal(yVal);
//                            //totalSales = number1.floatValue() / 10000000;
//                            totalSales = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
//                            //Log.d(TAG, "set.getKey() >" + totalStateSales);
//                            Log.d(TAG, "totalSales >" + totalSales);
//                            dataTable[j++] = String.valueOf("Total Sales");
//                            dataTable[j++] = String.valueOf(Math.abs(totalSales));
//                            dataTable[j++] = String.valueOf("100");
//                        }
//
//                        JSONObject element = dataMonthly.getJSONObject(i);
//                        int yVal = element.getInt("value");
//                        BigDecimal number1 = new BigDecimal(yVal);
//                        // float debitFloat = number1.floatValue() / 10000000;
//                        float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
//                        Log.d(TAG, "debitFloat >" + debitFloat);
//                        dataTable[j++] = String.valueOf(Math.abs(debitFloat));
//
//                    }
//                    items.add(new NexusWithImage(type, dataTable));
//                    Log.d(TAG, "dataTable >" + dataTable.toString());
//                    Log.d(TAG, "dataTable length >" + dataTable.length);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        // }
        return items;
    }



    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup> setClickListenerBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup>() {
        @Override
        public void onClickItem(NexusWithImage item, OriginalBodyCellViewGroup viewGroup, int row, int column) {

            //Snackbar.make(viewGroup, "Yes we do it " + item.data[0] + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();

           // Snackbar.make(viewGroup, "Yes we do it " + GlobalClass.salesStateCustomer.size() + " ( )", Snackbar.LENGTH_SHORT).show();
Log.d(TAG," Size>>"+GlobalClass.salesStateCustomer.size());
            Intent myIntent = new Intent(getActivity(), InvoiceDetailActivity_R.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("StateName", stateName);
//                bundle.putString("BackFrgment", "TotalSale");
//                bundle.putString("CurrentFrgment", stateName);
//                bundle.putString("CurrentFrgmentMain", item.data[0]);
//                bundle.putString("CutomerName", item.data[0]);
//                bundle.putString("Title", "Invoices for "+item.data[0]);
//                myIntent.putExtras(bundle);
            GlobalClass.currentFrgmentMain = item.data[0];
            GlobalClass.cutomerName = item.data[0];
            GlobalClass.title = "Invoices for " + item.data[0];
            startActivity(myIntent);
        }
    };

    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup> setClickListenerFirstBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup>() {
        @Override
        public void onClickItem(NexusWithImage item, OriginalFirstBodyCellViewGroup viewGroup, int row, int column) {



            fullAgainData = customerAgingFullList.get(item.data[column + 1]);
            Log.d("@@@@@@@@","customerAgingFullList:---"+customerAgingFullList.toString());
               Snackbar snackbar = Snackbar.make(viewGroup, "Cutomer Name is " + item.data[column + 1], Snackbar.LENGTH_LONG).setAction("Action", null);
           View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setMaxLines(5);
            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.chart4));
                snackbar.show();

         //   tabLayout.getTabAt(1).select();
            setSalesData(fullAgainData);
            chart.animateX(2000);
            chart.refreshDrawableState();

//            if (item.data[column + 1].equalsIgnoreCase("Total Sales")) {
//                fullAgainData = customerAgingFullList.get("totalStateSales");
//
//                Snackbar snackbar = Snackbar.make(viewGroup, item.data[column + 1], Snackbar.LENGTH_LONG).setAction("Action", null);
//                //  customerFullList.get(item.data[column + 1] );
//                View sbView = snackbar.getView();
//                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.chart4));
//                //snackbar.show();
//            }
//            else {
//                fullAgainData = customerAgingFullList.get(item.data[column + 1]);
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
        }
    };

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
    private void setSalesData(JSONObject fullAgaingData) {
        final Map<Float, String> xAxisLable = new HashMap<>();

        //JSONArray fullSaleData= customerFullList.get("totalStateSales");
        //JSONArray fullSaleData= customerFullList.get(position);
        String moneyString="";
        try {
            if (fullAgaingData != null ) {
                Log.d(TAG,"fullAgaingData > "+fullAgaingData.length());
                int i=0;
                ArrayList<BarEntry> values = new ArrayList<>();
//                "noDue": 0.0,
//                        "overdue": -7450000.0,
//                        "dayGreater90": -7450000.0,
//                        "day30": 0.0,
//                        "day60": 0.0,
//                        "day90": 0.0,
//                        "day15": 0.0
                double overdue=fullAgaingData.getDouble("overdue");
                BigDecimal number = new BigDecimal(overdue);
                float overdueFinal = number.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                float xVal = (float) (i++);
                xAxisLable.put(xVal, "overdue");
           moneyString = formatter.format(Math.abs(overdueFinal));
           Log.d("@!@!@!@!@", moneyString);
                values.add(new BarEntry(xVal, Utility.decimal2PalceAsInput(Math.abs(overdueFinal)),moneyString));

                double noDue=fullAgaingData.getDouble("noDue");
                BigDecimal number1 = new BigDecimal(noDue);
                float noDue_Final = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                float xVal1 = (float) (i++);
                xAxisLable.put(xVal1, "noDue");
                values.add(new BarEntry(xVal1, Utility.decimal2PalceAsInput(Math.abs(noDue_Final))));


                double day15=fullAgaingData.getDouble("day15");
                BigDecimal number2 = new BigDecimal(day15);
                float day15Final = number2.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                float xVal2 = (float) (i++);
                xAxisLable.put(xVal2, "day15");
                values.add(new BarEntry(xVal2, Utility.decimal2PalceAsInput(Math.abs(day15Final))));

                double day30=fullAgaingData.getDouble("day30");
                BigDecimal number3 = new BigDecimal(day30);
                float day30Final = number3.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                float xVal3 = (float) (i++);
                xAxisLable.put(xVal3, "day30");
                values.add(new BarEntry(xVal3,Utility.decimal2PalceAsInput(Math.abs(day30Final))));

                double day60=fullAgaingData.getDouble("day60");
                BigDecimal number4 = new BigDecimal(day60);
                float day60Final = number4.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                float xVal4 = (float) (i++);
                xAxisLable.put(xVal4, "day60");
                values.add(new BarEntry(xVal4, Utility.decimal2PalceAsInput(Math.abs(day60Final))));


                double day90=fullAgaingData.getDouble("day90");
                BigDecimal number5 = new BigDecimal(day90);
                float day90Final = number5.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                float xVal5 = (float) (i++);
                xAxisLable.put(xVal5, "day90");
                values.add(new BarEntry(xVal5, Math.abs(day90Final)));

                double dayGreater90=fullAgaingData.getDouble("dayGreater90");
                BigDecimal number6 = new BigDecimal(dayGreater90);
                float dayGreater90Final = number6.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                float xVal6 = (float) (i++);
                xAxisLable.put(xVal6, "dayGreater90");
                moneyString = formatter.format(Math.abs(dayGreater90Final));
                values.add(new BarEntry(xVal6, Utility.decimal2PalceAsInput(Math.abs(dayGreater90Final))) );



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





}