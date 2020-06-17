package com.cbat.cbat.ui.navigation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.os.Environment;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.cbat.cbat.R;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.Utility;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PdfCreatorActivity  {

    private static final String TAG = "PdfCreatorActivity";
    private EditText  mContentEditText;
    private Button mCreateButton;
    private File pdfFile;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;

    private String path;
    private File dir;
    private File file;
    private PdfPCell cell;
    private String textAnswer;
    private Image bgImage;
    ListView list;
    Context context;

    private static String input;
    private static int num;
    private static String[] units=
            {"",
                    " One",
                    " Two",
                    " Three",
                    " Four",
                    " Five",
                    " Six",
                    " Seven",
                    " Eight",
                    " Nine"
            };
    private static String[] teen=
            {" Ten",
                    " Eleven",
                    " Twelve",
                    " Thirteen",
                    " Fourteen",
                    " Fifteen",
                    " Sixteen",
                    " Seventeen",
                    " Eighteen",
                    " Nineteen"
            };
    private static String[] tens=
            { " Twenty",
                    " Thirty",
                    " Forty",
                    " Fifty",
                    " Sixty",
                    " Seventy",
                    " Eighty",
                    " Ninety"
            };
    private static String[] maxs=
            {"",
                    "",
                    " Hundred",
                    " Thousand",
                    " Lakh",
                    " Crore"
            };
   public  PdfCreatorActivity(Context context){
       this.context= context;

   }

    public  String createPdf() throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/CBatDownload");
        String fileName="";
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }
        Log.i(TAG, "pdfDataTab >> "+GlobalClass.pdfDataTab);
        if (GlobalClass.pdfDataTab.equalsIgnoreCase(context.getString(R.string.total_sales))) {
            //GlobalClass.totalSales;

             totalSalesPdf( docsFolder,GlobalClass.totalSales);
        }
        else if(GlobalClass.pdfDataTab.equalsIgnoreCase("customerDetails")){
           // fileName=customerDetailsPdf(docsFolder,GlobalClass.customerFullList);
            fileName=customerDetailsNewPdf(docsFolder,GlobalClass.customerFullList);
        }
        else if(GlobalClass.pdfDataTab.equalsIgnoreCase("invoiceDetails")){
           // fileName=invoiceDetailsPdf(docsFolder,GlobalClass.customerInvoiceFullList);
            fileName=invoiceDetailNewPdf(docsFolder,GlobalClass.customerInvoiceFullList );

        }
        else if(GlobalClass.pdfDataTab.equalsIgnoreCase("ledgerDetails")){
        //    fileName=ledgerDetailsPdf(docsFolder,GlobalClass.customerInvoiceSummaryList);
            fileName=ledgerDetailsNewPdf(docsFolder,GlobalClass.customerInvoiceSummaryList);
        }
        else if(GlobalClass.pdfDataTab.equalsIgnoreCase(context.getString(R.string.product_wise_sales))){
            fileName=productDetailsPdf(docsFolder,GlobalClass.productFullList);
        }
        else if(GlobalClass.pdfDataTab.equalsIgnoreCase(context.getString(R.string.sales_trend))){
            fileName=customerDetailsPdf(docsFolder,GlobalClass.customerSalesTrendFullList);
        }
        else if(GlobalClass.pdfDataTab.equalsIgnoreCase(context.getString(R.string.collection_trend))){
            fileName=customerDetailsPdf(docsFolder,GlobalClass.customerSalesTrendFullList);
        }
        else if(GlobalClass.pdfDataTab.equalsIgnoreCase(context.getString(R.string.credit_utilization))){
            fileName=creditUtilizationDetailsPdf(docsFolder,GlobalClass.creditUtilizeFullList);
        }
        else if(GlobalClass.pdfDataTab.equalsIgnoreCase(context.getString(R.string.total_receivable))){
             fileName=totalReceivablePdf( docsFolder,GlobalClass.totalSales);
        }
        else if(GlobalClass.pdfDataTab.equalsIgnoreCase("customerReceivableDetails")){
            fileName=customerRecevibleDetailsPdf(docsFolder,GlobalClass.customerReceivableFullList);
        } else if(GlobalClass.pdfDataTab.equalsIgnoreCase("invoiceReceivableDetails")){
            invoiceReceivableDetailsPdf(docsFolder,GlobalClass.customerInvoiceFullList);
        }
        else if(GlobalClass.pdfDataTab.equalsIgnoreCase("ledgerReceivableDetails")){
            fileName=ledgerReceivableDetailsPdf(docsFolder,GlobalClass.customerInvoiceSummaryList);
        }
        else if(GlobalClass.pdfDataTab.equalsIgnoreCase(context.getString(R.string.product_wise_receivable))){
            productReceivableDetailsPdf(docsFolder,GlobalClass.productFullList);

        }else if(GlobalClass.pdfDataTab.equalsIgnoreCase(context.getString(R.string.receivable_aging))){
            receiableAgingDetailsPdf( docsFolder,GlobalClass.totalSales);
        }


        else {

            //use to set background color
            BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
            BaseColor myColor1 = WebColors.getRGBColor("#757575");

            pdfFile = new File(docsFolder.getAbsolutePath(), "HelloWorld.pdf");
            OutputStream output = new FileOutputStream(pdfFile);
            Document doc = new Document();
            PdfWriter.getInstance(doc, output);
            doc.open();

            //  document.add(new Paragraph(mContentEditText.getText().toString()));
//create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = context.getResources().getDrawable(R.drawable.logo);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            try {
                bgImage = Image.getInstance(bitmapdata);
                bgImage.setAbsolutePosition(330f, 642f);
                cell.addElement(bgImage);
                pt.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.addElement(new Paragraph("Trinity Tuts"));

                cell.addElement(new Paragraph(""));
                cell.addElement(new Paragraph(""));
                pt.addCell(cell);
                cell = new PdfPCell(new Paragraph(""));
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);
                cell = new PdfPCell();
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);
                PdfPTable table = new PdfPTable(6);

                float[] columnWidth = new float[]{6, 30, 30, 20, 20, 30};
                table.setWidths(columnWidth);


                cell = new PdfPCell();


                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" "));
                cell.setColspan(6);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(6);

                cell.setBackgroundColor(myColor1);

                cell = new PdfPCell(new Phrase("#"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Header 1"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Header 2"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Header 3"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Header 4"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Header 5"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                //table.setHeaderRows(3);
                cell = new PdfPCell();
                cell.setColspan(6);

                for (int i = 1; i <= 10; i++) {
                    table.addCell(String.valueOf(i));
                    table.addCell("Header 1 row " + i);
                    table.addCell("Header 2 row " + i);
                    table.addCell("Header 3 row " + i);
                    table.addCell("Header 4 row " + i);
                    table.addCell("Header 5 row " + i);

                }

                PdfPTable ftable = new PdfPTable(6);
                ftable.setWidthPercentage(100);
                float[] columnWidthaa = new float[]{30, 10, 30, 10, 30, 10};
                ftable.setWidths(columnWidthaa);
                cell = new PdfPCell();
                cell.setColspan(6);
                cell.setBackgroundColor(myColor1);
                cell = new PdfPCell(new Phrase("Total Nunber"));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Paragraph("Footer"));
                cell.setColspan(6);
                ftable.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(6);
                cell.addElement(ftable);
                table.addCell(cell);
                doc.add(table);
                // Toast.makeText(getApplicationContext(), "created PDF", Toast.LENGTH_LONG).show();
                doc.close();
            } catch (DocumentException de) {
                Log.e("PDFCreator", "DocumentException:" + de);
            } catch (IOException e) {
                Log.e("PDFCreator", "ioException:" + e);
            } finally {
                doc.close();
            }
            //  previewPdf();

        }
        return fileName;
    }

    public void totalSalesPdf(File docsFolder,Map<String, JSONObject> totalSales)  {

        //use to set background color
        BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
        BaseColor myColor1 = WebColors.getRGBColor("#264553");
        BaseColor colorWhite = WebColors.getRGBColor("#FFFFFF");
        Font font = new Font();
        font.setColor(colorWhite);
        Document doc = new Document();
        try {
            String fileName="Total_Sales_"+GlobalClass.startDate+"_"+GlobalClass.endDate+".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), fileName);
        OutputStream output = new FileOutputStream(pdfFile);
        PdfWriter.getInstance(doc, output);
        doc.open();

        //  document.add(new Paragraph(mContentEditText.getText().toString()));
//create table
        PdfPTable pt = new PdfPTable(3);
        pt.setWidthPercentage(100);
        float[] fl = new float[]{20, 45, 35};
        pt.setWidths(fl);
        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);

        //set drawable in cell
        Drawable myImage = context.getResources().getDrawable(R.drawable.logo);
        Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();

            bgImage = Image.getInstance(bitmapdata);
            bgImage.setAbsolutePosition(330f, 642f);
            cell.addElement(bgImage);
            pt.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.addElement(new Paragraph("State wise Total Sales"+" from "+GlobalClass.startDate+" to "+GlobalClass.endDate));

            cell.addElement(new Paragraph(""));
            cell.addElement(new Paragraph(""));
            pt.addCell(cell);
            cell = new PdfPCell(new Paragraph(""));
            cell.setBorder(Rectangle.NO_BORDER);
            pt.addCell(cell);

            PdfPTable pTable = new PdfPTable(1);
            pTable.setWidthPercentage(100);
            cell = new PdfPCell();
            cell.setColspan(1);
            cell.addElement(pt);
            pTable.addCell(cell);

            int noOfDataCell=4;
            PdfPTable table = new PdfPTable(noOfDataCell);

            float[] columnWidth = new float[]{6, 40, 40, 40, };
            table.setWidths(columnWidth);


            cell = new PdfPCell();


            cell.setBackgroundColor(myColor);
            cell.setColspan(6);
            cell.addElement(pTable);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" "));
            cell.setColspan(6);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(6);

            cell.setBackgroundColor(myColor1);

            cell = new PdfPCell(new Phrase("Sr.No",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("State",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Sales",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Contribution",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);


            //table.setHeaderRows(3);
            cell = new PdfPCell();
            cell.setColspan(6);
            int count=1;
            double totalSale=0;
           // for (int i = 1; i <= GlobalClass.totalSales.size(); i++) {
                for(Map.Entry<String, JSONObject> element:totalSales.entrySet()){
                    try {

                table.addCell(String.valueOf(count++));
                table.addCell(element.getKey());
                table.addCell(Utility.decimal2Palce(Math.abs(element.getValue().getDouble("sales"))));
                table.addCell(Utility.decimal2Palce(element.getValue().getDouble("value"))+" %");
                        totalSale=totalSale+element.getValue().getDouble("sales");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
            }

            PdfPTable ftable = new PdfPTable(noOfDataCell);
            ftable.setWidthPercentage(100);
            float[] columnWidthaa = new float[]{30, 10, 30, 10};
            ftable.setWidths(columnWidthaa);
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBackgroundColor(myColor1);
            cell = new PdfPCell(new Phrase("Total Sales",font));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(Utility.decimal2Palce(Math.abs(totalSale)),font));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);
//
//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);
//
//            cell = new PdfPCell(new Paragraph("This is System Generate PDF."));
//            cell.setColspan(4);
//            ftable.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);

            cell.addElement(ftable);

            table.addCell(cell);

            doc.add(table);
            Toast.makeText(context, "Created PDF in CBatDownload Folder", Toast.LENGTH_LONG).show();
            doc.close();


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }
        //  previewPdf();

    }

    public String customerDetailsPdf(File docsFolder,Map<String, JSONArray> customerFullList)  {

        //use to set background color
        BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
        BaseColor myColor1 = WebColors.getRGBColor("#264553");
        BaseColor colorWhite = WebColors.getRGBColor("#FFFFFF");
        Font font = new Font();
        font.setColor(colorWhite);
        Document doc = new Document();
        String fileName="";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            // fileName=GlobalClass.title +"_"+ sdf.format(Calendar.getInstance().getTime()) + ".pdf";
            fileName="Customer_List" +"_"+ sdf.format(Calendar.getInstance().getTime()) + ".pdf";
           // String fileName="Customer_Details_"+GlobalClass.startDate+"_"+GlobalClass.endDate+".pdf";
            pdfFile = new File(docsFolder.getAbsolutePath(), fileName);
            OutputStream output = new FileOutputStream(pdfFile);
            PdfWriter.getInstance(doc, output);
            doc.open();

            //  document.add(new Paragraph(mContentEditText.getText().toString()));
//create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = context.getResources().getDrawable(R.drawable.logo);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();

            bgImage = Image.getInstance(bitmapdata);
            bgImage.setAbsolutePosition(330f, 642f);
            cell.addElement(bgImage);
            pt.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.addElement(new Paragraph(GlobalClass.title+" from "+GlobalClass.startDate+" to "+GlobalClass.endDate));

            cell.addElement(new Paragraph(""));
            cell.addElement(new Paragraph(""));
            pt.addCell(cell);
            cell = new PdfPCell(new Paragraph(""));
            cell.setBorder(Rectangle.NO_BORDER);
            pt.addCell(cell);

            PdfPTable pTable = new PdfPTable(1);
            pTable.setWidthPercentage(100);
            cell = new PdfPCell();
            cell.setColspan(1);
            cell.addElement(pt);
            pTable.addCell(cell);

            int noOfDataCell=16;
            PdfPTable table = new PdfPTable(noOfDataCell);

            float[] columnWidth = new float[]{10, 50,40, 40, 30,30,30,30,30,30,30,30,30,30,30,30 };
            table.setWidths(columnWidth);


            cell = new PdfPCell();


            cell.setBackgroundColor(myColor);
            cell.setColspan(noOfDataCell);
            cell.addElement(pTable);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" "));
            cell.setColspan(noOfDataCell);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);

            cell.setBackgroundColor(myColor1);


            /**
             *   ItemSortable headers[] = {
             *                 new ItemSortable("Total"),
             *                 new ItemSortable("% Contri."),
             *                 new ItemSortable("Apr"),
             *                 new ItemSortable("May"),
             *                 new ItemSortable("Jun"),
             *                 new ItemSortable("Jul"),
             *                 new ItemSortable("Aug"),
             *                 new ItemSortable("Sep"),
             *                 new ItemSortable("Oct"),
             *                 new ItemSortable("Nov"),
             *                 new ItemSortable("Dec"),
             *                 new ItemSortable("Jan"),
             *                 new ItemSortable("Feb"),
             *                 new ItemSortable("Mar"),
             *                 new ItemSortable("Customer"),
             */

            cell = new PdfPCell(new Phrase("Sr.No",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Customer",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Total",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("% Contri.",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Apr",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("May",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Jun",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Jul",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Aug",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Sep",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("oct",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Nov",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Dec",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Jan",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Feb",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Mar",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);


            //table.setHeaderRows(3);
            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);
            int count=1;
            double totalSale=0;
            for (Map.Entry<String, JSONArray> set : customerFullList.entrySet()) {
           // for(Map.Entry<String, JSONObject> element:GlobalClass.totalSales.entrySet()){
                try {
                    if (!set.getKey().equalsIgnoreCase("totalStateSales")) {
                        JSONArray dataMonthly = set.getValue().getJSONArray(1);
                        JSONArray dataYearly = set.getValue().getJSONArray(3);
                        JSONArray contriData = set.getValue().getJSONArray(4);
                      //  JSONArray dataCustomer =  set.getValue().getJSONArray(5);
                        //[] dataTable = new String[dataMonthly.length() + 4];
                        JSONObject elementYearly;
                        boolean yearFlag = true;
                        boolean contriFlag = true;
                        float totalSales = 0;
                        int j = 0;

                        for (int i = 0; i < dataMonthly.length(); i++) {
                            if (yearFlag) {
                                elementYearly = dataYearly.getJSONObject(i);
                                yearFlag = false;
                                int yVal = elementYearly.getInt("value");
                                BigDecimal number1 = new BigDecimal(yVal);
                                //totalSales = number1.floatValue() / 10000000;
                                totalSales = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                Log.d(TAG, "set.getKey() >" + set.getKey());
                                Log.d(TAG, "totalSales >" + totalSales);
                                table.addCell(String.valueOf(count++));
                                table.addCell(String.valueOf(set.getKey()));
                                table.addCell(Utility.decimal2Palce(Math.abs(totalSales)));

                            }
                            if (contriFlag) {
                                elementYearly = contriData.getJSONObject(i);
                                contriFlag = false;
                                double yVal = elementYearly.getDouble("value");
                                //BigDecimal number1 = new BigDecimal(yVal);
                                //totalSales = number1.floatValue() / 10000000;
                                //totalSales = number1.floatValue(); // GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                Log.d(TAG, "set.getKey() >" + set.getKey());
                                Log.d(TAG, "yVal >" + yVal);
                                // dataTable[j++] = String.valueOf(set.getKey());
                                table.addCell(Utility.decimal2Palce(Math.abs(yVal)));
                            }

                            JSONObject element = dataMonthly.getJSONObject(i);
                            int yVal = element.getInt("value");
                            BigDecimal number1 = new BigDecimal(yVal);
                            // float debitFloat = number1.floatValue() / 10000000;
                            float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            table.addCell( Utility.decimal2Palce(Math.abs(debitFloat)));

                        }
//                        dataTable[j++]=dataCustomer.getJSONObject(0).getJSONObject("customer").getString("address");
//                        items.add(new NexusWithImage(type, dataTable));
//                        Log.d(TAG, "dataTable >" + dataTable.toString());
//                        Log.d(TAG, "dataTable length >" + dataTable.length);
                    }
//                    table.addCell(String.valueOf(count++));
//                    table.addCell(element.getKey());
//                    table.addCell(Utility.decimal2Palce(Math.abs(element.getValue().getDouble("sales"))));
//                    table.addCell(Utility.decimal2Palce(element.getValue().getDouble("value"))+" %");
//                    totalSale=totalSale+element.getValue().getDouble("sales");
//
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            float totalSales = 0;

            try {
                if (customerFullList != null && !customerFullList.isEmpty() && customerFullList.containsKey("totalStateSales")) {
                    JSONArray setVal = customerFullList.get("totalStateSales");
                    JSONArray dataMonthly = setVal.getJSONArray(1);
                    JSONArray dataYearly = setVal.getJSONArray(3);

                    String[] dataTable = new String[dataMonthly.length() + 4];
                    JSONObject elementYearly;
                    boolean yearFlag = true;
                  //  float totalSales = 0;
                    int j = 0;
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
//                            dataTable[j++] = String.valueOf("Total Sales");
//                            dataTable[j++] = Utility.decimal2Palce(Math.abs(totalSales));
//                            dataTable[j++] = "100";
                        }

                        JSONObject element = dataMonthly.getJSONObject(i);
                        int yVal = element.getInt("value");
                        BigDecimal number1 = new BigDecimal(yVal);
                        // float debitFloat = number1.floatValue() / 10000000;
                        float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                        Log.d(TAG, "debitFloat >" + debitFloat);
                       // dataTable[j++] = Utility.decimal2Palce(Math.abs(debitFloat));

                    }
                    //items.add(new NexusWithImage(type, dataTable));
                   // Log.d(TAG, "dataTable >" + dataTable.toString());
                   // Log.d(TAG, "dataTable length >" + dataTable.length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            PdfPTable ftable = new PdfPTable(noOfDataCell);
            ftable.setWidthPercentage(100);
            float[] columnWidthaa = new float[]{50, 30,20, 50, 30,30,30,30,30,30,30,30,30,30,30,30  };
            ftable.setWidths(columnWidthaa);
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBackgroundColor(myColor1);
            cell = new PdfPCell(new Phrase("Total Sales",font));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);


//

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(Utility.decimal2Palce(Math.abs(totalSales)),font));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
//
//            cell = new PdfPCell(new Paragraph("This is System Generate PDF."));
//            cell.setColspan(noOfDataCell);
//            ftable.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);

            cell.addElement(ftable);

            table.addCell(cell);

            doc.add(table);
            Toast.makeText(context, "Created PDF in CBatDownload Folder", Toast.LENGTH_LONG).show();
            doc.close();


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }
        //  previewPdf();
    return fileName;
    }

    public String customerDetailsNewPdf(File docsFolder,Map<String, JSONArray> customerFullList)  {
       Log.d("customerFullList", customerFullList.toString());

        Map<String, JSONArray> list= new HashMap<>();
        ArrayList<String> customer=new ArrayList<>();
        ArrayList<Double> contri=new ArrayList<>();
        ArrayList<Double> value=new ArrayList<>();
        String time="";
        int total=0;
        String state="";
        String pinCode="";
try{
    int count=1;
    double totalSale=0;

    for (Map.Entry<String, JSONArray> set : customerFullList.entrySet()) {
        // for(Map.Entry<String, JSONObject> element:GlobalClass.totalSales.entrySet()){
        try {
            if (!set.getKey().equalsIgnoreCase("totalStateSales")) {
                JSONArray dataMonthly = set.getValue().getJSONArray(1);
                JSONArray dataYearly = set.getValue().getJSONArray(3);
                JSONArray contriData = set.getValue().getJSONArray(4);
                JSONArray info = set.getValue().getJSONArray(5);
                //  JSONArray dataCustomer =  set.getValue().getJSONArray(5);
                //[] dataTable = new String[dataMonthly.length() + 4];
                JSONObject elementYearly;
                boolean yearFlag = true;
                boolean contriFlag = true;
                float totalSales = 0;
                int j = 0;

                for (int i = 0; i < dataMonthly.length(); i++) {
                    if (yearFlag) {
                        elementYearly = dataYearly.getJSONObject(i);
                        yearFlag = false;
                        int yVal = elementYearly.getInt("value");
                        time=elementYearly.getString("name");
                        BigDecimal number1 = new BigDecimal(yVal);
                        //totalSales = number1.floatValue() / 10000000;
                        totalSales = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                        Log.d(TAG, "set.getKey() >" + set.getKey());
                        Log.d(TAG, "totalSales 123>" + totalSales+" "+time);
                        customer.add(set.getKey());
                        value.add((double) totalSales);
                           int val= (int) totalSales;
                        total= total+val;
                        Log.d(TAG, "totalSales 123>" + totalSales+" "+time+" "+total+" "+val);
                    }
                    if (contriFlag) {
                        elementYearly = contriData.getJSONObject(i);
                        contriFlag = false;
                        double yVal = elementYearly.getDouble("value");
                        BigDecimal number1 = new BigDecimal(yVal);
                     //   totalSales = number1.floatValue() / 10000000;
                        totalSales = number1.floatValue(); // GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                        Log.d(TAG, "set.getKey() >" + set.getKey());
                        Log.d(TAG, "yVal >" + yVal);
                        contri.add(yVal);
                        // dataTable[j++] = String.valueOf(set.getKey());

                    }

                    JSONObject element = dataMonthly.getJSONObject(i);
                    int yVal = element.getInt("value");
                    BigDecimal number1 = new BigDecimal(yVal);
                    // float debitFloat = number1.floatValue() / 10000000;
                    float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);


                }

                for(int k=0;k<info.length();k++){
                    Log.d("state1@1", info.toString());
                    JSONObject element = info.getJSONObject(k);
                    JSONObject obj=element.getJSONObject("customer");
                     state=obj.getString("state");
                    pinCode=obj.getString("pincode");
                  Log.d("state1@1", state);

                }
//                        dataTable[j++]=dataCustomer.getJSONObject(0).getJSONObject("customer").getString("address");
//                        items.add(new NexusWithImage(type, dataTable));
//                        Log.d(TAG, "dataTable >" + dataTable.toString());
//                        Log.d(TAG, "dataTable length >" + dataTable.length);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}catch (Exception e){
    Log.d(" key e" , String.valueOf(e));
}


        Document doc = new Document();
        String fileName="";
       try{

           SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
           fileName="Customer_List" +"_"+ sdf.format(Calendar.getInstance().getTime()) + ".pdf";
           pdfFile = new File(docsFolder.getAbsolutePath(), fileName);
           OutputStream output = new FileOutputStream(pdfFile);
           PdfWriter.getInstance(doc, output);
           doc.open();
           Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
           PdfPTable table = new PdfPTable(1);
           table.setWidthPercentage(100);

           PdfPCell cell1 = new PdfPCell(new Paragraph("Cell1"));

        //   table.addCell(cell1);
           try{
               PdfPTable table5 = new PdfPTable(4);
               table5.setWidthPercentage(100);
             //  float[] widths4 = new float[] { 50f, 120f, 50f, 60f, 50f, 45f, 40f, 40f};
            //   table4.setWidths(widths4);
               PdfPCell Report = new PdfPCell(new Paragraph("Report Of Sales:"));
               Report.setBorder(Rectangle.NO_BORDER);
               PdfPCell cus = new PdfPCell(new Paragraph("Customer Wise", boldFont));
               cus.setBorder(Rectangle.NO_BORDER);
               PdfPCell period = new PdfPCell(new Paragraph("Period:"));
               period.setBorder(Rectangle.NO_BORDER);
               PdfPCell perioidTime = new PdfPCell(new Paragraph(time, boldFont));
               perioidTime.setBorder(Rectangle.NO_BORDER);
               PdfPCell stat = new PdfPCell(new Paragraph("state:"));
               stat.setBorder(Rectangle.NO_BORDER);
               PdfPCell stateInfo = new PdfPCell(new Paragraph(state, boldFont));
               stateInfo.setBorder(Rectangle.NO_BORDER);
               PdfPCell pin = new PdfPCell(new Paragraph("pinCode:"));
               pin.setBorder(Rectangle.NO_BORDER);
               PdfPCell pinInfo = new PdfPCell(new Paragraph(pinCode, boldFont));
               pinInfo.setBorder(Rectangle.NO_BORDER);

               table5.addCell(stat);
               table5.addCell(stateInfo);
               table5.addCell(pin);
               table5.addCell(pinInfo);

               table5.addCell(Report);
               table5.addCell(cus);
               table5.addCell(period);
               table5.addCell(perioidTime);

               table.addCell(table5);
           }catch(Exception e){
               Log.e("table 5", "ioException:" + e);
           }


           try{
               PdfPTable table4 = new PdfPTable(6);
               table4.setWidthPercentage(100);
               float[] widths4 = new float[] { 100f,70f , 60f, 60f, 50f, 45f};
               table4.setWidths(widths4);


               PdfPCell customerName = new PdfPCell(new Paragraph("Customer Name"));
               PdfPCell Particular4 = new PdfPCell(new Paragraph("Particular"));
               PdfPCell contrib = new PdfPCell(new Paragraph("%Contibution"));
               PdfPCell Vouchertype4 = new PdfPCell(new Paragraph("Vch Type"));
               PdfPCell Debit4 = new PdfPCell(new Paragraph("Debit"));
               PdfPCell Credit4 = new PdfPCell(new Paragraph("Credit"));
               table4.addCell(customerName);
               table4.addCell(Particular4);
               table4.addCell(contrib);
               table4.addCell(Vouchertype4);
               table4.addCell(Debit4);
               table4.addCell(Credit4);
               table.addCell(table4);
           }catch(Exception e){
               Log.e("table 4", "ioException:" + e);
           }


           try{
               PdfPTable table1 = new PdfPTable(6);
               table1.setWidthPercentage(100);
               float[] widths1 = new float[] { 100f, 70f, 60f, 60f, 50f, 45f};
               table1.setWidths(widths1);
               for(int i=0;i<customer.size();i++){
                   PdfPCell BillDate1 = new PdfPCell(new Paragraph(customer.get(i)));
                   PdfPCell Particular1 = new PdfPCell(new Paragraph("Total Sales"));

                   PdfPCell PaymentTerm1 = new PdfPCell(new Paragraph(String.valueOf(contri.get(i))+"%"));

                   PdfPCell VchNo1 = new PdfPCell(new Paragraph("sales"));
                   PdfPCell Debit1 = new PdfPCell(new Paragraph(String.valueOf(value.get(i))));
                   PdfPCell Credit1 = new PdfPCell(new Paragraph(""));
                   table1.addCell(BillDate1);
                   table1.addCell(Particular1);

                   table1.addCell(PaymentTerm1);

                   table1.addCell(VchNo1);
                   table1.addCell(Debit1);
                   table1.addCell(Credit1);
               }

               table.addCell(table1);
           }catch(Exception e){
               Log.e("table 1", "ioException:" + e);
           }
           try{

               Log.e("table 1", "val:" + total);
               PdfPTable table2 = new PdfPTable(6);
               table2.setWidthPercentage(100);
               float[] widths2 = new float[] { 50f, 120f, 60f, 60f, 50f, 45f};
               table2.setWidths(widths2);
             //  PdfPCell BillDate2 = new PdfPCell(new Paragraph(""));
               PdfPCell Particular2 = new PdfPCell(new Paragraph("Total Of Debit and Credit"));
               Particular2.setColspan(2);
               PdfPCell DueDate2 = new PdfPCell(new Paragraph(""));
               PdfPCell VchNo2 = new PdfPCell(new Paragraph(""));
               PdfPCell Debit2 = new PdfPCell(new Paragraph((String.valueOf(total))));
               PdfPCell Credit2 = new PdfPCell(new Paragraph(""));
               //table2.addCell(BillDate2);
               table2.addCell(Particular2);
               table2.addCell(DueDate2);

               table2.addCell(VchNo2);
               table2.addCell(Debit2);
               table2.addCell(Credit2);
               table.addCell(table2);
           }catch(Exception e){
               Log.e("table 2", "ioException:" + e);
           }
           try{
               PdfPTable table3 = new PdfPTable(6);
               table3.setWidthPercentage(100);
               float[] widths3 = new float[] { 50f, 120f, 60f, 60f, 50f, 45f};
               table3.setWidths(widths3);
               PdfPCell BillDate3 = new PdfPCell(new Paragraph("Closing Bal Formula: "+"\n"+"Op bal + Dr Total - Cr Total= Cl Bal"));
               BillDate3.setColspan(2);
           //    PdfPCell Particular3 = new PdfPCell(new Paragraph(""));
               PdfPCell DueDate3 = new PdfPCell(new Paragraph(""));
               PdfPCell PaymentTerm3 = new PdfPCell(new Paragraph("Closing Bal"));
               PdfPCell Debit3 = new PdfPCell(new Paragraph(String.valueOf(total)));
               PdfPCell Credit3 = new PdfPCell(new Paragraph(""));
               table3.addCell(BillDate3);
            //   table3.addCell(Particular3);
               table3.addCell(DueDate3);
               table3.addCell(PaymentTerm3);
               table3.addCell(Debit3);
               table3.addCell(Credit3);
               table.addCell(table3);
           }catch(Exception e){
               Log.e("table 3", "ioException:" + e);
           }
           doc.add(table);
           Toast.makeText(context, "Created PDF in CBatDownload Folder", Toast.LENGTH_LONG).show();
           doc.close();
       }catch(Exception e){
           Log.e("PDFCreator", "ioException:" + e);
       } finally {
           doc.close();
       }
       return fileName;
    }
    public String invoiceDetailNewPdf(File docsFolder,List<JSONObject> customerInvoiceFullList)  {
        Log.d("customerFullList", customerInvoiceFullList.toString());
        Log.d("Globalclass", GlobalClass.CustomerName+""+GlobalClass.CustomerPincode+" "+GlobalClass.CustomerState);

        int count=1;
        double totalSale=0;
        ArrayList <String> invNo=new ArrayList<>();
        ArrayList <Integer> value=new ArrayList<>();
        ArrayList <String> cre=new ArrayList<>();
        ArrayList <String> date=new ArrayList<>();

        for (int i = 0; i < customerInvoiceFullList.size(); i++) {
            try {
                JSONObject element = customerInvoiceFullList.get(i);
                int yVal = element.getInt("value");

                invNo.add(element.getString("name"));
                value.add(yVal);
                date.add(element.getString("dueDate"));
                cre.add(String.valueOf(element.getInt("credirPeroid")));

            }catch(Exception e){
Log.d("invoice", String.valueOf(e));
            }
        }

        try{


        }catch (Exception e){
            Log.d(" key e" , String.valueOf(e));
        }


        Document doc = new Document();
        String fileName="";
        try{

            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            fileName="Customer_List" +"_"+ sdf.format(Calendar.getInstance().getTime()) + ".pdf";
            pdfFile = new File(docsFolder.getAbsolutePath(), fileName);
            OutputStream output = new FileOutputStream(pdfFile);
            PdfWriter.getInstance(doc, output);
            doc.open();
            Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);

            PdfPCell cell1 = new PdfPCell(new Paragraph("Cell1"));

            //   table.addCell(cell1);
            try{
                PdfPTable table5 = new PdfPTable(4);
                table5.setWidthPercentage(100);
                //  float[] widths4 = new float[] { 50f, 120f, 50f, 60f, 50f, 45f, 40f, 40f};
                //   table4.setWidths(widths4);
                PdfPCell Report = new PdfPCell(new Paragraph("Report Of Sales:"));
                Report.setBorder(Rectangle.NO_BORDER);
                PdfPCell cus = new PdfPCell(new Paragraph("Customer Wise", boldFont));
                cus.setBorder(Rectangle.NO_BORDER);
                PdfPCell period = new PdfPCell(new Paragraph("Period:"));
                period.setBorder(Rectangle.NO_BORDER);
                PdfPCell perioidTime = new PdfPCell(new Paragraph("time", boldFont));
                perioidTime.setBorder(Rectangle.NO_BORDER);
                PdfPCell stat = new PdfPCell(new Paragraph("state:"));
                stat.setBorder(Rectangle.NO_BORDER);
                PdfPCell stateInfo = new PdfPCell(new Paragraph(GlobalClass.CustomerState, boldFont));
                stateInfo.setBorder(Rectangle.NO_BORDER);
                PdfPCell pin = new PdfPCell(new Paragraph("pinCode:"));
                pin.setBorder(Rectangle.NO_BORDER);
                PdfPCell pinInfo = new PdfPCell(new Paragraph(GlobalClass.CustomerPincode, boldFont));
                pinInfo.setBorder(Rectangle.NO_BORDER);

                table5.addCell(stat);
                table5.addCell(stateInfo);
                table5.addCell(pin);
                table5.addCell(pinInfo);

                table5.addCell(Report);
                table5.addCell(cus);
                table5.addCell(period);
                table5.addCell(perioidTime);

                table.addCell(table5);
            }catch(Exception e){
                Log.e("table 5", "ioException:" + e);
            }


            try{
                PdfPTable table4 = new PdfPTable(6);
                table4.setWidthPercentage(100);
                float[] widths4 = new float[] { 100f,70f , 50f, 40f, 60f, 45f};
                table4.setWidths(widths4);


                PdfPCell customerName = new PdfPCell(new Paragraph("CUSTOMER CODE"));
                PdfPCell Particular4 = new PdfPCell(new Paragraph("INV.NO"));
                PdfPCell contrib = new PdfPCell(new Paragraph("SALES(TH)"));
                PdfPCell Vouchertype4 = new PdfPCell(new Paragraph("CRD.PERIOD"));
                PdfPCell Debit4 = new PdfPCell(new Paragraph("INV.DATE"));
                PdfPCell Credit4 = new PdfPCell(new Paragraph("DUE.DATE"));
                table4.addCell(customerName);
                table4.addCell(Particular4);
                table4.addCell(contrib);
                table4.addCell(Vouchertype4);
                table4.addCell(Debit4);
                table4.addCell(Credit4);
                table.addCell(table4);
            }catch(Exception e){
                Log.e("table 4", "ioException:" + e);
            }


            try{
                PdfPTable table1 = new PdfPTable(6);
                table1.setWidthPercentage(100);
                float[] widths1 = new float[] { 100f, 70f, 50f, 40f, 60f, 60f};
                table1.setWidths(widths1);
                for(int i=0;i<value.size();i++){
                    PdfPCell BillDate1 = new PdfPCell(new Paragraph(GlobalClass.CustomerName, boldFont));
                    PdfPCell Particular1 = new PdfPCell(new Paragraph(invNo.get(i), boldFont));

                    PdfPCell PaymentTerm1 = new PdfPCell(new Paragraph(String.valueOf(value.get(i)), boldFont));

                    PdfPCell VchNo1 = new PdfPCell(new Paragraph(cre.get(i), boldFont));
                    PdfPCell Debit1 = new PdfPCell(new Paragraph(date.get(i), boldFont));
                    PdfPCell Credit1 = new PdfPCell(new Paragraph(date.get(i), boldFont));
                    table1.addCell(BillDate1);
                    table1.addCell(Particular1);

                    table1.addCell(PaymentTerm1);

                    table1.addCell(VchNo1);
                    table1.addCell(Debit1);
                    table1.addCell(Credit1);
                }

                table.addCell(table1);
            }catch(Exception e){
                Log.e("table 1", "ioException:" + e);
            }
            try{
                int total=0;
for(int j=0; j<value.size();j++){
    total=total+value.get(j);
}
                Log.e("table 1", "val:" + "total");
                PdfPTable table2 = new PdfPTable(6);
                table2.setWidthPercentage(100);
                float[] widths2 = new float[] { 100f, 70f, 50f, 40f, 60f, 60f};
                table2.setWidths(widths2);
                //  PdfPCell BillDate2 = new PdfPCell(new Paragraph(""));
                PdfPCell Particular2 = new PdfPCell(new Paragraph("Total", boldFont));
                Particular2.setColspan(2);
                PdfPCell DueDate2 =  new PdfPCell(new Paragraph((String.valueOf(total)), boldFont));
                PdfPCell VchNo2 = new PdfPCell(new Paragraph(""));
                PdfPCell Debit2 = new PdfPCell(new Paragraph(""));
                PdfPCell Credit2 = new PdfPCell(new Paragraph(""));
                //table2.addCell(BillDate2);
                table2.addCell(Particular2);
                table2.addCell(DueDate2);

                table2.addCell(VchNo2);
                table2.addCell(Debit2);
                table2.addCell(Credit2);
                table.addCell(table2);
            }catch(Exception e){
                Log.e("table 2", "ioException:" + e);
            }
//            try{
//                PdfPTable table3 = new PdfPTable(6);
//                table3.setWidthPercentage(100);
//                float[] widths3 = new float[] { 50f, 120f, 60f, 60f, 50f, 45f};
//                table3.setWidths(widths3);
//                PdfPCell BillDate3 = new PdfPCell(new Paragraph("Closing Bal Formula: "+"\n"+"Op bal + Dr Total - Cr Total= Cl Bal"));
//                BillDate3.setColspan(2);
//                //    PdfPCell Particular3 = new PdfPCell(new Paragraph(""));
//                PdfPCell DueDate3 = new PdfPCell(new Paragraph(""));
//                PdfPCell PaymentTerm3 = new PdfPCell(new Paragraph("Closing Bal"));
//                PdfPCell Debit3 = new PdfPCell(new Paragraph(String.valueOf("total")));
//                PdfPCell Credit3 = new PdfPCell(new Paragraph(""));
//                table3.addCell(BillDate3);
//                //   table3.addCell(Particular3);
//                table3.addCell(DueDate3);
//                table3.addCell(PaymentTerm3);
//                table3.addCell(Debit3);
//                table3.addCell(Credit3);
//                table.addCell(table3);
//            }catch(Exception e){
//                Log.e("table 3", "ioException:" + e);
//            }
            doc.add(table);
            Toast.makeText(context, "Created PDF in CBatDownload Folder", Toast.LENGTH_LONG).show();
            doc.close();
        }catch(Exception e){
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }
        return fileName;
    }
    public String invoiceDetailsPdf(File docsFolder,List<JSONObject> customerInvoiceFullList)  {

       Log.d("customerInvoiceFullList",customerInvoiceFullList.toString());
        //use to set background color
        BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
        BaseColor myColor1 = WebColors.getRGBColor("#264553");
        BaseColor colorWhite = WebColors.getRGBColor("#FFFFFF");
        Font font = new Font();
        font.setColor(colorWhite);
        Document doc = new Document();
        String fileName="";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
             fileName=GlobalClass.title+"_"+ sdf.format(Calendar.getInstance().getTime()) + ".pdf";
            // String fileName="Customer_Details_"+GlobalClass.startDate+"_"+GlobalClass.endDate+".pdf";
            pdfFile = new File(docsFolder.getAbsolutePath(), fileName);
            OutputStream output = new FileOutputStream(pdfFile);
            PdfWriter.getInstance(doc, output);
            doc.open();

            //  document.add(new Paragraph(mContentEditText.getText().toString()));
//create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = context.getResources().getDrawable(R.drawable.logo);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();

            bgImage = Image.getInstance(bitmapdata);
            bgImage.setAbsolutePosition(330f, 642f);
            cell.addElement(bgImage);
            pt.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.addElement(new Paragraph(GlobalClass.title+" from "+GlobalClass.startDate+" to "+GlobalClass.endDate));

            cell.addElement(new Paragraph(""));
            cell.addElement(new Paragraph(""));
            pt.addCell(cell);
            cell = new PdfPCell(new Paragraph(""));
            cell.setBorder(Rectangle.NO_BORDER);
            pt.addCell(cell);

            PdfPTable pTable = new PdfPTable(1);
            pTable.setWidthPercentage(100);
            cell = new PdfPCell();
            cell.setColspan(1);
            cell.addElement(pt);
            pTable.addCell(cell);

            int noOfDataCell=7;
            PdfPTable table = new PdfPTable(noOfDataCell);




            float[] columnWidth = new float[]{10,40, 40, 30,30,30,30};
            table.setWidths(columnWidth);


            cell = new PdfPCell();


            cell.setBackgroundColor(myColor);
            cell.setColspan(noOfDataCell);
            cell.addElement(pTable);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" "));
            cell.setColspan(noOfDataCell);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);

            cell.setBackgroundColor(myColor1);


            /**
             *         new ItemSortable("Invoice No"),
             *             new ItemSortable("Sales"),
             *                     new ItemSortable("Invoice Date"),
             *                  new ItemSortable("Due Date"),
             *                  new ItemSortable("Credit Period"),
             *                  new ItemSortable("Arrears Days"),
             *                  new ItemSortable("Customer Code")
             */

            cell = new PdfPCell(new Phrase("Sr.No",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
//            cell = new PdfPCell(new Phrase("Customer",font));
//            cell.setBackgroundColor(myColor1);
//            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Invoice No",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Sales",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Invoice Date",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Due Date",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Credit Period",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Arrears Days",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);

            //table.setHeaderRows(3);
            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);
            int count=1;
            double totalSale=0;
            for (int i = 0; i < customerInvoiceFullList.size(); i++) {
                try {
                    JSONObject element = customerInvoiceFullList.get(i);
                    int yVal = element.getInt("value");
                    BigDecimal number1 = new BigDecimal(yVal);
                    float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);

                    String dueDate = element.getString("dueDate");
                    // float creditPeroid=(new BigDecimal(element.getInt("credirPeroid"))).floatValue()/ GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                    String creditPeroidStr = String.valueOf(element.getInt("credirPeroid"));
                    int arrearDaysInt = element.getInt("arrearDays");
                    String arrearDays = String.valueOf(arrearDaysInt);
                    Log.d(TAG, "debitFloat >" + debitFloat);
                    Log.d(TAG, "dueDate >" + dueDate);
                    Log.d(TAG, "creditPeroid >" + creditPeroidStr);
                    Log.d(TAG, "arrearDays >" + arrearDays);
                    String indicatorColor = "";
                    Font font1 = new Font();
                    if (arrearDaysInt < 0) {
                       // indicatorColor = "#FF0000";
                        BaseColor colorRed = WebColors.getRGBColor("#FF0000");
                        font1.setColor(colorRed);
                    } else {
                        //indicatorColor = "#008000";
                        BaseColor colorGreen = WebColors.getRGBColor("#008000");
                        font1.setColor(colorGreen);
                    }
                    table.addCell(String.valueOf(count++));
                    table.addCell(element.getString("name"));
                    table.addCell(Utility.decimal2Palce(Math.abs(debitFloat)));
                    table.addCell(element.getString("voucherDate"));
                    table.addCell(dueDate);
                    table.addCell(creditPeroidStr);
                    table.addCell(new Phrase(arrearDays,font1));
                   // String[] data = new String[]{GlobalClass.currentFrgmentMain, element.getString("name"), Utility.decimal2Palce(Math.abs(debitFloat)), element.getString("voucherDate"), dueDate, creditPeroidStr, arrearDays, element.getString("voucherGuid"), "", "", "", "", "", ""};
                   // items.add(new NexusWithImage(type, data, indicatorColor));
                }catch(Exception e){

                }
            }


            PdfPTable ftable = new PdfPTable(noOfDataCell);
            ftable.setWidthPercentage(100);
            float[] columnWidthaa = new float[]{50,20, 50, 30,30,30,30  };
            ftable.setWidths(columnWidthaa);
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBackgroundColor(myColor1);
            //cell = new PdfPCell(new Phrase("Total Sales",font));

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);


//

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

          //  cell = new PdfPCell(new Phrase(Utility.decimal2Palce(Math.abs(totalSales)),font));
            cell = new PdfPCell(new Phrase(""));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);

//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);

//
//            cell = new PdfPCell(new Paragraph("This is System Generate PDF."));
//            cell.setColspan(noOfDataCell);
//            ftable.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);

            cell.addElement(ftable);

            table.addCell(cell);

            doc.add(table);
            Toast.makeText(context, "Created PDF in CBatDownload Folder", Toast.LENGTH_LONG).show();
            doc.close();


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }

        return fileName;
        //  previewPdf();

    }

   public String  ledgerDetailsNewPdf(File docsFolder,List<JSONObject> customerInvoiceSummaryList){
       Document doc = new Document();
       String fileName="";
try{
    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    fileName=GlobalClass.title+"_"+ sdf.format(Calendar.getInstance().getTime()) + ".pdf";
    pdfFile = new File(docsFolder.getAbsolutePath(), fileName);
    OutputStream output = new FileOutputStream(pdfFile);
    PdfWriter.getInstance(doc, output);
    doc.open();
    Log.d("check Gaurav", customerInvoiceSummaryList.toString());

    int count=1;
    double totalSale=0;
    float totalAmount=0;
    boolean vocherFlag=true;
    String VoucherNo="";
    String companyName="";
    String state="";
    String voucherType="";
    String legderDateT="";
    String gstno="";
    String pin="";
    String creditPeroid="";
    String[] date = new String[8];
    ArrayList<String> item=new ArrayList<String>();
    ArrayList<String> quantity=new ArrayList<String>();
    ArrayList<String> rate=new ArrayList<String>();
    ArrayList<Double> amount=new ArrayList<Double>();
try{
    for(int i=1;i < customerInvoiceSummaryList.size(); i++){
        JSONObject element = customerInvoiceSummaryList.get(i);
        gstno=element.getString("partygstin");
        pin=element.getString("pincode");
        creditPeroid=element.getString("creditPeroid");
    }
}catch(Exception e){
    Log.d("gstno", e.toString());
}


    for (int i = 0; i < customerInvoiceSummaryList.size()-1; i++) {
        try {
            JSONObject element = customerInvoiceSummaryList.get(i);
            item.add(element.getString("name"));
            quantity.add(element.getString("quantity"));
            rate.add(element.getString("rate"));
            amount.add(element.getDouble("amount"));
            JSONObject element1 = element.getJSONObject("voucher");
            VoucherNo=element1.getString("voucherNo");
            companyName=element1.getString("name");
            state=element1.getString("state");
            legderDateT=element1.getString("date");
            date=legderDateT.split("T");
            Log.d("ch date ", String.valueOf(date[0]));
            voucherType=element1.getString("voucherType");
//            int yVal = element.getInt("amount");
//            BigDecimal number1 = new BigDecimal(yVal);
//            float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
//            Log.d(TAG, "debitFloat >" + debitFloat);
//            totalAmount = totalAmount + debitFloat;
//            if (vocherFlag) {
//                JSONObject voucher = element.getJSONObject("voucher");
//                String customerNameT = voucher.getString("accountName");
//                String legderDateT = voucher.getString("date");
//                String address = voucher.getString("address");
//                String[] date = legderDateT.split("T");
//                String ledgerTypeT = voucher.getString("voucherType");
//                String ledgerRefT = voucher.getString("refNo");
//                String stateT = voucher.getString("state");
//                vocherFlag = false;
//            }
            }catch (Exception e){
                e.printStackTrace();
            }
            // items.add(new NexusWithImage(type, element.getString("name"),  Utility.decimal2Palce(Math.abs(debitFloat )),element.getString("quantity") , element.getString("rate"), "", "", "","","","","","","",""));
        }
    PdfPTable table = new PdfPTable(1);
    table.getDefaultCell().setBorder(0);
    table.setWidthPercentage(100f);
    Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    PdfPTable table1 = new PdfPTable(2);
    PdfPCell Invoice = new PdfPCell(new Paragraph("Summary of Voucher" +" "+VoucherNo, boldFont));
    Invoice.setBorder(Rectangle.NO_BORDER);
    Invoice.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(Invoice);
    PdfPCell cell1 = new PdfPCell(new Paragraph("Cell 2"));
    PdfPCell cell2 = new PdfPCell(new Paragraph("Cell 2"));
    PdfPCell cell3 = new PdfPCell(new Paragraph("Cell 3"));
    PdfPCell cell4 = new PdfPCell(new Paragraph("Cell 4"));
    PdfPCell cell5 = new PdfPCell(new Paragraph("Cell 5"));

    PdfPCell cell6 = new PdfPCell(new Paragraph("Cell 6"));
    PdfPCell cell7 = new PdfPCell(new Paragraph("Cell 7"));
    cell6.setFixedHeight(300f);
    cell7.setFixedHeight(300f);


    PdfPTable table2 = new PdfPTable(1);
    PdfPCell cell8 = new PdfPCell(new Paragraph("Cell 8"));
    PdfPCell cell9 = new PdfPCell(new Paragraph("Cell 9"));
    cell8.setFixedHeight(100f);
    cell9.setFixedHeight(200f);

    PdfPTable table7 = new PdfPTable(2);

    PdfPCell Cmpny = new PdfPCell(new Paragraph(companyName, boldFont));
    PdfPCell info = new PdfPCell(new Paragraph(""));
    PdfPCell GST = new PdfPCell(new Paragraph("Company GSTIN/UIN: "));
    PdfPCell GSTinfo = new PdfPCell(new Paragraph(gstno, boldFont));
    GST.setFixedHeight(80f);
    Cmpny.setBorder(Rectangle.NO_BORDER);
    info.setBorder(Rectangle.NO_BORDER);
    GST.setBorder(Rectangle.NO_BORDER);
    GSTinfo.setBorder(Rectangle.NO_BORDER);

    table7.addCell(Cmpny);
    table7.addCell(info);
    table7.addCell(GST);
    table7.addCell(GSTinfo);

    table2.addCell(table7);


    PdfPTable table9 = new PdfPTable(2);

    PdfPCell buyer = new PdfPCell(new Paragraph("Buyers", boldFont));
    PdfPCell buyersName = new PdfPCell(new Paragraph(""));
    PdfPCell state1 = new PdfPCell(new Paragraph("State Name"));
    PdfPCell statename = new PdfPCell(new Paragraph(state, boldFont));
    PdfPCell pinCode = new PdfPCell(new Paragraph("pinCode"));
    PdfPCell pinCodeInfo = new PdfPCell(new Paragraph(pin, boldFont));
    GST.setFixedHeight(80f);
    buyer.setBorder(Rectangle.NO_BORDER);
    buyersName.setBorder(Rectangle.NO_BORDER);
    state1.setBorder(Rectangle.NO_BORDER);
    statename.setBorder(Rectangle.NO_BORDER);
    pinCode.setBorder(Rectangle.NO_BORDER);
    pinCodeInfo.setBorder(Rectangle.NO_BORDER);

    table9.addCell(buyer);
    table9.addCell(buyersName);
    table9.addCell(state1);
    table9.addCell(statename);
    table9.addCell(pinCode);
    table9.addCell(pinCodeInfo);

   // table2.addCell(table7);
    table2.addCell(table9);


    table1.addCell(table2);

    PdfPTable table3 = new PdfPTable(1);
    PdfPCell cell10 = new PdfPCell(new Paragraph("Cell 10"));
    PdfPCell cell11 = new PdfPCell(new Paragraph("Term of Deleivery"));
    cell10.setFixedHeight(200f);
    cell11.setFixedHeight(100f);

    PdfPTable table4 = new PdfPTable(2);
    PdfPCell InvoiceNo = new PdfPCell(new Paragraph("InvoiceNo" +"\n"));
    PdfPCell Date = new PdfPCell(new Paragraph("Dated" +"\n"+ date[0], boldFont));
    PdfPCell DelieveryNote = new PdfPCell(new Paragraph("DelieveryNote"+"\n"+ " "));
    PdfPCell Mode = new PdfPCell(new Paragraph("Mode/ Term of Payment"+"\n"+creditPeroid+" "+"days", boldFont));

    PdfPCell Supplier = new PdfPCell(new Paragraph("Supplier's Ref."+"\n"+" "));
    PdfPCell other = new PdfPCell(new Paragraph("Other Ref(s)"+"\n" +" "));

    PdfPCell buyers = new PdfPCell(new Paragraph("Buyer's Order No"+"\n"+" "));
    PdfPCell Dated = new PdfPCell(new Paragraph("Dated"+"\n"));

    PdfPCell dispatch = new PdfPCell(new Paragraph("Dispatch Doc. No"+"\n"+" "));
    PdfPCell delievry = new PdfPCell(new Paragraph("Delievry Note Date"+"\n" +" "));

    PdfPCell dispatchthrough = new PdfPCell(new Paragraph("Dispatch Through"+"\n"+" "));
    PdfPCell destination = new PdfPCell(new Paragraph("Destination "+"\n"+" "));

    table4.addCell(InvoiceNo);
    table4.addCell(Date);
    table4.addCell(DelieveryNote);
    table4.addCell(Mode);
    table4.addCell(Supplier);
    table4.addCell(other);
    table4.addCell(buyers);
    table4.addCell(Dated);
    table4.addCell(dispatch);
    table4.addCell(delievry);
    table4.addCell(dispatchthrough);
    table4.addCell(destination);

    table3.addCell(table4);
    table3.addCell(cell11);

    table1.addCell(table3);
    table.addCell(table1);



    PdfPTable table5 = new PdfPTable(8);
    float[] widths = new float[] { 15f, 80f, 40f, 35f, 35f, 50f, 25f, 65f};
    table5.setWidths(widths);

        PdfPCell SNO = new PdfPCell(new Paragraph("S.NO"));
        PdfPCell Des = new PdfPCell(new Paragraph("Description of Goods" ));
        PdfPCell HSN = new PdfPCell(new Paragraph("HSN/NAC"));
        PdfPCell Shipped = new PdfPCell(new Paragraph("Shipped"));
        PdfPCell Billed = new PdfPCell(new Paragraph("Billed"));

        PdfPCell Rate = new PdfPCell(new Paragraph("Rate"));
        PdfPCell Per = new PdfPCell(new Paragraph("Per"));
        PdfPCell Amount = new PdfPCell(new Paragraph("Amount"));

        table5.addCell(SNO);
        table5.addCell(Des);
        table5.addCell(HSN);
        table5.addCell(Shipped);
        table5.addCell(Billed);
        table5.addCell(Rate);
        table5.addCell(Per);
        table5.addCell(Amount);

    table.addCell(table5);

    PdfPTable table6 = new PdfPTable(8);
    float[] widths1 = new float[] { 15f, 80f, 40f, 35f, 35f, 50f, 25f, 65f};
    table6.setWidths(widths1);
int count1=1;
    for(int i=0; i<item.size(); i++) {

    PdfPCell SNO1 = new PdfPCell(new Paragraph(count1+"\n", boldFont));
        SNO1.setBorder( Rectangle.LEFT);
    PdfPCell Des1 = new PdfPCell(new Paragraph(item.get(i)+"\n", boldFont));
        Des1.setBorder(Rectangle.LEFT);
    PdfPCell HSN1 = new PdfPCell(new Paragraph(""));
        HSN1.setBorder(Rectangle.LEFT);
    PdfPCell Shipped1 = new PdfPCell(new Paragraph(quantity.get(i)+"\n", boldFont));
        Shipped1.setBorder(Rectangle.LEFT);
    PdfPCell Billed1 = new PdfPCell(new Paragraph(quantity.get(i)+"\n", boldFont));
        Billed1.setBorder(Rectangle.LEFT);
    PdfPCell Rate1 = new PdfPCell(new Paragraph(rate.get(i)+"\n", boldFont));
        Rate1.setBorder(Rectangle.LEFT);
    PdfPCell Per1 = new PdfPCell(new Paragraph(""));
        Per1.setBorder(Rectangle.LEFT);
    PdfPCell Amount1 = new PdfPCell(new Paragraph(String.valueOf(amount.get(i))+"\n", boldFont));
        Amount1.setBorder(Rectangle.LEFT);
    table6.addCell(SNO1);
    table6.addCell(Des1);
    table6.addCell(HSN1);
    table6.addCell(Shipped1);
    table6.addCell(Billed1);
    table6.addCell(Rate1);
    table6.addCell(Per1);
    table6.addCell(Amount1);


        count1=count1+1;
    }
   // SNO1.setFixedHeight(200f);
    table.addCell(table6);

    PdfPTable table8 = new PdfPTable(8);
    float[] widths2 = new float[] { 15f, 80f, 40f, 35f, 35f, 50f, 25f, 65f};
    table8.setWidths(widths2);
    double yVal=0.0;
    BigDecimal number1;
    float debitFloat;
    Float totalAmount1 = null;
    for(int i=0; i<amount.size(); i++) {
        yVal =yVal+ amount.get(i);
        Log.d(TAG, "yVal >" + yVal);

    }

    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
    String moneyString = formatter.format(yVal);
    System.out.print(moneyString+"moneyString");
    Locale locale = new Locale("en","IN");
    DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale);
    DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(locale);
    dfs.setCurrencySymbol("\u20B9");
    decimalFormat.setDecimalFormatSymbols(dfs);
    String total=decimalFormat.format(yVal);
    System.out.println(decimalFormat.format(yVal));
    System.out.println(total+"hmhj");
        PdfPCell SNO1 = new PdfPCell(new Paragraph(""));

        PdfPCell Des1 = new PdfPCell(new Paragraph("Total", boldFont));

        PdfPCell HSN1 = new PdfPCell(new Paragraph(""));

        PdfPCell Shipped1 = new PdfPCell(new Paragraph(""));

        PdfPCell Billed1 = new PdfPCell(new Paragraph(""));

        PdfPCell Rate1 = new PdfPCell(new Paragraph(""));

        PdfPCell Per1 = new PdfPCell(new Paragraph(""));

        PdfPCell Amount1 = new PdfPCell(new Paragraph(moneyString,boldFont));
    Amount1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table8.addCell(SNO1);
        table8.addCell(Des1);
        table8.addCell(HSN1);
        table8.addCell(Shipped1);
        table8.addCell(Billed1);
        table8.addCell(Rate1);
        table8.addCell(Per1);
        table8.addCell(Amount1);

    table.addCell(table8);

    try{
        int val=Integer.parseInt(String.valueOf(yVal));
        Log.e("words", "val:" + val);
    //    String words=convertNumberToWords(val);
      //  Log.d("words", words);
    }catch (Exception e){
        Log.e("words", "Convert:" + e);
    }

    PdfPTable table10 = new PdfPTable(2);
    PdfPCell amt = new PdfPCell(new Paragraph("Chargable amount"+ "\n "));
    PdfPCell amt1 = new PdfPCell(new Paragraph("E. & O.E", boldFont));
    amt1.setHorizontalAlignment(Element.ALIGN_RIGHT);
    table10.addCell(amt);
    table10.addCell(amt1);
    table.addCell(table10);
    PdfPTable table11 = new PdfPTable(5);
    float[] widths3 = new float[] {  100f, 40f, 65f, 65f, 40f };
    table11.setWidths(widths3);
    PdfPCell SAC = new PdfPCell(new Paragraph("HSN/SAC"));
    SAC.setHorizontalAlignment(Element.ALIGN_CENTER);
    PdfPCell tax = new PdfPCell(new Paragraph("Taxable Value"));
    PdfPCell centerTax = new PdfPCell(new Paragraph("Center Tax"));
    PdfPCell stateTax = new PdfPCell(new Paragraph("State Tax"));
    PdfPCell totalTax = new PdfPCell(new Paragraph("Total Tax Amount"));
    table11.addCell(SAC);
    table11.addCell(tax);

    PdfPTable table12 = new PdfPTable(1);
    PdfPCell centerTax1 = new PdfPCell(new Paragraph("Center Tax"));
    centerTax1.setHorizontalAlignment(Element.ALIGN_CENTER);
    centerTax1.setBorder(Rectangle.NO_BORDER);
    PdfPCell rateAmount = new PdfPCell(new Paragraph("Center Tax"));
    table12.addCell(centerTax1);
    PdfPTable table13 = new PdfPTable(2);
    PdfPCell centerRate = new PdfPCell(new Paragraph("Rate"));
    PdfPCell centerAmount = new PdfPCell(new Paragraph("Amount"));
//    centerRate.setBorder(Rectangle.NO_BORDER);
//    centerAmount.setBorder(Rectangle.NO_BORDER);
    table13.addCell(centerRate);
    table13.addCell(centerAmount);

    table12.addCell(table13);
    table11.addCell(table12);

    PdfPTable table14 = new PdfPTable(1);
    PdfPCell stateTax1 = new PdfPCell(new Paragraph("State Tax"));
    stateTax1.setHorizontalAlignment(Element.ALIGN_CENTER);
    PdfPCell stateTax2 = new PdfPCell(new Paragraph("Center Tax"));
    stateTax1.setBorder(Rectangle.NO_BORDER);
    table14.addCell(stateTax1);
    PdfPTable table15 = new PdfPTable(2);
    PdfPCell stateRate = new PdfPCell(new Paragraph("Rate"));
    PdfPCell sateAmount = new PdfPCell(new Paragraph("Amount"));
//    stateRate.setBorder(Rectangle.NO_BORDER);
//    sateAmount.setBorder(Rectangle.NO_BORDER);
    table15.addCell(stateRate);
    table15.addCell(sateAmount);
    table14.addCell(table15);


    table11.addCell(table14);
    table11.addCell(totalTax);
    table.addCell(table11);
    PdfPCell cell12 = new PdfPCell(new Paragraph("cell12"));
try{
    PdfPTable table16 = new PdfPTable(7);
    float[] widths4 = new float[] {  100f, 40f, 35f, 35f, 35f, 35f, 40f };
    table16.setWidths(widths4);
    PdfPCell SAC1 = new PdfPCell(new Paragraph("32151"));
    SAC1.setBorder( Rectangle.LEFT);
    PdfPCell Tax1 = new PdfPCell(new Paragraph("20000.00"));
    Tax1.setBorder( Rectangle.LEFT);
    PdfPCell rate1 = new PdfPCell(new Paragraph("6%"));
    rate1.setBorder( Rectangle.LEFT);
    PdfPCell amount1 = new PdfPCell(new Paragraph("1200"));
    amount1.setBorder( Rectangle.LEFT);
    PdfPCell rate2 = new PdfPCell(new Paragraph("6%"));
    rate2.setBorder( Rectangle.LEFT);
    PdfPCell amount2 = new PdfPCell(new Paragraph("1200"));
    amount2.setBorder( Rectangle.LEFT);
    PdfPCell tataltax1 = new PdfPCell(new Paragraph("2400"));
    tataltax1.setBorder( Rectangle.LEFT);
    table16.addCell(SAC1);
    table16.addCell(Tax1);
    table16.addCell(rate1);
    table16.addCell(amount1);
    table16.addCell(rate2);
    table16.addCell(amount2);
    table16.addCell(tataltax1);
    table.addCell(table16);

}catch(Exception e){
    Log.d("table 16", "table16"+e);
}
    try{
        PdfPTable table17 = new PdfPTable(7);
        float[] widths5 = new float[] {  100f, 40f, 35f, 35f, 35f, 35f, 40f };
        table17.setWidths(widths5);
        PdfPCell SAC1 = new PdfPCell(new Paragraph("Total", boldFont));
    //    SAC1.setBorder( Rectangle.LEFT);
        SAC1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell Tax1 = new PdfPCell(new Paragraph("20000.00", boldFont));
     //   Tax1.setBorder( Rectangle.LEFT);
        PdfPCell rate1 = new PdfPCell(new Paragraph(""));
       // rate1.setBorder( Rectangle.LEFT);
        PdfPCell amount1 = new PdfPCell(new Paragraph("1200",boldFont));
       // amount1.setBorder( Rectangle.LEFT);
        PdfPCell rate2 = new PdfPCell(new Paragraph(""));
        //rate2.setBorder( Rectangle.LEFT);
        PdfPCell amount2 = new PdfPCell(new Paragraph("1200", boldFont));
      //  amount2.setBorder( Rectangle.LEFT);
        PdfPCell tataltax1 = new PdfPCell(new Paragraph("2400", boldFont));
    //    tataltax1.setBorder( Rectangle.LEFT);
        table17.addCell(SAC1);
        table17.addCell(Tax1);
        table17.addCell(rate1);
        table17.addCell(amount1);
        table17.addCell(rate2);
        table17.addCell(amount2);
        table17.addCell(tataltax1);
        table.addCell(table17);

    }catch(Exception e){
        Log.d("table 16", "table16"+e);
    }
try{
    PdfPTable table19= new PdfPTable(2);
    PdfPCell taxAmount = new PdfPCell(new Paragraph("Tax Amount (in Words)"));
    PdfPCell words = new PdfPCell(new Paragraph("INR"+ "Two Thousand Four Hundred Only", boldFont));
    taxAmount.setBorder( Rectangle.NO_BORDER);
    words.setBorder( Rectangle.NO_BORDER);
    words.setHorizontalAlignment(Element.ALIGN_LEFT);
    table19.addCell(taxAmount);
    table19.addCell(words);
    table.addCell(table19);
}catch(Exception e){
    Log.d("table 16", "table16"+e);
}

    try{
        PdfPTable table20 = new PdfPTable(2);
        PdfPCell CompanyGST = new PdfPCell(new Paragraph("Company's GSTIN/UIN"));
        CompanyGST.setBorder( Rectangle.NO_BORDER);
        PdfPCell CompanyGSTNO = new PdfPCell(new Paragraph(gstno, boldFont));
        CompanyGSTNO.setBorder( Rectangle.NO_BORDER);
        CompanyGSTNO.setHorizontalAlignment(Element.ALIGN_LEFT);
        PdfPCell Dec = new PdfPCell(new Paragraph("Declaration:"+ "\n"+"We declare that this invoice show the actual price "+ "\n"+ "of the good descibed and all particular are true and Correct"));
        Dec.setBorder( Rectangle.NO_BORDER);
        PdfPCell sign = new PdfPCell(new Paragraph("for"+companyName+"\n\n\n"+"Authorised Signatory", boldFont));
        sign.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table20.addCell(CompanyGST);
        table20.addCell(CompanyGSTNO);
        table20.addCell(Dec);
        table20.addCell(sign);
        table.addCell(table20);

    }catch(Exception e){
        Log.d("table 20", "table16"+e);
    }

    try{
        PdfPTable table18 = new PdfPTable(1);
        PdfPCell generated = new PdfPCell(new Paragraph("This is a Computer Generated Invoice", boldFont));
        generated.setBorder( Rectangle.NO_BORDER);
        generated.setHorizontalAlignment(Element.ALIGN_CENTER);
        table18.addCell(generated);
        table.addCell(table18);
    }catch(Exception e){
        Log.d("table 18", "table 18"+e);
    }

    doc.add(table);
    Toast.makeText(context, "Created PDF in CBatDownload Folder", Toast.LENGTH_LONG).show();
    doc.close();

}catch (DocumentException de) {
    Log.e("PDFCreator", "DocumentException:" + de);
} catch (IOException e) {
    Log.e("PDFCreator", "ioException:" + e);
} finally {
    doc.close();
}

       return fileName;
    }

    public String ledgerDetailsPdf(File docsFolder,List<JSONObject> customerInvoiceSummaryList)  {

        //use to set background color
        BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
        BaseColor myColor1 = WebColors.getRGBColor("#264553");
        BaseColor colorWhite = WebColors.getRGBColor("#FFFFFF");
        Font font = new Font();
        font.setColor(colorWhite);
        Document doc = new Document();
        String fileName="";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
             fileName=GlobalClass.title+"_" + sdf.format(Calendar.getInstance().getTime()) + ".pdf";
            // String fileName="Customer_Details_"+GlobalClass.startDate+"_"+GlobalClass.endDate+".pdf";
            pdfFile = new File(docsFolder.getAbsolutePath(), fileName);
            OutputStream output = new FileOutputStream(pdfFile);
            PdfWriter.getInstance(doc, output);
            doc.open();


//create table


            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = context.getResources().getDrawable(R.drawable.logo);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();

            bgImage = Image.getInstance(bitmapdata);
            bgImage.setAbsolutePosition(330f, 642f);
            cell.addElement(bgImage);
            pt.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.addElement(new Paragraph(GlobalClass.title));

            cell.addElement(new Paragraph(""));
            cell.addElement(new Paragraph(""));
            pt.addCell(cell);
            cell = new PdfPCell(new Paragraph(""));
            cell.setBorder(Rectangle.NO_BORDER);
            pt.addCell(cell);

            PdfPTable pTable = new PdfPTable(1);
            pTable.setWidthPercentage(100);
            cell = new PdfPCell();
            cell.setColspan(1);
            cell.addElement(pt);
            pTable.addCell(cell);

            int noOfDataCell=5;
            PdfPTable table = new PdfPTable(noOfDataCell);




            float[] columnWidth = new float[]{10,40, 40, 30,30};
            table.setWidths(columnWidth);


            cell = new PdfPCell();


            cell.setBackgroundColor(myColor);
            cell.setColspan(noOfDataCell);
            cell.addElement(pTable);

            table.addCell(cell);

//            cell = new PdfPCell(new Phrase(" "));
//            cell.setColspan(noOfDataCell);
//            table.addCell(cell);

//            cell = new PdfPCell();
//            cell.setColspan(noOfDataCell);
//
//            cell.setBackgroundColor(myColor1);


            /**
             *         new ItemSortable("Invoice No"),
             *             new ItemSortable("Sales"),
             *                     new ItemSortable("Invoice Date"),
             *                  new ItemSortable("Due Date"),
             *                  new ItemSortable("Credit Period"),
             *                  new ItemSortable("Arrears Days"),
             *                  new ItemSortable("Customer Code")
             */

//            cell = new PdfPCell(new Phrase("Sr.No",font));
//            cell.setBackgroundColor(myColor1);
//            table.addCell(cell);
//            cell = new PdfPCell(new Phrase("Item",font));
//            cell.setBackgroundColor(myColor1);
//            table.addCell(cell);
//            cell = new PdfPCell(new Phrase("Amount",font));
//            cell.setBackgroundColor(myColor1);
//            table.addCell(cell);
//            cell = new PdfPCell(new Phrase("Quantity",font));
//            cell.setBackgroundColor(myColor1);
//            table.addCell(cell);
//            cell = new PdfPCell(new Phrase("Rate",font));
//            cell.setBackgroundColor(myColor1);
//            table.addCell(cell);

            //table.setHeaderRows(3);
            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);
            int count=1;
            double totalSale=0;
            float totalAmount=0;
            boolean vocherFlag=true;
            for (int i = 0; i < customerInvoiceSummaryList.size(); i++) {
                try {
                    JSONObject element = customerInvoiceSummaryList.get(i);
                    int yVal = element.getInt("amount");
                    BigDecimal number1 = new BigDecimal(yVal);
                    float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                    Log.d(TAG, "debitFloat >" + debitFloat);
                    totalAmount = totalAmount + debitFloat;
                    if (vocherFlag) {
                        JSONObject voucher = element.getJSONObject("voucher");
                        String customerNameT = voucher.getString("accountName");
                        String legderDateT = voucher.getString("date");
                        String address = voucher.getString("address");
                        String[] date = legderDateT.split("T");
                        String ledgerTypeT = voucher.getString("voucherType");
                        String ledgerRefT = voucher.getString("refNo");
                        String stateT = voucher.getString("state");

//                        customerName.setText(customerNameT);
//                        customerAddress.setText(address);
//                        customerState.setText(stateT);
//                        legderDate.setText(date.length > 0 ? date[0] : legderDateT);
//                        ledgerType.setText(" | Type# - " + ledgerTypeT);
//                        ledgerRef.setText("Ref# - " + ledgerRefT);
                        vocherFlag = false;

                        ///Details
                        PdfPTable ptd = new PdfPTable(2);
                        ptd.setWidthPercentage(100);
                        float[] fld = new float[]{20, 45};
                        ptd.setWidths(fld);
                        cell = new PdfPCell(new Phrase(customerNameT));
                        cell.setBorder(Rectangle.NO_BORDER);
                        ptd.addCell(cell);

                        cell = new PdfPCell(new Phrase(address));
                        cell.setBorder(Rectangle.NO_BORDER);
                        ptd.addCell(cell);

                        cell = new PdfPCell();
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.addElement(new Paragraph(stateT));

                        cell = new PdfPCell();
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.addElement(new Paragraph(date.length > 0 ? date[0] : legderDateT));

                        cell = new PdfPCell();
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.addElement(new Paragraph("Type# - " + ledgerTypeT));
                        ptd.addCell(cell);

                        cell = new PdfPCell();
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.addElement(new Paragraph("Ref# - " + ledgerRefT));
                        ptd.addCell(cell);

                        cell = new PdfPCell();
                        cell.setBackgroundColor(colorWhite);
                        cell.setColspan(noOfDataCell);
                        cell.addElement(ptd);

                        table.addCell(cell);

                        ///Table Item
                        cell = new PdfPCell(new Phrase(" "));
                        cell.setColspan(noOfDataCell);
                        table.addCell(cell);

                        cell = new PdfPCell();
                        cell.setColspan(noOfDataCell);
                        cell = new PdfPCell(new Phrase("Sr.No",font));
                        cell.setBackgroundColor(myColor1);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("Item",font));
                        cell.setBackgroundColor(myColor1);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("Amount",font));
                        cell.setBackgroundColor(myColor1);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("Quantity",font));
                        cell.setBackgroundColor(myColor1);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("Rate",font));
                        cell.setBackgroundColor(myColor1);
                        table.addCell(cell);
                    }
                    table.addCell(String.valueOf(count++));
                    table.addCell(element.getString("name"));
                    table.addCell(Utility.decimal2Palce(Math.abs(debitFloat))+" "+GlobalClass.currecyPrefix);
                    table.addCell(element.getString("quantity"));
                    table.addCell(element.getString("rate"));
                }catch (Exception e){
                    e.printStackTrace();
                }
               // items.add(new NexusWithImage(type, element.getString("name"),  Utility.decimal2Palce(Math.abs(debitFloat )),element.getString("quantity") , element.getString("rate"), "", "", "","","","","","","",""));
            }
            //items.add(new NexusWithImage(type, "Total",  Utility.decimal2Palce(Math.abs(totalAmount )),"" , "", "", "", "","","","","","","",""));


            PdfPTable ftable = new PdfPTable(noOfDataCell);
            ftable.setWidthPercentage(100);
            float[] columnWidthaa = new float[]{10,40, 50, 30,30 };
            ftable.setWidths(columnWidthaa);
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBackgroundColor(myColor1);

            cell = new PdfPCell(new Phrase("Total Sales",font));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(Utility.decimal2Palce(Math.abs(totalAmount))+" "+GlobalClass.currecyPrefix,font));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);

//
//            cell = new PdfPCell(new Paragraph("This is System Generate PDF."));
//            cell.setColspan(noOfDataCell);
//            ftable.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);

            cell.addElement(ftable);

            table.addCell(cell);

            doc.add(table);
            Toast.makeText(context, "Created PDF in CBatDownload Folder", Toast.LENGTH_LONG).show();
            doc.close();


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }
        //  previewPdf();
        return fileName;
    }

    public String productDetailsPdf(File docsFolder,Map<String, JSONArray> customerFullList)  {

        //use to set background color
        BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
        BaseColor myColor1 = WebColors.getRGBColor("#264553");
        BaseColor colorWhite = WebColors.getRGBColor("#FFFFFF");
        Font font = new Font();
        font.setColor(colorWhite);
        Document doc = new Document();
        String fileName="";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
             fileName=GlobalClass.title+"_" + sdf.format(Calendar.getInstance().getTime()) + ".pdf";
            // String fileName="Customer_Details_"+GlobalClass.startDate+"_"+GlobalClass.endDate+".pdf";
            pdfFile = new File(docsFolder.getAbsolutePath(), fileName);
            OutputStream output = new FileOutputStream(pdfFile);
            PdfWriter.getInstance(doc, output);
            doc.open();

            //  document.add(new Paragraph(mContentEditText.getText().toString()));
//create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = context.getResources().getDrawable(R.drawable.logo);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();

            bgImage = Image.getInstance(bitmapdata);
            bgImage.setAbsolutePosition(330f, 642f);
            cell.addElement(bgImage);
            pt.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.addElement(new Paragraph(GlobalClass.title+" from "+GlobalClass.startDate+" to "+GlobalClass.endDate));

            cell.addElement(new Paragraph(""));
            cell.addElement(new Paragraph(""));
            pt.addCell(cell);
            cell = new PdfPCell(new Paragraph(""));
            cell.setBorder(Rectangle.NO_BORDER);
            pt.addCell(cell);

            PdfPTable pTable = new PdfPTable(1);
            pTable.setWidthPercentage(100);
            cell = new PdfPCell();
            cell.setColspan(1);
            cell.addElement(pt);
            pTable.addCell(cell);

            int noOfDataCell=16;
            PdfPTable table = new PdfPTable(noOfDataCell);

            float[] columnWidth = new float[]{10, 50,40, 40, 30,30,30,30,30,30,30,30,30,30,30,30 };
            table.setWidths(columnWidth);


            cell = new PdfPCell();


            cell.setBackgroundColor(myColor);
            cell.setColspan(noOfDataCell);
            cell.addElement(pTable);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" "));
            cell.setColspan(noOfDataCell);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);

            cell.setBackgroundColor(myColor1);


            /**
             *   ItemSortable headers[] = {
             *                 new ItemSortable("Total"),
             *                 new ItemSortable("% Contri."),
             *                 new ItemSortable("Apr"),
             *                 new ItemSortable("May"),
             *                 new ItemSortable("Jun"),
             *                 new ItemSortable("Jul"),
             *                 new ItemSortable("Aug"),
             *                 new ItemSortable("Sep"),
             *                 new ItemSortable("Oct"),
             *                 new ItemSortable("Nov"),
             *                 new ItemSortable("Dec"),
             *                 new ItemSortable("Jan"),
             *                 new ItemSortable("Feb"),
             *                 new ItemSortable("Mar"),
             *                 new ItemSortable("Customer"),
             */

            cell = new PdfPCell(new Phrase("Sr.No",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Product",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Total",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("% Contri.",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Apr",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("May",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Jun",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Jul",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Aug",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Sep",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("oct",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Nov",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Dec",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Jan",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Feb",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Mar",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);


            //table.setHeaderRows(3);
            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);
            int count=1;
            double totalSale=0;
            for (Map.Entry<String, JSONArray> set : customerFullList.entrySet()) {
                // for(Map.Entry<String, JSONObject> element:GlobalClass.totalSales.entrySet()){
                try {
                    if (!set.getKey().equalsIgnoreCase("totalProductSales")) {
                        JSONArray dataMonthly = set.getValue().getJSONArray(1);
                        JSONArray dataYearly = set.getValue().getJSONArray(3);
                        JSONArray contriData = set.getValue().getJSONArray(4);
                        //JSONArray dataCustomer =  set.getValue().getJSONArray(5);
                        //[] dataTable = new String[dataMonthly.length() + 4];
                        JSONObject elementYearly;
                        boolean yearFlag = true;
                        boolean contriFlag = true;
                        float totalSales = 0;
                        int j = 0;

                        for (int i = 0; i < dataMonthly.length(); i++) {
                            if (yearFlag) {
                                elementYearly = dataYearly.getJSONObject(i);
                                yearFlag = false;
                                int yVal = elementYearly.getInt("value");
                                BigDecimal number1 = new BigDecimal(yVal);
                                //totalSales = number1.floatValue() / 10000000;
                                totalSales = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                Log.d(TAG, "set.getKey() >" + set.getKey());
                                Log.d(TAG, "totalSales >" + totalSales);
                                table.addCell(String.valueOf(count++));
                                table.addCell(String.valueOf(set.getKey()));
                                table.addCell(Utility.decimal2Palce(Math.abs(totalSales))+" "+GlobalClass.currecyPrefix);

                            }
                            if (contriFlag) {
                                elementYearly = contriData.getJSONObject(i);
                                contriFlag = false;
                                double yVal = elementYearly.getDouble("value");
                                //BigDecimal number1 = new BigDecimal(yVal);
                                //totalSales = number1.floatValue() / 10000000;
                                //totalSales = number1.floatValue(); // GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                Log.d(TAG, "set.getKey() >" + set.getKey());
                                Log.d(TAG, "yVal >" + yVal);
                                // dataTable[j++] = String.valueOf(set.getKey());
                                table.addCell(Utility.decimal2Palce(Math.abs(yVal)));
                            }

                            JSONObject element = dataMonthly.getJSONObject(i);
                            int yVal = element.getInt("value");
                            BigDecimal number1 = new BigDecimal(yVal);
                            // float debitFloat = number1.floatValue() / 10000000;
                            float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            table.addCell( Utility.decimal2Palce(Math.abs(debitFloat)));

                        }
//                        dataTable[j++]=dataCustomer.getJSONObject(0).getJSONObject("customer").getString("address");
//                        items.add(new NexusWithImage(type, dataTable));
//                        Log.d(TAG, "dataTable >" + dataTable.toString());
//                        Log.d(TAG, "dataTable length >" + dataTable.length);
                    }
//                    table.addCell(String.valueOf(count++));
//                    table.addCell(element.getKey());
//                    table.addCell(Utility.decimal2Palce(Math.abs(element.getValue().getDouble("sales"))));
//                    table.addCell(Utility.decimal2Palce(element.getValue().getDouble("value"))+" %");
//                    totalSale=totalSale+element.getValue().getDouble("sales");
//
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            float totalSales = 0;

            try {
                if (customerFullList != null && !customerFullList.isEmpty() && customerFullList.containsKey("totalProductSales")) {
                    JSONArray setVal = customerFullList.get("totalProductSales");
                    JSONArray dataMonthly = setVal.getJSONArray(1);
                    JSONArray dataYearly = setVal.getJSONArray(3);

                    String[] dataTable = new String[dataMonthly.length() + 4];
                    JSONObject elementYearly;
                    boolean yearFlag = true;
                    //  float totalSales = 0;
                    int j = 0;
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
//                            dataTable[j++] = String.valueOf("Total Sales");
//                            dataTable[j++] = Utility.decimal2Palce(Math.abs(totalSales));
//                            dataTable[j++] = "100";
                        }

                        JSONObject element = dataMonthly.getJSONObject(i);
                        int yVal = element.getInt("value");
                        BigDecimal number1 = new BigDecimal(yVal);
                        // float debitFloat = number1.floatValue() / 10000000;
                        float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                        Log.d(TAG, "debitFloat >" + debitFloat);
                        // dataTable[j++] = Utility.decimal2Palce(Math.abs(debitFloat));

                    }
                    //items.add(new NexusWithImage(type, dataTable));
                    // Log.d(TAG, "dataTable >" + dataTable.toString());
                    // Log.d(TAG, "dataTable length >" + dataTable.length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            PdfPTable ftable = new PdfPTable(noOfDataCell);
            ftable.setWidthPercentage(100);
            float[] columnWidthaa = new float[]{50, 30,20, 50, 30,30,30,30,30,30,30,30,30,30,30,30  };
            ftable.setWidths(columnWidthaa);
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBackgroundColor(myColor1);
            cell = new PdfPCell(new Phrase("Total Sales",font));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);


//

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(Utility.decimal2Palce(Math.abs(totalSales))+" "+GlobalClass.currecyPrefix,font));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
//
//            cell = new PdfPCell(new Paragraph("This is System Generate PDF."));
//            cell.setColspan(noOfDataCell);
//            ftable.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);

            cell.addElement(ftable);

            table.addCell(cell);

            doc.add(table);
            Toast.makeText(context, "Created PDF in CBatDownload Folder", Toast.LENGTH_LONG).show();
            doc.close();


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }
        //  previewPdf();
        return fileName;

    }

    public String creditUtilizationDetailsPdf(File docsFolder,Map<String,JSONObject> creditUtilizeFullList)  {

        //use to set background color
        BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
        BaseColor myColor1 = WebColors.getRGBColor("#264553");
        BaseColor colorWhite = WebColors.getRGBColor("#FFFFFF");
        Font font = new Font();
        font.setColor(colorWhite);
        Document doc = new Document();
        String fileName="";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
             fileName=GlobalClass.title+"_"+ sdf.format(Calendar.getInstance().getTime()) + ".pdf";
            // String fileName="Customer_Details_"+GlobalClass.startDate+"_"+GlobalClass.endDate+".pdf";
            pdfFile = new File(docsFolder.getAbsolutePath(), fileName);
            OutputStream output = new FileOutputStream(pdfFile);
            PdfWriter.getInstance(doc, output);
            doc.open();

            //  document.add(new Paragraph(mContentEditText.getText().toString()));
//create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = context.getResources().getDrawable(R.drawable.logo);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();

            bgImage = Image.getInstance(bitmapdata);
            bgImage.setAbsolutePosition(330f, 642f);
            cell.addElement(bgImage);
            pt.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.addElement(new Paragraph(GlobalClass.title+" from "+GlobalClass.startDate+" to "+GlobalClass.endDate));

            cell.addElement(new Paragraph(""));
            cell.addElement(new Paragraph(""));
            pt.addCell(cell);
            cell = new PdfPCell(new Paragraph(""));
            cell.setBorder(Rectangle.NO_BORDER);
            pt.addCell(cell);

            PdfPTable pTable = new PdfPTable(1);
            pTable.setWidthPercentage(100);
            cell = new PdfPCell();
            cell.setColspan(1);
            cell.addElement(pt);
            pTable.addCell(cell);

            int noOfDataCell=5;
            PdfPTable table = new PdfPTable(noOfDataCell);




            float[] columnWidth = new float[]{10,40, 40, 30,30};
            table.setWidths(columnWidth);


            cell = new PdfPCell();


            cell.setBackgroundColor(myColor);
            cell.setColspan(noOfDataCell);
            cell.addElement(pTable);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" "));
            cell.setColspan(noOfDataCell);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);

            cell.setBackgroundColor(myColor1);

/**
 *  ItemSortable headers[] = {
 *                 new ItemSortable("Credit"),
 *                 new ItemSortable("Receivable"),
 *                 new ItemSortable("Utilization"),
 *                 new ItemSortable("Customer"),
 */

            cell = new PdfPCell(new Phrase("Sr.No",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
//            cell = new PdfPCell(new Phrase("Customer",font));
//            cell.setBackgroundColor(myColor1);
//            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Customer",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Credit",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Receivable",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Utilization",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);

            //table.setHeaderRows(3);
            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);
            int count=1;
            double totalSale=0;
            for (Map.Entry<String, JSONObject> set : creditUtilizeFullList.entrySet()) {
                try {
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
                        Log.d(TAG, "set.getKey() >" + set.getKey());
                        Log.d(TAG, "totalSales >" + creditLimit);

                        int yValreceivable = set.getValue().getInt("receivable");
                        BigDecimal numberreceivable = new BigDecimal(yValreceivable);
                        receivable = numberreceivable.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);

                        int yValutilizePercent = set.getValue().getInt("creditUtilize");
                        BigDecimal numberutilizePercent = new BigDecimal(yValutilizePercent);
                        utilizePercent = numberutilizePercent.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);

                        String address = set.getValue().getJSONObject("customer").getString("address");

//                        dataTable[j++] = String.valueOf(set.getKey());
//                        dataTable[j++] = Utility.decimal2Palce(Math.abs(creditLimit));
//                        dataTable[j++] = Utility.decimal2Palce(Math.abs(receivable));
//                        dataTable[j++] = Utility.decimal2Palce(Math.abs(utilizePercent));
//                        dataTable[j++] = address;
                        table.addCell(String.valueOf(count++));
                        table.addCell(String.valueOf(set.getKey()));
                        table.addCell( Utility.decimal2Palce(Math.abs(creditLimit))+" "+GlobalClass.currecyPrefix);
                        table.addCell( Utility.decimal2Palce(Math.abs(receivable))+" "+GlobalClass.currecyPrefix);
                        table.addCell( Utility.decimal2Palce(Math.abs(utilizePercent))+" "+GlobalClass.currecyPrefix);

                        // items.add(new NexusWithImage(type, dataTable));
                        Log.d(TAG, "dataTable >" + dataTable.toString());
                        Log.d(TAG, "dataTable length >" + dataTable.length);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                count++;
            }

//
//            PdfPTable ftable = new PdfPTable(noOfDataCell);
//            ftable.setWidthPercentage(100);
//            float[] columnWidthaa = new float[]{50,20, 50, 30,30,30,30  };
//            ftable.setWidths(columnWidthaa);
//            cell = new PdfPCell();
//            cell.setColspan(2);
//            cell.setBackgroundColor(myColor1);
//            //cell = new PdfPCell(new Phrase("Total Sales",font));
//
//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);
//
//
////
//
//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);
//
//            //  cell = new PdfPCell(new Phrase(Utility.decimal2Palce(Math.abs(totalSales)),font));
//            cell = new PdfPCell(new Phrase(""));
//            cell.setColspan(2);
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);
//
//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);
//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);
//
//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);
//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);

//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);

//
//            cell = new PdfPCell(new Paragraph("This is System Generate PDF."));
//            cell.setColspan(noOfDataCell);
//            ftable.addCell(cell);

//            cell = new PdfPCell();
//            cell.setColspan(noOfDataCell);
//
//            cell.addElement(ftable);

            table.addCell(cell);

            doc.add(table);
            Toast.makeText(context, "Created PDF in CBatDownload Folder", Toast.LENGTH_LONG).show();
            doc.close();


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }
        //  previewPdf();
return fileName;
    }
    public String totalReceivablePdf(File docsFolder,Map<String, JSONObject> totalSales)  {

        //use to set background color
        BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
        BaseColor myColor1 = WebColors.getRGBColor("#264553");
        BaseColor colorWhite = WebColors.getRGBColor("#FFFFFF");
        Font font = new Font();
        font.setColor(colorWhite);
        Document doc = new Document();
        String fileName="";
        try {
            fileName="Total_Receivable_"+GlobalClass.startDate+"_"+GlobalClass.endDate+".pdf";
            pdfFile = new File(docsFolder.getAbsolutePath(), fileName);
            OutputStream output = new FileOutputStream(pdfFile);
            PdfWriter.getInstance(doc, output);
            doc.open();

            //  document.add(new Paragraph(mContentEditText.getText().toString()));
//create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = context.getResources().getDrawable(R.drawable.logo);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();

            bgImage = Image.getInstance(bitmapdata);
            bgImage.setAbsolutePosition(330f, 642f);
            cell.addElement(bgImage);
            pt.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.addElement(new Paragraph("State wise Total Receivable"+" from "+GlobalClass.startDate+" to "+GlobalClass.endDate));

            cell.addElement(new Paragraph(""));
            cell.addElement(new Paragraph(""));
            pt.addCell(cell);
            cell = new PdfPCell(new Paragraph(""));
            cell.setBorder(Rectangle.NO_BORDER);
            pt.addCell(cell);

            PdfPTable pTable = new PdfPTable(1);
            pTable.setWidthPercentage(100);
            cell = new PdfPCell();
            cell.setColspan(1);
            cell.addElement(pt);
            pTable.addCell(cell);

            int noOfDataCell=4;
            PdfPTable table = new PdfPTable(noOfDataCell);

            float[] columnWidth = new float[]{6, 40, 40, 40, };
            table.setWidths(columnWidth);


            cell = new PdfPCell();


            cell.setBackgroundColor(myColor);
            cell.setColspan(6);
            cell.addElement(pTable);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" "));
            cell.setColspan(6);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(6);

            cell.setBackgroundColor(myColor1);

            cell = new PdfPCell(new Phrase("Sr.No",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("State",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Receivable",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Contribution",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);


            //table.setHeaderRows(3);
            cell = new PdfPCell();
            cell.setColspan(6);
            int count=1;
            double totalSale=0;
            // for (int i = 1; i <= GlobalClass.totalSales.size(); i++) {
            for(Map.Entry<String, JSONObject> element:totalSales.entrySet()){
                try {

                    table.addCell(String.valueOf(count++));
                    table.addCell(element.getKey());
                    table.addCell(Utility.decimal2Palce(Math.abs(element.getValue().getDouble("sales"))));
                    table.addCell(Utility.decimal2Palce(element.getValue().getDouble("value"))+" %");
                    totalSale=totalSale+element.getValue().getDouble("sales");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            PdfPTable ftable = new PdfPTable(noOfDataCell);
            ftable.setWidthPercentage(100);
            float[] columnWidthaa = new float[]{30, 10, 30, 10};
            ftable.setWidths(columnWidthaa);
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBackgroundColor(myColor1);
            cell = new PdfPCell(new Phrase("Total Receivable",font));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(Utility.decimal2Palce(Math.abs(totalSale)),font));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);
//
//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);
//
//            cell = new PdfPCell(new Paragraph("This is System Generate PDF."));
//            cell.setColspan(4);
//            ftable.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);

            cell.addElement(ftable);

            table.addCell(cell);

            doc.add(table);
            Toast.makeText(context, "Created PDF in CBatDownload Folder", Toast.LENGTH_LONG).show();
            doc.close();


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }
        //  previewPdf();
return fileName;
    }

    public String customerRecevibleDetailsPdf(File docsFolder,Map<String, JSONArray> customerFullList)  {

        //use to set background color
        BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
        BaseColor myColor1 = WebColors.getRGBColor("#264553");
        BaseColor colorWhite = WebColors.getRGBColor("#FFFFFF");
        Font font = new Font();
        font.setColor(colorWhite);
        Document doc = new Document();
        String fileName="";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
             fileName=GlobalClass.title +"_"+ sdf.format(Calendar.getInstance().getTime()) + ".pdf";
            // String fileName="Customer_Details_"+GlobalClass.startDate+"_"+GlobalClass.endDate+".pdf";
            pdfFile = new File(docsFolder.getAbsolutePath(), fileName);
            OutputStream output = new FileOutputStream(pdfFile);
            PdfWriter.getInstance(doc, output);
            doc.open();

            //  document.add(new Paragraph(mContentEditText.getText().toString()));
//create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = context.getResources().getDrawable(R.drawable.logo);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();

            bgImage = Image.getInstance(bitmapdata);
            bgImage.setAbsolutePosition(330f, 642f);
            cell.addElement(bgImage);
            pt.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.addElement(new Paragraph(GlobalClass.title+" from "+GlobalClass.startDate+" to "+GlobalClass.endDate));

            cell.addElement(new Paragraph(""));
            cell.addElement(new Paragraph(""));
            pt.addCell(cell);
            cell = new PdfPCell(new Paragraph(""));
            cell.setBorder(Rectangle.NO_BORDER);
            pt.addCell(cell);

            PdfPTable pTable = new PdfPTable(1);
            pTable.setWidthPercentage(100);
            cell = new PdfPCell();
            cell.setColspan(1);
            cell.addElement(pt);
            pTable.addCell(cell);

            int noOfDataCell=16;
            PdfPTable table = new PdfPTable(noOfDataCell);

            float[] columnWidth = new float[]{10, 50,40, 40, 30,30,30,30,30,30,30,30,30,30,30,30 };
            table.setWidths(columnWidth);


            cell = new PdfPCell();


            cell.setBackgroundColor(myColor);
            cell.setColspan(noOfDataCell);
            cell.addElement(pTable);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" "));
            cell.setColspan(noOfDataCell);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);

            cell.setBackgroundColor(myColor1);


            /**
             *   ItemSortable headers[] = {
             *                 new ItemSortable("Total"),
             *                 new ItemSortable("% Contri."),
             *                 new ItemSortable("Apr"),
             *                 new ItemSortable("May"),
             *                 new ItemSortable("Jun"),
             *                 new ItemSortable("Jul"),
             *                 new ItemSortable("Aug"),
             *                 new ItemSortable("Sep"),
             *                 new ItemSortable("Oct"),
             *                 new ItemSortable("Nov"),
             *                 new ItemSortable("Dec"),
             *                 new ItemSortable("Jan"),
             *                 new ItemSortable("Feb"),
             *                 new ItemSortable("Mar"),
             *                 new ItemSortable("Customer"),
             */

            cell = new PdfPCell(new Phrase("Sr.No",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Customer",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Total",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("% Contri.",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Apr",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("May",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Jun",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Jul",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Aug",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Sep",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("oct",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Nov",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Dec",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Jan",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Feb",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Mar",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);


            //table.setHeaderRows(3);
            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);
            int count=1;
            double totalSale=0;
            for (Map.Entry<String, JSONArray> set : customerFullList.entrySet()) {
                // for(Map.Entry<String, JSONObject> element:GlobalClass.totalSales.entrySet()){
                try {
                    if (!set.getKey().equalsIgnoreCase("totalStateSales")) {
                        JSONArray dataMonthly = set.getValue().getJSONArray(1);
                        JSONArray dataYearly = set.getValue().getJSONArray(3);
                        JSONArray contriData = set.getValue().getJSONArray(4);
                        JSONArray dataCustomer =  set.getValue().getJSONArray(5);
                        //[] dataTable = new String[dataMonthly.length() + 4];
                        JSONObject elementYearly;
                        boolean yearFlag = true;
                        boolean contriFlag = true;
                        float totalSales = 0;
                        int j = 0;

                        for (int i = 0; i < dataMonthly.length(); i++) {
                            if (yearFlag) {
                                elementYearly = dataYearly.getJSONObject(i);
                                yearFlag = false;
                                int yVal = elementYearly.getInt("value");
                                BigDecimal number1 = new BigDecimal(yVal);
                                //totalSales = number1.floatValue() / 10000000;
                                totalSales = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                Log.d(TAG, "set.getKey() >" + set.getKey());
                                Log.d(TAG, "totalSales >" + totalSales);
                                table.addCell(String.valueOf(count++));
                                table.addCell(String.valueOf(set.getKey()));
                                table.addCell(Utility.decimal2Palce(Math.abs(totalSales)));

                            }
                            if (contriFlag) {
                                elementYearly = contriData.getJSONObject(i);
                                contriFlag = false;
                                double yVal = elementYearly.getDouble("value");
                                //BigDecimal number1 = new BigDecimal(yVal);
                                //totalSales = number1.floatValue() / 10000000;
                                //totalSales = number1.floatValue(); // GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                Log.d(TAG, "set.getKey() >" + set.getKey());
                                Log.d(TAG, "yVal >" + yVal);
                                // dataTable[j++] = String.valueOf(set.getKey());
                                table.addCell(Utility.decimal2Palce(Math.abs(yVal)));
                            }

                            JSONObject element = dataMonthly.getJSONObject(i);
                            int yVal = element.getInt("value");
                            BigDecimal number1 = new BigDecimal(yVal);
                            // float debitFloat = number1.floatValue() / 10000000;
                            float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            table.addCell( Utility.decimal2Palce(Math.abs(debitFloat)));

                        }
//                        dataTable[j++]=dataCustomer.getJSONObject(0).getJSONObject("customer").getString("address");
//                        items.add(new NexusWithImage(type, dataTable));
//                        Log.d(TAG, "dataTable >" + dataTable.toString());
//                        Log.d(TAG, "dataTable length >" + dataTable.length);
                    }
//                    table.addCell(String.valueOf(count++));
//                    table.addCell(element.getKey());
//                    table.addCell(Utility.decimal2Palce(Math.abs(element.getValue().getDouble("sales"))));
//                    table.addCell(Utility.decimal2Palce(element.getValue().getDouble("value"))+" %");
//                    totalSale=totalSale+element.getValue().getDouble("sales");
//
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            float totalSales = 0;

            try {
                if (customerFullList != null && !customerFullList.isEmpty() && customerFullList.containsKey("totalStateSales")) {
                    JSONArray setVal = customerFullList.get("totalStateSales");
                    JSONArray dataMonthly = setVal.getJSONArray(1);
                    JSONArray dataYearly = setVal.getJSONArray(3);

                    String[] dataTable = new String[dataMonthly.length() + 4];
                    JSONObject elementYearly;
                    boolean yearFlag = true;
                    //  float totalSales = 0;
                    int j = 0;
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
//                            dataTable[j++] = String.valueOf("Total Sales");
//                            dataTable[j++] = Utility.decimal2Palce(Math.abs(totalSales));
//                            dataTable[j++] = "100";
                        }

                        JSONObject element = dataMonthly.getJSONObject(i);
                        int yVal = element.getInt("value");
                        BigDecimal number1 = new BigDecimal(yVal);
                        // float debitFloat = number1.floatValue() / 10000000;
                        float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                        Log.d(TAG, "debitFloat >" + debitFloat);
                        // dataTable[j++] = Utility.decimal2Palce(Math.abs(debitFloat));

                    }
                    //items.add(new NexusWithImage(type, dataTable));
                    // Log.d(TAG, "dataTable >" + dataTable.toString());
                    // Log.d(TAG, "dataTable length >" + dataTable.length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            PdfPTable ftable = new PdfPTable(noOfDataCell);
            ftable.setWidthPercentage(100);
            float[] columnWidthaa = new float[]{50, 30,20, 50, 30,30,30,30,30,30,30,30,30,30,30,30  };
            ftable.setWidths(columnWidthaa);
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBackgroundColor(myColor1);
            cell = new PdfPCell(new Phrase("Total Sales",font));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);


//

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(Utility.decimal2Palce(Math.abs(totalSales)),font));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
//
//            cell = new PdfPCell(new Paragraph("This is System Generate PDF."));
//            cell.setColspan(noOfDataCell);
//            ftable.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);

            cell.addElement(ftable);

            table.addCell(cell);

            doc.add(table);
            Toast.makeText(context, "Created PDF in CBatDownload Folder", Toast.LENGTH_LONG).show();
            doc.close();


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }
        //  previewPdf();
return fileName;
    }
    public void invoiceReceivableDetailsPdf(File docsFolder,List<JSONObject> customerInvoiceFullList)  {

       Log.d("invoice",customerInvoiceFullList.toString());
        //use to set background color
        BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
        BaseColor myColor1 = WebColors.getRGBColor("#264553");
        BaseColor colorWhite = WebColors.getRGBColor("#FFFFFF");
        Font font = new Font();
        font.setColor(colorWhite);
        Document doc = new Document();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            String fileName=GlobalClass.title+"_"+ sdf.format(Calendar.getInstance().getTime()) + ".pdf";
            // String fileName="Customer_Details_"+GlobalClass.startDate+"_"+GlobalClass.endDate+".pdf";
            pdfFile = new File(docsFolder.getAbsolutePath(), fileName);
            OutputStream output = new FileOutputStream(pdfFile);
            PdfWriter.getInstance(doc, output);
            doc.open();

            //  document.add(new Paragraph(mContentEditText.getText().toString()));
//create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = context.getResources().getDrawable(R.drawable.logo);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();

            bgImage = Image.getInstance(bitmapdata);
            bgImage.setAbsolutePosition(330f, 642f);
            cell.addElement(bgImage);
            pt.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.addElement(new Paragraph(GlobalClass.title+" from "+GlobalClass.startDate+" to "+GlobalClass.endDate));

            cell.addElement(new Paragraph(""));
            cell.addElement(new Paragraph(""));
            pt.addCell(cell);
            cell = new PdfPCell(new Paragraph(""));
            cell.setBorder(Rectangle.NO_BORDER);
            pt.addCell(cell);

            PdfPTable pTable = new PdfPTable(1);
            pTable.setWidthPercentage(100);
            cell = new PdfPCell();
            cell.setColspan(1);
            cell.addElement(pt);
            pTable.addCell(cell);

            int noOfDataCell=7;
            PdfPTable table = new PdfPTable(noOfDataCell);




            float[] columnWidth = new float[]{10,40, 40, 30,30,30,30};
            table.setWidths(columnWidth);


            cell = new PdfPCell();


            cell.setBackgroundColor(myColor);
            cell.setColspan(noOfDataCell);
            cell.addElement(pTable);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" "));
            cell.setColspan(noOfDataCell);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);

            cell.setBackgroundColor(myColor1);


            /**
             *         new ItemSortable("Invoice No"),
             *             new ItemSortable("Sales"),
             *                     new ItemSortable("Invoice Date"),
             *                  new ItemSortable("Due Date"),
             *                  new ItemSortable("Credit Period"),
             *                  new ItemSortable("Arrears Days"),
             *                  new ItemSortable("Customer Code")
             */

            cell = new PdfPCell(new Phrase("Sr.No",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
//            cell = new PdfPCell(new Phrase("Customer",font));
//            cell.setBackgroundColor(myColor1);
//            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Invoice No",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Sales",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Invoice Date",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Due Date",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Credit Period",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Arrears Days",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);

            //table.setHeaderRows(3);
            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);
            int count=1;
            double totalSale=0;
            for (int i = 0; i < customerInvoiceFullList.size(); i++) {
                try {
                    JSONObject element = customerInvoiceFullList.get(i);
                    int yVal = element.getInt("value");
                    BigDecimal number1 = new BigDecimal(yVal);
                    float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);

                    String dueDate = element.getString("dueDate");
                    // float creditPeroid=(new BigDecimal(element.getInt("credirPeroid"))).floatValue()/ GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                    String creditPeroidStr = String.valueOf(element.getInt("credirPeroid"));
                    int arrearDaysInt = element.getInt("arrearDays");
                    String arrearDays = String.valueOf(arrearDaysInt);
                    Log.d(TAG, "debitFloat >" + debitFloat);
                    Log.d(TAG, "dueDate >" + dueDate);
                    Log.d(TAG, "creditPeroid >" + creditPeroidStr);
                    Log.d(TAG, "arrearDays >" + arrearDays);
                    String indicatorColor = "";
                    Font font1 = new Font();
                    if (arrearDaysInt < 0) {
                        // indicatorColor = "#FF0000";
                        BaseColor colorRed = WebColors.getRGBColor("#FF0000");
                        font1.setColor(colorRed);
                    } else {
                        //indicatorColor = "#008000";
                        BaseColor colorGreen = WebColors.getRGBColor("#008000");
                        font1.setColor(colorGreen);
                    }
                    table.addCell(String.valueOf(count++));
                    table.addCell(element.getString("name"));
                    table.addCell(Utility.decimal2Palce(Math.abs(debitFloat)));
                    table.addCell(element.getString("voucherDate"));
                    table.addCell(dueDate);
                    table.addCell(creditPeroidStr);
                    table.addCell(new Phrase(arrearDays,font1));
                    // String[] data = new String[]{GlobalClass.currentFrgmentMain, element.getString("name"), Utility.decimal2Palce(Math.abs(debitFloat)), element.getString("voucherDate"), dueDate, creditPeroidStr, arrearDays, element.getString("voucherGuid"), "", "", "", "", "", ""};
                    // items.add(new NexusWithImage(type, data, indicatorColor));
                }catch(Exception e){

                }
            }


            PdfPTable ftable = new PdfPTable(noOfDataCell);
            ftable.setWidthPercentage(100);
            float[] columnWidthaa = new float[]{50,20, 50, 30,30,30,30  };
            ftable.setWidths(columnWidthaa);
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBackgroundColor(myColor1);
            //cell = new PdfPCell(new Phrase("Total Sales",font));

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);


//

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            //  cell = new PdfPCell(new Phrase(Utility.decimal2Palce(Math.abs(totalSales)),font));
            cell = new PdfPCell(new Phrase(""));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);

//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);

//
//            cell = new PdfPCell(new Paragraph("This is System Generate PDF."));
//            cell.setColspan(noOfDataCell);
//            ftable.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);

            cell.addElement(ftable);

            table.addCell(cell);

            doc.add(table);
            Toast.makeText(context, "Created PDF in CBatDownload Folder", Toast.LENGTH_LONG).show();
            doc.close();


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }
        //  previewPdf();

    }

    public String ledgerReceivableDetailsPdf(File docsFolder,List<JSONObject> customerInvoiceSummaryList)  {

        //use to set background color
        BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
        BaseColor myColor1 = WebColors.getRGBColor("#264553");
        BaseColor colorWhite = WebColors.getRGBColor("#FFFFFF");
        Font font = new Font();
        font.setColor(colorWhite);
        Document doc = new Document();
        String fileName="";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            fileName=GlobalClass.title+"_" + sdf.format(Calendar.getInstance().getTime()) + ".pdf";
            // String fileName="Customer_Details_"+GlobalClass.startDate+"_"+GlobalClass.endDate+".pdf";
            pdfFile = new File(docsFolder.getAbsolutePath(), fileName);
            OutputStream output = new FileOutputStream(pdfFile);
            PdfWriter.getInstance(doc, output);
            doc.open();


//create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = context.getResources().getDrawable(R.drawable.logo);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();

            bgImage = Image.getInstance(bitmapdata);
            bgImage.setAbsolutePosition(330f, 642f);
            cell.addElement(bgImage);
            pt.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.addElement(new Paragraph(GlobalClass.title));

            cell.addElement(new Paragraph(""));
            cell.addElement(new Paragraph(""));
            pt.addCell(cell);
            cell = new PdfPCell(new Paragraph(""));
            cell.setBorder(Rectangle.NO_BORDER);
            pt.addCell(cell);

            PdfPTable pTable = new PdfPTable(1);
            pTable.setWidthPercentage(100);
            cell = new PdfPCell();
            cell.setColspan(1);
            cell.addElement(pt);
            pTable.addCell(cell);

            int noOfDataCell=5;
            PdfPTable table = new PdfPTable(noOfDataCell);




            float[] columnWidth = new float[]{10,40, 40, 30,30};
            table.setWidths(columnWidth);


            cell = new PdfPCell();


            cell.setBackgroundColor(myColor);
            cell.setColspan(noOfDataCell);
            cell.addElement(pTable);

            table.addCell(cell);

//            cell = new PdfPCell(new Phrase(" "));
//            cell.setColspan(noOfDataCell);
//            table.addCell(cell);

//            cell = new PdfPCell();
//            cell.setColspan(noOfDataCell);
//
//            cell.setBackgroundColor(myColor1);


            /**
             *         new ItemSortable("Invoice No"),
             *             new ItemSortable("Sales"),
             *                     new ItemSortable("Invoice Date"),
             *                  new ItemSortable("Due Date"),
             *                  new ItemSortable("Credit Period"),
             *                  new ItemSortable("Arrears Days"),
             *                  new ItemSortable("Customer Code")
             */

//            cell = new PdfPCell(new Phrase("Sr.No",font));
//            cell.setBackgroundColor(myColor1);
//            table.addCell(cell);
//            cell = new PdfPCell(new Phrase("Item",font));
//            cell.setBackgroundColor(myColor1);
//            table.addCell(cell);
//            cell = new PdfPCell(new Phrase("Amount",font));
//            cell.setBackgroundColor(myColor1);
//            table.addCell(cell);
//            cell = new PdfPCell(new Phrase("Quantity",font));
//            cell.setBackgroundColor(myColor1);
//            table.addCell(cell);
//            cell = new PdfPCell(new Phrase("Rate",font));
//            cell.setBackgroundColor(myColor1);
//            table.addCell(cell);

            //table.setHeaderRows(3);
            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);
            int count=1;
            double totalSale=0;
            float totalAmount=0;
            boolean vocherFlag=true;
            for (int i = 0; i < customerInvoiceSummaryList.size(); i++) {
                try {
                    JSONObject element = customerInvoiceSummaryList.get(i);
                    int yVal = element.getInt("amount");
                    BigDecimal number1 = new BigDecimal(yVal);
                    float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                    Log.d(TAG, "debitFloat >" + debitFloat);
                    totalAmount = totalAmount + debitFloat;
                    if (vocherFlag) {
                        JSONObject voucher = element.getJSONObject("voucher");
                        String customerNameT = voucher.getString("accountName");
                        String legderDateT = voucher.getString("date");
                        String address = voucher.getString("address");
                        String[] date = legderDateT.split("T");
                        String ledgerTypeT = voucher.getString("voucherType");
                        String ledgerRefT = voucher.getString("refNo");
                        String stateT = voucher.getString("state");

//                        customerName.setText(customerNameT);
//                        customerAddress.setText(address);
//                        customerState.setText(stateT);
//                        legderDate.setText(date.length > 0 ? date[0] : legderDateT);
//                        ledgerType.setText(" | Type# - " + ledgerTypeT);
//                        ledgerRef.setText("Ref# - " + ledgerRefT);
                        vocherFlag = false;

                        ///Details
                        PdfPTable ptd = new PdfPTable(2);
                        ptd.setWidthPercentage(100);
                        float[] fld = new float[]{20, 45};
                        ptd.setWidths(fld);
                        cell = new PdfPCell(new Phrase(customerNameT));
                        cell.setBorder(Rectangle.NO_BORDER);
                        ptd.addCell(cell);

                        cell = new PdfPCell(new Phrase(address));
                        cell.setBorder(Rectangle.NO_BORDER);
                        ptd.addCell(cell);

                        cell = new PdfPCell();
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.addElement(new Paragraph(stateT));

                        cell = new PdfPCell();
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.addElement(new Paragraph(date.length > 0 ? date[0] : legderDateT));

                        cell = new PdfPCell();
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.addElement(new Paragraph("Type# - " + ledgerTypeT));
                        ptd.addCell(cell);

                        cell = new PdfPCell();
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.addElement(new Paragraph("Ref# - " + ledgerRefT));
                        ptd.addCell(cell);

                        cell = new PdfPCell();
                        cell.setBackgroundColor(colorWhite);
                        cell.setColspan(noOfDataCell);
                        cell.addElement(ptd);

                        table.addCell(cell);

                        ///Table Item
                        cell = new PdfPCell(new Phrase(" "));
                        cell.setColspan(noOfDataCell);
                        table.addCell(cell);

                        cell = new PdfPCell();
                        cell.setColspan(noOfDataCell);
                        cell = new PdfPCell(new Phrase("Sr.No",font));
                        cell.setBackgroundColor(myColor1);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("Item",font));
                        cell.setBackgroundColor(myColor1);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("Amount",font));
                        cell.setBackgroundColor(myColor1);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("Quantity",font));
                        cell.setBackgroundColor(myColor1);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("Rate",font));
                        cell.setBackgroundColor(myColor1);
                        table.addCell(cell);
                    }
                    table.addCell(String.valueOf(count++));
                    table.addCell(element.getString("name"));
                    table.addCell(Utility.decimal2Palce(Math.abs(debitFloat))+" "+GlobalClass.currecyPrefix);
                    table.addCell(element.getString("quantity"));
                    table.addCell(element.getString("rate"));
                }catch (Exception e){
                    e.printStackTrace();
                }
                // items.add(new NexusWithImage(type, element.getString("name"),  Utility.decimal2Palce(Math.abs(debitFloat )),element.getString("quantity") , element.getString("rate"), "", "", "","","","","","","",""));
            }
            //items.add(new NexusWithImage(type, "Total",  Utility.decimal2Palce(Math.abs(totalAmount )),"" , "", "", "", "","","","","","","",""));


            PdfPTable ftable = new PdfPTable(noOfDataCell);
            ftable.setWidthPercentage(100);
            float[] columnWidthaa = new float[]{10,40, 50, 30,30 };
            ftable.setWidths(columnWidthaa);
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBackgroundColor(myColor1);

            cell = new PdfPCell(new Phrase("Total Sales",font));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(Utility.decimal2Palce(Math.abs(totalAmount))+" "+GlobalClass.currecyPrefix,font));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);

//
//            cell = new PdfPCell(new Paragraph("This is System Generate PDF."));
//            cell.setColspan(noOfDataCell);
//            ftable.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);

            cell.addElement(ftable);

            table.addCell(cell);

            doc.add(table);
            Toast.makeText(context, "Created PDF in CBatDownload Folder", Toast.LENGTH_LONG).show();
            doc.close();


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }
        //  previewPdf();
        return fileName;
    }

    public void productReceivableDetailsPdf(File docsFolder,Map<String, JSONArray> customerFullList)  {

        //use to set background color
        BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
        BaseColor myColor1 = WebColors.getRGBColor("#264553");
        BaseColor colorWhite = WebColors.getRGBColor("#FFFFFF");
        Font font = new Font();
        font.setColor(colorWhite);
        Document doc = new Document();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            String fileName=GlobalClass.title+"_" + sdf.format(Calendar.getInstance().getTime()) + ".pdf";
            // String fileName="Customer_Details_"+GlobalClass.startDate+"_"+GlobalClass.endDate+".pdf";
            pdfFile = new File(docsFolder.getAbsolutePath(), fileName);
            OutputStream output = new FileOutputStream(pdfFile);
            PdfWriter.getInstance(doc, output);
            doc.open();

            //  document.add(new Paragraph(mContentEditText.getText().toString()));
//create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = context.getResources().getDrawable(R.drawable.logo);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();

            bgImage = Image.getInstance(bitmapdata);
            bgImage.setAbsolutePosition(330f, 642f);
            cell.addElement(bgImage);
            pt.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.addElement(new Paragraph(GlobalClass.title+" from "+GlobalClass.startDate+" to "+GlobalClass.endDate));

            cell.addElement(new Paragraph(""));
            cell.addElement(new Paragraph(""));
            pt.addCell(cell);
            cell = new PdfPCell(new Paragraph(""));
            cell.setBorder(Rectangle.NO_BORDER);
            pt.addCell(cell);

            PdfPTable pTable = new PdfPTable(1);
            pTable.setWidthPercentage(100);
            cell = new PdfPCell();
            cell.setColspan(1);
            cell.addElement(pt);
            pTable.addCell(cell);

            int noOfDataCell=16;
            PdfPTable table = new PdfPTable(noOfDataCell);

            float[] columnWidth = new float[]{10, 50,40, 40, 30,30,30,30,30,30,30,30,30,30,30,30 };
            table.setWidths(columnWidth);


            cell = new PdfPCell();


            cell.setBackgroundColor(myColor);
            cell.setColspan(noOfDataCell);
            cell.addElement(pTable);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" "));
            cell.setColspan(noOfDataCell);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);

            cell.setBackgroundColor(myColor1);


            /**
             *   ItemSortable headers[] = {
             *                 new ItemSortable("Total"),
             *                 new ItemSortable("% Contri."),
             *                 new ItemSortable("Apr"),
             *                 new ItemSortable("May"),
             *                 new ItemSortable("Jun"),
             *                 new ItemSortable("Jul"),
             *                 new ItemSortable("Aug"),
             *                 new ItemSortable("Sep"),
             *                 new ItemSortable("Oct"),
             *                 new ItemSortable("Nov"),
             *                 new ItemSortable("Dec"),
             *                 new ItemSortable("Jan"),
             *                 new ItemSortable("Feb"),
             *                 new ItemSortable("Mar"),
             *                 new ItemSortable("Customer"),
             */

            cell = new PdfPCell(new Phrase("Sr.No",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Product",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Total",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("% Contri.",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Apr",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("May",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Jun",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Jul",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Aug",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Sep",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("oct",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Nov",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Dec",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Jan",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Feb",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Mar",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);


            //table.setHeaderRows(3);
            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);
            int count=1;
            double totalSale=0;
            for (Map.Entry<String, JSONArray> set : customerFullList.entrySet()) {
                // for(Map.Entry<String, JSONObject> element:GlobalClass.totalSales.entrySet()){
                try {
                    if (!set.getKey().equalsIgnoreCase("totalProductSales")) {
                        JSONArray dataMonthly = set.getValue().getJSONArray(1);
                        JSONArray dataYearly = set.getValue().getJSONArray(3);
                        JSONArray contriData = set.getValue().getJSONArray(4);
                        //JSONArray dataCustomer =  set.getValue().getJSONArray(5);
                        //[] dataTable = new String[dataMonthly.length() + 4];
                        JSONObject elementYearly;
                        boolean yearFlag = true;
                        boolean contriFlag = true;
                        float totalSales = 0;
                        int j = 0;

                        for (int i = 0; i < dataMonthly.length(); i++) {
                            if (yearFlag) {
                                elementYearly = dataYearly.getJSONObject(i);
                                yearFlag = false;
                                int yVal = elementYearly.getInt("value");
                                BigDecimal number1 = new BigDecimal(yVal);
                                //totalSales = number1.floatValue() / 10000000;
                                totalSales = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                Log.d(TAG, "set.getKey() >" + set.getKey());
                                Log.d(TAG, "totalSales >" + totalSales);
                                table.addCell(String.valueOf(count++));
                                table.addCell(String.valueOf(set.getKey()));
                                table.addCell(Utility.decimal2Palce(Math.abs(totalSales))+" "+GlobalClass.currecyPrefix);

                            }
                            if (contriFlag) {
                                elementYearly = contriData.getJSONObject(i);
                                contriFlag = false;
                                double yVal = elementYearly.getDouble("value");
                                //BigDecimal number1 = new BigDecimal(yVal);
                                //totalSales = number1.floatValue() / 10000000;
                                //totalSales = number1.floatValue(); // GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                Log.d(TAG, "set.getKey() >" + set.getKey());
                                Log.d(TAG, "yVal >" + yVal);
                                // dataTable[j++] = String.valueOf(set.getKey());
                                table.addCell(Utility.decimal2Palce(Math.abs(yVal)));
                            }

                            JSONObject element = dataMonthly.getJSONObject(i);
                            int yVal = element.getInt("value");
                            BigDecimal number1 = new BigDecimal(yVal);
                            // float debitFloat = number1.floatValue() / 10000000;
                            float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                            table.addCell( Utility.decimal2Palce(Math.abs(debitFloat)));

                        }
//                        dataTable[j++]=dataCustomer.getJSONObject(0).getJSONObject("customer").getString("address");
//                        items.add(new NexusWithImage(type, dataTable));
//                        Log.d(TAG, "dataTable >" + dataTable.toString());
//                        Log.d(TAG, "dataTable length >" + dataTable.length);
                    }
//                    table.addCell(String.valueOf(count++));
//                    table.addCell(element.getKey());
//                    table.addCell(Utility.decimal2Palce(Math.abs(element.getValue().getDouble("sales"))));
//                    table.addCell(Utility.decimal2Palce(element.getValue().getDouble("value"))+" %");
//                    totalSale=totalSale+element.getValue().getDouble("sales");
//
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            float totalSales = 0;

            try {
                if (customerFullList != null && !customerFullList.isEmpty() && customerFullList.containsKey("totalProductSales")) {
                    JSONArray setVal = customerFullList.get("totalProductSales");
                    JSONArray dataMonthly = setVal.getJSONArray(1);
                    JSONArray dataYearly = setVal.getJSONArray(3);

                    String[] dataTable = new String[dataMonthly.length() + 4];
                    JSONObject elementYearly;
                    boolean yearFlag = true;
                    //  float totalSales = 0;
                    int j = 0;
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
//                            dataTable[j++] = String.valueOf("Total Sales");
//                            dataTable[j++] = Utility.decimal2Palce(Math.abs(totalSales));
//                            dataTable[j++] = "100";
                        }

                        JSONObject element = dataMonthly.getJSONObject(i);
                        int yVal = element.getInt("value");
                        BigDecimal number1 = new BigDecimal(yVal);
                        // float debitFloat = number1.floatValue() / 10000000;
                        float debitFloat = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                        Log.d(TAG, "debitFloat >" + debitFloat);
                        // dataTable[j++] = Utility.decimal2Palce(Math.abs(debitFloat));

                    }
                    //items.add(new NexusWithImage(type, dataTable));
                    // Log.d(TAG, "dataTable >" + dataTable.toString());
                    // Log.d(TAG, "dataTable length >" + dataTable.length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            PdfPTable ftable = new PdfPTable(noOfDataCell);
            ftable.setWidthPercentage(100);
            float[] columnWidthaa = new float[]{50, 30,20, 50, 30,30,30,30,30,30,30,30,30,30,30,30  };
            ftable.setWidths(columnWidthaa);
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBackgroundColor(myColor1);
            cell = new PdfPCell(new Phrase("Total Sales",font));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);


//

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(Utility.decimal2Palce(Math.abs(totalSales))+" "+GlobalClass.currecyPrefix,font));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
//
//            cell = new PdfPCell(new Paragraph("This is System Generate PDF."));
//            cell.setColspan(noOfDataCell);
//            ftable.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);

            cell.addElement(ftable);

            table.addCell(cell);

            doc.add(table);
            Toast.makeText(context, "Created PDF in CBatDownload Folder", Toast.LENGTH_LONG).show();
            doc.close();


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }
        //  previewPdf();

    }

    public void receiableAgingDetailsPdf(File docsFolder,Map<String,JSONObject> customerAgingFullList)  {

        //use to set background color
        BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
        BaseColor myColor1 = WebColors.getRGBColor("#264553");
        BaseColor colorWhite = WebColors.getRGBColor("#FFFFFF");
        Font font = new Font();
        font.setColor(colorWhite);
        Document doc = new Document();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            String fileName="Customer_Aging"+"_"+ sdf.format(Calendar.getInstance().getTime()) + ".pdf";
            // String fileName="Customer_Details_"+GlobalClass.startDate+"_"+GlobalClass.endDate+".pdf";
            pdfFile = new File(docsFolder.getAbsolutePath(), fileName);
            OutputStream output = new FileOutputStream(pdfFile);
            PdfWriter.getInstance(doc, output);
            doc.open();

            //  document.add(new Paragraph(mContentEditText.getText().toString()));
//create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = context.getResources().getDrawable(R.drawable.logo);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();

            bgImage = Image.getInstance(bitmapdata);
            bgImage.setAbsolutePosition(330f, 642f);
            cell.addElement(bgImage);
            pt.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.addElement(new Paragraph("Customer Aging from "+GlobalClass.startDate+" to "+GlobalClass.endDate));

            cell.addElement(new Paragraph(""));
            cell.addElement(new Paragraph(""));
            pt.addCell(cell);
            cell = new PdfPCell(new Paragraph(""));
            cell.setBorder(Rectangle.NO_BORDER);
            pt.addCell(cell);

            PdfPTable pTable = new PdfPTable(1);
            pTable.setWidthPercentage(100);
            cell = new PdfPCell();
            cell.setColspan(1);
            cell.addElement(pt);
            pTable.addCell(cell);

            int noOfDataCell=8;
            PdfPTable table = new PdfPTable(noOfDataCell);




            float[] columnWidth = new float[]{10,40, 40, 30,30,30,30,30};
            table.setWidths(columnWidth);


            cell = new PdfPCell();


            cell.setBackgroundColor(myColor);
            cell.setColspan(noOfDataCell);
            cell.addElement(pTable);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" "));
            cell.setColspan(noOfDataCell);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);

            cell.setBackgroundColor(myColor1);


            /**
             *           new ItemSortable("Receivable"),
             *                 new ItemSortable("Overdue"),
             *                 new ItemSortable("No Due"),
             *                 new ItemSortable("0-15"),
             *                 new ItemSortable("15-30"),
             *                 new ItemSortable("30-60"),
             *                 new ItemSortable("60-90"),
             *                 new ItemSortable(">90"),
             *                 new ItemSortable("Customer"),
             */

            cell = new PdfPCell(new Phrase("Sr.No",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
//            cell = new PdfPCell(new Phrase("Customer",font));
//            cell.setBackgroundColor(myColor1);
//            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Customer",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Overdue",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("No Due",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("0-15",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("30-60",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("60-90",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(">90",font));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);

            //table.setHeaderRows(3);
            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);
            int count=1;
            double totalSale=0;
            if(customerAgingFullList!=null && !customerAgingFullList.isEmpty() ) {
                for (Map.Entry<String, JSONObject> set : customerAgingFullList.entrySet()) {
                    try {
                            Log.d(TAG, "Key >" + set.getKey());
                            if (!set.getKey().equalsIgnoreCase("totalStateSales")) {
                                JSONObject fullAgaingData = set.getValue();

                                String[] dataTable = new String[9];
                                int j = 0;
                                table.addCell(String.valueOf(count++));
                                double receivable = fullAgaingData.getDouble("receivable");
                                BigDecimal receivablenumber = new BigDecimal(receivable);
                                float receivableFinal = receivablenumber.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                table.addCell(set.getKey());
                                // dataTable[j++] = String.valueOf("Receivable");
                                dataTable[j++] = Utility.decimal2Palce(Math.abs(receivableFinal));

                                double overdue = fullAgaingData.getDouble("overdue");
                                BigDecimal number = new BigDecimal(overdue);
                                float overdueFinal = number.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                table.addCell(Utility.decimal2Palce(Math.abs(overdueFinal)));

                                double noDue = fullAgaingData.getDouble("noDue");
                                BigDecimal number1 = new BigDecimal(noDue);
                                float noDue_Final = number1.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                table.addCell(Utility.decimal2Palce(Math.abs(noDue_Final)));


                                double day15 = fullAgaingData.getDouble("day15");
                                BigDecimal number2 = new BigDecimal(day15);
                                float day15Final = number2.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                table.addCell(Utility.decimal2Palce(Math.abs(day15Final)));

                                double day30 = fullAgaingData.getDouble("day30");
                                BigDecimal number3 = new BigDecimal(day30);
                                float day30Final = number3.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                table.addCell(Utility.decimal2Palce(Math.abs(day30Final)));

                                double day60 = fullAgaingData.getDouble("day60");
                                BigDecimal number4 = new BigDecimal(day60);
                                float day60Final = number4.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                table.addCell( Utility.decimal2Palce(Math.abs(day60Final)));


                                double day90 = fullAgaingData.getDouble("day90");
                                BigDecimal number5 = new BigDecimal(day90);
                                float day90Final = number5.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                table.addCell(Utility.decimal2Palce(Math.abs(day90Final)));

                                double dayGreater90 = fullAgaingData.getDouble("dayGreater90");
                                BigDecimal number6 = new BigDecimal(dayGreater90);
                                float dayGreater90Final = number6.floatValue() / GlobalClass.currencyFormater.get(GlobalClass.currecyPrefix);
                                table.addCell(Utility.decimal2Palce(Math.abs(dayGreater90Final)));


                                //items.add(new NexusWithImage(type, dataTable));
                                Log.d(TAG, "dataTable >" + dataTable.toString());
                                Log.d(TAG, "dataTable length >" + dataTable.length);

                                if(!set.getKey().equalsIgnoreCase("Total Receivable")) {
                                    GlobalClass.salesStateCustomer.add(set.getKey());
                                }
                            }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
            }



            PdfPTable ftable = new PdfPTable(noOfDataCell);
            ftable.setWidthPercentage(100);
            float[] columnWidthaa = new float[]{50,20, 50, 30,30,30,30,30  };
            ftable.setWidths(columnWidthaa);
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBackgroundColor(myColor1);
            //cell = new PdfPCell(new Phrase("Total Sales",font));

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);


//

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            //  cell = new PdfPCell(new Phrase(Utility.decimal2Palce(Math.abs(totalSales)),font));
            cell = new PdfPCell(new Phrase(""));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);

//            cell = new PdfPCell(new Phrase(""));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(myColor1);
//            ftable.addCell(cell);

//
//            cell = new PdfPCell(new Paragraph("This is System Generate PDF."));
//            cell.setColspan(noOfDataCell);
//            ftable.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(noOfDataCell);

            cell.addElement(ftable);

            table.addCell(cell);

            doc.add(table);
            Toast.makeText(context, "Created PDF in CBatDownload Folder", Toast.LENGTH_LONG).show();
            doc.close();


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }
        //  previewPdf();

    }

    public String convertNumberToWords(int n)
    {
        input=numToString(n);
        String converted="";
        int pos=1;
        boolean hun=false;
        while(input.length()> 0)
        {
            if(pos==1) // TENS AND UNIT POSITION
            {   if(input.length()>= 2) // TWO DIGIT NUMBERS
            {
                String temp=input.substring(input.length()-2,input.length());
                input=input.substring(0,input.length()-2);
                converted+=digits(temp);
            }
            else if(input.length()==1) // 1 DIGIT NUMBER
            {
                converted+=digits(input);
                input="";
            }
                pos++;
            }
            else if(pos==2) // HUNDRED POSITION
            {
                String temp=input.substring(input.length()-1,input.length());
                input=input.substring(0,input.length()-1);
                if(converted.length()> 0&&digits(temp)!="")
                {
                    converted=(digits(temp)+maxs[pos]+" and")+converted;
                    hun=true;
                }
                else
                {
                    if
                    (digits(temp)=="");
                    else
                        converted=(digits(temp)+maxs[pos])+converted;hun=true;
                }
                pos++;
            }
            else if(pos > 2) // REMAINING NUMBERS PAIRED BY TWO
            {
                if(input.length()>= 2) // EXTRACT 2 DIGITS
                {
                    String temp=input.substring(input.length()-2,input.length());
                    input=input.substring(0,input.length()-2);
                    if(!hun&&converted.length()> 0)
                        converted=digits(temp)+maxs[pos]+" and"+converted;
                    else
                    {
                        if(digits(temp)=="")  ;
                        else
                            converted=digits(temp)+maxs[pos]+converted;
                    }
                }
                else if(input.length()==1) // EXTRACT 1 DIGIT
                {
                    if(!hun&&converted.length()> 0)
                        converted=digits(input)+maxs[pos]+" and"+converted;
                    else
                    {
                        if(digits(input)=="")  ;
                        else
                            converted=digits(input)+maxs[pos]+converted;
                        input="";
                    }
                }
                pos++;
            }
        }
        return converted;
    }
    private String digits(String temp) // TO RETURN SELECTED NUMBERS IN WORDS
    {
        String converted="";
        for(int i=temp.length()-1;i >= 0;i--)
        {   int ch=temp.charAt(i)-48;
            if(i==0&&ch>1 && temp.length()> 1)
                converted=tens[ch-2]+converted; // IF TENS DIGIT STARTS WITH 2 OR MORE IT FALLS UNDER TENS
            else if(i==0&&ch==1&&temp.length()==2) // IF TENS DIGIT STARTS WITH 1 IT FALLS UNDER TEENS
            {
                int sum=0;
                for(int j=0;j < 2;j++)
                    sum=(sum*10)+(temp.charAt(j)-48);
                return teen[sum-10];
            }
            else
            {
                if(ch > 0)
                    converted=units[ch]+converted;
            } // IF SINGLE DIGIT PROVIDED
        }
        return converted;
    }
    private String numToString(int x) // CONVERT THE NUMBER TO STRING
    {
        String num="";
        while(x!=0)
        {
            num=((char)((x%10)+48))+num;
            x/=10;
        }
        return num;
    }
}
