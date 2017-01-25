package com.example.kentversion1.jamesjrh52.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.media.MediaPlayer;

//import static com.example.kentversion1.jamesjrh52.myapplication.R.raw.splashjingle2;

/**
 * Created by James on 30/03/2016.
 *
 * Class for the splash screen
 */
public class First extends Activity {

    MediaPlayer splashSound;


    @Override
    protected void onCreate(Bundle savedInstaneState) {
        super.onCreate(savedInstaneState);
        setContentView(R.layout.front);
        //play wav audio file
        splashSound = MediaPlayer.create(this, R.raw.splashjingle2);
        splashSound.start();
        Thread myThread = new Thread(){
            @Override

            //sleep for a second then start MapsActivity
        public void run(){

                try {
                    sleep(1000);
                    Intent startMapsActivity = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(startMapsActivity);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();

    }

}
