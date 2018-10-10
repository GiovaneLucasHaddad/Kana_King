package com.example.android.kanaking.model;

import java.util.ArrayList;

public class Caixa {
    private long numero;
    private String dataAbertura;
    private String horaAbertura;
    private String dataFechamento;
    private String horaFechamento;
    private ArrayList<Pedido> pedidos;

    public Caixa(){
        pedidos = new ArrayList<>();
        dataFechamento = "";
        horaFechamento = "";
    }

    public void addPedido(Pedido pedido){
        pedidos.add(pedido);
        pedido.setCaixa(this);
    }

    public boolean isAberto(){
        return dataFechamento.equals("");
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public String getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(String dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public String getHoraAbertura() {
        return horaAbertura;
    }

    public void setHoraAbertura(String horaAbertura) {
        this.horaAbertura = horaAbertura;
    }

    public String getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(String dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public String getHoraFechamento() {
        return horaFechamento;
    }

    public void setHoraFechamento(String horaFechamento) {
        this.horaFechamento = horaFechamento;
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(ArrayList<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}
