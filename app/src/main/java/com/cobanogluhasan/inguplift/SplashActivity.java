package com.cobanogluhasan.inguplift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN=2500;

    Animation topAnim,bottomAnim;
    ImageView imageViewCard;
    TextView logo, slogan;

   public void getSplashScreen() {
       getSupportActionBar().hide();

       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

       topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
       bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

       imageViewCard=(ImageView) findViewById(R.id.imageViewCard);
       logo = (TextView) findViewById(R.id.textView2);
       slogan = (TextView) findViewById(R.id.textView);



       imageViewCard.setAnimation(topAnim);
       logo.setAnimation(bottomAnim);
       slogan.setAnimation(bottomAnim);

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
               startActivity(mainIntent);

               finish();
           }
       }, SPLASH_SCREEN);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        getSplashScreen();
    }
}