package com.cbat.cbat.ui.services;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;

import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cbat.cbat.R;
import com.cbat.cbat.adapter.ListPopupWindowAdapter;
import com.cbat.cbat.adapter.PayableSliderPagerAdapter;
import com.cbat.cbat.adapter.SliderPagerAdapter;
import com.cbat.cbat.model.ListPopupItem;
import com.cbat.cbat.ui.BaseFragment;
import com.cbat.cbat.util.ChartUtilsCustom;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class PayableFragment extends Fragment implements OnChartValueSelectedListener {


    public static PayableFragment newInstance() {
        return new PayableFragment();
    }

    private PieChart piChart;
    private PieChartData data;

    private LineChartView lineChartView;
    private LineChartData lineChartDatadata;

    private boolean hasLabels = true;
    private boolean hasLabelsOutside = false;
    private boolean hasCenterCircle = false;
    private boolean hasCenterText1 = true;
    private boolean hasCenterText2 = false;
    private boolean isExploded = false;
    private boolean hasLabelForSelected = false;

    private static final int DEFAULT_DATA = 0;
    private static final int SUBCOLUMNS_DATA = 1;
    private static final int STACKED_DATA = 2;
    private static final int NEGATIVE_SUBCOLUMNS_DATA = 3;
    private static final int NEGATIVE_STACKED_DATA = 4;

    private ColumnChartView columnChartView;
    private ColumnChartData columnChartData;
    private boolean hasAxes = true;
    private boolean hasAxesNames = false;
    private boolean hasColumnChartLabels = false;
    private boolean hasColumnChartLabelForSelected = false;
    private int dataType = DEFAULT_DATA;
    Map<String,String> slider_image_map;

    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    PayableSliderPagerAdapter sliderPagerAdapter;
    ArrayList<String> slider_image_list;
    private TextView[] dots;
    int page_position = 0;
    TextView totalSalesVal;

    //MPPIchart
    private PieChart pieChart;
    protected final String[] parties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };
    private BarChart chart;
    protected Typeface tfRegular;
    protected Typeface tfLight;

    //MPLine
    private LineChart lineChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.services_payable_fragment, container, false);
        pieChart = (PieChart) v.findViewById(R.id.piChart);
        lineChart=(LineChart) v.findViewById(R.id.lineChart);
        //piChart.setOnValueTouchListener(new ValueTouchListener());
        //piChart.setChartRotationEnabled(false);
        lineChart = v.findViewById(R.id.lineChart);
        lineChart.setOnChartValueSelectedListener(this);
        lineChart.setDrawGridBackground(false);

        generateData();
        lineChartSetup();

//        columnChartView = (ColumnChartView) v.findViewById(R.id.chart);
//        columnChartView.setOnValueTouchListener(new ValueColumnBarTouchListener());
//        columnChartView.setZoomEnabled(false);
       // generateDefaultData();
        //prepareDataAnimation();
        //prepareColumnDataAnimation();
        vp_slider = (ViewPager) v.findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) v.findViewById(R.id.ll_dots);
        totalSalesVal=(TextView)v.findViewById(R.id.totalSalesVal);
        init();
        addBottomDots(0);
        final Handler handler = new Handler();
    final ImageView salesMore=(ImageView)v.findViewById(R.id.salesMore);
        salesMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                showListTotalSalesPopupWindow(v);
            }
        });//closing the setOnClickListener method
        final ImageView puchaseMore=(ImageView)v.findViewById(R.id.puchaseMore);
        puchaseMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                showListTotalPurchasePopupWindow(v);
            }
        });//closing the setOnClickListener method
        final ImageView productWiseMore=(ImageView)v.findViewById(R.id.productWiseMore);
        productWiseMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                showListProductWisePopupWindow(v);
            }
        });//closing the setOnClickListener method
        final ImageView salesWiseMore=(ImageView)v.findViewById(R.id.salesWiseMore);
        salesWiseMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                showListSalesWisePopupWindow(v);
            }
        });//closing the setOnClickListener method
        return v;
    }


    private void showListTotalSalesPopupWindow(View anchor) {
        List<ListPopupItem> listPopupItems = new ArrayList<>();
        listPopupItems.add(new ListPopupItem("Yearly", R.mipmap.ic_launcher));
        listPopupItems.add(new ListPopupItem("Quarterly", R.mipmap.ic_launcher));
        listPopupItems.add(new ListPopupItem("Monthly", R.mipmap.ic_launcher));

        final ListPopupWindow listPopupWindow =
                createListPopupWindow(anchor, 200, listPopupItems);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();
                float amount = 100000;
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

                String moneyString = formatter.format(amount);
                //totalSalesVal.setText(moneyString);
                Toast.makeText(getActivity(), "clicked at " + position, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        listPopupWindow.show();
    }
    private void showListTotalPurchasePopupWindow(View anchor) {
        List<ListPopupItem> listPopupItems = new ArrayList<>();
        listPopupItems.add(new ListPopupItem("Yearly", R.mipmap.ic_launcher));
        listPopupItems.add(new ListPopupItem("Quarterly", R.mipmap.ic_launcher));
        listPopupItems.add(new ListPopupItem("Monthly", R.mipmap.ic_launcher));

        final ListPopupWindow listPopupWindow =
                createListPopupWindow(anchor, 200, listPopupItems);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();
                float amount = 100000;
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

                String moneyString = formatter.format(amount);
             //   totalSalesVal.setText(moneyString);
                Toast.makeText(getActivity(), "clicked at " + position, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        listPopupWindow.show();
    }
    private void showListProductWisePopupWindow(View anchor) {
        List<ListPopupItem> listPopupItems = new ArrayList<>();
        listPopupItems.add(new ListPopupItem("State", R.mipmap.ic_launcher));
        listPopupItems.add(new ListPopupItem("Cutomer", R.mipmap.ic_launcher));
        listPopupItems.add(new ListPopupItem("Product", R.mipmap.ic_launcher));
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
                Toast.makeText(getActivity(), "clicked at " + position, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        listPopupWindow.show();
    }
    private void showListSalesWisePopupWindow(View anchor) {
        List<ListPopupItem> listPopupItems = new ArrayList<>();
        listPopupItems.add(new ListPopupItem("State", R.mipmap.ic_launcher));
        listPopupItems.add(new ListPopupItem("Cutomer", R.mipmap.ic_launcher));
        listPopupItems.add(new ListPopupItem("Product", R.mipmap.ic_launcher));


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
                Toast.makeText(getActivity(), "clicked at " + position, Toast.LENGTH_SHORT)
                        .show();
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

//    @Override
//    protected int getLayoutId() {
//        return R.layout.home_fragment;
//    }

    /**
     * To animate values you have to change targets values and then call {@link -Chart#startDataAnimation()}
     * method(don't confuse with View.animate()).
     */
    private void prepareDataAnimation() {
        for (SliceValue value : data.getValues()) {
            value.setTarget((float) Math.random() * 30 + 15);
        }
    }

    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }



    private String formatMinutes(float value) {
        StringBuilder sb = new StringBuilder();

        // translate value to seconds, for example
        int valueInSeconds = (int) (value * 60);
        int minutes = (int) Math.floor(valueInSeconds / 60);
        int seconds = (int) valueInSeconds % 60;

        sb.append(String.valueOf(minutes)).append(':');
        if (seconds < 10) {
            sb.append('0');
        }
        sb.append(String.valueOf(seconds));
        return sb.toString();
    }

    /**
     * Recalculated height values to display on axis. For this example I use auto-generated height axis so I
     * override only formatAutoValue method.
     */
    private static class HeightValueFormatter extends SimpleAxisValueFormatter {

        private float scale;
        private float sub;
        private int decimalDigits;

        public HeightValueFormatter(float scale, float sub, int decimalDigits) {
            this.scale = scale;
            this.sub = sub;
            this.decimalDigits = decimalDigits;
        }

        @Override
        public int formatValueForAutoGeneratedAxis(char[] formattedValue, float value, int autoDecimalDigits) {
            float scaledValue = (value + sub) / scale;
            return super.formatValueForAutoGeneratedAxis(formattedValue, scaledValue, this.decimalDigits);
        }
    }

//    private void generateDefaultData() {
//        int numSubcolumns = 1;
//        int numColumns = 8;
//        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
//        List<Column> columns = new ArrayList<Column>();
//        List<SubcolumnValue> values;
//        for (int i = 0; i < numColumns; ++i) {
//
//            values = new ArrayList<SubcolumnValue>();
//            for (int j = 0; j < numSubcolumns; ++j) {
//                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtilsCustom.nextColor()));
//            }
//
//            Column column = new Column(values);
//            column.setHasLabels(hasColumnChartLabels);
//            column.setHasLabelsOnlyForSelected(hasColumnChartLabelForSelected);
//            columns.add(column);
//        }
//
//        columnChartData = new ColumnChartData(columns);
//
//        if (hasAxes) {
//            Axis axisX = new Axis();
//            Axis axisY = new Axis().setHasLines(true);
//            if (hasAxesNames) {
//                axisX.setName("Axis X");
//                axisY.setName("Axis Y");
//            }
//            columnChartData.setAxisXBottom(axisX);
//            columnChartData.setAxisYLeft(axisY);
//        } else {
//            columnChartData.setAxisXBottom(null);
//            columnChartData.setAxisYLeft(null);
//        }
//
//        columnChartView.setColumnChartData(columnChartData);
//
//    }

    /**
     * To animate values you have to change targets values and then call {@link -Chart#startDataAnimation()}
     * method(don't confuse with View.animate()).
     */
//    private void prepareColumnDataAnimation() {
//        for (Column column : columnChartData.getColumns()) {
//            for (SubcolumnValue value : column.getValues()) {
//                value.setTarget((float) Math.random() * 100);
//            }
//        }
//    }

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

//Add few items to slider_image_list ,this should contain url of images which should be displayed in slider
// here i am adding few sample image links, you can add your own

       // slider_image_list.add("Sales Trend");
       // slider_image_list.add("Recivable");
       // slider_image_list.add("Product wise Sales");
       // slider_image_list.add("Payment");
        slider_image_list.add("Overdue Report");
        slider_image_list.add("Payables Aging Report");


        slider_image_map = new HashMap<>();

//Add few items to slider_image_list ,this should contain url of images which should be displayed in slider
// here i am adding few sample image links, you can add your own

       // slider_image_map.put("Sales Trend","PiChart");
        //slider_image_map.put("Recivable","ColumnChart");
        //slider_image_map.put("Product wise Sales","TempoChart");
        //slider_image_map.put("Payment","Bubblechart");
        slider_image_map.put("Overdue Report","MpBar");
        slider_image_map.put("Payables Aging Report","MpPieChart");


        sliderPagerAdapter = new PayableSliderPagerAdapter(getContext(), slider_image_list,slider_image_map);
        vp_slider.setAdapter(sliderPagerAdapter);

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
    }

    private void piChartSetup(){
        //pieChart = new PieChart(getActivity());
        //ViewGroup.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
       // pieChart.setLayoutParams(lp);
       // pieChart.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //pieChart.setMinimumHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
       // pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setCenterTextTypeface(tfLight);
        //pieChart.setCenterText(generateCenterSpannableText());
        pieChart.setDrawCenterText(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

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
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        pieChart.setDrawEntryLabels(false);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTypeface(tfRegular);
        pieChart.setEntryLabelTextSize(12f);
        setPieData(4,10);
    }
    private void setPieData(int count, float range) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry((float) ((Math.random() * range) + range / 5),
                    parties[i % parties.length],
                    getResources().getDrawable(R.drawable.star)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Sales Results");

        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);

        colors.add(getResources().getColor(R.color.chart1));
        colors.add(getResources().getColor(R.color.chart2));
        colors.add(getResources().getColor(R.color.chart3));
        colors.add(getResources().getColor(R.color.chart4));
        colors.add(getResources().getColor(R.color.chart5));
        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(11f);
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

    private void lineChartSetup(){
//        lineChart = v.findViewById(R.id.lineChart);
//        lineChart.setOnChartValueSelectedListener(this);
//        lineChart.setDrawGridBackground(false);

        // no description text
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        lineChart.getDescription().setEnabled(true);
        lineChart.getDescription().setPosition(1f,0f);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setTouchEnabled(false);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);
        lineChart.setBorderColor(getResources().getColor(R.color.textColorSecondary));
        // set an alternative background color
        // chart.setBackgroundColor(Color.GRAY);

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
        xAxis.setEnabled(false);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMaxValue(250f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawGridLines(false);
       // l.setEnabled(false);
        // don't forget to refresh the drawing
        lineChart.invalidate();
        setData(20,200);
        lineChart.animateXY(2000, 2000);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {}
    private void setData(int count, float range) {

        ArrayList<Entry> entries = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float xVal = (float) (Math.random() * range);
            float yVal = (float) (Math.random() * range);
            entries.add(new Entry(xVal, yVal));
        }

        // sort by x-value
        Collections.sort(entries, new EntryXComparator());

        // create a dat aset and give it a type
        LineDataSet set1 = new LineDataSet(entries, "Sales");
        set1.setDrawCircles(false);
        set1.setDrawCircleHole(false);
        set1.setLineWidth(1.5f);
        set1.setDrawHorizontalHighlightIndicator(true);
        set1.setDrawHighlightIndicators(true);
        //set1.setColor(Color.rgb(255, 241, 46));
        set1.setColor(getResources().getColor(R.color.chart5));
        set1.setFillAlpha(255);
        //set1.setFillColor(Color.GREEN);
        //set1.setHighLightColor(getResources().getColor(R.color.chart5));
        //set1.setCircleRadius(4f);

        // create a data object with the data sets
        LineData data = new LineData(set1);

        // set data
        lineChart.setData(data);
        List<ILineDataSet> sets = lineChart.getData().getDataSets();
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
    }
}