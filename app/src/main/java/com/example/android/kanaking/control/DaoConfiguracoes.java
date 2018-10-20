package com.example.android.kanaking.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.example.android.kanaking.Constantes.MOENDA;

public class DaoConfiguracoes {
    private SQLiteDatabase db;

    public DaoConfiguracoes(Context context){
        BD auxBD = new BD(context);
        db = auxBD.getWritableDatabase();
    }
    public int definir_MAC(String modo, String mac){
        ContentValues valores = new ContentValues();
        if(modo.equals(MOENDA)){
            valores.put(Esquema.Configuracoes.MAC1,mac);
        }else{
            valores.put(Esquema.Configuracoes.MAC2,mac);
        }
        String selecao = Esquema.Configuracoes._ID + " = ?";
        String[] args = {"1"};

        return db.update(Esquema.Configuracoes.TABELA,valores,selecao,args);
    }
    public String buscar_MAC(String modo){
        String[] projecao;
        if(modo.equals(MOENDA)){
            projecao = new String [] {Esquema.Configuracoes.MAC1};
        }else {
            projecao = new String [] {Esquema.Configuracoes.MAC2};
        }
        String selecao = Esquema.Configuracoes._ID + " = ?";
        String[] args = {"1"};

        Cursor cursor = db.query(Esquema.Configuracoes.TABELA,projecao,selecao,args,null,null,null);

        String mac = "";
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            mac = cursor.getString(0);
        }
        cursor.close();

        if(mac.equals("0")){
            mac = "";
        }

        return mac;
    }
}
