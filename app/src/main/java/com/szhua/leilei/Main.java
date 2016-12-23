package com.szhua.leilei;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.szhua.scoreview.ScoreView;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScoreView scoreView = (ScoreView) findViewById(R.id.score_view);
        scoreView.setRealScore("60");
        scoreView.setTotalScore("100");
        scoreView.startAnimation();
    }
}
