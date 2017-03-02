package com.zhousl.musicplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zhousl.musicplayer.view.PlayProgressView;

public class MainActivity extends AppCompatActivity {

    private PlayProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressView = (PlayProgressView) findViewById(R.id.progress);
    }
}
