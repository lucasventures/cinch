package com.virtualspaces.cinch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by LUCASVENTURES on 6/3/2016.
 */
class MyViewPagerAdapter extends FragmentStatePagerAdapter {


    private static final String TAG = "MyViewPager";
    private ArrayList<FinanceEntryFragment> finFragments;

    public MyViewPagerAdapter(FragmentManager fm, ArrayList<FinanceEntryFragment> fragments) {
        super(fm);
        this.finFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {


        /*
        make circle change within the switch
         */
        Log.d(TAG, "getItem: change circle to ---> "+position);

        switch (position) {
            case 0:
                return finFragments.get(position);
            case 1:
                return finFragments.get(position);
            case 2:
                return finFragments.get(position);
            case 3:
                return finFragments.get(position);
            case 4:
                return finFragments.get(position);
        }

        return null;
    }

    @Override
    public int getCount() {

        return finFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
     return null;
    }

}
