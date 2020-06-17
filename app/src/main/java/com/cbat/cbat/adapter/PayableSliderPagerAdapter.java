package com.cbat.cbat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.cbat.cbat.R;
import com.cbat.cbat.util.ChartUtilsCustom;
import com.cbat.cbat.util.DayAxisValueFormatter;
import com.cbat.cbat.util.MyValueFormatter;
import com.cbat.cbat.util.XYMarkerView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.listener.BubbleChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.BubbleChartData;
import lecho.lib.hellocharts.model.BubbleValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.BubbleChartView;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class PayableSliderPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    Context activity;
    ArrayList<String> image_arraylist;
    Map<String,String> slider_image_map;
    ColumnChartView columnChartView;
    TextView chartText;

    ColumnChartData columnChartData;
    PieChartView pieChartView;
    private PieChartData pieChartData;
    LineChartData lineChartDatadata;
    LineChartView lineChartView;
    private boolean hasAxes = true;
    private boolean hasAxesNames = false;
    private boolean hasColumnChartLabels = false;
    private boolean hasColumnChartLabelForSelected = false;
    private int dataType = 0;


    //Pie param
    private boolean hasLabels = true;
    private boolean hasLabelsOutside = false;
    private boolean hasCenterCircle = false;
    private boolean hasCenterText1 = true;
    private boolean hasCenterText2 = false;
    private boolean isExploded = false;
    private boolean hasLabelForSelected = false;

    //bubble chart
    private BubbleChartView bubbleChartView;
    private BubbleChartData bubbleChartData;
    //private boolean hasAxes = true;
    //private boolean hasAxesNames = true;
    private ValueShape shape = ValueShape.CIRCLE;
   // private boolean hasLabels = false;
   // private boolean hasLabelForSelected = false;
   private static final int BUBBLES_NUM = 8;

   //MPChart bar
   private BarChart chart;
    protected Typeface tfRegular;
    protected Typeface tfLight;

    //MPPIchart
    private PieChart pieChart;
    protected final String[] parties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    public PayableSliderPagerAdapter(Context activity, ArrayList<String> image_arraylist, Map<String,String> slider_image_map) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
        this.slider_image_map= slider_image_map;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.home_slider, container, false);
       // ImageView im_slider = (ImageView) view.findViewById(R.id.sliderChart);
        LinearLayout chats = (LinearLayout) view.findViewById(R.id.chartLayout);
        tfRegular = Typeface.createFromAsset(activity.getAssets(), "arial.ttf");
        tfLight = Typeface.createFromAsset(activity.getAssets(), "arial.ttf");

       // columnChartView = (ColumnChartView) view.findViewById(R.id.sliderChart);
        chartText=(TextView)view.findViewById(R.id.chartText);
        chartText.setText(image_arraylist.get(position));
        if(slider_image_map.get(image_arraylist.get(position)).equalsIgnoreCase("ColumnChart")) {
           columnChartView = new ColumnChartView(activity);
           columnChartView.setOnValueTouchListener(new ValueColumnBarTouchListener());

           generateColumnsDefaultData();
           prepareColumnDataAnimation();
          // chats.addView(columnChartView);
       }else if(slider_image_map.get(image_arraylist.get(position)).equalsIgnoreCase("PiChart")) {

           pieChartView =  new PieChartView(activity);
           pieChartView.setOnValueTouchListener(new ValueTouchListener());
           pieChartView.setChartRotationEnabled(false);
           generatePiChartData();
          // chats.addView(pieChartView);

       }
       else if(slider_image_map.get(image_arraylist.get(position)).equalsIgnoreCase("TempoChart")) {

           lineChartView =  new LineChartView(activity);
           generateTempoData();

            //chats.addView(lineChartView);

       }
        else if(slider_image_map.get(image_arraylist.get(position)).equalsIgnoreCase("Bubblechart")) {
;
            bubbleChartView = new BubbleChartView(activity);
            bubbleChartView.setOnValueTouchListener(new ValueBubbleTouchListener());
            generateBubbleData();
            //chats.addView(bubbleChartView);

        }
        else if(slider_image_map.get(image_arraylist.get(position)).equalsIgnoreCase("MpBar")) {
            chart=new BarChart(activity);
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
            // chart.setDrawYLabels(false);
            chart.getAxisLeft().setDrawGridLines(false);
            chart.getXAxis().setDrawGridLines(false);
            ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart,2019);

            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTypeface(tfLight);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f); // only intervals of 1 day
            xAxis.setLabelCount(7);
            xAxis.setValueFormatter(xAxisFormatter);

            ValueFormatter custom = new MyValueFormatter("$");

            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.setTypeface(tfLight);
            leftAxis.setLabelCount(8, false);
            leftAxis.setValueFormatter(custom);
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setSpaceTop(15f);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            YAxis rightAxis = chart.getAxisRight();
            rightAxis.setEnabled(false);
            rightAxis.setDrawGridLines(false);
            rightAxis.setTypeface(tfLight);
            rightAxis.setLabelCount(8, false);
            rightAxis.setValueFormatter(custom);
            rightAxis.setSpaceTop(15f);
            rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            Legend l = chart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setForm(LegendForm.SQUARE);
            l.setFormSize(9f);
            l.setTextSize(11f);
            l.setXEntrySpace(4f);

            XYMarkerView mv = new XYMarkerView(activity, xAxisFormatter);
            mv.setChartView(chart); // For bounds control
            chart.setMarker(mv); // Set the marker to the chart
            setData(20, 100);
            chart.animateX(2000);
            chats.addView(chart);

        }
        else if(slider_image_map.get(image_arraylist.get(position)).equalsIgnoreCase("MpPieChart")) {
            pieChart = new PieChart(activity);
            ViewGroup.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            pieChart.setLayoutParams(lp);
            pieChart.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            pieChart.setMinimumHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            pieChart.setUsePercentValues(true);
            Description description = new Description();
            description.setText("");
            pieChart.setDescription(description);

            pieChart.getDescription().setEnabled(true);
            pieChart.getDescription().setPosition(1f,0f);            //pieChart.setExtraOffsets(5, 10, 5, 5);

            pieChart.setDragDecelerationFrictionCoef(0.95f);
            pieChart.setSaveEnabled(false);
            pieChart.setCenterTextTypeface(tfLight);
            pieChart.setCenterText(generateCenterSpannableText());

            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColor(Color.WHITE);

            pieChart.setTransparentCircleColor(Color.WHITE);
            pieChart.setTransparentCircleAlpha(110);

            pieChart.setHoleRadius(58f);
            pieChart.setTransparentCircleRadius(61f);

            pieChart.setDrawCenterText(true);

            pieChart.setRotationAngle(0);
            // enable rotation of the chart by touch
            pieChart.setRotationEnabled(true);
            pieChart.setHighlightPerTapEnabled(true);

            // chart.setUnit(" €");
            // chart.setDrawUnitsInChart(true);

            // add a selection listener
           // pieChart.setOnChartValueSelectedListener(activity.getApplicationContext());

            //seekBarX.setProgress(4);
            //seekBarY.setProgress(10);

            pieChart.animateY(2400, Easing.EaseInOutQuad);
            //pieChart.spin(2000, 0, 0);
            pieChart.setMotionEventSplittingEnabled(true);
            Legend l = pieChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);

            // entry label styling
            pieChart.setRotationEnabled(false);
            pieChart.setDrawEntryLabels(false);
            pieChart.setEntryLabelColor(Color.WHITE);
            pieChart.setEntryLabelTypeface(tfRegular);
            pieChart.setEntryLabelTextSize(12f);
            setPieData(4,10);
            chats.addView(pieChart);

        }
        container.addView(view);
        return view;
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Chart\ndeveloped ");
//        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }
    private void setPieData(int count, float range) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry((float) ((Math.random() * range) + range / 5),
                    parties[i % parties.length],
                    activity.getResources().getDrawable(R.drawable.star)));
        }


        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(activity.getResources().getColor(R.color.chart1));
        colors.add(activity.getResources().getColor(R.color.chart2));
        colors.add(activity.getResources().getColor(R.color.chart3));
        colors.add(activity.getResources().getColor(R.color.chart4));
        colors.add(activity.getResources().getColor(R.color.chart5));
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);

        // add a lot of colors

//        ArrayList<Integer> colors = new ArrayList<>();
//
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
//
//        colors.add(ColorTemplate.getHoloBlue());
//
//        dataSet.setColors(colors);
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
    private void setData(int count, float range) {

        float start = 1f;

        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = (int) start; i < start + count; i++) {
            float val = (float) (Math.random() * (range + 1));

            if (Math.random() * 100 < 25) {
                values.add(new BarEntry(i, val, activity.getResources().getDrawable(R.drawable.star)));
            } else {
                values.add(new BarEntry(i, val));
            }
        }

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.setHighlightEnabled(false);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "The year 2017");

            set1.setDrawIcons(false);

//            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            /*int startColor = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor = ContextCompat.getColor(this, android.R.color.holo_blue_bright);
            set1.setGradientColor(startColor, endColor);*/

            int startColor1 = ContextCompat.getColor(activity, android.R.color.holo_orange_light);
            int startColor2 = ContextCompat.getColor(activity, android.R.color.holo_blue_light);
            int startColor3 = ContextCompat.getColor(activity, android.R.color.holo_orange_light);
            int startColor4 = ContextCompat.getColor(activity, android.R.color.holo_green_light);
            int startColor5 = ContextCompat.getColor(activity, android.R.color.holo_red_light);
            int endColor1 = ContextCompat.getColor(activity, android.R.color.holo_blue_dark);
            int endColor2 = ContextCompat.getColor(activity, android.R.color.holo_purple);
            int endColor3 = ContextCompat.getColor(activity, android.R.color.holo_green_dark);
            int endColor4 = ContextCompat.getColor(activity, android.R.color.holo_red_dark);
            int endColor5 = ContextCompat.getColor(activity, android.R.color.holo_orange_dark);

            List<GradientColor> gradientColors = new ArrayList<>();
            gradientColors.add(new GradientColor(startColor1, endColor1));
            gradientColors.add(new GradientColor(startColor2, endColor2));
            gradientColors.add(new GradientColor(startColor3, endColor3));
            gradientColors.add(new GradientColor(startColor4, endColor4));
            gradientColors.add(new GradientColor(startColor5, endColor5));

            set1.setGradientColors(gradientColors);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(tfLight);
            data.setBarWidth(0.9f);

            chart.setData(data);
        }
    }
    private void generateTempoData() {
        // I got speed in range (0-50) and height in meters in range(200 - 300). I want this chart to display both
        // information. Differences between speed and height values are large and chart doesn't look good so I need
        // to modify height values to be in range of speed values.

        // The same for displaying Tempo/Height chart.

        float minHeight = 200;
        float maxHeight = 300;
        float tempoRange = 15; // from 0min/km to 15min/km

        float scale = tempoRange / maxHeight;
        float sub = (minHeight * scale) / 2;

        int numValues = 52;

        Line line;
        List<PointValue> values;
        List<Line> lines = new ArrayList<Line>();

        // Height line, add it as first line to be drawn in the background.
        values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            // Some random height values, add +200 to make line a little more natural
            float rawHeight = (float) (Math.random() * 100 + 200);
            float normalizedHeight = rawHeight * scale - sub;
            values.add(new PointValue(i, normalizedHeight));
        }

        line = new Line(values);
        line.setColor(Color.GRAY);
        line.setHasPoints(false);
        line.setFilled(true);
        line.setStrokeWidth(1);
        lines.add(line);

        // Tempo line is a little tricky because worse tempo means bigger value for example 11min per km is worse
        // than 2min per km but the second should be higher on the chart. So you need to know max tempo and
        // tempoRange and set
        // chart values to minTempo - realTempo.
        values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            // Some random raw tempo values.
            float realTempo = (float) Math.random() * 6 + 2;
            float revertedTempo = tempoRange - realTempo;
            values.add(new PointValue(i, revertedTempo));
        }

        line = new Line(values);
        line.setColor(ChartUtils.COLOR_RED);
        line.setHasPoints(false);
        line.setStrokeWidth(3);
        lines.add(line);

        // Data and axes
        lineChartDatadata = new LineChartData(lines);

        // Distance axis(bottom X) with formatter that will ad [km] to values, remember to modify max label charts
        // value.
//        Axis distanceAxis = new Axis();
//        distanceAxis.setName("Distance");
//        distanceAxis.setTextColor(ChartUtils.COLOR_ORANGE);
//        distanceAxis.setMaxLabelChars(4);
//        distanceAxis.setFormatter(new SimpleAxisValueFormatter().setAppendedText("km".toCharArray()));
//        distanceAxis.setHasLines(true);
//        distanceAxis.setHasTiltedLabels(true);
//        lineChartDatadata.setAxisXBottom(distanceAxis);

        // Tempo uses minutes so I can't use auto-generated axis because auto-generation works only for decimal
        // system. So generate custom axis values for example every 15 seconds and set custom labels in format
        // minutes:seconds(00:00), you could do it in formatter but here will be faster.
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
//        for (float i = 0; i < tempoRange; i += 0.25f) {
//            // I'am translating float to minutes because I don't have data in minutes, if You store some time data
//            // you may skip translation.
//            axisValues.add(new AxisValue(i).setLabel(formatMinutes(tempoRange - i)));
//        }

        Axis tempoAxis = new Axis(axisValues).setName(" Data[min/km]").setHasLines(true).setMaxLabelChars(4)
                .setTextColor(ChartUtils.COLOR_RED);
        lineChartDatadata.setAxisYLeft(tempoAxis);

        // *** Same as in Speed/Height chart.
        // Height axis, this axis need custom formatter that will translate values back to real height values.
        lineChartDatadata.setAxisYRight(new Axis().setName("Height [m]").setMaxLabelChars(3)
                .setFormatter(new HeightValueFormatter(scale, sub, 0)));

        // Set data
        lineChartView.setLineChartData(lineChartDatadata);

        // Important: adjust viewport, you could skip this step but in this case it will looks better with custom
        // viewport. Set
        // viewport with Y range 0-12;
        Viewport v = lineChartView.getMaximumViewport();
        v.set(v.left, tempoRange, v.right, 0);
        lineChartView.setMaximumViewport(v);
        lineChartView.setCurrentViewport(v);
        lineChartView.startDataAnimation();


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
    private void generatePiChartData() {
        int numValues = 5;

        List<SliceValue> values = new ArrayList<SliceValue>();
        for (int i = 0; i < numValues; ++i) {
            SliceValue sliceValue = new SliceValue((float) Math.random() * 30 + 15, ChartUtilsCustom.nextColor());
            values.add(sliceValue);
        }

        pieChartData = new PieChartData(values);
        pieChartData.setHasLabels(hasLabels);
        pieChartData.setValueLabelBackgroundEnabled(false);

        pieChartData.setHasLabelsOnlyForSelected(hasLabelForSelected);
        pieChartData.setHasLabelsOutside(hasLabelsOutside);
        pieChartData.setHasCenterCircle(hasCenterCircle);

        if (isExploded) {
            pieChartData.setSlicesSpacing(24);
        }

        if (hasCenterText1) {
            pieChartData.setCenterText1("Hello!");

            // Get roboto-italic font.
            Typeface tf = Typeface.createFromAsset(activity.getAssets(), "Roboto-Italic.ttf");
            pieChartData.setCenterText1Typeface(tf);

            //  Get font size from dimens.xml and convert it to sp(library uses sp values).
            pieChartData.setCenterText1FontSize(ChartUtilsCustom.px2sp(activity.getResources().getDisplayMetrics().scaledDensity,
                    (int) activity.getResources().getDimension(R.dimen.pie_chart_text1_size)));
        }

        if (hasCenterText2) {
            pieChartData.setCenterText2("Charts (Roboto Italic)");

            Typeface tf = Typeface.createFromAsset(activity.getAssets(), "Roboto-Italic.ttf");

            pieChartData.setCenterText2Typeface(tf);
            pieChartData.setCenterText2FontSize(ChartUtilsCustom.px2sp(activity.getResources().getDisplayMetrics().scaledDensity,
                    (int) activity.getResources().getDimension(R.dimen.pie_chart_text2_size)));
        }

        pieChartView.setPieChartData(pieChartData);
    }
    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            Toast.makeText(activity, "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }
    @Override
    public int getCount() {
        return image_arraylist.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    private void generateColumnsDefaultData() {
        int numSubcolumns = 1;
        int numColumns = 8;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtilsCustom.nextColor()));
            }

            Column column = new Column(values);
            column.setHasLabels(hasColumnChartLabels);
            column.setHasLabelsOnlyForSelected(hasColumnChartLabelForSelected);
            columns.add(column);
        }

        columnChartData = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            columnChartData.setAxisXBottom(axisX);
            columnChartData.setAxisYLeft(axisY);
        } else {
            columnChartData.setAxisXBottom(null);
            columnChartData.setAxisYLeft(null);
        }

        columnChartView.setColumnChartData(columnChartData);

    }

    /**
     * To animate values you have to change targets values and then call {@link -Chart#startDataAnimation()}
     * method(don't confuse with View.animate()).
     */
    private void prepareColumnDataAnimation() {
        for (Column column : columnChartData.getColumns()) {
            for (SubcolumnValue value : column.getValues()) {
                value.setTarget((float) Math.random() * 100);
            }
        }
    }

    private class ValueColumnBarTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            Toast.makeText( activity, "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }
    private class ValueBubbleTouchListener implements BubbleChartOnValueSelectListener {

        @Override
        public void onValueSelected(int bubbleIndex, BubbleValue value) {
            Toast.makeText(activity, "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }
    }
    private void generateBubbleData() {

        List<BubbleValue> values = new ArrayList<BubbleValue>();
        for (int i = 0; i < BUBBLES_NUM; ++i) {
            BubbleValue value = new BubbleValue(i, (float) Math.random() * 100, (float) Math.random() * 1000);
            value.setColor(ChartUtils.nextColor());
            value.setShape(shape);
            values.add(value);
        }

        bubbleChartData = new BubbleChartData(values);
        bubbleChartData.setHasLabels(hasLabels);
        bubbleChartData.setHasLabelsOnlyForSelected(hasLabelForSelected);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            bubbleChartData.setAxisXBottom(axisX);
            bubbleChartData.setAxisYLeft(axisY);
        } else {
            bubbleChartData.setAxisXBottom(null);
            bubbleChartData.setAxisYLeft(null);
        }

        bubbleChartView.setBubbleChartData(bubbleChartData);

    }
}