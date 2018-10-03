package com.example.android.kanaking.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android.kanaking.R;
import com.example.android.kanaking.model.Pedido;

import static com.example.android.kanaking.Constantes.CAIXA;
import static com.example.android.kanaking.Constantes.LANCADO;
import static com.example.android.kanaking.Constantes.MOENDA;

public class Entrada extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrada);
    }

    public void entrarCaixa(View view){chamarIntent(CAIXA);}
    public void entrarMoenda(View view){chamarIntent(MOENDA);}

    public void chamarIntent(String modo){
//        Pedido pedido = new Pedido(1,1,1,LANCADO,0.0,1,11092018,022740);
//        Toast.makeText(this,"Pedido " + pedido.getId()+pedido.getVenda()+pedido.getComanda()+pedido.getValor()+pedido.getEstado()+pedido.getValor()+pedido.getFormaPagamento()+pedido.getData()+pedido.getHora(), Toast.LENGTH_SHORT).show();
        Intent telaVendas = new Intent(this,Vendas.class);
        telaVendas.putExtra("MODO",modo);
        startActivity(telaVendas);

    }
}
