package com.cobanogluhasan.inguplift;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.cobanogluhasan.inguplift.Adapter.SynonymAdapter;
import com.cobanogluhasan.inguplift.AdmobAds.AdmobAds;
import com.cobanogluhasan.inguplift.Interfaces.MediaPlayerListener;
import com.cobanogluhasan.inguplift.PlayAudioManager.PlayAudioManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;


public class SynonymActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {


    private static final String TAG = "SynonymActivity";
    SynonymAdapter adapter;
    private ArrayList<String> synonymList = new ArrayList<>();
    private ArrayList<String> synonymExampleList = new ArrayList<>();
    DiscreteScrollView scrollView;
    String url = "";
    InfiniteScrollAdapter wrapper;
    ArrayList<String> synonym;
    ArrayList<String> synonymExample;

    String days;
    int[] clickedNumber = new int[2];
    TextToSpeech textToSpeech;
    SharedPreferences langPreferences;
    private   String mWord="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synonym);
        langPreferences=this.getSharedPreferences("com.cobanogluhasan.inguplift", Context.MODE_PRIVATE);

        String defaultLang = langPreferences.getString("language", "");


        loadBanner(this);

        if(defaultLang.equals("turkish")) {

            getSupportActionBar().setTitle("Eş Anlamlı Kelimeler");

        }
        else {
            getSupportActionBar().setTitle("Synonym Words");
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        scrollView = findViewById(R.id.scroll);

        adapter = new SynonymAdapter(this, synonymList, synonymExampleList);
        wrapper = InfiniteScrollAdapter.wrap(adapter);

        scrollView.setAdapter(adapter);
        scrollView.setAdapter(wrapper);

        textToSpeech = new TextToSpeech(this, this);
        textToSpeech.setLanguage(Locale.US);



        scrollViewSet();
        getIncomingIntent();
        textGet();

    }



    public void scrollViewSet(){

        scrollView.setOverScrollEnabled(true);
        scrollView.setSlideOnFlingThreshold(800);

        scrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.1f)
                .setMinScale(0.6f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build());

        scrollView.addScrollStateChangeListener(new DiscreteScrollView.ScrollStateChangeListener<RecyclerView.ViewHolder>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onScrollStart(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                 i = wrapper.getRealCurrentPosition();
                String word =  synonymList.get(i);
                String splitWord[] = word.split(" ", 2);


                try {

                     mWord = splitWord[0].toLowerCase();

                    url="http://packs.shtooka.net/eng-wcp-us/mp3/En-us-"+mWord+ ".mp3";

                    PlayAudioManager.playAudio(getApplicationContext(), url, new MediaPlayerListener() {
                        @Override
                        public void onErrorListener(boolean isError) {
                            if(isError){
                                Toast.makeText(SynonymActivity.this, "Check your internet connection!\n Please Waiting!", Toast.LENGTH_SHORT).show();
                                textToSpeech.speak(mWord, TextToSpeech.QUEUE_ADD, null);
                            }

                        }
                    });





                } catch (Exception e) {
                e.printStackTrace();

                }

            }

            @Override
            public void onScrollEnd(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                AdmobAds.displayInterstitialAds(getApplicationContext());

            }

            @Override
            public void onScroll(float v, int i, int i1, @Nullable RecyclerView.ViewHolder viewHolder, @Nullable RecyclerView.ViewHolder t1) {


            }
        });









    }




    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking the incoming intents");

        if (getIntent().hasExtra("Days")) { //check if there's incoming intent otherwise the app will crash
            Log.d(TAG, "getIncomingIntent: found extras");
            days = getIntent().getStringExtra("Days");


            try {
                String regex = "[^\\d]+";
                String[] str = days.split(regex);

                Log.i(TAG, "getIncomingIntent: " + str[1]);
                clickedNumber[0] = Integer.parseInt(str[1]);
                clickedNumber[1]=clickedNumber[0];
            } catch (Exception e) {
                String s = days.replaceAll("(\\d+).+", "$1");
                Log.i(TAG, "getIncomingIntent: " + s);
                clickedNumber[1] = Integer.parseInt(s);
                clickedNumber[0]=clickedNumber[1];
            }


        }

    }


    private void textGet() {
        getSynonym();
        getSynonymExamples();


        int i=0;

        try {


            while (i < 248) {
                if (i / 10 + 1 == clickedNumber[1]) {
                    synonymList.add(synonym.get(i));
                    if (i != 0) {
                        synonymExampleList.add(synonymExample.get(2 * i) + "\n\n" + synonymExample.get(2 * i + 1));
                    } else
                        synonymExampleList.add(synonymExample.get(0) + "\n\n" + synonymExample.get(1));
                }
                i++;
            }
        }catch (Exception e)
        {
            Log.i(TAG, "textGet: Exception:" + e);
        }


    }


    public void getSynonym() {


        synonym = new ArrayList<String>();

        BufferedReader dict = null; //Holds the dictionary file
        AssetManager am = this.getAssets();

        try {
            //dictionary.txt should be in the assets folder.
            dict = new BufferedReader(new InputStreamReader(am.open("synonym.txt")));

            String word;
            while ((word = dict.readLine()) != null) {

                synonym.add(word);

            }
            dict.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }





    public void getSynonymExamples() {


        synonymExample = new ArrayList<String>();

        BufferedReader dict = null; //Holds the dictionary file
        AssetManager am = this.getAssets();

        try {
            //dictionary.txt should be in the assets folder.
            dict = new BufferedReader(new InputStreamReader(am.open("synonymexamples.txt")));

            String word;
            while ((word = dict.readLine()) != null) {
                synonymExample.add(word);

            }
            dict.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    @Override
    public void onInit(int status) {

    }






    public void loadBanner(Context context) {
        //noinspection deprecation
        MobileAds.initialize(context, context.getResources().getString(R.string.admob_app_id));

        AdView mAdview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

    }









}