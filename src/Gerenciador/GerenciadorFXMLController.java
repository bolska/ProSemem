package Gerenciador;

import Model.SnackbarModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Bolska
 */
public class GerenciadorFXMLController implements Initializable {

    @FXML
    private VBox rootPane;
    
    @FXML
    private AnchorPane sceneAnchorPane;
    
    @FXML
    private void openProtocolo(ActionEvent evt){
        try{ 
            AnchorPane subPane = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Gerenciador/Protocolo/ProtocoloFXML.fxml"));
            subPane.setPrefSize(sceneAnchorPane.getWidth(), sceneAnchorPane.getHeight());
            sceneAnchorPane.getChildren().add(subPane);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    @FXML
    private void openFazenda(ActionEvent evt){
        try{ 
            AnchorPane subPane = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Gerenciador/Fazenda/FazendaFXML.fxml"));
            subPane.setPrefSize(sceneAnchorPane.getWidth(), sceneAnchorPane.getHeight());
            sceneAnchorPane.getChildren().add(subPane);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    @FXML
    private void openSessao(ActionEvent evt) {
        try{ 
            AnchorPane subPane = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Gerenciador/Sessao/SessaoFXML.fxml"));
            subPane.setPrefSize(sceneAnchorPane.getWidth(), sceneAnchorPane.getHeight());
            sceneAnchorPane.getChildren().add(subPane);
        }
        catch(Exception e){
            System.out.println("teste"+e);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SnackbarModel.pane = rootPane;
    }    
    
}
