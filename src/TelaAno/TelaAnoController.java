/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TelaAno;

import BancoDeDados.DaoEvento;
import Classes.Evento;
import Model.Modelo;
import Model.SnackbarModel;
import com.jfoenix.controls.JFXPopup;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author rodrigo
 */
public class TelaAnoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private HBox rootPane;

    @FXML
    private GridPane fullCalendarGridPane;
    
    public void startYearPage() {
        String[] months = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", //String para label com nome do mes
                           "Junho", "Julho", "Agosto", "Setembro", "Outubro",
                           "Novembro", "Dezembro",};
        String[] days = {
            "D", "S", "T", "Q", "Q", "S", "S", //Inicial dos dias
        };
        
        DaoEvento daoEvento = new DaoEvento();
        ObservableList<Evento> listaEvento = daoEvento.getListEventoForCalendario();
        
        fullCalendarGridPane.setId(Integer.toString(Modelo.getInstance().dataAtualAno.getYear()));
        
        //Etapa1
        rootPane.setPrefSize(Modelo.getInstance().centerWidth, Modelo.getInstance().centerHeight); //Seta tamanho para vBox
        fullCalendarGridPane.setPrefSize(Modelo.getInstance().centerWidth, Modelo.getInstance().centerHeight); //Seta mesmo tamanho para o gridpane
        
        for (int row = 0; row < 3; row++) { //Função para colocar vBox nos nodes
            for (int column = 0; column < 4; column++) {

                VBox vBoxCalendar = new VBox();

                vBoxCalendar.setPrefWidth(fullCalendarGridPane.getPrefWidth() / 7);

                GridPane.setVgrow(vBoxCalendar, Priority.ALWAYS);
                fullCalendarGridPane.add(vBoxCalendar, column, row);
            }
        }
        //Etapa1
        //Etapa2
       
        int aux = 0; //Variavel para auxiliar na inserção dos nomes dos meses
        int mesAno = 1; //Variavel para auxiliar a rodar todos os meses para distribuir os dias no calendario
        
        for(Node node : fullCalendarGridPane.getChildren()) { //Roda todo o gridpane maior
            if(aux < 12 && mesAno < 13) {
                VBox vBoxCalendary = (VBox) node; //Transforma em vBox

                HBox hBoxMonth = new HBox(); //Cria hBox para o nome do mes
                StackPane paneM = new StackPane(); //Cria um stack pane

                hBoxMonth.setHgrow(paneM, Priority.ALWAYS);
                paneM.setMaxWidth(Double.MAX_VALUE);
                paneM.setPrefWidth(vBoxCalendary.getWidth()); //Tamanho do hBox, baseado no vBox da grid

                hBoxMonth.getChildren().add(paneM); //Adiciona os stack pane no hBox
                paneM.getChildren().add(new Label(months[aux])); //Adiciona a string em uma label que adiciona no stack pane

            //Etapa2    
            //Etapa3
                HBox hBoxWeek = new HBox(); //Cria um hBox para os dias 
                for (int i = 0; i < 7; i++) { //Roda pelas strings dos 7 dias 

                    StackPane paneD = new StackPane(); //Cria um stack pane

                    hBoxWeek.setHgrow(paneD, Priority.ALWAYS);
                    paneD.setMaxWidth(Double.MAX_VALUE);
                    paneD.setPrefWidth(vBoxCalendary.getWidth() / 7); //Divide em tamanhos iguais

                    hBoxWeek.getChildren().add(paneD); //Adiciona os stack panes no hBox
                    paneD.getChildren().add(new Label(days[i])); //Adiciona Strings no stack pane
                }

            //Etapa3
            //Etapa4
                GridPane gridMiniCalendar = new GridPane(); //Cria o gridpane do calendario
                gridMiniCalendar.setPrefWidth(vBoxCalendary.getWidth()); //Largura baseada no box do grid maior
                gridMiniCalendar.setVgap(2); //Espaço entre os nodes de 1
                gridMiniCalendar.setHgap(2); //Espaço entre os nodes de 1
                gridMiniCalendar.setAlignment(Pos.CENTER); //Alinha centro

                for (int i = 0; i < 7; i++) {
                    ColumnConstraints colConst = new ColumnConstraints(); //Novas colunas
                    colConst.setPrefWidth(gridMiniCalendar.getWidth() / 7); //Divide o tamanho pela quantidade
                    colConst.setHgrow(Priority.ALWAYS);
                    colConst.setHalignment(HPos.CENTER);
                    gridMiniCalendar.getColumnConstraints().add(colConst); //Adiciona ao gridpane
                }
                for (int i = 0; i < 6; i++) {
                    RowConstraints rowConst = new RowConstraints(); //novas linhas 
                    rowConst.setPrefHeight(gridMiniCalendar.getHeight() / 6); //Divide o tamanho pela quantidade
                    rowConst.setVgrow(Priority.ALWAYS);
                    rowConst.setValignment(VPos.CENTER);
                    gridMiniCalendar.getRowConstraints().add(rowConst); //Adiciona ao gridpane   
                }

                //Etapa1 novamente
                for (int row = 0; row < 6; row++) { //Adiciona vBox aos nodes do gridpane menor
                    for (int column = 0; column < 7; column++) {

                        VBox vBoxDays = new VBox();

                        vBoxDays.setPrefWidth(gridMiniCalendar.getWidth() / 7);
                        vBoxDays.setAlignment(Pos.CENTER);
                        
                        GridPane.setVgrow(vBoxDays, Priority.ALWAYS);
                        gridMiniCalendar.add(vBoxDays, column, row);
                    }
                }
                //Etapa1 novamente
                //=======================================================================
                //CODIGO IGUAL DO CALENDARIO PRINCIPAL===================================
                /*Muda que o mes percorre de 1 até 12, portanto usa uma variavel diferente
                que a do calendario principal. Esta faltando alguns detalhes como deixar o codigo mais 
                entendivel e tambem detalhes no front-end*/
                LocalDate data = Modelo.getInstance().dataAtualAno;  

                int ano = data.getYear();

                Modelo.getInstance().dataAtualAno = data.of(ano, mesAno, 1);

                //Variáveis de controle
                int contDiaAtual = 1;       //Valor dos dias do mês atual
                int contGrid = 1;           //Valor da posição dos dias do mês anterior
                int contDiaPosterior = 1;   //Valor dos dias do mês posterior
                int contDiaSemana = 1;      //Valor do dia de semana (Sempre começa no Domingo)

                //Variáveis para manipular os dias do mês atual
                LocalDate primeiroDiaMes = Modelo.getInstance().dataAtualAno.withDayOfMonth(1);                     //Pega a data do primeiro dia do mês
                int diaComecaMesAtual = primeiroDiaMes.getDayOfWeek().getValue() + 1;       //Usa-se +1 para corrigir o dia da semana
                int qtdeDiasMesAtual = Modelo.getInstance().dataAtualAno.lengthOfMonth();                           //Quantidade de dias no mês

                //Variáveis para manipular os dias do mês anterior
                int qtdeDiasMesAnterior = data.minusMonths(1).lengthOfMonth();
                int valorDoDiaMesAnterior = qtdeDiasMesAnterior - diaComecaMesAtual + 2; 

                    for(Node node2 : gridMiniCalendar.getChildren()) {
                        VBox vBoxDay = (VBox) node2;
                        vBoxDay.getStylesheets().add("CSS/CalendarioCSS.css");
                        vBoxDay.getStyleClass().add("Box-dia");
                        vBoxDay.getChildren().clear();

                        //Ajusta para começar na primeira linha do calendário (tava começando na segunda linha)
                        if(diaComecaMesAtual == 8){
                            contGrid = 10; //Qualquer número maior que diaComecaMes
                        }
                        else if(diaComecaMesAtual == 9){
                            diaComecaMesAtual = 2;
                        }

                        //Reseta a variável contDiaSemana
                        if(contDiaSemana == 8){
                            contDiaSemana = 1;
                        }

                        //String para definir o estilo do VBoxDia
                        StringBuilder stringColor = new StringBuilder("-fx-background-color: ");

                        //Adiciona os dias do mês anterior
                        if(contGrid < diaComecaMesAtual) {
                            Label label = new Label((Integer.toString(valorDoDiaMesAnterior)));
                            label.setPadding(new Insets(5));
                            label.setId(diaSemanaString(contDiaSemana)); //Seta o id do label para o dia da semana

                            vBoxDay.getChildren().add(label);
                            vBoxDay.setId("anterior");

                            if(contDiaSemana == 1) {
                                stringColor.append("#FFA09B;");
            //                    boxDia.setStyle("-fx-background-color: #FFA09B");
                            } else {
                                stringColor.append("#7BD0ED;");
            //                    boxDia.setStyle("-fx-background-color: #7bd0ed"); //#cce7f0
                            }

                            contGrid++;
                            valorDoDiaMesAnterior++;
                            contDiaSemana++;
                            showTodayDate(stringColor, vBoxDay);
                        } 
                        else {
                            //Adiciona os dias do mês posterior
                            if(contDiaAtual > qtdeDiasMesAtual) {
                                Label label = new Label((Integer.toString(contDiaPosterior)));
                                label.setPadding(new Insets(5));
                                label.setId(diaSemanaString(contDiaSemana)); //Seta o id do label para o dia da semana

                                vBoxDay.getChildren().add(label); 
                                vBoxDay.setId("posterior");

                                if(contDiaSemana == 1) {
                                    stringColor.append("#FFA09B;");
            //                        boxDia.setStyle("-fx-background-color: #FFA09B");
                                } else {
                                    stringColor.append("#7BD0ED;");
            //                        boxDia.setStyle("-fx-background-color: #7bd0ed"); //#cce7f0
                                }

                                contDiaPosterior++;
                                contDiaSemana++;
                                showTodayDate(stringColor, vBoxDay);
                            } 
                            //Adiciona os dias do mês atual
                            else {
                                Label label = new Label(Integer.toString(contDiaAtual));
                                label.setPadding(new Insets(5));
                                label.setId(diaSemanaString(contDiaSemana)); //Seta o id do label para o dia da semana

                                vBoxDay.getChildren().add(label);
                                vBoxDay.setId("atual");

                                if(contDiaSemana == 1) {
                                    stringColor.append("#FFE4C4;");
            //                        boxDia.setStyle("-fx-background-color: ");
                                } else {
                                    stringColor.append("#E3ECF0;");
            //                        boxDia.setStyle("-fx-background-color: #e3ecf0");
                                }

                                contDiaSemana++;
                                showTodayDate(stringColor, vBoxDay);
                            }
                            contDiaAtual++;
                        }
                        
                        carregaEventos(vBoxDay, listaEvento);
                    }
                //===========================================================================
                vBoxCalendary.getChildren().clear(); //Adiciona tudo criado ao vBox
                vBoxCalendary.getChildren().add(hBoxMonth);
                vBoxCalendary.getChildren().add(hBoxWeek);
                vBoxCalendary.getChildren().add(gridMiniCalendar);

                aux++;
                mesAno++;
            
                
            }
        }
    
    }
    
    private String diaSemanaString(int diaSemana){
        switch(diaSemana){
            case 1:
                return "domingo";
            case 2:
                return "segunda";
            case 3:
                return "terça";
            case 4:
                return "quarta";
            case 5:
                return "quinta";
            case 6:
                return "sexta";
            case 7:
                return "sábado";
        }
        return null;
    }
    
    private void showTodayDate(StringBuilder stringColor, VBox vBoxDay) {
        LocalDate todayDate = LocalDate.now();
        LocalDate showingDate = Modelo.getInstance().dataAtualAno;
        
        stringColor.append(" -fx-border-color: ");
        
        
        if(vBoxDay.getId().equals("atual")){
            if(showingDate.getYear() == todayDate.getYear()){

                if(showingDate.getMonthValue() == todayDate.getMonthValue()){

                    Label labelDay = (Label) vBoxDay.getChildren().get(0);
                    int dayValue = Integer.parseInt(labelDay.getText());

                    if(todayDate.getDayOfMonth() == dayValue){
                        stringColor.append("blue;");
                    }
                    else{
                        stringColor.append("white;");
                    }
                }
                else{
                    stringColor.append("white;");
                }
            }
            else{
                stringColor.append("white;");
            }
        }
        else{
            stringColor.append("white;");
        }
        
        vBoxDay.setStyle(stringColor.toString());
    }
    
    private void carregaEventos(VBox vBox, ObservableList<Evento> listaEvento){
        
        int anoAtual = Modelo.getInstance().dataAtualAno.getYear();
        int mesAtual = Modelo.getInstance().dataAtualAno.getMonthValue();    
        
        for(Evento evento : listaEvento){   
                
            if(vBox.getId().equals("anterior")){
                int mes, ano;
                if(mesAtual == 1){
                   ano = anoAtual - 1;
                   mes = 12;
                }
                else{
                    mes = mesAtual - 1;
                    ano = anoAtual;
                }
                adicionaEvento(mes, ano, vBox, evento);
            }
            else if(vBox.getId().equals("posterior")){
                int mes, ano;
                if(mesAtual == 12){
                    ano = anoAtual + 1;
                    mes = 1;
                }
                else{
                    mes = mesAtual + 1;
                    ano = anoAtual;
                }
                adicionaEvento(mes, ano, vBox, evento);
            }
            else{
                adicionaEvento(mesAtual, anoAtual, vBox, evento);
            }
            
        }
    }
    
    
    private void adicionaEvento(int mes, int ano, VBox vBox, Evento evento){
        if(ano == evento.getData().toLocalDate().getYear()){
            
            if(mes == evento.getData().toLocalDate().getMonthValue()){

                Label label = (Label) vBox.getChildren().get(0);
                int dia = evento.getData().toLocalDate().getDayOfMonth();
                
                if(Integer.parseInt(label.getText()) == dia){
                    vBox.setId(Integer.toString(mes));
                    if(evento.getCompromissoId() != 0) {
                        vBox.setStyle("-fx-background-color: #FFDF00");
                    } else {
                        vBox.setStyle("-fx-background-color: #FE3636");
                    }
                    
                    vBox.setOnMouseClicked((e) -> {
                    if(e.getClickCount() == 1 && !e.isConsumed()) {
                        Label labelDia = (Label) vBox.getChildren().get(0);
                        
                        LocalDate data = LocalDate.now();
                        Modelo.getInstance().dataDiaEvento = data.of(Integer.parseInt(fullCalendarGridPane.getId()), Integer.parseInt(vBox.getId()), Integer.parseInt(labelDia.getText()));

                        try {
                            openListEvents(vBox, evento);

                        } catch (IOException ex) {
                            Modelo.getInstance().showAlertErro(ex.getMessage());
                        }
                    }
                    });
                }
            }
        }
    }
    
    private void openListEvents(VBox vBox, Evento evento) throws IOException {
        int xOffSet = 5;

        AnchorPane popupRoot = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("DescricaoEvento/ListaEventoFXML.fxml"));
        JFXPopup popup = new JFXPopup(popupRoot);

        popup.setOnShowing( (evtShowing) -> {
            if(evento.getCompromissoId() != 0) {
                vBox.setStyle("-fx-background-color: #FFBA00");
            } else {
                vBox.setStyle("-fx-background-color: #FE1A1A");
            }
        });

        popup.setOnHiding( (evtHiding) -> {
            if(evento.getCompromissoId() != 0) {
                vBox.setStyle("-fx-background-color: #FFDF00");
            } else {
                vBox.setStyle("-fx-background-color: #FE3636");
            }
        });

        //se x.popup > width.scene e y.popup > height.scene
        if((vBox.getLayoutX() + vBox.getWidth() + popupRoot.getPrefWidth() + xOffSet) > rootPane.getPrefWidth() 
            && (vBox.getLayoutY() + popupRoot.getPrefHeight()) > rootPane.getPrefHeight() ){

            popup.show(vBox, JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.RIGHT, -(vBox.getWidth() + xOffSet), 0);
        }
        //se x.popup > width.scene e y.popup <= height.scene
        else if((vBox.getLayoutX() + vBox.getWidth() + popupRoot.getPrefWidth() + xOffSet) > rootPane.getPrefWidth() 
            && (vBox.getLayoutY() + popupRoot.getPrefHeight()) <= rootPane.getPrefHeight() ){

            popup.show(vBox, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, -(vBox.getWidth() + xOffSet), 0);
        }
        //se x.popup <= width.scene e y.popup > height.scene
        else if((vBox.getLayoutX() + vBox.getWidth() + popupRoot.getPrefWidth() + xOffSet) <= rootPane.getPrefWidth() 
            && (vBox.getLayoutY() + popupRoot.getPrefHeight()) > rootPane.getPrefHeight() ){

            popup.show(vBox, JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT, (vBox.getWidth() + xOffSet), 0);
        }
        //se x.popup <= width.scene e y.popup <= height.scene
        else{
            popup.show(vBox, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, (vBox.getWidth() + xOffSet), 0);
        }

        Modelo.getInstance().popup = popup;
        SnackbarModel.pane = rootPane;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        startYearPage();
    }    
    
}
