package com.example.android.kanaking.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BD extends SQLiteOpenHelper {
    public static final String NOME_BD = "KanaKing.db";
    public static final int VERSAO_BD = 1;

    public static final String CRIAR_TABELA_ITEM_PEDIDO =
            "CREATE TABLE " + Esquema.ItemPedido.TABELA + " ("
                    + Esquema.ItemPedido._ID + " INTEGER PRIMARY KEY,"
                    + Esquema.ItemPedido.SEQUENCIA + " INTEGER,"
                    + Esquema.ItemPedido.VENDA_PEDIDO + " INTEGER,"
                    + Esquema.ItemPedido.NUM_CAIXA + " INTEGER,"
                    + Esquema.ItemPedido.SABOR + " INTEGER,"
                    + Esquema.ItemPedido.RECIPIENTE + " INTEGER,"
                    + Esquema.ItemPedido.QUANTIDADE + " INTEGER,"
                    + Esquema.ItemPedido.ENTREGUE + " INTEGER,"
                    + Esquema.ItemPedido.OBSERVACAO + " INTEGER)";

    public static final String CRIAR_TABELA_PEDIDO =
            "CREATE TABLE " + Esquema.Pedido.TABELA + " ("
                    + Esquema.Pedido._ID + " INTEGER PRIMARY KEY,"
                    + Esquema.Pedido.VENDA + " INTEGER,"
                    + Esquema.Pedido.NUM_CAIXA + " INTEGER,"
                    + Esquema.Pedido.COMANDA + " INTEGER,"
                    + Esquema.Pedido.ESTADO + " INTEGER,"
                    + Esquema.Pedido.VALOR + " REAL,"
                    + Esquema.Pedido.FORMA_PAGAMENTO + " INTEGER,"
                    + Esquema.Pedido.DATA + " INTEGER,"
                    + Esquema.Pedido.HORA + " INTEGER)";

    public static final String CRIAR_TABELA_CAIXA =
            "CREATE TABLE " + Esquema.Caixa.TABELA + " ("
                    + Esquema.Caixa._ID + " INTEGER PRIMARY KEY,"
                    + Esquema.Caixa.NUMERO + " INTEGER,"
                    + Esquema.Caixa.DATA_ABERTURA + " INTEGER,"
                    + Esquema.Caixa.HORA_ABERTURA + " INTEGER,"
                    + Esquema.Caixa.DATA_FECHAMENTO + " INTEGER,"
                    + Esquema.Caixa.HORA_FECHAMENTO + " INTEGER)";

    public static final String DELETAR_TABELA_ITEM_PEDIDO = "DROP TABLE IF EXISTS " + Esquema.ItemPedido.TABELA;
    public static final String DELETAR_TABELA_PEDIDO = "DROP TABLE IF EXISTS " + Esquema.Pedido.TABELA;
    public static final String DELETAR_TABELA_CAIXA = "DROP TABLE IF EXISTS " + Esquema.Caixa.TABELA;

    public BD(Context context){
        super(context, NOME_BD, null, VERSAO_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CRIAR_TABELA_ITEM_PEDIDO);
        db.execSQL(CRIAR_TABELA_PEDIDO);
        db.execSQL(CRIAR_TABELA_CAIXA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETAR_TABELA_ITEM_PEDIDO);
        db.execSQL(DELETAR_TABELA_PEDIDO);
        db.execSQL(DELETAR_TABELA_CAIXA);
        onCreate(db);
    }
}
