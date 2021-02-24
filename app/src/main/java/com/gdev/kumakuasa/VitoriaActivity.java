package com.gdev.kumakuasa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class VitoriaActivity extends AppCompatActivity {

    private TextView score;
    private ImageView image;

    private Methods methods;
    SharedPreferences preference;

    private int score_value, modo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitoria);


        score = (TextView) findViewById(R.id.score);
        image = (ImageView) findViewById(R.id.taca);

        methods = new Methods(this);
        Intent intent = getIntent();
        preference = getSharedPreferences("preferencia", Context.MODE_PRIVATE);

        score_value = intent.getIntExtra("Score", 0);
        modo = intent.getIntExtra("Modo",0);

        methods.somVitoria();
        score.setText("Score: " + score_value);
        anima_func();
        updateBestScore();
    }


    @Override
    public void onBackPressed() {
        methods.song.stop();
        methods.goToMain();
    }


    public void go_to_inicio(View view) {
        methods.song.stop();
        methods.somClick();
        methods.goToMain();
    }


    private void updateBestScore() {
        int SavedScore = 0;
        String ScoreType="BestScore";
        if(modo == 0)
            SavedScore = preference.getInt("BestScore", 0);

        else if(modo == 1) {
            SavedScore = preference.getInt("BestScoreRapido", 0);
            ScoreType = "BestScoreRapido";
        }

        if (score_value > SavedScore) {
            SharedPreferences.Editor ed = preference.edit();
            ed.putInt(ScoreType, score_value);
            ed.apply();
        }
    }

    public void anima_func() {
        Animation desloca = new TranslateAnimation(0, 0, -150, 60);
        Animation desloca2 = new TranslateAnimation(200, 0, 0, 0);
        desloca.setFillAfter(true);
        desloca.setDuration(4000);
        desloca2.setFillAfter(true);
        desloca2.setDuration(4000);
        image.startAnimation(desloca);
        score.startAnimation(desloca2);


    }
}