package com.cbat.cbat.ui.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cbat.cbat.R;


public class UITabView {

	@SuppressLint("InflateParams")
	public static View createTabView(LayoutInflater inflat,final int iconSelectId, final int iconId, String tabname) {
		View tabview = inflat.inflate(R.layout.view_uitabview, null);
		//ImageView img_select_icon = tabview.findViewById(R.id.tab_img_select_icon);
		 final ImageView img_icon = tabview.findViewById(R.id.tab_img_icon);
		TextView tv_name = tabview.findViewById(R.id.tab_tv_name);
		img_icon.setImageResource(iconId);
		//img_select_icon.setImageResource(iconSelectId);
		tv_name.setText(tabname);
//		img_icon.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				img_icon.setImageResource(iconSelectId);
//			}
//		});
//
//'

		return tabview;
	}


}
