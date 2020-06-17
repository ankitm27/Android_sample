package com.cbat.cbat.adapter.original_sortable;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbat.cbat.R;
import com.cbat.cbat.adapter.TableFixHeaderAdapter;


/**
 * Created by miguel on 09/02/2016.
 */
public class OriginalBodyCellViewGroup extends FrameLayout
        implements
        TableFixHeaderAdapter.BodyBinder<NexusWithImage> {

    private Context context;
    public TextView textView;
    public View vg_root;
    public ImageView imageIdicator;
    public ImageView dottedLine;


    public OriginalBodyCellViewGroup(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public OriginalBodyCellViewGroup(Context context, AttributeSet attrs) {
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
    public void bindBody(NexusWithImage item, int row, int column) {

        textView.setText(item!=null && item.data!=null && item.data.length>0?item.data[column + 1]:"");
        textView.setTypeface(null, Typeface.NORMAL);
        textView.setGravity(Gravity.CENTER);
        vg_root.setBackgroundResource(R.drawable.no_cell_border);
        imageIdicator.setVisibility(GONE);
        dottedLine.setPadding(0,0,0,0);
       // vg_root.setBackgroundResource(R.drawable.cell_white_border_bottom_right_gray);
        imageIdicator.setBackgroundColor(row % 2 == 0 ? getResources().getColor(R.color.chart5 ):getResources().getColor(R.color.chart3 ));
    }
}
