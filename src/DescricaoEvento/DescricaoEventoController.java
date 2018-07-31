/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DescricaoEvento;

import BancoDeDados.DaoAtividade;
import BancoDeDados.DaoEncarregado;
import BancoDeDados.DaoEvento;
import BancoDeDados.DaoFazenda;
import BancoDeDados.DaoProtocolo;
import BancoDeDados.DaoSessao;
import Classes.Atividade;
import Classes.Encarregado;
import Classes.Evento;
import Classes.Fazenda;
import Classes.Protocolo;
import Classes.Sessao;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import Model.Modelo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author user
 */
public class DescricaoEventoController implements Initializable {
    
    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField textFieldSessao;

    @FXML
    private JFXTextField textFieldProtocolo;

    @FXML
    private JFXTextField textFieldAtividade;

    @FXML
    private JFXTextField textFieldEncarregado;

    @FXML
    private JFXTextField textFieldFazenda;

    @FXML
    private JFXTextArea textAreaProtocoloObs;
    
    @FXML
    private JFXTextArea textAreaAtividadeObs;
    
    @FXML
    private JFXColorPicker colorPicker;
    
    @FXML
    private JFXTextField textFieldTipoAtividade;
    
    @FXML
    private JFXCheckBox checkBoxConfirmado;
    
    private Protocolo protocolo;
    private Atividade atividade;
    
    @FXML
    private void editableTextArea(ActionEvent event) {
        if(protocolo.getObs() == null){
            updateObs(protocolo);
            event.consume();
        }
        else if(!protocolo.getObs().equals(textAreaProtocoloObs.getText())){
            updateObs(protocolo);
            event.consume();
        }
        else if(atividade.getObs() == null){
            updateObs(atividade);
            event.consume();
        }
        else if(!atividade.getObs().equals(textAreaAtividadeObs.getText())){
            updateObs(atividade);
            event.consume();
        }
        else{
            Modelo.getInstance().popup.setAutoHide(false);
            Modelo.getInstance().showAlertErro("Não houve nenhuma alteração no campo Observação.");
            Modelo.getInstance().popup.setAutoHide(true);
            
            event.consume();
        }
    }
    
    @FXML
    private void buttonRemoverEvento(ActionEvent e){
        if(Modelo.getInstance().showAlertConfirm("Tem certeza que quer remover esse evento?")){
            Evento evento = Modelo.getInstance().evento;
            DaoEvento daoE = new DaoEvento();
            daoE.removeEventoById(evento.getId());
            
            Modelo.getMainController().atualizaCalendario();
            e.consume();
        }
        else{
            e.consume();
        }
    }
    
    @FXML
    private void buttonRemoverSessao(ActionEvent e){
        if(Modelo.getInstance().showAlertConfirm("Tem certeza que quer remover essa Sessão? Todos seus eventos"
                + " serão removidos.")){
            Evento evento = Modelo.getInstance().evento;
            DaoSessao daoS = new DaoSessao();
            daoS.removeSessaoById(evento.getSessaoId());
            
            Modelo.getMainController().atualizaCalendario();
            e.consume();
        }
        else{
            e.consume();
        }
    }
    
    public void fecharJanela(MouseEvent e) {
       Modelo.getInstance().popup.hide();
       e.consume();
    }
    
    public void insereLabel() {
        DaoFazenda daoF = new DaoFazenda();
        DaoProtocolo daoP = new DaoProtocolo();
        DaoAtividade daoA = new DaoAtividade();
        DaoEncarregado daoE = new DaoEncarregado();
        DaoSessao daoS = new DaoSessao();
        
        Evento evento = Modelo.getInstance().evento;
        Sessao sessao = daoS.getSessaoById(evento.getSessaoId());
        protocolo = daoP.getProtocoloById(evento.getProtocoloId());
        atividade = daoA.getAtividadeById(evento.getAtividadeId());
        Fazenda fazenda = daoF.getFazendaById(sessao.getFazendaId());
        Encarregado encarregado = daoE.getEncarregadoById(fazenda.getEncarregadoId());
        
        colorPicker.setValue(Color.web(sessao.getCor()));
        colorPicker.setOnAction( (evt) -> {
                    sessao.setCor(colorPicker.getValue().toString());
                    daoS.updateSessaoCor(sessao);
                    Modelo.getMainController().atualizaCalendario();
        });
        
        textFieldSessao.setText(evento.getSessaoId());
        textFieldProtocolo.setText(protocolo.getDescricao());
        textFieldAtividade.setText(atividade.getDescricao());
        textFieldTipoAtividade.setText(atividade.getTipo());
        textFieldFazenda.setText(fazenda.getNome());
        textFieldEncarregado.setText(encarregado.getNome());
        textAreaProtocoloObs.setText(protocolo.getObs());
        textAreaAtividadeObs.setText(atividade.getObs());
        
        if(evento.getConfirmado() == 1){
            checkBoxConfirmado.setSelected(true);
        }
        else{
            checkBoxConfirmado.setSelected(false);
        }

        checkBoxConfirmado.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    evento.setConfirmado(1);
                }
                else{
                    evento.setConfirmado(0);
                }
                
                DaoEvento daoE = new DaoEvento();
                daoE.updateConfirmadoEvento(evento);
                Modelo.getMainController().atualizaCalendario();
            }
        });
    }
    
    private void setEditableFalse(){
        textFieldSessao.setEditable(false);
        textFieldProtocolo.setEditable(false);
        textFieldAtividade.setEditable(false);
        textFieldFazenda.setEditable(false);
        textFieldEncarregado.setEditable(false);
        textFieldTipoAtividade.setEditable(false);
    }
    
    private void updateObs(Protocolo prot){
        prot.setObs(textAreaProtocoloObs.getText());    
        DaoProtocolo daoP = new DaoProtocolo();
        daoP.atualizaProtocolo(prot);
    }
    
    private void updateObs(Atividade atv){
        atv.setObs(textAreaAtividadeObs.getText());
        DaoAtividade daoA = new DaoAtividade();
        daoA.atualizaAtividade(atv);
    }
    
    private void updateObsEnter() {
        if(protocolo.getObs() == null){
            updateObs(protocolo);
        }
        else if(!protocolo.getObs().equals(textAreaProtocoloObs.getText())){
            updateObs(protocolo);
        }
        else if(atividade.getObs() == null){
            updateObs(atividade);
        }
        else if(!atividade.getObs().equals(textAreaAtividadeObs.getText())){
            updateObs(atividade);
        }
        else{
            Modelo.getInstance().popup.setAutoHide(false);
            Modelo.getInstance().showAlertErro("Não houve nenhuma alteração no campo Observação.");
            Modelo.getInstance().popup.setAutoHide(true);
          
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setEditableFalse();
        insereLabel();
    }    
    
}
