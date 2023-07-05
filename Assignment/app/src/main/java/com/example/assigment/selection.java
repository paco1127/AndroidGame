package com.example.assigment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class selection extends AppCompatActivity {

    @Override
    public void onBackPressed() {
    }

    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.difficulty_selection);
        // ad
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView mAdView = findViewById(R.id.adView);
        mAdView.loadAd(adRequest);


        Button btneasy = findViewById(R.id.easy);
        Button btnmedium = findViewById(R.id.medium);
        Button btnhard = findViewById(R.id.hard);
        Button btnimpo = findViewById(R.id.impossible);

        // level btn listener
        btneasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(selection.this, game_easy.class);
                startActivity(intent);
                finish();
            }
        });

        btnmedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(selection.this, game_medium.class);
                startActivity(intent);
                finish();
            }
        });

        btnhard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(selection.this, game_hard.class);
                startActivity(intent);
                finish();
            }
        });

        btnimpo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(selection.this, game_impossible.class);
                startActivity(intent);
                finish();
            }
        });
    }
}