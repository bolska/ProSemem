package Classes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Compromisso {
    private SimpleIntegerProperty id;
    private SimpleStringProperty descricao;
    private SimpleStringProperty tipo;
    private SimpleStringProperty cor;

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

    public String getTipo() {
        return tipo.getValue();
    }

    public void setTipo(String tipo) {
        this.tipo = new SimpleStringProperty(tipo);
    }

    public String getCor() {
        return cor.getValue();
    }

    public void setCor(String cor) {
        this.cor = new SimpleStringProperty(cor);
    }
}
