package com.example.android.kanaking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.kanaking.model.Pedido;
import com.example.android.kanaking.view.Vendas;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.kanaking.Constantes.CAIXA;
import static com.example.android.kanaking.Constantes.CANCELADO;
import static com.example.android.kanaking.Constantes.CARTAO;
import static com.example.android.kanaking.Constantes.DINHEIRO;
import static com.example.android.kanaking.Constantes.LANCADO;
import static com.example.android.kanaking.Constantes.MOENDA;
import static com.example.android.kanaking.Constantes.PREPARANDO;
import static com.example.android.kanaking.Constantes.PRONTO;
import static com.example.android.kanaking.Constantes.TERMINADO;

public class PedidoAdapter extends BaseAdapter {
    private Context mContext;
    private List<Pedido> listaPedidos = new ArrayList<>();

    public PedidoAdapter(@NonNull Context context,  ArrayList<Pedido> list) {
        this.mContext = context;
        this.listaPedidos = list;
    }

    @Override
    public int getCount() {
        return listaPedidos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaPedidos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaPedidos.get(position).getVenda();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        final Pedido pedidoAtual = listaPedidos.get(position);

        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.pedido,parent,false);

        TextView comanda = (TextView)listItem.findViewById(R.id.comanda);
        comanda.setText(String.valueOf(pedidoAtual.getComanda()));
        comanda.setTag(pedidoAtual);

        //Configurando GridView
        GridView itemGrid = listItem.findViewById(R.id.itens);
        ItemPedidoAdapter itemAdapter = new ItemPedidoAdapter(mContext,pedidoAtual.getItemPedidos());
        itemGrid.setAdapter(itemAdapter);


        itemGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, "Clique longo",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        itemGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, "Clique simples",Toast.LENGTH_SHORT).show();
            }

        });

        if(Vendas.MODO.equals(CAIXA)){
            TextView valor = (TextView)listItem.findViewById(R.id.valor);
            DecimalFormat df = new DecimalFormat(",##0.00");
            valor.setText(df.format(pedidoAtual.getValor()));

            ImageView tipoPagamento = (ImageView)listItem.findViewById(R.id.tipo_pagamento);
            switch(pedidoAtual.getFormaPagamento()){
                case CARTAO:
                    tipoPagamento.setImageResource(R.drawable.cartao);
                    break;
                case DINHEIRO:
                    tipoPagamento.setImageResource(R.drawable.dinheiro);
                    break;
            }

            Button opcoes = listItem.findViewById(R.id.opcoes);
            opcoes.setTag(pedidoAtual);

        }else{
            FrameLayout pagEOpcoes = listItem.findViewById(R.id.pag_e_opcoes);
            pagEOpcoes.setVisibility(View.GONE);
        }



        LinearLayout fundo = (LinearLayout)listItem.findViewById(R.id.fundo);
        switch(pedidoAtual.getEstado()){
            case LANCADO://cores escolhidas com a ajuda de http://erikasarti.com/html/tabela-cores/
                fundo.setBackgroundResource(R.color.corLancado);
                break;
            case PREPARANDO:
                fundo.setBackgroundResource(R.color.corPreparando);
                break;
            case PRONTO:
                fundo.setBackgroundResource(R.color.corPronto);
                break;
            case CANCELADO:
                fundo.setBackgroundResource(R.color.corCancelado);
                break;
            case TERMINADO:
                fundo.setBackgroundResource(R.color.corTerminado);
                break;
        }


        return listItem;
    }
}
