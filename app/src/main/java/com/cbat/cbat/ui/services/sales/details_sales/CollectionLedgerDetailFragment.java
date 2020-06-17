package com.cbat.cbat.ui.services.sales.details_sales;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cbat.cbat.R;
import com.cbat.cbat.adapter.TableFixHeaderAdapter;
import com.cbat.cbat.adapter.TableFixHeadersAdapterFactory;
import com.cbat.cbat.adapter.original_sortable.ItemSortable;
import com.cbat.cbat.adapter.original_sortable.NexusWithImage;
import com.cbat.cbat.adapter.original_sortable.OriginalBodyCellViewGroup;
import com.cbat.cbat.adapter.original_sortable.OriginalFirstBodyCellViewGroup;
import com.cbat.cbat.adapter.original_sortable.OriginalSortableTableFixHeader;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.Utility;
import com.github.mikephil.charting.charts.BarChart;
import com.google.android.material.snackbar.Snackbar;
import com.inqbarna.tablefixheaders.TableFixHeaders;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import jp.gr.java_conf.androtaku.geomap.GeoMapView;


public class CollectionLedgerDetailFragment extends Fragment {
    String TAG="LedgerDetailFragment";
    private LayoutInflater layoutInflater;
    private TableFixHeaders tableFixHeaders;
    private TableFixHeadersAdapterFactory tableFixHeadersAdapterFactory;
    GeoMapView geoMapView;
    LinearLayout totalSalesLayout;
    LinearLayout customerDetailLayout;
    LinearLayout barchats;
    View chartLayout;
    private BarChart chart;
    protected Typeface tfLight;
    TextView customerName,legderDate,ledgerType,ledgerRef,customerState,customerAddress;
    public static List<JSONObject> customerInvoiceSummaryList=new ArrayList<>();
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));


    public static CollectionLedgerDetailFragment newInstance() {
        return new CollectionLedgerDetailFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tfLight = Typeface.createFromAsset(getActivity().getAssets(), "arial.ttf");

        View v = inflater.inflate(R.layout.ledger_detail_fragment, container, false);
        totalSalesLayout=(LinearLayout)v.findViewById(R.id.totalSalesLayout);
        customerDetailLayout=(LinearLayout)v.findViewById(R.id.customerDetailLayout);
        customerName=(TextView)v.findViewById(R.id.customerName);
        legderDate=(TextView)v.findViewById(R.id.legderDate);
        ledgerType=(TextView)v.findViewById(R.id.ledgerType);
        ledgerRef=(TextView)v.findViewById(R.id.ledgerRef);
        customerState=(TextView)v.findViewById(R.id.customerState);
        customerAddress=(TextView)v.findViewById(R.id.customerAddress);
        tableFixHeaders = (TableFixHeaders) v.findViewById(R.id.tablefixheaders);
        // tableFixHeaders = new TableFixHeaders(getContext());
        tableFixHeadersAdapterFactory = new TableFixHeadersAdapterFactory(getContext());
        createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE,getCustomerSixMBody());
    //    final ProgressBar progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        final FrameLayout totalSalesContainer = (FrameLayout)v.findViewById(R.id.totalSalesContainer);
        GlobalClass.pdfDataTab="ledgerDetails";

        return v;
    }

    private void createTable(int type,List<NexusWithImage> customerBody) {

        //BaseTableAdapter originalSortableTableFixHeader=tableFixHeadersAdapterFactory.getAdapter(type);
        OriginalSortableTableFixHeader originalSortableTableFixHeader=new OriginalSortableTableFixHeader(getContext());
        originalSortableTableFixHeader.setHeader(getCustomerHeader());
        originalSortableTableFixHeader.setBody(customerBody);
        originalSortableTableFixHeader.setClickListenerBody(setClickListenerBody);
        originalSortableTableFixHeader.setClickListenerFirstBody(setClickListenerFirstBody);
        tableFixHeaders.setAdapter(originalSortableTableFixHeader.getInstance());
        originalSortableTableFixHeader.getInstance().notifyDataSetChanged();


        //tableFixHeaders.setAdapter(tableFixHeadersAdapterFactory.getAdapter(type));
    }

    public ItemSortable[] getCustomerHeader(){
        ItemSortable headers[] = {
              //  new ItemSortable("Amount("+GlobalClass.currecyPrefix+")"),
                new ItemSortable("Amount"),
                new ItemSortable("Quantity"),
                new ItemSortable("Rate"),
                new ItemSortable("Item")
        };
        return headers;
    }

    public List<NexusWithImage> getCustomerSixMBody(){
        List<Integer> resImages = Arrays.asList();

        List<NexusWithImage> items = new ArrayList<>();
        String type = "Invoice";
        if(GlobalClass.customerInvoiceSummaryList!=null && !GlobalClass.customerInvoiceSummaryList.isEmpty()){
            customerInvoiceSummaryList=GlobalClass.customerInvoiceSummaryList;
            Log.d(TAG, "debitFloat >" + GlobalClass.customerInvoiceSummaryList.size());
        }

        Log.d(TAG, "Hi >");
        try {
            boolean vocherFlag=true;
           // items.add(new NexusWithImage(type, "404","Rs. 1,30,546", "20", "", "", "", "","","","","","",""));
            float totalAmount=0;
            for (int i = 0; i < customerInvoiceSummaryList.size(); i++) {
                JSONObject element = customerInvoiceSummaryList.get(i);
                int yVal = element.getInt("amount");
                BigDecimal number1 = new BigDecimal(yVal);
               /// float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                float debitFloat = number1.floatValue();
                Log.d(TAG, "debitFloat >" + debitFloat);
                totalAmount=totalAmount+debitFloat;
                if(vocherFlag){
                JSONObject  voucher=  element.getJSONObject("voucher");
                String customerNameT=voucher.getString("accountName");
                    String legderDateT=voucher.getString("date");
                    String address=voucher.getString("address");
                    String[] date=legderDateT.split("T");
                    String ledgerTypeT=voucher.getString("voucherType");
                    String ledgerRefT=voucher.getString("refNo");
                    String stateT=voucher.getString("state");
                    customerName.setText(customerNameT);
                    customerAddress.setText(address);
                    customerState.setText(stateT);
                    legderDate.setText(date.length>0?date[0]:legderDateT);
                    ledgerType.setText(" | Type# - "+ledgerTypeT);
                    ledgerRef.setText("Ref# - "+ledgerRefT);
                    vocherFlag=false;
                }
                String moneyString = formatter.format(Math.abs(debitFloat));
               // items.add(new NexusWithImage(type, element.getString("name"),  Utility.decimal2Palce(Math.abs(debitFloat )),element.getString("quantity") , element.getString("rate"), "", "", "","","","","","","",""));
                items.add(new NexusWithImage(type, element.getString("name"),  moneyString,element.getString("quantity") , element.getString("rate"), "", "", "","","","","","","",""));

            }
            items.add(new NexusWithImage(type, "Total",  formatter.format(Math.abs(totalAmount )),"" , "", "", "", "","","","","","","",""));

        }catch (Exception e){
            e.printStackTrace();
        }

        return items;
    }

    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup> setClickListenerBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup>() {
        @Override
        public void onClickItem(NexusWithImage item, OriginalBodyCellViewGroup viewGroup, int row, int column) {
            Snackbar.make(viewGroup, "Yes we do it " + item.data[column + 1] + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();
        }
    };

    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup> setClickListenerFirstBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup>() {
        @Override
        public void onClickItem(NexusWithImage item, OriginalFirstBodyCellViewGroup viewGroup, int row, int column) {
            Snackbar.make(viewGroup, "Cutomer Name is " + item.data[column + 1] + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();
//            Intent myIntent = new Intent(getActivity(), LedgerDetailActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("BackFrgment", "TotalSale");
//            bundle.putString("CurrentFrgment", "J&K");
//            bundle.putString("CurrentFrgmentMain", "Amazone");
//            bundle.putString("CurrentFrgmentFinal", "404");
//            bundle.putString("Title", "404 Voucher Detail ");
            //          myIntent.putExtras(bundle);
            //       startActivity(myIntent);
        }
    };



}