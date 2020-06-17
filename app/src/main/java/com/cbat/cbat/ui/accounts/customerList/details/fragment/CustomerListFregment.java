package com.cbat.cbat.ui.accounts.customerList.details.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cbat.cbat.R;
import com.cbat.cbat.ui.BaseFragment;
import com.cbat.cbat.ui.accounts.customerList.details.CustomerLedgerActivity;
import com.cbat.cbat.ui.accounts.customerList.details.CustomerListActivity;
import com.cbat.cbat.ui.accounts.customerList.details.adapter.CustomerListAdapter;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.Utility;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class CustomerListFregment extends BaseFragment {

    ListView list;

    String[] maintitle ={
            "Akaash","Bajaj Finance Ltd",
            "Cash at Bank Accounts","Order Booked Account",
            "Sales Account","Purchase Account",
            "Open Purchase Account", "Stock Account",
            "Top Accounts"
    };

    String[] totalvalue ={
            "10000","20000",
            "12345","16540",
            "170650","12665",
            "89789", "170650",
            "12665",

    };
    String[] subtitle ={
            "Sub Title 1","Sub Title 2",
            "Sub Title 3","Sub Title 4",
            "Sub Title 5","Sub Title 6","Sub Title 7","Sub Title 8","Sub Title 9"
    };

    Integer[] imgid={
            R.drawable.ic_customer_accounts,R.drawable.ic_customer_accounts,
            R.drawable.ic_customer_accounts,R.drawable.ic_customer_accounts,
            R.drawable.ic_customer_accounts,R.drawable.ic_customer_accounts,
            R.drawable.ic_customer_accounts,R.drawable.ic_customer_accounts,
            R.drawable.ic_customer_accounts
    };

    Integer[] sales={
            R.drawable.sales_accounts,R.drawable.sales_accounts,
            R.drawable.sales_accounts,R.drawable.sales_accounts,
            R.drawable.sales_accounts,R.drawable.sales_accounts,
            R.drawable.sales_accounts,R.drawable.sales_accounts,
            R.drawable.sales_accounts
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.customer_list, container, false);

        CustomerListAdapter adapter=new CustomerListAdapter(getActivity(), maintitle, subtitle, sales,totalvalue, imgid );
        list=(ListView)v.findViewById(R.id.list);
        list.setAdapter(adapter);
        Utility.setListViewHeightBasedOnItems(list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
                if(position == 0) {
                    //code specific to first list item
                    Toast.makeText(getContext(),"Customer List ",Toast.LENGTH_SHORT).show();
                    Log.d("details:<>",parent.getItemAtPosition(position).toString());
                    GlobalClass.CustomerAccountName=parent.getItemAtPosition(position).toString();
                    Intent intent = new Intent(getContext(), CustomerLedgerActivity.class);
                    startActivity(intent);
                }

                else if(position == 1) {
                    //code specific to 2nd list item
                    Toast.makeText(getContext(),"Place Your Second Option Code",Toast.LENGTH_SHORT).show();
                }

                else if(position == 2) {

                    Toast.makeText(getContext(),"Place Your Third Option Code",Toast.LENGTH_SHORT).show();
                }
                else if(position == 3) {

                    Toast.makeText(getContext(),"Place Your Forth Option Code",Toast.LENGTH_SHORT).show();
                }
                else if(position == 4) {

                    Toast.makeText(getContext(),"Place Your Fifth Option Code",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return v;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.customer_list;
    }




}
