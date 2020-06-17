package com.cbat.cbat.ui.services.recievable.details_receivable;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cbat.cbat.R;
import com.cbat.cbat.adapter.TableFixHeadersAdapterFactory;
import com.cbat.cbat.adapter.original_sortable.ItemSortable;
import com.cbat.cbat.adapter.original_sortable.NexusWithImage;
import com.cbat.cbat.adapter.original_sortable.OriginalSortableTableFixHeader;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.MyValueFormatter;
import com.cbat.cbat.util.PichartMarkerView;
import com.cbat.cbat.util.Utility;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.cbat.cbat.ui.navigation.ProductDetailActivity.TAG;

public class RecivableTop5DetailFragment extends Fragment {

    private ProgressDialog progressDialog;
    Map<String,Double> productSalesMap;
    Map<String, JSONArray> customerFullList;
    Map<String,JSONObject> customerAgingFullList;
    Map<String, JSONArray> productFullList;
    private LayoutInflater layoutInflater;
    private TableFixHeaders tableFixHeaders;
    private TableFixHeadersAdapterFactory tableFixHeadersAdapterFactory;
    private TableFixHeaders tableFixHeadersProduct;
    private TableFixHeadersAdapterFactory tableFixHeadersAdapterFactoryProduct;
    private TableFixHeaders tableFixHeadersAging;
    private TableFixHeadersAdapterFactory tableFixHeadersAdapterFactoryAging;
    LinearLayout totalSalesLayout;
    LinearLayout customerChartLayout;
    LinearLayout barchats;
    JSONObject fullAgainData;

    View chartLayout;
    private BarChart chart;
    protected Typeface tfLight;
    String stateName;
    Map<String,Double> cutomerSalesMap;
    JSONArray fullCustomerData;
    JSONArray fullproductData;
    TabLayout tabLayout;
    ImageView crImage;
    ImageView lakhImage;

    ImageView thImage;
    ImageView top5Image;
    ImageView top10Image;
    ImageView top15Image;
    ImageView allImage;
    public static RecivableTop5DetailFragment newInstance() {
        return new RecivableTop5DetailFragment();
    }
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
    //MPLine
    private LineChart lineChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.receivable_top_5_detail_fragment, container, false);
       // final ProgressBar progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        Log.d("URL :- ","Test");
        GlobalClass.sortingOn = -1;
        tfLight = Typeface.createFromAsset(getActivity().getAssets(), "arial.ttf");
        if(GlobalClass.customerTop5!=null && GlobalClass.customerTop5.size()>0) {
            customerFullList=GlobalClass.customerTop5;
            Log.d(TAG,"customerAgingFullList > "+customerFullList.size());
        }

        if(GlobalClass.productTop5!=null && GlobalClass.productTop5.size()>0) {
            productFullList=GlobalClass.productTop5;
            Log.d(TAG,"productFullList > "+productFullList.size());
        }

        if(GlobalClass.agingTop5!=null && GlobalClass.agingTop5.size()>0) {
            customerAgingFullList=GlobalClass.agingTop5;
            Log.d(TAG,"customerAgingFullList > "+customerAgingFullList.size());
        }
        //heighSetting( v );
        tableFixHeaders = (TableFixHeaders) v.findViewById(R.id.customerTable);
        // tableFixHeaders = new TableFixHeaders(getContext());
        tableFixHeadersAdapterFactory = new TableFixHeadersAdapterFactory(getContext());
        createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());

       // tableFixHeadersProduct = (TableFixHeaders) v.findViewById(R.id.productTable);
        // tableFixHeaders = new TableFixHeaders(getContext());
        tableFixHeadersAdapterFactoryProduct = new TableFixHeadersAdapterFactory(getContext());
        //createTableProduct(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBodyProduct());
        // final ProgressBar progressBar = (ProgressBar)v.findViewById(R.id.progressBar);

        tableFixHeadersAging = (TableFixHeaders) v.findViewById(R.id.agingTable);
        // tableFixHeaders = new TableFixHeaders(getContext());
        tableFixHeadersAdapterFactoryAging = new TableFixHeadersAdapterFactory(getContext());
        createTableAging(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBodyAging());

        // getTotalSaleCutomerWise();
        crImage = v.findViewById(R.id.cr);
        crImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.currecyPrefix = "CR";

                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
               // createTableProduct(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBodyProduct());
                createTableAging(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBodyAging());
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
              //  createTableProduct(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBodyProduct());
                createTableAging(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBodyAging());
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
               // createTableProduct(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBodyProduct());
                createTableAging(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBodyAging());
                resetCurrencyImage();
//                Toast.makeText(getContext(),
//
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
            }
        });
//        top5Image = v.findViewById(R.id.top5);
//        top5Image.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                GlobalClass.sortingOn = 5;
//                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
//                fullCustomerData = customerFullList.get(0);
//                //  barChartCreate(tabLayout);
//                resetShortingImage();
//               // tabLayout.getTabAt(1).select();
//
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
//            }
//        });
//        top10Image = v.findViewById(R.id.top10);
//        top10Image.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                GlobalClass.sortingOn = 10;
//                fullCustomerData = customerFullList.get(0);
//                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
//                // barChartCreate(tabLayout);
//                resetShortingImage();
//               // tabLayout.getTabAt(1).select();
//
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
//            }
//        });
//        top15Image = v.findViewById(R.id.top15);
//        top15Image.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                GlobalClass.sortingOn = 15;
//                fullCustomerData = customerFullList.get(0);
//                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
//                // barChartCreate(tabLayout);
//                resetShortingImage();
//               // tabLayout.getTabAt(1).select();
//                ;
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
//            }
//        });
//        allImage = v.findViewById(R.id.alltop);
//        allImage.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                GlobalClass.sortingOn = -1;
//                fullCustomerData = customerFullList.get(0);
//                createTable(TableFixHeadersAdapterFactory.ORIGINAL_SORTABLE, getCustomerSixMBody());
//                // barChartCreate(tabLayout);
//              //  resetShortingImage();
//                Toast.makeText(getContext(),
//                        "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
//            }
//        });
        resetCurrencyImage();
        //resetShortingImage();


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

//    public void resetShortingImage() {
//        if (GlobalClass.sortingOn <= -1) {
//            top5Image.setImageDrawable(getResources().getDrawable(R.drawable.top_5_white));
//            top10Image.setImageDrawable(getResources().getDrawable(R.drawable.top_10_white));
//            top15Image.setImageDrawable(getResources().getDrawable(R.drawable.top_15_white));
//            allImage.setImageDrawable(getResources().getDrawable(R.drawable.top_all));
//
//        } else if (GlobalClass.sortingOn == 5) {
//            top5Image.setImageDrawable(getResources().getDrawable(R.drawable.top_5));
//            top10Image.setImageDrawable(getResources().getDrawable(R.drawable.top_10_white));
//            top15Image.setImageDrawable(getResources().getDrawable(R.drawable.top_15_white));
//            allImage.setImageDrawable(getResources().getDrawable(R.drawable.top_all_white));
//
//        } else if (GlobalClass.sortingOn == 10) {
//            top5Image.setImageDrawable(getResources().getDrawable(R.drawable.top_5_white));
//            top10Image.setImageDrawable(getResources().getDrawable(R.drawable.top_10));
//            top15Image.setImageDrawable(getResources().getDrawable(R.drawable.top_15_white));
//            allImage.setImageDrawable(getResources().getDrawable(R.drawable.top_all_white));
//        } else if (GlobalClass.sortingOn == 15) {
//            top5Image.setImageDrawable(getResources().getDrawable(R.drawable.top_5_white));
//            top10Image.setImageDrawable(getResources().getDrawable(R.drawable.top_10_white));
//            top15Image.setImageDrawable(getResources().getDrawable(R.drawable.top_15));
//            allImage.setImageDrawable(getResources().getDrawable(R.drawable.top_all_white));
//        }
//    }

//   // @OnClick({R.id.cr,R.id.lakh,R.id.th,R.id.top5,R.id.top10,R.id.top15,R.id.all})
//    @OnClick(R.id.th)
//    public void submit() {
//        Toast.makeText(getContext(),
//                "Hello from Butterknife OnClick annotation", Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void createTable(int type, List<NexusWithImage> customerBody) {

        //BaseTableAdapter originalSortableTableFixHeader=tableFixHeadersAdapterFactory.getAdapter(type);
        OriginalSortableTableFixHeader originalSortableTableFixHeader = new OriginalSortableTableFixHeader(getContext());
        originalSortableTableFixHeader.setHeader(getCustomerHeader());
        originalSortableTableFixHeader.setBody(customerBody);
       // originalSortableTableFixHeader.setClickListenerBody(setClickListenerBody);
       // originalSortableTableFixHeader.setClickListenerFirstBody(setClickListenerFirstBody);
        tableFixHeaders.setAdapter(originalSortableTableFixHeader.getInstance());
        originalSortableTableFixHeader.getInstance().notifyDataSetChanged();


        //tableFixHeaders.setAdapter(tableFixHeadersAdapterFactory.getAdapter(type));
    }

    public List<NexusWithImage> getCustomerSixMBody() {
        List<Integer> resImages = Arrays.asList();

        List<NexusWithImage> items = new ArrayList<>();
        String type = "Total Sales";
        // items.add(new NexusWithImage(type, resImages));
        //GlobalClass.salesStateCustomer.clear();
//        if(GlobalClass.customerFullList!=null && !GlobalClass.customerFullList.isEmpty()) {
//            customerFullList = GlobalClass.customerFullList;
//        }


        int count = 0;
        for (Map.Entry<String, JSONArray> set : customerFullList.entrySet()) {
            try {
                if (count < GlobalClass.sortingOn || GlobalClass.sortingOn == -1) {
                    Log.d(TAG, "Key >" + set.getKey());
                    if (!set.getKey().equalsIgnoreCase("totalStateSales")) {
                        JSONArray dataMonthly = set.getValue().getJSONArray(0);
                       // JSONArray dataYearly = set.getValue().getJSONArray(3);
                        JSONArray contriData = set.getValue().getJSONArray(1);

                        String[] dataTable = new String[dataMonthly.length() + 4];
                        JSONObject elementYearly;
                        boolean yearFlag = true;
                        boolean contriFlag = true;
                        float totalSales = 0;
                        int j = 0;
                        String moneyString="";

                        for (int i = 0; i < dataMonthly.length(); i++) {
//                            if (yearFlag) {
//                                elementYearly = dataYearly.getJSONObject(i);
//                                yearFlag = false;
//                                int yVal = elementYearly.getInt("value");
//                                BigDecimal number1 = new BigDecimal(yVal);
//                                //totalSales = number1.floatValue() / 10000000;
//                                totalSales = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
//                                Log.d(TAG, "set.getKey() >" + set.getKey());
//                                Log.d(TAG, "totalSales >" + totalSales);
//                                dataTable[j++] = String.valueOf(set.getKey());
//                                dataTable[j++] = Utility.decimal2Palce(Math.abs(totalSales));
//                                GlobalClass.salesStateCustomer.add(set.getKey());
//                            }
                            if (contriFlag) {
                                elementYearly = contriData.getJSONObject(i);
                                contriFlag = false;
                                double yVal = elementYearly.getDouble("value");
                                //BigDecimal number1 = new BigDecimal(yVal);
                                //totalSales = number1.floatValue() / 10000000;
                                //totalSales = number1.floatValue(); // GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                double yValT = elementYearly.getDouble("sales");
                                BigDecimal number1 = new BigDecimal(yValT);
                                //totalSales = number1.floatValue() / 10000000;
                                totalSales = number1.floatValue(); // GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                Log.d(TAG, "set.getKey() >" + set.getKey());
                                Log.d(TAG, "yVal >" + yVal);
                                dataTable[j++] = String.valueOf(set.getKey());
                                moneyString = formatter.format(Math.abs(totalSales));
                                 dataTable[j++] =moneyString; // Utility.decimal2Palce(Math.abs(totalSales));
                                moneyString = formatter.format(Math.abs(yVal));
                                dataTable[j++] = moneyString; //Utility.decimal2Palce(Math.abs(yVal));
                            }

                            JSONObject element = dataMonthly.getJSONObject(i);
                            int yVal = element.getInt("value");
                            BigDecimal number1 = new BigDecimal(yVal);
                            // float debitFloat = number1.floatValue() / 10000000;
                            float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            moneyString = formatter.format(Math.abs(debitFloat));
                            dataTable[j++] = moneyString;
                                   // Utility.decimal2Palce(Math.abs(debitFloat));

                        }

                        items.add(new NexusWithImage(type, dataTable));
                        Log.d(TAG, "dataTable >" + dataTable.toString());
                        Log.d(TAG, "dataTable length >" + dataTable.length);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            count++;
        }
        if (GlobalClass.sortingOn == -1) {
            try {
                if (customerFullList != null && !customerFullList.isEmpty() && customerFullList.containsKey("totalStateSales")) {
                    JSONArray setVal = customerFullList.get("totalStateSales");
                    JSONArray dataMonthly = setVal.getJSONArray(1);
                    JSONArray dataYearly = setVal.getJSONArray(3);
                    String[] dataTable = new String[dataMonthly.length() + 4];
                    JSONObject elementYearly;
                    boolean yearFlag = true;
                    float totalSales = 0;
                    int j = 0;
                    String moneyString="";
                    for (int i = 0; i < dataMonthly.length(); i++) {
                        if (yearFlag) {
                            elementYearly = dataYearly.getJSONObject(i);
                            yearFlag = false;
                            int yVal = elementYearly.getInt("value");
                            BigDecimal number1 = new BigDecimal(yVal);
                            //totalSales = number1.floatValue() / 10000000;
                            totalSales = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            //Log.d(TAG, "set.getKey() >" + totalStateSales);
                            Log.d(TAG, "totalSales >" + totalSales);
                            dataTable[j++] = "Total Sales";
                            dataTable[j++] = Utility.decimal2Palce(Math.abs(totalSales));
                            dataTable[j++] = "100";
                        }

                        JSONObject element = dataMonthly.getJSONObject(i);
                        int yVal = element.getInt("value");
                        BigDecimal number1 = new BigDecimal(yVal);
                        // float debitFloat = number1.floatValue() / 10000000;
                        float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                        Log.d(TAG, "debitFloat >" + debitFloat);
                        String money=Utility.decimal2Palce(Math.abs(debitFloat));
                        moneyString = formatter.format(Math.abs(Integer.parseInt(money)));
                        dataTable[j++] = moneyString;

                    }
                    items.add(new NexusWithImage(type, dataTable));
                    Log.d(TAG, "dataTable >" + dataTable.toString());
                    Log.d(TAG, "dataTable length >" + dataTable.length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // }
        return items;
    }

    public ItemSortable[] getCustomerHeader() {
        ItemSortable headers[] = {
                new ItemSortable("Total"),
                new ItemSortable("% Contri."),
                new ItemSortable("Apr"),
                new ItemSortable("May"),
                new ItemSortable("Jun"),
                new ItemSortable("Jul"),
                new ItemSortable("Aug"),
                new ItemSortable("Sep"),
                new ItemSortable("Oct"),
                new ItemSortable("Nov"),
                new ItemSortable("Dec"),
                new ItemSortable("Jan"),
                new ItemSortable("Feb"),
                new ItemSortable("Mar"),
                new ItemSortable("Customer"),

        };
        return headers;
    }

    private void createTableProduct(int type, List<NexusWithImage> customerBody) {

        //BaseTableAdapter originalSortableTableFixHeader=tableFixHeadersAdapterFactory.getAdapter(type);
        OriginalSortableTableFixHeader originalSortableTableFixHeader = new OriginalSortableTableFixHeader(getContext());
        originalSortableTableFixHeader.setHeader(getCustomerHeaderProduct());
        originalSortableTableFixHeader.setBody(customerBody);
        // originalSortableTableFixHeader.setClickListenerBody(setClickListenerBody);
        // originalSortableTableFixHeader.setClickListenerFirstBody(setClickListenerFirstBody);
        tableFixHeadersProduct.setAdapter(originalSortableTableFixHeader.getInstance());
        originalSortableTableFixHeader.getInstance().notifyDataSetChanged();


        //tableFixHeaders.setAdapter(tableFixHeadersAdapterFactory.getAdapter(type));
    }

    public ItemSortable[] getCustomerHeaderProduct() {
        ItemSortable headers[] = {
                new ItemSortable("Total"),
                new ItemSortable("% Contri."),
                new ItemSortable("Apr"),
                new ItemSortable("May"),
                new ItemSortable("Jun"),
                new ItemSortable("Jul"),
                new ItemSortable("Aug"),
                new ItemSortable("Sep"),
                new ItemSortable("Oct"),
                new ItemSortable("Nov"),
                new ItemSortable("Dec"),
                new ItemSortable("Jan"),
                new ItemSortable("Feb"),
                new ItemSortable("Mar"),
                new ItemSortable("Product"),

        };
        return headers;
    }

    public List<NexusWithImage> getCustomerSixMBodyProduct() {
        List<Integer> resImages = Arrays.asList();

        List<NexusWithImage> items = new ArrayList<>();
        String type = "Total Sales";

        int count = 0;
        for (Map.Entry<String, JSONArray> set : productFullList.entrySet()) {
            try {
                if (count < GlobalClass.sortingOn || GlobalClass.sortingOn == -1) {
                    Log.d(TAG, "Key >" + set.getKey());
                    if (!set.getKey().equalsIgnoreCase("totalStateSales")) {
                        JSONArray dataMonthly = set.getValue().getJSONArray(0);
                        // JSONArray dataYearly = set.getValue().getJSONArray(3);
                        JSONArray contriData = set.getValue().getJSONArray(1);

                        String[] dataTable = new String[dataMonthly.length() + 4];
                        JSONObject elementYearly;
                        boolean yearFlag = true;
                        boolean contriFlag = true;
                        float totalSales = 0;
                        int j = 0;

                        for (int i = 0; i < dataMonthly.length(); i++) {
//                            if (yearFlag) {
//                                elementYearly = dataYearly.getJSONObject(i);
//                                yearFlag = false;
//                                int yVal = elementYearly.getInt("value");
//                                BigDecimal number1 = new BigDecimal(yVal);
//                                //totalSales = number1.floatValue() / 10000000;
//                                totalSales = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
//                                Log.d(TAG, "set.getKey() >" + set.getKey());
//                                Log.d(TAG, "totalSales >" + totalSales);
//                                dataTable[j++] = String.valueOf(set.getKey());
//                                dataTable[j++] = Utility.decimal2Palce(Math.abs(totalSales));
//                                GlobalClass.salesStateCustomer.add(set.getKey());
//                            }
                            if (contriFlag) {
                                elementYearly = contriData.getJSONObject(i);
                                contriFlag = false;
                                double yVal = elementYearly.getDouble("value");
                                //BigDecimal number1 = new BigDecimal(yVal);
                                //totalSales = number1.floatValue() / 10000000;
                                //totalSales = number1.floatValue(); // GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                double yValT = elementYearly.getDouble("sales");
                                BigDecimal number1 = new BigDecimal(yValT);
                                //totalSales = number1.floatValue() / 10000000;
                                totalSales = number1.floatValue(); // GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                Log.d(TAG, "set.getKey() >" + set.getKey());
                                Log.d(TAG, "yVal >" + yVal);
                                dataTable[j++] = String.valueOf(set.getKey());
                                dataTable[j++] = Utility.decimal2Palce(Math.abs(totalSales));
                                dataTable[j++] = Utility.decimal2Palce(Math.abs(yVal));
                            }

                            JSONObject element = dataMonthly.getJSONObject(i);
                            int yVal = element.getInt("value");
                            BigDecimal number1 = new BigDecimal(yVal);
                            // float debitFloat = number1.floatValue() / 10000000;
                            float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            dataTable[j++] = Utility.decimal2Palce(Math.abs(debitFloat));

                        }

                        items.add(new NexusWithImage(type, dataTable));
                        Log.d(TAG, "dataTable >" + dataTable.toString());
                        Log.d(TAG, "dataTable length >" + dataTable.length);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            count++;
        }

        // }
        return items;
    }

    private void createTableAging(int type, List<NexusWithImage> customerBody) {

        //BaseTableAdapter originalSortableTableFixHeader=tableFixHeadersAdapterFactory.getAdapter(type);
        OriginalSortableTableFixHeader originalSortableTableFixHeader = new OriginalSortableTableFixHeader(getContext());
        originalSortableTableFixHeader.setHeader(getCustomerHeaderAging());
        originalSortableTableFixHeader.setBody(customerBody);
      //  originalSortableTableFixHeader.setClickListenerBody(setClickListenerBodyAging);
      //  originalSortableTableFixHeader.setClickListenerFirstBody(setClickListenerFirstBody);
        tableFixHeadersAging.setAdapter(originalSortableTableFixHeader.getInstance());
        originalSortableTableFixHeader.getInstance().notifyDataSetChanged();


        //tableFixHeaders.setAdapter(tableFixHeadersAdapterFactory.getAdapter(type));
    }

    public ItemSortable[] getCustomerHeaderAging() {
        ItemSortable headers[] = {
                new ItemSortable("Receivable"),
                new ItemSortable("Overdue"),
                new ItemSortable("No Due"),
                new ItemSortable("0-15"),
                new ItemSortable("15-30"),
                new ItemSortable("30-60"),
                new ItemSortable("60-90"),
                new ItemSortable(">90"),
                new ItemSortable("Customer"),

        };
        return headers;
    }

    public List<NexusWithImage> getCustomerSixMBodyAging() {
        List<Integer> resImages = Arrays.asList();

        List<NexusWithImage> items = new ArrayList<>();
        String type = "Total Sales";
        // items.add(new NexusWithImage(type, resImages));
        // GlobalClass.salesStateCustomer.clear();
//        if(GlobalClass.customerFullList!=null && !GlobalClass.customerFullList.isEmpty()) {
//            customerFullList = GlobalClass.customerFullList;
//        }


        int count = 0;
        String moneyString="";
        Log.d(TAG, "customerAgingFullList size >" + customerAgingFullList.size());
        if(customerAgingFullList!=null && !customerAgingFullList.isEmpty() ) {
            for (Map.Entry<String, JSONObject> set : customerAgingFullList.entrySet()) {
                try {
                    if (count < GlobalClass.sortingOn || GlobalClass.sortingOn == -1) {
                        Log.d(TAG, "Key >" + set.getKey());
                        if (!set.getKey().equalsIgnoreCase("totalStateSales")) {
                            JSONObject fullAgaingData = set.getValue();

                            String[] dataTable = new String[9];
                            int j = 0;

                            double receivable = fullAgaingData.getDouble("receivable");
                            BigDecimal receivablenumber = new BigDecimal(receivable);
                            float receivableFinal = receivablenumber.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            dataTable[j++] = String.valueOf(set.getKey());
                            moneyString = formatter.format(Math.abs(receivableFinal));
                            // dataTable[j++] = String.valueOf("Receivable");
                            dataTable[j++] = moneyString;
                            //Utility.decimal2Palce(Math.abs(receivableFinal));

                            double overdue = fullAgaingData.getDouble("overdue");
                            BigDecimal number = new BigDecimal(overdue);
                            float overdueFinal = number.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            moneyString = formatter.format(Math.abs(overdueFinal));
                            dataTable[j++] = moneyString;
                            //Utility.decimal2Palce(Math.abs(overdueFinal));

                            double noDue = fullAgaingData.getDouble("noDue");
                            BigDecimal number1 = new BigDecimal(noDue);
                            float noDue_Final = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            moneyString = formatter.format(Math.abs(noDue_Final));
                            dataTable[j++] = moneyString;
                         //   dataTable[j++] = Utility.decimal2Palce(Math.abs(noDue_Final));


                            double day15 = fullAgaingData.getDouble("day15");
                            BigDecimal number2 = new BigDecimal(day15);
                            float day15Final = number2.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            moneyString = formatter.format(Math.abs(day15Final));
                            dataTable[j++] = moneyString;
                      //      dataTable[j++] = Utility.decimal2Palce(Math.abs(day15Final));

                            double day30 = fullAgaingData.getDouble("day30");
                            BigDecimal number3 = new BigDecimal(day30);
                            float day30Final = number3.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            moneyString = formatter.format(Math.abs(day30Final));
                            dataTable[j++] = moneyString;
                            //     dataTable[j++] = Utility.decimal2Palce(Math.abs(day30Final));

                            double day60 = fullAgaingData.getDouble("day60");
                            BigDecimal number4 = new BigDecimal(day60);
                            float day60Final = number4.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            moneyString = formatter.format(Math.abs(day60Final));
                            dataTable[j++] = moneyString;
                            //  dataTable[j++] = Utility.decimal2Palce(Math.abs(day60Final));


                            double day90 = fullAgaingData.getDouble("day90");
                            BigDecimal number5 = new BigDecimal(day90);
                            float day90Final = number5.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            moneyString = formatter.format(Math.abs(day90Final));
                            dataTable[j++] = moneyString;
                            // dataTable[j++] = Utility.decimal2Palce(Math.abs(day90Final));

                            double dayGreater90 = fullAgaingData.getDouble("dayGreater90");
                            BigDecimal number6 = new BigDecimal(dayGreater90);
                            float dayGreater90Final = number6.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            moneyString = formatter.format(Math.abs(dayGreater90Final));
                            dataTable[j++] = moneyString;
                            //    dataTable[j++] = Utility.decimal2Palce(Math.abs(dayGreater90Final));


                            items.add(new NexusWithImage(type, dataTable));
                            Log.d(TAG, "dataTable >" + dataTable.toString());
                            Log.d(TAG, "dataTable length >" + dataTable.length);

                            if(!set.getKey().equalsIgnoreCase("Total Receivable")) {
                                GlobalClass.salesStateCustomer.add(set.getKey());
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                count++;
            }
        }
//        if (GlobalClass.sortingOn == -1) {
//            try {
//                if (customerFullList != null && !customerFullList.isEmpty() && customerFullList.containsKey("totalStateSales")) {
//                    JSONArray setVal = customerFullList.get("totalStateSales");
//                    JSONArray dataMonthly = setVal.getJSONArray(1);
//                    JSONArray dataYearly = setVal.getJSONArray(3);
//                    String[] dataTable = new String[dataMonthly.length() + 3];
//                    JSONObject elementYearly;
//                    boolean yearFlag = true;
//                    float totalSales = 0;
//                    int j = 0;
//                    for (int i = 0; i < dataMonthly.length(); i++) {
//                        if (yearFlag) {
//                            elementYearly = dataYearly.getJSONObject(i);
//                            yearFlag = false;
//                            int yVal = elementYearly.getInt("value");
//                            BigDecimal number1 = new BigDecimal(yVal);
//                            //totalSales = number1.floatValue() / 10000000;
//                            totalSales = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
//                            //Log.d(TAG, "set.getKey() >" + totalStateSales);
//                            Log.d(TAG, "totalSales >" + totalSales);
//                            dataTable[j++] = String.valueOf("Total Sales");
//                            dataTable[j++] = String.valueOf(Math.abs(totalSales));
//                            dataTable[j++] = String.valueOf("100");
//                        }
//
//                        JSONObject element = dataMonthly.getJSONObject(i);
//                        int yVal = element.getInt("value");
//                        BigDecimal number1 = new BigDecimal(yVal);
//                        // float debitFloat = number1.floatValue() / 10000000;
//                        float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
//                        Log.d(TAG, "debitFloat >" + debitFloat);
//                        dataTable[j++] = String.valueOf(Math.abs(debitFloat));
//
//                    }
//                    items.add(new NexusWithImage(type, dataTable));
//                    Log.d(TAG, "dataTable >" + dataTable.toString());
//                    Log.d(TAG, "dataTable length >" + dataTable.length);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        // }
        return items;
    }



}