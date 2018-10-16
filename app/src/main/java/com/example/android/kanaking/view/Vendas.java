package com.example.android.kanaking.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import java.util.ArrayList;

import static com.example.android.kanaking.Constantes.ABACAXI;
import static com.example.android.kanaking.Constantes.ABRINDO_CAIXA;
import static com.example.android.kanaking.Constantes.CAIXA;
import static com.example.android.kanaking.Constantes.CANCELAR;
import static com.example.android.kanaking.Constantes.COCO;
import static com.example.android.kanaking.Constantes.CONCLUIR;
import static com.example.android.kanaking.Constantes.COPO_300;
import static com.example.android.kanaking.Constantes.COPO_400;
import static com.example.android.kanaking.Constantes.COPO_500;
import static com.example.android.kanaking.Constantes.DEVICE_NAME;
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
import static com.example.android.kanaking.Constantes.OBSERVACAO;
import static com.example.android.kanaking.Constantes.PURO;
import static com.example.android.kanaking.Constantes.P_COMANDA;
import static com.example.android.kanaking.Constantes.P_DATA;
import static com.example.android.kanaking.Constantes.P_ESTADO;
import static com.example.android.kanaking.Constantes.P_HORA;
import static com.example.android.kanaking.Constantes.P_PAGTO;
import static com.example.android.kanaking.Constantes.P_VALOR;
import static com.example.android.kanaking.Constantes.P_VENDA;
import static com.example.android.kanaking.Constantes.QUANTIDADE;
import static com.example.android.kanaking.Constantes.RECIPIENTE;
import static com.example.android.kanaking.Constantes.SABOR;
import static com.example.android.kanaking.Constantes.SICILIANO;
import static com.example.android.kanaking.Constantes.TAITI;
import static com.example.android.kanaking.Constantes.TOAST;

public class Vendas extends AppCompatActivity{

    public static String MODO;

    //Relacionado ao Bluetooth
    private static final int SOLICITACAO_CONEXAO_SEGURA = 1;
    private static final int SOLICITACAO_CONEXAO_INSEGURA = 2;
    private static final int SOLICITACAO_ATIVACAO = 3;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothService servicoBluetooth;
    private StringBuffer bufferSaida;
    private String nomeDispositivoConectado = null;

    private NumberPicker comanda;
    private ListView listaPedidos;
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
    private TextView somaSiciliano;
    private TextView somaAbacaxi;
    private TextView somaPuro;
    private TextView somaGengibre;

    //Controle de objetos
    boolean abrirCaixa = false;//true quando o caixa está fechado
    private Caixa caixa;
    private ItemPedido itemAux;
    private int etapa = 0;
    private Double soma = 0.0;
    private long numCaixa = 1;
    private long numPedido = 1;
    private int numItemPedido = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendas);

        Intent intent = getIntent();
        MODO = intent.getStringExtra("MODO");

        if(MODO.equals(CAIXA)) {
            LinearLayout barraContagem = findViewById(R.id.barra_contagem);
            barraContagem.setVisibility(View.GONE);

            //Configurando NumberPicker
            comanda = (NumberPicker) findViewById(R.id.add_comanda);
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
            GridView itemGrid = (GridView) findViewById(R.id.add_itens);
            if (itensList == null) {
                itensList = new ArrayList<>();
            }

            if (itemAdapter == null) {
                itemAdapter = new ItemPedidoAdapter(this, itensList);
                itemGrid.setAdapter(itemAdapter);
            } else {
                itemAdapter.notifyDataSetChanged();
            }
            //Tratamento de cliques
            itemGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(Vendas.this, "Clique longo", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }else if(MODO.equals(MOENDA)){
            LinearLayout barraLancamento = findViewById(R.id.barra_lancamento);
            barraLancamento.setVisibility(View.GONE);
            somaTaiti = findViewById(R.id.soma_taiti);
            somaSiciliano= findViewById(R.id.soma_siciliano);
            somaAbacaxi= findViewById(R.id.soma_abacaxi);
            somaPuro= findViewById(R.id.soma_puro);
            somaGengibre= findViewById(R.id.soma_gengibre);
        }

        //Obtendo o adaptador Bluetooth e verificando se é suportado
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null){
            Toast.makeText(Vendas.this,"Bluetooth não suportado!",Toast.LENGTH_LONG).show();
            finish();
        }

        textValor = (EditText)findViewById(R.id.add_valor);
        barraEstado = (LinearLayout)findViewById(R.id.fundo_estado);
        estado = (TextView)findViewById(R.id.estado);
        listaPedidos = findViewById(R.id.lista_pedidos);

        //Configurando abertura de caixa
        DaoCaixa daoCaixa = new DaoCaixa(this);
        long id = daoCaixa.ultimoId();
        Toast.makeText(this, "Ultimo ID: " + id, Toast.LENGTH_SHORT).show();
        caixa = daoCaixa.ultimoCaixa(id);
        if(caixa == null){//Primeiro Caixa
            abrirCaixa = true;
        }else{
            numCaixa = caixa.getNumero() + 1;
            if(!caixa.isAberto()) {//Último caixa fechado
                abrirCaixa = true;
            }
        }

        if (abrirCaixa){
            Toast.makeText(this,"Caixa fechado/Primeiro Caixa", Toast.LENGTH_SHORT).show();
            listaPedidos.setEnabled(false);
        }else{
            Toast.makeText(this,"Caixa aberto", Toast.LENGTH_SHORT).show();
            Toast.makeText(Vendas.this, "Caixa: DataAbertura: " + caixa.getDataAbertura() + " HoraAbertura: " + caixa.getHoraAbertura() + " DataFechamento: " + caixa.getDataFechamento() + " HoraFechamento: " + caixa.getHoraFechamento() + " Fundo: " + caixa.getFundo(), Toast.LENGTH_SHORT).show();
        }

        //Configurando o ArrayAdapter PedidoAdapter para a ListView listaPedidos
        //Preencher com as últimas vendas salvas
        DaoPedido daoPedido = new DaoPedido(this);
        pedidosList = daoPedido.buscarPedidos(caixa);

        if (pedidosList == null) {//Nenhum pedido encontrado
            pedidosList = new ArrayList<>();
        }else{//Pedidos encontrados
            DaoItemPedido daoItemPedido = new DaoItemPedido(this);
            for(int cont = 0; cont < pedidosList.size(); cont++){
                daoItemPedido.buscarItemPedidos(pedidosList.get(cont));
            }
            numPedido = pedidosList.get(pedidosList.size()-1).getVenda() + 1;
        }

//        if (pedidoAdapter == null){
        pedidoAdapter = new PedidoAdapter(this,pedidosList);
        listaPedidos.setAdapter(pedidoAdapter);
//        }else {
//            pedidoAdapter.notifyDataSetChanged();
//        }
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


    private void configuraTransmissao(){
        //Inicializar serviço para conexões Bluetooth
        servicoBluetooth = new BluetoothService(this,handler);

        //Inicializar buffer de saída
        bufferSaida = new StringBuffer("");
    }

//    private void tornarVisivel(){
//        //Torna visível por 5 minutos
//        if(bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
//            Intent intentVisivel = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//            intentVisivel.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
//            startActivity(intentVisivel);
//        }
//    }

    public void adicionar(View view){
        if (!abrirCaixa){//caixa aberto
            Double valor = Double.valueOf(textValor.getText().toString().replace(",","."));
            if(valor > 0.0) {
                numItemPedido = 1;
                Pedido pedido = new Pedido(comanda.getValue(), numPedido, comanda.getValue(), LANCADO, valor, pagamento.getSelectedItemPosition(), "", "", itensList);
                numPedido++;
                enviar(PedidoToStringJSON(pedido));
            }else{
                Toast.makeText(this,"Pedido sem preço", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Caixa fechado", Toast.LENGTH_SHORT).show();
        }
    }

    public void adicionarItem(final View v){//Disparado pelo botão "+" de ItemPedidos
        //Para adicionar itens de pedido
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

                    menuItem = (MenuItem)menu.findItem(R.id.c_300);
                    menuItem.setIcon(itemAux.getIconeSabor(COPO_300));

                    menuItem = (MenuItem)menu.findItem(R.id.c_400);
                    menuItem.setIcon(itemAux.getIconeSabor(COPO_400));

                    menuItem = (MenuItem)menu.findItem(R.id.c_500);
                    menuItem.setIcon(itemAux.getIconeSabor(COPO_500));

                    menuItem = (MenuItem)menu.findItem(R.id.g_500);
                    menuItem.setIcon(itemAux.getIconeSabor(GARRAFA_500));

                    menuItem = (MenuItem)menu.findItem(R.id.g_1000);
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
                                itensList.add(0,itemAux);
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

                menuItem = (MenuItem) menu.findItem(R.id.qtde);
                menuItem.setTitle("Quantidade: " + itemAux.getQuantidade());

                menuItem = (MenuItem) menu.findItem(R.id.obs);
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
        TextView valor, estado;
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

    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Activity activity = Vendas.this;
            switch (msg.what) {
                case MENSAGEM_MUDANCA_ESTADO: {
                    switch (msg.arg1) {
                        case BluetoothService.ESTADO_CONECTADO:
                            setStatus(getString(R.string.title_connected_to, nomeDispositivoConectado));
                            pedidosList.clear();
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

//                    Toast.makeText(activity, "Escrever: " + writeMessage, Toast.LENGTH_LONG).show();

                    Pedido pedido = JSONStringToPedido(writeMessage);
                    int estado = pedido.getEstado();
                    if (estado > FECHANDO_CAIXA) {//Operação com Pedido
                        DaoPedido daoPedido = new DaoPedido(activity);
                        long id = daoPedido.inserir(pedido);
                        if (id == -1){
                            Toast.makeText(activity, "Ocorreu algum erro ao lançar o Pedido", Toast.LENGTH_SHORT).show();
                            numPedido--;
                            break;
                        }
                        pedido.setId(id);

                        ArrayList<ItemPedido> itemPedidos = pedido.getItemPedidos();
                        DaoItemPedido daoItemPedido = new DaoItemPedido(activity);
                        for(int cont = 0; cont < itemPedidos.size(); cont++){
                            id = daoItemPedido.inserir(itemPedidos.get(cont));
                            if(id == -1){
                                Toast.makeText(activity, "Ocorreu algum erro ao lançar Itens do Pedido", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            itemPedidos.get(cont).setId(id);
                        }

                        if(id == -1){
                            Toast.makeText(activity, "Desculpe, tente novamente", Toast.LENGTH_SHORT).show();
                        }
                        pedidosList.add(0, pedido);
                        caixa.addPedido(pedido);
                        zerar();
                        pedidoAdapter.notifyDataSetChanged();
                    } else if (estado == ABRINDO_CAIXA) {
                        DaoCaixa daoCaixa = new DaoCaixa(activity);
                        if (daoCaixa.abrirCaixa(caixa) == -1){
                            Toast.makeText(activity, "Ocorreu algum erro ao abrir o caixa", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        numCaixa++;
                        abrirCaixa = false;
                        listaPedidos.setEnabled(true);
                        invalidateOptionsMenu();

                    } else {//FECHANDO_CAIXA
                        DaoCaixa daoCaixa = new DaoCaixa(activity);
                        if (daoCaixa.fecharCaixa(caixa) < 1){
                            Toast.makeText(activity, "Ocorreu algum erro ao fechar o caixa", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        abrirCaixa = true;
                        listaPedidos.setEnabled(false);
                        invalidateOptionsMenu();

                    }
                    break;
                }
                case MENSAGEM_LER: {//Quem recebe a mensagem
                    byte[] readBuf = (byte[]) msg.obj;
                    // Construir um string com os bytes validos do buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

//                    Toast.makeText(activity, "Ler: " + readMessage, Toast.LENGTH_LONG).show();

                    Pedido pedido = JSONStringToPedido(readMessage);
                    int estado = pedido.getEstado();
                    if (estado > FECHANDO_CAIXA) {//Operação com Pedido
                        ////////////////////////////////////
                        DaoPedido daoPedido = new DaoPedido(activity);
                        long id = daoPedido.inserir(pedido);
                        if (id == -1){
                            Toast.makeText(activity, "Ocorreu algum erro ao lançar o Pedido", Toast.LENGTH_SHORT).show();
                            numPedido--;
                            break;
                        }
                        pedido.setId(id);

                        ArrayList<ItemPedido> itemPedidos = pedido.getItemPedidos();
                        DaoItemPedido daoItemPedido = new DaoItemPedido(activity);
                        for(int cont = 0; cont < itemPedidos.size(); cont++){
                            id = daoItemPedido.inserir(itemPedidos.get(cont));
                            if(id == -1){
                                Toast.makeText(activity, "Ocorreu algum erro ao lançar Itens do Pedido", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            itemPedidos.get(cont).setId(id);
                        }

                        if(id == -1){
                            Toast.makeText(activity, "Desculpe, tente novamente", Toast.LENGTH_SHORT).show();
                        }
                        ////////////////////////////////////
                        pedidosList.add(0, pedido);
                        caixa.addPedido(pedido);
                        if(MODO.equals(MOENDA)) {
                            Toast.makeText(activity, "Verificando modo", Toast.LENGTH_SHORT).show();
                            adicionarSomaItens(pedido);
                        }
                        pedidoAdapter.notifyDataSetChanged();

                    } else if (estado == ABRINDO_CAIXA) {
                        Toast.makeText(activity, "Ler: Abrir Caixa", Toast.LENGTH_SHORT).show();

                        caixa = new Caixa();
                        caixa.setFundo(pedido.getValor());
                        caixa.setNumero(pedido.getVenda());
                        caixa.setDataAbertura(pedido.getData());
                        caixa.setHoraAbertura(pedido.getHora());
                        //////////////////////////////
                        DaoCaixa daoCaixa = new DaoCaixa(activity);
                        if (daoCaixa.abrirCaixa(caixa) == -1){
                            Toast.makeText(activity, "Ocorreu algum erro ao abrir o caixa", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        //////////////////////////////
                        numCaixa++;
                        abrirCaixa = false;
                        listaPedidos.setEnabled(true);
                        invalidateOptionsMenu();

                    } else {//FECHANDO_CAIXA
                        caixa.setDataFechamento(pedido.getData());
                        caixa.setHoraFechamento(pedido.getHora());
                        //////////////////////////////
                        DaoCaixa daoCaixa = new DaoCaixa(activity);
                        if (daoCaixa.fecharCaixa(caixa) < 1){
                            Toast.makeText(activity, "Ocorreu algum erro ao fechar o caixa", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        //////////////////////////////
                        abrirCaixa = true;
                        listaPedidos.setEnabled(false);
                        invalidateOptionsMenu();

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



    public void menuPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.cancelar:
                        Toast.makeText(Vendas.this,"Cancelar",Toast.LENGTH_SHORT).show();
                        return true;
//                        break;
                    case R.id.apagar:
                        Toast.makeText(Vendas.this,"Apagar",Toast.LENGTH_SHORT).show();
                        return true;
//                        break;
                }
                return false;
            }
        });
        popup.inflate(R.menu.menu_pedido);
        popup.show();
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
        servicoBluetooth.connect(device, seguro);//TODO- Potencial
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch(MODO){
            case CAIXA:
                getMenuInflater().inflate(R.menu.menu_caixa, menu);
                MenuItem menuItem;
                menuItem = (MenuItem) menu.findItem(R.id.abrirCaixa);
                if(caixa == null){//Primeiro caixa
                    menuItem.setTitle(R.string.abrir_caixa);
                    menuItem = (MenuItem) menu.findItem(R.id.reabrirCaixa);
                    menuItem.setEnabled(false);

                }else if (!caixa.isAberto()){//Último caixa fechado
                    menuItem.setTitle(R.string.abrir_caixa);
                    menuItem = (MenuItem) menu.findItem(R.id.reabrirCaixa);
                    menuItem.setEnabled(true);
                }else{//Último caixa aberto
                    menuItem.setTitle(R.string.fechar_caixa);
                    menuItem = (MenuItem) menu.findItem(R.id.reabrirCaixa);
                    menuItem.setEnabled(false);
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
                menuItem = (MenuItem) menu.findItem(R.id.abrirCaixa);
                if(caixa == null){//Primeiro caixa
                    menuItem.setTitle(R.string.abrir_caixa);
                    menuItem = (MenuItem) menu.findItem(R.id.reabrirCaixa);
                    menuItem.setEnabled(false);

                }else if (!caixa.isAberto()){//Último caixa está fechado
                    menuItem.setTitle(R.string.abrir_caixa);
                    menuItem = (MenuItem) menu.findItem(R.id.reabrirCaixa);
                    menuItem.setEnabled(true);
                }else{//Último caixa aberto
                    menuItem.setTitle(R.string.fechar_caixa);
                    menuItem = (MenuItem) menu.findItem(R.id.reabrirCaixa);
                    menuItem.setEnabled(false);
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
            case R.id.conectar_inseguro: {
                //Ver a lista de dispositivos
                Intent intentServidor = new Intent(this, ListaDeDispositivos.class);
                startActivityForResult(intentServidor, SOLICITACAO_CONEXAO_INSEGURA);
                return true;
            }
//            case R.id.visivel: {
//                //Tornar Visível aos outros dispositivos
//                tornarVisivel();
//                return true;
//            }
            case R.id.abrirCaixa: {
                if(isConectado()) {
                    if (abrirCaixa) {//Fechado -> Operação Abrir
                        caixa = new Caixa();

                        //Sobre o AlertDialog
                        LayoutInflater layoutInflater = getLayoutInflater();
                        View view = layoutInflater.inflate(R.layout.fundo_caixa, null);
                        final EditText valor_fundo = view.findViewById(R.id.valor_fundo);

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Digite o valor inicial do caixa");
                        builder.setView(view);
                        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                caixa.setNumero(numCaixa);
                                caixa.setFundo(Double.parseDouble(valor_fundo.getText().toString().replace(",", ".")));
                                caixa.setDataAbertura("1");
                                caixa.setHoraAbertura("1");

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

                        //TODO - Rever o Reabrir Caixa
                        //TODO - Chamar tela com o relatório do caixa
                        return true;

                    } else {//Aberto -> Operação Fechar
                        //Sobre o AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Confirma fechamento do caixa?");
                        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                caixa.setDataFechamento("2");
                                caixa.setHoraFechamento("2");

                                Pedido pedido = new Pedido();
                                pedido.setEstado(FECHANDO_CAIXA);
                                pedido.setData(caixa.getDataFechamento());
                                pedido.setHora(caixa.getHoraFechamento());
                                enviar(PedidoToStringJSON(pedido));
                            }
                        });
                        builder.setNegativeButton("Cancelar", null);
                        builder.show();
                        //Sobre o AlertDialog

                        return true;
                    }
                }
            }
            case R.id.reabrirCaixa:{
                if(isConectado()) {
                    item.setEnabled(false);
                }
            }
            case R.id.relAgora: {

                return true;
            }
            case R.id.relPeriodo: {

                return true;
            }
        }
        return false;
    }
    public String PedidoToStringJSON(Pedido pedido){
        JSONObject jsonPedido = new JSONObject();
        JSONArray jsonItens = new JSONArray();
        JSONObject jsonItem;
        ItemPedido itemPedido;
        try {
            jsonPedido.put(P_VENDA,pedido.getVenda());
            jsonPedido.put(P_COMANDA,pedido.getComanda());
            jsonPedido.put(P_ESTADO,pedido.getEstado());
            jsonPedido.put(P_VALOR,pedido.getValor());
            jsonPedido.put(P_PAGTO,pedido.getFormaPagamento());
            jsonPedido.put(P_DATA,pedido.getData());
            jsonPedido.put(P_HORA,pedido.getHora());
//            jsonPedido.put(P_CAIXA_NUM,pedido.getCaixa().getNumero());
            for(int cont = 0; cont < itensList.size(); cont++){
                itemPedido = itensList.get(cont);
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
        }

        return pedido;
    }
    public void adicionarSomaItens(Pedido pedido){
        Toast.makeText(this, "Adicionar soma itens", Toast.LENGTH_SHORT).show();
        somaTaiti.setText(pedido.getQtdTotal(TAITI)+" mL");
        somaSiciliano.setText(pedido.getQtdTotal(SICILIANO)+" mL");
        somaAbacaxi.setText(pedido.getQtdTotal(ABACAXI)+" mL");
        somaPuro.setText(pedido.getQtdTotal(PURO)+" mL");
        somaGengibre.setText(pedido.getQtdTotal(GENGIBRE)+" mL");
    }
}