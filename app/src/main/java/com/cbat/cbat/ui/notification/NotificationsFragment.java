package com.cbat.cbat.ui.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cbat.cbat.R;
import com.cbat.cbat.adapter.AccountsListAdapter;
import com.cbat.cbat.adapter.NotificationListAdapter;
import com.cbat.cbat.ui.BaseFragment;

public class NotificationsFragment extends BaseFragment {

    ListView list;

    String[] maintitle ={
            "Rs.20000 Payment Due","Rs.40000 Receivable Due",
            "Rs.15000 Loan Due"
    };

    String[] subtitle ={
            "Sub Title 1","Sub Title 2",
            "Sub Title 3"
    };

    Integer[] imgid={
            R.drawable.payment_due,R.drawable.receivable_due,
            R.drawable.loan_due
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.notification_fragment, container, false);

        NotificationListAdapter adapter=new NotificationListAdapter(getActivity(), maintitle, subtitle,imgid);
        list=(ListView)v.findViewById(R.id.notificationList);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
                if(position == 0) {
                    //code specific to first list item
                    Toast.makeText(getContext(),"Place Your First Option Code",Toast.LENGTH_SHORT).show();
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