package com.gdev.kumakuasa.dominio.repositorio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gdev.kumakuasa.dominio.entidades.Curiosidade;
import com.gdev.kumakuasa.dominio.entidades.Pergunta;


public class BDRepositorio {

    private SQLiteDatabase conexao;

    //construtor
    public BDRepositorio(SQLiteDatabase conexao){
        this.conexao = conexao;
    }


    public void inserir(Pergunta pergunta){

        ContentValues contentValues = new ContentValues();
        contentValues.put("QUESTION", pergunta.question);
        contentValues.put("ANSWER",pergunta.answer);
        contentValues.put("OPONE",pergunta.opone);
        contentValues.put("OPTWO",pergunta.optwo);
        contentValues.put("OPTHREE",pergunta.opthree);

        conexao.insertOrThrow("PERGUNTAS",null, contentValues);

    }

    public void inserirCuriosidade(Curiosidade curiosidade){

        ContentValues contentValues = new ContentValues();
        contentValues.put("CURIOSIDADE", curiosidade.text);

        conexao.insertOrThrow("CURIOSIDADES",null, contentValues);

    }

    public void excluir(int id){

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(id);

        //conexao.delete("PERGUNTAS","ID = ?",parametros);
        conexao.delete("PERGUNTAS",null,null);
        conexao.delete("CURIOSIDADES",null,null);
    }

    public void alterar(Pergunta pergunta){

        ContentValues contentValues = new ContentValues();
        contentValues.put("QUESTION",pergunta.question);
        contentValues.put("ANSWER",pergunta.answer);
        contentValues.put("OPONE",pergunta.opone);
        contentValues.put("OPTWO",pergunta.optwo);
        contentValues.put("OPTHREE",pergunta.opthree);

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(pergunta.id);

        conexao.update("PERGUNTAS",contentValues,"ID = ?",parametros);
        //ID = ? esta criar um parametro que será passado pelo ultimo parametro do metodo update


    }

    public Pergunta buscarPergunta(int id){

        Pergunta pergunta = new Pergunta();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * ");
        sql.append(" FROM PERGUNTAS ");
        sql.append(" WHERE ID = ? ");

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(id);

        Cursor resultado = conexao.rawQuery(sql.toString(),parametros);


        if(resultado.getCount() > 0){

            resultado.moveToFirst();

            pergunta.id       = resultado.getInt(resultado.getColumnIndexOrThrow("ID"));
            pergunta.question = resultado.getString(resultado.getColumnIndexOrThrow("QUESTION"));
            pergunta.answer   = resultado.getString(resultado.getColumnIndexOrThrow("ANSWER"));
            pergunta.opone    = resultado.getString(resultado.getColumnIndexOrThrow("OPONE"));
            pergunta.optwo    = resultado.getString(resultado.getColumnIndexOrThrow("OPTWO"));
            pergunta.opthree  = resultado.getString(resultado.getColumnIndexOrThrow("OPTHREE"));


            return pergunta;

        }

        return null;
    }

    public Curiosidade buscarCuriosidade(int id){

        Curiosidade curiosidade = new Curiosidade();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * ");
        sql.append(" FROM CURIOSIDADES ");
        sql.append(" WHERE ID = ? ");

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(id);

        Cursor resultado = conexao.rawQuery(sql.toString(),parametros);


        if(resultado.getCount() > 0){

            resultado.moveToFirst();

            curiosidade.id       = resultado.getInt(resultado.getColumnIndexOrThrow("ID"));
            curiosidade.text = resultado.getString(resultado.getColumnIndexOrThrow("CURIOSIDADE"));

            return curiosidade;

        }

        return null;
    }
}
