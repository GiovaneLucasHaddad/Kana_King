package com.example.android.kanaking.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.android.kanaking.model.Caixa;
import com.example.android.kanaking.model.Pedido;

import java.util.ArrayList;

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

        return db.insert(Esquema.Pedido.TABELA,null,valores);
    }

    public int remover(Pedido pedido){
        String selecao = Esquema.Pedido._ID + " LIKE ?";
        String[] args = { ""+pedido.getId() };
        return db.delete(Esquema.Pedido.TABELA, selecao, args);
    }

    public int atualizarEstado(Pedido pedido){
        ContentValues valores = new ContentValues();
        valores.put(Esquema.Pedido.ESTADO,pedido.getEstado());

        String selecao = Esquema.Pedido._ID + " = ?";
        String[] args = {"" + pedido.getId()};

        return db.update(Esquema.Pedido.TABELA,valores,selecao,args);
    }

    public ArrayList<Pedido> buscarPedidos(Caixa caixa){
        String[] projecao = {
                Esquema.Pedido._ID,
                Esquema.Pedido.COMANDA,
                Esquema.Pedido.ESTADO,
                Esquema.Pedido.VALOR,
                Esquema.Pedido.FORMA_PAGAMENTO,
                Esquema.Pedido.DATA,
                Esquema.Pedido.HORA};
        String selecao = Esquema.Pedido.ID_CAIXA + " = ?";
        String[] args = {"" + caixa.getNumero()};
        String ordem = Esquema.Pedido._ID + " ASC";

        Cursor cursor = db.query(Esquema.Pedido.TABELA,projecao,selecao,args,null,null,ordem);
        Pedido pedidoAux = null;
        ArrayList<Pedido> pedidos = new ArrayList<>();
        while(cursor.moveToNext()){
            pedidoAux = new Pedido();
            pedidoAux.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Esquema.Pedido._ID)));
            pedidoAux.setComanda(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.Pedido.COMANDA)));
            pedidoAux.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.Pedido.ESTADO)));
            pedidoAux.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow(Esquema.Pedido.VALOR)));
            pedidoAux.setFormaPagamento(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.Pedido.FORMA_PAGAMENTO)));
            pedidoAux.setData(cursor.getString(cursor.getColumnIndexOrThrow(Esquema.Pedido.DATA)));
            pedidoAux.setHora(cursor.getString(cursor.getColumnIndexOrThrow(Esquema.Pedido.HORA)));
            pedidoAux.setCaixa(caixa);

            pedidos.add(pedidoAux);
        }
        cursor.close();

        return pedidos;
    }

    public Pedido buscarPedido(Long id, Caixa caixa){ //Consulta um pedido que acabou de ser inserido
        String[] projecao = {
                Esquema.Pedido._ID,
                Esquema.Pedido.COMANDA,
                Esquema.Pedido.ESTADO,
                Esquema.Pedido.VALOR,
                Esquema.Pedido.FORMA_PAGAMENTO,
                Esquema.Pedido.DATA,
                Esquema.Pedido.HORA};
        String selecao = Esquema.Pedido._ID + " = ?";
        String[] args = {"" + id};

        Cursor cursor = db.query(Esquema.Pedido.TABELA,projecao,selecao,args,null,null,null);
        Pedido pedidoAux = null;

        if(cursor.getCount() > 0){
            cursor.moveToFirst();

            pedidoAux = new Pedido();
            pedidoAux.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Esquema.Pedido._ID)));
            pedidoAux.setComanda(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.Pedido.COMANDA)));
            pedidoAux.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.Pedido.ESTADO)));
            pedidoAux.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow(Esquema.Pedido.VALOR)));
            pedidoAux.setFormaPagamento(cursor.getInt(cursor.getColumnIndexOrThrow(Esquema.Pedido.FORMA_PAGAMENTO)));
            pedidoAux.setData(cursor.getString(cursor.getColumnIndexOrThrow(Esquema.Pedido.DATA)));
            pedidoAux.setHora(cursor.getString(cursor.getColumnIndexOrThrow(Esquema.Pedido.HORA)));
            pedidoAux.setCaixa(caixa);
        }
        cursor.close();

        return pedidoAux;
    }
}
