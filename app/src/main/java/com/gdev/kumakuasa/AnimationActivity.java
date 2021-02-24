package com.gdev.kumakuasa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gdev.kumakuasa.dominio.entidades.Curiosidade;

import java.util.ArrayList;
import java.util.Random;

public class AnimationActivity extends AppCompatActivity {

    private Animation animation;

    private ImageView avatar, map;

    private int value, atualMap, newMap, sinal = 1;

    private Methods methods;
    private Dialog dialog;
    private Intent importantValues;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);


        avatar = findViewById(R.id.avatar);
        map = findViewById(R.id.saoTome);

        dialog = new Dialog(this);

        importantValues = getIntent();
        value = importantValues.getIntExtra("Score", 0);

        if(value == 0)
            methods = new Methods(this);

        else// recupera curiosidades usadas.
            methods = new Methods(this,importantValues.getIntegerArrayListExtra("Cusadas"));




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timer.cancel();
        int firstTime = importantValues.getIntExtra("firstTime", 0);
        Intent intent = new Intent(this, ModoNormalActivity.class);

        if (firstTime == 0) { // envia dados importantes para modo normal para dar seguimento de onde parou
            intent.putExtra("inteiros", importantValues.getIntArrayExtra("inteiros"));
            intent.putIntegerArrayListExtra("usadas", importantValues.getIntegerArrayListExtra("usadas"));
            intent.putIntegerArrayListExtra("Cusadas", importantValues.getIntegerArrayListExtra("Cusadas"));
            intent.putExtra("Score", importantValues.getIntExtra("Score", 0));
            intent.putExtra("fifty", importantValues.getBooleanExtra("fifty", false));
        }
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        switch (value) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.set);
                atualMap = R.drawable.stnull;
                newMap = R.drawable.caue;
                avatar.startAnimation(animation);
                delay(4600, true);
                break;

            case 10:
                atualMap = R.drawable.stnull;
                newMap = R.drawable.caue;
                piscar();
                break;

            case 20:
                map.setImageResource(R.drawable.caue);
                animation = AnimationUtils.loadAnimation(this, R.anim.to_cantagalo);
                animation.setFillAfter(true);
                atualMap = R.drawable.caue;
                newMap = R.drawable.cantagalo;
                avatar.startAnimation(animation);
                delay(3600, false);
                break;

            case 30:
                map.setImageResource(R.drawable.cantagalo);
                animation = AnimationUtils.loadAnimation(this, R.anim.to_lemba);
                animation.setFillAfter(true);
                atualMap = R.drawable.cantagalo;
                newMap = R.drawable.lemba;
                avatar.startAnimation(animation);
                delay(3600, false);
                break;

            case 40:
                map.setImageResource(R.drawable.lemba);
                animation = AnimationUtils.loadAnimation(this, R.anim.to_mezochi);
                animation.setFillAfter(true);
                atualMap = R.drawable.lemba;
                newMap = R.drawable.mezochi;
                avatar.startAnimation(animation);
                delay(3600, false);
                break;

            case 50:
                map.setImageResource(R.drawable.mezochi);
                animation = AnimationUtils.loadAnimation(this, R.anim.to_lobata);
                animation.setFillAfter(true);
                atualMap = R.drawable.mezochi;
                newMap = R.drawable.lobata;
                avatar.startAnimation(animation);
                delay(3600, false);
                break;

            case 60:
                map.setImageResource(R.drawable.lobata);
                animation = AnimationUtils.loadAnimation(this, R.anim.to_agua_grande);
                animation.setFillAfter(true);
                atualMap = R.drawable.lobata;
                newMap = R.drawable.st;
                avatar.startAnimation(animation);
                delay(3600, false);
                break;

            case 70:
                map.setImageResource(R.drawable.st);
                animation = AnimationUtils.loadAnimation(this, R.anim.to_principe);
                animation.setFillAfter(true);
                atualMap = R.drawable.principenull;
                newMap = R.drawable.principe;
                map = findViewById(R.id.principe);
                avatar.startAnimation(animation);
                delay(3600, false);
                break;

            default:
                break;
        }
    }

    private void delay(int time, final boolean begin) {
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long untilF) {
            }

            @Override
            public void onFinish() {
                if (begin){
                    openDialog(begin);
                }

                else
                    piscar();
            }
        }.start();
    }

    private void piscar() {
        timer = new CountDownTimer(2700, 500) {
            @Override
            public void onTick(long untilF) {
                if (untilF > 500) {
                    if (sinal == 1) {
                        map.setImageResource(newMap);
                        sinal = 0;
                    } else {
                        map.setImageResource(atualMap);
                        sinal = 1;
                    }
                } else
                    map.setImageResource(newMap);
            }

            @Override
            public void onFinish() {
                openDialog(false);
            }
        }.start();
    }


    private void openDialog(final boolean begin) {
        SharedPreferences preference = getSharedPreferences("preferencia", Context.MODE_PRIVATE);
        Boolean firstRun = preference.getBoolean("firstRun",true);

        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView close = dialog.findViewById(R.id.close);
        Button ok = dialog.findViewById(R.id.ok);
        TextView title = dialog.findViewById(R.id.dialog_title);
        TextView texto = dialog.findViewById(R.id.curiosidade);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(begin)
                    methods.goToModoNormal();
                else
                    onBackPressed();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(begin)
                    methods.goToModoNormal();
                else
                    onBackPressed();
            }
        });

        if(begin == firstRun) {
            if (!firstRun) {
                methods.criarConexao();
                Curiosidade c = methods.getCuriosity();
                title.setText("Curiosidade");
                texto.setText(c.text);
            } else {
                SharedPreferences.Editor ed = preference.edit();
                ed.putBoolean("firstRun", false);
                ed.apply();
            }

            dialog.show();
        }

        else
            methods.goToModoNormal();
    }

}