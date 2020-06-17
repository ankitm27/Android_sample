package com.cbat.cbat.ui.services.sales.details_sales;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cbat.cbat.R;
import com.cbat.cbat.adapter.TableFixHeaderAdapter;
import com.cbat.cbat.adapter.TableFixHeadersAdapterFactory;
import com.cbat.cbat.adapter.original_sortable.ItemSortable;
import com.cbat.cbat.adapter.original_sortable.NexusWithImage;
import com.cbat.cbat.adapter.original_sortable.OriginalBodyCellViewGroup;
import com.cbat.cbat.adapter.original_sortable.OriginalFirstBodyCellViewGroup;
import com.cbat.cbat.adapter.original_sortable.OriginalSortableTableFixHeader;
import com.cbat.cbat.ui.navigation.CustomerDetailActivity;
import com.cbat.cbat.ui.navigation.InvoiceDetailActivity;
import com.cbat.cbat.util.DayAxisValueFormatter;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.MyValueFormatter;
import com.cbat.cbat.util.PichartMarkerView;
import com.cbat.cbat.util.Utility;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.inqbarna.tablefixheaders.TableFixHeaders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.gr.java_conf.androtaku.geomap.GeoMapView;


public class CreditUtilizationDetailFragment extends Fragment {
    String TAG = "CustomerDetailFragment";
    private LayoutInflater layoutInflater;
    private TableFixHeaders tableFixHeaders;
    private TableFixHeadersAdapterFactory tableFixHeadersAdapterFactory;
    GeoMapView geoMapView;
    LinearLayout totalSalesLayout;
    //  LinearLayout customerChartLayout;
    //  LinearLayout barchats;
    //  View chartLayout;
    //  private BarChart chart;
    protected Typeface tfLight;
    String stateName;
    private ProgressDialog progressDialog;
    Map<String, Double> cutomerSalesMap;
    Map<String, JSONObject> customerFullList;
    JSONArray fullSaleData = new JSONArray();
    TabLayout tabLayout;
    ImageView crImage;
    ImageView lakhImage;

    ImageView thImage;
    ImageView top5Image;
    ImageView top10Image;
    ImageView top15Image;
    ImageView allImage;
    Map<String,JSONObject> customerDetailMap= new HashMap<>();
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    public static CreditUtilizationDetailFragment newInstance() {
        return new CreditUtilizationDetailFragment();
    }

    //    public  void setMap(Map<String,Double> cutomerSalesMap){
//        Log.d(TAG,"Size >> "+cutomerSalesMap.size());
//        this.cutomerSalesMap=cutomerSalesMap;
//    }
//    public  void setFullMap(Map<String,JSONArray> customerFullList){
//        //Log.d(TAG,"Size >> "+customerFullList.size());
//        fullSaleData= GlobalClass.customerFullList.get("totalStateSales");
//        //Log.d(TAG,"fullSaleData >> "+fullSaleData.length());
//        this.customerFullList=customerFullList;
//    }
    //MPLine
    private LineChart lineChart;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        //stateName = getArguments().getString("stateName");
        //GlobalClass.stateName = getArguments().getString("stateName");
        Log.d(TAG, "Tab >> " + GlobalClass.backFrgment);
//        if(GlobalClass.backFrgment.equalsIgnoreCase("Sales Trend")) {
//            Log.d(TAG,"Sales Trend Size >> "+GlobalClass.customerSalesTrendFullList.size());
//            customerFullList= GlobalClass.customerSalesTrendFullList;
//        }else
        if (GlobalClass.creditUtilizeFullList != null && !GlobalClass.creditUtilizeFullList.isEmpty()) {
            Log.d(TAG, "Other Size >> " + GlobalClass.creditUtilizeFullList.size());
            customerFullList = GlobalClass.creditUtilizeFullList;

        } else {
            customerFullList = new HashMap<>();
        }
        GlobalClass.sortingOn = -1;
        tfLight = Typeface.createFromAsset(getActivity().getAssets(), "arial.ttf");

        View v = inflater.inflate(R.layout.credit_utilization_detail_fragment, container, false);
        unbinder = ButterKnife.bind(getContext(), v);
        //heighSetting( v );
        tableFixHeaders = (TableFixHeaders) v.findViewById(R.id.tablefixheaders);
        // tableFixHeaders = new TableFixHeaders(getContext());
        tableFixHeadersAdapterFactory = new TableFixHeadersAdapterFactory(getContext());
        createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
        // final ProgressBar progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        final FrameLayout totalSalesContainer = (FrameLayout) v.findViewById(R.id.totalSalesContainer);
        totalSalesLayout = (LinearLayout) v.findViewById(R.id.totalSalesLayout);
        // customerChartLayout = (LinearLayout) v.findViewById(R.id.customerChartLayout);
        //   chartLayout = layoutInflater.inflate(R.layout.chart_tab_layout, customerChartLayout, false);
        //  tabLayout = (TabLayout) chartLayout.findViewById(R.id.chart_filter_tabs);
        //  barchats = (LinearLayout) chartLayout.findViewById(R.id.chart_container);
        //fullSaleData= customerFullList.get((customerFullList.size()-1));

        if (!customerFullList.isEmpty()) {
            Log.d(TAG, "Cusomter size " + customerFullList.size());
            // fullSaleData = customerFullList.get("totalStateSales");
        } else {
            fullSaleData = new JSONArray();
        }

        // barChartCreate(tabLayout);
        // getTotalSaleCutomerWise();
        crImage = v.findViewById(R.id.cr);
        crImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.currecyPrefix = "CR";

                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                // barChartCreate(tabLayout);
                resetCurrencyImage();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        lakhImage = v.findViewById(R.id.lakh);
        lakhImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.currecyPrefix = "LH";
                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                // barChartCreate(tabLayout);
                resetCurrencyImage();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        thImage = v.findViewById(R.id.th);
        thImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.currecyPrefix = "TH";
                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                //barChartCreate(tabLayout);
                resetCurrencyImage();
//                Toast.makeText(getContext(),
//
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        top5Image = v.findViewById(R.id.top5);
        top5Image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.sortingOn = 5;
                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                // customerFullList;
                //  barChartCreate(tabLayout);
                resetShortingImage();
                //  tabLayout.getTabAt(1).select();
                // setSalesData(1, fullSaleData);
                // chart.animateX(2000);
                // chart.refreshDrawableState();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        top10Image = v.findViewById(R.id.top10);
        top10Image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.sortingOn = 10;
                //fullSaleData = customerFullList.get(0);
                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                // barChartCreate(tabLayout);
                resetShortingImage();
                // tabLayout.getTabAt(1).select();
//                setSalesData(1, fullSaleData);
//                chart.animateX(2000);
//                chart.refreshDrawableState();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        top15Image = v.findViewById(R.id.top15);
        top15Image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.sortingOn = 15;
                //fullSaleData = customerFullList.get(0);
                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                // barChartCreate(tabLayout);
                resetShortingImage();
                // tabLayout.getTabAt(1).select();
//                setSalesData(1, fullSaleData);
//                chart.animateX(2000);
//                chart.refreshDrawableState();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        allImage = v.findViewById(R.id.alltop);
        allImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.sortingOn = -1;
                //fullSaleData = customerFullList.get("totalStateSales");
                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
                // barChartCreate(tabLayout);
                resetShortingImage();
                //tabLayout.getTabAt(1).select();
//                setSalesData(1, fullSaleData);
//                chart.animateX(2000);
//                chart.refreshDrawableState();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
        resetCurrencyImage();
        resetShortingImage();

        return v;
    }

    public void resetCurrencyImage() {
        if (GlobalClass.currecyPrefix.equalsIgnoreCase("CR")) {
            crImage.setImageDrawable(getResources().getDrawable(R.drawable.cr_orange_icon));
            thImage.setImageDrawable(getResources().getDrawable(R.drawable.th_icon));
            lakhImage.setImageDrawable(getResources().getDrawable(R.drawable.lakh));

        } else if (GlobalClass.currecyPrefix.equalsIgnoreCase("LH")) {
            crImage.setImageDrawable(getResources().getDrawable(R.drawable.cr_icon));
            thImage.setImageDrawable(getResources().getDrawable(R.drawable.th_icon));
            lakhImage.setImageDrawable(getResources().getDrawable(R.drawable.lakh_icon));

        } else if (GlobalClass.currecyPrefix.equalsIgnoreCase("TH")) {
            crImage.setImageDrawable(getResources().getDrawable(R.drawable.cr_icon));
            thImage.setImageDrawable(getResources().getDrawable(R.drawable.th));
            lakhImage.setImageDrawable(getResources().getDrawable(R.drawable.lakh));
        }
    }

    public void resetShortingImage() {
        if (GlobalClass.sortingOn <= -1) {
            top5Image.setImageDrawable(getResources().getDrawable(R.drawable.top_5_white));
            top10Image.setImageDrawable(getResources().getDrawable(R.drawable.top_10_white));
            top15Image.setImageDrawable(getResources().getDrawable(R.drawable.top_15_white));
            allImage.setImageDrawable(getResources().getDrawable(R.drawable.top_all));

        } else if (GlobalClass.sortingOn == 5) {
            top5Image.setImageDrawable(getResources().getDrawable(R.drawable.top_5));
            top10Image.setImageDrawable(getResources().getDrawable(R.drawable.top_10_white));
            top15Image.setImageDrawable(getResources().getDrawable(R.drawable.top_15_white));
            allImage.setImageDrawable(getResources().getDrawable(R.drawable.top_all_white));

        } else if (GlobalClass.sortingOn == 10) {
            top5Image.setImageDrawable(getResources().getDrawable(R.drawable.top_5_white));
            top10Image.setImageDrawable(getResources().getDrawable(R.drawable.top_10));
            top15Image.setImageDrawable(getResources().getDrawable(R.drawable.top_15_white));
            allImage.setImageDrawable(getResources().getDrawable(R.drawable.top_all_white));
        } else if (GlobalClass.sortingOn == 15) {
            top5Image.setImageDrawable(getResources().getDrawable(R.drawable.top_5_white));
            top10Image.setImageDrawable(getResources().getDrawable(R.drawable.top_10_white));
            top15Image.setImageDrawable(getResources().getDrawable(R.drawable.top_15));
            allImage.setImageDrawable(getResources().getDrawable(R.drawable.top_all_white));
        }
    }

//   // @OnClick({R.id.cr,R.id.lakh,R.id.th,R.id.top5,R.id.top10,R.id.top15,R.id.all})
//    @OnClick(R.id.th)
//    public void submit() {
//        Toast.makeText(getContext(),
//                "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                new ItemSortable("Cred.("+GlobalClass.currecyPrefix+")"),
                new ItemSortable("Rece.("+GlobalClass.currecyPrefix+")"),
                new ItemSortable("Util.("+GlobalClass.currecyPrefix+")"),
                new ItemSortable("Customer"),

        };
        return headers;
    }

    public List<NexusWithImage> getCustomerSixMBody() {
        List<Integer> resImages = Arrays.asList();

        List<NexusWithImage> items = new ArrayList<>();
        String type = "Total Sales";


        int count = 0;
        for (Map.Entry<String, JSONObject> set : customerFullList.entrySet()) {
            try {
                if (count < GlobalClass.sortingOn || GlobalClass.sortingOn == -1) {
                    Log.d(TAG, "Key >" + set.getKey());

                    String[] dataTable = new String[5];
                    JSONObject elementYearly;

                    float creditLimit = 0;
                    float receivable = 0;
                    float utilizePercent = 0;
                    int j = 0;
                    int yVal = set.getValue().getInt("creditLimit");
                    BigDecimal number1 = new BigDecimal(yVal);
                    creditLimit = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                    Log.d(TAG, "Customer Name >>" + set.getKey());
                    Log.d(TAG, "totalSales >>" + creditLimit);

                    int yValreceivable = set.getValue().getInt("receivable");
                    BigDecimal numberreceivable = new BigDecimal(yValreceivable);
                    receivable = numberreceivable.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);

                    int yValutilizePercent = set.getValue().getInt("creditUtilize");
                    BigDecimal numberutilizePercent = new BigDecimal(yValutilizePercent);
                    utilizePercent = numberutilizePercent.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);

                    String address = set.getValue().getJSONObject("customer").getString("address");
                   // customerDetailMap.put(dataTable[0],dataCustomer);
                    String creditLimitStr = formatter.format(Math.abs(creditLimit));
                    String receivableStr = formatter.format(Math.abs(receivable));
                    String utilizePercentStr = formatter.format(Math.abs(utilizePercent));
                    dataTable[j++] = String.valueOf(set.getKey());
//                    dataTable[j++] = Utility.decimal2Palce(Math.abs(creditLimit));
//                    dataTable[j++] = Utility.decimal2Palce(Math.abs(receivable));
//                    dataTable[j++] = Utility.decimal2Palce(Math.abs(utilizePercent));
                    dataTable[j++] = creditLimitStr;
                    dataTable[j++] = receivableStr;
                    dataTable[j++] = utilizePercentStr;
                    dataTable[j++] = address;
                    customerDetailMap.put(dataTable[0],set.getValue().getJSONObject("customer"));
                    items.add(new NexusWithImage(type, dataTable));
                    Log.d(TAG, "dataTable >" + dataTable.toString());
                    Log.d(TAG, "dataTable length >" + dataTable.length);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            count++;
        }

        return items;
    }


    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup> setClickListenerBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup>() {
        @Override
        public void onClickItem(NexusWithImage item, OriginalBodyCellViewGroup viewGroup, int row, int column) {

            //Snackbar.make(viewGroup, "Yes we do it " + item.data[0] + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();

            //Snackbar.make(viewGroup, "Yes we do it " + item.data[column + 1] + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();
//            Intent myIntent = new Intent(getActivity(), InvoiceDetailActivity.class);
////                Bundle bundle = new Bundle();
////                bundle.putString("StateName", stateName);
////                bundle.putString("BackFrgment", "TotalSale");
////                bundle.putString("CurrentFrgment", stateName);
////                bundle.putString("CurrentFrgmentMain", item.data[0]);
////                bundle.putString("CutomerName", item.data[0]);
////                bundle.putString("Title", "Invoices for "+item.data[0]);
////                myIntent.putExtras(bundle);
//            GlobalClass.currentFrgmentMain = item.data[0];
//            GlobalClass.cutomerName = item.data[0];
//            GlobalClass.title = "Invoices for " + item.data[0];
//            startActivity(myIntent);
        }
    };

    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup> setClickListenerFirstBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup>() {
        @Override
        public void onClickItem(NexusWithImage item, OriginalFirstBodyCellViewGroup viewGroup, int row, int column) {

            Snackbar snackbar = Snackbar.make(viewGroup, "Name: " + item.data[column + 1]+"\nAddress: "+item.data[4], Snackbar.LENGTH_LONG).setAction("Action", null);
            //  customerFullList.get(item.data[column + 1] );
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setMaxLines(5);
            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.chart4));
            sbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.chart4));
               // snackbar.show();
            JSONObject dataCustomer=customerDetailMap.get(item.data[column + 1]);
            System.out.println("dataCustomer>>>>>>>> >>>"+ dataCustomer);
            try {

                View view = getLayoutInflater().inflate(R.layout.cutomer_profile_detail, null, false);
                Button closeBtn = (Button) view.findViewById(R.id.closeWin);
                TextView nameText = (TextView) view.findViewById(R.id.nameText);
                nameText.setText(item.data[column + 1]);
                TextView addressText = (TextView) view.findViewById(R.id.addressText);
                addressText.setText( dataCustomer.getString("address"));
                TextView stateText = (TextView) view.findViewById(R.id.stateText);
                stateText.setText(dataCustomer.getString("state"));
                TextView pincodeText = (TextView) view.findViewById(R.id.pincodeText);
                pincodeText.setText(dataCustomer.getString("pincode"));
                TextView panText = (TextView) view.findViewById(R.id.panText);
                panText.setText(dataCustomer.getString("panapplicablefrom"));
                //TextView registrationTypeText = (TextView) view.findViewById(R.id.registrationTypeText);
                //registrationTypeText.setText(dataCustomer.getString("gstregistrationtype"));
                TextView gstText = (TextView) view.findViewById(R.id.gstText);
                gstText.setText(dataCustomer.getString("partygstin"));
                TextView creditPeroidText = (TextView) view.findViewById(R.id.creditPeroidText);
                String creditPeroidTxt=dataCustomer.getString("creditPeroid");
                creditPeroidText.setText(creditPeroidTxt!=null && !creditPeroidTxt.isEmpty()?creditPeroidTxt +"Days":"");
                //creditPeroidText.setText(dataCustomer.getString("creditPeroid"));
                TextView creditLimitText = (TextView) view.findViewById(R.id.creditLimitText);
                String creditLimitTxt=dataCustomer.getString("creditAmount");
                //String moneyString = formatter.format(creditLimitText);
                creditLimitText.setText(creditLimitTxt!=null && !creditLimitTxt.isEmpty()?formatter.format(Math.abs(Integer.parseInt(creditLimitTxt))):"");
                //creditLimitText.setText(dataCustomer.getString("creditAmount"));

                PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Finish the registration screen and return to the Login activity
                        popupWindow.dismiss();
                        //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

            //tabLayout.getTabAt(1).select();
//            setSalesData(1, fullSaleData);
//            chart.animateX(2000);
//            chart.refreshDrawableState();

        }
    };


}