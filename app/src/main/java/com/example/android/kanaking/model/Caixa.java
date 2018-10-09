package com.example.android.kanaking.model;

import java.util.ArrayList;

public class Caixa {
    private long numero;
    private int dataAbertura;
    private int horaAbertura;
    private int dataFechamento;
    private int horaFechamento;
    private ArrayList<Pedido> pedidos;

    public void addPedido(Pedido pedido){
        pedidos.add(pedido);
        pedido.setCaixa(this);
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public int getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(int dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public int getHoraAbertura() {
        return horaAbertura;
    }

    public void setHoraAbertura(int horaAbertura) {
        this.horaAbertura = horaAbertura;
    }

    public int getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(int dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public int getHoraFechamento() {
        return horaFechamento;
    }

    public void setHoraFechamento(int horaFechamento) {
        this.horaFechamento = horaFechamento;
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(ArrayList<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}
