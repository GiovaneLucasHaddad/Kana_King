package com.example.android.kanaking.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.android.kanaking.model.ItemPedido;

public class DaoItemPedido {
    private SQLiteDatabase db;

    public DaoItemPedido(Context context){
        BD auxBD = new BD(context);
        db = auxBD.getWritableDatabase();
        Toast.makeText(context,"ItemPedido iniciado",Toast.LENGTH_SHORT).show();
    }

    public long inserir(ItemPedido itemPedido){
        ContentValues valores = new ContentValues();
        valores.put(Esquema.ItemPedido.ID_PEDIDO,itemPedido.getPedido().getId());
        valores.put(Esquema.ItemPedido.SABOR,itemPedido.getSabor());
        valores.put(Esquema.ItemPedido.RECIPIENTE,itemPedido.getRecipiente());
        valores.put(Esquema.ItemPedido.QUANTIDADE,itemPedido.getQuantidade());
        valores.put(Esquema.ItemPedido.ENTREGUE,itemPedido.getEntregue());
        valores.put(Esquema.ItemPedido.OBSERVACAO,itemPedido.getObservacao());

        long id = db.insert(Esquema.Pedido.TABELA,null,valores);
        return id;
    }

    public int remover(ItemPedido itemPedido){
        String selecao = Esquema.ItemPedido._ID + " LIKE ?";
        String[] args = { ""+itemPedido.getId() };
        int linhas = db.delete(Esquema.Pedido.TABELA, selecao, args);
        return linhas;
    }

    public int atualizarEstado(ItemPedido itemPedido){
        ContentValues valores = new ContentValues();
        valores.put(Esquema.ItemPedido.ENTREGUE,itemPedido.getEntregue());

        String selecao = Esquema.ItemPedido._ID + " = ?";
        String[] selecaoArgs = {"" + itemPedido.getId()};
        int linhas = db.update(Esquema.ItemPedido.TABELA,valores,selecao,selecaoArgs);
        return  linhas;
    }
}
