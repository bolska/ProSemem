/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import BancoDeDados.DaoEncarregado;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author user
 */
public class FazendaProperty {
    private SimpleIntegerProperty id;
    private SimpleStringProperty nome;
    private SimpleStringProperty descricao;
    private SimpleIntegerProperty encarregadoId;
    private SimpleStringProperty encarregadoNome;
    private SimpleStringProperty sigla;
    private Fazenda fazenda;
    
    public FazendaProperty(Fazenda f) {
        this.fazenda = f;
        this.id = new SimpleIntegerProperty(f.getId());
        this.nome = new SimpleStringProperty(f.getNome());
        this.encarregadoId = new SimpleIntegerProperty(f.getEncarregadoId());
        this.encarregadoNome = new SimpleStringProperty (f.getEncarregadoNome());
        this.sigla = new SimpleStringProperty (f.getSigla());
    }
    
    public int getId() {
        return id.getValue();
    }
    
    public String getNome() {
        return nome.getValue();
    }
    
    public String getDescricao() {
        return descricao.getValue();
    }
    
    public Fazenda getFazenda() {
        return fazenda;
    }
    
    public int getEncarregadoId() {
        return encarregadoId.getValue();
    }
    
    public String getEncarregadoNome(){
        return this.encarregadoNome.getValue();
    }
    
    public String getSigla(){
        return this.sigla.getValue();
    }
    
    public Encarregado getEncarregado(){
        Encarregado encarregado = new Encarregado();
        encarregado.setId(encarregadoId.getValue());
        encarregado.setNome(encarregadoNome.getValue());
        return encarregado;
    }
    
    public void setEncarregadoNome(String nome){
        this.encarregadoNome.setValue(nome);
    }
    
    public void setNomeProperty(String nome) {
        this.nome = new SimpleStringProperty(nome);
        this.fazenda.setNome(nome);
    }
    
    public void setDescricaoProperty(String descricao) {
        this.descricao = new SimpleStringProperty(descricao);
        this.fazenda.setDescricao(descricao);
    }
    
    public void setSiglaProperty(String sigla){
        this.sigla = new SimpleStringProperty(sigla);
        this.fazenda.setSigla(sigla);
    }
}
