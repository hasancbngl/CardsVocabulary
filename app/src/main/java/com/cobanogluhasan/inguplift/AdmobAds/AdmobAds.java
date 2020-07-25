package com.cobanogluhasan.inguplift.AdmobAds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.cobanogluhasan.inguplift.Adapter.SynonymAdapter;
import com.cobanogluhasan.inguplift.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class AdmobAds {

    private static InterstitialAd interstitialAd;

    public static void displayInterstitialAds(Context context) {

        //noinspection deprecation
        MobileAds.initialize(context, context.getResources().getString(R.string.admob_app_id));

        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(context.getResources().getString(R.string.interstitial_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdLoaded() {
                interstitialAd.show();
            }

        });

    }

}
