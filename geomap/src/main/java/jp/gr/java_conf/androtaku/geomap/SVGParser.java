package jp.gr.java_conf.androtaku.geomap;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by takuma on 2015/07/18.
 */
public class SVGParser {
    public static float xMax = 0;
    public static float yMax = 0;

    public static List<CountrySection> getCountrySections(Context context){
       // InputStream inputStream = context.getResources().openRawResource(R.raw.world);
        InputStream inputStream = context.getResources().openRawResource(R.raw.indiahigh);
        //InputStream inputStream = context.getResources().openRawResource(R.raw.india1);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<CountrySection> countySections = new ArrayList<>();
        String tempString;
        try {
            //skip lines until <g>
            while(!reader.readLine().contains("<g>"));

            //parse path
            while(!(tempString = reader.readLine()).contains("</g>")){
                CountrySection countrySection = new CountrySection();
                String idPrefix = "id=\"";
                int index = tempString.indexOf(idPrefix) + idPrefix.length();
               // Log.d("index",String.valueOf(index));
                //Log.d("tempString",String.valueOf(tempString));
                String countryCode = tempString.substring(index, tempString.indexOf("\"", index));
                countrySection.setCountryCode(countryCode);
                String titlePrefix = "title=\"";
                int title = tempString.indexOf(titlePrefix) + titlePrefix.length();
                // Log.d("index",String.valueOf(index));
                //Log.d("tempString",String.valueOf(tempString));
                String titleString = tempString.substring(title, tempString.indexOf("\"", title));
                countrySection.setCountryName(titleString);
                Log.d("titleString",titleString);

                String pathPrefix = " d=\"";
                index = tempString.indexOf(pathPrefix) + pathPrefix.length();
               // Log.d("index1",String.valueOf(index));
                int endIndex = tempString.indexOf("\"", index);
                List<List<Float>> xPathList = new ArrayList<>();
                List<List<Float>> yPathList = new ArrayList<>();
                List<Float> xPathTmp = new ArrayList<>();
                List<Float> yPathTmp = new ArrayList<>();
                float preXPos = 0;
                float preYPos = 0;
                Status status = Status.READ_ORDER;
                while(index < endIndex){
                    String tmpXPos, tmpYPos, tmp;
                    switch(status){
                        case READ_ORDER:
                            String order = tempString.substring(index, ++index);
                       //     Log.d("order > ",order);
                            switch(order){
                                case "M":
                                    status = Status.READ_ABS_X_PATH;
                                    xPathTmp = new ArrayList<>();
                                    yPathTmp = new ArrayList<>();
                                    break;
                                case "m":
                                    status = Status.READ_REL_X_PATH;
                                    xPathTmp = new ArrayList<>();
                                    yPathTmp = new ArrayList<>();
                                    break;
                                case "L":
                                    status = Status.READ_ABS_X_PATH;
                                    break;
                                case "l":
                                    status = Status.READ_REL_X_PATH;
                                    break;
                                case "z":
                                    xPathList.add(xPathTmp);
                                    yPathList.add(yPathTmp);
                            }
                            break;
                        case READ_ABS_X_PATH:
                            tmpXPos = tempString.substring(index, tempString.indexOf(",", index));
                          //  Log.d("tmpXPos > ",tmpXPos);
                            xPathTmp.add(Float.valueOf(tmpXPos));
                            preXPos = Float.valueOf(tmpXPos);
                            index = tempString.indexOf(",", index) + 1;
                            status = Status.READ_ABS_Y_PATH;
                            break;
                        case READ_ABS_Y_PATH:
                            tmpYPos = "";
                            while((tmp = tempString.substring(index, ++index)).matches("[0-9.-]")){
                                tmpYPos += tmp;
                            }
                           // Log.d("tmpYPos > ",tmpYPos);
                            yPathTmp.add(Float.valueOf(tmpYPos));
                            preYPos = Float.valueOf(tmpYPos);
                            status = Status.READ_ORDER;
                            --index;
                            break;
                        case READ_REL_X_PATH:
                            tmpXPos = tempString.substring(index, tempString.indexOf(",", index));
                            xPathTmp.add(preXPos + Float.valueOf(tmpXPos));
                            preXPos += Float.valueOf(tmpXPos);
                            index = tempString.indexOf(",", index) + 1;
                            status = Status.READ_REL_Y_PATH;
                            break;
                        case READ_REL_Y_PATH:
                            tmpYPos = "";
                            while((tmp = tempString.substring(index, ++index)).matches("[0-9.-]")){
                                tmpYPos += tmp;
                            }
                            yPathTmp.add(preYPos + Float.valueOf(tmpYPos));
                            preYPos += Float.valueOf(tmpYPos);
                            status = Status.READ_ORDER;
                            --index;
                    }
                }
                if(xMax < preXPos){
                    xMax = preXPos;
                }
                if(yMax < preYPos){
                    yMax = preYPos;
                }
               // Log.d("xMax",String.valueOf(xMax));
               // Log.d("yMax",String.valueOf(yMax));
                countrySection.setXPathList(xPathList);
                countrySection.setYPathList(yPathList);
                countySections.add(countrySection);
            }
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return countySections;
    }

    private enum Status {
        READ_ORDER,
        READ_ABS_X_PATH,
        READ_ABS_Y_PATH,
        READ_REL_X_PATH,
        READ_REL_Y_PATH,
    }
}
