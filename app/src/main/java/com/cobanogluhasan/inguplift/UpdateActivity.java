package com.cobanogluhasan.inguplift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class UpdateActivity extends AppCompatActivity {


    private TextInputLayout textInputWord2;
    private TextInputLayout textInputDefinition2;
    private TextInputLayout textInputSynonym2;
    private TextInputLayout textInputExample2;
    Button updateButton;
    TextInputEditText wordEditText;
    TextInputEditText definitionEditText;
    TextInputEditText synonymEditText;
    TextInputEditText exampleEditText;

    SharedPreferences langPreferences;
    String defaultLang="";
    Word mWord;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        loadBanner(this);

        langPreferences=this.getSharedPreferences("com.cobanogluhasan.inguplift", Context.MODE_PRIVATE);
        defaultLang=langPreferences.getString("language", "");

        updateButton = (Button) findViewById(R.id.updateButton);
        textInputWord2 = (TextInputLayout) findViewById(R.id.textInputWord2);
        textInputDefinition2 = (TextInputLayout) findViewById(R.id.textInputDefinition2);
        textInputSynonym2 = (TextInputLayout) findViewById(R.id.textInputSynonym2);
        textInputExample2 = (TextInputLayout) findViewById(R.id.textInputExample2);

        wordEditText = (TextInputEditText) findViewById(R.id.wordEditText);
        definitionEditText = (TextInputEditText) findViewById(R.id.definitionEditText);
        synonymEditText = (TextInputEditText) findViewById(R.id.synonymEditText);
        exampleEditText = (TextInputEditText) findViewById(R.id.exampleEditText);

        langPref();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getIntentData();

          databaseHelper = new DatabaseHelper(this);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validateWord() | !validateSynonym()) {
                    return;
                }
                mWord.setDefinition(definitionEditText.getEditableText().toString().trim());
                mWord.setExample(exampleEditText.getEditableText().toString().trim());


                databaseHelper.updateData(String.valueOf(mWord.getId()), mWord.getWord(),mWord.getSynonym(),mWord.getDefinition(),mWord.getExample());

                Intent intent = new Intent(UpdateActivity.this, MyWordListActivity.class);
                startActivity(intent);

            }
        });









    }











    void getIntentData() {
        if(getIntent().hasExtra("id") && getIntent().hasExtra("word" )
         && getIntent().hasExtra("synonym")) {


            mWord = new Word();
            mWord.setId(getIntent().getLongExtra("id", 0));
            mWord.setWord(getIntent().getStringExtra("word"));
            mWord.setDefinition(getIntent().getStringExtra("definition"));
            mWord.setSynonym(getIntent().getStringExtra("synonym"));
            mWord.setExample(getIntent().getStringExtra("example"));


            wordEditText.setText(mWord.getWord());
            definitionEditText.setText(mWord.getDefinition());
            synonymEditText.setText(mWord.getSynonym());
            exampleEditText.setText(mWord.getExample());



        }



    }

    public void loadBanner(Context context) {
        //noinspection deprecation
        MobileAds.initialize(context, context.getResources().getString(R.string.admob_app_id));

        AdView mAdview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

    }

    public void langPref() {

        if(defaultLang.equals("turkish")) {

            textInputWord2.setHint("Kelime");
            textInputDefinition2.setHint("Tanım");
            textInputSynonym2.setHint("Eş anlamlısı");
            textInputExample2.setHint("Örnek");
            updateButton.setText("Kaydet");

            getSupportActionBar().setTitle("Düzenle");

        }
        else {
            getSupportActionBar().setTitle("Edit");
        }



    }


    private boolean validateWord() {
        mWord.setWord(textInputWord2.getEditText().getText().toString().trim()); //trim removes spaces at the beginning and end

        if(mWord.getWord().isEmpty()) {

            if (defaultLang.equals("turkish")) {
                textInputWord2.setError("Bu alan boş bırakılamaz!");
            }

            else{   textInputWord2.setError("This field can not be empty");}

            return false;
        }

        else if(mWord.getWord().length() > 20) {
            if (defaultLang.equals("turkish")) {
                textInputWord2.setError("Maximum karakter sayısını aşıyor!");
            }

            else{ textInputWord2.setError("It's too long");}

            return false;


        }

        else {
            textInputWord2.setError(null);
            return true;
        }

    }

    private boolean validateSynonym() {
        mWord.setSynonym(synonymEditText.getEditableText().toString().trim()); //trim removes spaces at the beginning and end



        if(mWord.getSynonym().isEmpty()) {

            if (defaultLang.equals("turkish")) {
                textInputSynonym2.setError("Bu alan boş bırakılamaz!");
            }

            else{   textInputSynonym2.setError("This field can not be empty");}

            return false;
        }

        else if(mWord.getSynonym().length() > 20) {
            if (defaultLang.equals("turkish")) {
                textInputSynonym2.setError("Maximum karakter sayısını aşıyor!");
            }

            else{ textInputSynonym2.setError("It's too long");}

            return false;


        }
        else  {
            textInputSynonym2.setError(null);
            return true;
        }

    }





}