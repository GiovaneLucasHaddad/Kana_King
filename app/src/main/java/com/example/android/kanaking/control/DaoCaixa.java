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

    public Caixa ultimoCaixa(){//Traz o último registro de caixa para iniciar a lista de Pedidos
        String[] projecao = {
                Esquema.Caixa._ID,
                Esquema.Caixa.NUMERO,
                Esquema.Caixa.DATA_ABERTURA,
                Esquema.Caixa.HORA_ABERTURA,
                Esquema.Caixa.DATA_FECHAMENTO,
                Esquema.Caixa.HORA_FECHAMENTO,
                Esquema.Caixa.FUNDO};
        String selecao = Esquema.Caixa._ID + " = ?";
        String[] args = {"" + ultimoId()};

        Cursor cursor = db.query(Esquema.Caixa.TABELA,projecao,selecao,args,null,null,null);
        Caixa caixa = null;
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            caixa = new Caixa();
            caixa.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Esquema.Caixa._ID)));
            caixa.setNumero(cursor.getLong(cursor.getColumnIndexOrThrow(Esquema.Caixa.NUMERO)));
            caixa.setDataAbertura(cursor.getString(cursor.getColumnIndexOrThrow(Esquema.Caixa.DATA_ABERTURA)));
            caixa.setHoraAbertura(cursor.getString(cursor.getColumnIndexOrThrow(Esquema.Caixa.HORA_ABERTURA)));
            caixa.setDataFechamento(cursor.getString(cursor.getColumnIndexOrThrow(Esquema.Caixa.DATA_FECHAMENTO)));
            caixa.setHoraFechamento(cursor.getString(cursor.getColumnIndexOrThrow(Esquema.Caixa.HORA_FECHAMENTO)));
            caixa.setFundo(cursor.getDouble(cursor.getColumnIndexOrThrow(Esquema.Caixa.FUNDO)));
        }
        cursor.close();
        return caixa;
    }

    private long ultimoId(){
        String consulta = "SELECT MAX(" + Esquema.Caixa._ID + ") FROM " + Esquema.Caixa.TABELA;
        Cursor cursor = db.rawQuery(consulta,null);
        cursor.moveToFirst();
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(Esquema.Caixa._ID));
        cursor.close();
        return id;
    }
}
