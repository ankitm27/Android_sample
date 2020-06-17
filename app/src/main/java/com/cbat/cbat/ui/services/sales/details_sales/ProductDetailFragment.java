package com.cbat.cbat.ui.services.sales.details_sales;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.cbat.cbat.R;
import com.cbat.cbat.adapter.TableFixHeaderAdapter;
import com.cbat.cbat.adapter.TableFixHeadersAdapterFactory;
import com.cbat.cbat.adapter.original_sortable.ItemSortable;
import com.cbat.cbat.adapter.original_sortable.NexusWithImage;
import com.cbat.cbat.adapter.original_sortable.OriginalBodyCellViewGroup;
import com.cbat.cbat.adapter.original_sortable.OriginalFirstBodyCellViewGroup;
import com.cbat.cbat.adapter.original_sortable.OriginalSortableTableFixHeader;
import com.cbat.cbat.ui.navigation.CustomerDetailActivity;
import com.cbat.cbat.util.DayAxisValueFormatter;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jp.gr.java_conf.androtaku.geomap.GeoMapView;


public class ProductDetailFragment extends Fragment {
    String TAG = "ProductDetailFragment";

    protected Typeface tfLight;
    GeoMapView geoMapView;
    LinearLayout totalSalesLayout;
    LinearLayout customerChartLayout;
    LinearLayout barchats;
    View chartLayout;
    String stateName;
    Map<String, Double> cutomerSalesMap;
    Map<String, JSONArray> customerFullList;
    JSONArray fullSaleData = new JSONArray();
    TabLayout tabLayout;
    ImageView crImage;
    ImageView lakhImage;
    ImageView thImage;
    ImageView top5Image;
    ImageView top10Image;
    ImageView top15Image;
    ImageView allImage;
    String productColor;
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup> setClickListenerBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup>() {
        @Override
        public void onClickItem(NexusWithImage item, OriginalBodyCellViewGroup viewGroup, int row, int column) {

            //Snackbar.make(viewGroup, "Yes we do it " + item.data[0] + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();

            //Snackbar.make(viewGroup, "Yes we do it " + item.data[column + 1] + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();
            Intent myIntent = new Intent(getActivity(), CustomerDetailActivity.class);
            GlobalClass.productName = item.data[0];
            GlobalClass.currentFrgmentMain = item.data[0];
            GlobalClass.currentFrgment = item.data[0];
            GlobalClass.backFrgment = "Product Wise Sales";
            //GlobalClass.title = "Customer List for " + item.data[0];
            if(column>=2) {
                GlobalClass.month = GlobalClass.monthList.get(column - 2);
                GlobalClass.title = "Customer List of " +GlobalClass.month+ " for " + item.data[0];
                System.out.println("Month >>>"+ GlobalClass.month);
            }else{
                GlobalClass.month="";
                GlobalClass.title = "Customer List for " + item.data[0];
            }
            startActivity(myIntent);
        }
    };
    private LayoutInflater layoutInflater;
    private TableFixHeaders tableFixHeaders;
    private TableFixHeadersAdapterFactory tableFixHeadersAdapterFactory;
    private BarChart chart;
    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup> setClickListenerFirstBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup>() {
        @Override
        public void onClickItem(NexusWithImage item, OriginalFirstBodyCellViewGroup viewGroup, int row, int column) {


            if (item.data[column + 1].equalsIgnoreCase("Total Sales")) {
                fullSaleData = GlobalClass.productFullList.get("totalProductSales");
                productColor=GlobalClass.productColorMap.get("Total Sales");
                Snackbar snackbar = Snackbar.make(viewGroup, item.data[column + 1], Snackbar.LENGTH_LONG).setAction("Action", null);
                View sbView = snackbar.getView();
                sbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                sbView.setBackgroundColor(Color.parseColor(productColor));
                snackbar.show();
            } else {
                fullSaleData = GlobalClass.productFullList.get(item.data[column + 1]);
//                    productColor = GlobalClass.productColorMap.get(item.data[column + 1]);
                if(productColor==null || productColor.isEmpty()){
        //            productColor = GlobalClass.productColorMap.get("Product");
                }
                Snackbar snackbar = Snackbar.make(viewGroup, "Product Name is " + item.data[column + 1], Snackbar.LENGTH_LONG).setAction("Action", null);
                //  customerFullList.get(item.data[column + 1] );
                View sbView = snackbar.getView();
//                sbView.setBackgroundColor( Color.parseColor(productColor));
                sbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                //snackbar.show();
            }
            tabLayout.getTabAt(1).select();
            setSalesData(1, fullSaleData);
            chart.animateX(2000);
            chart.refreshDrawableState();

        }
    };
    private ProgressDialog progressDialog;
    //MPLine
    private LineChart lineChart;

    public static ProductDetailFragment newInstance() {
        return new ProductDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        //stateName = getArguments().getString("stateName");
        //GlobalClass.stateName = getArguments().getString("stateName");
        if (GlobalClass.productFullList != null && !GlobalClass.productFullList.isEmpty()) {
            Log.d(TAG, "Other Size >> " + GlobalClass.productFullList.size());
            customerFullList = GlobalClass.productFullList;

        } else {
            customerFullList = new HashMap<>();
        }
        tfLight = Typeface.createFromAsset(getActivity().getAssets(), "arial.ttf");

        View v = inflater.inflate(R.layout.customer_detail_fragment, container, false);
        //heighSetting( v );
        tableFixHeaders = (TableFixHeaders) v.findViewById(R.id.tablefixheaders);
        // tableFixHeaders = new TableFixHeaders(getContext());
        tableFixHeadersAdapterFactory = new TableFixHeadersAdapterFactory(getContext());
        createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
        //final ProgressBar progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        final FrameLayout totalSalesContainer = (FrameLayout) v.findViewById(R.id.totalSalesContainer);
        totalSalesLayout = (LinearLayout) v.findViewById(R.id.totalSalesLayout);
        customerChartLayout = (LinearLayout) v.findViewById(R.id.customerChartLayout);
        chartLayout = layoutInflater.inflate(R.layout.chart_tab_layout, customerChartLayout, false);
        tabLayout = (TabLayout) chartLayout.findViewById(R.id.chart_filter_tabs);
        barchats = (LinearLayout) chartLayout.findViewById(R.id.chart_container);
        //fullSaleData= customerFullList.get((customerFullList.size()-1));
        if (!GlobalClass.productFullList.isEmpty() && GlobalClass.productFullList.containsKey("totalProductSales")) {
            fullSaleData = customerFullList.get("totalProductSales");
        } else {
            fullSaleData = new JSONArray();
        }
        barChartCreate(tabLayout);

        crImage = v.findViewById(R.id.cr);
        crImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.currecyPrefix = "CR";
                if (getFragmentManager() != null) {
                    //  Fragment  frm=getFragmentManager().findFragmentByTag(TAG);
                    Fragment cutomerFragment = new ProductDetailFragment();
                    //((CustomerDetailFragment) cutomerFragment).setMap(cutomerSalesMap);
                    //((CustomerDetailFragment) cutomerFragment).setFullMap(customerFullList);
                    // GlobalClass.customerFullList=customerFullList;
                    //Bundle bundle = new Bundle();
                    // bundle.putString("stateName", stateName);
                    //  GlobalClass.stateName=stateName;
                    // cutomerFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.layout_service_fragment_content_product, cutomerFragment)
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
                    Fragment cutomerFragment = new ProductDetailFragment();
                    //((CustomerDetailFragment) cutomerFragment).setMap(cutomerSalesMap);
                    //((CustomerDetailFragment) cutomerFragment).setFullMap(customerFullList);
                    // GlobalClass.customerFullList=customerFullList;
                    //Bundle bundle = new Bundle();
                    // bundle.putString("stateName", stateName);
                    //  GlobalClass.stateName=stateName;
                    // cutomerFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.layout_service_fragment_content_product, cutomerFragment)
                            .commit();
                } else {
                    createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                    tabLayout.removeAllTabs();
                    barChartCreate(tabLayout);
                    resetCurrencyImage();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
                }

//                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
//                tabLayout.removeAllTabs();
//                barChartCreate(tabLayout);
//                resetCurrencyImage();
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
                    Fragment cutomerFragment = new ProductDetailFragment();
                    //((CustomerDetailFragment) cutomerFragment).setMap(cutomerSalesMap);
                    //((CustomerDetailFragment) cutomerFragment).setFullMap(customerFullList);
                    // GlobalClass.customerFullList=customerFullList;
                    //Bundle bundle = new Bundle();
                    // bundle.putString("stateName", stateName);
                    //  GlobalClass.stateName=stateName;
                    // cutomerFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.layout_service_fragment_content_product, cutomerFragment)
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
                    Fragment cutomerFragment = new ProductDetailFragment();
                    //((CustomerDetailFragment) cutomerFragment).setMap(cutomerSalesMap);
                    //((CustomerDetailFragment) cutomerFragment).setFullMap(customerFullList);
                    // GlobalClass.customerFullList=customerFullList;
                    //Bundle bundle = new Bundle();
                    // bundle.putString("stateName", stateName);
                    //  GlobalClass.stateName=stateName;
                    // cutomerFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.layout_service_fragment_content_product, cutomerFragment)
                            .commit();
                } else {
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
                    Fragment cutomerFragment = new ProductDetailFragment();
                    //((CustomerDetailFragment) cutomerFragment).setMap(cutomerSalesMap);
                    //((CustomerDetailFragment) cutomerFragment).setFullMap(customerFullList);
                    // GlobalClass.customerFullList=customerFullList;
                    //Bundle bundle = new Bundle();
                    // bundle.putString("stateName", stateName);
                    //  GlobalClass.stateName=stateName;
                    // cutomerFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.layout_service_fragment_content_product, cutomerFragment)
                            .commit();
                } else {
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

//                Toast.makeText(getContext(),
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
                    Fragment cutomerFragment = new ProductDetailFragment();
                    //((CustomerDetailFragment) cutomerFragment).setMap(cutomerSalesMap);
                    //((CustomerDetailFragment) cutomerFragment).setFullMap(customerFullList);
                    // GlobalClass.customerFullList=customerFullList;
                    //Bundle bundle = new Bundle();
                    // bundle.putString("stateName", stateName);
                    //  GlobalClass.stateName=stateName;
                    // cutomerFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.layout_service_fragment_content_product, cutomerFragment)
                            .commit();
                } else {
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
                    Fragment cutomerFragment = new ProductDetailFragment();
                    //((CustomerDetailFragment) cutomerFragment).setMap(cutomerSalesMap);
                    //((CustomerDetailFragment) cutomerFragment).setFullMap(customerFullList);
                    // GlobalClass.customerFullList=customerFullList;
                    //Bundle bundle = new Bundle();
                    // bundle.putString("stateName", stateName);
                    //  GlobalClass.stateName=stateName;
                    // cutomerFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.layout_service_fragment_content_product, cutomerFragment)
                            .commit();
                } else {
                    fullSaleData = customerFullList.get("totalProductSales");
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

    public void barChartCreate(final TabLayout tabLayout) {
        customerChartLayout.removeAllViews();
        //final TabLayout tabLayout = (TabLayout) chartLayout.findViewById(R.id.chart_filter_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Day"));
        tabLayout.addTab(tabLayout.newTab().setText("Mth"));
        tabLayout.addTab(tabLayout.newTab().setText("Qtr"));
       // tabLayout.addTab(tabLayout.newTab().setText("YTD"));
       // tabLayout.addTab(tabLayout.newTab().setText("3YTD"));s
        // tabLayout.addTab(tabLayout.newTab().setText("5Y"));
        chart = new BarChart(getActivity());
        ViewGroup.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        chart.setLayoutParams(lp);
        chart.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        chart.setMinimumHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
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
     //   ValueFormatter custom = new MyValueFormatter("");

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setLabelCount(6, false);
        //leftAxis.setAxisMinValue(-1f);
        leftAxis.setDrawAxisLine(false);
  //      leftAxis.setValueFormatter(custom);
        //leftAxis.setGranularity(2f);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(5f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setValueFormatter(new ValueFormatter() {
            // private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
            @Override
            public String getFormattedValue(float value) {
                Log.d("bar chrt", value+" chart!@!!!!!");
                DecimalFormat mFormat = new DecimalFormat("###.#");
                return mFormat.format(value) + " " + GlobalClass.currecyPrefix;
            }
        });

        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawGridLines(true);
    //    leftAxis.setAxisMaximum(30000.0f);
        leftAxis.setGridColor(getResources().getColor(R.color.gray));

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(tfLight);
        rightAxis.setLabelCount(10, false);
       // rightAxis.setValueFormatter(custom);
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
                new ItemSortable("Total("+GlobalClass.currecyPrefix+")"),
                new ItemSortable("% Contri"),
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
                new ItemSortable("Product"),

        };
        return headers;
    }

    public List<NexusWithImage> getCustomerSixMBody() {
        List<Integer> resImages = Arrays.asList();

        List<NexusWithImage> items = new ArrayList<>();
        String type = "Total Sales";
        // items.add(new NexusWithImage(type, resImages));
        GlobalClass.productsList.clear();
//        if(GlobalClass.customerFullList!=null && !GlobalClass.customerFullList.isEmpty()) {
//            customerFullList = GlobalClass.customerFullList;
//        }
        int count = 0;
        Float[] dataTableTotal = null;
        String[] dataTableT = null;
        if (customerFullList!=null && !customerFullList.isEmpty()){

            boolean totalFlag = true;

            for (Map.Entry<String, JSONArray> set : customerFullList.entrySet()) {

                try {
                    if (count < GlobalClass.sortingOn || GlobalClass.sortingOn == -1) {

                        Log.d(TAG, "Key >" + set.getKey());
                        if (!set.getKey().equalsIgnoreCase("totalProductSales")) {
                            JSONArray dataEveryMonthly = set.getValue().getJSONArray(1);
                            JSONArray dataYearly = set.getValue().getJSONArray(3);
                            JSONArray contriData = set.getValue().getJSONArray(4);
                           // JSONArray dataMonthly =  set.getValue().getJSONArray(5);
                            JSONArray dataMonthly =  set.getValue().getJSONArray(5);
                            String[] dataTable = new String[dataMonthly.length() + 3];
                            if(totalFlag) {
                                dataTableTotal = new Float[dataMonthly.length() + 3];
                            }
                            dataTableT= new String[dataMonthly.length() + 3];
                            JSONObject elementYearly;
                            boolean yearFlag = true;
                            boolean contriFlag = true;

                            double totalSales = 0;
                            int j = 0;
                            int k = 2;
                            String indicatorColor = null;
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
                                    String moneyString = formatter.format(Math.abs(totalSales));
                                    dataTableTotal[0] =(dataTableTotal[0]!=null?dataTableTotal[0]:new Float(0))+new Float(totalSales);
                                    //dataTable[j++] = Utility.decimal2Palce(Math.abs(totalSales));
                                    dataTable[j++]=moneyString;
                                    GlobalClass.productsList.add(set.getKey());
                                }
                                if (contriFlag) {
                                    elementYearly = contriData.getJSONObject(i);
                                    contriFlag = false;
                                    double yVal = elementYearly.getDouble("value");
                                    BigDecimal number1 = new BigDecimal(yVal);
                                    //totalSales = number1.floatValue() / 10000000;
                                    totalSales = number1.floatValue(); // GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                    // Log.d(TAG, "set.getKey() >" + set.getKey());
                                    //Log.d(TAG, "yVal >" + yVal);
                                    // dataTable[j++] = String.valueOf(set.getKey());
                                    dataTable[j++] = Utility.decimal2Palce(Math.abs(totalSales));
                                    dataTableTotal[1] =(dataTableTotal[1]!=null?dataTableTotal[1]:new Float(0))+new Float(totalSales);
                                    //dataTable[j++] = "NA";
                                }

                                JSONObject element = dataMonthly.getJSONObject(i);
                                int yVal = element.getInt("value");
                                BigDecimal number1 = new BigDecimal(yVal);
                                double debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                String moneyString = formatter.format(Utility.decimal2PalceAsInput(Math.abs(debitFloat)));
                                ;
//                                dataTableTotla[k] =
//                                        String.valueOf(Float.parseFloat(dataTableTotla[k]!=null && !dataTableTotla[k].isEmpty()?dataTableTotla[k]:"0")+debitFloat);
                                Log.d(TAG, dataTableTotal[k]+" <> "+k+" dataTableTotla[k] >##########" + number1.floatValue());
                                dataTableTotal[k] =(dataTableTotal[k]!=null?dataTableTotal[k]:new Float(0))+number1.floatValue();
                                k++;
                                dataTable[j++] =moneyString;
                                //dataTable[j++] = Utility.decimal2Palce(Math.abs(debitFloat));

                           //     indicatorColor = GlobalClass.productColorMap.get(set.getKey());
                                if(indicatorColor==null || indicatorColor.isEmpty()){
                              //      indicatorColor = GlobalClass.productColorMap.get("Product");
                                }

                            }
                            items.add(new NexusWithImage(type, dataTable, indicatorColor));
                            Log.d(TAG, "dataTable >" + dataTable.toString());
                            Log.d(TAG, "dataTable length >" + dataTable.length);
                        }
                        count++;
                        totalFlag=false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

//            Log.d(TAG, "dataTableTotla >" + dataTableTotal.toString());
//            Log.d(TAG, "dataTable length >" + dataTableTotal.length);
            if( GlobalClass.sortingOn >0) {
                int l=1;
                dataTableT[0]="Total";
//                dataTableT[1]="100";
//                dataTableT[2]="2";
                for(Float f:dataTableTotal){
                    if(l<dataTableTotal.length && l>2) {
                        dataTableT[l] = formatter.format(Utility.decimal2PalceAsInput(Math.abs(f/ GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix))));//String.valueOf(f);
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
                JSONArray setVal = GlobalClass.productFullList.get("totalProductSales");
                JSONArray dataMonthly = setVal.getJSONArray(2);
                JSONArray dataYearly = setVal.getJSONArray(4);
                // JSONArray contriData = setVal.getJSONArray(4);
                //JSONArray contriData = new JSONArray();
                String[] dataTable = new String[dataMonthly.length() + 3];
                JSONObject elementYearly;
                boolean yearFlag = true;
                float totalSales = 0;
                boolean contriFlag = true;
                int j = 0;
                String indicatorColorT = null;
                for (int i = 0; i < dataMonthly.length(); i++) {
                    if (yearFlag) {
                        elementYearly = dataYearly.getJSONObject(i);
                        yearFlag = false;
                        int yVal = elementYearly.getInt("value");
                        BigDecimal number1 = new BigDecimal(yVal);
                        totalSales = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                        //Log.d(TAG, "set.getKey() >" + totalStateSales);
                        Log.d(TAG, "totalSales >" + totalSales);
                        dataTable[j++] = String.valueOf("Total");
                        String moneyString = formatter.format(Utility.decimal2PalceAsInput(Math.abs(totalSales)));
                        dataTable[j++] =moneyString;

                       // dataTable[j++] = Utility.decimal2Palce(Math.abs(totalSales));

                    }
                    if (contriFlag) {
//                         elementYearly = contriData.getJSONObject(i);
                        contriFlag = false;
//                         double yVal = elementYearly.getDouble("value");
//                        BigDecimal number1 = new BigDecimal(yVal);
//                        //totalSales = number1.floatValue() / 10000000;
//                        totalSales = number1.floatValue(); // GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
//                        // Log.d(TAG, "set.getKey() >" + set.getKey());
//                        //Log.d(TAG, "yVal >" + yVal);
//                        // dataTable[j++] = String.valueOf(set.getKey());
//                         dataTable[j++] = Utility.decimal2Palce(Math.abs(totalSales));
                        dataTable[j++] = "100";
                    }
                    JSONObject element = dataMonthly.getJSONObject(i);
                    int yVal = element.getInt("value");
                    BigDecimal number1 = new BigDecimal(yVal);
                    double debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                    Log.d(TAG, "debitFloat >" + debitFloat);
                    String moneyString = formatter.format(Utility.decimal2PalceAsInput(Math.abs(debitFloat)));
                    dataTable[j++] =moneyString;
                    //dataTable[j++] = Utility.decimal2Palce(Math.abs(debitFloat));
                   indicatorColorT = GlobalClass.productColorMap.get("Total Sales");
                    if(indicatorColorT==null || indicatorColorT.isEmpty()){
                        indicatorColorT = GlobalClass.productColorMap.get("Product");
                    }
                }
                items.add(new NexusWithImage(type, dataTable, indicatorColorT));
                Log.d(TAG, "dataTable >" + dataTable.toString());
                Log.d(TAG, "dataTable length >" + dataTable.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
     return items;

    }

    private void setSalesData(int tabType, JSONArray fullSaleData) {
        final Map<Float, String> xAxisLable = new HashMap<>();

        //JSONArray fullSaleData= customerFullList.get("totalStateSales");
        //JSONArray fullSaleData= customerFullList.get(position);
        try {
            //Log.d(TAG,fullSaleData.length()+" >><<");
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
                    float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);


                    // Log.d(TAG, "lableName > " + lableName);
                    // Log.d(TAG, "debitFloat > " + debitFloat);

                    values.add(new BarEntry(xVal, Utility.decimal2PalceAsInput(Math.abs(debitFloat))));

                }

                BarDataSet set1;


                set1 = new BarDataSet(values, "");
                set1.setDrawIcons(false);
                if(productColor!=null && !productColor.isEmpty()) {
                    set1.setColor(Color.parseColor(productColor));

                }else{
                    set1.setColor(getActivity().getResources().getColor(R.color.chart1));

                }
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
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