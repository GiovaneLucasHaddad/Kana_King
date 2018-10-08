package com.example.android.kanaking.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.android.kanaking.model.Pedido;

public class DaoPedido {
    private SQLiteDatabase db;

    public DaoPedido(Context context){
        BD auxBD = new BD(context);
        db = auxBD.getWritableDatabase();
        Toast.makeText(context,"Pedido iniciado",Toast.LENGTH_SHORT).show();
    }

    public long inserir(Pedido pedido){
        ContentValues valores = new ContentValues();
        valores.put(Esquema.Pedido.ID_CAIXA,pedido.getCaixa().getNumero());
        valores.put(Esquema.Pedido.COMANDA,pedido.getComanda());
        valores.put(Esquema.Pedido.ESTADO,pedido.getEstado());
        valores.put(Esquema.Pedido.VALOR,pedido.getValor());
        valores.put(Esquema.Pedido.FORMA_PAGAMENTO,pedido.getFormaPagamento());
        valores.put(Esquema.Pedido.DATA,pedido.getData());
        valores.put(Esquema.Pedido.HORA,pedido.getHora());

        long id = db.insert(Esquema.Pedido.TABELA,null,valores);
        return id;
    }

    public int remover(Pedido pedido){
        String selecao = Esquema.Pedido._ID + " LIKE ?";
        String[] args = { ""+pedido.getId() };
        int linhas = db.delete(Esquema.Pedido.TABELA, selecao, args);
        return linhas;
    }

    public int atualizarEstado(Pedido pedido){
        ContentValues valores = new ContentValues();
        valores.put(Esquema.Pedido.ESTADO,pedido.getEstado());

        String selecao = Esquema.Pedido._ID + " = ?";
        String[] selecaoArgs = {"" + pedido.getId()};
        int linhas = db.update(Esquema.Pedido.TABELA,valores,selecao,selecaoArgs);
        return  linhas;
    }


}
