package com.example.android.kanaking;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.kanaking.model.Pedido;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.kanaking.Constantes.CANCELADO;
import static com.example.android.kanaking.Constantes.CARTAO;
import static com.example.android.kanaking.Constantes.DINHEIRO;
import static com.example.android.kanaking.Constantes.LANCADO;
import static com.example.android.kanaking.Constantes.PREPARANDO;
import static com.example.android.kanaking.Constantes.PRONTO;
import static com.example.android.kanaking.Constantes.TERMINADO;

public class PedidoAdapter extends ArrayAdapter<Pedido> {
    private Context mContext;
    private List<Pedido> pedidosList = new ArrayList<>();

    public PedidoAdapter(@NonNull Context context, @LayoutRes ArrayList<Pedido> list) {
        super(context, 0 , list);
        mContext = context;
        pedidosList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.pedido,parent,false);

        Pedido pedidoAtual = pedidosList.get(position);

        TextView name = (TextView)listItem.findViewById(R.id.comanda);
        name.setText(String.valueOf(pedidoAtual.getComanda()));

        TextView valor = (TextView)listItem.findViewById(R.id.valor);
        valor.setText(String.valueOf(pedidoAtual.getValor()));

        ImageView tipoPagamento = (ImageView)listItem.findViewById(R.id.tipo_pagamento);
        switch(pedidoAtual.getFormaPagamento()){
            case CARTAO:
                tipoPagamento.setImageResource(R.drawable.cartao);
                break;
            case DINHEIRO:
                tipoPagamento.setImageResource(R.drawable.dinheiro);
        }


        LinearLayout fundo = (LinearLayout)listItem.findViewById(R.id.fundo);
        switch(pedidoAtual.getEstado()){
            case LANCADO://cores escolhidas com a ajuda de http://erikasarti.com/html/tabela-cores/
                fundo.setBackgroundColor(Color.parseColor("#0000FF"));//RGB(0,0,255) Blue
                break;
            case PREPARANDO:
                fundo.setBackgroundColor(Color.parseColor("#FF8C00"));//RGB(255,140,0) DarkOrange
                break;
            case PRONTO:
                fundo.setBackgroundColor(Color.parseColor("#00FF00"));//RGB(0,255,0) Lime
                break;
            case CANCELADO:
                fundo.setBackgroundColor(Color.parseColor("#E9967A"));//RGB	(255,160,122) LightSalmon
                break;
            case TERMINADO:
                fundo.setBackgroundColor(Color.parseColor("#696969"));//RGB	(105,105,105) DimGray
                break;
        }


        return listItem;
    }
}
