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
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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
    
    private final int primeiraHoraDoDia = 7;
    
    private final int ultimaHoraDoDia = 23;
    
    private void inicializaGridSemana() {
        for( int coluna = 0; coluna < 7; coluna ++) {
            for( int linha = 0; linha < 16; linha++) {
                StackPane pane = new StackPane();
                pane.getStylesheets().add("CSS/CalendarioCSS.css");
                pane.getStyleClass().add("pane-semana");

                semanaGridPane.add(pane, coluna, linha);   
            }
        }                              
    }
     
    private void inicializaCabecarioSemana() {
        
        LocalDate hoje = LocalDate.now();
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
            
            String data = Integer.toString(diaAtual.getDayOfMonth()) + "/" + Integer.toString(diaAtual.getMonthValue());
      
            //  Dias do mes
            StackPane newPane = new StackPane();
            Label labelData = new Label(data);
            
            cabecarioDiasNum.setHgrow(newPane, Priority.ALWAYS);
            newPane.setMaxWidth(Double.MAX_VALUE);
            newPane.setMinWidth(cabecarioDiasNum.getPrefWidth() / 7);
            
            if(diaAtual.isEqual(hoje)) {
                labelData.getStylesheets().add("CSS/CalendarioCSS.css");
                labelData.getStyleClass().add("Label-hoje");
            }
            
            cabecarioDiasNum.getChildren().add(newPane);
            
            newPane.getChildren().add(labelData);
            
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
        for (int i = primeiraHoraDoDia; i <= ultimaHoraDoDia; i++) {
            String labelHora = Integer.toString(i)+"h00";
            StackPane pane = new StackPane();
            
            pane.setPrefHeight(vBoxHorarios.getPrefHeight() / (ultimaHoraDoDia - primeiraHoraDoDia));
            pane.setMaxHeight(pane.getPrefHeight());
            
            vBoxHorarios.getChildren().add(pane);
            
            pane.getChildren().add(new Label(labelHora));
        }
    }
    
    private void carregaEventos() {
        DaoEvento daoEvento = new DaoEvento();
        ObservableList<Evento> listaEvento = daoEvento.getListEventoForCalendario();

        for(Evento evento : listaEvento) {
            for(int i=0; i < 7; i++) {
                LocalDate date = firstDayOfWeek.plusDays(i); 
                if(date.isEqual(evento.getData().toLocalDate()))
                    addEvento(evento, date);
            }
        }
    }
    
    private void addEvento(Evento evento, LocalDate date) {
        int horaInicio = primeiraHoraDoDia+3;
        int duracao = 8;
        
        Label labelEvento = new Label();
        Tooltip tooltip = new Tooltip();
        String cor = new String();
        
        /*
        int horaInicio = evento.getHoraInicio();
        int duracao = evento.getDuracao();
        
        
        if(evento.getHoraInicio().equals(null)) {
            horaInicio = primeiraHoraDoDia;
            duracao = 1;
        }
        */
        
        int linhaInicio = horaInicio - 7;
        int coluna = date.getDayOfWeek().plus(1).getValue() - 1; //plus(1) para corrigir a ordem dos dias da semana de dom-sab para seg-dom
        StringBuilder styleString = new StringBuilder(); //cor do stackpane
        
        if(evento.getCompromissoId() != 0) {    // Evento do tipo Compromisso
            DaoCompromisso daoCompromisso = new DaoCompromisso();
            Compromisso compromisso = daoCompromisso.getCompromissoById(evento.getCompromissoId());
            
            labelEvento.setText(compromisso.getDescricao());
            tooltip.setText(compromisso.getDescricao());
            cor = compromisso.getCor();
        }
        
        else if(evento.getSessaoId() != null) { // Evento do tipo Sessão
            DaoSessao daoSessao = new DaoSessao();
            DaoAtividade daoAtividade = new DaoAtividade();
            DaoFazenda daoFazenda = new DaoFazenda();
            
            Sessao sessao = daoSessao.getSessaoById(evento.getSessaoId());
            Atividade atividade = daoAtividade.getAtividadeById(evento.getAtividadeId());
            Fazenda fazenda = daoFazenda.getFazendaById(sessao.getFazendaId());
            
            labelEvento.setText(atividade.getDescricao() + "" + fazenda.getSigla().toUpperCase());
            tooltip.setText(atividade.getDescricao() + "" + fazenda.getSigla().toUpperCase());
            cor = sessao.getCor();
        }

            labelEvento.getStylesheets().add("CSS/CalendarioCSS.css");
            labelEvento.getStyleClass().add("Label-evento-semana");
            
            StackPane pane = new StackPane(labelEvento);
            styleString.append("-fx-background-color: #").append(cor.substring(2,8)).append(";");
            pane.setStyle(styleString.toString());

            pane.getStylesheets().add("CSS/CalendarioCSS.css");
            pane.getStyleClass().add("pane-semana");
            labelEvento.setTooltip(tooltip);
            
            semanaGridPane.add(pane, coluna, linhaInicio, 1, duracao);
            
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
        inicializaCabecarioSemana();
        inicializaGridSemana();
        inicializaCabecarioHorarios();  
        carregaEventos();
    }    
    
}
