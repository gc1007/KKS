package com.gdev.kumakuasa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gdev.kumakuasa.dominio.entidades.Pergunta;

public class ModoRapidoActivity extends AppCompatActivity {

    private Button bt1, bt2, bt3, bt4;
    private TextView quest,score,time;
    private ProgressBar pg;
    private ImageView sinal1,sinal2,sinal3,sinal4,motivation;

    private Methods methods;

    private long counter, maxTime=10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_rapido);

        methods = new Methods(this);

        quest = findViewById(R.id.txt_question);
        score = findViewById(R.id.txt_score);
        bt1 = findViewById(R.id.bt_op1);
        bt2 = findViewById(R.id.bt_op2);
        bt3 = findViewById(R.id.bt_op3);
        bt4 = findViewById(R.id.bt_op4);
        time = findViewById(R.id.temp);
        pg = findViewById(R.id.progresbar);
        sinal1 = findViewById(R.id.sinalR1);
        sinal2 = findViewById(R.id.sinalR2);
        sinal3 = findViewById(R.id.sinalR3);
        sinal4 = findViewById(R.id.sinalR4);
        motivation = findViewById(R.id.im_motivation);

        criarConexao();
        showQuest();
    }

    @Override
    public void onBackPressed() {
        methods.timer.cancel();
        methods.goToMain();
    }

    protected void onPause(){
        super.onPause();
        methods.timer.cancel();
    }

    protected void onRestart(){
        super.onRestart();
        temporizador();
    }


    private void criarConexao(){
        try {
            methods.criarConexao();
            Toast.makeText(this,"Conexao feita",Toast.LENGTH_LONG).show();

        }
        catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Erro");
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton("OK",null);
            dlg.show();
        }
    }


    private void showQuest(){
        Pergunta pergunta= methods.getQuest();
        methods.organizeOptions(pergunta);
        quest.setText(pergunta.question);
        bt1.setText(methods.Opcoes[0]);
        bt2.setText(methods.Opcoes[1]);
        bt3.setText(methods.Opcoes[2]);
        bt4.setText(methods.Opcoes[3]);
        methods.Opcoes[0]="";
        methods.Opcoes[1]="";
        methods.Opcoes[2]="";
        methods.Opcoes[3]="";
        setVisible();
        time.setTextColor(Color.WHITE);
        maxTime = 10000;
        temporizador();
    }

    // CLICK DE BUTÕES***************************************************************************************
    public void click_button1(View view) throws Exception {
        methods.somClick();
        boolean evalution = methods.evaluateAnswer(0);
        if (evalution) {
            setEnabled(false);
            if(methods.answerSelected(score,sinal1,motivation,1)) {
                littleDelayAndShowNewQuest();
                setEnabled(true);
            }
        }
        else {
            sinal1.setImageResource(R.drawable.erado);
            sinal1.setVisibility(View.VISIBLE);
            setEnabled(false);
            methods.findCorectAnswerAndExit(sinal1,sinal2,sinal3,sinal4,1);
        }

    }

    public void click_button2(View view) throws Exception {
        methods.somClick();
        boolean evalution = methods.evaluateAnswer(1);
        if (evalution) {
            setEnabled(false);
            if(methods.answerSelected(score,sinal2,motivation,1)) {
                littleDelayAndShowNewQuest();
                setEnabled(true);
            }
        }
        else {
            sinal2.setImageResource(R.drawable.erado);
            sinal2.setVisibility(View.VISIBLE);
            setEnabled(false);
            methods.findCorectAnswerAndExit(sinal1,sinal2,sinal3,sinal4,1);
        }

    }

    public void click_button3(View view) throws Exception {
        methods.somClick();
        boolean evalution = methods.evaluateAnswer(2);
        if (evalution) {
            setEnabled(false);
            if(methods.answerSelected(score,sinal3,motivation,1)) {
                littleDelayAndShowNewQuest();
                setEnabled(true);
            }
        }
        else {
            sinal3.setImageResource(R.drawable.erado);
            sinal3.setVisibility(View.VISIBLE);
            setEnabled(false);
            methods.findCorectAnswerAndExit(sinal1,sinal2,sinal3,sinal4,1);
        }

    }

    public void click_button4(View view) throws Exception {
        methods.somClick();
        boolean evalution = methods.evaluateAnswer(3);
        if (evalution) {
            setEnabled(false);
            if(methods.answerSelected(score,sinal4,motivation,1)) {
                littleDelayAndShowNewQuest();
                setEnabled(true);
            }
        }
        else {
            sinal4.setImageResource(R.drawable.erado);
            sinal4.setVisibility(View.VISIBLE);
            setEnabled(false);
            methods.findCorectAnswerAndExit(sinal1,sinal2,sinal3,sinal4,1);
        }

    }
    //************************************************************************************************************

    private void setEnabled(boolean i) {
        bt1.setEnabled(i);
        bt2.setEnabled(i);
        bt3.setEnabled(i);
        bt4.setEnabled(i);
    }

    private void setVisible(){
        bt1.setVisibility(View.VISIBLE);
        bt2.setVisibility(View.VISIBLE);
        bt3.setVisibility(View.VISIBLE);
        bt4.setVisibility(View.VISIBLE);
    }

    private void littleDelayAndShowNewQuest(){
        CountDownTimer timer = new CountDownTimer(600,1000) {
            @Override
            public void onTick(long l){ }
            @Override
            public void onFinish() { showQuest();}
        }.start();
    }


    private void temporizador() {

        methods.timer = new CountDownTimer(maxTime, 1000) {
            public void onTick(long millisUntilFinished) {
                maxTime = millisUntilFinished;
                counter = (maxTime / 1000);
                time.setText(String.valueOf(counter));
                pg.setProgress((int) ((counter*10)-10));
                if (counter < 6) {
                    time.setTextColor(Color.YELLOW);
                    methods.somAlert();
                }
            }
            @Override
            public void onFinish() {
                methods.goToGameOverRapido();
            }
        }.start();
    }


}