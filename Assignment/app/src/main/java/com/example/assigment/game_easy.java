package com.example.assigment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class game_easy extends AppCompatActivity {
    @Override// disable back in phone
    public void onBackPressed() {
    }
    // declare variable
    String difficulty = "Easy";
    int move = 0;
    int firstImgInd=0;
    int firstImgBtnInd=0;
    boolean alreadyClicked = false;
    SQLiteDatabase db;
    ImageButton[] imageButtons;
    public int[] images ;
    Integer[] imageIndices = {0, 0, 1, 1, 2, 2, 3, 3};
    List<Integer> list = Arrays.asList(imageIndices);
    Random r = new Random();
    int time = r.nextInt(10)+1;
    TextView tvMove;
    Button btncon;
    String timeElapsedFormatted;
    double timeElapsedSeconds;
    int seconds = 0;
    int milliseconds = 0;
    int minutes =0;
    boolean Iscomplete;
    boolean IsHint;
    Button btnHint;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        // timer(10ms)

        public void run() {
            milliseconds += 10;
            if (milliseconds == 1000) {
                // increase second when ms is 1000
                seconds++;
                milliseconds = 0;
            }
            if (seconds == 60){
                // increase minute when second is 60
                minutes++;
                seconds = 0;
            }
            timeElapsedFormatted = String.format("%01d:%02d:%02d", minutes ,seconds, milliseconds / 10);
            timeElapsedSeconds = (double)(minutes * 60) + (double)seconds + ((double)milliseconds / 1000.0);
            tvMove.setText(String.format(getString(R.string.move), move, timeElapsedFormatted, calculateScore()));
            if(Iscomplete){
                // output when win the game
                handler.removeCallbacks(runnable);

                tvMove.setText(String.format(getString(R.string.fin), move, timeElapsedFormatted, calculateScore()));
            } else if (IsHint) {
                handler.removeCallbacks(runnable);
            }
            else{
                handler.postDelayed(this, 10);
            }


        }
    };
    private RewardedAd rewardedAd;
    private player bg = new player();
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handler.postDelayed(runnable, 100);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_easy);
        btnHint = findViewById(R.id.hint);
        // stop any bgm from playing
        player.mediaPlayer.stop();
        // set vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        // set ad for hint
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("TAG", loadAdError.toString());
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d("Thanks", "You just load an ad.");
                    }
                });
        btnHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make the following code uncommented if you don't want ads
                // IsHint = true;
                // handleHint();

                if (rewardedAd != null) {
                    Activity activityContext = game_easy.this;
                    rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            IsHint = true;
                            handleHint();
                        }
                    });
                } else {
                    Log.d("TAG", "The rewarded ad wasn't ready yet.");
                    Toast.makeText(game_easy.this, "ONLY 1 AD IN EACH GAME!", Toast.LENGTH_SHORT).show();

                }
            }
        });


    // set image
        images = new int[]{R.drawable.easy_1, R.drawable.easy_2, R.drawable.easy_3, R.drawable.easy_4};


        // random the image for at least 1 time, at most 11 times
        // make the game so random
        for (int i=0; i<time;i++){
            Collections.shuffle(list, new Random());
        }

        // set image id
        imageButtons = new ImageButton[]{findViewById(R.id.btn1), findViewById(R.id.btn2), findViewById(R.id.btn3),
                findViewById(R.id.btn4), findViewById(R.id.btn5), findViewById(R.id.btn6), findViewById(R.id.btn7),
                findViewById(R.id.btn8)};
        // set edit text
        tvMove = findViewById(R.id.move);
        btncon = findViewById(R.id.con);
        // continue btn code
        btncon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // set default image to card.jpg for all image
        for (ImageButton imageButton : imageButtons) {
            imageButton.setImageResource(R.drawable.card);
            imageButton.setClickable(true);
        }


        // set on click listener on image buttons
        for (int i = 0; i < imageButtons.length; i++) {
            final int index = i;
            imageButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    main(index);
                }
            });
        }
        // play bgm for easy
        bg.playbg(game_easy.this, R.raw.easy_bg);


    }
    // take onclick listener and do things
    public void main(int i){
        // disclose the image
        imageButtons[i].setImageResource(images[list.get(i)]);
        if(alreadyClicked){
            // if already clicked check whether the images are same
            for (ImageButton imageButton : imageButtons) {
                // disable press of all btn
                imageButton.setClickable(false);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // increase move count
                    move++;
                    if(firstImgInd == list.get(i)){
                        // 2 card are the same, play correct sound, vibrate
                        imageButtons[i].setVisibility(View.INVISIBLE);
                        imageButtons[firstImgBtnInd].setVisibility(View.INVISIBLE);

                        MediaPlayer.create(game_easy.this, R.raw.match_sound).start();
                        if (vibrator.hasVibrator()) {
                            // vibrate for 500 milliseconds
                            vibrator.vibrate(100);
                        }
                    }else{
                        // 2 card not same, vibrate, play not correct sound
                        MediaPlayer.create(game_easy.this, R.raw.wrong).start();

                        if (vibrator != null && vibrator.hasVibrator()) {
                            vibrator.vibrate(500);
                        }
                    }

                    for (ImageButton imageButton : imageButtons) {
                        // set back all card image to default (card.jpg)
                        imageButton.setImageResource(R.drawable.card);
                        imageButton.setClickable(true);
                    }
                    if(imageButtons[0].getVisibility() == View.INVISIBLE &&
                            imageButtons[1].getVisibility() == View.INVISIBLE &&
                            imageButtons[2].getVisibility() == View.INVISIBLE &&
                            imageButtons[3].getVisibility() == View.INVISIBLE &&
                            imageButtons[4].getVisibility() == View.INVISIBLE &&
                            imageButtons[5].getVisibility() == View.INVISIBLE &&
                            imageButtons[6].getVisibility() == View.INVISIBLE &&
                            imageButtons[7].getVisibility() == View.INVISIBLE ){
                        // if all btn is INVISIBLE (i.e. all btn matched)
                        // do win thing
                        Iscomplete = true;
                        btncon.setVisibility(View.VISIBLE);
                        btnHint.setVisibility(View.INVISIBLE);
                        done();
                    }
                    alreadyClicked = false;
                }
            }, 1000);
        }else{
            // set already clicked, show btn's image
            alreadyClicked = true;
            imageButtons[i].setClickable(false);
            firstImgInd = list.get(i);
            firstImgBtnInd=i;
        }
    }
    public void done(){
        bg.stop();
        bg.play(game_easy.this, R.raw.win_sound);
        if (vibrator.hasVibrator()) {
            // out put morse code in vibration method
//            String morseCode = ".... . .-.. .--."; // Morse code
//            long[] patternDot = {0, 50, 200}; // Vibration pattern for a dot
//            long[] patternDash = {0, 150, 200}; // Vibration pattern for a dash
//            long[] patternLetter = {0, 0, 300}; // Vibration pattern for a letter pause

            long[] pattern = {0, 50, 200,0, 50, 200,0, 50, 200,0, 50, 200,0, 0, 300,
                    0, 50, 200,0, 0, 300,0, 50, 200,0, 150, 200,0, 50, 200,0, 50, 200,
                    0, 0, 300,0, 50, 200,0, 150, 200,0, 150, 200,0, 50, 200};

            vibrator.vibrate(pattern, -1);
        }
        // date time thing for db
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(currentDate);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String formattedTime = timeFormat.format(currentDate);
        SimpleDateFormat timesFormat = new SimpleDateFormat("hh:mm:ss");
        String formattedTimes = timesFormat.format(currentDate);
        String gameID = formattedDate+formattedTimes;

        db = SQLiteDatabase.openDatabase("/data/data/com.example.assigment/AssigmentDB", null, SQLiteDatabase.OPEN_READWRITE);
        // insert db
        db.execSQL("INSERT INTO Record(gameID, playDate, playTime, moves, difficulty, score) values( '" +
                    gameID + "','" + formattedDate + "','" + formattedTime + "','" + move + "','"+ difficulty + "','"+ calculateScore() +"');");

        db.close();

    }
    private String calculateScore(){
        // score calculate and times 100 for beautify the format
        double score;
        double basicScore = 100.0;
        double timePenalty = (timeElapsedSeconds-30.0)*2;
        double movePenalty = (move-6)*5;
        score= basicScore-timePenalty-movePenalty;
        return ""+Math.round(score*100);
    }

    private void handleHint(){
        // check is hint or not
        if(IsHint){
            // random show some image (in a range of 1-4)(luck based)
            Random ra = new Random();
            for (ImageButton imageButton : imageButtons) {
                imageButton.setImageResource(0);
                imageButton.setClickable(false);
            }
            for (int i = 0; i < 4; i++) {
                // disclose image
                int index = ra.nextInt(7);
                imageButtons[index].setImageResource(images[list.get(index)]);
            }
            // wait for 5s to disclose the card ( not exactly 5s, based on ad video)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    IsHint = false;
                    handler.postDelayed(runnable, 100);
                    for (ImageButton imageButton : imageButtons) {
                        imageButton.setImageResource(R.drawable.card);
                        imageButton.setClickable(true);
                    }
                }
            },5000);

        }
    }
}