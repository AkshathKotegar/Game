package com.improstech.housie.app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.improstech.housie.app.R;
import com.improstech.housie.app.helper.ViewPagerAdapter;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

/**
 * Created by User2 on 18-07-2017.
 */

public class HomeFragment extends Fragment {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private TabLayout tab;
    private ViewPager pager;
    private String[] titles = new String[]{"Registered Games", "Invited Games", "Open Games"};
    private AdView mAdView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        pager = (ViewPager) view.findViewById(R.id.viewpager);
        tab = (TabLayout) view.findViewById(R.id.tab);
        pager.setAdapter(new ViewPagerAdapter(getActivity().getSupportFragmentManager(), titles));
        tab.setupWithViewPager(pager);
        tab.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        tab.setTabTextColors(Color.parseColor("#44DCDADA"), Color.parseColor("#ffffff"));
        mAdView = new AdView(getActivity());
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");//ca-app-pub-7564172399491583/2469127557  ca-app-pub-3940256099942544/6300978111
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.adLayout);
        layout.setVisibility(View.VISIBLE);
        layout.setBackgroundColor(Color.BLACK);
        layout.addView(mAdView);
        AdRequest adRequestBanner = new AdRequest.Builder()
                .build();

        mAdView.loadAd(adRequestBanner);
        return view;
    }
}















































/*BottomBar bottomBar = (BottomBar) view.findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_registered:
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_frame, RegFragment.newInstance(), "MainActivity")
                                .commit();
                        break;
                    case R.id.tab_invited:
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_frame, RegFragment.newInstance(), "MainActivity")
                                .commit();
                        break;
                    case R.id.tab_open:
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_frame, RegFragment.newInstance(), "MainActivity")
                                .commit();
                        break;

                }
            }
        });*/