package com.cbat.cbat.adapter.original_sortable;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.cbat.cbat.R;
import com.cbat.cbat.adapter.TableFixHeaderAdapter;
import com.cbat.cbat.adapter.TableFixHeaderAdapter;


/**
 * Created by miguel on 06/03/2016.
 */
public class OriginalFirstBodyCellViewGroup extends FrameLayout
        implements
        TableFixHeaderAdapter.FirstBodyBinder<NexusWithImage> {
  String  TAG="OriginalFirstBodyCellViewGroup";
    private Context context;
    public TextView textView;
    public View vg_root;
    public ImageView imageIdicator;
    public ImageView dottedLine;
    public OriginalFirstBodyCellViewGroup(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public OriginalFirstBodyCellViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.text_view_group, this, true);
        textView = (TextView) findViewById(R.id.tv_text);
        vg_root = findViewById(R.id.vg_root);
        imageIdicator=findViewById(R.id.iv_image_idicator);
        dottedLine=findViewById(R.id.dottedLine);
    }

    @Override
    public void bindFirstBody(NexusWithImage item, int row) {
        textView.setText(item.data[0]);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        //vg_root.setBackgroundResource(R.drawable.cell_gray_border_bottom_right_gray);
        vg_root.setBackgroundResource(R.drawable.no_cell_border);
        Log.d(TAG,"item.indicatorColor"+item.indicatorColor);
        if(item.indicatorColor!=null && !item.indicatorColor.isEmpty()) {
            imageIdicator.setBackgroundColor(Color.parseColor(item.indicatorColor));
        }else{
            imageIdicator.setBackgroundColor(row % 2 == 0 ? getResources().getColor(R.color.chart5) : getResources().getColor(R.color.chart3));

        }
    }
}
