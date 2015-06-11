package com.example.android.happybirthday;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity {

    private SoundMeter mSensor;
    private static final int POLL_INTERVAL = 200;


    public int counter = 0;
    public int mThreshold = 8;
    private int mPollDelay = 1;

    AnimationDrawable frameAnimation;
    ImageView flameView;
    private Handler mHandler = new Handler();

    private Runnable mSleepTask = new Runnable() {
        public void run() {
            start();
        }
    };
    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = mSensor.getAmplitude();
            if(amp > mThreshold){
                counter++;
            }
            if(counter > 4){
                flameView.setBackgroundResource(R.drawable.stick);
            }
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flameView = (ImageView) findViewById(R.id.flame);
        flameView.setBackgroundResource(R.drawable.animated_flame);
        frameAnimation = (AnimationDrawable) flameView.getBackground();

        mSensor = new SoundMeter();
        start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            frameAnimation.start();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onStop() {
        super.onStop();
        stop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void start() {
        counter = 0;
        mSensor.start();
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }

    private void stop() {
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        mSensor.stop();
    }

    private void sleep() {
        mSensor.stop();
        mHandler.postDelayed(mSleepTask, 1000*mPollDelay);
    }

}