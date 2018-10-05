package com.example.android.kanaking;

public interface Constantes {

    // Tipos de mensagens enviados do Handler do BluetoothChatService
    public static final int MENSAGEM_MUDANCA_ESTADO = 1;
    public static final int MENSAGEM_LER = 2;
    public static final int MENSAGEM_ESCREVER = 3;
    public static final int MENSAGEM_NOME_DISPOSITIVO = 4;
    public static final int MENSAGEM_TOAST = 5;

    // Nomes chave recebidos do Handler do BluetoothChatService
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    public static final String CAIXA = "CAIXA";
    public static final String MOENDA = "MOENDA";

    // Estados de um pedido ou controle de Vendas
    public static final int ABRINDO_CAIXA = 1;
    public static final int FECHANDO_CAIXA = 2;
    public static final int LANCADO = 3;
    public static final int PREPARANDO = 4;
    public static final int PRONTO = 5;
    public static final int CANCELADO = 6;
    public static final int TERMINADO = 7;

    public static final String COMANDA = "comanda";
    public static final String VALOR = "valor";
    public static final String PAGAMENTO = "pagamento";

    //Formas de pagamento
    public static final int CARTAO = 1;
    public static final int DINHEIRO = 0;

    //Etapas dos itens do pedido
    public static final int SABOR = 0;
    public static final int RECIPIENTE = 1;
    public static final int CONCLUIR = 2;
    public static final int QUANTIDADE = 3;
    public static final int OBSERVACAO = 4;

    //Sabor de Itens de Pedido
    public static final int SICILIANO = 1;
    public static final int TAITI = 2;
    public static final int ABACAXI = 3;
    public static final int PURO = 4;
    public static final int GENGIBRE = 5;
    public static final int COCO = 6;
    public static final int CANCELAR = 7;

    //Recipiente de Itens do Pedido
    public static final int COCO_FRUTA = 1;
    public static final int COPO_300 = 2;
    public static final int COPO_400 = 3;
    public static final int COPO_500 = 4;
    public static final int GARRAFA_500 = 5;
    public static final int GARRAFA_1000 = 6;

    //Observações de Itens do Pedido
    public static final int NENHUMA = 0;
    public static final int SEM_GELO = 1;
    public static final int POUCO_GELO = 2;

    //PREÇOS
    public static final Double PRECOS [] =
        //COCO_FRUTA, COPO_300, COPO_400, COPO_500, GARRAFA_500, GARRAFA_1000
    {0.0, 6.0,        4.0,      5.0,      6.0,      7.0,         12.0};
}
