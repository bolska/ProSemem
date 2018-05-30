/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gerenciador.Atividade;

import BancoDeDados.DaoAtividade;
import BancoDeDados.DaoEvento;
import Classes.Atividade;
import Classes.AtividadeProperty;
import Model.Modelo;
import Model.SnackbarModel;
import RegrasNegocio.Verify;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.converter.IntegerStringConverter;


/**
 * FXML Controller class
 *
 * @author Bolska
 */
public class AtividadeController implements Initializable {

    private Atividade atividade;
    
    @FXML
    private TableView<AtividadeProperty> tableAtividade;

    @FXML
    private TableColumn<AtividadeProperty, String> columnDescr;

    @FXML
    private TableColumn<AtividadeProperty, String> columnTipo;

    @FXML
    private TableColumn<AtividadeProperty, Integer> columnIntervalo;

    @FXML
    private JFXTextArea textAreaAtividadeObs;
    
    @FXML
    void buttonAddAtividade(ActionEvent event) {
        if (Modelo.protocoloController.isTableSelected()) {
            try {
                Pane rootPane = Modelo.getInstance().pane; //Pega o rootPane do Protocolo
                
                AnchorPane popupRoot = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Gerenciador/Atividade/AddAtividade/AddAtividadeFXML.fxml"));
                JFXPopup popup = new JFXPopup(popupRoot);
                
                double centerX = (rootPane.getPrefWidth()/2 - popupRoot.getPrefWidth()/2);
                double centerY = Modelo.protocoloController.tableProtocolo.getLayoutY() + 1;
                
                Modelo.getInstance().popup = popup;
                SnackbarModel.pane = rootPane;
                
                popup.show(SnackbarModel.pane, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, centerX, centerY);
            
            } 
            catch (Exception e) {
                Modelo.getInstance().showAlertErro(e.getMessage());
            }
//            try {
//                Stage stage = new Stage();
//                new AddAtividadeLoader().start(stage);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        else{
            Modelo.getInstance().showAlertErro("Selecione um Protocolo.");
        }
    }
    
    @FXML
    private void buttonExcluir(ActionEvent evt){
        if(tableAtividade.getSelectionModel().isEmpty()){
            Modelo.getInstance().showAlertErro("Selecione uma Atividade.");
        }
        else if(Verify.hasEvent(tableAtividade.getSelectionModel().getSelectedItem().getAtividade())){
            if(Modelo.getInstance().showAlertConfirm("Excluir uma Atividade também irá excluir o Evento em que está"
            + " atrelado, se tiver algum. Deseja continuar?")){

                DaoAtividade daoAtividade = new DaoAtividade();
                DaoEvento daoEvento = new DaoEvento();

                Atividade atv = tableAtividade.getSelectionModel().getSelectedItem().getAtividade();

                daoEvento.removeEventoAtividadeById(atv.getId());
                daoAtividade.removeAtividade(atv.getId());

                iniciaTabela();
                Modelo.getMainController().atualizaCalendario();
            }
        }
        else{
            DaoAtividade daoAtividade = new DaoAtividade();
            DaoEvento daoEvento = new DaoEvento();

            Atividade atv = tableAtividade.getSelectionModel().getSelectedItem().getAtividade();

            daoEvento.removeEventoAtividadeById(atv.getId());
            daoAtividade.removeAtividade(atv.getId());

            iniciaTabela();
            Modelo.getMainController().atualizaCalendario();
        }
    }
    
    @FXML
    void buttonSalvarObs(ActionEvent event) {
        Modelo.getInstance().showAlertInfo("Em desenvolvimento.");
        event.consume();
//        if(atividade.getObs() == null){
//            updateObs(atividade);
//            event.consume();
//        }
//        else if(!atividade.getObs().equals(textAreaAtividadeObs.getText())){
//            updateObs(atividade);
//            event.consume();
//        }
//        else{
//            Modelo.getInstance().showAlertErro("Não houve nenhuma alteração no campo Observação.");
//            
//            event.consume();
//        }
    }
    
    private void updateObs(Atividade atv){
        atv.setObs(textAreaAtividadeObs.getText());
        DaoAtividade daoA = new DaoAtividade();
        daoA.atualizaAtividade(atv);
    }
    
    private ObservableList<AtividadeProperty> listaTodos() {
        DaoAtividade daoAtividade = new DaoAtividade();
        int protocoloId = Modelo.getInstance().protocolo.getId();
        
        ObservableList<AtividadeProperty> listaAtiv = FXCollections.observableArrayList();
        daoAtividade.getListaAtividadeByProtocoloId(protocoloId).forEach(atividade ->{
            listaAtiv.add(new AtividadeProperty(atividade));
        });
        
        return listaAtiv;
    }
   
    public void iniciaTabela() {
        columnDescr.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        columnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        columnIntervalo.setCellValueFactory(new PropertyValueFactory<>("intervalo"));
        columnIntervalo.setSortType(TableColumn.SortType.ASCENDING);
        
        columnDescr.setCellFactory(TextFieldTableCell.forTableColumn());
        columnDescr.setOnEditCommit(e ->{
            if(e.getOldValue().trim().equals(e.getNewValue().trim())){
                e.consume();
            }
            else if(!e.getNewValue().trim().isEmpty()){
                AtividadeProperty atividade = tableAtividade.getSelectionModel().getSelectedItem();
                atividade.setDescricaoProperty(e.getNewValue().trim());

                DaoAtividade daoAtividade = new DaoAtividade();
                daoAtividade.atualizaAtividade(atividade.getAtividade());
                Modelo.getMainController().atualizaCalendario();  
            }
            else{
                Modelo.getInstance().showAlertErro("Descrição vazia.");
                iniciaTabela();
            }
        });
        
        columnIntervalo.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        columnIntervalo.setOnEditCommit(e ->{
            if(e.getOldValue().equals(e.getNewValue())){
                e.consume();
            }
            else if(e.getNewValue() != null){
                AtividadeProperty atividade = tableAtividade.getSelectionModel().getSelectedItem();
                try{
                    atividade.setIntervaloProperty(Integer.parseInt(e.getNewValue().toString()));
                    
                }
                catch(NumberFormatException error){
                    Modelo.getInstance().showAlertErro("Número inválido.");
                }

                DaoAtividade daoAtividade = new DaoAtividade();
                daoAtividade.atualizaAtividade(atividade.getAtividade());
                Modelo.getMainController().atualizaCalendario();   
            }
            else{
                Modelo.getInstance().showAlertErro("Intervalo vazio.");
                iniciaTabela();
            }
        });
        
        ObservableList<String> listaTipos = FXCollections.observableArrayList();
        listaTipos.add("Principal"); 
        listaTipos.add("Importante");
        listaTipos.add("Secundário");
        
        columnTipo.setCellFactory(ComboBoxTableCell.forTableColumn(listaTipos));
        columnTipo.setOnEditCommit(e ->{
            DaoAtividade daoAtividade = new DaoAtividade();
            AtividadeProperty atividade = tableAtividade.getSelectionModel().getSelectedItem();
            if(e.getNewValue().equals("Principal")){
                atividade.setTipoProperty(e.getNewValue());
                atividade.setIntervaloProperty(0);
                
                changeOldPrincipalToImportante(atividade.getProtocoloId());
            }
            else if(e.getOldValue().equals("Principal")){
                atividade.setTipoProperty(e.getNewValue());
                atividade.setIntervaloProperty(-1);
            }
            else{
                atividade.setTipoProperty(e.getNewValue());
            }
            daoAtividade.atualizaAtividade(atividade.getAtividade());
            iniciaTabela();
        });   
        
        tableAtividade.setItems(listaTodos());
        tableAtividade.setPlaceholder(new Label("Lista Vazia."));
        tableAtividade.setEditable(true);
        tableAtividade.getSortOrder().add(columnIntervalo);
        tableAtividade.sort();
        
        tableAtividade.setOnMouseClicked( (e) -> {
            atividade = tableAtividade.getSelectionModel().getSelectedItem().getAtividade();
            textAreaAtividadeObs.disableProperty().set(false);
            if(atividade.getObs() != null){
                textAreaAtividadeObs.setText(atividade.getObs());
            }
        });
    }
    
    private void changeOldPrincipalToImportante(int protoID){
        DaoAtividade daoA = new DaoAtividade();
        Atividade atvP = daoA.getAtividadePrincipalProtocolo(protoID);
        atvP.setTipo("Secundário");
        atvP.setIntervalo(-1);
        daoA.atualizaAtividade(atvP);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Modelo.getInstance().setAtividadeController(this);
        tableAtividade.setPlaceholder(new Label("Selecione um Protocolo."));
        textAreaAtividadeObs.disableProperty().set(true);
    }
}
