package com.example.android.kanaking.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.android.kanaking.model.Caixa;

import java.util.ArrayList;

public class DaoCaixa {
    private SQLiteDatabase db;

    public DaoCaixa(Context context){
        BD auxBD = new BD(context);
        db = auxBD.getWritableDatabase();
    }

    public long abrirCaixa(Caixa caixa){
        ContentValues valores = new ContentValues();
        //Inserir o registro
        valores.put(Esquema.Caixa.NUMERO, caixa.getNumero());
        valores.put(Esquema.Caixa.DATA_ABERTURA, caixa.getDataAbertura());
        valores.put(Esquema.Caixa.HORA_ABERTURA, caixa.getHoraAbertura());
        valores.put(Esquema.Caixa.FUNDO, caixa.getFundo());
        return db.insert(Esquema.Caixa.TABELA, null, valores);
    }

    public int fecharCaixa(Caixa caixa){
        ContentValues valores = new ContentValues();
        valores.put(Esquema.Caixa.DATA_FECHAMENTO,caixa.getDataFechamento());
        valores.put(Esquema.Caixa.HORA_FECHAMENTO,caixa.getHoraFechamento());

        String selecao = Esquema.Caixa._ID + " = ?";
        String[] args = {"" + caixa.getId()};
        return  db.update(Esquema.Caixa.TABELA,valores,selecao,args);
    }

    public int reabrirCaixa(Caixa caixa){
        ContentValues valores = new ContentValues();
        valores.put(Esquema.Caixa.DATA_FECHAMENTO,"");
        valores.put(Esquema.Caixa.HORA_FECHAMENTO,"");

        String selecao = Esquema.Caixa._ID + " = ?";
        String[] args = {"" + caixa.getId()};
        return  db.update(Esquema.Caixa.TABELA,valores,selecao,args);
    }

    public Caixa ultimoCaixa(long ultimoId){//Traz o último registro de caixa para iniciar a lista de Pedidos
        if(ultimoId == -1){
            return null;
        }
        String[] projecao = {
                Esquema.Caixa._ID,
                Esquema.Caixa.NUMERO,
                Esquema.Caixa.DATA_ABERTURA,
                Esquema.Caixa.HORA_ABERTURA,
                Esquema.Caixa.DATA_FECHAMENTO,
                Esquema.Caixa.HORA_FECHAMENTO,
                Esquema.Caixa.FUNDO};
        String selecao = Esquema.Caixa._ID + " = ?";
        String[] args = {"" + ultimoId};

        Caixa caixa = null;
        String aux;
        Cursor cursor = db.query(Esquema.Caixa.TABELA,projecao,selecao,args,null,null,null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            caixa = new Caixa();
            caixa.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Esquema.Caixa._ID)));
            caixa.setNumero(cursor.getLong(cursor.getColumnIndexOrThrow(Esquema.Caixa.NUMERO)));
            caixa.setDataAbertura(cursor.getString(cursor.getColumnIndexOrThrow(Esquema.Caixa.DATA_ABERTURA)));
            caixa.setHoraAbertura(cursor.getString(cursor.getColumnIndexOrThrow(Esquema.Caixa.HORA_ABERTURA)));

            aux = cursor.getString(cursor.getColumnIndexOrThrow(Esquema.Caixa.DATA_FECHAMENTO));
            if(aux != null){
                caixa.setDataFechamento(aux);
            }
            aux = cursor.getString(cursor.getColumnIndexOrThrow(Esquema.Caixa.HORA_FECHAMENTO));
            if(aux != null){
                caixa.setHoraFechamento(aux);
            }
            caixa.setFundo(cursor.getDouble(cursor.getColumnIndexOrThrow(Esquema.Caixa.FUNDO)));
        }
        cursor.close();
        return caixa;
    }

    public long ultimoId(){
        String[] projecao = {Esquema.Caixa._ID};
        String ordem = Esquema.Caixa._ID + " ASC";
        long id = -1;
        Cursor cursor = db.query(Esquema.Caixa.TABELA,projecao,null,null,null,null,ordem);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            id = cursor.getLong(cursor.getColumnIndexOrThrow(Esquema.Caixa._ID));
        }
        cursor.close();
        return id;
    }

    public String pesuisarCaixas(String dataInicial, String dataFinal){
        String[] projecao = {
                Esquema.Caixa.NUMERO};
        String selecao = Esquema.Caixa.DATA_ABERTURA + " BETWEEN ? AND ?";
        String[] args = {dataInicial,dataFinal};
        String ordem = Esquema.Caixa.NUMERO + " ASC";

        StringBuilder caixas = new StringBuilder();
        String aux;
        Cursor cursor = db.query(Esquema.Caixa.TABELA,projecao,selecao,args,null,null,ordem);
        if (cursor.getCount() > 0){
            while(cursor.moveToNext()) {
                aux = String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(Esquema.Caixa.NUMERO)));
                if(aux.equals("")){
                    continue;
                }
                caixas.append(aux).append(",");
            }
        }
        cursor.close();
        if(caixas.toString().equals("")){
            return "";
        }
        return caixas.substring(0,caixas.length()-1);
    }
}
