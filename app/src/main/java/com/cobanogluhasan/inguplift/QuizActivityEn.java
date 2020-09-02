package com.cobanogluhasan.inguplift;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cobanogluhasan.inguplift.FinalScoreDialog;
import com.cobanogluhasan.inguplift.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class QuizActivityEn extends AppCompatActivity {

    private static final String TAG = "QuizActivityEn";

    LinearLayout layout;
    private RadioGroup radioGroup;

    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;

    private Button buttonNext;
    String buttonText="", myFinalScoreText,buttonText2;
    private TextView scoreTextview;
    private TextView timerTextview;
    private TextView questionsTextview;
    private TextView questionNumberTextview;

    int counter=0;
    int locationOfTheCorrectAnswer;
    int number;

    ArrayList<String> questions;
    ArrayList<String> options;

    Random random;
    String infoQuestion;
    CountDownTimer countDownTimer;

    ArrayList<String> answers=new ArrayList<String>();
    RadioButton radioButtonSelected;
    Handler handler;

    TextToSpeech textToSpeechQuiz;
   private int score=0;
   private int correctAnswer=0,wrongAnswer=0;

    public FinalScoreDialog finalScoreDialog;
    int answerNumber=0;

    private Context context;
    String message = "";
    String incomingIntent="";
    String keepCurrent="";
    SharedPreferences langPreferences;
    String defaultLang="";
    String myScore="";
    TextView infoTextview;
    String myInfo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        langPreferences=this.getSharedPreferences("com.cobanogluhasan.inguplift", Context.MODE_PRIVATE);

        incomingIntent=getIntent().getStringExtra("filePref");

        getSupportActionBar().hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        textToSpeechQuiz = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    textToSpeechQuiz.setLanguage(Locale.UK);
                }
            }
        });

        handler=new Handler();
        setUpView();


        defaultLang=langPreferences.getString("language", "");
        languageSet(defaultLang);


        startQuiz();

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkQuestion();

            }
        });


        finalScoreDialog = new FinalScoreDialog(this);







    }

    private void languageSet(String defaultLang) {


            if(defaultLang.equals("turkish")) {
                infoQuestion = "Soru:";
                message="Lütfen seçim yapınız!";
                myScore="Puan:";
                myInfo="Tanıma en uygun kelime hangisidir?";
                buttonText="Sonraki";
                buttonText2="Yeniden Başla!";
                myFinalScoreText="Güncel Puan: ";
            }

            else if(defaultLang.equals("english")) {
                infoQuestion = "Question:";
                message="Please Select an option";
                myScore="Score:";
                myInfo="Which The Word Match the definiton?";
                buttonText="Next";
                myFinalScoreText="Final Score: ";
                buttonText2="Start Again!";
            }

            scoreTextview.setText(myScore);
            infoTextview.setText(myInfo);
            buttonNext.setText(buttonText);


    }


    private void checkQuestion() {
         radioButtonSelected = findViewById(radioGroup.getCheckedRadioButtonId());

         answerNumber = radioGroup.indexOfChild(radioButtonSelected);





        if(answerNumber==-1) { Toast.makeText(this, message, Toast.LENGTH_SHORT).show(); }

        else if(locationOfTheCorrectAnswer==answerNumber) {
            correctAnswer++;


            radioButtonSelected.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_answer_correct));
            textToSpeechQuiz.speak("correct", TextToSpeech.QUEUE_ADD, null);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    generateQuestion();

              clearCheck();

                }
            },600);


        }

        else{

            switch (locationOfTheCorrectAnswer) {
                case 0:
                    radioButton1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_answer_correct));
                    break;
                case 1:
                    radioButton2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_answer_correct));
                    break;
                case 2:
                    radioButton3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_answer_correct));
                    break;
                case 3:
                    radioButton4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_answer_correct));
                    break;

            }


            wrongAnswer++;

            radioButtonSelected.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_answer_wrong));
            textToSpeechQuiz.speak("wrong", TextToSpeech.QUEUE_ADD, null);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    generateQuestion();
               clearCheck();

                }
            },600);


        }

        score=10*correctAnswer - 5*wrongAnswer;

        scoreTextview.setText(myScore + String.valueOf(score));


    }

    private void startQuiz() {

        generateQuestion();


         countDownTimer = new CountDownTimer(60200, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextview.setText(String.valueOf(millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                //playAgainButton.setVisibility(View.VISIBLE);
                timerTextview.setText("0s");



                finalScoreDialog.finalScoreDialog(correctAnswer,wrongAnswer,counter,keepCurrent,myFinalScoreText,buttonText2);


                finalScoreDialog.playAgainButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playAgain();
                    }
                });

            }
        }.start();




    }

    public void playAgain() {
        timerTextview.setText("01:00");
        score=0;
        correctAnswer=0;
        wrongAnswer=0;
        counter=0;

        questionNumberTextview.setText(infoQuestion + String.valueOf(counter));
        scoreTextview.setText(myScore + String.valueOf(score));


        setUpView();
        startQuiz();

        finalScoreDialog.finalScoreDialog.dismiss();


    }

    public void generateNumber() {
        try {

            number = random.nextInt(998);
        } catch (Exception e) {
            e.printStackTrace();

            number = random.nextInt(998);
        }
    }

    private void generateQuestion() {
       getQuestion();
       getOptions();

       clearCheck();
        counter++;


        answers.clear();

       questionNumberTextview.setText(infoQuestion + String.valueOf(counter));

        random = new Random();

        generateNumber();

        String correctAnswerString="";

        questionsTextview.setText(questions.get(number));
        correctAnswerString=options.get(number);


         locationOfTheCorrectAnswer= random.nextInt(4);


        for(int i=0;i<4;i++) {

            if(i==locationOfTheCorrectAnswer) {

                answers.add(correctAnswerString);
            }

            else {
                generateNumber();
                String wrongAnswer =options.get(number);

                while(correctAnswerString.equals(wrongAnswer)) {

                   generateNumber();


                }
                wrongAnswer =options.get(number);
                answers.add(wrongAnswer);

                }

                }




        radioButton1.setText(answers.get(0));
        radioButton2.setText(answers.get(1));
        radioButton3.setText(answers.get(2));
        radioButton4.setText(answers.get(3));



    }


    public void clearCheck() {
        radioGroup.clearCheck();
      if(!(answerNumber!=-1)){
          radioButtonSelected.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.checkbox_selector));
      }

        radioButton1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.checkbox_selector));
        radioButton2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.checkbox_selector));
        radioButton3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.checkbox_selector));
        radioButton4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.checkbox_selector));

    }




    private void setUpView() {


        layout = findViewById(R.id.layout);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        radioButton4 = (RadioButton) findViewById(R.id.radioButton4);

        buttonNext = (Button) findViewById(R.id.buttonNext);
        timerTextview = (TextView) findViewById(R.id.timerTextView);
        scoreTextview = (TextView) findViewById(R.id.scoreTextView);
        questionsTextview = (TextView) findViewById(R.id.questionstextView);
        questionNumberTextview = (TextView) findViewById(R.id.questionNumberTextView);
        infoTextview = (TextView) findViewById(R.id.infoTextView);



    }

    private void animatedGradientSet() {
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

    }

    public void getQuestion() {



        questions = new ArrayList<String>();

        BufferedReader dict = null; //Holds the dictionary file
        AssetManager am = this.getAssets();

        try {
            //dictionary.txt should be in the assets folder.
            dict = new BufferedReader(new InputStreamReader(am.open("definition.txt")));

            String word;
            while((word = dict.readLine()) != null){
                questions.add(word);

            }
            dict.close();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }




    public void getOptions() {

        options = new ArrayList<String>();

        BufferedReader dict = null; //Holds the dictionary file
        AssetManager am = this.getAssets();

        try {
            //dictionary.txt should be in the assets folder.
            if (incomingIntent.equals("En")) {
            dict = new BufferedReader(new InputStreamReader(am.open("dictionary.txt")));
                keepCurrent="En";
            }
            else if(incomingIntent.equals("Tr")) {
                dict = new BufferedReader(new InputStreamReader(am.open("translationtr.txt")));
                keepCurrent="Tr";
            }

            String word;
            while((word = dict.readLine()) != null){
                options.add(word);

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











}