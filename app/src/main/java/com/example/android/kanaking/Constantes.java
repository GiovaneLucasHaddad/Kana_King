package com.example.android.kanaking;

public interface Constantes {

    // Tipos de mensagens enviados do Handler do BluetoothChatService
    int MENSAGEM_MUDANCA_ESTADO = 1;
    int MENSAGEM_LER = 2;
    int MENSAGEM_ESCREVER = 3;
    int MENSAGEM_NOME_DISPOSITIVO = 4;
    int MENSAGEM_TOAST = 5;

    // Nomes chave recebidos do Handler do BluetoothChatService
    String DEVICE_NAME = "device_name";
    String TOAST = "toast";

    String CAIXA = "CAIXA";
    String MOENDA = "MOENDA";
    String AUXILIAR = "AUXILIAR";

    // Estados de um pedido ou controle de Vendas
    int ABRINDO_CAIXA = 1;
    int FECHANDO_CAIXA = 2;
    int LANCADO = 3;
    int PREPARANDO = 4;
    int PRONTO = 5;
    int CANCELADO = 6;
    int TERMINADO = 7;
    int APAGANDO = 8;
    int ENTREGANDO_ITEM = 9;
    int REABRINDO_CAIXA = 10;//TODO - Ao gerar nova versão do banco, colocar reabrindo caixa próximo de abrindo/fechando

    // Campos titulo para o JSON
    //Pedido
    String P_VENDA = "P_VENDA";
    String P_COMANDA = "P_COMANDA";
    String P_ESTADO = "P_ESTADO";
    String P_VALOR = "P_VALOR";
    String P_PAGTO = "P_PAGTO";
    String P_DATA = "P_DATA";
    String P_HORA = "P_HORA";
    String P_CAIXA_NUM = "P_C_NUM";
    //ItemPedido
    String ITEM_PEDIDOS = "ITEM_PEDIDOS";
    String IP_SEQUENCIA = "IP_SEQ";
    String IP_SABOR = "IP_SABOR";
    String IP_RECIP = "IP_RECIP";
    String IP_QTD = "IP_QTD";
    String IP_ENTREGUE = "IP_ENTREGUE";
    String IP_OBS = "IP_OBS";
    String IP_PEDIDO_VENDA = "IP_P_VENDA";

    //Formas de pagamento
    int CARTAO = 1;
    int DINHEIRO = 0;

    //Etapas dos itens do pedido
    int SABOR = 0;
    int RECIPIENTE = 1;
    int CONCLUIR = 2;
    int QUANTIDADE = 3;
    int OBSERVACAO = 4;

    //Sabor de Itens de Pedido
    int SICILIANO = 1;
    int TAITI = 2;
    int ABACAXI = 3;
    int PURO = 4;
    int GENGIBRE = 5;
    int COCO = 6;
    int CANCELAR = 7;

    //Recipiente de Itens do Pedido
    int COCO_FRUTA = 1;
    int COPO_300 = 2;
    int COPO_400 = 3;
    int COPO_500 = 4;
    int GARRAFA_500 = 5;
    int GARRAFA_1000 = 6;

    //Observações de Itens do Pedido
    int NENHUMA = 0;
    int SEM_GELO = 1;
    int POUCO_GELO = 2;

    //PREÇOS
    Double PRECOS [] =
        //COCO_FRUTA, COPO_300, COPO_400, COPO_500, GARRAFA_500, GARRAFA_1000
    {0.0, 6.0,        4.0,      5.0,      6.0,      7.0,         12.0};
    //QUANTIDADE (ML) RECIPIENTES
    int QTD_RECIPIENTE [] =
        //COCO_FRUTA, COPO_300, COPO_400, COPO_500, GARRAFA_500, GARRAFA_1000
    {0, 0,            300,      400,      500,      500,         1000};

    //Útil para entregue
    int SIM = 1;
    int NAO = 0;
}
