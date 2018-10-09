package com.example.android.kanaking.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.android.kanaking.model.ItemPedido;
import com.example.android.kanaking.model.Pedido;

import java.util.ArrayList;

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

        return db.insert(Esquema.Pedido.TABELA,null,valores);
    }

    public int remover(ItemPedido itemPedido){
        String selecao = Esquema.ItemPedido._ID + " LIKE ?";
        String[] args = { ""+itemPedido.getId() };

        return db.delete(Esquema.Pedido.TABELA, selecao, args);
    }

    public int atualizarEstado(ItemPedido itemPedido){
        ContentValues valores = new ContentValues();
        valores.put(Esquema.ItemPedido.ENTREGUE,itemPedido.getEntregue());

        String selecao = Esquema.ItemPedido._ID + " = ?";
        String[] selecaoArgs = {"" + itemPedido.getId()};

        return db.update(Esquema.ItemPedido.TABELA,valores,selecao,selecaoArgs);
    }

    public ArrayList<ItemPedido> buscarItemPedidos(Pedido pedido){
        String[] projecao = {
                Esquema.ItemPedido._ID,
                Esquema.ItemPedido.SABOR,
                Esquema.ItemPedido.RECIPIENTE,
                Esquema.ItemPedido.QUANTIDADE,
                Esquema.ItemPedido.ENTREGUE,
                Esquema.ItemPedido.OBSERVACAO};
        String selecao = Esquema.ItemPedido.ID_PEDIDO + " = ?";
        String[] args = {"" + pedido.getId()};
        String ordem = Esquema.ItemPedido._ID + " ASC";

        Cursor cursor = db.query(Esquema.ItemPedido.TABELA,projecao,selecao,args,null,null,ordem);
        ItemPedido itemAux = null;
        ArrayList<ItemPedido> itemPedidos = new ArrayList<>();
        while(cursor.moveToNext()){
            itemAux = new ItemPedido();
            itemAux.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Esquema.ItemPedido._ID)));
            itemAux.setSabor(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.ItemPedido.SABOR)));
            itemAux.setRecipiente(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.ItemPedido.RECIPIENTE)));
            itemAux.setQuantidade(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.ItemPedido.QUANTIDADE)));
            itemAux.setEntregue(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.ItemPedido.ENTREGUE)));
            itemAux.setObservacao(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.ItemPedido.OBSERVACAO)));
            itemAux.setPedido(pedido);

            itemPedidos.add(itemAux);
        }
        cursor.close();
        return itemPedidos;
    }
}
