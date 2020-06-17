package com.cbat.cbat.adapter;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbat.cbat.R;
import com.github.florent37.materialleanback.MaterialLeanBack;


/**
 * Created by florentchampigny on 28/08/15.
 */
public class TestViewHolder extends MaterialLeanBack.ViewHolder {

    public TextView textView;
    public TextView saleAmount;
    public FrameLayout cardFrame;

    public TestViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.textView);
        saleAmount = (TextView) itemView.findViewById(R.id.saleAmount);
        cardFrame= (FrameLayout) itemView.findViewById(R.id.cardFrame);
    }
}
