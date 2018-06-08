/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gerenciador.Fazenda.AddFazenda;

import BancoDeDados.DaoEncarregado;
import BancoDeDados.DaoFazenda;
import Classes.Encarregado;
import Classes.Fazenda;
import Model.Modelo;
import RegrasNegocio.Verify;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author user
 */
public class AddFazendaFXMLController implements Initializable {
    
    @FXML
    private AnchorPane rootPane;
    
    @FXML
    private JFXTextField textFieldNome;
    
    @FXML
    private JFXTextField textFieldSigla;
    
    @FXML
    private JFXTextField textFieldEncarregado;
    
    @FXML
    private JFXTextArea textAreaDescricao;
    
    @FXML
    private void addFazenda(ActionEvent evt) {
        if(!textFieldNome.getText().trim().isEmpty()){
            if(!textFieldSigla.getText().trim().isEmpty()){
                DaoFazenda daoF = new DaoFazenda();
                Fazenda fazenda = new Fazenda();

                fazenda.setNome(textFieldNome.getText().trim());
                fazenda.setSigla(textFieldSigla.getText().trim());
                if(!Verify.hasEqual(fazenda)){
                    if(!textFieldEncarregado.getText().trim().isEmpty()){

                        DaoEncarregado daoE = new DaoEncarregado();
                        Encarregado encarregado = new Encarregado();
                        encarregado.setNome(textFieldEncarregado.getText().trim());

                        if(!Verify.hasEqual(encarregado)){
                            daoE.insertEncarregado(encarregado);
                            encarregado = daoE.getEncarregadoByNome(encarregado.getNome());

                            fazenda.setNome(textFieldNome.getText().trim());
                            fazenda.setDescricao(textAreaDescricao.getText().trim());
                            fazenda.setEncarregadoId(encarregado.getId());
                            daoF.insertFazenda(fazenda);

                            Modelo.fazendaController.startTable();

                            closeWindow();
                        }
                        else{
                            Modelo.getInstance().popup.setAutoHide(false);
                            Modelo.getInstance().showAlertErro("Já possui um Encarregado com o mesmo nome.");
                            Modelo.getInstance().popup.setAutoHide(true);
                        }
                    }
                    else{
                        Modelo.getInstance().popup.setAutoHide(false);
                        Modelo.getInstance().showAlertErro("Campo Encarregado está vazio.");
                        Modelo.getInstance().popup.setAutoHide(true);
                    }
                }
                else{
                    Modelo.getInstance().popup.setAutoHide(false);
                    Modelo.getInstance().showAlertErro("Já possui uma Fazenda ou uma Sigla igual.");
                    Modelo.getInstance().popup.setAutoHide(true);
                }
            }
            else{
                Modelo.getInstance().popup.setAutoHide(false);
                Modelo.getInstance().showAlertErro("Campo Sigla está vazio.");
                Modelo.getInstance().popup.setAutoHide(true);
            }
        }
        else{
            Modelo.getInstance().popup.setAutoHide(false);
            Modelo.getInstance().showAlertErro("Campo Fazenda está vazio.");
            Modelo.getInstance().popup.setAutoHide(true);
        }
    }
    
    @FXML
    private void cancelFazenda(ActionEvent evt) {
        Modelo.getInstance().popup.hide();
    }
    
    private void closeWindow() {
        Modelo.getInstance().popup.hide();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
