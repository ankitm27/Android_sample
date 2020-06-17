package jp.gr.java_conf.androtaku.geomap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by takuma on 2015/07/18.
 */
public class GeoMapView extends ImageView {
    private static final String TAG ="GeoMapView" ;
    private List<CountrySection> countrySections;
    private Context context;
    private Paint defaultPaint;
    private Thread prepareThread = null;
    private Thread thread = null;
    private HashMap<String, Paint> countryPaints;
    private HashMap<String, String> sales;

    private OnInitializedListener listener;
    boolean freeTouched = false;
    Path freePath;
    Path selectedPath;
    Matrix identityMatrix;
    Bitmap myCanvasBitmap = null;
    Canvas myCanvas = null;
    float x, y;
    ArrayList<Path> paths = new ArrayList<Path>();
    HashMap<Path, String> pathsMap = new HashMap<Path, String>();

    public GeoMapView(Context context) {
        super(context);
        this.context = context;
        countryPaints = new HashMap<>();
        sales = new HashMap<>();
        initialize();
    }

    public GeoMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        countryPaints = new HashMap<>();
        sales = new HashMap<>();
        initialize();
    }

    /**
     * initialize GeoMapView from world.svg on other thread
     */
    private void initialize() {
        defaultPaint = new Paint();
        defaultPaint.setColor(Color.BLACK);
        defaultPaint.setStyle(Paint.Style.STROKE);
        defaultPaint.setAntiAlias(true);

        final Handler handler = new Handler();

        prepareThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //parse world.svg
                countrySections = SVGParser.getCountrySections(context);
//                for (CountrySection cs : countrySections) {
//                  //  Log.d("countery index", cs.getCountryCode());
//                }
                //create bitmap
                final Bitmap bitmap = Bitmap.createBitmap(GeoMapView.this.getWidth(),
                        GeoMapView.this.getHeight(), Bitmap.Config.ARGB_4444);
                //draw map on bitmap
                final Canvas canvas = new Canvas(bitmap);
                myCanvasBitmap = bitmap;
                myCanvas = canvas;
                identityMatrix = new Matrix();

                setMeasuredDimension(GeoMapView.this.getWidth(), GeoMapView.this.getHeight());
                //run on main thread
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        GeoMapView.this.setImageBitmap(bitmap);

                        GeoMapView.this.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Log.d("path", "Cliced");
                                //Creating the instance of PopupMenu
                                // selectTab(R.id.threeM_tab);

                            }
                        });
                        if(listener!=null) {
                            listener.onInitialized(GeoMapView.this);
                        }
                    }
                });
            }
        });
        prepareThread.start();
    }

    /**
     * draw map on canvas
     *
     * @param canvas target canvas
     */
    private void drawMap(Canvas canvas) {
        float ratio = (float) canvas.getWidth() / SVGParser.xMax;
       // Log.d("ratio", String.valueOf(ratio));
        for (CountrySection countrySection : countrySections) {
            Log.d(TAG,"countrySections"+countrySection.getCountryCode());
            float centerX=0,centerY=0;
            List<List<Float>> xPathList = countrySection.getXPathList();
            List<List<Float>> yPathList = countrySection.getYPathList();
            int numList = xPathList.size();
            for (int i = 0; i < numList; ++i) {
                Path path = new Path();
                path.moveTo(xPathList.get(i).get(0) * ratio, yPathList.get(i).get(0) * ratio);
                int numPoint = xPathList.get(i).size();
                for (int j = 1; j < numPoint; ++j) {
                    path.lineTo(xPathList.get(i).get(j) * ratio, yPathList.get(i).get(j) * ratio);
                }
                Paint paint = countryPaints.get(countrySection.getCountryCode());
                // Paint paintText = countryPaints.get(countrySection.getCountryCode());

                if (paint != null) {
             //       Log.d("path", String.valueOf(path));
                    canvas.drawPath(path, paint);
//                    Paint paintText = new Paint();
//                    paintText.setColor(Color.BLACK);
//                   // paintText.setStyle(Paint.Style.FILL);
//                   // paintText.setAntiAlias(true);
//                    RectF pBounds = new RectF();
//                    path.computeBounds(pBounds, true);
//                    Log.d("Center X:Y",String.valueOf(pBounds.centerX())+" : "+String.valueOf(pBounds.centerX()));
//                    paintText.setTextSize(20);
//                    canvas.drawText(countrySection.getCountryCode(),pBounds.centerX(),pBounds.centerY(),paintText);
                    Paint paintText = new Paint();
                    paintText.setColor(Color.BLACK);
                    paintText.setTextSize(26);
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    // paintText.setAntiAlias(true);
                    RectF pBounds = new RectF();
                    path.computeBounds(pBounds, true);
                    Log.d(TAG, "Countey  >> "+countrySection.getCountryCode()+" : "+String.valueOf(pBounds.centerX()) + " : " + String.valueOf(pBounds.centerX()));
if(i==numList-1) {
    canvas.drawText(countrySection.getCountryCode(), (pBounds.centerX() - 40), (pBounds.centerY() + 10), paintText);
}
                    //centerX=(pBounds.centerX() - 30);
                    //centerY=(pBounds.centerY() + 10);
                    //canvas.drawText(sales.get(countrySection.getCountryCode()), (pBounds.centerX() - 30), (pBounds.centerY() + 10), paintText);
                    canvas.drawPath(path, defaultPaint);
                } else {
                    canvas.drawPath(path, defaultPaint);
                }
                paths.add(path);
                pathsMap.put(path, countrySection.getCountryCode());

            }
//            Paint paintText = new Paint();
//            paintText.setColor(Color.BLACK);
//            paintText.setTextSize(26);
//            canvas.drawText(countrySection.getCountryCode(),centerX, centerY, paintText);
        }
    }

    /**
     * set filling color
     *
     * @param countryCode target country code
     * @param color       filling color
     */
    public void setCountryColor(String countryCode, String color, String totalSale) {
        Paint paint = new Paint();
        Log.d("color > ",color);
        paint.setColor(Color.parseColor(color));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        //countryPaints.put(countryCode, defaultPaint);
        countryPaints.put(countryCode, paint);
        //countryPaints.put(countryCode, defaultPaint);
        sales.put(countryCode, totalSale);
    }

    /**
     * set filling color
     *
     * @param countryCode target country code
     * @param red         0 to 255
     * @param green       0 to 255
     * @param blue        0 to 255
     */
    public void setCountryColor(String countryCode, int red, int green, int blue) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(red, green, blue));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        countryPaints.put(countryCode, paint);

    }

    /**
     * remove filling color
     *
     * @param countryCode target country code
     */
    public void removeCountryColor(String countryCode) {
        countryPaints.remove(countryCode);
    }

    /**
     * clear all filling color
     */
    public void clearCountryColor() {
        countryPaints = new HashMap<>();
    }

    /**
     * refresh GeoMapView
     * you need call this method after initialized
     */
    public void refresh() {
        final Handler handler = new Handler();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = Bitmap.createBitmap(GeoMapView.this.getWidth()+60,
                        GeoMapView.this.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawMap(canvas);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        GeoMapView.this.setImageBitmap(bitmap);
                    }
                });
            }
        });
        thread.start();
    }

    /**
     * stop all threads
     */
    public void destroy() {
        if (prepareThread != null) {
            prepareThread.interrupt();
            prepareThread = null;
        }
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    /**
     * set OnInitializedListener
     *
     * @param listener
     */
    public void setOnInitializedListener(OnInitializedListener listener) {
        this.listener = listener;
    }

//    public Bitmap get(){
//        return this.getDrawingCache();
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //screen touch get x of the touch event
                x = event.getX();
                //screen touch get y of the touch event
                y = event.getY();

                //check if touch point intersects the path bounds
                for (Path p : paths) {
                    RectF pBounds = new RectF();
                    p.computeBounds(pBounds, true);
                    if (pBounds.contains(x, y)) {
                        //select path
                        selectedPath = p;// where selectedPath is assumed declared.
                        String stateCode = pathsMap.get(selectedPath);
                       // Log.d("touch", "X : " + x + " Y : " + y);
                        //Log.d("stateCode", stateCode);

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

    public ArrayList<Path> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<Path> paths) {
        this.paths = paths;
    }

    public HashMap<Path, String> getPathsMap() {
        return pathsMap;
    }

    public void setPathsMap(HashMap<Path, String> pathsMap) {
        this.pathsMap = pathsMap;
    }
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        switch(event.getAction()){
//            case MotionEvent.ACTION_UP:
//                freeTouched = false;
//                break;
//            case MotionEvent.ACTION_DOWN:
//                freeTouched = true;
//                Log.d("touch","X : "+event.getX()+" Y : "+ event.getY());
//
//               // freePath = new Path();
//                //freePath.moveTo(event.getX(), event.getY());
//
//                //myCanvasBitmap.eraseColor(Color.BLACK);
//
//                break;
////            case MotionEvent.ACTION_MOVE:
////                //freePath.lineTo(event.getX(), event.getY());
////                invalidate();
////                break;
//        }
//
//        return true;
//    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        int w = MeasureSpec.getSize(widthMeasureSpec);
//        int h = MeasureSpec.getSize(heightMeasureSpec);
//
////        myCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
////        myCanvas = new Canvas();
////        myCanvas.setBitmap(myCanvasBitmap);
//
//        final Bitmap bitmap = Bitmap.createBitmap(GeoMapView.this.getWidth(),
//                GeoMapView.this.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        drawMap(canvas);
//        identityMatrix = new Matrix();
//
//        setMeasuredDimension(w, h);
//    }

//    public Bitmap getCanvasBitmap(){
//
//        return myCanvasBitmap;
//
//    }
}
