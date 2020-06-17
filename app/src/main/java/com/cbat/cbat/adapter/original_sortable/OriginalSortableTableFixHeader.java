package com.cbat.cbat.adapter.original_sortable;

import android.content.Context;

import android.util.Log;

import com.cbat.cbat.R;
import com.cbat.cbat.adapter.TableFixHeaderAdapter;
import com.inqbarna.tablefixheaders.adapters.BaseTableAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by miguel on 05/06/2016.
 */
public class OriginalSortableTableFixHeader {
    private Context context;
    private ItemSortableCheckBox firstHeader;
    private List<ItemSortable> header;
    private List<NexusWithImage> body;
    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
    ItemSortable headers[];
    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup> clickListenerBody;
    TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup> clickListenerFirstBody;
    public OriginalSortableTableFixHeader(Context context) {
        this.context = context;
    }

    public BaseTableAdapter getInstance() {
        OriginalSortableTableFixHeaderAdapter adapter = new OriginalSortableTableFixHeaderAdapter(context);
        body = getBody();
        header = getHeader();
        Log.d(TAG,"header > > "+header.size());
        firstHeader = new ItemSortableCheckBox(header.get(header.size() - 1).text);
        adapter.setFirstHeader(firstHeader);
        adapter.setHeader(header.subList(0, header.size() - 1));
        adapter.setFirstBody(body);
        adapter.setBody(body);
        adapter.setSection(body);

        setListeners(adapter);

        return adapter;
    }

    public void setClickListenerBody(TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup> clickListenerBody){
        this.clickListenerBody=clickListenerBody;
    }
    public void setClickListenerFirstBody(TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup> clickListenerFirstBody){
        this.clickListenerFirstBody=clickListenerFirstBody;
    }
    private void updateOderIndicators(ItemSortable item, final int column, TableFixHeaderAdapter adapter) {
        final boolean orderAsc = item.order == 1;
        firstHeader.order = 0;
        for (ItemSortable itemAux : header) itemAux.order = 0;
        item.order = !orderAsc ? 1 : -1;
    }

    private void applyOrder(final boolean orderAsc, final boolean orderSectionsAsc, final int column, TableFixHeaderAdapter adapter) {
        // Order devices by column data, not really a good comparation, simply compare strings.
        Collections.sort(body, new Comparator<NexusWithImage>() {
            @Override
            public int compare(NexusWithImage nexus1, NexusWithImage nexus2) {
                if (nexus1.isSection() || nexus2.isSection()) return 1;
                else if (orderAsc)
                    return nexus1.data[column + 1].compareToIgnoreCase(nexus2.data[column + 1]);
                else
                    return nexus2.data[column + 1].compareToIgnoreCase(nexus1.data[column + 1]);
            }
        });


        // Group devices by section
        Collections.sort(body, new Comparator<NexusWithImage>() {
            @Override
            public int compare(NexusWithImage nexus1, NexusWithImage nexus2) {
                int compare = orderSectionsAsc ? nexus1.type.compareToIgnoreCase(nexus2.type) : nexus2.type.compareToIgnoreCase(nexus1.type);
                boolean areEquals = compare == 0;

                if (areEquals)
                    return nexus1.isSection() ? -1 : nexus2.isSection() ? 1 : 0;

                return compare;
            }
        });

        adapter.setBody(body);
    }

    private void applyOrder1(final boolean orderAsc, final boolean orderSectionsAsc, final int column, TableFixHeaderAdapter adapter) {
        // Order devices by column data, not really a good comparation, simply compare strings.
        Collections.sort(body, new Comparator<NexusWithImage>() {
            @Override
            public int compare(NexusWithImage nexus1, NexusWithImage nexus2) {
                try {
                    if (nexus1.isSection() || nexus2.isSection()) return 1;
                    else if (orderAsc)
                        if (column >= 0) {
                            Log.d(TAG, "%%%%%%%%%%%%%%%%%%%%%%% 1 > " + nexus1.data[column + 1]);
                            Log.d(TAG, "%%%%%%%%%%%%%%%%%%%%%%% 2 > " + nexus2.data[column + 1]);
                            //return Double.parseDouble(nexus1.data[column + 1])==Double.parseDouble(nexus2.data[column + 1])?0:1;
                        if(!nexus1.data[column + 1].contains("-")) {
                            double value1 = nexus1.data[column + 1].contains("₹") ? format.parse(nexus1.data[column + 1]).doubleValue() : Double.parseDouble(nexus1.data[column + 1]);
                            double value2 = nexus2.data[column + 1].contains("₹") ? format.parse(nexus2.data[column + 1]).doubleValue() : Double.parseDouble(nexus2.data[column + 1]);
                            return Double.compare(value1, value2);
    //return Double.compare(Double.parseDouble(nexus1.data[column + 1]), Double.parseDouble(nexus2.data[column + 1]));
                        }else{
                            double value1 = Double.parseDouble(nexus1.data[column + 1].replaceAll("[^0-9]+", ""));
                            double value2 = Double.parseDouble(nexus2.data[column + 1].replaceAll("[^0-9]+", ""));
                            return Double.compare(value1, value2);
                        }
                        } else {
                            return nexus1.data[column + 1].compareToIgnoreCase(nexus2.data[column + 1]);
                        }
                    else {
                        if (column >= 0) {
                           // Log.d(TAG, "%%%%%%%%%%%%%%%%%%%%%%% 1 > " + nexus1.data[column + 1]);
                           // Log.d(TAG, "%%%%%%%%%%%%%%%%%%%%%%% 2 > " + nexus2.data[column + 1]);
                            //return Double.parseDouble(nexus1.data[column + 1])==Double.parseDouble(nexus2.data[column + 1])?0:1;
                            //return Double.compare(Double.parseDouble(nexus2.data[column + 1]), Double.parseDouble(nexus1.data[column + 1]));
                            if(!nexus1.data[column + 1].contains("-")) {
                                double value1 = nexus2.data[column + 1].contains("₹") ? format.parse(nexus2.data[column + 1]).doubleValue() : Double.parseDouble(nexus2.data[column + 1]);
                                double value2 = nexus1.data[column + 1].contains("₹") ? format.parse(nexus1.data[column + 1]).doubleValue() : Double.parseDouble(nexus1.data[column + 1]);
                                return Double.compare(value1, value2);
                            }else{
                                double value1 = Double.parseDouble(nexus2.data[column + 1].replaceAll("[^0-9]+", ""));
                                double value2 = Double.parseDouble(nexus1.data[column + 1].replaceAll("[^0-9]+", ""));
                                return Double.compare(value1, value2);
                            }
                        } else {
                            return nexus2.data[column + 1].compareToIgnoreCase(nexus1.data[column + 1]);
                        }

                    }
                }catch(Exception e){
                    e.printStackTrace();
                    return 0;
                }

            }
        });


        // Group devices by section
        Collections.sort(body, new Comparator<NexusWithImage>() {
            @Override
            public int compare(NexusWithImage nexus1, NexusWithImage nexus2) {
                int compare = orderSectionsAsc ? nexus1.type.compareToIgnoreCase(nexus2.type) : nexus2.type.compareToIgnoreCase(nexus1.type);
                boolean areEquals = compare == 0;

                if (areEquals)
                    return nexus1.isSection() ? -1 : nexus2.isSection() ? 1 : 0;

                return compare;
            }
        });

        adapter.setBody(body);
    }

    private void setListeners(final OriginalSortableTableFixHeaderAdapter adapter) {
        TableFixHeaderAdapter.ClickListener<ItemSortableCheckBox, OriginalFirstHeaderCellViewGroup> clickListenerFirstHeader = new TableFixHeaderAdapter.ClickListener<ItemSortableCheckBox, OriginalFirstHeaderCellViewGroup>() {
            @Override
            public void onClickItem(ItemSortableCheckBox item, OriginalFirstHeaderCellViewGroup viewGroup, int row, int column) {
                updateOderIndicators(item, column, adapter);
                boolean orderAsc = item.order == 1;
                boolean orderSectionsAsc = item.orderSectionsAsc;
                //applyOrder(orderAsc, orderSectionsAsc, column, adapter);
                applyOrder1(orderAsc, orderSectionsAsc, column, adapter);
                String order = orderAsc ? "ASC" : "DESC";
                //Snackbar.make(viewGroup, "Click on " + item.text + " (" + row + "," + column + ") " + order, Snackbar.LENGTH_SHORT).show();
            }
        };

        TableFixHeaderAdapter.ClickListener<ItemSortable, OriginalHeaderCellViewGroup> clickListenerHeader = new TableFixHeaderAdapter.ClickListener<ItemSortable, OriginalHeaderCellViewGroup>() {
            @Override
            public void onClickItem(ItemSortable item, OriginalHeaderCellViewGroup viewGroup, int row, int column) {
                updateOderIndicators(item, column, adapter);
                boolean orderAsc = item.order == 1;
                boolean orderSectionsAsc =firstHeader.orderSectionsAsc;
                applyOrder1(orderAsc, orderSectionsAsc, column, adapter);

                String order = orderAsc ? "ASC" : "DESC";
                //Snackbar.make(viewGroup, "Click on >>" + item.text + " (" + row + "," + column + ") " + order, Snackbar.LENGTH_SHORT).show();
            }
        };

//        TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup> clickListenerBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalBodyCellViewGroup>() {
//            @Override
//            public void onClickItem(NexusWithImage item, OriginalBodyCellViewGroup viewGroup, int row, int column) {
//                Snackbar.make(viewGroup, "Click on " + item.data[column + 1] + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();
//            }
//        };

//        TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup> clickListenerFirstBody = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalFirstBodyCellViewGroup>() {
//            @Override
//            public void onClickItem(NexusWithImage item, OriginalFirstBodyCellViewGroup viewGroup, int row, int column) {
//                Snackbar.make(viewGroup, "Click on " + item.data[column + 1] + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();
//            }
//        };

        TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalSectionCellViewGroup> clickListenerSection = new TableFixHeaderAdapter.ClickListener<NexusWithImage, OriginalSectionCellViewGroup>() {
            @Override
            public void onClickItem(NexusWithImage item, OriginalSectionCellViewGroup viewGroup, int row, int column) {
               // Snackbar.make(viewGroup, "Click on " + item.type + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();
            }
        };

        adapter.setClickListenerFirstHeader(clickListenerFirstHeader);
        adapter.setClickListenerHeader(clickListenerHeader);
        adapter.setClickListenerFirstBody(clickListenerFirstBody);
        adapter.setClickListenerBody(clickListenerBody);
        adapter.setClickListenerSection(clickListenerSection);
    }

    private List<ItemSortable> getHeader() {

//        final ItemSortable headers[] = {
//                new ItemSortable("Company"),
//                new ItemSortable("Version"),
//                new ItemSortable("API"),
//                new ItemSortable("Storage"),
//                new ItemSortable("Size"),
//                new ItemSortable("RAM"),
//                new ItemSortable("Name")
//        };
        final ItemSortable headers[]=this.headers;
        return Arrays.asList(headers);
    }

    public void setHeader(ItemSortable headers[]){
        this.headers=headers;
    }

    private List<NexusWithImage> getBody() {
       // List<Integer> resImages = Arrays.asList(R.drawable.ic_home_24dp, R.drawable.ic_android_24dp, R.drawable.ic_settings_24dp, R.drawable.ic_sd_storage_24dp, R.drawable.ic_aspect_ratio_24dp, R.drawable.ic_memory_24dp);
//        List<Integer> resImages = Arrays.asList();
//
//        List<NexusWithImage> items = new ArrayList<>();
//        String type = "Mobiles";
//        items.add(new NexusWithImage(type, resImages));
//        items.add(new NexusWithImage(type, "Nexus One", "HTC", "Gingerbread", "10", "512 MB", "3.7\"", "512 MB"));
//        items.add(new NexusWithImage(type, "Nexus S", "Samsung", "Gingerbread", "10", "16 GB", "4\"", "512 MB"));
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
//
//        //return items;
        return body;
    }

    public void setBody(List<NexusWithImage> items){
        body=items;
    }
}
