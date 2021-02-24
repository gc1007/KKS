package com.gdev.kumakuasa.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DadosOpenHelper extends SQLiteOpenHelper {
    public DadosOpenHelper(Context context) {
        super(context, "DADOS", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(ScriptDLL.getCreateTableQuestions() );
        db.execSQL(ScriptDLL.getCreateTableCuriosity() );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder sql = new StringBuilder();
        sql.append(" DROP TABLE IF EXISTS PERGUNTAS;");
        sql.append(" DROP TABLE IF EXISTS CURIOSIDADES;");
        db.execSQL(sql.toString());
        onCreate(db);

    }
}
