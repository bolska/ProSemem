/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import BancoDeDados.DaoSessao;
import Classes.Atividade;
import Classes.Evento;
import Classes.Fazenda;
import Classes.Protocolo;
import Classes.Sessao;
import Gerenciador.Atividade.AtividadeController;
import Gerenciador.Fazenda.FazendaFXMLController;
import Gerenciador.Protocolo.ProtocoloFXMLController;
import Main.MainController;
import com.jfoenix.controls.JFXPopup;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


/**
 *
 * @author Bolska
 */
public class Modelo {
    private final static Modelo instance = new Modelo();
    
    public static String lastSessaoId;
    
    //Controllers
    private static MainController mainController;
    public static ProtocoloFXMLController protocoloController;
    public static AtividadeController atividadeController;
    public static FazendaFXMLController fazendaController;
    
    //Data
    public LocalDate dataAtual; //Data em que o calendário está
    public LocalDate dataAtualAno; //Data em que o calendario está para ano
    public LocalDate dataHoje; 
    
    //Data do Evento
    public int eventoDiaSelecionado;
    public int eventoMesSelecionado;
    public int eventoAnoSelecionado;
    
    public Evento evento;
    
    //RODRIGO===============================
    public String labelId; //Para comparar o seu id, obter o dia da semana quando o click é em um vbox
    //RODRIGO===============================
    
    //Protocolo selecionado
    public Protocolo protocolo;
    public Atividade atividade;
    public Fazenda fazenda;
    public Sessao sessao;
    
    //
    public String detalheEvento;
    
    //
    public Pane pane;
    
    //ID do Copromisso (-1 = nenhum selecionado, Diferente de -1 = compromisso selecionado) 
    public int idCompromisso = -1;
    
    //Popup
    public JFXPopup popup;
    
    //Data SQL para sessão
    public Date dataSessao;
    
    
    //Métodos
    public Modelo(){
        updateLastSessaoId();
    }
    
    public static Modelo getInstance(){
        return instance;
    }
    
    public static MainController getMainController(){
        return mainController;
    }
    
    public static void setMainController(MainController main){
        mainController = main;
    }
    
    public void setProcotoloController(ProtocoloFXMLController controller){
       protocoloController = controller;
    }
    
    public void setAtividadeController(AtividadeController controller){
        atividadeController = controller;
    }
    
    public void setFazendaController(FazendaFXMLController controller){
        fazendaController = controller;
    }
    
    //Retorna o mês em String
    public String getStringMes(int num) {
        switch(num){
            case 1:
                return "Janeiro";
            case 2:
                return "Fevereiro";
            case 3:
                return "Março";
            case 4:
                return "Abril";
            case 5:
                return "Maio";
            case 6:
                return "Junho";
            case 7:
                return "Julho";
            case 8:
                return "Agosto";
            case 9:
                return "Setembro";
            case 10:
                return "Outubro";
            case 11:
                return "Novembro";
            case 12:
                return "Dezembro";  
        }
        return null;
    }
    
    public void showAlertInfo(String texto){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(texto);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.show();
    }
    
    public void showAlertErro(String texto){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Mensagem de Erro");
        alert.setHeaderText(texto);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.show();
    }
    
    public boolean showAlertConfirm(String texto){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText(texto);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }
    
    public void updateLastSessaoId(){
        DaoSessao daoS = new DaoSessao();
        lastSessaoId = daoS.getLastSessaoId(); //Pega o último ID da Sessão
        if(lastSessaoId == null){
            lastSessaoId = "A001";
        }
        else{    
            int num = Integer.parseInt(lastSessaoId.subSequence(1, 4).toString()); //pega apenas os algorismos
            num++; //Incrementa 1

            StringBuilder aux = new StringBuilder(Integer.toString(num));

            //Prenche com 0 a esquerda
            while(aux.length() < 3){
                aux.insert(0, "0");
            }

            //Insere a letra
            aux.insert(0, lastSessaoId.charAt(0));

            lastSessaoId = aux.toString();
        }
    }
}
