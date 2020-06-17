package com.cbat.cbat.ui.services.recievable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.agrawalsuneet.svgloaderspack.loaders.SVGLoader;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cbat.cbat.R;
import com.cbat.cbat.adapter.TableFixHeaderAdapter;
import com.cbat.cbat.adapter.TableFixHeadersAdapterFactory;
import com.cbat.cbat.adapter.TestViewHolder;
import com.cbat.cbat.adapter.original_sortable.ItemSortable;
import com.cbat.cbat.adapter.original_sortable.NexusWithImage;
import com.cbat.cbat.adapter.original_sortable.OriginalBodyCellViewGroup;
import com.cbat.cbat.adapter.original_sortable.OriginalFirstBodyCellViewGroup;
import com.cbat.cbat.adapter.original_sortable.OriginalSortableTableFixHeader;
import com.cbat.cbat.ui.services.ManagmentFragment;
import com.cbat.cbat.ui.services.recievable.details_receivable.CustomerDetailActivity_R;
import com.cbat.cbat.util.Constants;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.TooltipWindow;
import com.cbat.cbat.util.Utility;
import com.github.florent37.materialleanback.MaterialLeanBack;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.snackbar.Snackbar;
import com.inqbarna.tablefixheaders.TableFixHeaders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.androtaku.geomap.GeoMapView;
import jp.gr.java_conf.androtaku.geomap.OnInitializedListener;

public class TotalRecivableFragment extends Fragment {
    private TableFixHeaders tableFixHeaders;
    private TableFixHeadersAdapterFactory tableFixHeadersAdapterFactory;
    GeoMapView geoMapView;
    ArrayList mapSlectedState = new ArrayList();
    Map<String, Integer> salesMap = new HashMap<>();
    Map<String, Integer> finalMap = new HashMap<>();
    LinearLayout totalSalesLayout;
    SVGLoader svgLoader;
    private ProgressDialog progressDialog;
    String[] hexColors;
    // ColorSlider colorSlider;
    LinearLayout stateList;
    TooltipWindow tipWindow;
    Animation animZoomOut;
    MaterialLeanBack materialLeanBack;
    private static final String TAG = "TotalRecivableFragment";
    LinearLayout mapLegend;
    LinearLayout stateSales;

    public static TotalRecivableFragment newInstance() {
        return new TotalRecivableFragment();
    }

    //MPLine
    private LineChart lineChart;
Map<String,String> stateMapCode=new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.services_sales_totalsales_fragment, container, false);
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
       // materialLeanBack=v.findViewById(R.id.materialLeanBack);
        GlobalClass.apiKey="Sales";
        final FrameLayout totalSalesContainer = (FrameLayout) v.findViewById(R.id.totalSalesContainer);
        totalSalesLayout = (LinearLayout) v.findViewById(R.id.totalSalesLayout);
        mapLegend=v.findViewById(R.id.mapLegend);
       stateSales=v.findViewById(R.id.casts_container);

        geoMapView = (GeoMapView) v.findViewById(R.id.totalSalesGeoMap);
   //     getTotalSaleStateWise(geoMapView);
        if(GlobalClass.receivableMap != null && !GlobalClass.receivableMap.isEmpty()) {
            loadpage(geoMapView,GlobalClass.receivableMap);
        }else{
            getTotalSaleStateWise(geoMapView);
        }
        Button btn10to0=v.findViewById(R.id.btn10to0);
        Button btn20to10=v.findViewById(R.id.btn20to10);
        Button btn30to20=v.findViewById(R.id.btn30to20);
        Button btn50to30=v.findViewById(R.id.btn50to30);
        Button btn100to50=v.findViewById(R.id.btn100to50);

        btn30to20.setBackground(getResources().getDrawable(R.drawable.receivable_30_20));
        btn50to30.setBackground(getResources().getDrawable(R.drawable.receivable_50_30));
        btn100to50.setBackground(getResources().getDrawable(R.drawable.receivable_100_50));

        btn10to0.setText(getResources().getString(R.string.receivable10_0));
        btn20to10.setText(getResources().getString(R.string.receivable20_10));
        btn30to20.setText(getResources().getString(R.string.receivable30_20));
        btn50to30.setText(getResources().getString(R.string.receivable50_30));
        btn100to50.setText(getResources().getString(R.string.receivableGreater50));
//        mapLegend.removeAllViews();
//        int count=0;
//        for (float val:GlobalClass.receivableColor) {
//            LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View contentView = inflater1.inflate(R.layout.legend_view, null, false);
//            TextView tv= contentView.findViewById(R.id.colorText);
//            LinearLayout ll=contentView.findViewById(R.id.textLayout);
//            // mapLegend.addView(contentView, 0);
//            //dots[count] = new TextView(getContext());
//            String text=String.valueOf((int)val);
//            tv.setText(text);
//            //dots[count].setTextSize(15);
//            ll.setBackgroundColor(Color.parseColor(Utility.getReceivableColour((int)val)));
//            mapLegend.addView(contentView, 0);
//            count++;
//        }

        return v;
    }


    private void createTable(int type) {

        // BaseTableAdapter originalSortableTableFixHeader=tableFixHeadersAdapterFactory.getAdapter(type);
        OriginalSortableTableFixHeader originalSortableTableFixHeader = new OriginalSortableTableFixHeader(getContext());
        originalSortableTableFixHeader.setHeader(getCustomerHeader());
        originalSortableTableFixHeader.setBody(getCustomerBody());
        originalSortableTableFixHeader.setClickListenerBody(setClickListenerBody);
        originalSortableTableFixHeader.setClickListenerFirstBody(setClickListenerFirstBody);
        tableFixHeaders.setAdapter(originalSortableTableFixHeader.getInstance());
        //tableFixHeaders.setAdapter(tableFixHeadersAdapterFactory.getAdapter(type));
    }

    public ItemSortable[] getCustomerHeader() {
        ItemSortable headers[] = {
                new ItemSortable("Total Sales"),
                new ItemSortable("Total Product"),
                new ItemSortable("Customer Name")
        };
        return headers;
    }

    public List<NexusWithImage> getCustomerBody() {
        List<Integer> resImages = Arrays.asList();

        List<NexusWithImage> items = new ArrayList<>();
        String type = "Total Sales";

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
        }
    };

    private void loadFragment(String stateName) {
        Log.d("stae!!",stateName );
        GlobalClass.customerReceivableFullList=null;
        Intent myIntent = new Intent(getContext(), CustomerDetailActivity_R.class);
        Bundle bundle = new Bundle();
        GlobalClass.stateName = stateName;
        // GlobalClass.backFrgment="TotalSale";
        GlobalClass.currentFrgment = Utility.getStateCode(stateName);
        GlobalClass.currentFrgmentMain = "CustomerList";
        GlobalClass.title = "Customer Wise " + GlobalClass.backFrgment + " in " + Utility.getStateCode(stateName);
Log.d(TAG,"Start Activity >> CustomerDetailActivity_R");
        startActivity(myIntent);

    }

    private void getTotalSaleStateWise(GeoMapView geoMapView) {
        //svgLoader.startAnimation();

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();
        String url = Constants.BASE_LOCAL__URL + "getReceivableListByDate/state/"+GlobalClass.startDate+"/"+GlobalClass.endDate;
        JSONObject paramsJsonObj = new JSONObject();
        try {

            paramsJsonObj.put("id", GlobalClass.comapnyId);


            Log.d("URL :- ", url);
            Log.d("paramsJsonObj :- ", paramsJsonObj.toString());
            JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, url, paramsJsonObj, this.createRequestSuccessListener(0, geoMapView), this.createRequestErrorListener(System.currentTimeMillis()));
            jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT_TIME,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Log.d("No resul :- ", paramsJsonObj.toString());
            Volley.newRequestQueue(getContext()).add(jsonObjRequest);
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Adding request to request queue

        // Cancelling request
//        AppController.getInstance().getRequestQueue().cancelAll(TAG);
    }


    private Response.Listener<JSONObject> createRequestSuccessListener(final int responseCode, final GeoMapView geoMapView) {
        Response.Listener<JSONObject> listenerObj = null;
        //    if (requestCode == REQUEST_LOGIN) {

        listenerObj = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (responseCode == 0) {
                        Log.d("Res", response.toString());
//                        if (progressDialog != null) {
//                            progressDialog.dismiss();
//                        }
                        if (response.getString("code").equals(Integer.toString(206))) {

                            // GlobalClass.lastName = response.optString("userrole");
                            //Save to prefrence
                            JSONArray contentList = response.getJSONArray("contentList");

                            GlobalClass.totalSales= new HashMap<>();
                            for (int i = 0; i < contentList.length(); i++) {
                                JSONObject element = (JSONObject) contentList.get(i);
                                salesMap.put(element.getString("name"), (Math.abs(element.getInt("value"))));
                                GlobalClass.totalSales.put(element.getString("name"),element);
                            }
GlobalClass.receivableMap=salesMap;
                           stateSales.removeAllViews();
                            int count=0;
                            for (Map.Entry<String, Integer> val:salesMap.entrySet()) {
                                LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View contentView = inflater1.inflate(R.layout.cell_test, null, false);
                                TextView tv= contentView.findViewById(R.id.saleAmount);
                                TextView tvState= contentView.findViewById(R.id.textView);
                                RelativeLayout frame=contentView.findViewById(R.id.cardFrame);
                                LinearLayout ll=contentView.findViewById(R.id.textLayout);
                                // mapLegend.addView(contentView, 0);
                                //dots[count] = new TextView(getContext());
                                String text=String.valueOf(val.getValue())+" %";
                                tv.setText(text);
                                tvState.setText(val.getKey());
                                //dots[count].setTextSize(15);
                                frame.setBackgroundColor(Color.parseColor(Utility.getReceivableColour(val.getValue())));

                                View contentV = inflater1.inflate(R.layout.horizontal_divider, null, false);

                                stateSales.addView(contentView, 0);
                                stateSales.addView(contentV, 0);

                                contentView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        TextView cast = (TextView) v.findViewById(R.id.saleAmount);
                                        TextView castTextView = (TextView) v.findViewById(R.id.textView);
                                        // Toast.makeText(getContext(), castTextView.getText()+" >> "+Utility.getStateName(castTextView.getText().toString()), Toast.LENGTH_SHORT).show();
                                        loadFragment(Utility.getStateName(castTextView.getText().toString()));
//Click able view for casts
                                    }
                                });
                                count++;
                            }
                            loadpage(geoMapView,salesMap);
//                            geoMapView.setOnInitializedListener(new OnInitializedListener() {
//                                @RequiresApi(api = Build.VERSION_CODES.O)
//                                @Override
//                                public void onInitialized(GeoMapView geoMapView) {
////                                    svgLoader.setVisibility(View.INVISIBLE);
////                                    svgLoader.endAnimation();
//                                    if (progressDialog != null) {
//                                        progressDialog.dismiss();
//                                    }
//                                    GlobalClass.salesState.clear();
//                                    hexColors = new String[salesMap.size()];
//                                    int i = 0;
//
//                                    for (Map.Entry<String, Integer> set : salesMap.entrySet()) {
//                                        // View stateView = getLayoutInflater().inflate(R.layout.state_list, null);
//                                        String stateCode = Utility.getStateCode(set.getKey());
//                                        // GlobalClass.salesState.add(set.getKey());
//                                        GlobalClass.salesState.add(stateCode);
//                                        mapSlectedState.add(stateCode);
//                                        finalMap.put(stateCode,set.getValue());
//                                        geoMapView.setCountryColor(stateCode, Utility.getReceivableColour(set.getValue()), String.valueOf(set.getValue()) + " Cr");
//                                        hexColors[i] = Utility.getReceivableColour(set.getValue());
//                                        i++;
//                                    }
//                                    materialLeanBack.setAdapter(new MaterialLeanBack.Adapter<TestViewHolder>() {
//                                        @Override
//                                        public int getLineCount() {
//                                            return 1;
//                                        }
//
//                                        @Override
//                                        public int getCellsCount(int line) {
//                                            return 4;
//                                        }
//
//                                        @Override
//                                        public TestViewHolder onCreateViewHolder(ViewGroup viewGroup, int line) {
//                                            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_test, viewGroup, false);
//                                            return new TestViewHolder(view);
//                                        }
//
//                                        @Override
//                                        public void onBindViewHolder(TestViewHolder viewHolder, int i) {
//                                            viewHolder.textView.setText(((String)mapSlectedState.get(i)));
//                                            viewHolder.saleAmount.setText(String.valueOf((Integer) finalMap.get((String)mapSlectedState.get(i)))+" Cr");
//                                            viewHolder.cardFrame.setBackgroundColor(Color.parseColor(hexColors[i]));
//                                            //String url = "http://www.lorempixel.com/40" + viewHolder.row + "/40" + viewHolder.cell + "/";
//                                            //  Picasso.with(viewHolder.imageView.getContext()).load(url).into(viewHolder.imageView);
//                                        }
//
//                                        @Override
//                                        public String getTitleForRow(int row) {
//                                            return "Line " + row;
//                                        }
//
//                                        @Override
//                                        public boolean hasRowTitle(int row) {
//                                            return row != 6;
//                                        }
//
//
//                                        //region customView
//                                        @Override
//                                        public RecyclerView.ViewHolder getCustomViewForRow(ViewGroup viewgroup, int row) {
////                                                if (row == 3) {
////                                                   // View view = LayoutInflater.from(viewgroup.getContext()).inflate(R.layout.header, viewgroup, false);
////                                                    return new RecyclerView.ViewHolder(view) {
////                                                    };
////                                                } else
////                                                    return null;
//                                            return null;
//                                        }
//
//                                        @Override
//                                        public boolean isCustomView(int row) {
//                                            return row == 3;
//                                        }
//
//                                        @Override
//                                        public void onBindCustomView(RecyclerView.ViewHolder viewHolder, int row) {
//                                            super.onBindCustomView(viewHolder, row);
//                                        }
//
////                                            @Override
////                                            public int getEnlargedItemPosition(int position) {
////
////                                                Toast.makeText(getActivity(),String.valueOf(position),Toast.LENGTH_LONG).show();
////
////                                                return super.getEnlargedItemPosition(position);
////                                            }
//
//                                        //endregion
//
//                                    });
//
//                                    materialLeanBack.setOnItemClickListener(new MaterialLeanBack.OnItemClickListener() {
//
//                                        @Override
//                                        public void onTitleClicked(int row, String text) {
//                                            Toast.makeText(getActivity(), "onTitleClicked " + row + " " + text, Toast.LENGTH_SHORT).show();
//                                        }
//
//                                        @Override
//                                        public void onItemClicked(int row, int column) {
//                                            Log.d(TAG,"column > "+column+" row >"+row);
//                                            Log.d(TAG,"mapSlectedState > "+mapSlectedState.size());
//                                            Log.d(TAG,"mapSlectedState.get(column) > "+mapSlectedState.get(column-1));
//
//                                            //Toast.makeText(getActivity(), "onItemClicked " + row + " " + column, Toast.LENGTH_SHORT).show();
//                                            loadFragment(Utility.getStateName((String)mapSlectedState.get(column-1)));
//                                        }
//                                    });
//                                    //  colorSlider.setHexColors( hexColors);
//
//
//                                    geoMapView.refresh();
//                                }
//                            });
//
//                            geoMapView.setOnTouchListener(new View.OnTouchListener() {
//                                @Override
//                                public boolean onTouch(View v, MotionEvent event) {
//
//                                    // have same code as onTouchEvent() (for the Activity) above
//
//                                    switch (event.getAction()) {
//                                        case MotionEvent.ACTION_DOWN:
//
//                                            //screen touch get x of the touch event
//                                            float x = event.getX();
//                                            //screen touch get y of the touch event
//                                            float y = event.getY();
//
//                                            //check if touch point intersects the path bounds
//                                            for (Path p : geoMapView.getPaths()) {
//                                                RectF pBounds = new RectF();
//                                                p.computeBounds(pBounds, true);
//                                                Log.d("touched", "  pBounds.centerX : " + pBounds.centerX() + "centerY :  " + pBounds.centerY());
//
//                                                if (pBounds.contains(x, y)) {
//                                                    //select path
//                                                    Path selectedPath = p;// where selectedPath is assumed declared.
//                                                    Log.d("touched", " isConvex : " + selectedPath.isConvex());
//                                                    String stateCode = geoMapView.getPathsMap().get(selectedPath);
//                                                    Log.d("touched", "X : " + x + " Y : " + y);
//                                                    Log.d("stateCode##", stateCode);
//                                                    // totalSalesLayout.removeAllViews();
//                                                    // totalSalesLayout.addView(tableFixHeaders);
//                                                    if (mapSlectedState.contains(stateCode)) {
//                                                        // ManagmentFragment fragment = new ManagmentFragment();
//                                                        loadFragment(Utility.getStateName(stateCode));
//                                                    }
//                                                    break;
//                                                }
//                                            }
//                                            //  dv.invalidate();
//                                            break;
//
////                 case MotionEvent.ACTION_UP:
////                        //screen touch get x of the touch event
////                        x = event.getX();
////                        //screen touch get y of the touch event
////                        y = event.getY();
////                        break;
////
////                case MotionEvent.ACTION_MOVE:
////                        //screen touch get x of the touch event
////                        x = event.getX();
////                        //screen touch get y of the touch event
////                        y = event.getY();
////                        break;
////                }
//                                    }
//                                    return true;
//                                }
//                            });
                            // sliderPagerAdapter.notifyDataSetChanged();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        };

        return listenerObj;
    }

    public void loadpage(final GeoMapView geoMapView,final Map<String, Integer>  salesMap){
        geoMapView.setOnInitializedListener(new OnInitializedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onInitialized(GeoMapView geoMapView) {
//                                    svgLoader.setVisibility(View.INVISIBLE);
//                                    svgLoader.endAnimation();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                GlobalClass.salesState.clear();
                hexColors = new String[salesMap.size()];
                int i = 0;

if(stateSales!=null){
    stateSales.removeAllViews();
}

                int count=0;
                for (Map.Entry<String, Integer> val:salesMap.entrySet()) {
                    String stateCode = Utility.getStateCode(val.getKey());
                    LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View contentView = inflater1.inflate(R.layout.cell_test, null, false);
                    TextView tv= contentView.findViewById(R.id.saleAmount);
                    TextView tvState= contentView.findViewById(R.id.textView);
                    RelativeLayout frame=contentView.findViewById(R.id.cardFrame);
                    LinearLayout ll=contentView.findViewById(R.id.textLayout);
                    // mapLegend.addView(contentView, 0);
                    //dots[count] = new TextView(getContext());
                    String text=String.valueOf(val.getValue())+" %";
                    tv.setText(text);
                    tvState.setText(stateCode);
                    //dots[count].setTextSize(15);
                    frame.setBackgroundColor(Color.parseColor(Utility.getReceivableColour(val.getValue())));


                    finalMap.put(stateCode,val.getValue());
                    geoMapView.setCountryColor(stateCode, Utility.getReceivableColour(val.getValue()), stateCode);

                    View contentV = inflater1.inflate(R.layout.horizontal_divider, null, false);

                    stateSales.addView(contentView, 0);
                    stateSales.addView(contentV, 0);

                    contentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView cast = (TextView) v.findViewById(R.id.saleAmount);
                            TextView castTextView = (TextView) v.findViewById(R.id.textView);
                            // Toast.makeText(getContext(), castTextView.getText()+" >> "+Utility.getStateName(castTextView.getText().toString()), Toast.LENGTH_SHORT).show();
                            loadFragment(Utility.getStateName(castTextView.getText().toString()));
//Click able view for casts
                        }
                    });
                    count++;
                }

//                for (Map.Entry<String, Integer> set : salesMap.entrySet()) {
//                    // View stateView = getLayoutInflater().inflate(R.layout.state_list, null);
//                    String stateCode = Utility.getStateCode(set.getKey());
//                    // GlobalClass.salesState.add(set.getKey());
//                    GlobalClass.salesState.add(stateCode);
//                    mapSlectedState.add(stateCode);
//                    finalMap.put(stateCode,set.getValue());
//                    geoMapView.setCountryColor(stateCode, Utility.getReceivableColour(set.getValue()), String.valueOf(set.getValue()) + " Cr");
//                    hexColors[i] = Utility.getReceivableColour(set.getValue());
//                    i++;
//                }
//                materialLeanBack.setAdapter(new MaterialLeanBack.Adapter<TestViewHolder>() {
//                    @Override
//                    public int getLineCount() {
//                        return 1;
//                    }
//
//                    @Override
//                    public int getCellsCount(int line) {
//                        return 4;
//                    }
//
//                    @Override
//                    public TestViewHolder onCreateViewHolder(ViewGroup viewGroup, int line) {
//                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_test, viewGroup, false);
//                        return new TestViewHolder(view);
//                    }
//
//                    @Override
//                    public void onBindViewHolder(TestViewHolder viewHolder, int i) {
//                        viewHolder.textView.setText(((String)mapSlectedState.get(i)));
//                        viewHolder.saleAmount.setText(String.valueOf((Integer) finalMap.get((String)mapSlectedState.get(i)))+" Cr");
//                        viewHolder.cardFrame.setBackgroundColor(Color.parseColor(hexColors[i]));
//                        //String url = "http://www.lorempixel.com/40" + viewHolder.row + "/40" + viewHolder.cell + "/";
//                        //  Picasso.with(viewHolder.imageView.getContext()).load(url).into(viewHolder.imageView);
//                    }
//
//                    @Override
//                    public String getTitleForRow(int row) {
//                        return "Line " + row;
//                    }
//
//                    @Override
//                    public boolean hasRowTitle(int row) {
//                        return row != 6;
//                    }
//
//
//                    //region customView
//                    @Override
//                    public RecyclerView.ViewHolder getCustomViewForRow(ViewGroup viewgroup, int row) {
////                                                if (row == 3) {
////                                                   // View view = LayoutInflater.from(viewgroup.getContext()).inflate(R.layout.header, viewgroup, false);
////                                                    return new RecyclerView.ViewHolder(view) {
////                                                    };
////                                                } else
////                                                    return null;
//                        return null;
//                    }
//
//                    @Override
//                    public boolean isCustomView(int row) {
//                        return row == 3;
//                    }
//
//                    @Override
//                    public void onBindCustomView(RecyclerView.ViewHolder viewHolder, int row) {
//                        super.onBindCustomView(viewHolder, row);
//                    }
//
////                                            @Override
////                                            public int getEnlargedItemPosition(int position) {
////
////                                                Toast.makeText(getActivity(),String.valueOf(position),Toast.LENGTH_LONG).show();
////
////                                                return super.getEnlargedItemPosition(position);
////                                            }
//
//                    //endregion
//
//                });
//
//                materialLeanBack.setOnItemClickListener(new MaterialLeanBack.OnItemClickListener() {
//
//                    @Override
//                    public void onTitleClicked(int row, String text) {
//                        Toast.makeText(getActivity(), "onTitleClicked " + row + " " + text, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onItemClicked(int row, int column) {
//                        Log.d(TAG,"column > "+column+" row >"+row);
//                        Log.d(TAG,"mapSlectedState > "+mapSlectedState.size());
//                        Log.d(TAG,"mapSlectedState.get(column) > "+mapSlectedState.get(column-1));
//
//                        //Toast.makeText(getActivity(), "onItemClicked " + row + " " + column, Toast.LENGTH_SHORT).show();
//                        loadFragment(Utility.getStateName((String)mapSlectedState.get(column-1)));
//                    }
//                });
//                //  colorSlider.setHexColors( hexColors);
//

                geoMapView.refresh();
            }
        });

        geoMapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // have same code as onTouchEvent() (for the Activity) above

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //screen touch get x of the touch event
                        float x = event.getX();
                        //screen touch get y of the touch event
                        float y = event.getY();

                        //check if touch point intersects the path bounds
                        for (Path p : geoMapView.getPaths()) {
                            RectF pBounds = new RectF();
                            p.computeBounds(pBounds, true);
                            Log.d("touched", "  pBounds.centerX : " + pBounds.centerX() + "centerY :  " + pBounds.centerY());

                            if (pBounds.contains(x, y)) {
                                //select path
                                Path selectedPath = p;// where selectedPath is assumed declared.
                                Log.d("touched", " isConvex : " + selectedPath.isConvex());
                                String stateCode = geoMapView.getPathsMap().get(selectedPath);
                                Log.d("touched", "X : " + x + " Y : " + y);
                                Log.d("stateCode##", stateCode);
                                // totalSalesLayout.removeAllViews();
                                // totalSalesLayout.addView(tableFixHeaders);
                                Log.d("state finalMap -->", finalMap.toString());
                                if (finalMap.containsKey(stateCode)) {
                                    Log.d("state code -->", stateCode);
                                    Log.d("mapSlectedState -->", finalMap.toString());
                                  //   ManagmentFragment fragment = new ManagmentFragment();
                                    loadFragment(Utility.getStateName(stateCode));
                                }
                                break;
                            }
                        }
                        //  dv.invalidate();
                        break;

//                 case MotionEvent.ACTION_UP:
//                        //screen touch get x of the touch event
//                        x = event.getX();
//                        //screen touch get y of the touch event
//                        y = event.getY();
//                        break;
//
//                case MotionEvent.ACTION_MOVE:
//                        //screen touch get x of the touch event
//                        x = event.getX();
//                        //screen touch get y of the touch event
//                        y = event.getY();
//                        break;
//                }
                }
                return true;
            }
        });
    }
    private Response.ErrorListener createRequestErrorListener(final long mRequestStartTime) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Log.d("Error :- ", error.toString());

                    if (error instanceof NetworkError) {
                        Toast.makeText(getContext(), "Network Error.", Toast.LENGTH_LONG).show();

                    } else if (error instanceof ServerError) {
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(getContext(), "Authentication fail.", Toast.LENGTH_LONG).show();

                    } else if (error instanceof ParseError) {
                        Toast.makeText(getContext(), "We are not able to process login request due to technical issue.", Toast.LENGTH_LONG).show();

                    } else if (error instanceof NoConnectionError) {
                        Toast.makeText(getContext(), "No connection available to connect with Server.", Toast.LENGTH_LONG).show();

                    } else if (error instanceof TimeoutError) {

                        long totalRequestTime = System.currentTimeMillis() - mRequestStartTime;
                        final String timeString =
                                new SimpleDateFormat("HH:mm:ss").format(mRequestStartTime);
                        Log.d("Err", error.toString());
                        Log.d("error.getClass :", error.getClass().toString());
                        Log.d("StartTime", timeString);
                        Log.d("EndTime", Utility.getCurrentTimeStamp());
                        Log.d("error total Time :", String.valueOf(totalRequestTime));
                        StringBuffer strBuff = new StringBuffer();
                        strBuff.append("The request timed out");
                        // strBuff.append(GlobalClass.userId);
                        strBuff.append(Utility.getCurrentTimeStamp());
                        Toast.makeText(getContext(), strBuff.toString(), Toast.LENGTH_LONG).show();


                    }
                    //  onRequestFailed();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
    }

}