package com.cobanogluhasan.inguplift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cobanogluhasan.inguplift.Adapter.CardAdapter;
import com.cobanogluhasan.inguplift.AdmobAds.AdmobAds;
import com.cobanogluhasan.inguplift.Interfaces.MediaPlayerListener;
import com.cobanogluhasan.inguplift.Model.Model;
import com.cobanogluhasan.inguplift.PlayAudioManager.PlayAudioManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.huxq17.swipecardsview.SwipeCardsView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class LearnWordsActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {


    private SwipeCardsView swipeCardsView1;
    private List<Model> modelList = new ArrayList<>();
    ArrayList<String> dictionary;
    ArrayList<String> definition;
    int number;
    Button startButton;
    TextView dayTextView;
    int counter = 0;
    TextView textView5;
    Button repeatButton;
    boolean valueForRepeat=false;
    int[] numberOfWords;
    Random random;
    String url="";
    TextView currentnoTextview;

    TextToSpeech textToSpeech;
    SharedPreferences sharedPreferences;

    private String keep="";
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_words);

        getSupportActionBar().setTitle("Learn 10 words a day");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        loadBanner(this);


      sharedPreferences = this.getSharedPreferences("com.cobanogluhasan.inguplift", Context.MODE_PRIVATE);

         startButton = (Button) findViewById(R.id.startSynonymButton);
         dayTextView = (TextView) findViewById(R.id.dayTextView);

        textView5 = (TextView) findViewById(R.id.textView5);

        repeatButton = (Button) findViewById(R.id.repeatButton);
        currentnoTextview = (TextView) findViewById(R.id.currentnoTextview);


        numberOfWords = new int[10];

        textToSpeech = new TextToSpeech(getApplicationContext(), this);
        textToSpeech.setLanguage(Locale.US);

        swipeCardsView1  = (SwipeCardsView) findViewById(R.id.swipeCardsView1);
        swipeCardsView1.retainLastCard(false);
        swipeCardsView1.enableSwipe(false);

        counter=sharedPreferences.getInt("day", counter);

        if(counter!=0) {
            dayTextView.setText("Day :" + Integer.toString(counter));
        }






        swipeCardsView1.setCardsSlideListener(new SwipeCardsView.CardsSlideListener() {
            @Override
            public void onShow(int index) {
                currentnoTextview.setText(String.valueOf(index+1) + "/10");



                try {

                    keep=dictionary.get(numberOfWords[index]);
                    url="http://packs.shtooka.net/eng-wcp-us/mp3/En-us-"+ dictionary.get(numberOfWords[index])+ ".mp3";
                    PlayAudioManager.playAudio(getApplicationContext(), url, new MediaPlayerListener() {
                        @Override
                        public void onErrorListener(boolean isError) {
                            if(isError){

                                textToSpeech.speak(keep, TextToSpeech.QUEUE_ADD, null);
                            }

                        }
                    });

                } catch (Exception e) {

                    e.printStackTrace();


                }


            }

            @Override
            public void onCardVanish(int index, SwipeCardsView.SlideType type) {

                Log.i("type: ", String.valueOf(type));


                if(index==9) {
                    textView5.setText( Integer.toString(counter) + " . Day Finished");
                    startButton.setVisibility(View.VISIBLE);
                    repeatButton.setVisibility(View.VISIBLE);
                    swipeCardsView1.enableSwipe(false);
                    sharedPreferences.edit().putInt("day", counter).apply();

                    AdmobAds.displayInterstitialAds(getApplicationContext());

                    if(counter%10==0) {

                        textView5.setText( Integer.toString(counter) + " . Day Finished\n Congratulations you learnt: " + counter*10 + " words!!" );
                    }
                    else if(counter==100) {
                        repeatButton.setVisibility(View.INVISIBLE);
                        swipeCardsView1.enableSwipe(false);
                        startButton.setText("Start again!!");
                        textView5.setText( Integer.toString(counter) + " . Day Finished\n Congratulations you learnt: " + counter*10 + " words!!" );
                        sharedPreferences.edit().clear();
                        counter=0;
                    }


                }
                else if(index>9) {
                    index=0;

                }

            }

            @Override
            public void onItemClick(View cardImageView, int index) {

               // String keep=dictionary.get(numberOfWords[index]);

                try {
                    url="http://packs.shtooka.net/eng-wcp-us/mp3/En-us-"+ dictionary.get(numberOfWords[index])+ ".mp3";

                    PlayAudioManager.playAudio(getApplicationContext(), url, new MediaPlayerListener() {
                        @Override
                        public void onErrorListener(boolean isError) {

                            if(isError){

                                textToSpeech.speak(keep, TextToSpeech.QUEUE_ADD, null);
                            }

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();

                 //   textToSpeech.speak(keep, TextToSpeech.QUEUE_ADD, null);

                }



            }


        });












    }



    public void repeatClicked(View view) {
        swipeCardsView1.enableSwipe(true);
        repeatButton.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.INVISIBLE);
        textView5.setText("");
        modelList.clear();
        valueForRepeat=true;
        getDay();


    }


    public void startClicked(View view) {
        swipeCardsView1.enableSwipe(true);
        startButton.setVisibility(View.INVISIBLE);
        repeatButton.setVisibility(View.INVISIBLE);
        textView5.setText("");
        modelList.clear();
        valueForRepeat=false;
        getDay();



        counter++;
        
        dayTextView.setText("Day :" + Integer.toString(counter));
        startButton.setText("Next Day");





    }


    private void getDay() {

        newWordEn();
        newWordDEf();

        if (valueForRepeat==false) {

            for (int i = 0; i < 10; i++) {
                random = new Random();
                number = random.nextInt(1001);
                numberOfWords[i] = number;


                modelList.add((new Model(dictionary.get(number), "a", definition.get(number))));


            }

        }

        else if (valueForRepeat==true)  {
            for (int i = 0; i < 10; i++) {


                modelList.add((new Model(dictionary.get(numberOfWords[i]), "a", definition.get(numberOfWords[i]))));



            }

        }


        CardAdapter cardAdapter  = new CardAdapter(modelList,this);
        swipeCardsView1.setAdapter(cardAdapter);







    }

    public void newWordDEf() {



        definition = new ArrayList<String>();

        BufferedReader dict = null; //Holds the dictionary file
        AssetManager am = this.getAssets();

        try {
            //dictionary.txt should be in the assets folder.
            dict = new BufferedReader(new InputStreamReader(am.open("definition.txt")));

            String word;
            while((word = dict.readLine()) != null){
                definition.add(word);

            }
            dict.close();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }




    public void newWordEn() {



        dictionary = new ArrayList<String>();

        BufferedReader dict = null; //Holds the dictionary file
        AssetManager am = this.getAssets();

        try {
            //dictionary.txt should be in the assets folder.
            dict = new BufferedReader(new InputStreamReader(am.open("dictionary.txt")));

            String word;
            while((word = dict.readLine()) != null){
                dictionary.add(word);

            }
            dict.close();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }


    public void loadBanner(Context context) {
        //noinspection deprecation
        MobileAds.initialize(context, context.getResources().getString(R.string.admob_app_id));

        AdView mAdview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

    }




    @Override
    public void onInit(int status) {

    }
}
