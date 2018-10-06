package com.example.android.kanaking.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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
import com.example.android.kanaking.control.BluetoothService;
import com.example.android.kanaking.model.ItemPedido;
import com.example.android.kanaking.model.Pedido;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static com.example.android.kanaking.Constantes.ABACAXI;
import static com.example.android.kanaking.Constantes.CAIXA;
import static com.example.android.kanaking.Constantes.COCO;
import static com.example.android.kanaking.Constantes.COCO_FRUTA;
import static com.example.android.kanaking.Constantes.COMANDA;
import static com.example.android.kanaking.Constantes.CONCLUIR;
import static com.example.android.kanaking.Constantes.COPO_300;
import static com.example.android.kanaking.Constantes.COPO_400;
import static com.example.android.kanaking.Constantes.COPO_500;
import static com.example.android.kanaking.Constantes.DEVICE_NAME;
import static com.example.android.kanaking.Constantes.GARRAFA_1000;
import static com.example.android.kanaking.Constantes.GARRAFA_500;
import static com.example.android.kanaking.Constantes.GENGIBRE;
import static com.example.android.kanaking.Constantes.LANCADO;
import static com.example.android.kanaking.Constantes.MENSAGEM_ESCREVER;
import static com.example.android.kanaking.Constantes.MENSAGEM_LER;
import static com.example.android.kanaking.Constantes.MENSAGEM_MUDANCA_ESTADO;
import static com.example.android.kanaking.Constantes.MENSAGEM_NOME_DISPOSITIVO;
import static com.example.android.kanaking.Constantes.MENSAGEM_TOAST;
import static com.example.android.kanaking.Constantes.MOENDA;
import static com.example.android.kanaking.Constantes.NENHUMA;
import static com.example.android.kanaking.Constantes.OBSERVACAO;
import static com.example.android.kanaking.Constantes.PAGAMENTO;
import static com.example.android.kanaking.Constantes.POUCO_GELO;
import static com.example.android.kanaking.Constantes.PRECOS;
import static com.example.android.kanaking.Constantes.PURO;
import static com.example.android.kanaking.Constantes.QUANTIDADE;
import static com.example.android.kanaking.Constantes.RECIPIENTE;
import static com.example.android.kanaking.Constantes.SABOR;
import static com.example.android.kanaking.Constantes.SEM_GELO;
import static com.example.android.kanaking.Constantes.SICILIANO;
import static com.example.android.kanaking.Constantes.TAITI;
import static com.example.android.kanaking.Constantes.TOAST;
import static com.example.android.kanaking.Constantes.VALOR;

public class Vendas extends AppCompatActivity{

    private static String MODO;

    //Relacionado ao Bluetooth
    private static final int SOLICITACAO_CONEXAO_SEGURA = 1;
    private static final int SOLICITACAO_CONEXAO_INSEGURA = 2;
    private static final int SOLICITACAO_ATIVACAO = 3;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothService servicoBluetooth;
    private StringBuffer bufferSaida;
    private String nomeDispositivoConectado = null;

    private ListView listView;
    private NumberPicker comanda;
    private int numComanda;
    private int formaPagto;
    private PedidoAdapter pedidoAdapter;
    private ArrayList<Pedido> pedidosList;
    private Spinner pagamento;
    private PagamentoAdapter pagtoAdapter;
    private GridView itemGrid;
    private ItemPedidoAdapter itemAdapter;
    private ArrayList<ItemPedido> itensList;
    private TextView textValor;
    private Double valor;
    private int imgPagto;
    private LinearLayout barraEstado;
    private TextView estado;

    //Item de Pedido temporário
    private ItemPedido itemAux;
    private int etapa = 0;
    private Double soma = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendas);

        Intent intent = getIntent();
        String modo = intent.getStringExtra("MODO");
        MODO = modo;

        //Configurando NumberPicker
        comanda = (NumberPicker)findViewById(R.id.add_comanda);
        comanda.setMaxValue(20);
        comanda.setMinValue(1);

        //Configurando Spinner Forma de pagamento
        ArrayList<Pagamento> listaPagto = new ArrayList<>();
        listaPagto.add(new Pagamento(R.drawable.dinheiro));
        listaPagto.add(new Pagamento(R.drawable.cartao));

        pagamento = findViewById(R.id.add_pagamento);
        pagtoAdapter = new PagamentoAdapter(this,R.layout.lista_imagem,listaPagto);
        pagamento.setAdapter(pagtoAdapter);

        //Configurando GridView onde aparecerão os itens do pedido a lançar
        itemGrid = (GridView) findViewById(R.id.add_itens);
        if (itensList == null) {
            itensList = new ArrayList<>();
        }

        if (itemAdapter == null){
            itemAdapter = new ItemPedidoAdapter(this,itensList);
            itemGrid.setAdapter(itemAdapter);
        }else {
            itemAdapter.notifyDataSetChanged();
        }
        //Tratamento de cliques
        itemGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Vendas.this, "Clique longo",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        itemGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Vendas.this, "Clique simples",Toast.LENGTH_SHORT).show();
            }

        });

        //Configurando o ArrayAdapter PedidoAdapter para a ListView listaPedidos
        listView = (ListView) findViewById(R.id.lista_pedidos);

        if (pedidosList == null) {
            pedidosList = new ArrayList<>();
        }

        if (pedidoAdapter == null){
            pedidoAdapter = new PedidoAdapter(this,pedidosList);
            listView.setAdapter(pedidoAdapter);
        }else {
            pedidoAdapter.notifyDataSetChanged();
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Realizando essa verificação abrange-se o caso do Bluetooth estar desabilitado
        // durante o onStart(), então a tela está pausada para habilitá-lo...
        // onResume() será chamado quando a atividade SOLICITACAO_ATIVACAO retornar.
        if (servicoBluetooth != null) {
            // Somente se o estado for ESTADO_NENHUM, sabemos se ainda não iniciamos
            if (servicoBluetooth.getState() == servicoBluetooth.ESTADO_NENHUM) {
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

    private void tornarVisivel(){
        //Torna visível por 5 minutos
        if(bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            Intent intentVisivel = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            intentVisivel.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
            startActivity(intentVisivel);
        }
    }

    public void adicionar(View view){
        //TODO - talvez este conteudo estará no Handler

        numComanda = comanda.getValue();

        valor = Double.valueOf(textValor.getText().toString());

        formaPagto = pagamento.getSelectedItemPosition();

        JSONObject jsonPedido = new JSONObject();
            try {
                jsonPedido.put(COMANDA,numComanda);
                jsonPedido.put(VALOR,valor);
                jsonPedido.put(PAGAMENTO,formaPagto);
                } catch (JSONException e) {
                e.printStackTrace();
                return;
                }
                String conteudo = jsonPedido.toString();

//        pedidosList.add(0,new Pedido(1,1,comanda.getValue(),LANCADO,Double.valueOf(valor.getText().toString()),imgPagto,21092018,1613,itensList));

        enviar(conteudo);
    }
    public void zerar(View view){
        TextView valor, estado;
        valor = (EditText)findViewById(R.id.add_valor);

        itensList.clear();
        soma = 0.0;
        valor.setText("0");

        pagamento.setSelection(0);
    }

    public void zerar(){
        TextView valor, estado;
        valor = (EditText)findViewById(R.id.add_valor);

        itensList.clear();
        soma = 0.0;
        valor.setText("0");

        pagamento.setSelection(0);
    }

    private void enviar(String conteudo){
        //Verificar se estamos conectados antes de tentar algo
        if (servicoBluetooth.getState() != BluetoothService.ESTADO_CONECTADO) {
            Toast.makeText(this, R.string.nao_conectado, Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificando se tem algo para enviar
        if (conteudo.length() > 0) {
            // Obter os bytes do conteudo e enviar pelo BluetoothService
            byte[] enviar = conteudo.getBytes();
            servicoBluetooth.write(enviar);

            // Zerar buffer de saída
            bufferSaida.setLength(0);
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
                case MENSAGEM_MUDANCA_ESTADO:
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
                case MENSAGEM_ESCREVER:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // Construir uma string com o buffer
                    String writeMessage = new String(writeBuf);

                    Toast.makeText(activity,"Escrever: " + writeMessage,Toast.LENGTH_LONG);

                    pedidosList.add(0,new Pedido(1,1,numComanda,LANCADO,valor,formaPagto,21092018,1613,itensList));
                    pedidoAdapter.notifyDataSetChanged();
                    break;

                case MENSAGEM_LER:
                    byte[] readBuf = (byte[]) msg.obj;
                    // Construir um string com os bytes validos do buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    Toast.makeText(activity,"Ler: " + readMessage,Toast.LENGTH_LONG);

                    JSONObject jsonPedido;
                    try{
                        jsonPedido = new JSONObject(readMessage);
                        numComanda = jsonPedido.getInt(COMANDA);
                        valor = jsonPedido.getDouble(VALOR);
                        formaPagto = jsonPedido.getInt(PAGAMENTO);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                    pedidosList.add(0,new Pedido(1,1,numComanda,LANCADO,valor,formaPagto,21092018,1613,itensList));
                    pedidoAdapter.notifyDataSetChanged();
                    break;
                case MENSAGEM_NOME_DISPOSITIVO:
                    // save the connected device's name
                    nomeDispositivoConectado = msg.getData().getString(DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Conectado com " + nomeDispositivoConectado, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MENSAGEM_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(TOAST),Toast.LENGTH_SHORT).show();
                    }
                    break;
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

    public void adicionarItem(final View v){
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
                        int escolha = -1;
                        switch (item.getItemId()){
                            case R.id.siciliano:
                                escolha = SICILIANO;
                                break;
                            case R.id.taiti:
                                escolha = TAITI;
                                break;
                            case R.id.abacaxi:
                                escolha = ABACAXI;
                                break;
                            case R.id.puro:
                                escolha = PURO;
                                break;
                            case R.id.gengibre:
                                escolha = GENGIBRE;
                                break;
                            case R.id.coco:
                                escolha = COCO;
                                break;
                            case R.id.cancelar:
                                return true;
                        }
                        itemAux.setSabor(escolha);
                        //CONTROLE DE TESTES
                        Toast.makeText(Vendas.this,"Item: Sabor:"+itemAux.getSabor()+ " Recip:"+ itemAux.getRecipiente() + " Qtd:" + itemAux.getQuantidade() + " Obs:"+ itemAux.getObservacao(),Toast.LENGTH_SHORT).show();
                        etapa = RECIPIENTE;
                        adicionarItem(v);
                        return true;
                    }
                });
                popup.inflate(R.menu.menu_sabor_item);
                popup.show();
                break;

            case RECIPIENTE:

                if (itemAux == null){
                    Toast.makeText(Vendas.this,"Desculpe, tente novamente.",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(itemAux.getSabor() == COCO){
                    //Se o sabor for COCO
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int escolha = -1;
                            switch (item.getItemId()){
                                case R.id.c_fruta:
                                    escolha = COCO_FRUTA;
                                    break;
                                case R.id.c_c_500:
                                    escolha = COPO_500;
                                    break;
                                case R.id.c_g_500:
                                    escolha = GARRAFA_500;
                                    break;
                                case R.id.c_g_1000:
                                    escolha = GARRAFA_1000;
                                    break;
                                case R.id.cancelar:
                                    etapa = SABOR;//Retorna à 1a etapa
                                    return true;
                            }
                            itemAux.setRecipiente(escolha);
                            //CONTROLE DE TESTES
                            Toast.makeText(Vendas.this,"Item: Sabor:"+itemAux.getSabor()+ " Recip:"+ itemAux.getRecipiente() + " Qtd:" + itemAux.getQuantidade() + " Obs:"+ itemAux.getObservacao(),Toast.LENGTH_SHORT).show();
                            etapa = CONCLUIR;
                            adicionarItem(v);
                            return true;
                        }
                    });

                    popup.inflate(R.menu.menu_recipiente_coco_item);
                }else{
                    //Se for caldo de cana mesmo
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int escolha = -1;
                            switch (item.getItemId()){
                                case R.id.c_300:
                                    escolha = COPO_300;
                                    break;
                                case R.id.c_400:
                                    escolha = COPO_400;
                                    break;
                                case R.id.c_500:
                                    escolha = COPO_500;
                                    break;
                                case R.id.g_500:
                                    escolha = GARRAFA_500;
                                    break;
                                case R.id.g_1000:
                                    escolha = GARRAFA_1000;
                                    break;
                                case R.id.cancelar:
                                    etapa = SABOR;//Retorna à 1a etapa
                                    return true;
                            }
                            itemAux.setRecipiente(escolha);
                            //CONTROLE DE TESTES
                            Toast.makeText(Vendas.this,"Item: Sabor:"+itemAux.getSabor()+ " Recip:"+ itemAux.getRecipiente() + " Qtd:" + itemAux.getQuantidade() + " Obs:"+ itemAux.getObservacao(),Toast.LENGTH_SHORT).show();
                            etapa = CONCLUIR;
                            adicionarItem(v);
                            return true;
                        }
                    });

                    popup.inflate(R.menu.menu_recipiente_sabor_item);
                    Menu menu = popup.getMenu();
                    MenuItem menuItem;
                    switch(itemAux.getSabor()){
                        case SICILIANO:
                            menuItem = (MenuItem)menu.findItem(R.id.c_300);
                            menuItem.setIcon(R.drawable.s_c);

                            menuItem = (MenuItem)menu.findItem(R.id.c_400);
                            menuItem.setIcon(R.drawable.s_c);

                            menuItem = (MenuItem)menu.findItem(R.id.c_500);
                            menuItem.setIcon(R.drawable.s_c);

                            menuItem = (MenuItem)menu.findItem(R.id.g_500);
                            menuItem.setIcon(R.drawable.s_g);

                            menuItem = (MenuItem)menu.findItem(R.id.g_1000);
                            menuItem.setIcon(R.drawable.s_g);
                            break;

                        case TAITI:
                            menuItem = (MenuItem)menu.findItem(R.id.c_300);
                            menuItem.setIcon(R.drawable.t_c);

                            menuItem = (MenuItem)menu.findItem(R.id.c_400);
                            menuItem.setIcon(R.drawable.t_c);

                            menuItem = (MenuItem)menu.findItem(R.id.c_500);
                            menuItem.setIcon(R.drawable.t_c);

                            menuItem = (MenuItem)menu.findItem(R.id.g_500);
                            menuItem.setIcon(R.drawable.t_g);

                            menuItem = (MenuItem)menu.findItem(R.id.g_1000);
                            menuItem.setIcon(R.drawable.t_g);
                            break;

                        case ABACAXI:
                            menuItem = (MenuItem)menu.findItem(R.id.c_300);
                            menuItem.setIcon(R.drawable.a_c);

                            menuItem = (MenuItem)menu.findItem(R.id.c_400);
                            menuItem.setIcon(R.drawable.a_c);

                            menuItem = (MenuItem)menu.findItem(R.id.c_500);
                            menuItem.setIcon(R.drawable.a_c);

                            menuItem = (MenuItem)menu.findItem(R.id.g_500);
                            menuItem.setIcon(R.drawable.a_g);

                            menuItem = (MenuItem)menu.findItem(R.id.g_1000);
                            menuItem.setIcon(R.drawable.a_g);
                            break;

                        case PURO:
                            menuItem = (MenuItem)menu.findItem(R.id.c_300);
                            menuItem.setIcon(R.drawable.p_c);

                            menuItem = (MenuItem)menu.findItem(R.id.c_400);
                            menuItem.setIcon(R.drawable.p_c);

                            menuItem = (MenuItem)menu.findItem(R.id.c_500);
                            menuItem.setIcon(R.drawable.p_c);

                            menuItem = (MenuItem)menu.findItem(R.id.g_500);
                            menuItem.setIcon(R.drawable.p_g);

                            menuItem = (MenuItem)menu.findItem(R.id.g_1000);
                            menuItem.setIcon(R.drawable.p_g);
                            break;

                        case GENGIBRE:
                            menuItem = (MenuItem)menu.findItem(R.id.c_300);
                            menuItem.setIcon(R.drawable.g_c);

                            menuItem = (MenuItem)menu.findItem(R.id.c_400);
                            menuItem.setIcon(R.drawable.g_c);

                            menuItem = (MenuItem)menu.findItem(R.id.c_500);
                            menuItem.setIcon(R.drawable.g_c);

                            menuItem = (MenuItem)menu.findItem(R.id.g_500);
                            menuItem.setIcon(R.drawable.g_g);

                            menuItem = (MenuItem)menu.findItem(R.id.g_1000);
                            menuItem.setIcon(R.drawable.g_g);
                            break;
                    }
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

                                soma += calcSoma(itemAux);
                                TextView valor;
                                valor = (EditText)findViewById(R.id.add_valor);
                                valor.setText(String.valueOf(soma));


                                itensList.add(0,itemAux);
                                itemAdapter.notifyDataSetChanged();
                                //TODO - Colocar formatador de String

                                break;
                            case R.id.cancelar:
                                etapa = SABOR;//Retorna à 1a etapa
                                return true;
                        }
                        //CONTROLE DE TESTES
                        Toast.makeText(Vendas.this,"Item: Sabor:"+itemAux.getSabor()+ " Recip:"+ itemAux.getRecipiente() + " Qtd:" + itemAux.getQuantidade() + " Obs:"+ itemAux.getObservacao(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popup.inflate(R.menu.menu_finalizar_item);
                Menu menu_fin = popup.getMenu();
                MenuItem menuItem_fin;

                menuItem_fin = (MenuItem) menu_fin.findItem(R.id.qtde);
                menuItem_fin.setTitle("Quantidade: " + itemAux.getQuantidade());

                menuItem_fin = (MenuItem) menu_fin.findItem(R.id.obs);
                switch(itemAux.getObservacao()){
                    case SEM_GELO:
                        menuItem_fin.setTitle("Obs: Sem Gelo");
                        break;

                    case POUCO_GELO:
                        menuItem_fin.setTitle("Obs: Pouco Gelo");
                        break;
                }
                popup.show();
                break;

            case QUANTIDADE:
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int escolha = -1;
                        switch (item.getItemId()){
                            case R.id.qtd_1:
                                escolha = 1;
                                break;
                            case R.id.qtd_2:
                                escolha = 2;
                                break;
                            case R.id.qtd_3:
                                escolha = 3;
                                break;
                            case R.id.qtd_4:
                                escolha = 4;
                                break;
                            case R.id.qtd_5:
                                escolha = 5;
                                break;
                            case R.id.qtd_6:
                                escolha = 6;
                                break;
                            case R.id.qtd_7:
                                escolha = 7;
                                break;
                            case R.id.qtd_8:
                                escolha = 8;
                                break;
                            case R.id.qtd_9:
                                escolha = 9;
                                break;
                            case R.id.qtd_10:
                                escolha = 10;
                                break;
                            case R.id.cancelar:
                                escolha = itemAux.getQuantidade();
                                break;
                        }
                        itemAux.setQuantidade(escolha);
                        //CONTROLE DE TESTES
                        Toast.makeText(Vendas.this,"Item: Sabor:"+itemAux.getSabor()+ " Recip:"+ itemAux.getRecipiente() + " Qtd:" + itemAux.getQuantidade() + " Obs:"+ itemAux.getObservacao(),Toast.LENGTH_SHORT).show();
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
                        int escolha = -1;
                        switch (item.getItemId()){
                            case R.id.sem_gelo:
                                escolha = SEM_GELO;
                                break;
                            case R.id.pouco_gelo:
                                escolha = POUCO_GELO;
                                break;
                            case R.id.nenhuma:
                                escolha = NENHUMA;
                                break;
                            case R.id.cancelar:
                                escolha = itemAux.getObservacao();
                                break;
                        }
                        itemAux.setObservacao(escolha);
                        //CONTROLE DE TESTES
                        Toast.makeText(Vendas.this,"Item: Sabor:"+itemAux.getSabor()+ " Recip:"+ itemAux.getRecipiente() + " Qtd:" + itemAux.getQuantidade() + " Obs:"+ itemAux.getObservacao(),Toast.LENGTH_SHORT).show();
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
    //TODO - Por esta função no Model
    private Double calcSoma (ItemPedido item){
        return (item.getQuantidade()* PRECOS[item.getRecipiente()]);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case SOLICITACAO_ATIVACAO:
                //Quando a solicitação de ativação do Bluetooth retorna
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(Vendas.this,"Bluetooth Ativado",Toast.LENGTH_SHORT).show();
                    configuraTransmissao();
                }else{
                    Toast.makeText(Vendas.this,"Bluetooth Não Ativado",Toast.LENGTH_SHORT).show();
                }
            case SOLICITACAO_CONEXAO_SEGURA:
                //Quando é retornado um dispositivp escolhido a conectar
                if(resultCode == Activity.RESULT_OK){
                    conectarDispositivo(data,true);
                }
            case SOLICITACAO_CONEXAO_INSEGURA:
                //Quando é retornado um dispositivp escolhido a conectar
                if(resultCode == Activity.RESULT_OK){
                    conectarDispositivo(data, false);
                }
        }
    }

    private void conectarDispositivo(Intent data, boolean seguro){
        // Obter o endereço MAC
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
                return true;
            case MOENDA:
                getMenuInflater().inflate(R.menu.menu_moenda, menu);
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
            case R.id.visivel: {
                //Tornar Visível aos outros dispositivos
                tornarVisivel();
                return true;
            }
            case R.id.abrirCaixa: {
                Toast.makeText(this, "" + item.getTitle(), Toast.LENGTH_SHORT).show();
                switch (item.getTitle().toString()) {
                    case "Abrir Caixa":
                        item.setTitle(R.string.fechar_caixa);
                        return true;
                    case "Fechar Caixa":
                        item.setTitle(R.string.reabrir_caixa);
                        //TODO - Chamar tela com o relatório do caixa
                        return true;
                    case "Reabrir Caixa":
                        item.setTitle(R.string.fechar_caixa);
                        return true;
                }
                return true;
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
}