package com.example.android.kanaking.model;

import android.widget.Toast;

import java.util.ArrayList;

import static com.example.android.kanaking.Constantes.ABACAXI;
import static com.example.android.kanaking.Constantes.GENGIBRE;
import static com.example.android.kanaking.Constantes.PURO;
import static com.example.android.kanaking.Constantes.SICILIANO;
import static com.example.android.kanaking.Constantes.TAITI;

public class Pedido {
    private long id;
    private long venda;
    private int comanda;
    private int estado;
    private double valor;
    private int formaPagamento;
    private String data;
    private String hora;
    private ArrayList<ItemPedido> itemPedidos;
    private Caixa caixa;

    public Pedido() {
        id = 0;
        venda = 0;
        comanda = 0;
        estado = 0;
        valor = 0.0;
        formaPagamento = 0;
        data = "";
        hora = "";
        itemPedidos = new ArrayList<>();
        caixa = null;
    }

    public Pedido(long id, long venda, int comanda, int estado, double valor, int formaPagamento, String data, String hora, ArrayList<ItemPedido> itens) {
        this.id = id;
        this.venda = venda;
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

    public int getQtdTotal(int sabor){
        int qtde = 0;
        if(itemPedidos.size()>0) {
            for (int cont = 0; cont < itemPedidos.size(); cont++){
                if(itemPedidos.get(cont).getSabor() == sabor){
                    qtde = itemPedidos.get(cont).calcQtdRecipiente();
                }
            }
        }
        return qtde;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVenda() {
        return venda;
    }

    public void setVenda(long venda) {
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