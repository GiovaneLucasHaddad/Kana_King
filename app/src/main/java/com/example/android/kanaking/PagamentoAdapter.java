package com.example.android.kanaking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class PagamentoAdapter extends ArrayAdapter<Pagamento>{
    private int groupId;
    public Context context;
    private ArrayList<Pagamento> lista;
    private LayoutInflater inflater;

    public PagamentoAdapter(@NonNull Context context, int groupId, ArrayList<Pagamento> lista) {
        super(context, 0, lista);
        this.lista = lista;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupId = groupId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(groupId,parent,false);

        ImageView img = itemView.findViewById(R.id.icone);
        img.setImageResource(lista.get(position).getIdImagem());

        return itemView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
