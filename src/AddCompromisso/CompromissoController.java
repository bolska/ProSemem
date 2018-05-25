package AddCompromisso;

import BancoDeDados.DaoCompromisso;
import Classes.Compromisso;
import Model.Modelo;
import RegrasNegocio.Verify;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;

public class CompromissoController implements Initializable {
    
    @FXML
    private JFXColorPicker jfxColorPicker;
    
    @FXML
    private JFXTextField textFieldDescr;
    
    @FXML
    private JFXButton button;
    
    private Compromisso compromisso;
    
    private void addCompromisso(){
        DaoCompromisso daoCompromisso = new DaoCompromisso();
        compromisso.setDescricao(textFieldDescr.getText().trim());
        compromisso.setTipo("V");
        compromisso.setCor(jfxColorPicker.getValue().toString());
        if(!compromisso.getDescricao().trim().isEmpty()){
            if(!Verify.hasEqual(compromisso)){
                daoCompromisso.insertCompromisso(compromisso);

                Modelo.getMainController().populateVBoxCompromissos();
                Modelo.getInstance().popup.hide();
            }
            else{
                Modelo.getInstance().popup.setAutoHide(false);
                Modelo.getInstance().showAlertErro("Já possui um Compromisso com essa descrição.");
                Modelo.getInstance().popup.setAutoHide(true);
            }
        }
        else{
            Modelo.getInstance().popup.setAutoHide(false);
            Modelo.getInstance().showAlertErro("Descrição vazia.");
            Modelo.getInstance().popup.setAutoHide(true);
        }
    }
    
    private void updateCompromisso(){
        DaoCompromisso daoCompromisso = new DaoCompromisso();
        
        if(textFieldDescr.getText().trim().isEmpty()){
            Modelo.getInstance().showAlertErro("Descrição vazia.");
        }
        else if(!compromisso.getDescricao().equals(textFieldDescr.getText().trim()) && !compromisso.getCor().equals(jfxColorPicker.getValue().toString())){
            compromisso.setDescricao(textFieldDescr.getText().trim());
            if(!Verify.hasEqual(compromisso)){
                compromisso.setCor(jfxColorPicker.getValue().toString());
                daoCompromisso.updateCompromisso(compromisso);

                Modelo.getMainController().populateVBoxCompromissos();
                Modelo.getInstance().popup.hide();
            }
            else{
                Modelo.getInstance().popup.setAutoHide(false);
                Modelo.getInstance().showAlertErro("Já possui um Compromisso com essa descrição.");
                Modelo.getInstance().popup.setAutoHide(true);
            }    
        }
        else if(!compromisso.getCor().equals(jfxColorPicker.getValue().toString())){
            compromisso.setCor(jfxColorPicker.getValue().toString());
            daoCompromisso.updateCompromissoCor(compromisso);

            Modelo.getMainController().populateVBoxCompromissos();
            Modelo.getInstance().popup.hide();
        }
        else if(!compromisso.getDescricao().equals(textFieldDescr.getText().trim())){
            compromisso.setDescricao(textFieldDescr.getText().trim());
            if(!Verify.hasEqual(compromisso)){
                daoCompromisso.updateCompromissoDescr(compromisso);

                Modelo.getMainController().populateVBoxCompromissos();
                Modelo.getInstance().popup.hide();
            }
            else{
                Modelo.getInstance().popup.setAutoHide(false);
                Modelo.getInstance().showAlertErro("Já possui um Compromisso com essa descrição.");
                Modelo.getInstance().popup.setAutoHide(true);
            } 
        }
        else{
            Modelo.getInstance().popup.setAutoHide(false);
            Modelo.getInstance().showAlertErro("Não foi feita nenhuma alteração no Compromisso.");
            Modelo.getInstance().popup.setAutoHide(true);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        compromisso = new Compromisso();
        if(Modelo.getInstance().idCompromisso != -1){
            DaoCompromisso daoCompromisso = new DaoCompromisso();
            compromisso = daoCompromisso.getCompromissoById(Modelo.getInstance().idCompromisso);
            
            textFieldDescr.setText(compromisso.getDescricao());
            jfxColorPicker.setValue(Color.web(compromisso.getCor())); //Pega Stirng em Hexa e transforma em Color
            
            button.setText("Atualizar");
            button.setOnAction((e) -> {
                updateCompromisso();
            });
        }
        else{
            textFieldDescr.setText("");
            
            button.setOnAction((e) -> {
                addCompromisso();
            });
        }
    }    
}
