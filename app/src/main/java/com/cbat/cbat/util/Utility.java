package com.cbat.cbat.util;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Utility {
   public static Map<String, String> stateMap = new HashMap<>();
    static {



        stateMap.put("AN","Andaman and Nicobar Islands");
        stateMap.put("AP","Andhra Pradesh");
        stateMap.put("AR","Arunachal Pradesh");
        stateMap.put("AS","Assam");
        stateMap.put("BR","Bihar");
        stateMap.put("CH","Chandigarh");
        stateMap.put("CT","Chhattisgarh");
        stateMap.put("DD","Daman and Diu");
        stateMap.put("DL","Delhi");
        stateMap.put("DN","Dadra and Nagar Haveli");
        stateMap.put("GA","Goa");
        stateMap.put("GJ","Gujarat");
        stateMap.put("HP","Himachal Pradesh");
        stateMap.put("HR","Haryana");
        stateMap.put("JH","Jharkhand");
        stateMap.put("JK","Jammu and Kashmir");
        stateMap.put("KA","Karnataka");
        stateMap.put("KL","Kerala");
        stateMap.put("LD","Lakshadweep");
        stateMap.put("MH","Maharashtra");
        stateMap.put("ML","Meghalaya");
        stateMap.put("MN","Manipur");
        stateMap.put("MP","Madhya Pradesh");
        stateMap.put("MZ","Mizoram");
        stateMap.put("NL","Nagaland");
        stateMap.put("OR","Odisha");
        stateMap.put("PB","Punjab");
        stateMap.put("PY","Puducherry");
        stateMap.put("RJ","Rajasthan");
        stateMap.put("SK","Sikkim");
        stateMap.put("TG","Telangana");
        stateMap.put("TN","Tamil Nadu");
        stateMap.put("TR","Tripura");
        stateMap.put("UP","Uttar Pradesh");
        stateMap.put("UT","Uttarakhand");
        stateMap.put("WB","West Bengal");

    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;

        } else {
            return false;
        }

    }

    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }


    public static String convertDouleValueToString(double value) {
        try {
            NumberFormat fmt = NumberFormat.getInstance();
            fmt.setGroupingUsed(false);
            fmt.setMaximumIntegerDigits(999);
            fmt.setMaximumFractionDigits(999);
           String total2 = fmt.format(Math.abs(value));
            System.out.println("1) " + total2);
            return total2;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }



    public static String decimal2Palce(float value){
        DecimalFormat df = new DecimalFormat("0.0");
        df.setRoundingMode(RoundingMode.UP);
      //  BigDecimal bd = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        return df.format(value);
    }

    public static String decimal2Palce(double value){
        DecimalFormat df = new DecimalFormat("0.0");
        df.setRoundingMode(RoundingMode.UP);
        //  BigDecimal bd = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        return df.format(value);
    }

    public static double decimal2PalceAsInput(double value){
        DecimalFormat df = new DecimalFormat("0.0");
        df.setRoundingMode(RoundingMode.UP);
        //  BigDecimal bd = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        return Double.parseDouble(df.format(value));
    }

    public static float decimal2PalceAsInput(float value){
        DecimalFormat df = new DecimalFormat("0.0");
        df.setRoundingMode(RoundingMode.UP);
        //  BigDecimal bd = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        return Float.parseFloat(df.format(value));
    }


    public static String formatMinutes(float value) {
        StringBuilder sb = new StringBuilder();

        // translate value to seconds, for example
        int valueInSeconds = (int) (value * 60);
        int minutes = (int) Math.floor(valueInSeconds / 60);
        int seconds = (int) valueInSeconds % 60;

        sb.append(String.valueOf(minutes)).append(':');
        if (seconds < 10) {
            sb.append('0');
        }
        sb.append(String.valueOf(seconds));
        return sb.toString();
    }

    public static String getStateName(String value) {
       return stateMap.get(value);
    }

    public static String getStateCode(String value) {
        String stateCode="";
        for(Map.Entry<String, String> set:stateMap.entrySet()){
            if(set.getValue().equalsIgnoreCase(value)){
                stateCode=set.getKey();
            }

        }
        return stateCode;
    }

    public static String getSalesColour(float value) {
        String colorCode="";

        if(value>=50){
            colorCode="#2A9D8F";
        }
        else if(value>=45){
            colorCode="#55B1A5";
        }
        else if(value>=30){
            colorCode="#77C1B7";
        }else if(value>=25){
            colorCode="#92CDC5";
        }else if(value>=20){
            colorCode="#c57c0d";
        }else if(value>=15){
            colorCode="#c5600d";
        }else if(value>=10){
            colorCode="#c5570d";
        }else if(value>=6){
            colorCode="#c57c0d";
        }else if(value>=5){
            colorCode="#E5E50E";
        }else if(value>=4){
            colorCode="#f77b72";
        }else if(value>=3){
            colorCode="#f6685e";
        }else if(value>=2){
            colorCode="#f5554a";
        }else{
            colorCode="#f44336";
        }
        Log.d("colorCode >> ",colorCode);
        return colorCode;
    }


    public static String getReceivableColour(int value) {
        String colorCode="";

        if(value>=50){
            colorCode="#E9C46A";
        }
        else if(value>=45){
            colorCode="#EDD088";
        }
        else if(value>=30){
            colorCode="#F1D9A0";
        }else if(value>=25){
            colorCode="#F4E1B3";
        }else if(value>=20){
            colorCode="#c57c0d";
        }else if(value>=15){
            colorCode="#c5600d";
        }else if(value>=10){
            colorCode="#c5570d";
        }else if(value>=6){
            colorCode="#c57c0d";
        }else if(value>=5){
            colorCode="#E5E50E";
        }else if(value>=4){
            colorCode="#f77b72";
        }else if(value>=3){
            colorCode="#f6685e";
        }else if(value>=2){
            colorCode="#f5554a";
        }else{
            colorCode="#f44336";
        }
        return colorCode;
    }


    public static String getProductColour(String productName) {
        String colorCode="";

        if(productName.equalsIgnoreCase("Vegetables")){
            colorCode="#0e191f";
        }
        else if(productName.equalsIgnoreCase("Fruits")){
            colorCode="#162830";
        }
        else if(productName.equalsIgnoreCase("Grocery")){
            colorCode="#1e3642";
        }else{
            colorCode="#d34e04";
        }
        return colorCode;
    }


}
