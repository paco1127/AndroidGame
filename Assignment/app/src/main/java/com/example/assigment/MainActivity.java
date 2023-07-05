package com.example.assigment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
    }
    SQLiteDatabase db;
    private AdView mAdView;

    player bg =new player();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ad showing
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView = findViewById(R.id.adView);
        // load the ad
        mAdView.loadAd(adRequest);



        // create db
        String sql;
        db = SQLiteDatabase.openDatabase("/data/data/com.example.assigment/AssigmentDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);

        sql = "CREATE TABLE IF NOT EXISTS Record(" + "gameID int PRIMARY KEY ," + "playDate text, "  + "playTime text, " + "moves int, " + " difficulty text, " + "score double); ";
        db.execSQL(sql);
        db.close();

        Button btnplay = findViewById(R.id.play);
        Button btnrank = findViewById(R.id.rank);
        Button btnrecord = findViewById(R.id.record);
        Button btnclose = findViewById(R.id.close);
        Button btnhelp = findViewById(R.id.help);

        // play bgm
        bg.playbg(MainActivity.this, R.raw.main_bg);

        // different btn go to different page

        btnhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, help.class);
                startActivity(intent);
            }
        });

        btnrank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, g_rank.class);
                startActivity(intent);
            }
        });

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, selection.class);
                startActivity(intent);
            }
        });

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                System.exit(0);
            }
        });

        btnrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, p_record.class);
                startActivity(intent);
            }
        });
    }

    // play the bgm on resume( when player some back from playing)

    @Override
    protected void onResume() {
        super.onResume();
        if (player.mediaPlayer == null){
            bg.playbg(MainActivity.this, R.raw.main_bg);
        }

    }
}