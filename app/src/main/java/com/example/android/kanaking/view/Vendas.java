package com.example.android.kanaking.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.kanaking.Pagamento;
import com.example.android.kanaking.PagamentoAdapter;
import com.example.android.kanaking.PedidoAdapter;
import com.example.android.kanaking.R;
import com.example.android.kanaking.model.ItemPedido;
import com.example.android.kanaking.model.Pedido;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static com.example.android.kanaking.Constantes.ABACAXI;
import static com.example.android.kanaking.Constantes.COCO;
import static com.example.android.kanaking.Constantes.COCO_FRUTA;
import static com.example.android.kanaking.Constantes.CONCLUIR;
import static com.example.android.kanaking.Constantes.COPO_300;
import static com.example.android.kanaking.Constantes.COPO_400;
import static com.example.android.kanaking.Constantes.COPO_500;
import static com.example.android.kanaking.Constantes.GARRAFA_1000;
import static com.example.android.kanaking.Constantes.GARRAFA_500;
import static com.example.android.kanaking.Constantes.GENGIBRE;
import static com.example.android.kanaking.Constantes.NENHUMA;
import static com.example.android.kanaking.Constantes.OBSERVACAO;
import static com.example.android.kanaking.Constantes.POUCO_GELO;
import static com.example.android.kanaking.Constantes.PURO;
import static com.example.android.kanaking.Constantes.QUANTIDADE;
import static com.example.android.kanaking.Constantes.RECIPIENTE;
import static com.example.android.kanaking.Constantes.SABOR;
import static com.example.android.kanaking.Constantes.SEM_GELO;
import static com.example.android.kanaking.Constantes.SICILIANO;
import static com.example.android.kanaking.Constantes.TAITI;

public class Vendas extends AppCompatActivity{
    private ListView listView;
    private NumberPicker comanda;
    private PedidoAdapter pedidoAdapter;
    private ArrayList<Pedido> pedidosList;
    private Spinner pagamento;
    private PagamentoAdapter pagtoAdapter;

    //Item de Pedido temporário
    private ItemPedido itemAux;
    private int etapa = 0;
    private Double soma = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendas);

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
    }

    public void adicionar(View view){
        TextView sValor, sEstado;
        sValor = (EditText)findViewById(R.id.add_valor);
        sEstado = (EditText)findViewById(R.id.add_estado);

        int imgPagto = pagamento.getSelectedItemPosition();

        pedidosList.add(0,new Pedido(1,1,comanda.getValue(),Integer.valueOf(sEstado.getText().toString()),Double.valueOf(sValor.getText().toString()),imgPagto,21092018,1613));
        pedidoAdapter.notifyDataSetChanged();
    }

    public void zerar(View view){
        Toast.makeText(Vendas.this,"Zerar",Toast.LENGTH_SHORT).show();
    }

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
                                //TODO - aqui o ItemPedido está pronto para ser lançado
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
}
