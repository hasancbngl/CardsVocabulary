package com.cobanogluhasan.inguplift;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import hotchemi.android.rate.AppRate;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    TextView selectTextview;
    Button trButton;
    Button engButton;
    RelativeLayout menuRelativeLayoutEn;
    Button learnWordsButton;
    Button synonymButton;

    RelativeLayout menuRelativeLayoutTr;
    Button kelimeOgrenButton;
    Button esAnlamliButton;
    SharedPreferences langPreferences;
    String defaultLang="";

    RelativeLayout firstMenuRelativeLayout;

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);


        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, MySettings.class);
                startActivity(intent);
                return true;
            case R.id.share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String  shareBody= getString(R.string.content);
                String url="https://play.google.com/store/apps/details?id=com.cobanogluhasan.inguplift";
                String app="Cards Vocabulary";

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, app);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody + "\n\n\n" +url);

                startActivity(Intent.createChooser(sharingIntent,  getString(R.string.sharevia)));

                return true;
            case R.id.aboutMyApp:
                Intent aboutIntent = new Intent(this, aboutActivity.class);

                startActivity(aboutIntent);

                return true;
            default:
                return false;

        }
    }




    public void turkishClicked(View view) {

        langPreferences.edit().putString("language" , "turkish").apply();

        selectTextview.setVisibility(View.INVISIBLE);
        engButton.setVisibility(View.INVISIBLE);
        trButton.setVisibility(View.INVISIBLE);

        menuRelativeLayoutTr.setVisibility(View.VISIBLE);
        menuRelativeLayoutEn.setVisibility(View.INVISIBLE);


    }

    public void englishClicked(View view) {

        langPreferences.edit().putString("language" , "english").apply();

        selectTextview.setVisibility(View.INVISIBLE);
        trButton.setVisibility(View.INVISIBLE);
        engButton.setVisibility(View.INVISIBLE);


        menuRelativeLayoutTr.setVisibility(View.INVISIBLE);
        menuRelativeLayoutEn.setVisibility(View.VISIBLE);




    }

    public void quizClicked(View view) {

        Intent quizIntent =new Intent(getApplicationContext(), MainQuizActivity.class);
        startActivity(quizIntent);


    }


    public void learnWordsClicked(View view) {

        Intent intent2 =new Intent(getApplicationContext(),LearnWordsActivity.class);
        startActivity(intent2);



    }

    public void kelimeOgrenClicked(View view) {

        Intent intent =new Intent(getApplicationContext(), KelimeOgrenActivity.class);
        startActivity(intent);



    }

    public void synonymClicked(View view) {

        Intent daysIntent = new Intent(getApplicationContext(), DaysActivity.class);

        startActivity(daysIntent);

    }

    public void myWordListClicked(View view) {

        Intent myWordListIntent = new Intent(getApplicationContext(), MyWordListActivity.class);

        startActivity(myWordListIntent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        langPreferences=this.getSharedPreferences("com.cobanogluhasan.inguplift", Context.MODE_PRIVATE);


        selectTextview = (TextView) findViewById(R.id.selectTextView);
        trButton = (Button) findViewById(R.id.trButton);

        engButton = (Button) findViewById(R.id.engButton);
        learnWordsButton= (Button) findViewById(R.id.learnWordsButton);
        synonymButton = (Button) findViewById(R.id.synonymButton);

        menuRelativeLayoutEn = (RelativeLayout) findViewById(R.id.menuRelativeLayoutEn);
        menuRelativeLayoutTr = (RelativeLayout) findViewById(R.id.menuRelativeLayoutTr);

        kelimeOgrenButton= (Button) findViewById(R.id.kelimeOgrenButton);
        esAnlamliButton = (Button) findViewById(R.id.esAnlamliButton);

        firstMenuRelativeLayout = findViewById(R.id.firstMenuRelativeL);

        animatedGradient();



      defaultLang=langPreferences.getString("language", "");



        if(defaultLang.equals("turkish")) {

            selectTextview.setVisibility(View.INVISIBLE);
            engButton.setVisibility(View.INVISIBLE);
            trButton.setVisibility(View.INVISIBLE);

            menuRelativeLayoutTr.setVisibility(View.VISIBLE);
            menuRelativeLayoutEn.setVisibility(View.INVISIBLE);

        }

        else if(defaultLang.equals("english")) {

            selectTextview.setVisibility(View.INVISIBLE);
            trButton.setVisibility(View.INVISIBLE);
            engButton.setVisibility(View.INVISIBLE);


            menuRelativeLayoutTr.setVisibility(View.INVISIBLE);
            menuRelativeLayoutEn.setVisibility(View.VISIBLE);

}



     loadBanner(this);

        rateMyApp();


    }

    private void animatedGradient() {

        AnimationDrawable animationDrawable = (AnimationDrawable) firstMenuRelativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000); //Animated background
        animationDrawable.setExitFadeDuration(4000);

        animationDrawable.start();




    }

    private void rateMyApp() {
        //rate the app dialog

        AppRate.with(this)
                .setInstallDays(1) // default 10, 0 means install day.
                .setLaunchTimes(2) // default 10
                .setRemindInterval(1) // default 1
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);






    }


    public void loadBanner(Context context) {
        //noinspection deprecation
        MobileAds.initialize(context, context.getResources().getString(R.string.admob_app_id));

        AdView mAdview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

    }






}
