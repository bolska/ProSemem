package Main;

import Model.Modelo;
import Animation.TransitionAnimation;
import BancoDeDados.DaoCompromisso;
import BancoDeDados.DaoAtividade;
import BancoDeDados.DaoEvento;
import BancoDeDados.DaoFazenda;
import BancoDeDados.DaoSessao;
import Classes.Atividade;
import Classes.Compromisso;
import Classes.Evento;
import Classes.Fazenda;
import Classes.Sessao;
import Model.SnackbarModel;
import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import RegrasNegocio.Verify;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPopup;
import java.io.IOException;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;


public class MainController implements Initializable {

    private Label labelEventoAnimation = new Label();
    private boolean isMenuOpen = true;
    private boolean isEventoAnimation = false;
    private String whichCompromissoIsSelected; //Seleciona o Compromisso a ser adicionado
    private final Stage gerenciadorStage = new Stage();
    private final DataFormat DRAG_EVENT = new DataFormat("DRAG_EVENT");
    private final TransitionAnimation animation = new TransitionAnimation();
    private final String N_COMP = "SELECT COUNT(COMP_ID) FROM COMPROMISSO NATURAL JOIN EVENTO WHERE EVT_DATA = (SELECT CURRENT_DATE());";
    
    private final String N_SESS = "SELECT COUNT(SES_ID) FROM SESSAO NATURAL JOIN EVENTO WHERE EVT_DATA = (SELECT CURRENT_DATE());";

    private HBox differentCalendarViewRootPane = new HBox(new Label()); //Horizontal box criada justamente para ser usada para incluir algo na tela
    
    private AnchorPane rootDias;
    
    private HBox rootSemana;
    
    @FXML private VBox rootPane;
    
    @FXML private VBox vBoxCalendario;
    
    @FXML private GridPane calendarioGridPane;
    
    @FXML private VBox leftVBox;

    @FXML private JFXComboBox comboMes;
    
    @FXML private Spinner spinnerAno;

    @FXML private HBox cabecarioSemana;
    
    @FXML private StackPane centerStackPane;
    
    @FXML private JFXHamburger hamburgerIcon;
    
    @FXML private VBox vBoxCompromissos;
    
    @FXML private JFXButton buttonCompromisso;
    
    @FXML private ScrollPane scrollPaneConfirmacao;
    
    @FXML private VBox vBoxConfirmacao;

    
    public MainController() throws IOException {    //rootDias criado para fazer as verificações em openMes, openAno e etc..
        this.rootDias = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Dia/DiaFXML.fxml"));
    }
    
    @FXML
    private void animationMenu(MouseEvent evt) throws InterruptedException{
        if(animation.isRunning()){
            if(isMenuOpen){
                isMenuOpen = false;
                animation.setTranslateAnimationOn(leftVBox, 0, -leftVBox.getWidth(), 500);
                animation.setTranslateResizeAnimationOn(centerStackPane, 0, -leftVBox.getWidth(), 500, leftVBox.getWidth());
                animation.hambumguerAnimation(hamburgerIcon, 360, 500);
            }
            else{
                isMenuOpen = true;
                animation.setTranslateAnimationOn(leftVBox, -leftVBox.getWidth(), 0, 500);
                animation.setTranslateResizeAnimationOn(centerStackPane, -leftVBox.getWidth(), 0, 500, -leftVBox.getWidth());
                animation.hambumguerAnimation(hamburgerIcon, -360, 500);  
            }   
        }
    }
    
    @FXML
    private void openGerenciador(ActionEvent evt){
        if(!gerenciadorStage.isShowing()){
            try {
               Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Gerenciador/GerenciadorFXML.fxml"));
               gerenciadorStage.setMaximized(true);
               gerenciadorStage.setTitle("Gerenciador");
               gerenciadorStage.getIcons().add(new Image("Icons/unect_logo.png"));
               
               // Mostra a cena no layout
               Scene scene = new Scene(root);
               gerenciadorStage.setScene(scene);
               gerenciadorStage.show();

            } 
            catch (Exception e) {
                Modelo.getInstance().showAlertErro(e.getMessage());
            }
        }
        else{
            Modelo.getInstance().showAlertErro("Gerenciador já está aberto.");
        }
    }
    
    @FXML
    private void buttonTodayDate(ActionEvent evt){
        
        //Apenas troca de data se for diferente da que o calendário está mostrando
        if(Modelo.getInstance().dataHoje.getDayOfYear() != Modelo.getInstance().dataAtual.getDayOfYear()
                || Modelo.getInstance().dataHoje.getMonthValue() != Modelo.getInstance().dataAtual.getMonthValue()) {
            
            Modelo.getInstance().dataAtual = Modelo.getInstance().dataHoje;
            comboMes.getSelectionModel().select(Modelo.getInstance().dataAtual.getMonthValue() - 1);
            spinnerAno.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2015, 2100, Modelo.getInstance().dataAtual.getYear()));

            atualizaCalendario();
        }
    }
    
    @FXML
    private void onScroll(ScrollEvent evt){
        double deltaY = evt.getDeltaY();
        if(deltaY > 0){
            //Rolagem do mouse para cima. Volta um mês
            comboMes.getSelectionModel().selectPrevious();
        }
        else{
            //Rolagem do mouse para baixo. Vai para o próximo mês
            comboMes.getSelectionModel().selectNext();
        }
    }
    
    //Talvez haja uma forma de fazer a mudança de tela entre dia,semana,mes,ano e proximos de forma mais otimizada
    @FXML
    private void openDia(){
        
        try {
                centerStackPane.getChildren().clear();
                AnchorPane rootDias = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Dia/DiaFXML.fxml"));
                centerStackPane.getChildren().add(rootDias); 
        }
        catch (Exception e) {
            Modelo.getInstance().showAlertErro("Erro ao abrir a janela Dia " + e.getMessage());
        }
    }
    
    @FXML
    private void openSemana(){
        try {  
                centerStackPane.getChildren().clear();
                HBox rootSemana = (HBox) FXMLLoader.load(getClass().getClassLoader().getResource("TelaSemana/TelaSemana.fxml"));
                centerStackPane.getChildren().add(rootSemana);   
        }
        catch (Exception e) {
            Modelo.getInstance().showAlertErro("Erro ao abrir a janela Semana " + e.getMessage());
        }
    }
    
    @FXML
    private void openMes(){
        try {
                if(!centerStackPane.getChildren().isEmpty()) // Quando abrir o programa o StackPane estara vazio, entao apenas adiciona o vBox
                    centerStackPane.getChildren().clear();
                centerStackPane.getChildren().add(vBoxCalendario);
        }
        catch (Exception e) {
            Modelo.getInstance().showAlertErro("Erro ao abrir a janela Mês " + e.getMessage());
        }
    }
    
    @FXML
    private void openAno(){
        try {    
                centerStackPane.getChildren().clear();
                // Load differentCalendarViewRootPane              
                centerStackPane.getChildren().add(differentCalendarViewRootPane);
            
            Label label = (Label) differentCalendarViewRootPane.getChildren().get(0);
            label.setText("Janela Ano em desenvolvimento.");
        }
        catch (Exception e) {
            Modelo.getInstance().showAlertErro("Erro ao abrir a janela Semana" + e.getMessage());
        }
    }
    
    @FXML
    private void openProximos(){
        try {                
                centerStackPane.getChildren().clear();
                //Load differentCalendarViewRootPane FXML
                centerStackPane.getChildren().add(differentCalendarViewRootPane);
                       
            Label label = (Label) differentCalendarViewRootPane.getChildren().get(0);
            label.setText("Janela Próximos ainda em desenvolvimento");
//            AnchorPane rootProximos = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Gerenciador/Protocolo/ProtocoloFXML.fxml"));

        }
        catch (Exception e) {
            Modelo.getInstance().showAlertErro("Erro ao abrir a janela Ano" + e.getMessage());
        }
    }
    
    private void carregaCalendarioLabels() {
        LocalDate dataAtual = Modelo.getInstance().dataAtual; 
        
        //Variáveis de controle
        int contDiaAtual = 1;       //Valor dos dias do mês atual
        int contGrid = 1;           //Valor da posição dos dias do mês anterior
        int contDiaPosterior = 1;   //Valor dos dias do mês posterior
        int contDiaSemana = 1;      //Valor do dia de semana (Sempre começa no Domingo)
        
        //Variáveis para manipular os dias do mês atual
        LocalDate primeiroDiaMes = dataAtual.withDayOfMonth(1);                     //Pega a data do primeiro dia do mês
        int diaComecaMesAtual = primeiroDiaMes.getDayOfWeek().getValue() + 1;       //Usa-se +1 para corrigir o dia da semana
        int qtdeDiasMesAtual = dataAtual.lengthOfMonth();                           //Quantidade de dias no mês
        
        //Variáveis para manipular os dias do mês anterior
        int qtdeDiasMesAnterior = dataAtual.minusMonths(1).lengthOfMonth();
        int valorDoDiaMesAnterior = qtdeDiasMesAnterior - diaComecaMesAtual + 2; 

        //Comando For anda por todo nó (Node) do GridPane
        for(Node node : calendarioGridPane.getChildren()) {
            
            VBox vBoxDay = (VBox) node;
            vBoxDay.getStylesheets().add("CSS/CalendarioCSS.css");
            vBoxDay.getStyleClass().add("Box-dia");
            vBoxDay.getChildren().clear();   //Limpa a lista de filhos do VBox
            
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
    
    private void inicializaCabecarioSemana() {
        String[] diasSemana = {
            "Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", 
        };
        for (int i = 0; i < 7; i++) {

            StackPane pane = new StackPane();

            HBox.setHgrow(pane, Priority.ALWAYS);
            pane.setMaxWidth(Double.MAX_VALUE);
            pane.setMinWidth(cabecarioSemana.getPrefWidth() / 7);

            cabecarioSemana.getChildren().add(pane);
            
            
            pane.getChildren().add(new Label(diasSemana[i]));
        }
    }
    
    private void inicializaCalendarioGridPane() {
        for (int linha = 0; linha < 7; linha++) {
            for (int coluna = 0; coluna < 7; coluna++) {

                VBox vBoxDia = new VBox();

                vBoxDia.setMinWidth(cabecarioSemana.getPrefWidth() / 7);

                vBoxDia.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    //Verifica se Compromisso está selecionado
                    if(!whichCompromissoIsSelected.isEmpty() && e.getClickCount() == 1 && !e.isConsumed()){
                        DaoEvento daoEvento = new DaoEvento();
                        
                        setEventoDateInModelo(vBoxDia);
                        
                        int ano = Modelo.getInstance().eventoAnoSelecionado;
                        int mes = Modelo.getInstance().eventoMesSelecionado;
                        int dia = Modelo.getInstance().eventoDiaSelecionado;

                        LocalDate localDate = LocalDate.of(ano, mes, dia);
                        Date sqlDate = Date.valueOf(localDate);
                        
                        //Se o compromisso estiver inserido no dia, remove do dia
                        if(Verify.isCompromissoInsertedOn(vBoxDia, whichCompromissoIsSelected)){
                            daoEvento.removeEventoCompromisso(Integer.parseInt(whichCompromissoIsSelected), sqlDate);
                        }
                        //Se o compromisso não estiver inserido no dia, adiciona no dia
                        else{
                            DaoCompromisso daoCompromisso = new DaoCompromisso();
                            Compromisso compromisso = daoCompromisso.getCompromissoById(Integer.parseInt(whichCompromissoIsSelected));
                            
                            SnackbarModel.pane = rootPane;
                            daoEvento.inserirEvento(compromisso, sqlDate);
                        }
                        
                        atualizaCalendario();
                    }
                    //Abre tela de adicionar evento-protocolo
                    else if(whichCompromissoIsSelected.isEmpty() && e.getClickCount() == 2 && !e.isConsumed()) {
                        SnackbarModel.pane = rootPane;
                        abreAddEventoPopup(vBoxDia, e);     
                    }
                });

                GridPane.setVgrow(vBoxDia, Priority.ALWAYS);
                calendarioGridPane.add(vBoxDia, coluna, linha);
                
                setDragAndDropEventTarget(vBoxDia);
            }
        }
    }

    private void setEventoDateInModelo(VBox vBox){
        LocalDate data;
        
        if(vBox.getId().equals("anterior")){
            data = Modelo.getInstance().dataAtual.minusMonths(1);
        }
        else if(vBox.getId().equals("posterior")){
            data = Modelo.getInstance().dataAtual.plusMonths(1);
        }
        else{
            data = Modelo.getInstance().dataAtual;
        }
        
        Label label = (Label) vBox.getChildren().get(0);

        Modelo.getInstance().labelId = label.getId();

        Modelo.getInstance().eventoDiaSelecionado = Integer.parseInt(label.getText());
        Modelo.getInstance().eventoMesSelecionado = data.getMonthValue();
        Modelo.getInstance().eventoAnoSelecionado = data.getYear();
    }
    
    private void abreAddEventoPopup(VBox vBox, MouseEvent e) {
        
        setEventoDateInModelo(vBox);
        
        try {
            int xOffSet = 5;
            Label label = new Label("Novo Evento");
            label.getStylesheets().add("CSS/CalendarioCSS.css");
            label.getStyleClass().add("Label-atividade");
            label.setStyle("-fx-background-color: #6eb3ef; -fx-border-color: #6eb3ef");
            label.setMaxWidth(Double.MAX_VALUE);
            
            AnchorPane popupRoot = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("AddEvento/AddEventoFXML.fxml"));
            JFXPopup popup = new JFXPopup(popupRoot);
            
            popup.setOnShowing( (evtShowing) -> {
                vBox.getChildren().add(label);
            });
            
            popup.setOnHiding( (evtHiding) -> {
                vBox.getChildren().remove(label);
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
        catch (Exception exception) {
            Modelo.getInstance().showAlertErro(exception.getMessage());
        }     
    }

    private void carregaEventos(){
        DaoEvento daoEvento = new DaoEvento();
        ObservableList<Evento> listaEvento = daoEvento.getListEventoForCalendario();
        
        int anoAtual = Modelo.getInstance().dataAtual.getYear();
        int mesAtual = Modelo.getInstance().dataAtual.getMonthValue();    
        
        for(Evento evento : listaEvento){
            for(Node node: calendarioGridPane.getChildren()){
                
                VBox vBox = (VBox) node;
                
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
        
        eventoAnimation();
    }

    private void eventoAnimation(){
        if(isEventoAnimation){
            isEventoAnimation = false;
            TransitionAnimation scale = new TransitionAnimation();
            scale.setScaleAnimation(labelEventoAnimation, 0.2, 0.2, 200);
        }
    }
    
    private void adicionaEvento(int mes, int ano, VBox vBox, Evento evento){
        if(ano == evento.getData().toLocalDate().getYear()){
            
            if(mes == evento.getData().toLocalDate().getMonthValue()){

                Label label = (Label) vBox.getChildren().get(0);
                int dia = evento.getData().toLocalDate().getDayOfMonth();
                
                if(Integer.parseInt(label.getText()) == dia){
                    
                    Label labelEvento = new Label(); 
                    labelEvento.getStylesheets().add("CSS/CalendarioCSS.css");
                    labelEvento.getStyleClass().add("Label-atividade");
                    labelEvento.setMaxWidth(Double.MAX_VALUE);
                    
                    //Monta uma string para setar a cor do background e da borda da label
                    StringBuilder colorString = new StringBuilder();
                    
                     if(evento.getSessaoId() != null){ //nulo pq é uma String
                        DaoAtividade daoAtividade = new DaoAtividade();
                        DaoSessao daoSessao = new DaoSessao();
                        DaoFazenda daoFazenda = new DaoFazenda();
                        
                        Sessao sessao = daoSessao.getSessaoByEventoId(evento.getId()); //Para pegar a cor da Sessão
                        Fazenda fazenda = daoFazenda.getFazendaById(sessao.getFazendaId());
                        Atividade atv = daoAtividade.getAtividadeById(evento.getAtividadeId());

                        labelEvento.setId(Integer.toString(evento.getId())); //Armazena o id do Evento na label
                        labelEvento.setText(evento.getSessaoId() + " - " + fazenda.getSigla().toUpperCase() + " - " + atv.getDescricao());

                        if(isEventoAnimation){
                            if(labelEvento.getId().equals(labelEventoAnimation.getId())){
                                labelEventoAnimation = labelEvento;
                            }
                        }
                        
                        //Monta o estilo em CSS
                        colorString.append("-fx-background-color: #");
                        colorString.append(sessao.getCor().substring(2,8)).append(";\n");
                        
                        LocalDate date = LocalDate.of(ano, mes, dia);
                        Date sqlDate = Date.valueOf(date);
                        
                        if(Verify.hasEventOnDate(sqlDate) && evento.getConfirmado() == 0){
                            Label labelListView = new Label();
                            labelListView.setText(labelEvento.getText());
                            labelListView.setId(labelEvento.getId());
                            
                            if(atv.getTipo().equals("Principal") || atv.getTipo().equals("Importante")){
                                colorString.append("-fx-border-color: #ff0206");
                            }
                            else{
                                colorString.append("-fx-border-color: #000000");
                            }    
                        }
                        else{
                            colorString.append("-fx-border-color: #").append(sessao.getCor().substring(2,8));
                        }
                        
                        //Adiciona o estilo montado na label
                        labelEvento.setStyle(colorString.toString());
                    }
                    else if(evento.getCompromissoId() != 0){ //Em SQL, para int, nulo = 0
                        DaoCompromisso dao = new DaoCompromisso();
                        Compromisso compromisso = dao.getCompromissoById(evento.getCompromissoId());
                        
                        labelEvento.setText(compromisso.getDescricao());
                        labelEvento.setId(Integer.toString(evento.getId()));
                        
                        //Monta o estilo em CSS
                        colorString.append("-fx-background-color: #");
                        colorString.append(compromisso.getCor().substring(2,8)).append(";\n");
                        
                        colorString.append("-fx-border-color: #").append(compromisso.getCor().substring(2,8));
                        
                        //Adiciona o estilo montado na label
                        labelEvento.setStyle(colorString.toString());
                    }
                    
                    labelEvento.setOnMouseClicked( (e) -> {
                        if (e.getClickCount() == 1 && !e.isConsumed() && whichCompromissoIsSelected.isEmpty()) {
                            DaoSessao daoSessao = new DaoSessao();
                            DaoEvento daoEvento = new DaoEvento();

                            //Pega o id do Evento que está armazenada na label
                            Modelo.getInstance().evento = daoEvento.getEventoById(Integer.parseInt(labelEvento.getId()));
                            if(Modelo.getInstance().evento.getCompromissoId() == 0){
                                try {
                                    AnchorPane popupRoot = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("DescricaoEvento/DescricaoEventoFXML.fxml"));
                                    JFXPopup popup = new JFXPopup(popupRoot);

                                    double centerX = (rootPane.getWidth()/2 - popupRoot.getPrefWidth()/2);
                                    double centerY = (rootPane.getHeight()/2 - popupRoot.getPrefHeight()/2 - 80);

                                    Modelo.getInstance().popup = popup;
                                    SnackbarModel.pane = rootPane;

                                    popup.show(SnackbarModel.pane, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, centerX, centerY);

                                } 
                                catch (Exception error) {
                                    Modelo.getInstance().showAlertErro(error.getMessage());
                                }
                            }
                            else{
                                try {
                                    AnchorPane popupRoot = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("DescricaoEvento/DescricaoCompromissoFXML.fxml"));
                                    JFXPopup popup = new JFXPopup(popupRoot);

                                    double centerX = (rootPane.getWidth()/2 - popupRoot.getPrefWidth()/2);
                                    double centerY = (rootPane.getHeight()/2 - popupRoot.getPrefHeight()/2 - 80);

                                    Modelo.getInstance().popup = popup;
                                    SnackbarModel.pane = rootPane;

                                    popup.show(SnackbarModel.pane, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, centerX, centerY);

                                } 
                                catch (Exception error) {
                                    Modelo.getInstance().showAlertErro(error.getMessage());
                                }
                            }
                        }
                    });
                    
                    labelEvento.setOnMouseEntered( (e) -> {
                        labelEvento.setCursor(Cursor.OPEN_HAND);
                    });
                    
                    labelEvento.setOnMouseExited((e) ->{
                        labelEvento.setCursor(Cursor.DEFAULT);
                    });
                    
                    labelEvento.setOnMousePressed((e) ->{
                        labelEvento.setCursor(Cursor.CLOSED_HAND);
                    });
                    
                    setDragAndDropEventSource(labelEvento);
                    
                    
                    vBox.getChildren().add(labelEvento);
                }
            }
        }
    }
    
    public void atualizaCalendario(){
        carregaCalendarioLabels();
        carregaEventos();
        populateListConfirmacao();
    }
    
    private void inicializaComboMes(){
        ObservableList<String> listaMeses = FXCollections.observableArrayList();
        LocalDate data = Modelo.getInstance().dataAtual;
        for(int i = 1; i < 13; i++){
            listaMeses.add(Modelo.getInstance().getStringMes(i));
        }
        
        comboMes.setItems(listaMeses);
        comboMes.getSelectionModel().select(data.getMonthValue() - 1);
        
        comboMes.setOnAction((event) -> {
            LocalDate data2 = Modelo.getInstance().dataAtual; 

            int mes = comboMes.getSelectionModel().getSelectedIndex() + 1;
            int ano = data2.getYear();
            
            Modelo.getInstance().dataAtual = data2.of(ano, mes, 1);

            atualizaCalendario();
        });
    }
    
    private void inicializaSpinnerAno(){
        int ano = Modelo.getInstance().dataAtual.getYear();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2015, 2100, ano);
        spinnerAno.setValueFactory(valueFactory);
        
        spinnerAno.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                LocalDate data = Modelo.getInstance().dataAtual;
                Modelo.getInstance().dataAtual = data.of(newValue, data.getMonthValue(), 1);
                Modelo.getInstance().dataAtualAno = data.of(newValue, data.getMonthValue(), 1);
                if(centerStackPane.getChildren().contains(vBoxCalendario)) {
                    atualizaCalendario();
                } else if(centerStackPane.getChildren().contains(differentCalendarViewRootPane)){
                    try {
                        differentCalendarViewRootPane = (HBox) FXMLLoader.load(getClass().getClassLoader().getResource("TelaAno/TelaAno.fxml"));
                        centerStackPane.getChildren().clear();
                        centerStackPane.getChildren().add(differentCalendarViewRootPane);
                    } catch (IOException ex) {
                        Modelo.getInstance().showAlertErro("Erro atualizar tela Ano: " + ex.getMessage());
                    }
                }
            }
        });
    }
    
    private void inicializaData(){
        Modelo.getInstance().dataAtual = LocalDate.now();
        Modelo.getInstance().dataAtualAno = LocalDate.now();
        Modelo.getInstance().dataHoje = Modelo.getInstance().dataAtual;
    }
    
    private void inicializaTudo(){
        inicializaCalendarioGridPane();
        inicializaCabecarioSemana();
        inicializaData();
        inicializaComboMes();
        inicializaSpinnerAno();
    }
    
    public void populateVBoxCompromissos(){
        whichCompromissoIsSelected = "";
        vBoxCompromissos.getChildren().clear();
        
        DaoCompromisso daoCompromisso = new DaoCompromisso();
        ObservableList<Compromisso> listCompromisso = daoCompromisso.getListCompromisso();
        
        for(Compromisso compromisso : listCompromisso){
            if (compromisso.getTipo().equals("V")) {
                Label label = new Label(compromisso.getDescricao());
                label.setPadding(new Insets(5));
                label.setPrefWidth(90);
            
                HBox hbox = new HBox();
                hbox.getStylesheets().add("CSS/CalendarioCSS.css");
                hbox.getStyleClass().add("HBox");

                //Botão Excluir em Compromisso
                JFXButton buttonRemove = new JFXButton("X");
                buttonRemove.setPadding(new Insets(5));
                buttonRemove.setOnAction( (evtClose) ->{
                    if(Modelo.getInstance().showAlertConfirm("Também será excluído os Eventos atrelados a esse Compromisso."
                                                             + " Deseja Continuar?")){

                        SnackbarModel.pane = rootPane;
                        daoCompromisso.removeCompromisso(compromisso);

                        populateVBoxCompromissos();
                        atualizaCalendario();
                    }
                });

                //Botão Editar em Compromisso
                JFXButton buttonEdit = new JFXButton("...");
                buttonEdit.setPadding(new Insets(5));
                buttonEdit.setOnAction( (evtEdit) ->{
                    openCompromissoPopup(Integer.parseInt(hbox.getId()));
                });
                
                /*  
                    Cria uma StringBuilder para atribuir a cor no ColorPicker
                    Cria uma região de 5 pixels para posicionar corretamente o ColorPicker
                    Cria um HBox para alinhar o ColorPicker ao centro
                    Cria o ColorPicker, desativa a visualização da sua label e add uma função para atualizar a cor
                    Adiciona a região, o hbox e a label da descrição no hbox do compromisso
                */
                Region region = new Region();
                region.setMinWidth(5);
                region.setPrefWidth(5);
                
                HBox colorBox = new HBox();
                colorBox.setAlignment(Pos.CENTER);
                colorBox.setPrefSize(15, 15);
                colorBox.setMinSize(15, 15);
                
                JFXColorPicker colorPicker = new JFXColorPicker();
                colorPicker.setValue(Color.web(compromisso.getCor()));
                colorPicker.setMinSize(15, 15);
                colorPicker.setPrefSize(15, 15);
                colorPicker.setPadding(new Insets(5, 0, 0, 0));
                colorPicker.setStyle("-fx-color-label-visible: false ;");
                colorPicker.setOnAction( (evt) -> {
                    compromisso.setCor(colorPicker.getValue().toString());
                    daoCompromisso.updateCompromissoCor(compromisso);
                    atualizaCalendario();
                });

                colorBox.getChildren().add(colorPicker);
                
                //Select do Compromisso
                hbox.getChildren().addAll(region, colorBox, label);
                hbox.setId(Integer.toString(compromisso.getId())); //Fica mais fácil procurar pelo ID do que pela descrição
                hbox.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    if(e.getClickCount() == 1 && !e.isConsumed()){
                        selectCompromisso(hbox);
                    }
                });

                hbox.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> {
                    hbox.getChildren().add(buttonEdit);
                    hbox.getChildren().add(buttonRemove);
                });
                
                hbox.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> {
                    hbox.getChildren().remove(buttonEdit);
                    hbox.getChildren().remove(buttonRemove);
                });
                
                vBoxCompromissos.getChildren().add(hbox);
//                atualizaCalendario();
            }
        }
    }
    
    private void selectCompromisso(HBox hbox){
        //Se tiver nenhum selecionado
        if(whichCompromissoIsSelected.isEmpty()){
            hbox.setStyle("-fx-background-color: #FFA09B");
            whichCompromissoIsSelected = hbox.getId();
        }
        //Se o que estiver selecionado for clicado de novo
        else if(whichCompromissoIsSelected.equals(hbox.getId())){
            hbox.setStyle("-fx-background-color: #FFFFFF");
            whichCompromissoIsSelected = "";
        }
        //Se o que for clicado for diferente do que está selecionado
        else{
            //Limpa tudo
            for(int i = 0; i < vBoxCompromissos.getChildren().size(); i++){
                    vBoxCompromissos.getChildren().get(i).setStyle("-fx-background-color: #FFFFFF");
            }
            
            //Colore o que foi selecionado
            whichCompromissoIsSelected = hbox.getId();
            hbox.setStyle("-fx-background-color: #FFA09B");
        }
    }
    
    @FXML
    private void openCompromissoPopup(ActionEvent evt){
        Modelo.getInstance().idCompromisso = -1;
        
        loadCompromissoPopup();
    }

    //Carrega a Popup com o Compromisso selecionado
    private void openCompromissoPopup(int idCompromisso){
        Modelo.getInstance().idCompromisso = idCompromisso;
        
        loadCompromissoPopup();
    }
    
    private void loadCompromissoPopup() {
        try {
            VBox popupRoot = (VBox) FXMLLoader.load(getClass().getClassLoader().getResource("AddCompromisso/CompromissoFXML.fxml"));
            JFXPopup popup = new JFXPopup(popupRoot);
            
            Modelo.getInstance().popup = popup;
            SnackbarModel.pane = rootPane;
            
            popup.show(buttonCompromisso, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 39, 0);
        } catch (Exception e) {
            Modelo.getInstance().showAlertErro(e.getMessage());
        }
    }

    private void setDragAndDropEventSource(Label label){
        
        label.setOnDragDetected( (e) -> {
            SnackbarModel.pane = rootPane;
            
            Dragboard dragboard = label.startDragAndDrop(TransferMode.MOVE);

            ClipboardContent content = new ClipboardContent();
            content.put(DRAG_EVENT, true);
            
            dragboard.setContent(content);
            
            e.consume();
        });
        
        label.setOnDragDone( (e) -> {
            atualizaCalendario();
            
            e.consume();
        });
    }
    
    private void setDragAndDropEventTarget(VBox vBox){
        Label label = new Label("Solte Aqui.");
        label.getStylesheets().add("CSS/CalendarioCSS.css");
        label.getStyleClass().add("Label-atividade");
        label.setMaxWidth(Double.MAX_VALUE);
        
        vBox.setOnDragEntered( (e) -> {
            Label labelEvento = (Label) e.getGestureSource();
            VBox vParent = (VBox) labelEvento.getParent();
            
            if(!vBox.equals(vParent)){
                label.setStyle(labelEvento.getStyle());
                label.setOpacity(0.90);
                vBox.getChildren().add(label);
            }

            e.consume();
        });
        
        vBox.setOnDragExited( (e) -> {
            Label labelEvento = (Label) e.getGestureSource();
            VBox vParent = (VBox) labelEvento.getParent();
            
            if(!vBox.equals(vParent)){
                vBox.getChildren().remove(label);
            }

            e.consume();
        });
        
        vBox.setOnDragOver( (e) -> {
            Label labelEvento = (Label) e.getGestureSource();
            VBox vParent = (VBox) labelEvento.getParent();
            
            if(e.getDragboard().hasContent(DRAG_EVENT) && !vBox.equals(vParent)) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
        
            e.consume();
        });
        
        vBox.setOnDragDropped( (e) -> {
            boolean dragCompleted = false;

            // Transfer the data to the target
            Dragboard dragboard = e.getDragboard();
            
            Label labelEvento = (Label) e.getGestureSource();
            VBox vParent = (VBox) labelEvento.getParent();
            
            if(!vBox.equals(vParent)){
                
                if(dragboard.hasContent(DRAG_EVENT))
                {
                    setEventoDateInModelo(vBox);
                    int dia = Modelo.getInstance().eventoDiaSelecionado;
                    int mes = Modelo.getInstance().eventoMesSelecionado;
                    int ano = Modelo.getInstance().eventoAnoSelecionado;
                    
                    LocalDate data = LocalDate.of(ano, mes, dia);
                    Date sqlDate = Date.valueOf(data);
                    
                    DaoEvento daoE = new DaoEvento();
                    Evento evento = daoE.getEventoById(Integer.parseInt(labelEvento.getId()));

                    //Se evento.getCompromissoId() for diferente de 0, quer dizer que o evento é do tipo Compromisso
                    //Se for igual a 0, então é um evento do tipo Atividade
                    if(evento.getCompromissoId() != 0){
                        evento.setData(sqlDate);
                        
                        daoE.updateDateEvento(evento);
                    }
                    else{
                        DaoAtividade daoA = new DaoAtividade();
                        Atividade atividade = daoA.getAtividadeById(evento.getAtividadeId());
                        
                        //Se a Atividade for Principal, ele atualiza todos os eventos da sessão conforme a nova data
                        if(atividade.getTipo().equals("Principal")){
                            DaoSessao daoS = new DaoSessao();
                            
                            Sessao sessao = daoS.getSessaoByEventoId(evento.getId());
                            sessao.setDataAbertura(sqlDate);
                            
                            daoS.updateSessaoDate(sessao);
                            daoE.updateSessaoEvento(sessao);
                        }
                        //Se a Atividade não for principal, ele atualiza apenas o evento dessa Atividade
                        else{
                            evento.setData(sqlDate);
                            daoE.updateDateEvento(evento);
                        }
                    }
                    
                    // Data transfer is successful
                    dragCompleted = true;
                }
            }
            e.setDropCompleted(dragCompleted);
            e.consume();
        });
    }
    
    private void populateListConfirmacao(){
        vBoxConfirmacao.getChildren().clear();
        
        HBox hBox;
        Label label;
        Sessao sessao;
        Fazenda fazenda;
        Atividade atividade;
        
        DaoSessao daoS = new DaoSessao();
        DaoEvento daoE = new DaoEvento();
        DaoFazenda daoF = new DaoFazenda();
        DaoAtividade daoA = new DaoAtividade();
        
        Date data = Date.valueOf(Modelo.getInstance().dataHoje);
        ObservableList<Evento> listEvento = daoE.getListEventoNotConfirmed(data);
        
        if(!listEvento.isEmpty()){
            for(Evento evento : listEvento){
                StringBuilder text = new StringBuilder();

                sessao = daoS.getSessaoById(evento.getSessaoId());
                fazenda = daoF.getFazendaById(sessao.getFazendaId());
                atividade = daoA.getAtividadeById(evento.getAtividadeId());
                
                text.append(evento.getSessaoId()).append(" - ").append(fazenda.getSigla()).append(" - ").append(atividade.getDescricao());
                
                label = new Label(); 
                label.setText(text.toString());
                label.setId(Integer.toString(evento.getId()));
                
                label.setPrefWidth(vBoxConfirmacao.getPrefWidth());
                
                hBox = new HBox(label);
                hBox.getStylesheets().add("CSS/CalendarioCSS.css");
                
                hBox.setOnMouseClicked( (e) -> {
                    
                    openMes();
                    
                    isEventoAnimation = true;
                    labelEventoAnimation.setId(Integer.toString(evento.getId()));
                    
                    if(!isEventShowing(evento)){
                        Modelo.getInstance().dataAtual = evento.getData().toLocalDate();
                        
                        spinnerAno.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2015, 2100, Modelo.getInstance().dataAtual.getYear()));
                        comboMes.getSelectionModel().select(Modelo.getInstance().dataAtual.getMonthValue() - 1);
                    }
                    else{
                        atualizaCalendario();
                    }
                });
                
                if(atividade.getTipo().equals("Principal") || atividade.getTipo().equals("Importante")){
                    hBox.getStyleClass().add("HBox-Confirmacao-Important");
                }
                else{
                    hBox.getStyleClass().add("HBox-Confirmacao-notImportant");
                    
                }

                JFXCheckBox checkBoxConfirmation = new JFXCheckBox();
                checkBoxConfirmation.setPadding(new Insets(1));
                
                checkBoxConfirmation.setOnAction((e3) -> {
                    evento.setConfirmado(1);
                    DaoEvento daoEvento = new DaoEvento();
                    daoEvento.updateConfirmadoEvento(evento);
                    atualizaCalendario();
                });

                hBox.getChildren().add(checkBoxConfirmation);
                
                vBoxConfirmacao.getChildren().add(hBox);
            }
        }
        else{
            label = new Label("Lista Vazia");
            vBoxConfirmacao.getChildren().add(label);
        }
    }
    
    private boolean isEventShowing(Evento evento){
        LocalDate dateEvento = evento.getData().toLocalDate();
        LocalDate dateAtual = Modelo.getInstance().dataAtual;
        
        if(dateEvento.getYear() == dateAtual.getYear()){
            if(dateEvento.getMonthValue() == dateAtual.getMonthValue()){
                return true;
            }
        }
        return false;
    }
    
    private void showTodayDate(StringBuilder stringColor, VBox vBoxDay){    //Destacando o dia atual no calendário
        LocalDate todayDate = LocalDate.now();
        LocalDate showingDate = Modelo.getInstance().dataAtual;
        
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
        
        /*
        Se DataAtual.YEAR == DataHoje.YEAR {
            Se DataAtual.Month - 1 == DataHoje.Month {
                //anterior
                Se DiaLabel == DataHoje.Day {
                
                }      
            }
            Se DataAtual.Month == DataHoje.Month {
                //atual
                Se DiaLabel == DataHoje.Day {
                
                }
            }
            Se DataAtual.Month + 1 == DataHoje.Month {
                //posterior
                Se DiaLabel == DataHoje.Day {
                
                }
            }
            Se Não {
                //Nenhum
            }
        }
        */
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        leftVBox.getStyleClass().add("edge-to-edge");
        scrollPaneConfirmacao.getStyleClass().add("edge-to-edge");
        
        differentCalendarViewRootPane.setAlignment(Pos.CENTER);
        differentCalendarViewRootPane.setPrefSize(centerStackPane.getWidth(), centerStackPane.getHeight());
        
        SnackbarModel.pane = rootPane;
        
        Modelo.setMainController(this);
        
        inicializaTudo();
        atualizaCalendario();
        populateVBoxCompromissos();
    }
}
