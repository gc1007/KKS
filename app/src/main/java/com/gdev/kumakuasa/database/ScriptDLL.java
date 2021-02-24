package com.gdev.kumakuasa.database;

public class ScriptDLL {

    public static String getCreateTableQuestions(){
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS PERGUNTAS ( ");
        sql.append("    ID        INTEGER       PRIMARY KEY AUTOINCREMENT NOT NULL,");
        sql.append("    QUESTION  VARCHAR (250) NOT NULL DEFAULT (''),");
        sql.append("    ANSWER    VARCHAR (250) NOT NULL DEFAULT (''),");
        sql.append("    OPONE     VARCHAR (250) NOT NULL DEFAULT (''),");
        sql.append("    OPTWO     VARCHAR (250) NOT NULL DEFAULT (''),");
        sql.append("    OPTHREE   VARCHAR (250) NOT NULL DEFAULT ('')");
        sql.append(");");

        return sql.toString();
    }

    public static String getCreateTableCuriosity(){
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS CURIOSIDADES ( ");
        sql.append("    ID           INTEGER       PRIMARY KEY AUTOINCREMENT NOT NULL,");
        sql.append("    CURIOSIDADE  VARCHAR (350) NOT NULL DEFAULT ('')");
        sql.append(");");

        return sql.toString();
    }
}
