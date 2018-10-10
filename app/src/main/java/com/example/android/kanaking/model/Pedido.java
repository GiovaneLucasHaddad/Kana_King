package com.example.android.kanaking.model;

import java.util.ArrayList;

public class Pedido {
    private long id;
    private int comanda;
    private int estado;
    private double valor;
    private int formaPagamento;
    private String data;
    private String hora;
    private ArrayList<ItemPedido> itemPedidos;
    private Caixa caixa;

    public Pedido() {
        itemPedidos = new ArrayList<>();
    }

    public Pedido(long id, int comanda, int estado, double valor, int formaPagamento, String data, String hora, ArrayList<ItemPedido> itens) {
        this.id = id;
        this.comanda = comanda;
        this.estado = estado;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.data = data;
        this.hora = hora;
        this.itemPedidos = new ArrayList<>();
        this.itemPedidos.addAll(itens);
    }

    public void addItemPedido(ItemPedido itemPedido){
        itemPedidos.add(itemPedido);
        itemPedido.setPedido(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getComanda() {
        return comanda;
    }

    public void setComanda(int comanda) {
        this.comanda = comanda;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(int formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public ArrayList<ItemPedido> getItemPedidos() {
        return itemPedidos;
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }
}