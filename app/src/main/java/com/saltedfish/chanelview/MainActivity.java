package com.saltedfish.chanelview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ParallaxListView parallaxListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (null != getSupportActionBar()) {
            getSupportActionBar().hide();
        }
        parallaxListView = (ParallaxListView) findViewById(R.id.list);
        parallaxListView.setFastScrollEnabled(false);
        parallaxListView.setAdapter(new ParallaxAdapter(this));
    }
}
