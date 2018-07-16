/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Ano;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author rodrigo
 */
public class AnoFXMLController implements Initializable {

    @FXML
    private GridPane fullCalendarGridPane;

    @FXML
    private HBox cabecarioJan;

    @FXML
    private GridPane janGrid;

    @FXML
    private HBox cabecarioFev;

    @FXML
    private GridPane fevGrid;

    @FXML
    private HBox cabecarioMar;

    @FXML
    private GridPane marGrid;

    @FXML
    private HBox cabecarioAbr;

    @FXML
    private GridPane abrGrid;

    @FXML
    private HBox cabecarioMai;

    @FXML
    private GridPane maiGrid;

    @FXML
    private HBox cabecarioJun;

    @FXML
    private GridPane junGrid;

    @FXML
    private HBox cabecarioJul;

    @FXML
    private GridPane julGrid;

    @FXML
    private HBox cabecarioAgo;

    @FXML
    private GridPane agoGrid;

    @FXML
    private HBox cabecarioSet;

    @FXML
    private GridPane setGrid;

    @FXML
    private HBox cabecarioOut;

    @FXML
    private GridPane outGrid;

    @FXML
    private HBox cabecarioNov;

    @FXML
    private GridPane novGrid;

    @FXML
    private HBox cabecarioDez;

    @FXML
    private GridPane dezGrid;
    
    @FXML
    private Button buttonInicial;
    
    public void inicializaCabecarioSemanaAno() {
        
        String[] diasSemana = {
            "D", "S", "T", "Q", "Q", "S", "S", 
        };
        
        for (int i = 0; i < 7; i++) {
            //Janeiro-----------------------------------
            StackPane pane1 = new StackPane();

            HBox.setHgrow(pane1, Priority.ALWAYS);
            pane1.setMaxWidth(Double.MAX_VALUE);
            pane1.setMinWidth(cabecarioJan.getPrefWidth() / 7);

            cabecarioJan.getChildren().add(pane1);
            
            
            pane1.getChildren().add(new Label(diasSemana[i]));
            //Fevereiro---------------------------------
            StackPane pane2 = new StackPane();
            
            HBox.setHgrow(pane2, Priority.ALWAYS);
            pane2.setMaxWidth(Double.MAX_VALUE);
            pane2.setMinWidth(cabecarioFev.getPrefWidth() / 7);

            cabecarioFev.getChildren().add(pane2);
            
            
            pane2.getChildren().add(new Label(diasSemana[i]));
            //Março---------------------------------
            StackPane pane3 = new StackPane();
            
            HBox.setHgrow(pane3, Priority.ALWAYS);
            pane3.setMaxWidth(Double.MAX_VALUE);
            pane3.setMinWidth(cabecarioMar.getPrefWidth() / 7);

            cabecarioMar.getChildren().add(pane3);
            
            
            pane3.getChildren().add(new Label(diasSemana[i]));
            //Abril---------------------------------
            StackPane pane4 = new StackPane();
            
            HBox.setHgrow(pane4, Priority.ALWAYS);
            pane4.setMaxWidth(Double.MAX_VALUE);
            pane4.setMinWidth(cabecarioAbr.getPrefWidth() / 7);

            cabecarioAbr.getChildren().add(pane4);
            
            
            pane4.getChildren().add(new Label(diasSemana[i]));
            //Maio---------------------------------
            StackPane pane5 = new StackPane();
            
            HBox.setHgrow(pane5, Priority.ALWAYS);
            pane5.setMaxWidth(Double.MAX_VALUE);
            pane5.setMinWidth(cabecarioMai.getPrefWidth() / 7);

            cabecarioMai.getChildren().add(pane5);
            
            
            pane5.getChildren().add(new Label(diasSemana[i]));
            //Junho---------------------------------
            StackPane pane6 = new StackPane();
            
            HBox.setHgrow(pane6, Priority.ALWAYS);
            pane6.setMaxWidth(Double.MAX_VALUE);
            pane6.setMinWidth(cabecarioJun.getPrefWidth() / 7);

            cabecarioJun.getChildren().add(pane6);
            
            
            pane6.getChildren().add(new Label(diasSemana[i]));
            //Julho---------------------------------
            StackPane pane7 = new StackPane();
            
            HBox.setHgrow(pane7, Priority.ALWAYS);
            pane7.setMaxWidth(Double.MAX_VALUE);
            pane7.setMinWidth(cabecarioJul.getPrefWidth() / 7);

            cabecarioJul.getChildren().add(pane7);
            
            
            pane7.getChildren().add(new Label(diasSemana[i]));
            //Agosto---------------------------------
            StackPane pane8 = new StackPane();
            
            HBox.setHgrow(pane8, Priority.ALWAYS);
            pane8.setMaxWidth(Double.MAX_VALUE);
            pane8.setMinWidth(cabecarioAgo.getPrefWidth() / 7);

            cabecarioAgo.getChildren().add(pane8);
            
            
            pane8.getChildren().add(new Label(diasSemana[i]));
            //Setembro---------------------------------
            StackPane pane9 = new StackPane();
            
            HBox.setHgrow(pane9, Priority.ALWAYS);
            pane9.setMaxWidth(Double.MAX_VALUE);
            pane9.setMinWidth(cabecarioSet.getPrefWidth() / 7);

            cabecarioSet.getChildren().add(pane9);
            
            
            pane9.getChildren().add(new Label(diasSemana[i]));
            //Outubro---------------------------------
            StackPane pane10 = new StackPane();
            
            HBox.setHgrow(pane10, Priority.ALWAYS);
            pane10.setMaxWidth(Double.MAX_VALUE);
            pane10.setMinWidth(cabecarioOut.getPrefWidth() / 7);

            cabecarioOut.getChildren().add(pane10);
            
            
            pane10.getChildren().add(new Label(diasSemana[i]));
            //Novembro---------------------------------
            StackPane pane11 = new StackPane();
            
            HBox.setHgrow(pane11, Priority.ALWAYS);
            pane11.setMaxWidth(Double.MAX_VALUE);
            pane11.setMinWidth(cabecarioNov.getPrefWidth() / 7);

            cabecarioNov.getChildren().add(pane11);
            
            
            pane11.getChildren().add(new Label(diasSemana[i]));
            //Dezembro---------------------------------
            StackPane pane12 = new StackPane();
            
            HBox.setHgrow(pane12, Priority.ALWAYS);
            pane12.setMaxWidth(Double.MAX_VALUE);
            pane12.setMinWidth(cabecarioDez.getPrefWidth() / 7);

            cabecarioDez.getChildren().add(pane12);
            
            
            pane12.getChildren().add(new Label(diasSemana[i]));
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
