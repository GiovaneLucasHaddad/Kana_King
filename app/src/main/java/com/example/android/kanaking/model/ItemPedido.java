package com.example.android.kanaking.model;

import com.example.android.kanaking.R;

import static com.example.android.kanaking.Constantes.ABACAXI;
import static com.example.android.kanaking.Constantes.CANCELAR;
import static com.example.android.kanaking.Constantes.COCO;
import static com.example.android.kanaking.Constantes.COCO_FRUTA;
import static com.example.android.kanaking.Constantes.COPO_300;
import static com.example.android.kanaking.Constantes.COPO_400;
import static com.example.android.kanaking.Constantes.COPO_500;
import static com.example.android.kanaking.Constantes.GARRAFA_1000;
import static com.example.android.kanaking.Constantes.GARRAFA_500;
import static com.example.android.kanaking.Constantes.GENGIBRE;
import static com.example.android.kanaking.Constantes.NENHUMA;
import static com.example.android.kanaking.Constantes.POUCO_GELO;
import static com.example.android.kanaking.Constantes.PRECOS;
import static com.example.android.kanaking.Constantes.PURO;
import static com.example.android.kanaking.Constantes.QTD_RECIPIENTE;
import static com.example.android.kanaking.Constantes.SEM_GELO;
import static com.example.android.kanaking.Constantes.SICILIANO;
import static com.example.android.kanaking.Constantes.TAITI;

public class ItemPedido {
    private long id;
    private int sequencia;
    private int sabor;
    private int recipiente;
    private int quantidade;
    private int entregue;
    private int observacao;
    private Pedido pedido;

    public ItemPedido() {
        quantidade = 1;
        entregue = 0;
    }

    public Double calcSoma (){
        return (quantidade* PRECOS[recipiente]);
    }
    public int calcQtdRecipiente (){
        return (quantidade* QTD_RECIPIENTE[recipiente]);
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

    public int getConstSabor(int idSabor){
        switch (idSabor){
            case R.id.siciliano:
                return SICILIANO;
            case R.id.taiti:
                return TAITI;
            case R.id.abacaxi:
                return ABACAXI;
            case R.id.puro:
                return PURO;
            case R.id.gengibre:
                return GENGIBRE;
            case R.id.coco:
                return COCO;
        }
        return CANCELAR;
    }

    public int getConstRecipiente(int idRecipiente){
        switch (idRecipiente){
            case R.id.c_fruta:
                return COCO_FRUTA;
            case R.id.c_300:
                return COPO_300;
            case R.id.c_400:
                return COPO_400;
            case R.id.c_500:
                return COPO_500;
            case R.id.g_500:
                return GARRAFA_500;
            case R.id.g_1000:
                return GARRAFA_1000;
        }
        return CANCELAR;
    }

    public int getIconeSabor(int recipiente){
        switch(sabor){
            case TAITI:
                if (recipiente < GARRAFA_500){
                    return R.drawable.t_c;
                }else{
                    return R.drawable.t_g;
                }
            case SICILIANO:
                if (recipiente < GARRAFA_500){
                    return R.drawable.s_c;
                }else{
                    return R.drawable.s_g;
                }
            case ABACAXI:
                if (recipiente < GARRAFA_500){
                    return R.drawable.a_c;
                }else{
                    return R.drawable.a_g;
                }
            case PURO:
                if (recipiente < GARRAFA_500){
                    return R.drawable.p_c;
                }else{
                    return R.drawable.p_g;
                }
            case GENGIBRE:
                if (recipiente < GARRAFA_500){
                    return R.drawable.g_c;
                }else{
                    return R.drawable.g_g;
                }
        }
        return 0;
    }

    public String getObservacaoDescricao(){
        switch(observacao){
            case SEM_GELO:
                return "Sem Gelo";

            case POUCO_GELO:
                return "Pouco Gelo";
        }
        return "";
    }

    public int getQtdQuantidadeId(int idQuantidade){
        switch (idQuantidade) {
            case R.id.qtd_1:
                return 1;
            case R.id.qtd_2:
                return 2;
            case R.id.qtd_3:
                return 3;
            case R.id.qtd_4:
                return 4;
            case R.id.qtd_5:
                return 5;
            case R.id.qtd_6:
                return 6;
            case R.id.qtd_7:
                return 7;
            case R.id.qtd_8:
                return 8;
            case R.id.qtd_9:
                return 9;
            case R.id.qtd_10:
                return 10;
        }
        return quantidade;
    }

    public int getObsObservacaoId(int idObs){
        switch (idObs) {
            case R.id.sem_gelo:
                return SEM_GELO;
            case R.id.pouco_gelo:
                return POUCO_GELO;
            case R.id.nenhuma:
                return NENHUMA;
        }
        return observacao;
    }

    public String getQuantidadeDescricao(){
        return "x" + quantidade;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSequencia() {
        return sequencia;
    }

    public void setSequencia(int sequencia) {
        this.sequencia = sequencia;
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
