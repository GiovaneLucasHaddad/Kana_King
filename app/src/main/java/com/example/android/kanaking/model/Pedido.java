package com.example.android.kanaking.model;

import java.util.ArrayList;

import static com.example.android.kanaking.Constantes.APAGANDO;
import static com.example.android.kanaking.Constantes.CANCELADO;
import static com.example.android.kanaking.Constantes.LANCADO;
import static com.example.android.kanaking.Constantes.NAO;
import static com.example.android.kanaking.Constantes.PREPARANDO;
import static com.example.android.kanaking.Constantes.PRONTO;
import static com.example.android.kanaking.Constantes.TERMINADO;

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

    public Pedido(Pedido pedido){
        id = pedido.id;
        venda = pedido.venda;
        comanda = pedido.comanda;
        estado = pedido.estado;
        valor = pedido.valor;
        formaPagamento = pedido.formaPagamento;
        data = pedido.data;
        hora = pedido.hora;
        itemPedidos = pedido.itemPedidos;
        caixa = pedido.caixa;
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
    public boolean somaMoenda(){
        switch(estado){
            case LANCADO:
                return true;
            case PREPARANDO:
                return true;
            case PRONTO:
                return true;
            case CANCELADO:
                return false;
            case TERMINADO:
                return false;
            case APAGANDO:
                return false;
        }
        return false;
    }

    public boolean todosItensEntregues(){
        if(itemPedidos == null) {
            return false;
        }
        for(int cont = 0; cont < itemPedidos.size(); cont++){
            if(itemPedidos.get(cont).getEntregue() == NAO){
                return false;
            }
        }
        return true;
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

    public void setItemPedidos(ArrayList<ItemPedido> itemPedidos) {
        this.itemPedidos = itemPedidos;
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }
}