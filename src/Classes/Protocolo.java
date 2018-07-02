package Classes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Protocolo {
    private SimpleIntegerProperty id;
    private SimpleStringProperty descricao;
    private SimpleStringProperty observacao;
    
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

    public String getObs() {
        return observacao.getValue();
    }

    public void setObs(String obs) {
        this.observacao = new SimpleStringProperty(obs);
    }
}
