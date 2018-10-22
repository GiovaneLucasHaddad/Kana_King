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
    }

    public long inserir(ItemPedido itemPedido){
        ContentValues valores = new ContentValues();

        valores.put(Esquema.ItemPedido.SEQUENCIA, itemPedido.getSequencia());
        valores.put(Esquema.ItemPedido.VENDA, itemPedido.getPedido().getVenda());
        valores.put(Esquema.ItemPedido.CAIXA, itemPedido.getPedido().getCaixa().getNumero());
        valores.put(Esquema.ItemPedido.SABOR, itemPedido.getSabor());
        valores.put(Esquema.ItemPedido.RECIPIENTE, itemPedido.getRecipiente());
        valores.put(Esquema.ItemPedido.QUANTIDADE, itemPedido.getQuantidade());
        valores.put(Esquema.ItemPedido.ENTREGUE, itemPedido.getEntregue());
        valores.put(Esquema.ItemPedido.OBSERVACAO, itemPedido.getObservacao());

        return db.insert(Esquema.ItemPedido.TABELA, null, valores);
    }

    public int remover(ItemPedido itemPedido){
        String selecao = Esquema.ItemPedido.SEQUENCIA + " LIKE ?";
        String[] args = { ""+itemPedido.getSequencia() };

        return db.delete(Esquema.ItemPedido.TABELA, selecao, args);
    }

    public int atualizarEstado(ItemPedido itemPedido){
        ContentValues valores = new ContentValues();
        valores.put(Esquema.ItemPedido.ENTREGUE,itemPedido.getEntregue());
        String selecao = Esquema.ItemPedido.SEQUENCIA + " = ? AND " + Esquema.ItemPedido.VENDA + " = ? AND " + Esquema.ItemPedido.CAIXA + " = ?";
        String[] args = {"" + itemPedido.getSequencia(), "" + itemPedido.getPedido().getVenda(), "" + itemPedido.getPedido().getCaixa().getNumero()};

        return db.update(Esquema.ItemPedido.TABELA,valores,selecao,args);
    }

    public void buscarItemPedidos(Pedido pedido){
        String[] projecao = {
                Esquema.ItemPedido._ID,
                Esquema.ItemPedido.SEQUENCIA,
                Esquema.ItemPedido.SABOR,
                Esquema.ItemPedido.RECIPIENTE,
                Esquema.ItemPedido.QUANTIDADE,
                Esquema.ItemPedido.ENTREGUE,
                Esquema.ItemPedido.OBSERVACAO};
        String selecao = Esquema.ItemPedido.CAIXA + " = ? AND " + Esquema.ItemPedido.VENDA + " = ?";
        String[] args = {"" + pedido.getCaixa().getNumero(), "" + pedido.getVenda()};
        String ordem = Esquema.ItemPedido.SEQUENCIA + " ASC";

        Cursor cursor = db.query(Esquema.ItemPedido.TABELA,projecao,selecao,args,null,null,ordem);
        ItemPedido itemAux = null;
        ArrayList<ItemPedido> itemPedidos = new ArrayList<>();
        while(cursor.moveToNext()){
            itemAux = new ItemPedido();
            itemAux.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Esquema.ItemPedido._ID)));
            itemAux.setSequencia(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.ItemPedido.SEQUENCIA)));
            itemAux.setSabor(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.ItemPedido.SABOR)));
            itemAux.setRecipiente(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.ItemPedido.RECIPIENTE)));
            itemAux.setQuantidade(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.ItemPedido.QUANTIDADE)));
            itemAux.setEntregue(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.ItemPedido.ENTREGUE)));
            itemAux.setObservacao(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.ItemPedido.OBSERVACAO)));
            itemAux.setPedido(pedido);

            itemPedidos.add(itemAux);
        }
        cursor.close();
        pedido.setItemPedidos(itemPedidos);
    }
}
