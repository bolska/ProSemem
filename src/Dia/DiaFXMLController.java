/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dia;

import BancoDeDados.Conexao;
import Classes.Atividade;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import Classes.Sessao;
import javafx.scene.control.Label;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author gabrielgarcia
 */

//O Scroll do mouse altera os meses CONCERTAR ISSO!
//Fazer um if para saber se há pelo menos algum evento no dia
//Um if para saber se não estourou a tabela
//É importante atualizar o dia automaticamente assim que o usuário adiciona um compromisso novo para manter a consistência!

public class DiaFXMLController implements Initializable {
    
    private Conexao connDia;
    private ObservableList<DiaConstructor>dayEvents;
    //private final String DATA_ATUAL = "SELECT CURRENT_DATE()"; uma maneira de simplificar o codigo
    
    //número de compromissos e de sessões do dia atual
    //private final String N_COMP = "SELECT COUNT(COMP_ID) FROM COMPROMISSO NATURAL JOIN EVENTO WHERE EVT_DATA = (SELECT CURRENT_DATE());";
    //private final String N_SESS = "SELECT COUNT(SES_ID) FROM SESSAO NATURAL JOIN EVENTO WHERE EVT_DATA = (SELECT CURRENT_DATE());";
    
    @FXML
    private TextField dateYear;
  
    @FXML 
    private TableView<DiaConstructor> tableId;
    
    @FXML 
    private TableColumn <DiaConstructor, String> colHorario;
    
    @FXML 
    private TableColumn <DiaConstructor, String> colCompromissos;    

     
    @Override
    public void initialize(URL url, ResourceBundle rb) {    //Incrementando as células da tabela        
    
    //if((N_COMP != "0") || N_SESS != "0"){   se existe pelo menos uma sessão ou um compromisso
        try{
            connDia = new Conexao();
            
            Connection conn = connDia.getConexao(); //FICAR ESPERTO COM ISSO 
            dayEvents = FXCollections.observableArrayList();
                
            //Executando a query e armazenando em um resultset
            //formato da data retornada pelo SQL: YYYY-MM-DD
            ResultSet rsSess = conn.createStatement().executeQuery("SELECT SES_ID \n" +
                                                                   "FROM SESSAO NATURAL JOIN EVENTO\n" +
                                                                   "WHERE EVT_DATA = (SELECT CURRENT_DATE());");
            ResultSet rsComp = conn.createStatement().executeQuery("SELECT COMP_DESCRICAO \n" +
                                                                   "FROM COMPROMISSO NATURAL JOIN EVENTO\n" +
                                                                   "WHERE EVT_DATA = (SELECT CURRENT_DATE());");
            while(rsComp.next()){ 
               while(rsSess.next()){
                  dayEvents.add(new DiaConstructor("hh:mm", rsSess.getString(1)));    //Pegando primeiro as sessões
               }
               dayEvents.add(new DiaConstructor("hh:mm",rsComp.getString(1)));  //getString começa em 1, é o indice do meu select
            }
            
            tableId.setItems(null); 
            tableId.setItems(dayEvents);
            
            colHorario.setCellValueFactory(new PropertyValueFactory<>("colHorario"));
            colHorario.setCellFactory(TextFieldTableCell.forTableColumn()); //Tornando o campo Horario editável
            colCompromissos.setCellValueFactory(new PropertyValueFactory<>("colCompromissos"));

            colHorario.setStyle("-fx-alignment: CENTER");
            colCompromissos.setStyle("-fx-alignment: CENTER");
            
        } catch(SQLException ex){
            System.err.println("Error"+ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DiaFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    //Nome do dia da semana, assim como a data atual
        switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            case 2:
                dateYear.setText("Segunda-feira" +": " +Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) +'/'+Integer.toString(Calendar.getInstance().get(Calendar.MONTH)+1) +"/"+Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
                break;
            case 3:
                dateYear.setText("Terça-feira" +": " +Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) +'/'+Integer.toString(Calendar.getInstance().get(Calendar.MONTH)+1) +"/"+Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
                break;
            case 4:
                dateYear.setText("Quarta-feira" +": " +Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) +'/'+Integer.toString(Calendar.getInstance().get(Calendar.MONTH)+1) +"/"+Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
                break;
            case 5:
                dateYear.setText("Quinta-feira" +": " +Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) +'/'+Integer.toString(Calendar.getInstance().get(Calendar.MONTH)+1) +"/"+Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
                break;
            case 6:
                dateYear.setText("Sexta-feira" +": " +Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) +'/'+Integer.toString(Calendar.getInstance().get(Calendar.MONTH)+1) +"/"+Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
                break;
            case 7:
                dateYear.setText("Sábado" +": " +Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) +'/'+Integer.toString(Calendar.getInstance().get(Calendar.MONTH)+1) +"/"+Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
                break;
            default:
                dateYear.setText("Domingo" +": " +Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) +'/'+Integer.toString(Calendar.getInstance().get(Calendar.MONTH)+1) +"/"+Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
                break;
        }
        
    }      
    
}