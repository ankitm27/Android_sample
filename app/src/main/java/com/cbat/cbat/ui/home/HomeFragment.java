package com.cbat.cbat.ui.home;

import android.Manifest;
import android.animation.PropertyValuesHolder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

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
import com.cbat.cbat.adapter.ListPopupWindowAdapter;
import com.cbat.cbat.adapter.SliderPagerAdapter;
import com.cbat.cbat.model.ListPopupItem;
import com.cbat.cbat.ui.BaseFragment;
import com.cbat.cbat.ui.navigation.MainActivity;
import com.cbat.cbat.ui.navigation.ServicesDetailActivity;
import com.cbat.cbat.ui.widgets.NavigationDrawerFragment;
import com.cbat.cbat.util.ChartUtilsCustom;
import com.cbat.cbat.util.Constants;
import com.cbat.cbat.util.DayAxisValueFormatter;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.PichartMarkerView;
import com.cbat.cbat.util.SalesVsCollectionMarkerView;
import com.cbat.cbat.util.SalesVsCollectionValueFormatter;
import com.cbat.cbat.util.Utility;
import com.cbat.cbat.util.XYMarkerView;
import com.db.chart.animation.Animation;
import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.BarSet;
import com.db.chart.renderer.XRenderer;
import com.db.chart.renderer.YRenderer;
import com.db.chart.tooltip.Tooltip;
import com.db.chart.util.Tools;
import com.db.chart.view.BarChartView;
import com.db.chart.view.HorizontalBarChartView;
import com.db.chart.view.HorizontalStackBarChartView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class HomeFragment extends BaseFragment implements OnChartValueSelectedListener {


    private static final String TAG = "HomeFragment";

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private PieChartData data;

    private boolean hasLabels = true;
    private boolean hasLabelsOutside = false;
    private boolean hasCenterCircle = false;
    private boolean hasCenterText1 = true;
    private boolean hasCenterText2 = false;
    private boolean isExploded = false;
    private boolean hasLabelForSelected = false;

    private static final int DEFAULT_DATA = 0;

    private int dataType = DEFAULT_DATA;
    Map<String, String> slider_image_map;
    Map<String, Map<String, JSONArray>> charDataMap;

    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<String> slider_image_list;
    private TextView[] dots;
    int page_position = 0;
    TextView totalSalesVal;
    TextView totalPurchaseVal;
    double totalSales;
    double totalCollection;
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    //Willam chart

    //private  BarChartView barChart;
    private HorizontalBarChart chart;

    //MPPIchart
    private PieChart pieChart;

    protected Typeface tfRegular;
    protected Typeface tfLight;
    ValueFormatter salesVsCollFormatter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        View v = inflater.inflate(R.layout.home_fragment, container, false);
        pieChart = (PieChart) v.findViewById(R.id.piChart);

        chart = v.findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);
        salesVsCollFormatter = new SalesVsCollectionValueFormatter(chart);
        init();
        charDataMap = new HashMap<>();

        vp_slider = (ViewPager) v.findViewById(R.id.vp_slider);
        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ll_dots = (LinearLayout) v.findViewById(R.id.ll_dots);
        totalSalesVal = (TextView) v.findViewById(R.id.totalSalesVal);
        totalPurchaseVal = (TextView) v.findViewById(R.id.totalPurchaseVal);
        if (progressDialog != null) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Fetching Data...");
            progressDialog.show();
        }

        getHomeData();
        generateData();
        //getProductSalesData();
        addBottomDots(0);
        final Handler handler = new Handler();
         ImageView salesMore = (ImageView) v.findViewById(R.id.salesMore);
        TextView totalSalesText =(TextView) v.findViewById(R.id.totalSalesText);
        TextView totalPurchanseText =(TextView) v.findViewById(R.id.totalPurchanseText);

        salesMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                showListTotalSalesPopupWindow(v);
            }
        });//closing the setOnClickListener method
        final ImageView puchaseMore = (ImageView) v.findViewById(R.id.puchaseMore);
        puchaseMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                showListTotalPurchasePopupWindow(v);
            }
        });//closing the setOnClickListener method
        final ImageView productWiseMore = (ImageView) v.findViewById(R.id.productWiseMore);
        productWiseMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                showListProductWisePopupWindow(v);
            }
        });//closing the setOnClickListener method
        final ImageView salesWiseMore = (ImageView) v.findViewById(R.id.salesWiseMore);
        salesWiseMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                showListSalesWisePopupWindow(v);
            }
        });//closing the setOnClickListener method

        totalPurchanseText.setLongClickable(true);
        totalPurchanseText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent myIntent = new Intent(getContext(), ServicesDetailActivity.class);
                Bundle bundle = new Bundle();
                GlobalClass.selectedTabPosition = "3";
                GlobalClass.backFrgment = "Services";
                GlobalClass.currentFrgment = "Sales";
                myIntent.putExtras(bundle);
                startActivity(myIntent);
                return false;
            }
        });
        totalSalesText.setLongClickable(true);
        totalSalesText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent myIntent = new Intent(getContext(), ServicesDetailActivity.class);
                Bundle bundle = new Bundle();
                GlobalClass.selectedTabPosition = "0";
                GlobalClass.backFrgment = "Services";
                GlobalClass.currentFrgment = "Sales";
                myIntent.putExtras(bundle);
                startActivity(myIntent);
                return false;
            }
        });
        //salesVsCollectionData();
        //getSalesTrend();
       // getCollectionTrend();

        return v;
    }

    private void showListTotalSalesPopupWindow(View anchor) {
        List<ListPopupItem> listPopupItems = new ArrayList<>();
        listPopupItems.add(new ListPopupItem("Thousand(TH)", R.mipmap.ic_launcher));
        listPopupItems.add(new ListPopupItem("Lakh(LH)", R.mipmap.ic_launcher));
        listPopupItems.add(new ListPopupItem("Crore(CR)", R.mipmap.ic_launcher));
        listPopupItems.add(new ListPopupItem("View", R.mipmap.ic_launcher));

        final ListPopupWindow listPopupWindow =
                createListPopupWindow(anchor, 300, listPopupItems);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();
                double tempVariable = Math.abs(totalSales);
                Log.d(TAG, "tempVariable >>>>>> " + tempVariable);
                if (position == 0) {

                    tempVariable = tempVariable / 1000;
                    String moneyString = formatter.format(tempVariable);
                    Log.d(TAG, "fomate " + moneyString);
                    totalSalesVal.setText(moneyString + " TH");
                    totalSalesVal.setTextSize(16);

                } else if (position == 1) {
                    tempVariable = tempVariable / 100000;

                    String moneyString = formatter.format(tempVariable);
                    Log.d(TAG, "fomate " + moneyString);
                    totalSalesVal.setText(moneyString + " LAKH");
                    totalSalesVal.setTextSize(18);

                } else if (position == 2){
                    tempVariable = tempVariable/10000000;
                    String moneyString = formatter.format(tempVariable);
                    Log.d(TAG, "fomate " + moneyString);
                    totalSalesVal.setText(moneyString + " CR");
                     totalSalesVal.setTextSize(26);
                }else if (position == 3){
                    Intent myIntent = new Intent(getContext(), ServicesDetailActivity.class);
                    Bundle bundle = new Bundle();
                    GlobalClass.selectedTabPosition = "0";
                    GlobalClass.backFrgment = "Services";
                    GlobalClass.currentFrgment = "Sales";
                    myIntent.putExtras(bundle);
                    startActivity(myIntent);
                    //return false;
                }

//                Toast.makeText(getActivity(), "clicked at " + position, Toast.LENGTH_SHORT)
//                        .show();
            }
        });
        listPopupWindow.show();
    }

    private void showListTotalPurchasePopupWindow(View anchor) {
        List<ListPopupItem> listPopupItems = new ArrayList<>();
        listPopupItems.add(new ListPopupItem("Thousand(TH)", R.mipmap.ic_launcher));
        listPopupItems.add(new ListPopupItem("Lakh(LH)", R.mipmap.ic_launcher));
        listPopupItems.add(new ListPopupItem("Crore(CR)", R.mipmap.ic_launcher));
        listPopupItems.add(new ListPopupItem("View", R.mipmap.ic_launcher));

        final ListPopupWindow listPopupWindow =
                createListPopupWindow(anchor, 300, listPopupItems);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();
                Log.d(TAG, "position :" + position);
                //String totalSalesValStr=  totalSalesVal.getText().toString();

                double tempVariable = Math.abs(totalCollection);
                if (position == 0) {
                    tempVariable = tempVariable/1000;
                    String moneyString = formatter.format(tempVariable);
                    Log.d(TAG, "fomate " + moneyString);
                    totalPurchaseVal.setText(moneyString + " TH");
                     totalPurchaseVal.setTextSize(16);

                } else if (position == 1) {
                    tempVariable = tempVariable/100000;
                    String moneyString = formatter.format(tempVariable);
                    Log.d(TAG, "fomate " + moneyString);
                    totalPurchaseVal.setText(moneyString + " LAKH");
                    totalPurchaseVal.setTextSize(18);

                } else if (position == 2){
                    tempVariable = tempVariable/10000000;
                    String moneyString = formatter.format(tempVariable);
                    Log.d(TAG, "fomate " + moneyString);
                    totalPurchaseVal.setText(moneyString + " CR");
                     totalPurchaseVal.setTextSize(26);
                }else if (position == 3){
                    Intent myIntent = new Intent(getContext(), ServicesDetailActivity.class);
                    Bundle bundle = new Bundle();
                    GlobalClass.selectedTabPosition = "3";
                    GlobalClass.backFrgment = "Services";
                    GlobalClass.currentFrgment = "Sales";
                    myIntent.putExtras(bundle);
                    startActivity(myIntent);
                }


//                Toast.makeText(getActivity(), "clicked at " + position+">> "+tempVariable, Toast.LENGTH_SHORT)
//                        .show();
            }
        });
        listPopupWindow.show();
    }

    private void showListProductWisePopupWindow(View anchor) {
        List<ListPopupItem> listPopupItems = new ArrayList<>();
        listPopupItems.add(new ListPopupItem("View", R.mipmap.ic_launcher));
        //listPopupItems.add(new ListPopupItem("Delete", R.mipmap.ic_launcher));
        //listPopupItems.add(new ListPopupItem("Crore", R.mipmap.ic_launcher));

        final ListPopupWindow listPopupWindow =
                createListPopupWindow(anchor, 200, listPopupItems);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();
                float amount = 100000;
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

                String moneyString = formatter.format(amount);
                //  totalSalesVal.setText(moneyString);
                Intent myIntent = new Intent(getContext(), ServicesDetailActivity.class);
                Bundle bundle = new Bundle();
                GlobalClass.selectedTabPosition = "4";
                GlobalClass.backFrgment = "Services";
                GlobalClass.currentFrgment = "Sales";
                myIntent.putExtras(bundle);
                startActivity(myIntent);
//                Toast.makeText(getActivity(), "clicked at " + position, Toast.LENGTH_SHORT)
//                        .show();
            }
        });
        listPopupWindow.show();
    }

    private void showListSalesWisePopupWindow(View anchor) {
        List<ListPopupItem> listPopupItems = new ArrayList<>();
        listPopupItems.add(new ListPopupItem("View", R.mipmap.ic_launcher));
        // listPopupItems.add(new ListPopupItem("Delete", R.mipmap.ic_launcher));

        final ListPopupWindow listPopupWindow =
                createListPopupWindow(anchor, 200, listPopupItems);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();
                float amount = 100000;
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

                String moneyString = formatter.format(amount);
                // totalSalesVal.setText(moneyString);
                Intent myIntent = new Intent(getContext(), ServicesDetailActivity.class);
                Bundle bundle = new Bundle();
                GlobalClass.selectedTabPosition = "1";
                GlobalClass.backFrgment = "Services";
                GlobalClass.currentFrgment = "Sales";
                myIntent.putExtras(bundle);
                startActivity(myIntent);
//                Toast.makeText(getActivity(), "clicked at " + position, Toast.LENGTH_SHORT)
//                        .show();
            }
        });
        listPopupWindow.show();
    }

    private ListPopupWindow createListPopupWindow(View anchor, int width,
                                                  List<ListPopupItem> items) {
        final ListPopupWindow popup = new ListPopupWindow(getContext());
        ListAdapter adapter = new ListPopupWindowAdapter(items);
        popup.setAnchorView(anchor);
        popup.setWidth(width);
        popup.setAdapter(adapter);
        return popup;
    }

    private void generateData() {
        int numValues = 5;

        List<SliceValue> values = new ArrayList<SliceValue>();
        for (int i = 0; i < numValues; ++i) {
            SliceValue sliceValue = new SliceValue((float) Math.random() * 30 + 15, ChartUtilsCustom.nextColor());
            values.add(sliceValue);
        }

        data = new PieChartData(values);
        data.setHasLabels(hasLabels);
        data.setValueLabelBackgroundEnabled(false);

        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);

        if (isExploded) {
            data.setSlicesSpacing(24);
        }

        if (hasCenterText1) {
            data.setCenterText1("Hello!");

            // Get roboto-italic font.
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
            data.setCenterText1Typeface(tf);

            //  Get font size from dimens.xml and convert it to sp(library uses sp values).
            data.setCenterText1FontSize(ChartUtilsCustom.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));
        }

        if (hasCenterText2) {
            data.setCenterText2("Charts (Roboto Italic)");

            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");

            data.setCenterText2Typeface(tf);
            data.setCenterText2FontSize(ChartUtilsCustom.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));
        }
        piChartSetup();
        // piChart.setPieChartData(data);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }

    private class ValueColumnBarTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(25);
            dots[i].setTextColor(getResources().getColor(R.color.chart1));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.chart5));
    }

    private void init() {


        slider_image_list = new ArrayList<>();

        slider_image_list.add("SalesTrendLine");
        slider_image_list.add("CollectionTrendLine");
        slider_image_list.add("SalesVsCollectionLine");


        slider_image_map = new HashMap<>();

        slider_image_map.put("SalesTrendLine", getResources().getString(R.string.sales_trend));
        slider_image_map.put("CollectionTrendLine", getResources().getString(R.string.collection_trend));
        slider_image_map.put("SalesVsCollectionLine", "Sales Vs Collection Trend");


    }

    private void piChartSetup() {
        //pieChart = new PieChart(getActivity());
        //ViewGroup.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // pieChart.setLayoutParams(lp);
        // pieChart.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //pieChart.setMinimumHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 0, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setCenterTextTypeface(tfLight);
        //pieChart.setCenterText(generateCenterSpannableText());
        pieChart.setDrawCenterText(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(100);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleRadius(55f);
        pieChart.setTransparentCircleRadius(60f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        // pieChart.setOnChartValueSelectedListener(activity.getApplicationContext());

        //seekBarX.setProgress(4);
        //seekBarY.setProgress(10);

        pieChart.animateY(2400, Easing.EaseInOutQuad);
        // pieChart.spin(2000, 0, 360);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setXEntrySpace(2f);
        l.setYEntrySpace(8f);
        l.setYOffset(2f);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setFormSize(5f);
        l.setTextSize(8f);
        l.setXEntrySpace(2f);
        //l.resetCustom();
        // entry label styling
        pieChart.setDrawEntryLabels(false);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTypeface(tfRegular);
        pieChart.setEntryLabelTextSize(10f);
        // setPieData(4, 10);
    }

    private void setPieData(ArrayList<PieEntry> entries) {
        // ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
//        for (int i = 0; i < count; i++) {
//            entries.add(new PieEntry((float) ((Math.random() * range) + range / 5),
//                    parties[i % parties.length],
//                    getResources().getDrawable(R.drawable.star)));
//        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();
//        if(GlobalClass.productColorMap!=null && GlobalClass.productColorMap.isEmpty()) {
//        for(PieEntry pe:entries){
//            Log.d(TAG,"Label >> "+pe.getLabel());
//
//                Log.d(TAG, "colour code >> " + GlobalClass.productColorMap.get(pe.getLabel()));
//                colors.add(Color.parseColor(GlobalClass.productColorMap.get(pe.getLabel())));
//
//        }
//        }else {

        colors.add(getResources().getColor(R.color.chart6));
        colors.add(getResources().getColor(R.color.chart5));
        colors.add(getResources().getColor(R.color.chart3));
        colors.add(getResources().getColor(R.color.chart4));
//        colors.add(getResources().getColor(R.color.chart5));

       // }

//        colors.add(getResources().getColor(R.color.chart1));
//        colors.add(getResources().getColor(R.color.chart2));
//        colors.add(getResources().getColor(R.color.chart3));
//        colors.add(getResources().getColor(R.color.chart4));
//        colors.add(getResources().getColor(R.color.chart5));
//        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tfLight);

        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();

    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Trend Wise\nSales ");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 10, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length() - 11, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, s.length() - 11, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 0, s.length() - 11, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 10, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 10, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        BarEntry entry = (BarEntry) e;
        Log.i("VAL SELECTED",
                "Value: " + Math.abs(entry.getYVals()[h.getStackIndex()]));
    }

    @Override
    public void onNothingSelected() {
    }

    private void getHomeData() {
        if (progressDialog != null) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
        }
        String url = Constants.BASE_LOCAL__URL + "getHomeData/"+GlobalClass.startDate+"/"+GlobalClass.endDate;
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
                            JSONObject contentList = response.getJSONObject("contentList");
                            JSONArray salesTrend=contentList.getJSONArray("salesTrend");
                            JSONArray collectionTrend=contentList.getJSONArray("collectionTrend");
                            JSONArray productSummary=contentList.getJSONArray("productSummary");
                            JSONArray salesNdCollection=contentList.getJSONArray("salesNdCollection");

                            Map<String, JSONArray> salesTrendMap = new HashMap<>();

                            if (salesTrend.length() == 4) {
                                salesTrendMap.put("day", salesTrend.getJSONArray(0));
                                salesTrendMap.put("month", salesTrend.getJSONArray(1));
                                salesTrendMap.put("quater", salesTrend.getJSONArray(2));
                                salesTrendMap.put("yearly", salesTrend.getJSONArray(3));
                            }
                            charDataMap.put("SalesTrendLine", salesTrendMap);
                            Map<String, JSONArray> colleactionTrendMap = new HashMap<>();

                            if (collectionTrend.length() == 4) {
                                colleactionTrendMap.put("day", collectionTrend.getJSONArray(0));
                                colleactionTrendMap.put("month", collectionTrend.getJSONArray(1));
                                colleactionTrendMap.put("quater", collectionTrend.getJSONArray(2));
                                colleactionTrendMap.put("yearly", collectionTrend.getJSONArray(3));
                            }
                            charDataMap.put("CollectionTrendLine", colleactionTrendMap);
                            // sliderPagerAdapter.notifyDataSetChanged();
                            //Summary


                            List<Integer> debitList = new ArrayList();
                            Map<Integer, Integer> productMap = new HashMap<>();
                            Map<Integer, String> productNameMap = new HashMap<>();


                            for (int i = 0; i < productSummary.length(); i++) {
                                JSONObject element = productSummary.getJSONObject(i);
                                int value = element.getInt("value");
                                String name = element.getString("name");
                                debitList.add(Math.abs(value));
                                Log.d(TAG, name + "><><><>" + value);
                                productNameMap.put(Math.abs(value), name);

                            }
                            Collections.sort(debitList);

                            Log.d(TAG, debitList.toString());
                            Log.d(TAG, productNameMap.toString());

                            int j = 0;
                            float limitNumber = 0;
                            final String[] mLabels = new String[debitList.size()];
                            final float[] debitVal = new float[debitList.size()];

                            ArrayList<PieEntry> entries = new ArrayList<>();
                            for (int debitValTemp : debitList) {
                                String cutomerName = productNameMap.get(debitValTemp);

                                BigDecimal number1 = new BigDecimal(debitValTemp);
                                float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);;


                                mLabels[j] = cutomerName;
                                debitVal[j] = debitFloat;

                                entries.add(new PieEntry(Utility.decimal2PalceAsInput(Math.abs(debitFloat)), cutomerName));
                                j++;
                                if (j == debitList.size()) {
                                    limitNumber = Math.round(debitFloat) % 2 == 0 ? +Math.round(debitFloat) + 2 : Math.round(debitFloat) + 1;
                                }
                            }
                            setPieData(entries);
                            PichartMarkerView pichartView = new PichartMarkerView(getContext(), R.layout.pi_chart_marker_view);
                            pieChart.setMarker(pichartView);

                            //End summary

                            //Start salesNdCollection
                            // GlobalClass.lastName = response.optString("userrole");
                            //Save to prefrence
                           // JSONObject contentList = response.getJSONObject("contentList");
                          //  JSONObject company = salesNdCollection.getJSONObject("company");
                          //  JSONArray salesAndCollectionList = contentList.getJSONArray("salesAndCollectionList");
                          //  totalSales = contentList.getDouble("totalSales") / 10000000;
//                          //  totalCollection = contentList.getDouble("totalCollection") / 10000000;
//                            String companyName = company.getString("name");
//                            //Utility.convertDouleValueToString(totlaSales);
//                            Log.d(TAG, "company Name : " + companyName);
//                            Log.d(TAG, "Total Sales : " + totalSales);
//                            Log.d(TAG, "TotlaSales_s : " + Utility.convertDouleValueToString(totalSales));
//                            Log.d(TAG, "Total Collection : " + Utility.convertDouleValueToString(totalCollection));

                            //  NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
//
//                            String totalSalesStr = formatter.format(Math.abs(Double.parseDouble(Utility.convertDouleValueToString(totalSales)))) + " Cr";
//                            String totalCollectionStr = formatter.format(Math.abs(Double.parseDouble(Utility.convertDouleValueToString(totalCollection)))) + " Cr";
//
//                            Log.d(TAG, "fomate " + totalSalesStr);
//                            totalSalesVal.setText(totalSalesStr);
//                            totalPurchaseVal.setText(totalCollectionStr);
                            //totalSalesVal.setTextSize(26);
                            //totalPurchaseVal.setTextSize(26);
                            final String[] mLabels1 = new String[salesNdCollection.length()];
                            final float[] creditVal = new float[salesNdCollection.length()];
                            final float[] debitVal1 = new float[salesNdCollection.length()];
                            List<Double> debitList1 = new ArrayList();
                            Map<Double, Double> CreditMap = new HashMap<>();
                            Map<Double, String> CutomerNameMap = new HashMap<>();


                            for (int i = 0; i < salesNdCollection.length(); i++) {
                                Log.d(TAG, "salesNdCollection " + salesNdCollection);
                                JSONObject element = salesNdCollection.getJSONObject(i);
                                double creditAmt = element.getDouble("credit")/GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                double debitAmt = element.getDouble("sales")/GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                String name = element.getString("name");

                                if(name.equalsIgnoreCase("total")){
                                    String totalSalesStr = formatter.format(Utility.decimal2PalceAsInput(Math.abs(Double.parseDouble(Utility.convertDouleValueToString(debitAmt))))) +" "+ GlobalClass.currecyPrefix;
                                    String totalCollectionStr = formatter.format(Math.abs(Double.parseDouble(Utility.convertDouleValueToString(creditAmt))))+" " + GlobalClass.currecyPrefix;
                                    totalSales=Math.abs(Double.parseDouble(Utility.convertDouleValueToString( element.getDouble("sales"))));
                                    totalCollection=Math.abs(Double.parseDouble(Utility.convertDouleValueToString(element.getDouble("credit"))));
                                    Log.d(TAG, "fomate " + totalSalesStr);
                                    totalSalesVal.setText(totalSalesStr);
                                    totalPurchaseVal.setText(totalCollectionStr);
                                    if (GlobalClass.currecyPrefix.equalsIgnoreCase("TH")) {
                                        totalSalesVal.setTextSize(16);
                                        totalPurchaseVal.setTextSize(16);

                                    } else if (GlobalClass.currecyPrefix.equalsIgnoreCase("LAKH")) {

                                        totalSalesVal.setTextSize(18);
                                        totalPurchaseVal.setTextSize(16);

                                    } else if (GlobalClass.currecyPrefix.equalsIgnoreCase("CR")){

                                        totalSalesVal.setTextSize(26);
                                        totalPurchaseVal.setTextSize(16);
                                    }
                                }else{
                                    Log.d(TAG, creditAmt + "><>< SalesVsCollection ><>" + debitAmt);
                                    debitList1.add(Math.abs(debitAmt));
                                    CreditMap.put(Math.abs(debitAmt), creditAmt);
                                    CutomerNameMap.put(Math.abs(debitAmt), name);
                                }
                            }
                            Collections.sort(debitList1);
                            int j1 = 0;
                            float limitNumber1 = 0;
                            ArrayList<BarEntry> values = new ArrayList<>();
                            for (Double debitValTemp : debitList1) {
                                Double creditValTemp = CreditMap.get(debitValTemp);
                                String cutomerName = CutomerNameMap.get(debitValTemp);

                                BigDecimal number = new BigDecimal(creditValTemp);
                                BigDecimal number1 = new BigDecimal(debitValTemp);
//                                float creditFloat = Utility.decimal2PalceAsInput(number.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix));
//                                float debitFloat = Utility.decimal2PalceAsInput(number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix));

                                float creditFloat = Utility.decimal2PalceAsInput(number.floatValue() );
                                float debitFloat = Utility.decimal2PalceAsInput(number1.floatValue());
                                Log.d(TAG, creditFloat + "><><Again><>" + debitFloat);

                                mLabels1[j1] = cutomerName;
                                creditVal[j1] = (creditFloat < 0 ? creditFloat : -creditFloat);
                                debitVal1[j1] = debitFloat;
                                values.add(new BarEntry(j1, new float[]{creditVal[j1], debitFloat}));
                                j1++;
                                if (j1 == debitList1.size()) {
                                    limitNumber1 = Math.round(debitFloat) % 2 == 0 ? +Math.round(debitFloat) + 2 : Math.round(debitFloat) + 1;
                                }
                            }

                            chart.setDrawGridBackground(false);
                            chart.getDescription().setEnabled(false);

                            // scaling can now only be done on x- and y-axis separately
                            chart.setPinchZoom(false);
                            chart.setDoubleTapToZoomEnabled(false);
                            chart.setDrawBarShadow(false);
                            chart.setDrawValueAboveBar(true);
                            chart.setHighlightFullBarEnabled(false);

                            chart.getAxisLeft().setEnabled(false);
                            chart.getAxisRight().setAxisMaximum(limitNumber1);
                            chart.getAxisRight().setAxisMinimum(-limitNumber1);
                            chart.getAxisRight().setDrawGridLines(false);
                            chart.getAxisRight().setDrawZeroLine(false);
                            // chart.getAxisRight().setLabelCount(7, false);
                            chart.getAxisRight().setValueFormatter(new CustomFormatter());
                            chart.getAxisRight().setTextSize(9f);
                            chart.getAxisRight().setDrawGridLines(false);
                            chart.getXAxis().setEnabled(false);

                            Legend l = chart.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                            l.setDrawInside(false);
                            l.setXEntrySpace(4f);
                            l.setYEntrySpace(8f);
                            l.setYOffset(2f);
                            l.setForm(Legend.LegendForm.SQUARE);
                            l.setFormSize(5f);
                            l.setTextSize(8f);
                            l.setFormToTextSpace(4f);

                            BarDataSet set = new BarDataSet(values, "");
                            set.setDrawIcons(false);
                            set.setValueFormatter(new CustomFormatter());
                            set.setValueTextSize(7f);
                            set.setAxisDependency(YAxis.AxisDependency.RIGHT);
                            set.setColors(getResources().getColor(R.color.chart2), getResources().getColor(R.color.chart1));
                            set.setStackLabels(new String[]{
                                    "Collection", "Sales"
                            });
                            set.setHighlightEnabled(true); // allow highlighting for DataSet

                            // set this to false to disable the drawing of highlight indicator (lines)
                            //set.setDrawHighlightIndicators(true);
                            set.setHighLightColor(getResources().getColor(R.color.chart3)); // color for highlight indicator

                            BarData data = new BarData(set);
                            //data.setBarWidth(2f);
                            // data.groupBars(2f,5f,2f);
                            // chart.setPinchZoom(false);
                            // chart.setEnabled(false);
                            chart.setScaleEnabled(false);
                            chart.setData(data);
                            chart.invalidate();
                            SalesVsCollectionMarkerView mv = new SalesVsCollectionMarkerView(getContext(), salesVsCollFormatter, mLabels1, debitVal1, creditVal);
                            mv.setChartView(chart); // For bounds control
                            chart.setMarker(mv); // Set the marker to the chart

                            //  chart.setTooltipText("test");
                            //////End//


                        }
                        sliderPagerAdapter = new SliderPagerAdapter(getContext(), slider_image_list, slider_image_map, charDataMap);
                        vp_slider.setAdapter(sliderPagerAdapter);
                        sliderPagerAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        };
        //  }
//        if (requestCode == REQUEST_FORGOT_PWD) {
//
//            listenerObj = new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    try {
//                        Log.d("Res", response.toString());
//                        progressDialog.dismiss();
//                        if (response.getString("code").equals(Integer.toString(206))) {
//                            Toast.makeText(getBaseContext(), "Password reset done. Please check your email.", Toast.LENGTH_LONG).show();
//                        } else if (response.getString("code").equals(Integer.toString(220))) {
//                            Toast.makeText(getBaseContext(), "User doesn't exist", Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(getBaseContext(), "Request failed. Please try again.", Toast.LENGTH_LONG).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//
//                    }
//                }
//            };
//        }
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

    private class CustomFormatter extends ValueFormatter {

        private final DecimalFormat mFormat;

        CustomFormatter() {
            mFormat = new DecimalFormat("####");
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(Math.abs(value)) + GlobalClass.currecyPrefix;
        }
    }

}