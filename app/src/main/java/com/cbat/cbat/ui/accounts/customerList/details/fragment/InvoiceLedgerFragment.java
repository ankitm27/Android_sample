package com.cbat.cbat.ui.accounts.customerList.details.fragment;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.gr.java_conf.androtaku.geomap.GeoMapView;

import static android.content.ContentValues.TAG;
public class InvoiceLedgerFragment extends Fragment {
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
    TextView customerName,legderDate,ledgerType,ledgerRef,customerState;
    public static List<JSONObject> customerInvoiceSummaryList=new ArrayList<>();


    public static com.cbat.cbat.ui.services.recievable.details_receivable.LedgerDetailFragment_R newInstance() {
        return new com.cbat.cbat.ui.services.recievable.details_receivable.LedgerDetailFragment_R();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tfLight = Typeface.createFromAsset(getActivity().getAssets(), "arial.ttf");
        GlobalClass.pdfDataTab="ledgerReceivableDetails";

        View v = inflater.inflate(R.layout.invoice_ledger_detail_fragment, container, false);
        totalSalesLayout=(LinearLayout)v.findViewById(R.id.totalSalesLayout);
        customerDetailLayout=(LinearLayout)v.findViewById(R.id.customerDetailLayout);
        customerName=(TextView)v.findViewById(R.id.customerName);
        legderDate=(TextView)v.findViewById(R.id.legderDate);
        ledgerType=(TextView)v.findViewById(R.id.ledgerType);
        ledgerRef=(TextView)v.findViewById(R.id.ledgerRef);
        customerState=(TextView)v.findViewById(R.id.customerState);

        tableFixHeaders = (TableFixHeaders) v.findViewById(R.id.tablefixheaders);
        // tableFixHeaders = new TableFixHeaders(getContext());
        tableFixHeadersAdapterFactory = new TableFixHeadersAdapterFactory(getContext());
        createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE,getCustomerSixMBody());
        //    final ProgressBar progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        final FrameLayout totalSalesContainer = (FrameLayout)v.findViewById(R.id.totalSalesContainer);


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
            for (int i = 0; i < customerInvoiceSummaryList.size(); i++) {
                JSONObject element = customerInvoiceSummaryList.get(i);
                int yVal = element.getInt("amount");
                BigDecimal number1 = new BigDecimal(yVal);
                float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                Log.d(TAG, "debitFloat >" + debitFloat);

                if(vocherFlag){
                    JSONObject  voucher=  element.getJSONObject("voucher");
                    String customerNameT=voucher.getString("name");
                    String legderDateT=voucher.getString("date");
                    String[] date=legderDateT.split("T");
                    String ledgerTypeT=voucher.getString("voucherType");
                    String ledgerRefT=voucher.getString("refNo");
                    String stateT=voucher.getString("state");
                    customerName.setText(customerNameT);
                    customerState.setText(stateT);
                    legderDate.setText(date.length>0?date[0]:legderDateT);
                    ledgerType.setText(" | Type# - "+ledgerTypeT);
                    ledgerRef.setText("Ref# - "+ledgerRefT);
                }
                items.add(new NexusWithImage(type, element.getString("name"),  Utility.decimal2Palce(Math.abs(debitFloat )),element.getString("quantity") , element.getString("rate"), "", "", "","","","","","","",""));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // items.add(new NexusWithImage(type, resImages));
//        items.add(new NexusWithImage(type, "404","Rs. 1,30,546", "20", "", "", "", ""));
//        items.add(new NexusWithImage(type, "507","Rs. 3,10,146", "20", "", "", "", ""));
//        items.add(new NexusWithImage(type, "612","Rs. 2,50,146", "20", "", "", "", ""));
//        items.add(new NexusWithImage(type, "715","Rs. 90,146", "20", "", "", "", ""));
//        items.add(new NexusWithImage(type, "450","Rs. 50,146", "20", "", "", "", ""));
//        items.add(new NexusWithImage(type, "1098","Rs. 1,50,146", "20", "", "", "", ""));


//        //items.add(new NexusWithImage(type, "Nexus S", "Samsung", "Gingerbread", "10", "16 GB", "4\"", "512 MB"));
//        items.add(new NexusWithImage(type, "Galaxy Nexus (16 GB)", "Samsung", "Ice cream Sandwich", "15", "16 GB", "4.65\"", "1 GB"));
//        items.add(new NexusWithImage(type, "Galaxy Nexus (32 GB)", "Samsung", "Ice cream Sandwich", "15", "32 GB", "4.65\"", "1 GB"));
//        items.add(new NexusWithImage(type, "Nexus 4 (8 GB)", "LG", "Jelly Bean", "17", "8 GB", "4.7\"", "2 GB"));
//        items.add(new NexusWithImage(type, "Nexus 4 (16 GB)", "LG", "Jelly Bean", "17", "16 GB", "4.7\"", "2 GB"));
//
//        type = "Tablets";
//        items.add(new NexusWithImage(type, resImages));
//        items.add(new NexusWithImage(type, "Nexus 7 (16 GB)", "Asus", "Jelly Bean", "16", "16 GB", "7\"", "1 GB"));
//        items.add(new NexusWithImage(type, "Nexus 7 (32 GB)", "Asus", "Jelly Bean", "16", "32 GB", "7\"", "1 GB"));
//        items.add(new NexusWithImage(type, "Nexus 10 (16 GB)", "Samsung", "Jelly Bean", "17", "16 GB", "10\"", "2 GB"));
//        items.add(new NexusWithImage(type, "Nexus 10 (32 GB)", "Samsung", "Jelly Bean", "17", "32 GB", "10\"", "2 GB"));
//
//        type = "Others";
//        items.add(new NexusWithImage(type, resImages));
//        items.add(new NexusWithImage(type, "Nexus Q", "--", "Honeycomb", "13", "--", "--", "--"));

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