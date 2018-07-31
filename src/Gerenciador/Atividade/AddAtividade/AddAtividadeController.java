/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gerenciador.Atividade.AddAtividade;

import BancoDeDados.DaoAtividade;
import Classes.Atividade;
import Model.Modelo;
import RegrasNegocio.Verify;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bolska
 */
public class AddAtividadeController implements Initializable {
    
    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXTextField textFieldDescricao;

    @FXML
    private JFXComboBox comboBoxTipo;

    @FXML
    private JFXTextField textFieldIntervalo;
    
    @FXML
    private Label labelAtividade;
    
    
    @FXML
    private void buttonAddAtividade(){
        if(!textFieldDescricao.getText().trim().isEmpty() && !textFieldIntervalo.getText().trim().isEmpty() && !comboBoxTipo.getSelectionModel().isEmpty()){
            try {
                Atividade atividade = new Atividade();
                DaoAtividade daoAtividade = new DaoAtividade();

                atividade.setDescricao(textFieldDescricao.getText().trim());
                atividade.setTipo(comboBoxTipo.getSelectionModel().getSelectedItem().toString());
                atividade.setIntervalo(Integer.parseInt(textFieldIntervalo.getText().trim()));
                atividade.setProtocoloId(Modelo.getInstance().protocolo.getId());

                daoAtividade.inserirAtividade(atividade);

                Modelo.atividadeController.iniciaTabela();

                closeWindow();
            } catch (NumberFormatException e) {
                Modelo.getInstance().popup.setAutoHide(false);
                Modelo.getInstance().showAlertErro("Número inválido");
                Modelo.getInstance().popup.setAutoHide(true);
            }
            
        }
        else{
            Modelo.getInstance().popup.setAutoHide(false);
            Modelo.getInstance().showAlertErro("Campo Vazio");
            Modelo.getInstance().popup.setAutoHide(true);
        }
    }

    private void iniciaComboBoxTipo(){
        ObservableList<String> listaTipos = FXCollections.observableArrayList();

        if (Verify.protocoloHasPrincipal(Modelo.getInstance().protocolo)) {
            listaTipos.add("Importante");
            listaTipos.add("Secundário");
            
            labelAtividade.setText("Cadastro Atividade");
        } 
        else {
            listaTipos.add("Principal");
            labelAtividade.setText("Cadastro Atividade Principal");
            textFieldIntervalo.setText("0");
            textFieldIntervalo.setDisable(true);
        }
        comboBoxTipo.setItems(listaTipos);  
    }
    
    @FXML
    private void closeWindow(){
        Modelo.getInstance().popup.hide();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        iniciaComboBoxTipo();
        textFieldDescricao.setOnKeyPressed((k) -> {
            final KeyCombination enter = new KeyCodeCombination(KeyCode.ENTER);
            if(enter.match(k)) {
                buttonAddAtividade();
            }
        });
        textFieldIntervalo.setOnKeyPressed((k) -> {
            final KeyCombination enter = new KeyCodeCombination(KeyCode.ENTER);
            if(enter.match(k)) {
                buttonAddAtividade();
            }
        });
    }    
    
}