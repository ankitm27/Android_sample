package com.cbat.cbat.ui.accounts;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.cbat.cbat.R;
import com.cbat.cbat.adapter.AccountsListAdapter;
import com.cbat.cbat.ui.BaseFragment;
import com.cbat.cbat.ui.accounts.customerList.details.CustomerListActivity;
import com.cbat.cbat.ui.widgets.UIFragmentTabHost;
import com.cbat.cbat.util.Utility;

public class AccountsFragment extends BaseFragment {
    private FragmentManager fragmentManager;
    private UIFragmentTabHost fragmentTabHost;
    ListView list;

    String[] maintitle ={
            "Cutomer Accounts","Vendors Accounts",
            "Cash at Bank Accounts","Order Booked Account",
            "Sales Account","Purchase Account","Open Purchase Account",
            "Stock Account","Top Accounts"
    };

    String[] subtitle ={
            "Sub Title 1","Sub Title 2",
            "Sub Title 3","Sub Title 4",
            "Sub Title 5","Sub Title 6","Sub Title 7","Sub Title 8","Sub Title 9"
    };

    Integer[] imgid={
            R.drawable.ic_customer_accounts,R.drawable.vendor_accounts,
            R.drawable.cashat_bank_accounts,R.drawable.order_booked_accounts,
            R.drawable.sales_accounts,R.drawable.purchase_account,
            R.drawable.open_purchase_account,R.drawable.stock_accounts,
            R.drawable.top_accounts
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.accounts_fragment, container, false);

        AccountsListAdapter adapter=new AccountsListAdapter(getActivity(), maintitle, subtitle,imgid);
        list=(ListView)v.findViewById(R.id.list);

        list.setAdapter(adapter);

        Utility.setListViewHeightBasedOnItems(list);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
                if(position == 0) {
                    //code specific to first list item
                    Toast.makeText(getContext(),"Place Your First Option Code",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), CustomerListActivity.class);
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
        return R.layout.accounts_fragment;
    }


}