package com.gdev.kumakuasa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CreditosActivity extends AppCompatActivity {

    private Methods methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);

        methods = new Methods(this);
    }

    public void onBackPressed(){
        methods.goToMain();
    }

    public void go_to_inicio(View view){
        methods.somClick();
        methods.goToMain();
    }
}