package com.cbat.cbat.ui.services;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cbat.cbat.R;
import com.cbat.cbat.adapter.SliderPagerAdapter;
import com.cbat.cbat.ui.BaseFragment;
import com.cbat.cbat.ui.login.ForgetPassActivity;
import com.cbat.cbat.ui.login.LoginActivity;
import com.cbat.cbat.ui.navigation.ServicesDetailActivity;
import com.cbat.cbat.ui.widgets.DrawerActivity;
import com.cbat.cbat.util.ChartUtilsCustom;
import com.cbat.cbat.util.GlobalClass;

import java.util.ArrayList;
import java.util.List;

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

public class ServicesFragment extends BaseFragment {


    public static ServicesFragment newInstance() {
        return new ServicesFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.services_fragment, container, false);
        //titleBar.setMainTitleText("Services");
        LinearLayout salesLayout=(LinearLayout)v.findViewById(R.id.salesLayout);
        salesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), ServicesDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("BackFrgment", "Services");
//                bundle.putString("CurrentFrgment", "Sales");
//                bundle.putString("Title", "Sales");
//                myIntent.putExtras(bundle);
                GlobalClass.backFrgment="Services";
                GlobalClass.currentFrgment="Sales";
                GlobalClass.title="Sales";
                getActivity().startActivity(myIntent);

            }
        });
        LinearLayout payableLayout=(LinearLayout)v.findViewById(R.id.payableLayout);
        payableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), ServicesDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("BackFrgment", "Services");
//                bundle.putString("CurrentFrgment", "Payable");
//                bundle.putString("Title", "Payable");
              //  myIntent.putExtras(bundle);
                GlobalClass.backFrgment="Services";
                GlobalClass.currentFrgment="Payable";
                GlobalClass.title="Payable";
                getActivity().startActivity(myIntent);

            }
        });
        LinearLayout recievableLayout=(LinearLayout)v.findViewById(R.id.recievableLayout);
        recievableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), ServicesDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("BackFrgment", "Services");
//                bundle.putString("CurrentFrgment", "Recievable");
//                bundle.putString("Title", "Recievable");
               // myIntent.putExtras(bundle);
                GlobalClass.backFrgment="Services";
                GlobalClass.currentFrgment="Receivable";
                GlobalClass.title="Receivable";
                GlobalClass.selectedTabPosition="0";
                getActivity().startActivity(myIntent);

            }
        });
        LinearLayout inventoryLayout=(LinearLayout)v.findViewById(R.id.inventoryLayout);
        inventoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), ServicesDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("BackFrgment", "Services");
//                bundle.putString("CurrentFrgment", "Inventory");
//                bundle.putString("Title", "Inventory");
//                myIntent.putExtras(bundle);
                GlobalClass.backFrgment="Services";
                GlobalClass.currentFrgment="Inventory";
                GlobalClass.title="Inventory";
                getActivity().startActivity(myIntent);

            }
        });
        LinearLayout managmentLayout=(LinearLayout)v.findViewById(R.id.managmentLayout);
        managmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), ServicesDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("BackFrgment", "Services");
//                bundle.putString("CurrentFrgment", "Managment");
//                bundle.putString("Title", "Managment");
//                myIntent.putExtras(bundle);
                GlobalClass.backFrgment="Services";
                GlobalClass.currentFrgment="Managment";
                GlobalClass.title="Managment";
                getActivity().startActivity(myIntent);

            }
        });
        return v;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.services_fragment;
    }


}