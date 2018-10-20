package com.virtualspaces.cinch.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.virtualspaces.cinch.fragments.NotepadFragment;
import com.virtualspaces.cinch.fragments.SummaryFragment;

/**
 * Created by LUCASVENTURES on 5/21/2016.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private final int COUNT = 2;
    private String[] tabTitles = {"Summary", "My Notepad"};
    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new SummaryFragment();
            case 1:
                return new NotepadFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
