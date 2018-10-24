package com.example.android.kanaking.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.kanaking.ItemPedidoAdapter;
import com.example.android.kanaking.Pagamento;
import com.example.android.kanaking.PagamentoAdapter;
import com.example.android.kanaking.PedidoAdapter;
import com.example.android.kanaking.R;
import com.example.android.kanaking.control.BD;
import com.example.android.kanaking.control.BluetoothService;
import com.example.android.kanaking.control.DaoCaixa;
import com.example.android.kanaking.control.DaoItemPedido;
import com.example.android.kanaking.control.DaoPedido;
import com.example.android.kanaking.model.Caixa;
import com.example.android.kanaking.model.ItemPedido;
import com.example.android.kanaking.model.Pedido;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.android.kanaking.Constantes.ABACAXI;
import static com.example.android.kanaking.Constantes.ABRINDO_CAIXA;
import static com.example.android.kanaking.Constantes.APAGANDO;
import static com.example.android.kanaking.Constantes.CAIXA;
import static com.example.android.kanaking.Constantes.CANCELADO;
import static com.example.android.kanaking.Constantes.CANCELAR;
import static com.example.android.kanaking.Constantes.COCO;
import static com.example.android.kanaking.Constantes.CONCLUIR;
import static com.example.android.kanaking.Constantes.COPO_300;
import static com.example.android.kanaking.Constantes.COPO_400;
import static com.example.android.kanaking.Constantes.COPO_500;
import static com.example.android.kanaking.Constantes.DEVICE_NAME;
import static com.example.android.kanaking.Constantes.DINHEIRO;
import static com.example.android.kanaking.Constantes.ENTREGANDO_ITEM;
import static com.example.android.kanaking.Constantes.FECHANDO_CAIXA;
import static com.example.android.kanaking.Constantes.GARRAFA_1000;
import static com.example.android.kanaking.Constantes.GARRAFA_500;
import static com.example.android.kanaking.Constantes.GENGIBRE;
import static com.example.android.kanaking.Constantes.IP_ENTREGUE;
import static com.example.android.kanaking.Constantes.IP_OBS;
import static com.example.android.kanaking.Constantes.IP_QTD;
import static com.example.android.kanaking.Constantes.IP_RECIP;
import static com.example.android.kanaking.Constantes.IP_SABOR;
import static com.example.android.kanaking.Constantes.IP_SEQUENCIA;
import static com.example.android.kanaking.Constantes.ITEM_PEDIDOS;
import static com.example.android.kanaking.Constantes.LANCADO;
import static com.example.android.kanaking.Constantes.MENSAGEM_ESCREVER;
import static com.example.android.kanaking.Constantes.MENSAGEM_LER;
import static com.example.android.kanaking.Constantes.MENSAGEM_MUDANCA_ESTADO;
import static com.example.android.kanaking.Constantes.MENSAGEM_NOME_DISPOSITIVO;
import static com.example.android.kanaking.Constantes.MENSAGEM_TOAST;
import static com.example.android.kanaking.Constantes.MOENDA;
import static com.example.android.kanaking.Constantes.NAO;
import static com.example.android.kanaking.Constantes.OBSERVACAO;
import static com.example.android.kanaking.Constantes.PREPARANDO;
import static com.example.android.kanaking.Constantes.PRONTO;
import static com.example.android.kanaking.Constantes.PURO;
import static com.example.android.kanaking.Constantes.P_COMANDA;
import static com.example.android.kanaking.Constantes.P_DATA;
import static com.example.android.kanaking.Constantes.P_ESTADO;
import static com.example.android.kanaking.Constantes.P_HORA;
import static com.example.android.kanaking.Constantes.P_PAGTO;
import static com.example.android.kanaking.Constantes.P_VALOR;
import static com.example.android.kanaking.Constantes.P_VENDA;
import static com.example.android.kanaking.Constantes.QUANTIDADE;
import static com.example.android.kanaking.Constantes.REABRINDO_CAIXA;
import static com.example.android.kanaking.Constantes.RECIPIENTE;
import static com.example.android.kanaking.Constantes.SABOR;
import static com.example.android.kanaking.Constantes.SICILIANO;
import static com.example.android.kanaking.Constantes.SIM;
import static com.example.android.kanaking.Constantes.TAITI;
import static com.example.android.kanaking.Constantes.TERMINADO;
import static com.example.android.kanaking.Constantes.TOAST;

public class Vendas extends AppCompatActivity{

    public static String MODO;

    //Relacionado ao Bluetooth
    private static final int SOLICITACAO_CONEXAO_SEGURA = 1;
    private static final int SOLICITACAO_CONEXAO_INSEGURA = 2;
    private static final int SOLICITACAO_ATIVACAO = 3;
    private static final int DIALOGO_DATA = 4;
    private static final int INICIAL = 1;
    private static final int FINAL = 2;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothService servicoBluetooth;
    private StringBuffer bufferSaida;
    private String nomeDispositivoConectado = null;

    private NumberPicker comanda;
    private ListView listaPedidos;
    private FrameLayout bloqueio;
    private PedidoAdapter pedidoAdapter;
    private ArrayList<Pedido> pedidosList;
    private Spinner pagamento;
    private ItemPedidoAdapter itemAdapter;
    private ArrayList<ItemPedido> itensList;
    private TextView textValor;
    private LinearLayout barraEstado;
    private TextView estado;

    //Controle de soma dos sabores
    private TextView somaTaiti;
    private int totalTaiti = 0;
    private TextView somaSiciliano;
    private int totalSiciliano = 0;
    private TextView somaAbacaxi;
    private int totalAbacaxi = 0;
    private TextView somaPuro;
    private int totalPuro = 0;
    private TextView somaGengibre;
    private int totalGengibre = 0;

    //Controle de objetos
    private Caixa caixa;
    private ItemPedido itemAux;
    private int etapa = 0;
    private Double soma = 0.0;
    private long numCaixa = 1;
    private long numPedido = 1;
    private int numItemPedido = 1;
    private int tipoData;
    private String dataInicial= "";
    private String dataFinal = "";
    private boolean mostraTodos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendas);

        Intent intent = getIntent();
        MODO = intent.getStringExtra("MODO");

        textValor = (EditText)findViewById(R.id.add_valor);
        barraEstado = findViewById(R.id.fundo_estado);
        estado = findViewById(R.id.estado);
        listaPedidos = findViewById(R.id.lista_pedidos);
        bloqueio = findViewById(R.id.bloqueio);

        //Configurando abertura de caixa
        DaoCaixa daoCaixa = new DaoCaixa(this);
        long id = daoCaixa.ultimoId();
//        Toast.makeText(this, "Ultimo ID: " + id, Toast.LENGTH_SHORT).show();
        caixa = daoCaixa.ultimoCaixa(id);
        if(caixa != null){
            numCaixa = caixa.getNumero() + 1;
            if (caixa.isAberto()){
//                Toast.makeText(Vendas.this, "Caixa: DataAbertura: " + caixa.getDataAbertura() + " HoraAbertura: " + caixa.getHoraAbertura() + " DataFechamento: " + caixa.getDataFechamento() + " HoraFechamento: " + caixa.getHoraFechamento() + " Fundo: " + caixa.getFundo(), Toast.LENGTH_SHORT).show();
                listaPedidos.setEnabled(true);
                bloqueio.setVisibility(View.INVISIBLE);
            }else{
                listaPedidos.setEnabled(false);
                bloqueio.setVisibility(View.VISIBLE);
            }
        }else{//Primeiro Caixa
            listaPedidos.setEnabled(false);
            bloqueio.setVisibility(View.VISIBLE);
        }

        //Preencher com as últimas vendas salvas
        DaoPedido daoPedido = new DaoPedido(this);
        pedidosList = daoPedido.buscarPedidos(caixa);

        if (pedidosList != null) {
            DaoItemPedido daoItemPedido = new DaoItemPedido(this);
            for(int cont = 0; cont < pedidosList.size(); cont++){
                daoItemPedido.buscarItemPedidos(pedidosList.get(cont));
            }
            numPedido = pedidosList.get(pedidosList.size()-1).getVenda() + 1;
        }else{
            pedidosList = new ArrayList<>();
        }

        //Configurando o Lista de Pedidos
        if (pedidoAdapter == null){
        pedidoAdapter = new PedidoAdapter(this,pedidosList);
        listaPedidos.setAdapter(pedidoAdapter);
        }

        listarPedidos();

        //Particularidades de usuários
        if(MODO.equals(CAIXA)) {
            LinearLayout barraContagem = findViewById(R.id.barra_contagem);
            barraContagem.setVisibility(View.GONE);

            //Configurando NumberPicker
            comanda = findViewById(R.id.add_comanda);
            comanda.setMaxValue(20);
            comanda.setMinValue(1);

            //Configurando Spinner Forma de pagamento
            ArrayList<Pagamento> listaPagto = new ArrayList<>();
            listaPagto.add(new Pagamento(R.drawable.dinheiro));
            listaPagto.add(new Pagamento(R.drawable.cartao));

            pagamento = findViewById(R.id.add_pagamento);
            PagamentoAdapter pagtoAdapter = new PagamentoAdapter(this, R.layout.lista_imagem, listaPagto);
            pagamento.setAdapter(pagtoAdapter);

            //Configurando GridView onde aparecerão os itens do pedido a lançar
            GridView itemGrid = findViewById(R.id.add_itens);
            if (itensList == null) {
                itensList = new ArrayList<>();
            }

            if (itemAdapter == null) {
                itemAdapter = new ItemPedidoAdapter(this, itensList);
                itemGrid.setAdapter(itemAdapter);
            } else {
                itemAdapter.notifyDataSetChanged();
            }

        }else if(MODO.equals(MOENDA)){
            LinearLayout barraLancamento = findViewById(R.id.barra_lancamento);
            barraLancamento.setVisibility(View.GONE);
            somaTaiti = findViewById(R.id.soma_taiti);
            somaSiciliano= findViewById(R.id.soma_siciliano);
            somaAbacaxi= findViewById(R.id.soma_abacaxi);
            somaPuro= findViewById(R.id.soma_puro);
            somaGengibre= findViewById(R.id.soma_gengibre);
            for (int cont = 0; cont < pedidosList.size(); cont++) {
                if(pedidosList.get(cont).consideraPedido()){
                    calcSomaItens(pedidosList.get(cont));
                }
            }
            mostrarSomaItens();
        }

        //Obtendo o adaptador Bluetooth e verificando se é suportado
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(Vendas.this, "Bluetooth não suportado!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Verificando e solicitando ativação do Bluetooth
        if(!bluetoothAdapter.isEnabled()){
            Intent solicitaAtivacao = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(solicitaAtivacao,SOLICITACAO_ATIVACAO);

        }else if (servicoBluetooth == null){
            configuraTransmissao();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (servicoBluetooth != null){
            servicoBluetooth.stop();
        }
        BD auxBD = new BD(this);
        auxBD.close();
        Toast.makeText(this,"Banco fechado...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Realizando essa verificação abrange-se o caso do Bluetooth estar desabilitado
        // durante o onStart(), então a tela está pausada para habilitá-lo...
        // onResume() será chamado quando a atividade ACTION_REQUEST_ENABLE retornar.
        if (servicoBluetooth != null) {
            // Somente se o estado for ESTADO_NENHUM, sabemos se ainda não iniciamos
            if (servicoBluetooth.getState() == BluetoothService.ESTADO_NENHUM) {
                // Iniciar o serviço BluetothService
                servicoBluetooth.start();
            }
        }
    }

    public void listarPedidos(){
        if(mostraTodos){//Mostrar Todos
            DaoPedido daoPedido = new DaoPedido(this);
            pedidosList = new ArrayList<>();
            pedidosList = daoPedido.buscarPedidos(caixa);

            pedidoAdapter = new PedidoAdapter(this,pedidosList);
            listaPedidos.invalidateViews();
            listaPedidos.setAdapter(pedidoAdapter);
            if (pedidosList != null) {
//                Toast.makeText(this, "pedidosList != null size()" + pedidosList.size(), Toast.LENGTH_SHORT).show();
                DaoItemPedido daoItemPedido = new DaoItemPedido(this);
                for(int cont = 0; cont < pedidosList.size(); cont++){
                    daoItemPedido.buscarItemPedidos(pedidosList.get(cont));
                }
            }else{
//                Toast.makeText(this, "pedidosList null", Toast.LENGTH_SHORT).show();
                pedidosList = new ArrayList<>();
            }
            pedidoAdapter.notifyDataSetChanged();
        }else{//Esconder Camcelados e Terminados
            if(pedidosList != null) {
                for (int cont = 0; cont < pedidosList.size(); cont++) {
                    Pedido pedido = pedidosList.get(cont);
                    if (!pedido.consideraPedido()) {
                        pedidosList.remove(cont);
                        cont--;//A cada Pedido removido da lista, os seguintes voltam uma posição
                    }
                }
                pedidoAdapter.notifyDataSetChanged();
            }else{
                pedidosList = new ArrayList<>();
            }
        }
    }

    private void configuraTransmissao(){
        //Inicializar serviço para conexões Bluetooth
        servicoBluetooth = new BluetoothService(handler);

        //Inicializar buffer de saída
        bufferSaida = new StringBuffer("");
    }

    public void adicionar(View view){
        if(isConectado()) {
            if (caixa != null){
                if (caixa.isAberto()) {//caixa aberto
                    Double valor = Double.valueOf(textValor.getText().toString().replace(",", "."));
                    if (valor > 0.0) {
                        numItemPedido = 1;
                        //Formatação da data
                        SimpleDateFormat formatData = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm:ss");

                        Date data = new Date();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(data);
                        Date data_atual = cal.getTime();
                        Pedido pedido = new Pedido(0, numPedido, comanda.getValue(), LANCADO, valor, pagamento.getSelectedItemPosition(), formatData.format(data_atual), formatHora.format(data_atual), itensList);

                        numPedido++;
                        enviar(PedidoToStringJSON(pedido));
                    } else {
                        Toast.makeText(this, "Pedido sem preço", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Caixa fechado", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Caixa fechado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void adicionarItem(final View v){//Disparado pelo botão "+" de ItemPedidos
        PopupMenu popup = new PopupMenu(this, v);

        //Código copiado de https://readyandroid.wordpress.com/popup-menu-with-icon/
        //para forçar que o ícone seja mostrado
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Até aqui (Código copiado)
        switch(etapa){
            case SABOR:
                itemAux = new ItemPedido();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int escolha = itemAux.getConstSabor(item.getItemId());
                        if(escolha != CANCELAR) {
                            itemAux.setSabor(escolha);
                            etapa = RECIPIENTE;
                            adicionarItem(v);
                        }
                        return true;
                    }
                });
                popup.inflate(R.menu.menu_sabor_item);
                popup.show();
                break;

            case RECIPIENTE:
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int escolha = itemAux.getConstRecipiente(item.getItemId());
                        if(escolha != CANCELAR) {
                            itemAux.setRecipiente(escolha);
                            etapa = CONCLUIR;
                            adicionarItem(v);
                        }else{
                            etapa = SABOR;
                        }
                        return true;
                    }
                });

                if(itemAux.getSabor() == COCO){
                    popup.inflate(R.menu.menu_recipiente_coco_item);

                }else{//Sabores de caldo de cana
                    popup.inflate(R.menu.menu_recipiente_sabor_item);
                    Menu menu = popup.getMenu();
                    MenuItem menuItem;

                    menuItem = menu.findItem(R.id.c_300);
                    menuItem.setIcon(itemAux.getIconeSabor(COPO_300));

                    menuItem = menu.findItem(R.id.c_400);
                    menuItem.setIcon(itemAux.getIconeSabor(COPO_400));

                    menuItem = menu.findItem(R.id.c_500);
                    menuItem.setIcon(itemAux.getIconeSabor(COPO_500));

                    menuItem = menu.findItem(R.id.g_500);
                    menuItem.setIcon(itemAux.getIconeSabor(GARRAFA_500));

                    menuItem = menu.findItem(R.id.g_1000);
                    menuItem.setIcon(itemAux.getIconeSabor(GARRAFA_1000));

                }
                popup.show();
                break;

            case CONCLUIR:
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.qtde:
                                etapa = QUANTIDADE;
                                adicionarItem(v);
                                break;
                            case R.id.obs:
                                etapa = OBSERVACAO;
                                adicionarItem(v);
                                break;
                            case R.id.confirmar:
                                etapa = SABOR;

                                //Calculando e mostrando a soma dos preços
                                soma += itemAux.calcSoma();
                                DecimalFormat df = new DecimalFormat(",##0.00");
                                textValor.setText(df.format(soma));

                                itemAux.setSequencia(numItemPedido);
                                numItemPedido++;
                                ItemPedido itemPedido = new ItemPedido(itemAux);
                                itensList.add(itemPedido);
                                itemAdapter.notifyDataSetChanged();

                                break;
                            case R.id.cancelar:
                                etapa = SABOR;//Retorna à 1a etapa
                                return true;
                        }
                        return true;
                    }
                });
                popup.inflate(R.menu.menu_finalizar_item);
                Menu menu = popup.getMenu();
                MenuItem menuItem;

                menuItem = menu.findItem(R.id.qtde);
                menuItem.setTitle("Quantidade: " + itemAux.getQuantidade());

                menuItem = menu.findItem(R.id.obs);
                menuItem.setTitle("Obs: " + itemAux.getObservacaoDescricao());

                popup.show();
                break;

            case QUANTIDADE:
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        itemAux.setQuantidade(itemAux.getQtdQuantidadeId(item.getItemId()));
                        etapa = CONCLUIR;
                        adicionarItem(v);
                        return true;
                    }
                });
                popup.inflate(R.menu.menu_quantidade_item);
                popup.show();

                break;

            case OBSERVACAO:
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        itemAux.setObservacao(itemAux.getObsObservacaoId(item.getItemId()));
                        etapa = CONCLUIR;
                        adicionarItem(v);
                        return true;
                    }
                });
                popup.inflate(R.menu.menu_observacao_item);
                popup.show();

                break;
        }

    }

    public void zerar(View view){
        zerar();
    }

    public void zerar(){
        TextView valor;
        valor = (EditText)findViewById(R.id.add_valor);

        itensList.clear();
        soma = 0.0;
        DecimalFormat df = new DecimalFormat(",##0.00");
        valor.setText(df.format(0));

        pagamento.setSelection(0);
    }

    private void enviar(String conteudo){
        if(isConectado()) {
            // Verificando se tem algo para enviar
            if (conteudo.length() > 0) {
                // Obter os bytes do conteudo e enviar pelo BluetoothService
                byte[] enviar = conteudo.getBytes();
                servicoBluetooth.write(enviar);

                // Zerar buffer de saída
                bufferSaida.setLength(0);
            }
        }
    }

    private boolean isConectado(){
        //Verificar se estamos conectados antes de tentar algo
        if (servicoBluetooth.getState() != BluetoothService.ESTADO_CONECTADO) {
            Toast.makeText(this, R.string.nao_conectado, Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    private void setStatus(int resId) {
        estado.setText(resId);

        switch(resId){
            case R.string.title_connecting:
                barraEstado.setBackgroundResource(R.color.corConectando);
                break;
            case R.string.title_listening:
                barraEstado.setBackgroundResource(R.color.corOuvindo);
                break;
            case R.string.title_not_connected:
                barraEstado.setBackgroundResource(R.color.corNaoConectado);
                break;
        }
    }

    private void setStatus(CharSequence subTitle) {
        //Não verificado pois só um estado chama esta função
        estado.setText(subTitle);
        barraEstado.setBackgroundResource(R.color.corConectado);
    }

    private final Handler handler = new Handler(){//Interação do BluetoothService com a UI
        @Override
        public void handleMessage(Message msg) {
            Activity activity = Vendas.this;
            switch (msg.what) {
                case MENSAGEM_MUDANCA_ESTADO: {
                    switch (msg.arg1) {
                        case BluetoothService.ESTADO_CONECTADO:
                            setStatus(getString(R.string.title_connected_to, nomeDispositivoConectado));
                            break;
                        case BluetoothService.ESTADO_CONECTANDO:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothService.ESTADO_OUVIR:
                            setStatus(R.string.title_listening);
                            break;
                        case BluetoothService.ESTADO_NENHUM:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                }
                case MENSAGEM_ESCREVER: {//Quem envia a mensagem
                    byte[] writeBuf = (byte[]) msg.obj;
                    // Construir uma string com o buffer
                    String writeMessage = new String(writeBuf);
                    Pedido pedido = JSONStringToPedido(writeMessage);
                    int estado = pedido.getEstado();
                    switch(estado){
                        case ABRINDO_CAIXA: {
                            DaoCaixa daoCaixa = new DaoCaixa(activity);
                            if (daoCaixa.abrirCaixa(caixa) == -1) {
                                Toast.makeText(activity, "Ocorreu algum erro ao abrir o caixa", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            numCaixa++;
                            pedidosList.clear();
                            pedidoAdapter.notifyDataSetChanged();
                            listaPedidos.setEnabled(true);
                            bloqueio.setVisibility(View.INVISIBLE);
                            invalidateOptionsMenu();
                            break;
                        }
                        case FECHANDO_CAIXA: {
                            DaoCaixa daoCaixa = new DaoCaixa(activity);
                            if (daoCaixa.fecharCaixa(caixa) < 1) {
                                Toast.makeText(activity, "Ocorreu algum erro ao fechar o caixa", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            listaPedidos.setEnabled(false);
                            bloqueio.setVisibility(View.VISIBLE);
                            invalidateOptionsMenu();
                            break;
                        }
                        case REABRINDO_CAIXA: {
                            DaoCaixa daoCaixa = new DaoCaixa(activity);
                            if (daoCaixa.reabrirCaixa(caixa) < 1) {
                                Toast.makeText(activity, "Ocorreu algum erro ao reabrir o caixa", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            listaPedidos.setEnabled(true);
                            bloqueio.setVisibility(View.INVISIBLE);
                            invalidateOptionsMenu();
                            break;
                        }
                        case LANCADO: {
                            DaoPedido daoPedido = new DaoPedido(activity);
                            long id = daoPedido.inserir(pedido);
                            if (id == -1) {
                                Toast.makeText(activity, "Ocorreu algum erro ao lançar o Pedido", Toast.LENGTH_SHORT).show();
//                                numPedido--;
                                break;
                            }
                            pedido.setId(id);

                            ArrayList<ItemPedido> itemPedidos = pedido.getItemPedidos();
                            DaoItemPedido daoItemPedido = new DaoItemPedido(activity);
                            for (int cont = 0; cont < itemPedidos.size(); cont++) {
                                id = daoItemPedido.inserir(itemPedidos.get(cont));
                                if (id == -1) {
                                    Toast.makeText(activity, "Ocorreu algum erro ao lançar Itens do Pedido", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                itemPedidos.get(cont).setId(id);
                            }

                            if (id == -1) {
                                Toast.makeText(activity, "Desculpe, tente novamente", Toast.LENGTH_SHORT).show();
                            }
                            pedidosList.add(pedido);
                            caixa.addPedido(pedido);
                            zerar();
                            pedidoAdapter.notifyDataSetChanged();
                            break;
                        }
                        case PREPARANDO:{
                            executarMudancaEstado(pedido);
                            break;
                        }
                        case PRONTO:{
                            executarMudancaEstado(pedido);
                            break;
                        }
                        case TERMINADO:{
                            entregarTodosItens(pedido);
                            executarMudancaEstado(pedido);
                            break;
                        }
                        case CANCELADO:{
                            executarMudancaEstado(pedido);
                            break;
                        }
                        case APAGANDO:{
                            remover(pedido);
                            break;
                        }
                        case ENTREGANDO_ITEM:{
                            entregarItem(pedido);
                        }
                    }
                    break;
                }
                case MENSAGEM_LER: {//Quem recebe a mensagem
                    byte[] readBuf = (byte[]) msg.obj;
                    // Construir um string com os bytes validos do buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    Pedido pedido = JSONStringToPedido(readMessage);
                    int estado = pedido.getEstado();
                    switch (estado) {
                        case ABRINDO_CAIXA: {
                            caixa = new Caixa();
                            caixa.setFundo(pedido.getValor());
                            caixa.setNumero(pedido.getVenda());
                            caixa.setDataAbertura(pedido.getData());
                            caixa.setHoraAbertura(pedido.getHora());

                            DaoCaixa daoCaixa = new DaoCaixa(activity);
                            if (daoCaixa.abrirCaixa(caixa) == -1) {
                                Toast.makeText(activity, "Ocorreu algum erro ao abrir o caixa", Toast.LENGTH_SHORT).show();
                                break;
                            }

                            numCaixa++;
                            pedidosList.clear();
                            pedidoAdapter.notifyDataSetChanged();
                            listaPedidos.setEnabled(true);
                            bloqueio.setVisibility(View.INVISIBLE);
                            invalidateOptionsMenu();
                            if (MODO.equals(MOENDA)) {
                                zerarSomaItens();
                                calcSomaItens(pedido);
                                mostrarSomaItens();
                            }
                            break;
                        }

                        case FECHANDO_CAIXA: {
                            caixa.setDataFechamento(pedido.getData());
                            caixa.setHoraFechamento(pedido.getHora());
                            DaoCaixa daoCaixa = new DaoCaixa(activity);
                            if (daoCaixa.fecharCaixa(caixa) < 1) {
                                Toast.makeText(activity, "Ocorreu algum erro ao fechar o caixa", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            listaPedidos.setEnabled(false);
                            bloqueio.setVisibility(View.VISIBLE);
                            invalidateOptionsMenu();
                            break;
                        }
                        case REABRINDO_CAIXA: {
                            caixa.setDataFechamento("");
                            caixa.setHoraFechamento("");
                            DaoCaixa daoCaixa = new DaoCaixa(activity);
                            if (daoCaixa.reabrirCaixa(caixa) < 1) {
                                Toast.makeText(activity, "Ocorreu algum erro ao reabrir o caixa", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            listaPedidos.setEnabled(true);
                            bloqueio.setVisibility(View.INVISIBLE);
                            invalidateOptionsMenu();
                            break;
                        }
                        case LANCADO: {
                            DaoPedido daoPedido = new DaoPedido(activity);
                            long id = daoPedido.inserir(pedido);
                            if (id == -1) {
                                Toast.makeText(activity, "Ocorreu algum erro ao lançar o Pedido", Toast.LENGTH_SHORT).show();
//                                numPedido--;
                                break;
                            }
                            pedido.setId(id);

                            ArrayList<ItemPedido> itemPedidos = pedido.getItemPedidos();
                            DaoItemPedido daoItemPedido = new DaoItemPedido(activity);
                            for (int cont = 0; cont < itemPedidos.size(); cont++) {
                                id = daoItemPedido.inserir(itemPedidos.get(cont));
                                if (id == -1) {
                                    Toast.makeText(activity, "Ocorreu algum erro ao lançar Itens do Pedido", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                itemPedidos.get(cont).setId(id);
                            }

                            if (id == -1) {
                                Toast.makeText(activity, "Desculpe, tente novamente", Toast.LENGTH_SHORT).show();
                            }
                            pedidosList.add(pedido);
                            caixa.addPedido(pedido);
                            if (MODO.equals(MOENDA)) {
                                calcSomaItens(pedido);
                                mostrarSomaItens();
                            }
                            pedidoAdapter.notifyDataSetChanged();
                            break;
                        }
                        case PREPARANDO:{
                            executarMudancaEstado(pedido);
                            break;
                        }
                        case PRONTO:{
                            executarMudancaEstado(pedido);
                            break;
                        }
                        case TERMINADO:{
                            entregarTodosItens(pedido);
                            executarMudancaEstado(pedido);
                            if (MODO.equals(MOENDA)) {
                                mostrarSomaItens();
                            }
                            break;
                        }
                        case CANCELADO:{
                            executarMudancaEstado(pedido);
                            if (MODO.equals(MOENDA)) {
                                subtraiSomaItens(pedido);
                                mostrarSomaItens();
                            }
                            break;
                        }
                        case APAGANDO:{
                            remover(pedido);
                            if (MODO.equals(MOENDA)) {
                                subtraiSomaItens(pedido);
                                mostrarSomaItens();
                            }
                            break;
                        }
                        case ENTREGANDO_ITEM:{
                            entregarItem(pedido);
                        }
                    }
                    break;
                }
                case MENSAGEM_NOME_DISPOSITIVO: {
                    // save the connected device's name
                    nomeDispositivoConectado = msg.getData().getString(DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Conectado com " + nomeDispositivoConectado, Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case MENSAGEM_TOAST: {
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };

    public void mudarEstado(View v){
        int estado = 0;
        Pedido pedidoAux = (Pedido) v.getTag();
        Pedido pedido = new Pedido(pedidoAux);

        if(MODO.equals(CAIXA)){
            switch(pedido.getEstado()){
                case LANCADO:
                    estado = TERMINADO;
                    break;
                case PREPARANDO:
                    estado = TERMINADO;
                    break;
                case PRONTO:
                    estado = TERMINADO;
                    break;
            }
        }else{
            switch(pedido.getEstado()){
                case LANCADO:
                    estado = PREPARANDO;
                    break;
                case PREPARANDO:
                    estado = PRONTO;
                    break;
            }
        }

        if(estado != 0) {
            pedido.setEstado(estado);
            enviar(PedidoToStringJSON(pedido));
        }
    }

    public void executarMudancaEstado(Pedido pedido){//Efetua persistência da mudança de Estado
        DaoPedido daoPedido = new DaoPedido(this);
        if(daoPedido.atualizarEstado(pedido) < 1){
            Toast.makeText(this, "Ocorreu algum erro ao alterar estado do Pedido", Toast.LENGTH_SHORT).show();
        }
        for(int cont = 0; cont < pedidosList.size(); cont++){
            Pedido pedidoAux = pedidosList.get(cont);
            if(pedidoAux.getVenda() == pedido.getVenda()){
                pedidoAux.setEstado(pedido.getEstado());
                if(!pedidoAux.consideraPedido()){
                    if(!mostraTodos){
                        pedidosList.remove(cont);
                    }
                }
                pedidoAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    public void selecaoItem(View v){
        if(MODO.equals(CAIXA)) {
            ItemPedido item = (ItemPedido) v.getTag();
            if (item.getId() == 0) {//Lançamento de pedido -> Remover da lista a ser lançada
                int seq = item.getSequencia();
                for (int cont = 0; cont < itensList.size(); cont++) {
                    if (itensList.get(cont).getSequencia() == seq) {
                        soma -= item.calcSoma();
                        DecimalFormat df = new DecimalFormat(",##0.00");
                        textValor.setText(df.format(soma));

                        itensList.remove(cont);
                        itemAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            } else {//ItemPedido de Pedido já lançado -> entregar ItemPedido
                if(item.getEntregue() == NAO) {
                    Pedido pedido = new Pedido();
                    pedido.setComanda(item.getSequencia());
                    pedido.setVenda(item.getPedido().getVenda());
                    pedido.setEstado(ENTREGANDO_ITEM);
                    enviar(PedidoToStringJSON(pedido));
                }
            }
        }
    }

    public void entregarItem(Pedido pedido){
        int cont;
        for(cont = 0; cont < pedidosList.size(); cont++){
            if(pedidosList.get(cont).getVenda() == pedido.getVenda()){
                break;
            }
        }
        if(cont == pedidosList.size()){
            Toast.makeText(this, "Pedido não encontrado", Toast.LENGTH_SHORT).show();
        }else{
            Pedido pedidoAux = pedidosList.get(cont);
            ArrayList<ItemPedido> itensAux = pedidoAux.getItemPedidos();
            for(cont = 0; cont < itensAux.size(); cont++){
                ItemPedido item = itensAux.get(cont);
                if(item.getSequencia() == pedido.getComanda()){
                    item.setEntregue(SIM);
                    if(MODO.equals(MOENDA)) {
                        subtraiSomaItem(item);
                        mostrarSomaItens();
                    }
                    executarEntregaItem(item);
                    if(pedidoAux.todosItensEntregues()){
                        pedidoAux.setEstado(TERMINADO);
                        executarMudancaEstado(pedidoAux);
                    }else {
                        pedidoAdapter.notifyDataSetChanged();
                    }
                    break;
                }
            }
        }
    }

    public void entregarTodosItens(Pedido pedido){
        int cont;
        for(cont = 0; cont < pedidosList.size(); cont++){
            if(pedidosList.get(cont).getVenda() == pedido.getVenda()){
                break;
            }
        }
        if(cont == pedidosList.size()){
            Toast.makeText(this, "Pedido não encontrado", Toast.LENGTH_SHORT).show();
        }else{
            Pedido pedidoAux = pedidosList.get(cont);
            ArrayList<ItemPedido> itensAux = pedidoAux.getItemPedidos();
            for(cont = 0; cont < itensAux.size(); cont++){
                ItemPedido item = itensAux.get(cont);
                item.setEntregue(SIM);
                if(MODO.equals(MOENDA)) {
                    subtraiSomaItem(item);
                    mostrarSomaItens();
                }
                executarEntregaItem(item);
            }
//            pedidoAdapter.notifyDataSetChanged();
        }
    }

    public void executarEntregaItem(ItemPedido itemPedido){//Efetua persistência da Entrega
        DaoItemPedido daoItemPedido = new DaoItemPedido(this);
        if(daoItemPedido.atualizarEstado(itemPedido) < 1){
            Toast.makeText(this, "Ocorreu algum erro ao alterar estado do ItemPedido", Toast.LENGTH_SHORT).show();
        }
    }

    public void opcoes(View v){
        Pedido pedidoAux = (Pedido) v.getTag();
        final Pedido pedido = new Pedido(pedidoAux);

        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.cancelar:
                        pedido.setEstado(CANCELADO);
                        enviar(PedidoToStringJSON(pedido));
                        return true;
                    case R.id.apagar:
                        pedido.setEstado(APAGANDO);
                        enviar(PedidoToStringJSON(pedido));
                        return true;
                }
                return false;
            }
        });
        popup.inflate(R.menu.menu_pedido);
        popup.show();

    }

    public void remover(Pedido pedido){
        //Remoção do banco
        DaoPedido daoPedido = new DaoPedido(this);
        if(daoPedido.remover(pedido) < 1){
            Toast.makeText(this, "Ocorreu algum erro ao remover o Pedido", Toast.LENGTH_SHORT).show();
        }
        ArrayList<ItemPedido> itens = pedido.getItemPedidos();
        DaoItemPedido daoItemPedido = new DaoItemPedido(this);
        for(int cont = 0; cont < itens.size(); cont ++){
            if(daoItemPedido.remover(itens.get(cont)) < 1){
                Toast.makeText(this, "Ocorreu algum erro ao remover Item do Pedido", Toast.LENGTH_SHORT).show();
            }
        }

        //Remoção dos objetos
        for(int cont = 0; cont < pedidosList.size(); cont++){
            if(pedidosList.get(cont).getVenda() == pedido.getVenda()){
                pedidosList.remove(cont);
                pedidoAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case SOLICITACAO_ATIVACAO: {
                //Quando a solicitação de ativação do Bluetooth retorna
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Bluetooth Ativado", Toast.LENGTH_SHORT).show();
                    configuraTransmissao();
                } else {
                    Toast.makeText(this, "Bluetooth Não Ativado", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
            case SOLICITACAO_CONEXAO_SEGURA: {
                //Quando é retornado um dispositivp escolhido a conectar
                if (resultCode == RESULT_OK) {
                    conectarDispositivo(data, true);
                }
                break;
            }
            case SOLICITACAO_CONEXAO_INSEGURA: {
                //Quando é retornado um dispositivo escolhido a conectar
                if (resultCode == RESULT_OK) {
                    conectarDispositivo(data, false);
                }
                break;
            }
        }
    }

    private void conectarDispositivo(Intent data, boolean seguro){
        // Obter o endereço MAC//TODO - quando o banco estiver salvando configurações, obter endereços dele aqui
        String address = data.getExtras().getString(ListaDeDispositivos.EXTRA_DEVICE_ADDRESS);
        // Obter o objeto BluetoothDevice
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        // Tentar conectar ao dispositivo
        servicoBluetooth.connect(device, seguro);//TODO- Potencial para implementação de mais um dispositivo
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch(MODO){
            case CAIXA:
                getMenuInflater().inflate(R.menu.menu_caixa, menu);
                MenuItem menuItem;
                menuItem =  menu.findItem(R.id.abrirCaixa);
                if(caixa == null){//Primeiro caixa
                    menuItem.setTitle(R.string.abrir_caixa);
                    menuItem = menu.findItem(R.id.reabrirCaixa);
                    menuItem.setEnabled(false);

                }else if (caixa.isAberto()){//Último caixa aberto
                    menuItem.setTitle(R.string.fechar_caixa);
                    menuItem = menu.findItem(R.id.reabrirCaixa);
                    menuItem.setEnabled(false);

                }else{//Último caixa está fechado
                    menuItem.setTitle(R.string.abrir_caixa);
                    menuItem = menu.findItem(R.id.reabrirCaixa);
                    menuItem.setEnabled(true);
                }
                return true;
            case MOENDA:
                getMenuInflater().inflate(R.menu.menu_moenda, menu);
                return true;
        }
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch(MODO){
            case CAIXA:
                MenuItem menuItem;
                menuItem = menu.findItem(R.id.abrirCaixa);
                if(caixa == null){//Primeiro caixa
                    menuItem.setTitle(R.string.abrir_caixa);
                    menuItem = menu.findItem(R.id.reabrirCaixa);
                    menuItem.setEnabled(false);

                }else if (caixa.isAberto()){//Último caixa aberto
                    menuItem.setTitle(R.string.fechar_caixa);
                    menuItem = menu.findItem(R.id.reabrirCaixa);
                    menuItem.setEnabled(false);

                }else{//Último caixa está fechado
                    menuItem.setTitle(R.string.abrir_caixa);
                    menuItem = menu.findItem(R.id.reabrirCaixa);
                    menuItem.setEnabled(true);
                }
                menuItem = menu.findItem(R.id.mostrarTodos);
                if(mostraTodos){
                    menuItem.setTitle(R.string.esconder);
                }else{
                    menuItem.setTitle(R.string.mostrar_todos);
                }
                return true;
            case MOENDA:
                return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.conectar_seguro: {
                //Ver a lista de dispositivos
                Intent intentServidor = new Intent(this, ListaDeDispositivos.class);
                startActivityForResult(intentServidor, SOLICITACAO_CONEXAO_SEGURA);
                return true;
            }
//            case R.id.conectar_inseguro: {
//                //Ver a lista de dispositivos
//                Intent intentServidor = new Intent(this, ListaDeDispositivos.class);
//                startActivityForResult(intentServidor, SOLICITACAO_CONEXAO_INSEGURA);
//                return true;
//            }
            case R.id.abrirCaixa: {
                if(isConectado()) {
                    if (item.getTitle().equals("Abrir Caixa")) {//Abrir Caixa
                        caixa = new Caixa();

                        //Sobre o AlertDialog
                        LayoutInflater layoutInflater = getLayoutInflater();
                        View view = layoutInflater.inflate(R.layout.fundo_caixa, null);
                        final EditText valor_fundo = view.findViewById(R.id.valor_fundo);
                        valor_fundo.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            private String current = "";
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if(!s.toString().equals(current)) {
                                    Locale myLocale = new Locale("pt", "BR");
                                    //Nesse bloco ele monta a maskara para money
                                    valor_fundo.removeTextChangedListener(this);
                                    String cleanString = s.toString().replaceAll("[R$,.]", "");
                                    Double parsed = Double.parseDouble(cleanString);
                                    String formatted = NumberFormat.getCurrencyInstance(myLocale).format((parsed / 100));
                                    current = formatted;
                                    valor_fundo.setText(formatted);
                                    valor_fundo.setSelection(formatted.length());
                                    valor_fundo.addTextChangedListener(this);
                                }
                            }
                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Digite o valor inicial do caixa");
                        builder.setView(view);
                        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                caixa.setNumero(numCaixa);
                                caixa.setFundo(Double.parseDouble(valor_fundo.getText().toString().replace("R$", "").replace(",",".")));

                                //Formatando data
                                SimpleDateFormat formatData = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm:ss");

                                Date data = new Date();
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(data);
                                Date data_atual = cal.getTime();

                                caixa.setDataAbertura(formatData.format(data_atual));
                                caixa.setHoraAbertura(formatHora.format(data_atual));

                                Pedido pedido = new Pedido();
                                pedido.setEstado(ABRINDO_CAIXA);
                                pedido.setVenda(caixa.getNumero());
                                pedido.setValor(caixa.getFundo());
                                pedido.setData(caixa.getDataAbertura());
                                pedido.setHora(caixa.getHoraAbertura());
                                enviar(PedidoToStringJSON(pedido));
                            }
                        });
                        builder.setNegativeButton("Cancelar", null);
                        builder.show();
                        //Sobre o AlertDialog

                        return true;

                    } else {//Fechar Caixa
                        //Sobre o AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Confirma fechamento do caixa?");
                        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Formatando data
                                SimpleDateFormat formatData = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm:ss");

                                Date data = new Date();
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(data);
                                Date data_atual = cal.getTime();

                                caixa.setDataFechamento(formatData.format(data_atual));
                                caixa.setHoraFechamento(formatHora.format(data_atual));

                                Pedido pedido = new Pedido();
                                pedido.setEstado(FECHANDO_CAIXA);
                                pedido.setData(caixa.getDataFechamento());
                                pedido.setHora(caixa.getHoraFechamento());
                                enviar(PedidoToStringJSON(pedido));

                                relatorioVendas();
                            }
                        });
                        builder.setNegativeButton("Cancelar", null);
                        builder.show();
                        //Sobre o AlertDialog

                        return true;
                    }
                }
                return false;
            }
            case R.id.reabrirCaixa:{
                if(isConectado()) {
                    caixa.setDataFechamento("");
                    caixa.setHoraFechamento("");

                    Pedido pedido = new Pedido();
                    pedido.setEstado(REABRINDO_CAIXA);
                    pedido.setVenda(caixa.getNumero());
                    enviar(PedidoToStringJSON(pedido));
                }
                return true;
            }
            case R.id.mostrarTodos:{
                mostraTodos = !mostraTodos;
                if(mostraTodos){
                    item.setTitle(R.string.esconder);
                }else{
                    item.setTitle(R.string.mostrar_todos);
                }
                listarPedidos();
                return true;
            }
            case R.id.relAgora: {
                relatorioVendas();
                return true;
            }
            case R.id.relPeriodo: {
                dataInicial = "";
                dataFinal = "";
                //AlertDialog
                LayoutInflater layoutInflater = getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.definir_periodo, null);

                final Button btnDataInicial = view.findViewById(R.id.periodo_d_inicial);
                btnDataInicial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoData = INICIAL;
                        showDialog(DIALOGO_DATA);
                        v.setBackgroundResource(R.color.corPronto);
                    }
                });
                Button btnDataFinal = view.findViewById(R.id.periodo_d_final);
                btnDataFinal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoData = FINAL;
                        showDialog(DIALOGO_DATA);
                        v.setBackgroundResource(R.color.corPronto);
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Defina o período a consultar");
                builder.setView(view);
                builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(dataInicial.equals("") || dataFinal.equals("")){
                            Toast.makeText(Vendas.this, "Não foi escolhida a data Inicial ou Final", Toast.LENGTH_SHORT).show();
                        }else{
                            relatorioPeriodo(dataInicial,dataFinal);
                        }
                    }
                });
                builder.show();
                //AlertDialog
                return true;
            }
        }
        return false;
    }

    public void relatorioVendas(){
        if(caixa == null){
            Toast.makeText(this, "Caixa não identificado", Toast.LENGTH_SHORT).show();
            return;
        }
        Pedido pedido;
        Double fundoCaixa = caixa.getFundo();
        Double totalDinheiro = 0.0;
        int qtdDinheiro = 0;
        Double totalCartao = 0.0;
        int qtdCartao = 0;
        Double totalGeral;
        int qtdTotal;
        Double totalCaixa;

        DaoPedido daoPedido = new DaoPedido(this);
        ArrayList<Pedido> lista = daoPedido.buscarPedidos(caixa);

        if (lista != null) {
            DaoItemPedido daoItemPedido = new DaoItemPedido(this);
            for(int cont = 0; cont < lista.size(); cont++){
                daoItemPedido.buscarItemPedidos(lista.get(cont));
            }
        }else{
            lista = new ArrayList<>();
        }

        for(int cont = 0; cont < lista.size(); cont++){
            pedido = lista.get(cont);
            if(pedido.getEstado() != CANCELADO) {
                if (pedido.getFormaPagamento() == DINHEIRO) {
                    totalDinheiro += pedido.getValor();
                    qtdDinheiro++;
                } else {
                    totalCartao += pedido.getValor();
                    qtdCartao++;
                }
            }
        }

        totalGeral = totalDinheiro + totalCartao;
        totalCaixa = fundoCaixa + totalDinheiro;
        qtdTotal = qtdDinheiro + qtdCartao;

        //AlertDialog
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.rel_vendas, null);

        TextView data = view.findViewById(R.id.rel_v_data);

        //Convertendo data do banco para a GUI
        String dataCaixa = caixa.getDataAbertura().replaceAll("-","/");
        String[] s = dataCaixa.split("/");
        dataCaixa = s[2]+"/"+s[1]+"/"+s[0];
        data.setText(dataCaixa);

        DecimalFormat df = new DecimalFormat(",##0.00");

        TextView fundo = view.findViewById(R.id.rel_v_fundo_caixa);
        fundo.setText(df.format(fundoCaixa));

        TextView dinheiro = view.findViewById(R.id.rel_v_total_dinheiro);
        dinheiro.setText(df.format(totalDinheiro) + " (" + String.valueOf(qtdDinheiro) + " Pedidos)" );

        TextView cartao = view.findViewById(R.id.rel_v_total_cartao);
        cartao.setText(df.format(totalCartao) + " (" + String.valueOf(qtdCartao) + " Pedidos)");

        TextView geral = view.findViewById(R.id.rel_v_total_geral);
        geral.setText(df.format(totalGeral) + " (" + String.valueOf(qtdTotal) + " Pedidos)");

        TextView caixa = view.findViewById(R.id.rel_v_total_caixa);
        caixa.setText(df.format(totalCaixa));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.rel_vendas_ate_agora);
        builder.setView(view);
        builder.setPositiveButton("Certo!", null);
        builder.show();
        //AlertDialog
    }

    public void relatorioPeriodo(String dataInicial, String dataFinal){
        DaoPedido daoPedido = new DaoPedido(this);
        DaoCaixa daoCaixa = new DaoCaixa(this);
        String caixasPesquisar = daoCaixa.pesuisarCaixas(dataInicial, dataFinal);

        if(caixasPesquisar.equals("")){
            Toast.makeText(this, "Nenhum caixa encontrado no período pesquisado.", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] numCaixa = caixasPesquisar.split(",");

        ArrayList<Pedido> pedidos = new ArrayList<>();

        for (String aNumCaixa : numCaixa) {
            ArrayList<Pedido> temp = daoPedido.buscarPeriodo(aNumCaixa);
            pedidos.addAll(temp);
        }

        Pedido pedido;

        Double totalDinheiro = 0.0;
        int qtdDinheiro = 0;
        Double totalCartao = 0.0;
        int qtdCartao = 0;
        Double totalGeral;
        int qtdTotal;

        for(int cont = 0; cont < pedidos.size(); cont++){
            pedido = pedidos.get(cont);
            if(pedido.getEstado() != CANCELADO) {
                if (pedido.getFormaPagamento() == DINHEIRO) {
                    totalDinheiro += pedido.getValor();
                    qtdDinheiro++;
                } else {
                    totalCartao += pedido.getValor();
                    qtdCartao++;
                }
            }
        }

        totalGeral = totalDinheiro + totalCartao;
        qtdTotal = qtdDinheiro + qtdCartao;

        //AlertDialog
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.rel_vendas_periodo, null);

        TextView periodo = view.findViewById(R.id.rel_vp_periodo);

        //Convertendo data
        dataInicial = dataInicial.replaceAll("-","/");
        String[] s = dataInicial.split("/");
        dataInicial = s[2]+"/"+s[1]+"/"+s[0];

        dataFinal = dataFinal.replaceAll("-","/");
        s = dataFinal.split("/");
        dataFinal = s[2]+"/"+s[1]+"/"+s[0];

        periodo.setText(dataInicial + " até " + dataFinal);

        DecimalFormat df = new DecimalFormat(",##0.00");

        TextView dinheiro = view.findViewById(R.id.rel_vp_total_dinheiro);
        dinheiro.setText(df.format(totalDinheiro) + " (" + String.valueOf(qtdDinheiro) + " Pedidos)" );

        TextView cartao = view.findViewById(R.id.rel_vp_total_cartao);
        cartao.setText(df.format(totalCartao) + " (" + String.valueOf(qtdCartao) + " Pedidos)");

        TextView geral = view.findViewById(R.id.rel_vp_total_geral);
        geral.setText(df.format(totalGeral) + " (" + String.valueOf(qtdTotal) + " Pedidos)");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.rel_vendas_periodo);
        builder.setView(view);
        builder.setPositiveButton("Certo!", null);
        builder.show();
        //AlertDialog
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar calendario = Calendar.getInstance();

        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        switch (id) {
            case DIALOGO_DATA:
                return new DatePickerDialog(this, mDateSetListener, ano, mes, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    if(tipoData == INICIAL){
                        dataInicial = String.valueOf(year) + "-" + String.valueOf(monthOfYear+1) + "-" + String.valueOf(dayOfMonth);
                    }else{
                        dataFinal = String.valueOf(year) + "-" + String.valueOf(monthOfYear+1) + "-" + String.valueOf(dayOfMonth);
                    }
                }
            };

    public String PedidoToStringJSON(Pedido pedido){
        JSONObject jsonPedido = new JSONObject();
        JSONArray jsonItens = new JSONArray();
        JSONObject jsonItem;
        ItemPedido itemPedido;
        ArrayList<ItemPedido> itensPedido;
        itensPedido = pedido.getItemPedidos();
        try {
            jsonPedido.put(P_VENDA,pedido.getVenda());
            jsonPedido.put(P_COMANDA,pedido.getComanda());
            jsonPedido.put(P_ESTADO,pedido.getEstado());
            jsonPedido.put(P_VALOR,pedido.getValor());
            jsonPedido.put(P_PAGTO,pedido.getFormaPagamento());
            jsonPedido.put(P_DATA,pedido.getData());
            jsonPedido.put(P_HORA,pedido.getHora());
//            jsonPedido.put(P_CAIXA_NUM,pedido.getCaixa().getNumero());
            for(int cont = 0; cont < itensPedido.size(); cont++){
                itemPedido = itensPedido.get(cont);
                jsonItem = new JSONObject();
                jsonItem.put(IP_SEQUENCIA,itemPedido.getSequencia());
                jsonItem.put(IP_SABOR,itemPedido.getSabor());
                jsonItem.put(IP_RECIP,itemPedido.getRecipiente());
                jsonItem.put(IP_QTD,itemPedido.getQuantidade());
                jsonItem.put(IP_ENTREGUE,itemPedido.getEntregue());
                jsonItem.put(IP_OBS,itemPedido.getObservacao());
//                jsonItem.put(IP_PEDIDO_VENDA,itemPedido.getPedido().getId());
                jsonItens.put(jsonItem);
            }
            jsonPedido.put(ITEM_PEDIDOS,jsonItens);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Falha ao montar JSON", Toast.LENGTH_SHORT).show();
            return null;
        }
        return jsonPedido.toString();
    }

    public Pedido JSONStringToPedido(String stringJSON){
        JSONObject jsonPedido;
        JSONArray jsonItens;
        JSONObject jsonItem;

        Pedido pedido = new Pedido();
        ItemPedido itemPedido;
        try{
            jsonPedido = new JSONObject(stringJSON);
            pedido.setVenda(jsonPedido.getLong(P_VENDA));
            pedido.setComanda(jsonPedido.getInt(P_COMANDA));
            pedido.setEstado(jsonPedido.getInt(P_ESTADO));
            pedido.setValor(jsonPedido.getInt(P_VALOR));
            pedido.setFormaPagamento(jsonPedido.getInt(P_PAGTO));
            pedido.setData(jsonPedido.getString(P_DATA));
            pedido.setHora(jsonPedido.getString(P_HORA));
            pedido.setCaixa(caixa);
            jsonItens = jsonPedido.getJSONArray(ITEM_PEDIDOS);
            for(int cont = 0; cont < jsonItens.length();cont++){
                jsonItem = jsonItens.getJSONObject(cont);
                itemPedido = new ItemPedido();
                itemPedido.setSequencia(jsonItem.getInt(IP_SEQUENCIA));
                itemPedido.setSabor(jsonItem.getInt(IP_SABOR));
                itemPedido.setRecipiente(jsonItem.getInt(IP_RECIP));
                itemPedido.setQuantidade(jsonItem.getInt(IP_QTD));
                itemPedido.setEntregue(jsonItem.getInt(IP_ENTREGUE));
                itemPedido.setObservacao(jsonItem.getInt(IP_OBS));
                itemPedido.setPedido(pedido);
                pedido.addItemPedido(itemPedido);
            }
        }catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(this, "Falha ao montar JSON", Toast.LENGTH_SHORT).show();
        }

        return pedido;
    }

    public void calcSomaItens(Pedido pedido){
        totalTaiti += pedido.getQtdTotal(TAITI);
        totalSiciliano+= pedido.getQtdTotal(SICILIANO);
        totalAbacaxi += pedido.getQtdTotal(ABACAXI);
        totalPuro += pedido.getQtdTotal(PURO);
        totalGengibre += pedido.getQtdTotal(GENGIBRE);
    }

    public void subtraiSomaItens(Pedido pedido){
        totalTaiti -= pedido.getQtdTotal(TAITI);
        totalSiciliano -= pedido.getQtdTotal(SICILIANO);
        totalAbacaxi -= pedido.getQtdTotal(ABACAXI);
        totalPuro -= pedido.getQtdTotal(PURO);
        totalGengibre -= pedido.getQtdTotal(GENGIBRE);
    }

    public void subtraiSomaItem(ItemPedido itemPedido){
        switch(itemPedido.getSabor()){
            case TAITI:
                totalTaiti -= itemPedido.calcQtdRecipiente();
                break;
            case SICILIANO:
                totalSiciliano -= itemPedido.calcQtdRecipiente();
                break;
            case ABACAXI:
                totalAbacaxi -= itemPedido.calcQtdRecipiente();
                break;
            case PURO:
                totalPuro -= itemPedido.calcQtdRecipiente();
                break;
            case GENGIBRE:
                totalGengibre -= itemPedido.calcQtdRecipiente();
                break;
        }
    }

    public void mostrarSomaItens(){
        somaTaiti.setText(totalTaiti + " mL");
        somaSiciliano.setText(totalSiciliano + " mL");
        somaAbacaxi.setText(totalAbacaxi + " mL");
        somaPuro.setText(totalPuro + " mL");
        somaGengibre.setText(totalGengibre + " mL");
    }

    public void zerarSomaItens(){
        totalTaiti = 0;
        totalSiciliano = 0;
        totalAbacaxi = 0;
        totalPuro = 0;
        totalGengibre = 0;
    }
}