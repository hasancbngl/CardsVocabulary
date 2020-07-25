package com.cobanogluhasan.inguplift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.cobanogluhasan.inguplift.Adapter.DayListAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class DaysActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ArrayList<String> mDays = new ArrayList<>();

    SharedPreferences langPreferences;
    String defaultLang="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days);
        langPreferences=this.getSharedPreferences("com.cobanogluhasan.inguplift", Context.MODE_PRIVATE);


        loadBanner(this);



    initDayList();

        if(defaultLang.equals("turkish")) {
            getSupportActionBar().setTitle("Gün");
        }

        else if(defaultLang.equals("english")) {

            getSupportActionBar().setTitle("Day");

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void initDayList() {

        defaultLang=langPreferences.getString("language", "");

        if(defaultLang.equals("turkish")) {

            for(int i=1; i<=25;i++) {
                mDays.add(i + ".Gün");
            }
        }

        else if(defaultLang.equals("english")) {

            for(int i=1; i<=25;i++) {
                mDays.add("Day:" + i);
            }

        }


        initRecyclerView();

    }

    private void initRecyclerView() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        DayListAdapter adapter = new DayListAdapter(mDays,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }




    public void loadBanner(Context context) {
        //noinspection deprecation
        MobileAds.initialize(context, context.getResources().getString(R.string.admob_app_id));

        AdView mAdview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

    }




}