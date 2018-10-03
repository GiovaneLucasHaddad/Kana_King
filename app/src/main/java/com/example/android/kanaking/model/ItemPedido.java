package com.example.android.kanaking.model;

public class ItemPedido {
    private long id;
    private long idPedido;
    private int sabor;
    private int recipiente;
    private int quantidade;
    private int entregue;
    private int ordem;
    private int observacao;

    public ItemPedido() {
        quantidade = 1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(long idPedido) {
        this.idPedido = idPedido;
    }

    public int getSabor() {
        return sabor;
    }

    public void setSabor(int sabor) {
        this.sabor = sabor;
    }

    public int getRecipiente() {
        return recipiente;
    }

    public void setRecipiente(int recipiente) {
        this.recipiente = recipiente;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getEntregue() {
        return entregue;
    }

    public void setEntregue(int entregue) {
        this.entregue = entregue;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public int getObservacao() {
        return observacao;
    }

    public void setObservacao(int observacao) {
        this.observacao = observacao;
    }
}
