package com.cbat.cbat.ui.accounts.customerList.details.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cbat.cbat.R;
import com.cbat.cbat.adapter.TableFixHeaderAdapter;
import com.cbat.cbat.adapter.TableFixHeadersAdapterFactory;
import com.cbat.cbat.adapter.original_sortable.ItemSortable;
import com.cbat.cbat.adapter.original_sortable.NexusWithImage;
import com.cbat.cbat.adapter.original_sortable.OriginalBodyCellViewGroup;
import com.cbat.cbat.adapter.original_sortable.OriginalFirstBodyCellViewGroup;
import com.cbat.cbat.adapter.original_sortable.OriginalSortableTableFixHeader;
import com.cbat.cbat.ui.BaseFragment;
import com.cbat.cbat.ui.accounts.customerList.details.InvoiceLedgerActivity;
import com.cbat.cbat.ui.accounts.customerList.details.adapter.CustomerListAdapter;
import com.cbat.cbat.ui.services.recievable.details_receivable.InvoiceDetailActivity_R;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.Utility;
import com.google.android.material.snackbar.Snackbar;
import com.inqbarna.tablefixheaders.TableFixHeaders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class customerLedgerFragment extends BaseFragment {

    String[] Ledger ={
            "9",
            "SBI Bank Account",
            "sales",
            "8",
            "10000.00",
            "2,05,000",
            "89789",
            "89789",
            "89789",
    };


    JSONArray js=new JSONArray();
    private TableFixHeaders tableFixHeaders;
    private TableFixHeadersAdapterFactory tableFixHeadersAdapterFactory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.customer_ledger, container, false);
        try {
            addJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tableFixHeaders = (TableFixHeaders) v.findViewById(R.id.tablefixheaders);
        tableFixHeadersAdapterFactory = new TableFixHeadersAdapterFactory(getContext());
        createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());

        return v;
    }

    public List<NexusWithImage> getCustomerSixMBody() {
        List<Integer> resImages = Arrays.asList();

        List<NexusWithImage> items = new ArrayList<>();
        String type = "Customer Ledger";
        // items.add(new NexusWithImage(type, resImages));
        if( GlobalClass.salesStateCustomer!=null &&  !GlobalClass.salesStateCustomer.isEmpty()) {
            GlobalClass.salesStateCustomer.clear();
        }else{
            GlobalClass.salesStateCustomer= new ArrayList<>();
        }
//        if(GlobalClass.customerFullList!=null && !GlobalClass.customerFullList.isEmpty()) {
//            customerFullList = GlobalClass.customerFullList;
//        }

        int count = 0;


        String[] dataTable = new String[9];
        for(int i=0; i<Ledger.length; i++) {
            dataTable[i]=Ledger[i];
        }
        items.add(new NexusWithImage(type, dataTable));

        return items;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.customer_ledger;
    }
    private void createTable(int type, List<NexusWithImage> customerBody) {

        //BaseTableAdapter originalSortableTableFixHeader=tableFixHeadersAdapterFactory.getAdapter(type);
        OriginalSortableTableFixHeader originalSortableTableFixHeader = new OriginalSortableTableFixHeader(getContext());
        originalSortableTableFixHeader.setHeader(getCustomerHeader());
        originalSortableTableFixHeader.setBody(customerBody);
        originalSortableTableFixHeader.setClickListenerBody(setClickListenerBody);
        originalSortableTableFixHeader.setClickListenerFirstBody(setClickListenerFirstBody);
        tableFixHeaders.setAdapter(originalSortableTableFixHeader.getInstance());
        originalSortableTableFixHeader.getInstance().notifyDataSetChanged();


        //tableFixHeaders.setAdapter(tableFixHeadersAdapterFactory.getAdapter(type));
    }
    public ItemSortable[] getCustomerHeader() {
        ItemSortable headers[] = {
                new ItemSortable("Particular"),
                new ItemSortable("Cch Type"),
                new ItemSortable("Vch No."),
                new ItemSortable("Debit"),
                new ItemSortable("Credit"),
                new ItemSortable("Cur. Credit."),
                new ItemSortable("Cur. Close."),
                new ItemSortable("Close. bal."),
                new ItemSortable("Vouc.No"),

        };
        return headers;
    }
    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup> setClickListenerBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup>() {
        @Override
        public void onClickItem(NexusWithImage item, OriginalBodyCellViewGroup viewGroup, int row, int column) {

            //Snackbar.make(viewGroup, "Yes we do it " + item.data[0] + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();

            //Snackbar.make(viewGroup, "Yes we do it " + item.data[column + 1] + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();
            Intent myIntent = new Intent(getActivity(), InvoiceLedgerActivity.class);
//
            GlobalClass.currentFrgmentMain = item.data[0];
            GlobalClass.cutomerName = item.data[0];
            GlobalClass.title = "Invoices for " + item.data[0];
            startActivity(myIntent);
        }
    };

    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup> setClickListenerFirstBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup>() {
        @Override
        public void onClickItem(NexusWithImage item, OriginalFirstBodyCellViewGroup viewGroup, int row, int column) {


//            if (item.data[column + 1].equalsIgnoreCase("Total Sales")) {
//                fullSaleData = customerFullList.get("totalStateSales");
//                Snackbar snackbar = Snackbar.make(viewGroup, item.data[column + 1], Snackbar.LENGTH_LONG).setAction("Action", null);
//                //  customerFullList.get(item.data[column + 1] );
//                View sbView = snackbar.getView();
//                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.chart4));
//                snackbar.show();
//            } else {
//                fullSaleData = customerFullList.get(item.data[column + 1]);
//                Snackbar snackbar = Snackbar.make(viewGroup, "Cutomer Name is " + item.data[column + 1], Snackbar.LENGTH_LONG).setAction("Action", null);
//                //  customerFullList.get(item.data[column + 1] );
//                View sbView = snackbar.getView();
//                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.chart4));
//                snackbar.show();
//            }
            if (item.data[column + 1].equalsIgnoreCase("Total Sales")) {
           //     fullSaleData = customerFullList.get("totalStateSales");

                Snackbar snackbar = Snackbar.make(viewGroup, item.data[column + 1], Snackbar.LENGTH_LONG).setAction("Action", null);
                //  customerFullList.get(item.data[column + 1] );
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.chart4));
                snackbar.show();
            } else {
//                fullSaleData = customerFullList.get(item.data[column + 1]);
//                Snackbar snackbar = Snackbar.make(viewGroup, "Name: " + item.data[column + 1]+"\nAddress: "+item.data[15], Snackbar.LENGTH_LONG).setAction("Action", null);
//                //  customerFullList.get(item.data[column + 1] );
//                View sbView = snackbar.getView();
//                TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
//                textView.setMaxLines(5);
//                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.chart4));
//                snackbar.show();
            }
//            tabLayout.getTabAt(1).select();
//            setSalesData(1, fullSaleData);
//            chart.animateX(2000);
//            chart.refreshDrawableState();

        }
    };
public void addJson() throws JSONException {
    JSONObject obj = new JSONObject();
    try {
        obj.put("i", "9");
        obj.put("i", "SBI Bank Account");
        obj.put("i", "sales");
        obj.put("i", "10-12-2020");
        obj.put("i", "10000.00");
        obj.put("i", "2,05,000");
        obj.put("i", "89789");
        obj.put("i", "89789");
        obj.put("i", "89789");



    } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    js.put(obj);
    Log.d("json", js.toString());
    Log.d("json", String.valueOf(js.getJSONObject(0)));

}
}
