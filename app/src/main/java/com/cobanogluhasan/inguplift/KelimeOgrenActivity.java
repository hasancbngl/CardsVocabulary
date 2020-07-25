package com.cobanogluhasan.inguplift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
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




public class KelimeOgrenActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {


    private SwipeCardsView swipeCardsView;
    private List<Model> modelList = new ArrayList<>();
    ArrayList<String> dictionary;
    ArrayList<String> translation;
    int number;
    Button baslatbutton;
    TextView gunTextView;
    int sayac = 0;

    TextView textView5;
    Button tekrarButton;
    boolean valueForRepeat=false;
    int[] numberOfWords;
    Random random;
    String url="";
    TextView guncelnoTextview;
    private String keep = "";


    TextToSpeech textToSpeech;

    SharedPreferences sharedPreferences2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelime_ogren);

        getSupportActionBar().setTitle("Günde 10 Kelime Öğren!");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences2 = this.getSharedPreferences("com.cobanogluhasan.inguplift", Context.MODE_PRIVATE);


        baslatbutton = (Button) findViewById(R.id.baslatButton);
        gunTextView = (TextView) findViewById(R.id.gunTextView);

        textView5 = (TextView) findViewById(R.id.textView5);

        tekrarButton = (Button) findViewById(R.id.tekrarButton);
        guncelnoTextview = (TextView) findViewById(R.id.guncelnoTextview);

        numberOfWords = new int[10];

        textToSpeech = new TextToSpeech(getApplicationContext(), this);
        textToSpeech.setLanguage(Locale.US);


        swipeCardsView  = (SwipeCardsView) findViewById(R.id.swipeCardsView1);
        swipeCardsView.retainLastCard(false);
        swipeCardsView.enableSwipe(false);

        sayac=sharedPreferences2.getInt("gun", sayac);


        loadBanner(this);



        if(sayac!=0) {
            gunTextView.setText("Day :" + Integer.toString(sayac));
        }



        swipeCardsView.setCardsSlideListener(new SwipeCardsView.CardsSlideListener() {

            @Override
            public void onShow(int index) {
                guncelnoTextview.setText(String.valueOf(index+1) + "/10");



                try {
                    url="http://packs.shtooka.net/eng-wcp-us/mp3/En-us-"+ dictionary.get(numberOfWords[index])+ ".mp3";

                     keep=dictionary.get(numberOfWords[index]);

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

                    textView5.setText( Integer.toString(sayac) + " .Günü Bitirdiniz!! ");
                    baslatbutton.setVisibility(View.VISIBLE);
                    tekrarButton.setVisibility(View.VISIBLE);
                    swipeCardsView.enableSwipe(false);
                    sharedPreferences2.edit().putInt("gun", sayac).apply();

                    AdmobAds.displayInterstitialAds(getApplicationContext());


                    if(sayac%10==0) {

                        textView5.setText( Integer.toString(sayac) + ".Günü Bitirdiniz!!\n Tebrikler! Şu ana kadar : " + sayac*10 + " kelime öğrendiniz!!" );

                    }
                   else if(sayac==100) {
                        tekrarButton.setVisibility(View.INVISIBLE);
                        swipeCardsView.enableSwipe(false);
                        baslatbutton.setText("Yeniden Başla!");
                        textView5.setText( Integer.toString(sayac) + ".Günü Bitirdiniz!!\n Tebrikler! Şu ana kadar : 1000 kelime öğrendiniz!!" );
                        sharedPreferences2.edit().clear();
                        sayac=0;
                    }


                }
                else if(index>9) {
                    index=0;

                }

            }

            @Override
            public void onItemClick(View cardImageView, int index) {



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

                    
                }



            }
        });


    }




    public void tekrarClicked(View view) {

        swipeCardsView.enableSwipe(true);
        tekrarButton.setVisibility(View.INVISIBLE);
        baslatbutton.setVisibility(View.INVISIBLE);
        textView5.setText("");
        modelList.clear();
        valueForRepeat=true;
        getDay();


    }

    public void baslatClicked(View view) {

        swipeCardsView.enableSwipe(true);
        baslatbutton.setVisibility(View.INVISIBLE);
        tekrarButton.setVisibility(View.INVISIBLE);
        textView5.setText("");
        modelList.clear();
        valueForRepeat=false;
        getDay();
        sayac++;
        gunTextView.setText("Gün :" + Integer.toString(sayac));
        baslatbutton.setText("Bir Sonraki Gün!");


    }



    private void getDay() {

        newWordEn();
        newWordTr();

        if (valueForRepeat==false) {

            for (int i = 0; i < 10; i++) {
                random = new Random();
                number = random.nextInt(1001);
                numberOfWords[i] = number;


                modelList.add((new Model(dictionary.get(number), "a", translation.get(number))));


            }

        }

        else if (valueForRepeat==true)  {
            for (int i = 0; i < 10; i++) {


                modelList.add((new Model(dictionary.get(numberOfWords[i]), "a", translation.get(numberOfWords[i]))));



            }

        }


        CardAdapter cardAdapter  = new CardAdapter(modelList,this);
        swipeCardsView.setAdapter(cardAdapter);







    }


    public void newWordTr() {



        translation = new ArrayList<String>();

        BufferedReader dict = null; //Holds the dictionary file
        AssetManager am = this.getAssets();

        try {
            //dictionary.txt should be in the assets folder.
            dict = new BufferedReader(new InputStreamReader(am.open("translationtr.txt")));

            String word;
            while((word = dict.readLine()) != null){
                translation.add(word);

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

                if(word== null) {
                    Log.i("error: " , Integer.toString(number));
                }
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
