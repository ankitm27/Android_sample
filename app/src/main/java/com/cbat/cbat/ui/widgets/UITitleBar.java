package com.cbat.cbat.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.cbat.cbat.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Bithealth on 2017/3/4.
 */

@SuppressWarnings("unused")
public class UITitleBar extends RelativeLayout implements View.OnClickListener {

    private static final String TAG = "UITitleBar";

    /**
     * ........................ UITitleBarStyle Start ........................
     */

    public static final int UITitleBarStyleMainTitle = 0x01; // 0000 0001
    public static final int UITitleBarStyleLeftTitle = 0x02;// 0000 0010
    public static final int UITitleBarStyleRightTitle = 0x04;// 0000 0100

    @IntDef({UITitleBarStyleMainTitle, UITitleBarStyleLeftTitle,
            UITitleBarStyleRightTitle})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UITitleBarStyle {
    }

    /* ............... end ............... */

    private
    @UITitleBarStyle
    int titleBarStyle = UITitleBarStyleMainTitle;

    public interface ITitleBarDelegate {
        void didClickedTitle(View view, int titleStyle);
    }

    private ITitleBarDelegate titleBarDelegate;

    private TextView leftTitleTextView;
    private TextView mainTitleTextView;
    private TextView rightTitleTextView;

    public UITitleBar(Context context) {
        super(context);
    }

    public UITitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        onViewCreated();

        // 读取系统主题的colorPrimary用作默认背景颜色，如果没有，则设置为0xFF878787
        int defaultBgColor = ContextCompat.getColor(getContext(), R.color.color_titlebar_bg);
        int[] themeAttrs = {android.R.attr.colorPrimary};
        TypedArray themeTypedArray = context.obtainStyledAttributes(themeAttrs);
        if (null != themeTypedArray) {
            int n = themeTypedArray.getIndexCount();

            for (int i = 0; i < n; i++) {
                int attr = themeTypedArray.getIndex(i);

                switch (attr) {
                    case 0:
                        defaultBgColor = themeTypedArray.getColor(attr, defaultBgColor);
                        break;
                }
            }

            themeTypedArray.recycle();
        }

        // 如果xml中没有设置背景，则使用默认背景
        if (getBackground() == null) {
            this.setBackgroundColor(defaultBgColor);
           // this.setBackgroundResource(R.drawable.toolbar_color_gradient);

        }

        // 默认值
        int titleStyle = 0;
        CharSequence titleText = "Title";
        CharSequence leftTitleText = "LeftTitle";
        CharSequence rightTitleText = "RightTitle";
        Drawable leftDrawable = null;
        Drawable rightDrawable = null;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UITitleBar);
        if (null != typedArray) {
            int n = typedArray.getIndexCount();

            for (int i = 0; i < n; i++) {
                int attr = typedArray.getIndex(i);

                switch (attr) {
                    case R.styleable.UITitleBar_titleBarStyle:
                        titleStyle = typedArray.getInt(attr, UITitleBarStyleMainTitle);
                        break;

                    case R.styleable.UITitleBar_mainTitleText:
                        titleText = typedArray.getText(attr);
                        break;

                    case R.styleable.UITitleBar_leftTitleText:
                        leftTitleText = typedArray.getText(attr);
                        break;

                    case R.styleable.UITitleBar_leftTitleDrawable:
                        leftDrawable = typedArray.getDrawable(attr);
                        break;

                    case R.styleable.UITitleBar_rightTitleText:
                        rightTitleText = typedArray.getText(attr);
                        break;

                    case R.styleable.UITitleBar_rightTitleDrawable:
                        rightDrawable = typedArray.getDrawable(attr);
                        break;

                }
            }

            typedArray.recycle();
        }

        if (titleStyle != 0) {
            this.onTitleBarStyleChanged(titleStyle);
        }

        setMainTitleText(titleText);
        setLeftTitleText(leftTitleText);
        setLeftTitleDrawable(leftDrawable);
        setRightTitleText(rightTitleText);
        setRightTitleDrawable(rightDrawable);
    }

    public UITitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // FIXME: 2017/3/5 不知道什么原因：设置指定高度后，子view不能正常显示，只能在xml文件中指定高度
//        int defaultWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
//        int height = 168;// 168px
//        setMeasuredDimension(defaultWidth, height);

    }

    protected void onViewCreated() {
        // 从layout加载布局
        inflate(getContext(), R.layout.widget_uititlebar, this);

        leftTitleTextView = findViewById(R.id.widget_uititlebar_leftTextView);
        mainTitleTextView = findViewById(R.id.widget_uititlebar_titleTextView);
        rightTitleTextView = findViewById(R.id.widget_uititlebar_rightTextView);

        leftTitleTextView.setOnClickListener(this);
        mainTitleTextView.setOnClickListener(this);
        rightTitleTextView.setOnClickListener(this);
    }

    public int getTitleBarStyle() {
        return titleBarStyle;
    }

    public void setTitleBarStyle(int style) {
        onTitleBarStyleChanged(style);
    }

    private void onTitleBarStyleChanged(int style) {
        this.titleBarStyle = style;

        switch (titleBarStyle) {
            // 只有中间标题
            case UITitleBarStyleMainTitle:
                this.setLeftTitleVisibility(GONE);
                this.setMainTitleVisibility(VISIBLE);
                this.setRightTitleVisibility(GONE);
                break;
            // 只有左侧标题
            case UITitleBarStyleLeftTitle:
                this.setLeftTitleVisibility(VISIBLE);
                this.setMainTitleVisibility(GONE);
                this.setRightTitleVisibility(GONE);
                break;
            // 只有右侧标题
            case UITitleBarStyleRightTitle:
                this.setLeftTitleVisibility(GONE);
                this.setMainTitleVisibility(GONE);
                this.setRightTitleVisibility(VISIBLE);
                break;
            // 中间标题 + 左侧标题
            case UITitleBarStyleMainTitle | UITitleBarStyleLeftTitle:
                this.setLeftTitleVisibility(VISIBLE);
                this.setMainTitleVisibility(VISIBLE);
                this.setRightTitleVisibility(GONE);
                break;
            // 中间标题 + 右侧标题
            case UITitleBarStyleMainTitle | UITitleBarStyleRightTitle:
                this.setLeftTitleVisibility(GONE);
                this.setMainTitleVisibility(VISIBLE);
                this.setRightTitleVisibility(VISIBLE);
                break;
            // 左侧标题 + 右侧标题
            case UITitleBarStyleLeftTitle | UITitleBarStyleRightTitle:
                this.setLeftTitleVisibility(VISIBLE);
                this.setMainTitleVisibility(GONE);
                this.setRightTitleVisibility(VISIBLE);
                break;
            // 左侧 + 中间 + 右侧标题
            case UITitleBarStyleMainTitle | UITitleBarStyleLeftTitle |
                    UITitleBarStyleRightTitle:
                this.setLeftTitleVisibility(VISIBLE);
                this.setMainTitleVisibility(VISIBLE);
                this.setRightTitleVisibility(VISIBLE);
                break;

        }
    }

    private void updateViewClickable(TextView v) {
        if (null != v) {
            if (v.getVisibility() == VISIBLE)
                v.setClickable(true);
            else {
                v.setClickable(false);
            }
        }
    }

    /**
     * ........................ Title Start ........................
     */

    public void setMainTitleText(CharSequence text) {
        if (null != text) {
            mainTitleTextView.setText(text);
        }
    }

    public void setMainTitleTextColor(int color) {
        mainTitleTextView.setTextColor(color);
    }

    public void setMainTitleVisibility(int visibility) {
        if (null != mainTitleTextView) {
            mainTitleTextView.setVisibility(visibility);
            updateViewClickable(mainTitleTextView);
        }
    }

    /* ............... end ............... */

    /**
     * ........................ LeftTitle Start ........................
     */

    public void setLeftTitleText(CharSequence text) {
        if (null != text && leftTitleTextView != null) {
            leftTitleTextView.setText(text);
        }
    }

    public void setLeftTitleTextColor(int color) {
        leftTitleTextView.setTextColor(color);
    }

    public void setLeftTitleDrawable(int res) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), res);
        setLeftTitleDrawable(drawable);
    }

    public void setLeftTitleDrawable(Drawable drawable) {
        //null != drawable &&
        if (leftTitleTextView != null) {
            leftTitleTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null,
                    null, null);
        }
    }

    public void setLeftTitleVisibility(int visibility) {
        if (null != leftTitleTextView) {
            leftTitleTextView.setVisibility(visibility);
            updateViewClickable(leftTitleTextView);
        }
    }

    /* ............... end ............... */


    /**
     * ........................ Right Title Start ........................
     */

    public TextView getRightTitleTextView() {
        return rightTitleTextView;
    }

    public void setRightTitleText(CharSequence text) {
        if (null != text) {
            rightTitleTextView.setText(text);
        }
    }

    public void setRightTitleTextColor(int color) {
        rightTitleTextView.setTextColor(color);
    }

    public void setRightTitleDrawable(int res) {
        Drawable drawable;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(res, getContext().getTheme());
        } else {
            drawable = getResources().getDrawable(res);
        }
        setRightTitleDrawable(drawable);
    }

    public void setRightTitleDrawable(Drawable drawable) {
        if (null != drawable && null != rightTitleTextView) {
//            Log.d(TAG, "setRightTitleDrawable: executed!");
//            rightTitleTextView.setCompoundDrawables(null, null, drawable, null);
            rightTitleTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    drawable, null);
        }
    }
    
    public void setRightTitleBackground(Drawable drawable){
        if (null != drawable && null != rightTitleTextView) {
//            Log.d(TAG, "setRightTitleDrawable: executed!");
//            rightTitleTextView.setCompoundDrawables(null, null, drawable, null);
            rightTitleTextView.setBackground(drawable);
        }
    }

    public void setRightTitleVisibility(int visibility) {
        if (null != rightTitleTextView) {
            rightTitleTextView.setVisibility(visibility);
            updateViewClickable(rightTitleTextView);
        }
    }

    /* ............... end ............... */

    public void setTitleBarDelegate(ITitleBarDelegate titleBarDelegate) {
        Log.d("titalebar>>>>>>>>","###########  "+titleBarDelegate.getClass().toString());
        this.titleBarDelegate = titleBarDelegate;
    }

    @Override
    public void onClick(View view) {
        Log.d("view.getId()",String.valueOf(view.getId()));
        Log.d("R.id.leftTextView",String.valueOf(R.id.widget_uititlebar_leftTextView));
        Log.d("R.id.rightTextView",String.valueOf(R.id.widget_uititlebar_rightTextView));
        Log.d("R.id.titlaTextView",String.valueOf(R.id.widget_uititlebar_titleTextView));

        int title = 0;
        switch (view.getId()) {
            case R.id.widget_uititlebar_leftTextView:
                title = UITitleBarStyleLeftTitle;
                break;

            case R.id.widget_uititlebar_rightTextView:
                title = UITitleBarStyleRightTitle;
                break;

            case R.id.widget_uititlebar_titleTextView:
                title = UITitleBarStyleMainTitle;
                break;
        }
        Log.d("R.id.titleBarDelegate",String.valueOf(titleBarDelegate));
        Log.d("R.id.title",String.valueOf(title));

        if (titleBarDelegate != null && title != 0) {
            titleBarDelegate.didClickedTitle(view, title);
        }

    }

}
