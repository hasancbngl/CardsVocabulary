package com.cobanogluhasan.inguplift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.card.MaterialCardView;

public class MySettings extends AppCompatActivity {


    SharedPreferences langPreferences;

    public void langTurkishClick(View view) {

        langPreferences.edit().remove("language").commit();

        langPreferences.edit().putString("language" , "turkish").commit();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,-2000, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Toast.makeText(this, "Please Waiting", Toast.LENGTH_SHORT).show();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC,System.currentTimeMillis() + 0, pendingIntent);

       // System.exit(0); //close the app

    }


    public void langEnglishClick(View view) {
        langPreferences.edit().remove("language").commit();
        langPreferences.edit().putString("language" , "english").commit();


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Toast.makeText(this, "Please Waiting", Toast.LENGTH_SHORT).show();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC,System.currentTimeMillis() + 0, pendingIntent);

        //System.exit(0); //close the app


    }

    public void discreteScrollClicked(View view) {

        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/yarolegovich/DiscreteScrollView"));
        startActivity(launchBrowser);

    }

    public void licenseClicked(View view) {

        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://creativecommons.org/licenses/by/4.0/"));
        startActivity(launchBrowser);

    }

    public void iconClicked(View view) {

        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flaticon.com/free-icon/vocabulary_1006670?term=vocabulary&page=1&position=3"));
        startActivity(launchBrowser);

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);



        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        langPreferences=this.getSharedPreferences("com.cobanogluhasan.inguplift", Context.MODE_PRIVATE);


        loadBanner(this);


    }

    public void loadBanner(Context context) {
        //noinspection deprecation
        MobileAds.initialize(context, context.getResources().getString(R.string.admob_app_id));

        AdView mAdview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

    }



}