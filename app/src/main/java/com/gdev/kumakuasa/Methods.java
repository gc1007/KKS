package com.gdev.kumakuasa;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.gdev.kumakuasa.database.DadosOpenHelper;
import com.gdev.kumakuasa.dominio.entidades.Curiosidade;
import com.gdev.kumakuasa.dominio.entidades.Pergunta;
import com.gdev.kumakuasa.dominio.repositorio.BDRepositorio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Methods {

    private BDRepositorio BDRepositorio;

    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;

    private Context context;
    private InputStream is;
    public CountDownTimer timer;

    private MediaPlayer click, motivationSong,beep;
    public MediaPlayer song;

    private ArrayList<Integer> PerguntasUsadas, CuriosidadesUsadas;
    public String [] Opcoes;
    private int MaxQuest = 166, MaxCuriosity = 17, usedMotivation = -1, numSaltos = 2, PosAnswer;


    private int gamePoints, victoryPoints= 71;
    public boolean fiftyUsed = false;

    Intent importantValues;



    // Construtores****************************************
    public Methods(Context context){
        this.context = context;
        this.PerguntasUsadas = new ArrayList<Integer>();
        this.CuriosidadesUsadas = new ArrayList<Integer>();
        this.Opcoes = new String[4];
        this.gamePoints = 0;
        this.importantValues = new Intent(context, AnimationActivity.class);
    }

    public Methods(Context context, ArrayList<Integer> PUsadas, ArrayList<Integer> CUsadas, int gamePoints, int numSaltos, int usedMotivation ,boolean fiftyUsed){
        this.context = context;
        this.importantValues = new Intent(context, AnimationActivity.class);
        this.PerguntasUsadas = PUsadas;
        this.CuriosidadesUsadas = CUsadas;
        this.Opcoes = new String[4];
        this.gamePoints = gamePoints;
        this.numSaltos = numSaltos;
        this.usedMotivation = usedMotivation;
        this.fiftyUsed = fiftyUsed;

    }

    public Methods(Context context, ArrayList<Integer> CUsadas){
        this.context = context;
        this.CuriosidadesUsadas = CUsadas;
    }


    public Methods(Context context, InputStream is){
        this.context = context;
        this.is = is;
        this.PerguntasUsadas = new ArrayList<Integer>();
        this.CuriosidadesUsadas = new ArrayList<Integer>();
        this.Opcoes = new String[4];
        this.gamePoints = 0;
    }
    //*******************************************************

    //Manejamento de BD********************************************************************
    public void criarConexao(){

        dadosOpenHelper = new DadosOpenHelper(context);
        conexao = dadosOpenHelper.getWritableDatabase();

        BDRepositorio = new BDRepositorio(conexao);
    }
    //Controlador de ações*************************************************************************************

    public Pergunta getQuest(){

        Pergunta pergunta;
        Random random = new Random();
        int rand_int = random.nextInt(MaxQuest);

        while(PerguntasUsadas.contains(rand_int))
            rand_int = random.nextInt(MaxQuest);

        PerguntasUsadas.add(rand_int);

        pergunta = BDRepositorio.buscarPergunta(rand_int);

        if(pergunta != null)
            return pergunta;

        else
            return getQuest();
    }

    public Curiosidade getCuriosity(){
        Random random = new Random();
        Curiosidade curiosidade;
        int rand_int = random.nextInt(MaxCuriosity);

        while(CuriosidadesUsadas.contains(rand_int))
            rand_int = random.nextInt(MaxCuriosity);

        CuriosidadesUsadas.add(rand_int);

        curiosidade = BDRepositorio.buscarCuriosidade(rand_int);

        if(curiosidade != null)
            return curiosidade;

        else
            return getCuriosity();

    }

    public void organizeOptions(Pergunta pergunta){
        Random random = new Random();
        List<Integer> numUsados = new ArrayList<Integer>();

        int rand_int = random.nextInt(4);

        while (numUsados.size() < 4) {

            while (numUsados.contains(rand_int)) {
                rand_int = random.nextInt(4);
            }

            switch (numUsados.size()) {
                case 0:
                    Opcoes[rand_int] = pergunta.answer;
                    numUsados.add(rand_int);
                    PosAnswer = rand_int;
                    break;

                case 1:
                    Opcoes[rand_int] = pergunta.opone;
                    numUsados.add(rand_int);
                    break;

                case 2:
                    Opcoes[rand_int] = pergunta.optwo;
                    numUsados.add(rand_int);
                    break;

                case 3:
                    Opcoes[rand_int] = pergunta.opthree;
                    numUsados.add(rand_int);
                    break;

                default:
                    break;
            }
        }
        numUsados.clear();
    }


    public boolean evaluateAnswer(int numButton){

        if(numButton == PosAnswer){
          gamePoints++;
          return true;
        }
        return false;
    }


    public void findCorectAnswerAndExit(ImageView sinal1, ImageView sinal2, ImageView sinal3, ImageView sinal4, final int modo){
        if(modo == 1 && timer != null)
            timer.cancel();

        switch (PosAnswer) {
            case 0:
                sinal1.setImageResource(R.drawable.certo);
                sinal1.setVisibility(View.VISIBLE);
                break;

            case 1:
                sinal2.setImageResource(R.drawable.certo);
                sinal2.setVisibility(View.VISIBLE);
                break;

            case 2:
                sinal3.setImageResource(R.drawable.certo);
                sinal3.setVisibility(View.VISIBLE);
                break;

            case 3:
                sinal4.setImageResource(R.drawable.certo);
                sinal4.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
        timer = new CountDownTimer(600, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                goToGameOverNormal(modo);
            }
        }.start();
    }


    public boolean answerSelected(TextView score, final ImageView sinal, ImageView motivation, int modo){
        if(modo == 1 && timer != null)
            timer.cancel();

        sinal.setImageResource(R.drawable.certo);
        sinal.setVisibility(View.VISIBLE);
        score.setText("Score: " + gamePoints);
        if ((gamePoints % 8) == 0) {
            func_motivation(motivation);
        }
        if ((gamePoints % 10 ) == 0 && modo == 0){
            goToAnimation();

        }

        timer = new CountDownTimer(600, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (gamePoints != victoryPoints) {
                    sinal.setVisibility(View.INVISIBLE);
                }
            }
        }.start();
        if(gamePoints == victoryPoints && modo == 0) {
            goToVitoria(modo);
            return false;
        }

        else if(gamePoints == MaxQuest && modo == 1) {
            goToVitoria(modo);
            return false;
        }

        return true;
    }

    //*********************************************************************************************

    //Auxiliares***********************************************************************************

    public void func_motivation(ImageView motivation) {
        Random i = new Random();
        int choice = i.nextInt(3);

        while(choice == usedMotivation && usedMotivation !=-1)
            choice = i.nextInt(3);

        usedMotivation = choice;

        if (choice == 0) {
            motivation.setImageResource(R.drawable.iale);
            somMotivation(R.raw.iale);
        }
        if (choice == 1) {
            motivation.setImageResource(R.drawable.doxi);
            somMotivation(R.raw.doxi);
        }
        if (choice == 2) {
            motivation.setImageResource(R.drawable.ben_zogado);
            somMotivation(R.raw.ben_zogado);
        }
        motivation.setVisibility(View.VISIBLE);
        Animation desloca = new TranslateAnimation(-80, 0, 0, 0);
        desloca.setDuration(1000);
        motivation.startAnimation(desloca);
        motivation.setVisibility(View.INVISIBLE);

    }

    private void setInvisible(int position, Button bt1, Button bt2, Button bt3, Button bt4){
        Animation desloca;
        switch (position){
            case 0:
                desloca = new TranslateAnimation(0, 1500, 0, 0);
                desloca.setDuration(1000);
                bt1.startAnimation(desloca);
                bt1.setVisibility(View.INVISIBLE);
                break;

            case 1:
                desloca = new TranslateAnimation(0, -1500, 0, 0);
                desloca.setDuration(1000);
                bt2.startAnimation(desloca);
                bt2.setVisibility(View.INVISIBLE);
                break;

            case 2:
                desloca = new TranslateAnimation(0, 1500, 0, 0);
                desloca.setDuration(1000);
                bt3.startAnimation(desloca);
                bt3.setVisibility(View.INVISIBLE);
                break;

            case 3:
                desloca = new TranslateAnimation(0, -1500, 0, 0);
                desloca.setDuration(1000);
                bt4.startAnimation(desloca);
                bt4.setVisibility(View.INVISIBLE);
                break;

            default:
                break;
        }
    }

    private void putImportantValues(){
        int [] inteiros = new int [2];
        inteiros[0] = numSaltos;
        inteiros[1] = usedMotivation;
        importantValues.putExtra("inteiros",inteiros);
        importantValues.putExtra("fifty",fiftyUsed);
        importantValues.putExtra("Score",gamePoints);
        importantValues.putIntegerArrayListExtra("usadas",PerguntasUsadas);
        importantValues.putIntegerArrayListExtra("Cusadas",CuriosidadesUsadas);

    }

    //Helpers////////////////////////////////////////////////////////////////////////
    public boolean skip(Button bt) throws Exception {
        if (numSaltos > 0) {
            if (PerguntasUsadas.size() != MaxQuest) {
                numSaltos--;
            }
            else {
                goToVitoria(0);
                return false;
            }
        }
        if(numSaltos == 0) {
            bt.setVisibility(View.INVISIBLE);
        }
        return true;
    }

    public void FiftyFifty(Button bt1, Button bt2, Button bt3, Button bt4){
        Random i = new Random();
        int randInt = i.nextInt(4);
        int randInt2 = i.nextInt(4);

        while(randInt == PosAnswer || randInt2 == PosAnswer || randInt == randInt2) {
            randInt = i.nextInt(4);
            randInt2 = i.nextInt(4);
        }

        setInvisible(randInt,bt1,bt2,bt3,bt4);
        setInvisible(randInt2,bt1,bt2,bt3,bt4);
        fiftyUsed = true;

    }
    ///////////////////////////////////////////////////////////////////////////////////////

    //*********************************************************************************************

    // Manejamento de ficheiro e inserção na BD****************************************************

    public String loadJSONFromAsset(InputStream is) {
        String json;
        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public void InsereQuestsOnBD() {
        Pergunta pergunta = new Pergunta();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(is));
            // fetch JSONArray named users
            JSONArray userArray = obj.getJSONArray("questions");
            // implement for loop for getting users list data
            for (int i = 0; i < userArray.length(); i++) {
                // create a JSONObject for fetching single user data
                JSONObject userDetail = userArray.getJSONObject(i);
                // fetch email and name and store it in arraylist
                pergunta.question = userDetail.getString("pergunta");
                pergunta.answer = userDetail.getString("answer");
                pergunta.opone = userDetail.getString("op1");
                pergunta.optwo = userDetail.getString("op2");
                pergunta.opthree = userDetail.getString("op3");

                BDRepositorio.inserir(pergunta);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void InsereCuriositiesOnBD() {
        Curiosidade curiosidade = new Curiosidade();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(is));
            // fetch JSONArray named users
            JSONArray userArray = obj.getJSONArray("curiosities");
            // implement for loop for getting users list data
            for (int i = 0; i < userArray.length(); i++) {
                // create a JSONObject for fetching single user data
                JSONObject userDetail = userArray.getJSONObject(i);
                // fetch email and name and store it in arraylist
                curiosidade.text = userDetail.getString("text");
                BDRepositorio.inserirCuriosidade(curiosidade);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void apagar(){
        BDRepositorio.excluir(1);
    }
    //**********************************************************************************************

    //Sons*****************************************************************
    public void somClick(){
        if(click==null){
            click= MediaPlayer.create(context,R.raw.click);
            click.start();
        }
        else{
            if(click.isPlaying()){
                click.pause();
            }
            else
                click.start();
        }
    }

    public void somGameOver(){
        if(song==null){
            song= MediaPlayer.create(context,R.raw.game_over);
            song.start();
        }
        else{
            if(song.isPlaying()){
                song.pause();
            }
            else
                song.start();
        }
    }

    public void somTimeOut(){
        if(song==null){
            song= MediaPlayer.create(context,R.raw.time_out);
            song.start();
        }
        else{
            if(song.isPlaying()){
                song.pause();
            }
            else
                song.start();
        }
    }

    public void somMotivation(int song) {
        if (motivationSong == null) {
            motivationSong = MediaPlayer.create(context, song);
            motivationSong.start();
        }
        motivationSong = null;

    }

    public void somAlert(){
        if(beep==null){
            beep=MediaPlayer.create(context,R.raw.beep);
            beep.start();
        }
        else
            beep.start();
    }

    public void somVitoria() {
        if (song == null) {
            song = MediaPlayer.create(context, R.raw.vitoria);
            song.start();
        } else {
            if (song.isPlaying()) {
                song.pause();
            } else
                song.start();
        }
    }
    //*****************************************************************

    //Navegação entre Activities********************************************************

    private void goToVitoria(int modo){
        somClick();
        Intent i = new Intent(context, VitoriaActivity.class);
        i.putExtra("Modo",modo);
        i.putExtra("Score",gamePoints);
        context.startActivity(i);
    }

    public void goToModoNormal(){
        somClick();
        Intent new_page = new Intent(context,ModoNormalActivity.class);
        context.startActivity(new_page);

    }
    public void goToModoRapido(){
        somClick();
        Intent new_page = new Intent(context,ModoRapidoActivity.class);
        context.startActivity(new_page);
    }

    public void goToMain(){
        Intent new_page = new Intent(context,MainActivity.class);
        context.startActivity(new_page);
    }

    public void goToCreditos() {
        somClick();
        Intent i = new Intent(context, CreditosActivity.class);
        context.startActivity(i);
    }

    private void goToGameOverNormal(int modo){
        Intent i = new Intent(context, GameOverNormalActivity.class);
        i.putExtra("Score",gamePoints);
        i.putExtra("Modo",modo);
        context.startActivity(i);
    }

    public void goToGameOverRapido(){
            Intent i = new Intent(context, GameOverRapidoActivity.class);
            i.putExtra("Score",gamePoints);
            context.startActivity(i);
        }

    private void goToAnimation(){
        putImportantValues();
        context.startActivity(importantValues);
    }
    //*************************************************************************************

}
