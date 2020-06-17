package com.cbat.cbat.ui.services.sales.details_sales;

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
import com.cbat.cbat.util.DayAxisValueFormatter;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.MyValueFormatter;
import com.cbat.cbat.util.PichartMarkerView;
import com.cbat.cbat.util.Utility;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
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

import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.gr.java_conf.androtaku.geomap.GeoMapView;


public class SalesVsAchievmentDetailFragment extends Fragment {
    String TAG="CustomerDetailFragment";
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
    Map<String, Double> cutomerSalesMap;
    Map<String, JSONArray> customerFullList;
    JSONArray fullSaleData = new JSONArray();
    TabLayout tabLayout;


    public static SalesVsAchievmentDetailFragment newInstance() {
        return new SalesVsAchievmentDetailFragment();
    }

    //    public  void setMap(Map<String,Double> cutomerSalesMap){
//        Log.d(TAG,"Size >> "+cutomerSalesMap.size());
//        this.cutomerSalesMap=cutomerSalesMap;
//    }
//    public  void setFullMap(Map<String,JSONArray> customerFullList){
//        //Log.d(TAG,"Size >> "+customerFullList.size());
//        fullSaleData= GlobalClass.customerFullList.get("totalStateSales");
//        //Log.d(TAG,"fullSaleData >> "+fullSaleData.length());
//        this.customerFullList=customerFullList;
//    }
    //MPLine
    private LineChart lineChart;
    private Unbinder unbinder;

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
        if (GlobalClass.salesVsAchievmentFullList != null && !GlobalClass.salesVsAchievmentFullList.isEmpty()) {
            Log.d(TAG, "Other Size >> " + GlobalClass.salesVsAchievmentFullList.size());
            customerFullList = GlobalClass.salesVsAchievmentFullList;

        } else {
            customerFullList = new HashMap<>();
        }
        GlobalClass.sortingOn = -1;
        tfLight = Typeface.createFromAsset(getActivity().getAssets(), "arial.ttf");

        View v = inflater.inflate(R.layout.sales_vs_achievment_fragment, container, false);
        unbinder = ButterKnife.bind(getContext(), v);
        //heighSetting( v );
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


        return v;
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
        unbinder.unbind();
    }

    public void barChartCreate(final TabLayout tabLayout) {
        customerChartLayout.removeAllViews();
        //final TabLayout tabLayout = (TabLayout) chartLayout.findViewById(R.id.chart_filter_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Day"));
        tabLayout.addTab(tabLayout.newTab().setText("Mth"));
        tabLayout.addTab(tabLayout.newTab().setText("Qtr"));
       // tabLayout.addTab(tabLayout.newTab().setText("YTD"));
      //  tabLayout.addTab(tabLayout.newTab().setText("LTM"));

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
        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart, 2019);

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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabLayout.getSelectedTabPosition()==2) {
                    setSalesData(tabLayout.getSelectedTabPosition()+1, fullSaleData);
                }else{
                    setSalesData(tabLayout.getSelectedTabPosition(), fullSaleData);
                }

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





    private void setSalesData(int tabType, JSONArray fullSaleData) {
        final Map<Float, String> xAxisLable = new HashMap<>();

        //JSONArray fullSaleData= customerFullList.get("totalStateSales");
        //JSONArray fullSaleData= customerFullList.get(position);
        try {
            if (fullSaleData != null && fullSaleData.length() > 0) {
                float limit=0;
                JSONArray tabData = fullSaleData.getJSONArray(tabType);
                if(tabType==0){
                    limit=GlobalClass.salesTarget.get("day");

                }else if(tabType==1){
                    limit=GlobalClass.salesTarget.get("month");

                }else if(tabType==2){
                    limit=GlobalClass.salesTarget.get("quaterly");

                }else if(tabType==3){
                    limit=GlobalClass.salesTarget.get("yearly");

                }
                Log.d(TAG,"limit "+limit);
                ArrayList<BarEntry> values = new ArrayList<>();
                ArrayList<BarEntry> valuesLimit = new ArrayList<>();

                for (int i = 0; i < tabData.length(); i++) {
                    JSONObject element = tabData.getJSONObject(i);
                    float xVal = (float) (i);
                    int yVal = element.getInt("value");
                    String lableName = element.getString("name");

                    BigDecimal number1 = new BigDecimal(yVal);
                    xAxisLable.put(xVal, lableName);
                    //float debitFloat = number1.floatValue() / 10000000;
                    float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);

                    values.add(new BarEntry(xVal,Utility.decimal2PalceAsInput(Math.abs(debitFloat))));
                 //   valuesLimit.add(new BarEntry(xVal,limit));


                }
                BigDecimal number1 = new BigDecimal(limit);
                float limitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                BarDataSet set1 = new BarDataSet(values, "");
                set1.setDrawIcons(false);
                set1.setColor(getActivity().getResources().getColor(R.color.chart1));

                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);

//                BarDataSet set2 = new BarDataSet(valuesLimit, "");
//                set2.setDrawIcons(false);
//                set2.setColor(getActivity().getResources().getColor(R.color.chart1));
//                ArrayList<IBarDataSet> dataSets2 = new ArrayList<>();
//                dataSets2.add(set2);

                //BarData data = new BarData(dataSets,dataSets2);
                float groupSpace = 0.06f;
                float barSpace = 0.02f; // x2 dataset
                float barWidth = 0.45f; // x2 dataset

                BarData data = new BarData(dataSets);
                data.setValueTextSize(10f);
                data.setValueTypeface(tfLight);
               // data.setBarWidth(.5f);
               // data.setDrawValues(false);
                data.setBarWidth(barWidth);

                // make this BarData object grouped
                //data.groupBars(0, groupSpace, barSpace); // start at x =
                LimitLine ll = new LimitLine(Utility.decimal2PalceAsInput(limitFloat), "Target("+Utility.decimal2Palce(limitFloat)+" "+GlobalClass.currecyPrefix+")");
                ll.setLineWidth(4f);
                ll.setTextSize(16f);
                ll.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                YAxis leftAxis = chart.getAxisLeft();
                leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
                leftAxis.addLimitLine(ll);
                //chart.getAxisLeft().addLimitLine(ll);
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