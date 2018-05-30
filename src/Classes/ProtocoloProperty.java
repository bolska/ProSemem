/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author user
 */
public class ProtocoloProperty {
    private SimpleIntegerProperty id;
    private SimpleStringProperty descricao;
    private SimpleStringProperty obs;
    private Protocolo protocolo;
    
    public ProtocoloProperty(Protocolo p) {
        this.protocolo = p;
        this.id = new SimpleIntegerProperty(p.getId());
        this.descricao = new SimpleStringProperty(p.getDescricao());
        this.obs = new SimpleStringProperty(p.getObs());
    }
    
    public int getId() {
        return id.getValue();
    }
    
    public String getDescricao(){
        return descricao.getValue();
    }
    
    public Protocolo getProtocolo(){
        return protocolo;
    }
    
    public String getObs(){
        return obs.getValue();
    }
    
    public void setDescricaoProperty(String descr){
        this.descricao = new SimpleStringProperty(descr);
        this.protocolo.setDescricao(descr);
    }   
    
    public void setObsProperty(String obs){
        this.obs = new SimpleStringProperty(obs);
        this.protocolo.setObs(obs);
    }
}
