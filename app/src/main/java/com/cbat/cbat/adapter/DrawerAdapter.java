package com.cbat.cbat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbat.cbat.R;
import com.cbat.cbat.model.DrawerModel;
import com.cbat.cbat.ui.home.HomeFragment;
import com.cbat.cbat.ui.login.ForgetPassActivity;
import com.cbat.cbat.ui.login.LoginActivity;

import java.util.ArrayList;

public class DrawerAdapter extends ArrayAdapter<DrawerModel> {

    private final Context context;
    private final ArrayList<DrawerModel> modelsArrayList;

    public DrawerAdapter(Context context, ArrayList<DrawerModel> modelsArrayList) {

        super(context, R.layout.view_ui_drawer, modelsArrayList);

        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater

        View rowView = null;
        //if(!modelsArrayList.get(position).isGroupHeader()){
            rowView = inflater.inflate(R.layout.view_ui_drawer, parent, false);

            // 3. Get icon,title & counter views from the rowView
            ImageView imgView = (ImageView) rowView.findViewById(R.id.drawer_img_icon);
            TextView titleView = (TextView) rowView.findViewById(R.id.drawer_tv_name);
            //TextView counterView = (TextView) rowView.findViewById(R.id.item_counter);

            // 4. Set the text for textView
            imgView.setImageResource(modelsArrayList.get(position).getIcon());
            titleView.setText(modelsArrayList.get(position).getTitle());
           // counterView.setText(modelsArrayList.get(position).getCounter());
//        /}
//        else{
//            rowView = inflater.inflate(R.layout.group_header_item, parent, false);
//            TextView titleView = (TextView) rowView.findViewById(R.id.header);
//            titleView.setText(modelsArrayList.get(position).getTitle());
//
//        }

        // 5. retrn rowView
               return rowView;
    }
}
