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
        if(itemPedido.getIdComum() == 0) {//se for o que está lançando o ItemPedido
            valores.put(Esquema.ItemPedido.ID_PEDIDO, itemPedido.getPedido().getIdComum());
            valores.put(Esquema.ItemPedido.SABOR, itemPedido.getSabor());
            valores.put(Esquema.ItemPedido.RECIPIENTE, itemPedido.getRecipiente());
            valores.put(Esquema.ItemPedido.QUANTIDADE, itemPedido.getQuantidade());
            valores.put(Esquema.ItemPedido.ENTREGUE, itemPedido.getEntregue());
            valores.put(Esquema.ItemPedido.OBSERVACAO, itemPedido.getObservacao());

            long id = db.insert(Esquema.ItemPedido.TABELA, null, valores);

            valores = new ContentValues();
            valores.put(Esquema.ItemPedido.ID_COMUM,id);

            String selecao = Esquema.ItemPedido._ID + " = ?";
            String[] args = {"" + id};

            db.update(Esquema.ItemPedido.TABELA,valores,selecao,args);

            return id;
        }else{//se for quem está replicando o ItemPedido
            valores.put(Esquema.ItemPedido.ID_PEDIDO, itemPedido.getPedido().getIdComum());
            valores.put(Esquema.ItemPedido.ID_COMUM, itemPedido.getIdComum());
            valores.put(Esquema.ItemPedido.SABOR, itemPedido.getSabor());
            valores.put(Esquema.ItemPedido.RECIPIENTE, itemPedido.getRecipiente());
            valores.put(Esquema.ItemPedido.QUANTIDADE, itemPedido.getQuantidade());
            valores.put(Esquema.ItemPedido.ENTREGUE, itemPedido.getEntregue());
            valores.put(Esquema.ItemPedido.OBSERVACAO, itemPedido.getObservacao());

            return db.insert(Esquema.ItemPedido.TABELA, null, valores);
        }
    }

    public int remover(ItemPedido itemPedido){
        String selecao = Esquema.ItemPedido.ID_COMUM + " LIKE ?";
        String[] args = { ""+itemPedido.getIdComum() };

        return db.delete(Esquema.ItemPedido.TABELA, selecao, args);
    }

    public int atualizarEstado(ItemPedido itemPedido){
        ContentValues valores = new ContentValues();
        valores.put(Esquema.ItemPedido.ENTREGUE,itemPedido.getEntregue());

        String selecao = Esquema.ItemPedido.ID_COMUM + " = ?";
        String[] args = {"" + itemPedido.getIdComum()};

        return db.update(Esquema.ItemPedido.TABELA,valores,selecao,args);
    }

    public ArrayList<ItemPedido> buscarItemPedidos(Pedido pedido){
        String[] projecao = {
                Esquema.ItemPedido._ID,
                Esquema.ItemPedido.ID_COMUM,
                Esquema.ItemPedido.SABOR,
                Esquema.ItemPedido.RECIPIENTE,
                Esquema.ItemPedido.QUANTIDADE,
                Esquema.ItemPedido.ENTREGUE,
                Esquema.ItemPedido.OBSERVACAO};
        String selecao = Esquema.ItemPedido.ID_PEDIDO + " = ?";
        String[] args = {"" + pedido.getIdComum()};
        String ordem = Esquema.ItemPedido.ID_COMUM + " ASC";

        Cursor cursor = db.query(Esquema.ItemPedido.TABELA,projecao,selecao,args,null,null,ordem);
        ItemPedido itemAux = null;
        ArrayList<ItemPedido> itemPedidos = new ArrayList<>();
        while(cursor.moveToNext()){
            itemAux = new ItemPedido();
            itemAux.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Esquema.ItemPedido._ID)));
            itemAux.setIdComum(cursor.getLong(cursor.getColumnIndexOrThrow(Esquema.ItemPedido.ID_COMUM)));
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
