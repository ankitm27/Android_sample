package com.cbat.cbat.ui.accounts.customerList.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cbat.cbat.R;
import com.cbat.cbat.adapter.ListPopupWindowAdapter;
import com.cbat.cbat.model.ListPopupItem;
import com.cbat.cbat.ui.accounts.customerList.details.fragment.CustomerListFregment;
import com.cbat.cbat.ui.accounts.customerList.details.fragment.customerLedgerFragment;
import com.cbat.cbat.ui.accounts.customerList.details.navigation.AccountDrawerActivity;
import com.cbat.cbat.ui.navigation.InvoiceDetailActivity;
import com.cbat.cbat.ui.navigation.MainActivity;
import com.cbat.cbat.util.GlobalClass;

import java.util.ArrayList;
import java.util.List;

public class CustomerLedgerActivity  extends AccountDrawerActivity {
    LinearLayout customerLedger;
    FrameLayout customerledgerragment;
    String[] Customer ={
            "Akaash","Bajaj Finance Ltd",
            "Cash at Bank Accounts","Order Booked Account",
            "Sales Account","Purchase Account",
            "Open Purchase Account", "Stock Account",
            "Top Accounts"
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.customer_ledger_activity, null, false);

        customerLedger=(LinearLayout)contentView.findViewById(R.id.customerLedger);
        customerledgerragment=(FrameLayout)contentView.findViewById(R.id.customerledgerfragment);

        customerLedgerFragment fragment = new customerLedgerFragment();
        loadFragment(fragment);

        toolbar.setTitle("Cus.a/c.");

        navigationLayout.setVisibility(View.VISIBLE);
        imageLayout.setVisibility(View.GONE);
        navigationHome.setText("Accounts");
        navigationBack.setText("a/c.");
        navigationCurrentMain.setText(GlobalClass.CustomerAccountName);


        navigationBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                List<ListPopupItem> listPopupItems = new ArrayList<>();
                for(String cutomer:Customer) {
                    listPopupItems.add(new ListPopupItem(cutomer, R.mipmap.ic_launcher));

                }

                showListServicePopupWindow(v, listPopupItems);

            }
        });
        navigationHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                Intent intent = new Intent(getBaseContext(), MainActivity.class);

                GlobalClass.selectedMainTab = 2;
                startActivity(intent);
            }
        });
        drawer.addView(contentView, 0);
    }


    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.customerledgerfragment, fragment);
        fragmentTransaction.commit(); // save the changes
    }


    private void showListServicePopupWindow(View anchor, List<ListPopupItem> listPopup) {
        List<ListPopupItem> listPopupItems = new ArrayList<>();
        listPopupItems=listPopup;

        //listPopupItems.add(new ListPopupItem("Crore", R.mipmap.ic_launcher));

        final ListPopupWindow listPopupWindow =
                createListPopupWindow(anchor, 200, listPopupItems);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();
                // float amount = 100000;
                // NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

                // String moneyString = formatter.format(amount);
                //  totalSalesVal.setText(moneyString);
                ListPopupWindowAdapter.ViewHolder  adapter =(ListPopupWindowAdapter.ViewHolder) view.getTag();
                Intent intent = new Intent(getBaseContext(), CustomerListActivity.class);
               startActivity(intent);
//                Bundle bundle = new Bundle();
//                bundle.putString("StateName", stateName);
//                bundle.putString("BackFrgment", "TotalSale");
//                bundle.putString("CurrentFrgment", stateName);
//                bundle.putString("CurrentFrgmentMain", adapter.getTvTitle().getText().toString());
//                bundle.putString("CutomerName", adapter.getTvTitle().getText().toString());
//                bundle.putString("Title", "Invoices for "+adapter.getTvTitle().getText().toString());
//                myIntent.putExtras(bundle);
//                GlobalClass.currentFrgmentMain=adapter.getTvTitle().getText().toString();
//                GlobalClass.cutomerName=adapter.getTvTitle().getText().toString();
//                GlobalClass.title="Invoices "+(GlobalClass.month!=null && !GlobalClass.month.isEmpty()?"of "+GlobalClass.month:"")+" for "+adapter.getTvTitle().getText().toString();
//                startActivity(myIntent);
//                 Toast.makeText(getApplicationContext(), "clicked at " +adapter.getTvTitle().getText(), Toast.LENGTH_SHORT)
//                         .show();
            }
        });
        listPopupWindow.show();
    }

    private ListPopupWindow createListPopupWindow(View anchor, int width,
                                                  List<ListPopupItem> items) {
        final ListPopupWindow popup = new ListPopupWindow(getBaseContext());
        ListAdapter adapter = new ListPopupWindowAdapter(items);
        popup.setAnchorView(anchor);
        popup.setWidth(width);
        popup.setAdapter(adapter);
        return popup;
    }
}
