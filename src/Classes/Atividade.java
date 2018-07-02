package Classes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Atividade {
    private SimpleIntegerProperty id;
    private SimpleStringProperty descricao;
    private SimpleIntegerProperty intervalo;
    private SimpleStringProperty tipo;    //"P" = Principal, "I" = Importante, "S" = Secundário
    private SimpleStringProperty observacao;
    private SimpleIntegerProperty protocoloId;

    public int getId() {
        return id.getValue();
    }

    public void setId(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    public String getDescricao() {
        return descricao.getValue();
    }

    public void setDescricao(String descricao) {
        this.descricao = new SimpleStringProperty(descricao);
    }

    public int getIntervalo() {
        return intervalo.getValue();
    }

    public void setIntervalo(int intervalo) {
        this.intervalo = new SimpleIntegerProperty(intervalo);
    }

    public String getTipo() {
        return tipo.getValue();
    }

    public void setTipo(String tipo) {
        this.tipo = new SimpleStringProperty(tipo);
    }

    public String getObs() {
        return observacao.getValue();
    }

    public void setObs(String obs) {
        this.observacao = new SimpleStringProperty(obs);
    }

    public int getProtocoloId() {
        return protocoloId.getValue();
    }

    public void setProtocoloId(int protocoloId) {
        this.protocoloId = new SimpleIntegerProperty(protocoloId);
    }
}
