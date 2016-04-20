package com.allhailme.johan.animatedbitmap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DrawView mDrawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDrawView = new DrawView(this);

        setContentView(mDrawView);
    }

    //handle resume/focus events
    @Override
    public void onResume() {
        super.onResume();
        //pass the resume event on to the sub-class
        mDrawView.resume();
    }

    //handle pause/minimize events
    @Override
    public void onPause() {
        super.onPause();
        //pass the pause event on to the sub-class
        mDrawView.pause();
    }
}
