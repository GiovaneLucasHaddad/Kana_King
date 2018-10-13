package com.example.android.kanaking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.kanaking.model.ItemPedido;

import java.util.ArrayList;
import java.util.List;

public class ItemPedidoAdapter extends BaseAdapter {

    private Context context;
    private List<ItemPedido> listaItens = new ArrayList<>();

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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ItemPedido itemAtual = listaItens.get(position);

        if (convertView == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_pedido,parent,false);

        ImageView img = view.findViewById(R.id.sabor);
        img.setImageResource(itemAtual.getImgSaborRec());

        if (itemAtual.getEntregue() == 1){
            img = view.findViewById(R.id.entregue);
            img.setImageResource(R.drawable.confirmar);
        }

        TextView recipiente = view.findViewById(R.id.recipiente);
        recipiente.setText(itemAtual.getRecipienteDescricao());

        TextView quantidade = view.findViewById(R.id.quantidade);
        quantidade.setText(itemAtual.getQuantidadeDescricao());

//            imageView.setImageResource(lista[position]);
//            imageView.setAdjustViewBounds(true);
////            imageView.setLayoutParams(new ViewGroup.LayoutParams(85, 85));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(8, 8, 8, 8);
        return view;
    }
}
