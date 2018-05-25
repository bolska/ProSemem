/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.time.LocalDate;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Bolska
 */
public class AtividadeProperty {
    private SimpleIntegerProperty id;
    private SimpleStringProperty descricao;
    private SimpleIntegerProperty intervalo;
    private SimpleStringProperty tipo;    //"P" = Principal, "I" = Importante, "S" = Secundário
    private SimpleIntegerProperty protocoloId;
    private Atividade atividade;
    
    private SimpleStringProperty dataGerada;
    
  
    public AtividadeProperty(Atividade a) {
        this.atividade = a;
        descricao = new SimpleStringProperty(a.getDescricao());
        id = new SimpleIntegerProperty(a.getId());
        intervalo = new SimpleIntegerProperty(a.getIntervalo());
        tipo = new SimpleStringProperty(a.getTipo());
        protocoloId = new SimpleIntegerProperty(a.getProtocoloId());
    }

    public int getId(){
        return id.getValue();
    }
    
    public String getDescricao(){
        return descricao.getValue();
    }
    
    public int getIntervalo(){
        return intervalo.getValue();
    }
    
    public String getTipo(){
        return tipo.getValue();
    }
    
    public int getProtocoloId(){
        return protocoloId.getValue();
    }
    
    public Atividade getAtividade(){
        return atividade;
    }
    
    public void setDescricaoProperty(String descricao){
        this.descricao = new SimpleStringProperty(descricao);
        this.atividade.setDescricao(descricao);
    }
    
    public void setIntervaloProperty(int intervalo){
        this.intervalo = new SimpleIntegerProperty(intervalo);
        this.atividade.setIntervalo(intervalo);
    }
    
    public void setTipoProperty(String tipo){
        this.tipo = new SimpleStringProperty(tipo);
        this.atividade.setTipo(tipo);
    }
    
//    public String getDataGerada(){
//        return this.dataGerada.getValue();
//    }
//    
//    public void setDataGeradaProperty(LocalDate data){
//        data = data.plusDays(intervalo.getValue());
//        this.dataGerada = new SimpleStringProperty(data.toString());
//    }
}
