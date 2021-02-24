package com.gdev.kumakuasa;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gdev.kumakuasa.dominio.entidades.Pergunta;


public class ModoNormalActivity extends AppCompatActivity {


    private TextView quest, score;
    private Button bt1, bt2, bt3, bt4, btSkip, btfifty;
    private ImageView sinal1, sinal2, sinal3, sinal4, motivation;

    private Methods methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_normal);

        score = findViewById(R.id.txt_score);
        quest = findViewById(R.id.txt_question);

        bt1 = findViewById(R.id.bt_op1);
        bt2 = findViewById(R.id.bt_op2);
        bt3 = findViewById(R.id.bt_op3);
        bt4 = findViewById(R.id.bt_op4);
        btSkip = findViewById(R.id.bt_skip);
        btfifty = findViewById(R.id.bt_fifty);

        sinal1 = findViewById(R.id.sinal1);
        sinal2 = findViewById(R.id.sinal2);
        sinal3 = findViewById(R.id.sinal3);
        sinal4 = findViewById(R.id.sinal4);
        motivation = findViewById(R.id.im_motivation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startMethod();
        criarConexao();
        showQuest();
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


    private void startMethod(){
        Intent i = getIntent();
        int value = i.getIntExtra("Score",0);
        boolean usedFifty = i.getBooleanExtra("fifty",false);

        if(value != 0) {
            score.setText("Score: "+value);
            int[] inteiros = i.getIntArrayExtra("inteiros");
            assert inteiros != null;

            if(inteiros[0] == 0)
                btSkip.setVisibility(View.INVISIBLE);

            if(usedFifty)
                btfifty.setVisibility(View.INVISIBLE);

            methods = new Methods(this, i.getIntegerArrayListExtra("usadas"),i.getIntegerArrayListExtra("Cusadas"),value,inteiros[0],inteiros[1],usedFifty);
        }
        else
            methods = new Methods(this);
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
    }

    // CLICK DE BUTÕES***************************************************************************************
    public void click_button1(View view) throws Exception {
        methods.somClick();
        boolean evalution = methods.evaluateAnswer(0);
        if (evalution) {
            setEnabled(false);
            if(methods.answerSelected(score,sinal1,motivation,0)) {
                littleDelayAndShowNewQuest();
                setEnabled(true);
            }
        }
        else {
            sinal1.setImageResource(R.drawable.erado);
            sinal1.setVisibility(View.VISIBLE);
            setEnabled(false);
            methods.findCorectAnswerAndExit(sinal1,sinal2,sinal3,sinal4,0);
        }

    }

    public void click_button2(View view) throws Exception {
        methods.somClick();
        boolean evalution = methods.evaluateAnswer(1);
        if (evalution) {
            setEnabled(false);
            if(methods.answerSelected(score,sinal2,motivation,0)) {
                littleDelayAndShowNewQuest();
                setEnabled(true);
            }
        }
        else {
            sinal2.setImageResource(R.drawable.erado);
            sinal2.setVisibility(View.VISIBLE);
            setEnabled(false);
            methods.findCorectAnswerAndExit(sinal1,sinal2,sinal3,sinal4,0);

        }

    }

    public void click_button3(View view) throws Exception {
        methods.somClick();
        boolean evalution = methods.evaluateAnswer(2);
        if (evalution) {
            setEnabled(false);
            if(methods.answerSelected(score,sinal3,motivation,0)) {
                littleDelayAndShowNewQuest();
                setEnabled(true);
            }
        }
        else {
            sinal3.setImageResource(R.drawable.erado);
            sinal3.setVisibility(View.VISIBLE);
            setEnabled(false);
            methods.findCorectAnswerAndExit(sinal1,sinal2,sinal3,sinal4,0);
        }

    }

    public void click_button4(View view) throws Exception {
        methods.somClick();
        boolean evalution = methods.evaluateAnswer(3);
        if (evalution) {
            setEnabled(false);
            if(methods.answerSelected(score,sinal4,motivation,0)) {
                littleDelayAndShowNewQuest();
                setEnabled(true);
            }
        }
        else {
            sinal4.setImageResource(R.drawable.erado);
            sinal4.setVisibility(View.VISIBLE);
            setEnabled(false);
            methods.findCorectAnswerAndExit(sinal1,sinal2,sinal3,sinal4,0);

        }

    }
    //****************************************************************************************************

    private void setEnabled(boolean i) {
        bt1.setEnabled(i);
        bt2.setEnabled(i);
        bt3.setEnabled(i);
        bt4.setEnabled(i);
        btSkip.setEnabled(i);
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


    public void skip(View view) throws Exception {
        if(methods.skip(btSkip))
            showQuest();
    }

    public void FiftyFifty(View view){
       btfifty.setVisibility(View.INVISIBLE);
       methods.FiftyFifty(bt1,bt2,bt3,bt4);
    }


    //Navegação entre Activities*******************************************************************
    @Override
    public void onBackPressed(){
        methods.goToMain();
    }
    //**********************************************************************************************

}