package com.example.android.kanaking.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.kanaking.R;
import com.example.android.kanaking.model.ItemPedido;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.kanaking.Constantes.NENHUMA;
import static com.example.android.kanaking.Constantes.POUCO_GELO;
import static com.example.android.kanaking.Constantes.SEM_GELO;
import static com.example.android.kanaking.Constantes.SIM;

public class ItemPedidoAdapter extends BaseAdapter {

    private Context context;
    private List<ItemPedido> listaItens;

    public ItemPedidoAdapter(Context c, ArrayList<ItemPedido> lista){
        context = c;
        listaItens = lista;
    }

    @Override
    public int getCount() {
        return listaItens.size();
    }

    @Override
    public Object getItem(int position) {
        return listaItens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  listaItens.get(position).getSequencia();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ItemPedido itemAtual = listaItens.get(position);

        if (convertView == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_pedido,parent,false);

        view.setTag(itemAtual);

        ImageView img = view.findViewById(R.id.sabor);
        img.setImageResource(itemAtual.getImgSaborRec());

        if (itemAtual.getEntregue() == SIM){
            img = view.findViewById(R.id.entregue);
            img.setImageResource(R.drawable.confirmar);

            LinearLayout fundo = view.findViewById(R.id.fundo_item);
            fundo.setBackgroundResource(R.color.corTerminado);
        }

        img = view.findViewById(R.id.observacao);
        switch (itemAtual.getObservacao()){
            case NENHUMA:
                img.setImageResource(0);
                break;
            case POUCO_GELO:
                img.setImageResource(R.drawable.gelo);
                break;
            case SEM_GELO:
                img.setImageResource(R.drawable.sem_gelo);
        }

        TextView recipiente = view.findViewById(R.id.recipiente);
        recipiente.setText(itemAtual.getRecipienteDescricao());

        TextView quantidade = view.findViewById(R.id.quantidade);
        quantidade.setText(itemAtual.getQuantidadeDescricao());


        return view;
    }
}
