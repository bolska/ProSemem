/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Model.Modelo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Bolska
 */
public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainFXML.fxml"));
        
        Scene scene = new Scene(root);
        
        scene.getStylesheets().add("CSS/Calendario.css");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.getIcons().add(new Image("Icons/unect_logo.png"));
        stage.setTitle("Calendário");
        
        
        stage.setOnCloseRequest( (e) ->{
            if(Modelo.getInstance().showAlertConfirm("Deseja encerrar o programa?")){
                Platform.exit();
            }
            e.consume();
        });
        
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
