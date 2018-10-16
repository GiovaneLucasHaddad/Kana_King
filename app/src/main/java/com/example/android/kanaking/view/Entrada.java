package com.example.android.kanaking.view;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android.kanaking.R;

import static com.example.android.kanaking.Constantes.CAIXA;
import static com.example.android.kanaking.Constantes.MOENDA;

public class Entrada extends AppCompatActivity {

    private static final int SOLICITACAO_ATIVACAO = 3;
    private BluetoothAdapter bluetoothAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrada);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth não disponível", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, SOLICITACAO_ATIVACAO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SOLICITACAO_ATIVACAO) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this,"Bluetooth Ativado",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"Bluetooth Não Ativado",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void entrarCaixa(View view){chamarIntent(CAIXA);}
    public void entrarMoenda(View view){chamarIntent(MOENDA);}

    public void chamarIntent(String modo){
        Intent telaVendas = new Intent(this,Vendas.class);
        telaVendas.putExtra("MODO",modo);
        startActivity(telaVendas);

    }
}
