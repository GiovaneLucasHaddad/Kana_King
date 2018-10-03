package com.example.android.kanaking.model;

public class Pedido {
    private long id;
    private int venda;
    private int comanda;
    private int estado;
    private double valor;
    private int formaPagamento;
    private int data;
    private int hora;

    public Pedido() {
    }

    public Pedido(long id, int venda, int comanda, int estado, double valor, int formaPagamento, int data, int hora) {
        this.id = id;
        this.venda = venda;
        this.comanda = comanda;
        this.estado = estado;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.data = data;
        this.hora = hora;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVenda() {
        return venda;
    }

    public void setVenda(int venda) {
        this.venda = venda;
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

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }
}