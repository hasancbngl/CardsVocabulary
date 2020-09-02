package com.cobanogluhasan.inguplift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class GuessTheWordActivity extends AppCompatActivity {

    private static final String TAG = "GuessTheWordActivity";
    private ArrayList<String> guessWord;
    int number;
    Random random;

    TextView guessScoreTextview,textQuestion;
    LinearLayout parentLinearLayout,parentLinearLayout2;
    EditText guessTheWordEditText;

    private int counter=0;
    private int maxPressCounter;
    private String[] keys;
    private String textAnswer;
    Animation smallBigForth;
    private  int count,score=0,quesNumber=0;
    private Button next_Button;
    private TextView ques_Textview,guessTimerTextView;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_the_word);

        setupView();

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        Start();






        

    }

    private void Start(){
        newQuestion();
        countDownTimer = new CountDownTimer(60150,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                guessTimerTextView.setText(String.valueOf(millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                guessTimerTextView.setText("0s");

            }
        }.start();


    }


    private void newQuestion(){
        next_Button.setVisibility(View.INVISIBLE);
        getWord();
        quesNumber++;
        parentLinearLayout.removeAllViewsInLayout();
        parentLinearLayout2.removeAllViewsInLayout();
         int number=random.nextInt(998);

         textAnswer = guessWord.get(number);
          while(textAnswer.length()<3) {
             number=random.nextInt(998);
            textAnswer = guessWord.get(number);

        }

        keys=(textAnswer.toUpperCase()).split("");

        maxPressCounter = textAnswer.length();

        keys = shuffleArray(keys);

        sendTheLetters(keys);
    }

    private void sendTheLetters(String[] keys) {
         count=0;
        for(String key : keys) {

            if(count<=6) {
                addView(parentLinearLayout, key, guessTheWordEditText);

            }
            else if(count>6) {
                addView(parentLinearLayout2, key, guessTheWordEditText);

            }
            count++;
        }


    }


    private String[] shuffleArray(String[] array) {
        for(int i=keys.length -1; i>0; i--) {

            int index = random.nextInt(i+1);
            String a = array[index];
            array[index]=array[i];
            array[i] = a;
        }
        return  array;
    }



    private void addView(final LinearLayout viewParent, final String text, final EditText editText) {
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayoutParams.rightMargin = (int) getResources().getDimension(R.dimen.dp15);

        int padding  =(int) getResources().getDimension(R.dimen.dp1);
        final   TextView textView = new TextView(getApplicationContext());

        textView.setLayoutParams(linearLayoutParams);

        textView.setBackground(this.getResources().getDrawable(R.drawable.text3_background));
         textView.setTextColor(this.getResources().getColor(R.color.colorPrimary));
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setTextSize((int) getResources().getDimension(R.dimen.sp20));




        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.bad_script);

        textQuestion.setTypeface(typeface, Typeface.BOLD);
        guessScoreTextview.setTypeface(typeface, Typeface.BOLD);
      //  textView.setTypeface(typeface, Typeface.BOLD);
        editText.setTypeface(typeface, Typeface.BOLD);



        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter < maxPressCounter) {
                    if (counter == 0) {
                        editText.setText("");
                    }

                        editText.setText(editText.getText().toString() + text);
                          textView.startAnimation(smallBigForth);
                        textView.animate().alpha(0).setDuration(300);
                        viewParent.removeView(textView);
                        count--;
                        counter++;

                        if (counter == maxPressCounter) {
                            next_Button.setVisibility(View.VISIBLE);

                          next_Button.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                  check();
                              }
                          });
                    }

                }
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String b="";
                String current=editText.getText().toString();
                counter--;

                try {
                     b = current.substring(current.length()-1);
                    editText.setText(current.substring(0, current.length()-1));
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if(count<=6) {
                    addView(parentLinearLayout, b,guessTheWordEditText); }
                else {
                    addView(parentLinearLayout2, b,guessTheWordEditText); }

                count++;
            }
        });


        if(!textView.getText().toString().matches("")){
            textView.setPadding(padding,padding,padding,padding);
            viewParent.addView(textView);
        }



    }

    private void check() {
        counter=0;
        ques_Textview.setText("Question" + String.valueOf(quesNumber));
        parentLinearLayout.removeAllViews();
        parentLinearLayout2.removeAllViews();


        if(guessTheWordEditText.getText().toString().toLowerCase().equals(textAnswer)) {

            Toast.makeText(this, "correct", Toast.LENGTH_SHORT).show();
            score=score+10;
            guessScoreTextview.setText(String.valueOf(score + "P"));
        }
        else {
            Toast.makeText(this, "wrong.answer is: " + textAnswer , Toast.LENGTH_SHORT).show();

        }


        guessTheWordEditText.setText("");
        newQuestion();

    }


    private void setupView() {

        guessScoreTextview = (TextView) findViewById(R.id.guessScoreTextView);
        textQuestion=(TextView) findViewById(R.id.textQuestion);
        guessTheWordEditText = (EditText) findViewById(R.id.guessTheWordEditText);
        parentLinearLayout = (LinearLayout) findViewById(R.id.parentLinearLayout);
        parentLinearLayout2 = (LinearLayout) findViewById(R.id.parentLinearLayout2);

        next_Button = (Button) findViewById(R.id.next_Button);
        ques_Textview = (TextView) findViewById(R.id.quesTextView);
        guessTimerTextView = (TextView) findViewById(R.id.guessTimerTextView);

        random = new Random();

        smallBigForth = AnimationUtils.loadAnimation(this,R.anim.smallbigforth);
    }


    private void getWord() {

        guessWord = new ArrayList<String>();

        BufferedReader dict = null; //Holds the dictionary file
        AssetManager am = this.getAssets();

        try {
            //dictionary.txt should be in the assets folder.

            dict = new BufferedReader(new InputStreamReader(am.open("dictionary.txt")));


            String word;
            while((word = dict.readLine()) != null){
                guessWord.add(word);

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