/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DescricaoEvento;

import BancoDeDados.DaoAtividade;
import BancoDeDados.DaoCompromisso;
import BancoDeDados.DaoEvento;
import BancoDeDados.DaoFazenda;
import BancoDeDados.DaoSessao;
import Classes.Atividade;
import Classes.Compromisso;
import Classes.Evento;
import Classes.Fazenda;
import Classes.Sessao;
import Model.Modelo;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 *
 * @author rodrigo
 */
public class ListaEventoController implements Initializable {
    
    @FXML
    private VBox eventoBox;
    
    @FXML
    public void fecharJanela(MouseEvent e) {
       Modelo.getInstance().popup.hide();
       e.consume();
    }
    
    private void startListEvents() {
        DaoEvento daoEvt = new DaoEvento();
        DaoSessao daoSessao = new DaoSessao();
        DaoFazenda daoFazenda = new DaoFazenda();
        DaoAtividade daoAtividade = new DaoAtividade();
        DaoCompromisso daoCompromisso = new DaoCompromisso();
        
        ObservableList<Evento> listEvento = daoEvt.listByData(Date.valueOf(Modelo.getInstance().dataDiaEvento));
        
        StringBuilder colorString = new StringBuilder();

        for(Evento evento : listEvento) {
            Sessao sessao = daoSessao.getSessaoByEventoId(evento.getId());
            Fazenda fazenda = daoFazenda.getFazendaById(sessao.getFazendaId());
            Atividade atividade = daoAtividade.getAtividadeById(evento.getAtividadeId());
            Compromisso compromisso = daoCompromisso.getCompromissoById(evento.getCompromissoId());
            
            Label labelEvento = new Label();
            labelEvento.getStylesheets().add("CSS/CalendarioCSS.css");
            labelEvento.getStyleClass().add("Label-atividade");
            labelEvento.setMaxWidth(Double.MAX_VALUE);
            labelEvento.setId(Integer.toString(evento.getId()));
            
            if(evento.getSessaoId() != null) {
                labelEvento.setText(evento.getSessaoId() + " - " + fazenda.getSigla().toUpperCase() + " - " + atividade.getDescricao());
                colorString.append("-fx-background-color: #");
                colorString.append(sessao.getCor().substring(2,8)).append(";\n");
            } else {
                labelEvento.setText(compromisso.getDescricao());
                colorString.append("-fx-background-color: #");
                colorString.append(compromisso.getCor().substring(2,8)).append(";\n");
            }
            
            labelEvento.setStyle(colorString.toString());
            
            eventoBox.getChildren().add(labelEvento);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startListEvents();
    }
    
}
