package com.example.android.kanaking.model;

import com.example.android.kanaking.R;

import static com.example.android.kanaking.Constantes.ABACAXI;
import static com.example.android.kanaking.Constantes.COCO;
import static com.example.android.kanaking.Constantes.COCO_FRUTA;
import static com.example.android.kanaking.Constantes.COPO_300;
import static com.example.android.kanaking.Constantes.COPO_400;
import static com.example.android.kanaking.Constantes.COPO_500;
import static com.example.android.kanaking.Constantes.GARRAFA_1000;
import static com.example.android.kanaking.Constantes.GARRAFA_500;
import static com.example.android.kanaking.Constantes.GENGIBRE;
import static com.example.android.kanaking.Constantes.PURO;
import static com.example.android.kanaking.Constantes.SICILIANO;
import static com.example.android.kanaking.Constantes.TAITI;

public class ItemPedido {
    private long id;
    private long idPedido;
    private int sabor;
    private int recipiente;
    private int quantidade;
    private int entregue;
    private int ordem;
    private int observacao;
    private Pedido pedido;

    public ItemPedido() {
        quantidade = 1;
    }

    public int getImgSaborRec(){
        int num_drawable = -1;
        switch(sabor){
            case COCO:
                switch(recipiente) {
                    case COCO_FRUTA:
                        num_drawable = R.drawable.c;
                        break;
                    case COPO_500:
                        num_drawable = R.drawable.c_c;
                        break;
                    case GARRAFA_500:
                        num_drawable = R.drawable.c_g;
                        break;
                    case GARRAFA_1000:
                        num_drawable = R.drawable.c_g;
                        break;
                }
                break;
            case SICILIANO:
                switch(recipiente) {
                    case COPO_300:
                        num_drawable = R.drawable.s_c;
                        break;
                    case COPO_400:
                        num_drawable = R.drawable.s_c;
                        break;
                    case COPO_500:
                        num_drawable = R.drawable.s_c;
                        break;
                    case GARRAFA_500:
                        num_drawable = R.drawable.s_g;
                        break;
                    case GARRAFA_1000:
                        num_drawable = R.drawable.s_g;
                        break;
                }
                break;

            case TAITI:
                switch(recipiente) {
                    case COPO_300:
                        num_drawable = R.drawable.t_c;
                        break;
                    case COPO_400:
                        num_drawable = R.drawable.t_c;
                        break;
                    case COPO_500:
                        num_drawable = R.drawable.t_c;
                        break;
                    case GARRAFA_500:
                        num_drawable = R.drawable.t_g;
                        break;
                    case GARRAFA_1000:
                        num_drawable = R.drawable.t_g;
                        break;
                }
                break;

            case ABACAXI:
                switch(recipiente) {
                    case COPO_300:
                        num_drawable = R.drawable.a_c;
                        break;
                    case COPO_400:
                        num_drawable = R.drawable.a_c;
                        break;
                    case COPO_500:
                        num_drawable = R.drawable.a_c;
                        break;
                    case GARRAFA_500:
                        num_drawable = R.drawable.a_g;
                        break;
                    case GARRAFA_1000:
                        num_drawable = R.drawable.a_g;
                        break;
                }
                break;

            case PURO:
                switch(recipiente) {
                    case COPO_300:
                        num_drawable = R.drawable.p_c;
                        break;
                    case COPO_400:
                        num_drawable = R.drawable.p_c;
                        break;
                    case COPO_500:
                        num_drawable = R.drawable.p_c;
                        break;
                    case GARRAFA_500:
                        num_drawable = R.drawable.p_g;
                        break;
                    case GARRAFA_1000:
                        num_drawable = R.drawable.p_g;
                        break;
                }
                break;

            case GENGIBRE:
                switch(recipiente) {
                    case COPO_300:
                        num_drawable = R.drawable.g_c;
                        break;
                    case COPO_400:
                        num_drawable = R.drawable.g_c;
                        break;
                    case COPO_500:
                        num_drawable = R.drawable.g_c;
                        break;
                    case GARRAFA_500:
                        num_drawable = R.drawable.g_g;
                        break;
                    case GARRAFA_1000:
                        num_drawable = R.drawable.g_g;
                        break;
                }
                break;
        }
        return num_drawable;
    }

    public String getRecipienteDescricao(){
        String descricaoRecipiente = "";
        switch(recipiente){
            case COCO_FRUTA:
                break;
            case COPO_300:
                descricaoRecipiente = "300";
                break;
            case COPO_400:
                descricaoRecipiente = "400";
                break;
            case COPO_500:
                descricaoRecipiente = "500";
                break;
            case GARRAFA_500:
                descricaoRecipiente = "500";
                break;
            case GARRAFA_1000:
                descricaoRecipiente = "1L";
                break;
        }
        return descricaoRecipiente;
    }

    public String getQuantidadeDescricao(){
        return "X " + quantidade;
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

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}
