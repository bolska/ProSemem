package Gerenciador.Protocolo.AddProtocolo;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import BancoDeDados.DaoProtocolo;
import Classes.Protocolo;
import Model.Modelo;
import RegrasNegocio.Verify;
import javafx.scene.layout.AnchorPane;

public class AddProtocoloFXMLController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    
    @FXML
    private JFXTextField textFieldProtocolo;

    @FXML
    private void addProtocolo(ActionEvent event) {
        if(!textFieldProtocolo.getText().trim().isEmpty()){
            Protocolo protocolo = new Protocolo();
            
            protocolo.setDescricao(textFieldProtocolo.getText().trim());
            if(!Verify.hasEqual(protocolo)){
                DaoProtocolo daoP = new DaoProtocolo();
                daoP.inserirProtocolo(protocolo);
                Modelo.protocoloController.iniciaTabela();
                closeWindow();
            }
            else{
                Modelo.getInstance().popup.setAutoHide(false);
                Modelo.getInstance().showAlertErro("Já possui um Protocolo com o mesmo nome.");
                Modelo.getInstance().popup.setAutoHide(true);
            }
        }
        else{
            Modelo.getInstance().popup.setAutoHide(false);
            Modelo.getInstance().showAlertErro("Descrição vazia.");
            Modelo.getInstance().popup.setAutoHide(true);
        }
    }
    
    @FXML
    private void cancelarProtocolo(ActionEvent evt){
        Modelo.getInstance().popup.hide();
    }
    
    private void closeWindow(){
        Modelo.getInstance().popup.hide();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
