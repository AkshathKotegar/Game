package com.improstech.housie.app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.improstech.housie.app.R;
import com.improstech.housie.app.helper.CustomAdapter;

import java.util.ArrayList;

/**
 * Created by User2 on 15-07-2017.
 */

public class RegFragment extends Fragment {
    private static final String ARG_POSITION = "position";

    public static RegFragment newInstance(int position) {
        RegFragment f = new RegFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    private RecyclerView rvGameList;
    private CustomAdapter regAdapter;
    private ArrayList<String> regGameArrList = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.gamelist_layout, container, false);
        rvGameList = (RecyclerView) view.findViewById(R.id.rvGameList);
        regGameArrList.add("On 9-07-2017 at 1:00 pm");
        regGameArrList.add("On 9-07-2017 at 4:00 pm");
        regGameArrList.add("On 9-07-2017 at 4:30 pm");
        regGameArrList.add("On 9-07-2017 at 2:00 pm");
        regGameArrList.add("On 9-07-2017 at 4:00 pm");
        regGameArrList.add("On 10-07-2017 at 1:00 pm");
        regGameArrList.add("On 10-07-2017 at 4:00 pm");
        regGameArrList.add("On 10-07-2017 at 4:30 pm");
        regGameArrList.add("On 9-07-2017 at 2:00 pm");
        regGameArrList.add("On 9-07-2017 at 4:00 pm");
        populateToRecyler(rvGameList);
        return view;
    }

    public void populateToRecyler(RecyclerView rv) {
        regAdapter = new CustomAdapter(regGameArrList, R.layout.item_main, Color.parseColor("#940345"));
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(horizontalLayoutManagaer);
        rv.setAdapter(regAdapter);
    }
}


