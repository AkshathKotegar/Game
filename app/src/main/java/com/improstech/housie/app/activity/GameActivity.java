package com.improstech.housie.app.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.improstech.housie.app.R;
import com.improstech.housie.app.helper.CustomAdapter;
import com.improstech.housie.app.helper.GridAdapter;
import com.improstech.housie.app.helper.Webservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ch.halcyon.squareprogressbar.SquareProgressBar;

/**
 * Created by User2 on 19-07-2017.
 */

public class GameActivity extends AppCompatActivity {
    private AdView mAdView;
    private ArrayList<String> nosToRecycler = new ArrayList<String>();
    private RecyclerView horizontal_recycler_view;
    private CustomAdapter horizontalAdapter;
    private ArrayList<String> nosArrlist = new ArrayList<String>();
    GridView gridNos;
    static final String[] numbers = new String[]{
            "1", "12", " ", " ", "42", " ", "64", " ", " ",
            " ", " ", " ", "36", " ", "54", "68", "71", "83",
            " ", "16", "22", " ", " ", " ", " ", "80", " "
    };
    Button btnAllNumbers, ticket1, ticket2, ticket3;
    public static ArrayList<String> selectedNosArr = new ArrayList<String>();
    // public static boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        btnAllNumbers = (Button) findViewById(R.id.btnAllNumbers);
        horizontal_recycler_view = (RecyclerView) findViewById(R.id.recyclerViewDrawnNos);
        gridNos = (GridView) findViewById(R.id.gridView);
        //if (Webservice.isNetworkStatusAvialable(GameActivity.this))
        setAdView();
        populateInitialNos();
        //populateDrawnNos();

        btnAllNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    Runnable helloRunnable, hello;
    ScheduledExecutorService executor, executor1;

    @Override
    protected void onResume() {
        super.onResume();
        /*if (GameActivity.clicked) {
            clicked = false;
            populateInitialNos();
        }*/


        helloRunnable = new Runnable() {
            public void run() {
                Log.d("exe", "hello world");

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //int n = generateNo();
                        long no=Math.round(Math.random()*100);
                        populateDrawnNos(String.valueOf(no));
                    }
                });
            }
        };
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 15, TimeUnit.SECONDS);


        hello = new Runnable() {
            public void run() {
                Log.d("exe", "hello world");
                runOnUiThread(new Runnable() {
                    public void run() {
                        ArrayAdapter<String> adapter = new GridAdapter(GameActivity.this, R.layout.grid_tables, nosArrlist, selectedNosArr, nosToRecycler);
                        gridNos.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        executor1 = Executors.newScheduledThreadPool(1);
        executor1.scheduleAtFixedRate(hello, 0, 1, TimeUnit.SECONDS);

    }

    public int generateNo() {
        Random r = new Random(System.currentTimeMillis());
        return ((1 + r.nextInt(2)) * 10 + r.nextInt(10));
    }

    private void populateInitialNos() {
        for (int i = 0; i < numbers.length; i++)
            nosArrlist.add(numbers[i]);
        ArrayAdapter<String> adapter = new GridAdapter(GameActivity.this, R.layout.grid_tables, nosArrlist, selectedNosArr, nosToRecycler);
        gridNos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void populateDrawnNos(String no) {
        nosToRecycler.add(no);
        Log.d("exe-r", nosToRecycler.toString());
        Collections.reverse(nosToRecycler);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(GameActivity.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);
        horizontalAdapter = new CustomAdapter(nosToRecycler, R.layout.item, Color.parseColor("#D0583B"));
        horizontal_recycler_view.setAdapter(horizontalAdapter);
        horizontalAdapter.notifyDataSetChanged();

    }

    private void showDialog() {
        final Dialog dial;
        Button close;
        GridView gridView;
        ArrayList<String> nos1to90 = new ArrayList<String>();
        for (int i = 1; i <= 90; i++) {
            nos1to90.add(String.valueOf(i));
        }
        dial = new Dialog(GameActivity.this);
        getWindow().setGravity(Gravity.BOTTOM);
        dial.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dial.setContentView(R.layout.custom_dialog_allnos);
        gridView = (GridView) dial.findViewById(R.id.gridView);
        close = (Button) dial.findViewById(R.id.close);
        ArrayAdapter<String> adapter = new GridAdapter(GameActivity.this, R.layout.grid_all_nos, nos1to90, nosToRecycler, nosToRecycler);
        gridView.setAdapter(adapter);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dial.dismiss();
            }
        });
        dial.show();
    }

    private void setAdView() {
        mAdView = new AdView(GameActivity.this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");//ca-app-pub-7564172399491583/2469127557  ca-app-pub-3940256099942544/6300978111
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.adLayout);
        layout.setGravity(RelativeLayout.CENTER_HORIZONTAL);
        layout.setVisibility(View.VISIBLE);
        layout.setBackgroundColor(Color.BLACK);
        layout.addView(mAdView);
        AdRequest adRequestBanner = new AdRequest.Builder()
                .build();

        mAdView.loadAd(adRequestBanner);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        selectedNosArr.clear();
        executor.shutdownNow();
        executor1.shutdownNow();
        finish();
    }


    //Timer timer = new Timer();
    //timer.schedule(new SayHello(), 0, 5000);
    class SayHello extends TimerTask {
        public void run() {
            System.out.println("Hello World!");
        }
    }
}



/*  Runnable helloRunnable = new Runnable() {
            public void run() {
                Log.d("exe", "hello world");

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int n = generateNo();
                                populateDrawnNos(String.valueOf(n));
                            }
                        }, 1000);

                    }
                });
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 15, TimeUnit.SECONDS);*/