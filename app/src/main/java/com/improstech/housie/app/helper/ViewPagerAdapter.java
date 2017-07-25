package com.improstech.housie.app.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.improstech.housie.app.fragment.InviteFragment;
import com.improstech.housie.app.fragment.OpenFragment;
import com.improstech.housie.app.fragment.RegFragment;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private String[] titles;
    int pos;

    public ViewPagerAdapter(FragmentManager fm, String[] titles2) {
        super(fm);
        titles = titles2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return RegFragment.newInstance(position);
            case 1:
                return InviteFragment.newInstance(position);
            case 2:
                return OpenFragment.newInstance(position);
        }
        return null;
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}