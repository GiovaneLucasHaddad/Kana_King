package com.example.android.kanaking.model;

import java.util.ArrayList;

public class Caixa {
    private long id;
    private long numero;
    private Double fundo;
    private String dataAbertura;
    private String horaAbertura;
    private String dataFechamento;
    private String horaFechamento;
    private ArrayList<Pedido> pedidos;

    public Caixa(){
        id = 0;
        numero = 0;
        fundo = -1.0;
        dataAbertura = "";
        horaAbertura = "";
        dataFechamento = "";
        horaFechamento = "";
        pedidos = new ArrayList<>();
    }

    public void addPedido(Pedido pedido){
        pedidos.add(pedido);
        pedido.setCaixa(this);
    }

    public boolean isAberto(){
        return dataFechamento.equals("");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getFundo() {
        return fundo;
    }

    public void setFundo(Double fundo) {
        this.fundo = fundo;
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
