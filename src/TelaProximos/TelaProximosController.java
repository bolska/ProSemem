/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TelaProximos;

import BancoDeDados.DaoAtividade;
import BancoDeDados.DaoCompromisso;
import BancoDeDados.DaoEvento;
import BancoDeDados.DaoFazenda;
import BancoDeDados.DaoSessao;
import Classes.Atividade;
import Classes.Compromisso;
import Classes.Evento;
import Classes.Fazenda;
import Classes.Sessao;
import Model.Modelo;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author marilia
 */
public class TelaProximosController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML 
    private AnchorPane rootPane;
    
    @FXML    
    private VBox vBoxEventos = new VBox();   
       
    private Label labelEvento = new Label();
    
    private Date ultimaData;
    
    private void carregaEventos() {
        DaoEvento daoEvento = new DaoEvento();
        ObservableList<Evento> listaEvento = daoEvento.getListEventoForCalendario();

        LocalDate hoje = LocalDate.now();
        
        for(Evento evento : listaEvento) { //Eventos acontecendo a partir de hoje
            if(!evento.getData().toLocalDate().isBefore(hoje)) {
                addEvento(evento);
            }
        }
    }
    
    private void addEvento(Evento evento) {
        String[] months = { 
            "JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV", "DEZ" 
        };
         Date data = evento.getData();
         int dia = data.toLocalDate().getDayOfMonth();
         int mes = data.toLocalDate().getMonthValue() - 1;
         
         if((ultimaData == null) || (!ultimaData.equals(data))){ //Compara data do evento anterior com data do evento atual
             // novo label dia 
             String dataString = months[mes] + " " + dia;
             Label labelDia = new Label("\t\t" + dataString);
             labelDia.getStylesheets().add("CSS/CalendarioCSS.css");
             labelDia.getStyleClass().add("Label-proximos-eventos");
             AnchorPane pane = new AnchorPane();
             pane.getChildren().add(labelDia);
             pane.getStylesheets().add("CSS/CalendarioCSS.css");
             pane.getStyleClass().add("pane-semana");
             vBoxEventos.getChildren().add(pane);
         }
       
         ultimaData = data;
         
         if(evento.getCompromissoId() != 0) {    // Evento do tipo Compromisso

            DaoCompromisso daoCompromisso = new DaoCompromisso();
            Compromisso compromisso = daoCompromisso.getCompromissoById(evento.getCompromissoId());
            
            labelEvento = new Label("\t\thh:mm \t" + compromisso.getDescricao());
        }
        
        else if(evento.getSessaoId() != null) { // Evento do tipo Sessão
            DaoSessao daoSessao = new DaoSessao();
            DaoAtividade daoAtividade = new DaoAtividade();
            DaoFazenda daoFazenda = new DaoFazenda();
            
            Sessao sessao = daoSessao.getSessaoById(evento.getSessaoId());
            Atividade atividade = daoAtividade.getAtividadeById(evento.getAtividadeId());
            Fazenda fazenda = daoFazenda.getFazendaById(sessao.getFazendaId());
            
            labelEvento = new Label("\t\t" + evento.getSessaoId() + " - " + fazenda.getSigla().toUpperCase() + " - " + atividade.getDescricao());
            //labelEvento = new Label("\t\t" + atividade.getDescricao() + " " + fazenda.getSigla().toUpperCase());
        }
         
         
         AnchorPane pane = new AnchorPane();
         pane.getChildren().add(labelEvento);
         vBoxEventos.getChildren().add(pane);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        carregaEventos();
    }    
    
}
