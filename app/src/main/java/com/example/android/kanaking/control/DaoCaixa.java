package com.example.android.kanaking.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.android.kanaking.model.Caixa;

public class DaoCaixa {
    private SQLiteDatabase bd;

    public DaoCaixa(Context context){
        BD auxBD = new BD(context);
        bd = auxBD.getWritableDatabase();
        Toast.makeText(context,"Caixa iniciado",Toast.LENGTH_SHORT).show();
    }

    public long inserir(Caixa caixa){
        ContentValues valores = new ContentValues();
        valores.put(Esquema.Caixa.DATA_ABERTURA,caixa.getDataAbertura());
        valores.put(Esquema.Caixa.HORA_ABERTURA,caixa.getHoraAbertura());

        long id = bd.insert(Esquema.Caixa.TABELA,null,valores);
        return id;
    }




}
