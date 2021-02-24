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

public class GameOverRapidoActivity extends AppCompatActivity {

    private TextView score, bestScore;
    private ImageView image;

    private Methods methods;
    private SharedPreferences preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_rapido);

        methods = new Methods(this);

        score = findViewById(R.id.score);
        bestScore = findViewById(R.id.bestscore);

        image = findViewById(R.id.ImageA);

        preference = getSharedPreferences("preferencia", Context.MODE_PRIVATE);

        methods.somTimeOut();
        showScore();
        anima();
    }

    @Override
    public void onBackPressed() {
        methods.song.stop();
        methods.goToMain();
    }

    public void goToInicio(View view) {
        methods.song.stop();
        methods.somClick();
        methods.goToMain();
    }

    public void tentarDeNovo(View view) {
        methods.song.stop();
        methods.goToModoRapido();
    }

    private void showScore() {
        Intent i = getIntent();
        int AtualScore = i.getIntExtra("Score", 0);
        int SavedScore = preference.getInt("BestScoreRapido", 0);

        if (AtualScore > SavedScore) {
            SharedPreferences.Editor ed = preference.edit();
            ed.putInt("BestScoreRapido", AtualScore);
            ed.apply();
            score.setText("Score: " + AtualScore);
            bestScore.setText("Best Score: " + AtualScore);
        } else {
            score.setText("Score: " + AtualScore);
            bestScore.setText("Best Score: " + SavedScore);
        }
    }

    public void anima() {
        Animation desloca = new TranslateAnimation(0, 0, -150, 45);
        Animation desloca2 = new TranslateAnimation(50, 0, 0, 0);
        desloca.setFillAfter(true);
        desloca.setDuration(3000);
        desloca2.setFillAfter(true);
        desloca2.setDuration(3000);
        image.startAnimation(desloca);
        score.startAnimation(desloca2);
        bestScore.startAnimation(desloca2);
    }
}