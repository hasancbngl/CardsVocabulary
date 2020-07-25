package com.cobanogluhasan.inguplift;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cobanogluhasan.inguplift.AdmobAds.AdmobAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.textfield.TextInputLayout;

public class VocabularyListActivity extends AppCompatActivity {


    private static final String TAG = "VocabularyListActivity";
    private TextInputLayout textInputWord;
    private TextInputLayout textInputDefinition;
    private TextInputLayout textInputSynonym;
    private TextInputLayout textInputExample;
    String wordInput;
    String synonymInput;
    String getDefinition;
    String getExample;
    Button addButton;

    DatabaseHelper mDatabaseHelper;


    SharedPreferences langPreferences;
    String defaultLang="";


    public void vocabularyInput(View view) {

        if(!validateWord() | !validateSynonym()) {
            return;
        }
        getDefinition = textInputDefinition.getEditText().getText().toString().trim();
        getExample = textInputExample.getEditText().getText().toString().trim();



       Intent intent = new Intent(VocabularyListActivity.this, MyWordListActivity.class);

       AddData(wordInput, synonymInput,getDefinition, getExample);

        AdmobAds.displayInterstitialAds(getApplicationContext());

        startActivity(intent);




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_list);

        loadBanner(this);

        langPreferences=this.getSharedPreferences("com.cobanogluhasan.inguplift", Context.MODE_PRIVATE);

        addButton = (Button) findViewById(R.id.addButton);
        textInputWord = (TextInputLayout) findViewById(R.id.textInputWord);
        textInputDefinition = (TextInputLayout) findViewById(R.id.textInputDefinition);
        textInputSynonym = (TextInputLayout) findViewById(R.id.textInputSynonym);
        textInputExample = (TextInputLayout) findViewById(R.id.textInputExample);
        mDatabaseHelper = new DatabaseHelper(this);


        defaultLang=langPreferences.getString("language", "");

        if(defaultLang.equals("turkish")) {

            textInputWord.setHint("Kelime");
            textInputDefinition.setHint("Tanım");
            textInputSynonym.setHint("Eş anlamlısı");
            textInputExample.setHint("Örnek");
            addButton.setText("Ekle");

            getSupportActionBar().setTitle("Yeni Kelime Ekle");

        }
        else {
            getSupportActionBar().setTitle("Add a new word!");
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private boolean validateWord() {
         wordInput = textInputWord.getEditText().getText().toString().trim(); //trim removes spaces at the beginning and end

        if(wordInput.isEmpty()) {

            if (defaultLang.equals("turkish")) {
                textInputWord.setError("Bu alan boş bırakılamaz!");
            }

            else{   textInputWord.setError("This field can not be empty");}

            return false;
        }

        else if(wordInput.length() > 20) {
            if (defaultLang.equals("turkish")) {
                textInputWord.setError("Maximum karakter sayısını aşıyor!");
            }

            else{ textInputWord.setError("It's too long");}

            return false;


        }

        else {
            textInputWord.setError(null);
            return true;
        }

    }

    private boolean validateSynonym() {
         synonymInput = textInputSynonym.getEditText().getText().toString().trim(); //trim removes spaces at the beginning and end



        if(synonymInput.isEmpty()) {

            if (defaultLang.equals("turkish")) {
                textInputSynonym.setError("Bu alan boş bırakılamaz!");
            }

         else{   textInputSynonym.setError("This field can not be empty");}

            return false;
        }

       else if(synonymInput.length() > 20) {
            if (defaultLang.equals("turkish")) {
                textInputSynonym.setError("Maximum karakter sayısını aşıyor!");
            }

            else{ textInputSynonym.setError("It's too long");}

            return false;


        }
        else  {
            textInputSynonym.setError(null);
            return true;
        }

    }

    public void AddData(String word,String synonym, String definition, String example) {
        boolean insertData = mDatabaseHelper.addData(word,synonym,definition,example);

        if (defaultLang.equals("turkish")) {

            if (insertData) {
                toastMessage("Listeye Eklendi!");
            } else {
                toastMessage("Bir hata oluştu!");
            }

        }

        else {

            if (insertData) {
                toastMessage("Words Successfully added!");
            } else {
                toastMessage("Something went wrong");
            }
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }


    public void loadBanner(Context context) {
        //noinspection deprecation
        MobileAds.initialize(context, context.getResources().getString(R.string.admob_app_id));

        AdView mAdview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

    }





}