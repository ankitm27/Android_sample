package com.cbat.cbat.util;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

public class ChartUtilsCustom {

    public static final int DEFAULT_COLOR = Color.parseColor("#DFDFDF");
    public static final int DEFAULT_DARKEN_COLOR = Color.parseColor("#DDDDDD");
    public static final int CHART1 = Color.parseColor("#2a9d8f");
    public static final int CHART2 = Color.parseColor("#e9c46a");
    public static final int CHART3 = Color.parseColor("#f4a261");
    public static final int CHART4 = Color.parseColor("#e76f51");
    public static final int CHART5 = Color.parseColor("#264553");
    public static final int[] COLORS = new int[]{CHART1, CHART2, CHART3, CHART4, CHART5};
    private static final float DARKEN_SATURATION = 1.1f;
    private static final float DARKEN_INTENSITY = 0.9f;
    private static int COLOR_INDEX = 0;

    public static final int pickColor() {
        return COLORS[(int) Math.round(Math.random() * (COLORS.length - 1))];
    }

    public static final int nextColor() {
        if (COLOR_INDEX >= COLORS.length) {
            COLOR_INDEX = 0;
        }
        return COLORS[COLOR_INDEX++];
    }

    public static int dp2px(float density, int dp) {
        if (dp == 0) {
            return 0;
        }
        return (int) (dp * density + 0.5f);

    }

    public static int px2dp(float density, int px) {
        return (int) Math.ceil(px / density);
    }

    public static int sp2px(float scaledDensity, int sp) {
        if (sp == 0) {
            return 0;
        }
        return (int) (sp * scaledDensity + 0.5f);
    }

    public static int px2sp(float scaledDensity, int px) {
        return (int) Math.ceil(px / scaledDensity);
    }

    public static int mm2px(Context context, int mm) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, mm, context.getResources()
                .getDisplayMetrics()) + 0.5f);
    }

    public static int darkenColor(int color) {
        float[] hsv = new float[3];
        int alpha = Color.alpha(color);
        Color.colorToHSV(color, hsv);
        hsv[1] = Math.min(hsv[1] * DARKEN_SATURATION, 1.0f);
        hsv[2] = hsv[2] * DARKEN_INTENSITY;
        int tempColor = Color.HSVToColor(hsv);
        return Color.argb(alpha, Color.red(tempColor), Color.green(tempColor), Color.blue(tempColor));
    }


}
