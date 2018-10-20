package com.example.android.kanaking.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class BD extends SQLiteOpenHelper {
    public static final String NOME_BD = "KanaKing.db";
    public static final int VERSAO_BD = 2;

    public static final String CRIAR_TABELA_ITEM_PEDIDO =
            "CREATE TABLE " + Esquema.ItemPedido.TABELA + " ("
                    + Esquema.ItemPedido._ID + " INTEGER PRIMARY KEY,"
                    + Esquema.ItemPedido.SEQUENCIA + " INTEGER,"
                    + Esquema.ItemPedido.VENDA + " INTEGER,"
                    + Esquema.ItemPedido.CAIXA + " INTEGER,"
                    + Esquema.ItemPedido.SABOR + " INTEGER,"
                    + Esquema.ItemPedido.RECIPIENTE + " INTEGER,"
                    + Esquema.ItemPedido.QUANTIDADE + " INTEGER,"
                    + Esquema.ItemPedido.ENTREGUE + " INTEGER,"
                    + Esquema.ItemPedido.OBSERVACAO + " INTEGER)";

    public static final String CRIAR_TABELA_PEDIDO =
            "CREATE TABLE " + Esquema.Pedido.TABELA + " ("
                    + Esquema.Pedido._ID + " INTEGER PRIMARY KEY,"
                    + Esquema.Pedido.VENDA + " INTEGER,"
                    + Esquema.Pedido.CAIXA + " INTEGER,"
                    + Esquema.Pedido.COMANDA + " INTEGER,"
                    + Esquema.Pedido.ESTADO + " INTEGER,"
                    + Esquema.Pedido.VALOR + " REAL,"
                    + Esquema.Pedido.FORMA_PAGAMENTO + " INTEGER,"
                    + Esquema.Pedido.DATA + " TEXT,"
                    + Esquema.Pedido.HORA + " TEXT)";

    public static final String CRIAR_TABELA_CAIXA =
            "CREATE TABLE " + Esquema.Caixa.TABELA + " ("
                    + Esquema.Caixa._ID + " INTEGER PRIMARY KEY,"
                    + Esquema.Caixa.NUMERO + " INTEGER,"
                    + Esquema.Caixa.DATA_ABERTURA + " TEXT,"
                    + Esquema.Caixa.HORA_ABERTURA + " TEXT,"
                    + Esquema.Caixa.DATA_FECHAMENTO + " TEXT,"
                    + Esquema.Caixa.HORA_FECHAMENTO + " TEXT,"
                    + Esquema.Caixa.FUNDO + " REAL)";

    public static final String CRIAR_TABELA_CONFIGURACOES =
            "CREATE TABLE " + Esquema.Configuracoes.TABELA + " ("
                    + Esquema.Configuracoes._ID + " INTEGER PRIMARY KEY,"
                    + Esquema.Configuracoes.MAC1 + " TEXT DEFAULT '0',"
                    + Esquema.Configuracoes.MAC1 + " TEXT DEFAULT '0')";

    public static final String DELETAR_TABELA_ITEM_PEDIDO = "DROP TABLE IF EXISTS " + Esquema.ItemPedido.TABELA;
    public static final String DELETAR_TABELA_PEDIDO = "DROP TABLE IF EXISTS " + Esquema.Pedido.TABELA;
    public static final String DELETAR_TABELA_CAIXA = "DROP TABLE IF EXISTS " + Esquema.Caixa.TABELA;
    public static final String DELETAR_TABELA_CONFIGURACOES = "DROP TABLE IF EXISTS " + Esquema.Configuracoes.TABELA;

    public static final String INICIAR_TABELA_CONFIGURACOES = "INSERT INTO " + Esquema.Configuracoes.TABELA + " DEFAULT VALUES";

    public BD(Context context){
        super(context, NOME_BD, null, VERSAO_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CRIAR_TABELA_ITEM_PEDIDO);
        db.execSQL(CRIAR_TABELA_PEDIDO);
        db.execSQL(CRIAR_TABELA_CAIXA);
        db.execSQL(CRIAR_TABELA_CONFIGURACOES);

//        db.execSQL(INICIAR_TABELA_CONFIGURACOES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETAR_TABELA_ITEM_PEDIDO);
        db.execSQL(DELETAR_TABELA_PEDIDO);
        db.execSQL(DELETAR_TABELA_CAIXA);
        db.execSQL(DELETAR_TABELA_CONFIGURACOES);
        onCreate(db);
    }
}
