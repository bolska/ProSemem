package DescricaoEvento;

import BancoDeDados.DaoCompromisso;
import BancoDeDados.DaoEvento;
import Classes.Compromisso;
import Model.Modelo;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;


public class DescricaoCompromissoController implements Initializable {

    @FXML private AnchorPane root;

    @FXML private JFXTextField textFieldDescricao;
    
    @FXML private JFXTextField textFieldTipo;

    @FXML private JFXColorPicker colorPickerCompromisso;

    @FXML
    void buttonRemoverEvento(ActionEvent event) {
        DaoEvento daoE = new DaoEvento();
        daoE.removeEventoById(Modelo.getInstance().evento.getId());
        
        Modelo.getMainController().atualizaCalendario();
        closePopup(event);
    }

    @FXML
    private void closePopup(ActionEvent e) {
        closePopup();
    }
    
    private void closePopup() {
        Modelo.getInstance().popup.hide();
    }
    
    private void loadLabels(){
        DaoCompromisso daoC = new DaoCompromisso();
        Compromisso compromisso = daoC.getCompromissoById(Modelo.getInstance().evento.getCompromissoId());
        
        textFieldTipo.setText(compromisso.getTipo());
        textFieldTipo.setEditable(false);
        textFieldDescricao.setText(compromisso.getDescricao());
        textFieldDescricao.setEditable(false);
        colorPickerCompromisso.setValue(Color.web(compromisso.getCor()));
        colorPickerCompromisso.setOnAction( (evt) -> {
            compromisso.setCor(colorPickerCompromisso.getValue().toString());
            daoC.updateCompromissoCor(compromisso);
            Modelo.getMainController().atualizaCalendario();
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadLabels();
    }    
    
}
