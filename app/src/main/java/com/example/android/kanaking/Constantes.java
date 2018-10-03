package com.example.android.kanaking;

public interface Constantes {

    public static final String CAIXA = "CAIXA";
    public static final String MOENDA = "MOENDA";

    // Estados de um pedido ou controle de Vendas
    public static final int ABRINDO_CAIXA = 0;
    public static final int FECHANDO_CAIXA = 1;
    public static final int LANCADO = 2;
    public static final int PREPARANDO = 3;
    public static final int PRONTO = 4;
    public static final int CANCELADO = 5;
    public static final int TERMINADO = 6;

    public static final String COMANDA = "comanda";
    public static final String VALOR = "valor";
    public static final String PAGAMENTO = "pagamento";

    //Formas de pagamento
    public static final int CARTAO = 1;
    public static final int DINHEIRO = 0;

    //Etapas dos itens do pedido
    public static final int SABOR = 0;
    public static final int RECIPIENTE = 1;
    public static final int QUANTIDADE = 2;

    //Sabor de Itens de Pedido
    public static final int SICILIANO = 0;
    public static final int TAITI = 1;
    public static final int ABACAXI = 2;
    public static final int PURO = 3;
    public static final int GENGIBRE = 4;
    public static final int COCO = 5;
    public static final int CANCELAR = 6;

    //Recipiente de Itens do Pedido
    public static final int COCO_FRUTA = 0;
    public static final int COPO_300 = 1;
    public static final int COPO_400 = 2;
    public static final int COPO_500 = 3;
    public static final int GARRAFA_500 = 4;
    public static final int GARRAFA_1000 = 5;
}
