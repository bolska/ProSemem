/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import BancoDeDados.DaoFazenda;
import BancoDeDados.DaoProtocolo;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author user
 */
public class SessaoProperty {
    private SimpleStringProperty id;
    private SimpleStringProperty estado;
    private SimpleStringProperty protocoloDescricao;
    private SimpleStringProperty cor;
    private Sessao sessao;
    private Protocolo protocolo; 
    private Fazenda fazenda;
    
    public SessaoProperty(Sessao s) {
        DaoProtocolo daoP = new DaoProtocolo();
        DaoFazenda daoF = new DaoFazenda();
        
        protocolo = daoP.getProtocoloById(s.getProtocoloId());
        fazenda = daoF.getFazendaById(s.getFazendaId());
        
        this.sessao = s;
        this.estado = new SimpleStringProperty(s.getEstado());
        this.id = new SimpleStringProperty(s.getId());
        this.protocoloDescricao = new SimpleStringProperty(protocolo.getDescricao());
        this.cor = new SimpleStringProperty(s.getCor());
    }
    
    public String getId() {
        return id.getValue();
    }
    
    public Sessao getSessao() {
        return sessao;
    }
    
    public Protocolo getProtocolo(){
        return protocolo;
    }
    
    public Fazenda getFazenda(){
        return fazenda;
    }
    
    public String getProtocoloDescricao(){
        return protocolo.getDescricao();
    }
}
