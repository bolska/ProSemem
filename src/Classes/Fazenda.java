package Classes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Fazenda {
    private SimpleIntegerProperty id;
    private SimpleStringProperty nome;
    private SimpleStringProperty descricao;
    private SimpleIntegerProperty encarregadoId;
    private SimpleStringProperty encarregadoNome;
    private SimpleStringProperty sigla;

    public int getId() {
        return id.getValue();
    }

    public void setId(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    public String getNome() {
        return nome.getValue();
    }

    public void setNome(String nome) {
        this.nome = new SimpleStringProperty(nome);
    }

    public String getDescricao() {
        return descricao.getValue();
    }

    public void setDescricao(String descricao) {
        this.descricao = new SimpleStringProperty(descricao);
    }

    public int getEncarregadoId() {
        return encarregadoId.getValue();
    }

    public void setEncarregadoId(int encarregadoId) {
        this.encarregadoId = new SimpleIntegerProperty(encarregadoId);
    }

    public String getEncarregadoNome() {
        return encarregadoNome.getValue();
    }

    public void setEncarregadoNome(String encarregado_nome) {
        this.encarregadoNome = new SimpleStringProperty(encarregado_nome);
    }

    public String getSigla() {
        return sigla.getValue();
    }

    public void setSigla(String sigla) {
        this.sigla = new SimpleStringProperty(sigla);
    }
}
