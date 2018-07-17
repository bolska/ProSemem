package Classes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Fazenda {
    private int id;
    private String nome;
    private String descricao;
    private int encarregadoId;
    private String encarregadoNome;
    private String sigla;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getEncarregadoId() {
        return encarregadoId;
    }

    public void setEncarregadoId(int encarregadoId) {
        this.encarregadoId = encarregadoId;
    }

    public String getEncarregadoNome() {
        return encarregadoNome;
    }

    public void setEncarregadoNome(String encarregado_nome) {
        this.encarregadoNome = encarregado_nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}
