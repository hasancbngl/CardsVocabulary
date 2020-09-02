package com.cobanogluhasan.inguplift;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.security.PublicKey;

public class ResultActivity extends AppCompatActivity {

    TextView totalTextview;
    TextView resultHighScore;
    TextView correctTextview;
    TextView wrongTextview;
    Button result_playAgain;
    Button result_mainMenu;
    int wrong,correct,score;

    String defaultLang="";
    SharedPreferences langPreferences;

    int highscore=0;
    String myIntent="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        langPreferences=this.getSharedPreferences("com.cobanogluhasan.inguplift", Context.MODE_PRIVATE);

        totalTextview = (TextView) findViewById(R.id.totalTextView);
        resultHighScore = (TextView) findViewById(R.id.result_highScore);
        correctTextview = (TextView) findViewById(R.id.correctTextView);
        wrongTextview = (TextView) findViewById(R.id.wrongTextView);
        result_mainMenu = (Button) findViewById(R.id.result_mainMenu);
        result_playAgain = (Button) findViewById(R.id.result_playAgain);

        myIntent=getIntent().getStringExtra("filePref");

        SharedPreferences prefs = this.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);


        wrong = getIntent().getIntExtra("wrong",0);
        correct = getIntent().getIntExtra("correct",0);
        score = getIntent().getIntExtra("score",0);

        if(score>highscore) {
            prefs.edit().putInt("highscore", score).commit();
            score = prefs.getInt("highscore", 0);

        }



        defaultLang=langPreferences.getString("language", "");

        languageSet(defaultLang);




        getSupportActionBar();





        result_playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuizActivityEn.class);
                intent.putExtra("filePref", myIntent);
                startActivity(intent);
            }
        });


        result_mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenu = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainMenu);
            }
        });


    }


    public void languageSet(String defaultLang){
        if(defaultLang.equals("turkish")) {
            totalTextview.setText("Toplam Soru: " + (correct+wrong));
            resultHighScore.setText("En yüksek Puan:" + score);
            correctTextview.setText("Doğru: " + correct);
            wrongTextview.setText("Yanlış: " + wrong);
            result_playAgain.setText("Yeniden Başla!");
            result_mainMenu.setText("Ana Menü");

        }

        else if(defaultLang.equals("english")) {

            totalTextview.setText("Total Questions: " + (correct+wrong));
            resultHighScore.setText("Highest Score:" + score);
            correctTextview.setText("Correct Questions: " + correct);
            wrongTextview.setText("Wrong Questions: " + wrong);
            result_playAgain.setText("Start Again!");

        }




    }
}