package com.gdev.kumakuasa;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    // declaração de variáveis
    private ImageView iv1;
    private Button bt1, bt2,bt3;

    private Methods methods;

    //*******************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inicialização de variáveis
        iv1=(ImageView) findViewById(R.id.logo);
        bt1=(Button)findViewById(R.id.bt_mp1);
        bt2=(Button)findViewById(R.id.bt_mp2);
        bt3=(Button)findViewById(R.id.creditos);
        //***************************************************************************************

        //anima os elementos do ecrã ao iniciar
        bt1.setEnabled(false);
        bt2.setEnabled(false);
        bt3.setEnabled(false);
        anima();
        FirstStep();

    }

    @Override
    protected void onStart() {
        super.onStart();
        bt1.setEnabled(true);
        bt2.setEnabled(true);
        bt3.setEnabled(true);
    }

    //Manejamento de BD****************************************************************************

    // Anlisa se é necessário carregar ou apenas ler a BD
    private void FirstStep(){
        SharedPreferences preference = getSharedPreferences("preferencia", Context.MODE_PRIVATE);
        Boolean lerJSON = preference.getBoolean("leitura",true);

        if(lerJSON) {
            try {
                InputStream is= getAssets().open("questions.json");
                methods = new Methods(this, is);
                criarConexao();
                inserirQuests();
                //-------------
                is= getAssets().open("curiosities.json");
                methods = new Methods(this, is);
                criarConexao();
                inserirCuriosities();
                SharedPreferences.Editor ed = preference.edit();
                ed.putBoolean("leitura",false);
                ed.apply();
                //Toast.makeText(this,"Ficheiro lido",Toast.LENGTH_LONG).show();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else{

            methods = new Methods(this);
            criarConexao();
            //Toast.makeText(this,"Ficheiro não foi lido",Toast.LENGTH_LONG).show();
        }
    }


    //Liga-se a BD
    private void criarConexao(){
       try {
            methods.criarConexao();
            //Toast.makeText(this,"Conexao feita",Toast.LENGTH_LONG).show();

        }
        catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Erro");
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton("OK",null);
            dlg.show();
            //Toast.makeText(this,"Conexao falhou",Toast.LENGTH_LONG).show();
        }
    }

    //Insere dados na BD
    private void inserirQuests(){
        try {
            methods.InsereQuestsOnBD();
            //Toast.makeText(this,"Inseridos dados com sucesso",Toast.LENGTH_LONG).show();

        }
        catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Erro");
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton("OK",null);
            dlg.show();
        }
    }

    private void inserirCuriosities(){
        try {
            methods.InsereCuriositiesOnBD();
            //Toast.makeText(this,"Inseridos dadosC com sucesso",Toast.LENGTH_LONG).show();

        }
        catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Erro");
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton("OK",null);
            dlg.show();
        }
    }

    //**************************************************************************************************


    //Navegação entre Activities********************************************************************

    public void go_to_modo_normal(View view){
        Intent i = new Intent(this,AnimationActivity.class); // vai primeiro ao animation e dps para o normal
        i.putExtra("firstTime",1);
        startActivity(i);
        //methods.goToModoNormal();

    }
   public void go_to_modo_rapido(View view){
        methods.goToModoRapido();
    }

    public void go_to_creditos(View view) {
        methods.goToCreditos();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed(){
        finishAffinity();
    }
    //**************************************************************************************************


    public void anima(){
        Animation desloca= new TranslateAnimation(0,0,-100,50);
        Animation desloca2= new TranslateAnimation(50,0,0,0);
        Animation desloca3= new TranslateAnimation(-50,0,0,0);
        desloca.setFillAfter(true);
        desloca.setDuration(3500);
        desloca2.setDuration(3000);
        desloca3.setDuration(3000);
        iv1.startAnimation(desloca);
        bt1.startAnimation(desloca2);
        bt2.startAnimation(desloca3);
        bt3.startAnimation(desloca2);
    }
}