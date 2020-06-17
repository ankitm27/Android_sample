package com.cbat.cbat.util;

import android.util.Log;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class SalesVsCollectionValueFormatter extends ValueFormatter
{



    private final HorizontalBarChart chart;

    public SalesVsCollectionValueFormatter(HorizontalBarChart chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value) {

        int days = (int) value;


       // chart.getVisibleXRange();
        Log.d("Chart > ",String.valueOf(chart.getVisibleXRange()));
       return String.valueOf(value);
    }


}
