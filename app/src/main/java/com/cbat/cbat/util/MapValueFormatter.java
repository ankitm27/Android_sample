package com.cbat.cbat.util;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.util.Map;


public class MapValueFormatter extends IndexAxisValueFormatter {
    private String[] mValues;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.hh");

    public MapValueFormatter(String[] values) {
        this.mValues = values; }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        Log.d("formate",String.valueOf(value));
        return mValues[(int) value];
    }
}
