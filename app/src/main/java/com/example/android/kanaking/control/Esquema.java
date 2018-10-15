package com.example.android.kanaking.control;

import android.provider.BaseColumns;

public final class Esquema {
    private Esquema(){}

    public static class Caixa implements BaseColumns {
        public static final String TABELA = "CAIXA";
        public static final String NUMERO = "NUMERO";
        public static final String DATA_ABERTURA = "DATA_ABERTURA";
        public static final String HORA_ABERTURA = "HORA_ABERTURA";
        public static final String DATA_FECHAMENTO = "DATA_FECHAMENTO";
        public static final String HORA_FECHAMENTO = "HORA_FECHAMENTO";
        public static final String FUNDO = "FUNDO";
    }

    public static class Pedido implements BaseColumns {
        public static final String TABELA = "PEDIDO";
        public static final String VENDA = "VENDA";
        public static final String CAIXA = "CAIXA";
        public static final String COMANDA = "COMANDA";
        public static final String ESTADO = "ESTADO";
        public static final String VALOR = "VALOR";
        public static final String FORMA_PAGAMENTO = "FORMA_PAGAMENTO";
        public static final String DATA = "DATA";
        public static final String HORA = "HORA";
    }

    public static class ItemPedido implements BaseColumns {
        public static final String TABELA = "ITEM_PEDIDO";
        public static final String SEQUENCIA = "SEQUENCIA";
        public static final String VENDA = "VENDA";
        public static final String CAIXA = "CAIXA";
        public static final String SABOR = "SABOR";
        public static final String RECIPIENTE = "RECIPIENTE";
        public static final String QUANTIDADE = "QUANTIDADE";
        public static final String ENTREGUE = "ENTREGUE";
        public static final String OBSERVACAO = "OBSERVACAO";
    }
}
