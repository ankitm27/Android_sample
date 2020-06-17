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
import com.cbat.cbat.ui.navigation.LedgerDetailActivity;
import com.cbat.cbat.ui.services.sales.LedgerReceivableDetailActivity;
import com.cbat.cbat.util.GlobalClass;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jp.gr.java_conf.androtaku.geomap.GeoMapView;


public class InvoiceCollectionDetailFragment extends Fragment {
    String TAG="InvoiceDetailFragment";
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
    List<JSONObject> customerInvoiceFullList;
    Map<String, JSONArray> customerFullList;
    JSONArray fullSaleData = new JSONArray();
    TabLayout tabLayout;
    String stateName,BackFrgment,CurrentFrgment,CurrentFrgmentMain,CutomerName;
    private ProgressDialog progressDialog;
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    public static InvoiceCollectionDetailFragment newInstance() {
        return new InvoiceCollectionDetailFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tfLight = Typeface.createFromAsset(getActivity().getAssets(), "arial.ttf");

        View v = inflater.inflate(R.layout.invoice_detail_fragment, container, false);
        //heighSetting( v );
        tableFixHeaders = (TableFixHeaders) v.findViewById(R.id.tablefixheaders);
       // tableFixHeaders = new TableFixHeaders(getContext());
        tableFixHeadersAdapterFactory = new TableFixHeadersAdapterFactory(getContext());
        createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE,getCustomerSixMBody());
        //final ProgressBar progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        final FrameLayout totalSalesContainer = (FrameLayout)v.findViewById(R.id.totalSalesContainer);
        totalSalesLayout=(LinearLayout)v.findViewById(R.id.totalSalesLayout);
        GlobalClass.pdfDataTab="invoiceDetails";
        customerChartLayout = (LinearLayout) v.findViewById(R.id.customerChartLayout);
        chartLayout = layoutInflater.inflate(R.layout.chart_tab_layout, customerChartLayout, false);
        tabLayout = (TabLayout) chartLayout.findViewById(R.id.chart_filter_tabs);
        barchats = (LinearLayout) chartLayout.findViewById(R.id.chart_container);

        if (GlobalClass.customerInvoiceChartData!=null) {
            //Log.d(TAG, "Chart Data size " + GlobalClass.customerInvoiceChartData.length());
            fullSaleData = GlobalClass.customerInvoiceChartData;
        } else {
            fullSaleData = new JSONArray();
        }
        barChartCreate(tabLayout);
//        stateName = getArguments().getString("stateName");
//        BackFrgment = getArguments().getString("BackFrgment");
//        CurrentFrgment = getArguments().getString("CurrentFrgment");
//        CurrentFrgmentMain = getArguments().getString("CurrentFrgmentMain");
//        CutomerName = getArguments().getString("CutomerName");
//

        return v;
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
                 new ItemSortable("Inv.No"),
           // new ItemSortable("Credit Note"),
                 new ItemSortable("Rcpt"),
                    new ItemSortable("Date"),
                 new ItemSortable("Ref.No"),
                // new ItemSortable("Credit Period"),
                 //new ItemSortable("Arrears Days"),
                 new ItemSortable("Cust.Code")
        };
         return headers;
    }

    public List<NexusWithImage> getCustomerSixMBody(){
        List<Integer> resImages = Arrays.asList();

        List<NexusWithImage> items = new ArrayList<>();
        String type = "Invoice";
        try {
            if(GlobalClass.customerInvoiceFullList!=null &&   !GlobalClass.customerInvoiceFullList.isEmpty()){
                customerInvoiceFullList=  GlobalClass.customerInvoiceFullList;
            }
            if(customerInvoiceFullList!=null) {
                for (int i = 0; i < customerInvoiceFullList.size(); i++) {
                    JSONObject element = customerInvoiceFullList.get(i);
                    int yVal = element.getInt("value");
                    BigDecimal number1 = new BigDecimal(yVal);
                    float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);

                    String dueDate= element.getString("dueDate");
                   // float creditPeroid=(new BigDecimal(element.getInt("credirPeroid"))).floatValue()/ GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                    String creditPeroidStr=String.valueOf(element.getInt("credirPeroid"));
                    int arrearDaysInt=element.getInt("arrearDays");
                    String arrearDays=String.valueOf(arrearDaysInt);
//                    Log.d(TAG, "debitFloat >" + debitFloat);
//                    Log.d(TAG, "dueDate >" + dueDate);
//                    Log.d(TAG, "creditPeroid >" + creditPeroidStr);
//                    Log.d(TAG, "arrearDays >" + arrearDays);
                    String indicatorColor="";
//                    if(arrearDaysInt<0){
//                        indicatorColor="#FF0000";
//                    }else{
//                        indicatorColor="#008000";
//                    }
                    String moneyString = formatter.format(Math.abs(debitFloat));
                    String[] data=new String[]{GlobalClass.currentFrgmentMain, element.getString("name"), moneyString, element.getString("voucherDate"), element.getString("againrefNumber"),"", "", element.getString("voucherGuid"), "", "", "", "", "", ""};
                    items.add(new NexusWithImage(type, data,indicatorColor));
                }
            }else{
                //items.add(new NexusWithImage(type, GlobalClass.currentFrgmentMain, "No Data","No Data", "No Data", "No Data", "No Data", "No Data", "No Data", "", "", "", "", "", ""));

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return items;
    }

    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup> setClickListenerBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup>() {
            @Override
            public void onClickItem(NexusWithImage item, OriginalBodyCellViewGroup viewGroup, int row, int column) {
                //Snackbar.make(viewGroup, "Yes we do it " + item.data[3] + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();
            }
        };

    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup> setClickListenerFirstBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup>() {
        @Override
        public void onClickItem(NexusWithImage item, OriginalFirstBodyCellViewGroup viewGroup, int row, int column) {
            //Snackbar.make(viewGroup, "Cutomer Name is " + item.data[column + 2] + " (" + row + "," + column + ")"+"><>>"+item.data[5]+item.data[6]+item.data[7], Snackbar.LENGTH_SHORT).show();
            Intent myIntent = new Intent(getActivity(), LedgerReceivableDetailActivity.class);
            GlobalClass.title="Summay of Voucher "+item.data[column + 2];
            GlobalClass.currentFrgmentFinal=item.data[column + 2];
            GlobalClass.voucherNo=item.data[7];
            startActivity(myIntent);
        }
    };

    public void barChartCreate(final TabLayout tabLayout) {
        customerChartLayout.removeAllViews();
        tabLayout.removeAllTabs();
        //final TabLayout tabLayout = (TabLayout) chartLayout.findViewById(R.id.chart_filter_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Day"));
        tabLayout.addTab(tabLayout.newTab().setText("Mth"));
        tabLayout.addTab(tabLayout.newTab().setText("Qtr"));
        // tabLayout.addTab(tabLayout.newTab().setText("YTD"));
        // tabLayout.addTab(tabLayout.newTab().setText("LTM"));

        chart = new BarChart(getActivity());
        //chart.removeAllViews();
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
        //  ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart, 2019);

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
        // ValueFormatter custom = new MyValueFormatter("");

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.resetAxisMaximum();
        leftAxis.resetAxisMinimum();
        leftAxis.setTypeface(tfLight);
        leftAxis.setLabelCount(6, false);
        //leftAxis.setAxisMinValue(-1f);
        leftAxis.setDrawAxisLine(false);
        //leftAxis.setValueFormatter(custom);
        //leftAxis.setGranularity(2f);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(5f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        leftAxis.setValueFormatter(new ValueFormatter() {
            // private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
            @Override
            public String getFormattedValue(float value) {
                DecimalFormat mFormat = new DecimalFormat("###.#");
             //   System.out.println("value >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+value);
                //float debitFloat = value / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
              //  System.out.println("GlobalClass.currecyPrefix >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>."+GlobalClass.currecyPrefix);
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
        //  rightAxis.setValueFormatter(custom);
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
                // Toast.makeText(getContext(),
                //  "Tab position > "+tabLayout.getSelectedTabPosition(), Toast.LENGTH_SHORT).show();
//                if(tabLayout.getSelectedTabPosition()==2) {
//                    setSalesData(tabLayout.getSelectedTabPosition()+1, fullSaleData);
//                }else{
//                    setSalesData(tabLayout.getSelectedTabPosition(), fullSaleData);
//                }
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
      //  Log.d("selectedtab", String.valueOf(tabLayout.getSelectedTabPosition()));

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
                JSONArray tabData = fullSaleData.getJSONArray(tabType);
                Log.d(TAG, "tabData > " + tabData.length());
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


                  //  Log.d(TAG, "lableName > " + lableName);
                  //  Log.d(TAG, "debitFloat > " + debitFloat);

                    values.add(new BarEntry(xVal, Utility.decimal2PalceAsInput(Math.abs(debitFloat))));

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
                     //   Log.d(TAG, "lable >><<$$$$$$$$$$$$$$$$$$$$$$$$$$$ " + lable);
                        //  long millis = TimeUnit.HOURS.toMillis((long) value);
                        //  return mFormat.format(new Date(millis));
                        return lable != null ? lable : "";
                        // return value+"";

                    }
                });
                // chart.removeAllViews();
                chart.setData(data);
                chart.animateX(2000);
                chart.refreshDrawableState();
            }else{
               // Log.d(TAG, "lable >><< " + fullSaleData);
            }
            //}
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}