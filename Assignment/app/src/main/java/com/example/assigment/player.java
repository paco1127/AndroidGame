package com.example.assigment;

import android.content.Context;
import android.media.MediaPlayer;

public class player {
    public static MediaPlayer mediaPlayer;
    public player(){

    }
    public void playbg(Context loc, int ID){
        mediaPlayer = MediaPlayer.create(loc, ID);
        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaPlayer.setLooping(true); // set to loop indefinitely
            mediaPlayer.setVolume(0.5f, 0.5f);
        }
    }
    public void play(Context loc, int ID){
        mediaPlayer = MediaPlayer.create(loc, ID);
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }
    public void stop(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
