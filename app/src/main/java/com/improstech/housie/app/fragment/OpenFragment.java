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

public class OpenFragment extends Fragment {
    private static final String ARG_POSITION = "position";

    public static OpenFragment newInstance(int position) {
        OpenFragment f = new OpenFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    private RecyclerView rvGameList;
    private CustomAdapter openAdapter;
    private ArrayList<String> openGameArrlist = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.gamelist_layout, container, false);

        rvGameList = (RecyclerView) view.findViewById(R.id.rvGameList);
        openGameArrlist.add("On 11-07-2017 at 1:00 pm");
        openGameArrlist.add("On 11-07-2017 at 4:00 pm");
        openGameArrlist.add("On 11-07-2017 at 4:30 pm");
        openGameArrlist.add("On 12-07-2017 at 2:00 pm");
        openGameArrlist.add("On 14-07-2017 at 4:00 pm");
        openGameArrlist.add("On 16-07-2017 at 1:00 pm");
        openGameArrlist.add("On 16-07-2017 at 4:00 pm");
        openGameArrlist.add("On 19-07-2017 at 4:30 pm");
        openGameArrlist.add("On 9-07-2017 at 2:00 pm");
        openGameArrlist.add("On 9-07-2017 at 4:00 pm");
        populateToRecyler(rvGameList);
        return view;
    }

    public void populateToRecyler(RecyclerView rv) {
        openAdapter = new CustomAdapter(openGameArrlist, R.layout.item_main, Color.parseColor("#940345"));
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(horizontalLayoutManagaer);
        rv.setAdapter(openAdapter);
    }
}


