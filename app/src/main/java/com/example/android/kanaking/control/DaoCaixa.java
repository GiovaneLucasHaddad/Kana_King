package com.example.android.kanaking.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.android.kanaking.model.Caixa;

public class DaoCaixa {
    private SQLiteDatabase db;

    public DaoCaixa(Context context){
        BD auxBD = new BD(context);
        db = auxBD.getWritableDatabase();
        Toast.makeText(context,"Caixa iniciado",Toast.LENGTH_SHORT).show();
    }

    public long abrirCaixa(Caixa caixa){
        ContentValues valores = new ContentValues();
        valores.put(Esquema.Caixa.DATA_ABERTURA,caixa.getDataAbertura());
        valores.put(Esquema.Caixa.HORA_ABERTURA,caixa.getHoraAbertura());

        long id = db.insert(Esquema.Caixa.TABELA,null,valores);
        return id;
    }

    public int fecharCaixa(Caixa caixa){
        ContentValues valores = new ContentValues();
        valores.put(Esquema.Caixa.DATA_FECHAMENTO,caixa.getDataFechamento());
        valores.put(Esquema.Caixa.HORA_FECHAMENTO,caixa.getHoraFechamento());

        String selecao = Esquema.Caixa._ID + " = ?";
        String[] args = {"" + caixa.getNumero()};
        int linhas = db.update(Esquema.Caixa.TABELA,valores,selecao,args);
        return  linhas;
    }

    public Caixa ultimoCaixa(){
        String[] projecao = {
                Esquema.Caixa._ID,
                Esquema.Caixa.DATA_ABERTURA,
                Esquema.Caixa.HORA_ABERTURA,
                Esquema.Caixa.DATA_FECHAMENTO,
                Esquema.Caixa.HORA_FECHAMENTO};
        String selecao = Esquema.Caixa._ID + " = ?";
        String[] args = {"" + ultimoId()};

        Cursor cursor = db.query(Esquema.Caixa.TABELA,projecao,selecao,args,null,null,null);
        Caixa caixa = null;
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            caixa = new Caixa();
            caixa.setNumero(cursor.getLong(cursor.getColumnIndexOrThrow(Esquema.Caixa._ID)));
            caixa.setDataAbertura(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.Caixa.DATA_ABERTURA)));
            caixa.setHoraAbertura(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.Caixa.HORA_ABERTURA)));
            caixa.setDataFechamento(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.Caixa.DATA_FECHAMENTO)));
            caixa.setHoraFechamento(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.Caixa.HORA_FECHAMENTO)));
        }
        return caixa;
    }

    public long ultimoId(){
        String consulta = "SELECT MAX(" + Esquema.Caixa._ID + ") FROM " + Esquema.Caixa.TABELA;
        Cursor cursor = db.rawQuery(consulta,null);
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        cursor.close();
        return id;
    }
}
