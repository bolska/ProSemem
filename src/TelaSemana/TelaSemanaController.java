/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package TelaSemana;

import Model.Modelo;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author marilia
 */
public class TelaSemanaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML private HBox rootPane;
    
    @FXML private HBox hBoxSemana;
    
    @FXML private HBox cabecarioSemana;
    
    @FXML private HBox cabecarioDiasNum;
    
    @FXML private GridPane semanaGridPane;
    
    @FXML private VBox vBoxHorarios;
    
    private LocalDate firstDayOfWeek;
    
    private void inicializaGridSemana() {
        for( int coluna = 0; coluna < 7; coluna ++) {
            for( int linha = 0; linha < 15; linha++) {
                StackPane pane = new StackPane();
                pane.getStylesheets().add("CSS/CalendarioCSS.css");
                pane.getStyleClass().add("pane-semana");

                semanaGridPane.add(pane, coluna, linha);   
            }
        }                              
    }
     
    private void inicializaCabecarioSemana() {
        
        LocalDate hoje = Modelo.getInstance().dataHoje;
        LocalDate diaAtual;
        
        
        if(hoje.getDayOfWeek().getValue() == 7) //Domingo
            firstDayOfWeek = hoje;
        else
            firstDayOfWeek = hoje.minusDays(hoje.getDayOfWeek().getValue());
        
        String[] diasSemana = {
            "Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", 
        };
        
        for (int i = 0; i < 7; i++) {
            diaAtual = firstDayOfWeek.plusDays(i);
            int dia  = diaAtual.getDayOfMonth();
            int mes = diaAtual.getMonthValue();
            
            String data = Integer.toString(diaAtual.getDayOfMonth()) + "/" + Integer.toString(diaAtual.getMonthValue());
            
            
            //  Dias do mes
            StackPane newPane = new StackPane();
            
            cabecarioDiasNum.setHgrow(newPane, Priority.ALWAYS);
            newPane.setMaxWidth(Double.MAX_VALUE);
            newPane.setMinWidth(cabecarioDiasNum.getPrefWidth() / 7);
            
            cabecarioDiasNum.getChildren().add(newPane);
            
            newPane.getChildren().add(new Label(data));
            
            
            //    Dias da semana
            StackPane pane = new StackPane();
            
            cabecarioSemana.setHgrow(pane, Priority.ALWAYS);
            pane.setMaxWidth(Double.MAX_VALUE);
            pane.setMinWidth(cabecarioSemana.getPrefWidth() / 7);
            
            cabecarioSemana.getChildren().add(pane);
            
            pane.getChildren().add(new Label(diasSemana[i]));
        }
    }
    
    
    private void inicializaCabecarioHorarios() {
        for (int i = 7; i < 23; i++) {
            String labelHora = Integer.toString(i)+"h00";
            StackPane pane = new StackPane();
            
            vBoxHorarios.setVgrow(pane, Priority.ALWAYS);
            pane.setMaxHeight(Double.MAX_VALUE);
            pane.setPrefHeight(vBoxHorarios.getPrefHeight() / 15);
            
            vBoxHorarios.getChildren().add(pane);
            
            pane.getChildren().add(new Label(labelHora));
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
        inicializaCabecarioSemana();
        inicializaGridSemana();
        
        inicializaCabecarioHorarios();  
        System.out.println("Aqui sim");
    }    
    
}
