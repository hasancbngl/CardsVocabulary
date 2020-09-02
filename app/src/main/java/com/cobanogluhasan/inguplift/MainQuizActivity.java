package com.cobanogluhasan.inguplift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainQuizActivity extends AppCompatActivity {

    LinearLayout quizLinear;
    Button quizButtonEn;
    Button quizButtonTr;
    Button guessTheWordButton;

    String title="";
    String quizTextEn="";
    String quizTextTr="";
    String guessTheword="";
    String defaultLang="";
    SharedPreferences langPreferences;
    String putIntent="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz);
        langPreferences=this.getSharedPreferences("com.cobanogluhasan.inguplift", Context.MODE_PRIVATE);

        setUpView();
        animatedGradient();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        defaultLang=langPreferences.getString("language", "");
        languageSet(defaultLang);

        buttonClickListener();


    }

    private void buttonClickListener() {

        quizButtonEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putIntent="En";
                Intent intent = new Intent(getApplicationContext(), QuizActivityEn.class);
                intent.putExtra("filePref", putIntent);
                startActivity(intent);
            }
        });

        quizButtonTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putIntent = "Tr";
                Intent intent = new Intent(getApplicationContext(), QuizActivityEn.class);
                intent.putExtra("filePref", putIntent);
                startActivity(intent);
            }
        });

        guessTheWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guessTheWordIntent = new Intent(getApplicationContext(), GuessTheWordActivity.class);
                startActivity(guessTheWordIntent);
            }
        });




    }

    private void setUpView() {
        quizLinear=(LinearLayout) findViewById(R.id.quizLinear);
        quizButtonTr = (Button) findViewById(R.id.quizButtonTr);
        quizButtonEn=(Button) findViewById(R.id.quizButtonEn);
        guessTheWordButton = (Button) findViewById(R.id.guessTheWord);

    }


    private void animatedGradient() {

        AnimationDrawable animationDrawable = (AnimationDrawable) quizLinear.getBackground();
        animationDrawable.setEnterFadeDuration(2000); //Animated background
        animationDrawable.setExitFadeDuration(4000);

        animationDrawable.start();

    }


    public void languageSet(String defaultLang){
        if(defaultLang.equals("turkish")) {
             quizTextEn="Test:Tanımını seç ";
             quizTextTr="Test:Türkçe anlamı";
             guessTheword="Kelimeyi Tahmin Et";
             title="Test";
        }

        else if(defaultLang.equals("english")) {
            quizTextEn="Select The Definition";
            quizTextTr="Select The Translation Tr";
            guessTheword="Guess The Word";
            title="Quiz";
        }


        quizButtonTr.setText(quizTextTr);
        quizButtonEn.setText(quizTextEn);
        guessTheWordButton.setText(guessTheword);
        getSupportActionBar().setTitle(title);


    }


}