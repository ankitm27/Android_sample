package com.cbat.cbat.ui.accounts.customerList.details.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbat.cbat.R;

public class CustomerListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] maintitle;
    private final String[] subtitle;
    private final String[] totalvalue;
   private final Integer[] imgid;
    private final Integer[] sales;

    public CustomerListAdapter(Activity context, String[] maintitle, String[] subtitle, Integer[] sales, String[] total, Integer[] imgid) {
        super(context, R.layout.customer_card, maintitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.maintitle=maintitle;
        this.subtitle = subtitle;
        //    this.subtitle=subtitle;
        this.totalvalue=total;
        this.imgid=imgid;
        this.sales=sales;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.customer_card, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        ImageView imageV = (ImageView) rowView.findViewById(R.id.sales);
        TextView total = (TextView) rowView.findViewById(R.id.total);
        ImageView sideArrowView = (ImageView) rowView.findViewById(R.id.arrowIcon);


        titleText.setText(maintitle[position]);
        total.setText(totalvalue[position]);
        imageView.setImageResource(imgid[position]);
        imageV.setImageResource(sales[position]);

        return rowView;

    };
}

