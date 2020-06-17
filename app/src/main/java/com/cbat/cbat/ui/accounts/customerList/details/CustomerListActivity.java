package com.cbat.cbat.ui.accounts.customerList.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cbat.cbat.R;
import com.cbat.cbat.ui.accounts.customerList.details.navigation.AccountDrawerActivity;

import com.cbat.cbat.ui.accounts.customerList.details.fragment.CustomerListFregment;
import com.cbat.cbat.ui.navigation.MainActivity;
import com.cbat.cbat.util.GlobalClass;

public class CustomerListActivity extends AccountDrawerActivity {

    LinearLayout customerlist;
    FrameLayout customerlistfragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.customer_list_activity, null, false);

        customerlist=(LinearLayout)contentView.findViewById(R.id.customerlist);
        customerlistfragment=(FrameLayout)contentView.findViewById(R.id.customerlistfragment);

        CustomerListFregment fragment = new CustomerListFregment();
         loadFragment(fragment);

        toolbar.setTitle("Customer List");
        navigationLayout.setVisibility(View.VISIBLE);
        imageLayout.setVisibility(View.GONE);
        navigationBack.setText("Accounts");
        navigationCurrentMain.setText("Cust.a/c.");


        navigationBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                Intent intent = new Intent(getBaseContext(), MainActivity.class);

                GlobalClass.selectedMainTab = 2;
                startActivity(intent);
            }
        });
        navigationHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                Intent intent = new Intent(getBaseContext(), MainActivity.class);

                GlobalClass.selectedMainTab = 4;
                startActivity(intent);
            }
        });
        drawer.addView(contentView, 0);
    }


    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.customerlistfragment, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}
