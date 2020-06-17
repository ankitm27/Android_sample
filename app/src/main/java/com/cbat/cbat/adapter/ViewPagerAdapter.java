package com.cbat.cbat.adapter;


import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public  class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG ="ViewPagerAdapter" ;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        //Fragment  fragment =new Fragment();
        Log.d(TAG, "position " + (position));
        Log.d(TAG, "fragment class " + mFragmentList.get(position).getClass());

        return mFragmentList.get(position);
        //return fragment;
    }

    @Override
    public int getCount() {
        Log.d(TAG,"mFragmentList "+mFragmentList.size());
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mFragmentTitleList.get(position);
    }
}