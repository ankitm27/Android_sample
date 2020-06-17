package com.cbat.cbat.util;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.cbat.cbat.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;


public class SalesVsCollectionMarkerView extends MarkerView {

    private final TextView tvContent;
    private final TextView tvSales;
    private final TextView tvCollection;

    private final ValueFormatter xAxisValueFormatter;

    private final DecimalFormat format;
    String[] labels;
    float[] sales;
    float[] collections;
    public SalesVsCollectionMarkerView(Context context, ValueFormatter xAxisValueFormatter,String[] labels,float[] sales,float[] collections) {
        super(context, R.layout.salesvscollection_marker_view);
        this.labels=labels;
        this.collections=collections;
        this.sales=sales;
        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = findViewById(R.id.tvContent);
        tvCollection = findViewById(R.id.tvCollection);
        tvSales= findViewById(R.id.tvSales);
        format = new DecimalFormat("###.0");
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {


       String companyName= labels[(int)e.getX()];
       float sale=sales[(int)e.getX()];
        float collect=collections[(int)e.getX()];
       // Toast.makeText(getContext(), collect+" sale "+sale+" <> "+companyName, Toast.LENGTH_LONG).show();
        tvContent.setText(companyName);
        tvSales.setText(String.valueOf(sale)+" "+GlobalClass.currecyPrefix);
        tvCollection.setText(String.valueOf(collect)+" "+GlobalClass.currecyPrefix);



        //Toast.makeText(getContext(), e.getY()+" Y:X "+highlight.getX()+"e val"+e.getData(), Toast.LENGTH_SHORT).show();

        highlight.getY();

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}