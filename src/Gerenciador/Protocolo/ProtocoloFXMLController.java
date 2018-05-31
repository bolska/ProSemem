package Gerenciador.Protocolo;

import BancoDeDados.DaoProtocolo;
import Classes.Protocolo;
import Classes.ProtocoloProperty;
import Model.Modelo;
import Model.SnackbarModel;
import RegrasNegocio.Verify;
import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ProtocoloFXMLController implements Initializable {
    
    @FXML
    public TableView<ProtocoloProperty> tableProtocolo;
    
    @FXML
    private TableColumn<ProtocoloProperty, String> columnDescr;
    
    @FXML
    private AnchorPane rootPane;
    
    @FXML
    private AnchorPane paneAtividade;
    
    @FXML
    private JFXTextArea textAreaObs;
    
    @FXML
    private JFXButton buttonSalvar;
    
    @FXML
    private void tableClickedEvent(MouseEvent evt){
        if(evt.getClickCount() == 1 && !evt.isConsumed() && !tableProtocolo.getSelectionModel().isEmpty()){
            if(Modelo.getInstance().protocolo != tableProtocolo.getSelectionModel().getSelectedItem().getProtocolo()){
                
                Modelo.getInstance().protocolo = tableProtocolo.getSelectionModel().getSelectedItem().getProtocolo();
                Modelo.getInstance().pane = rootPane;
                
                Modelo.atividadeController.iniciaTabela();  
                Modelo.atividadeController.textAreaAtividadeObs.clear();
                Modelo.atividadeController.textAreaAtividadeObs.disableProperty().set(true);
                Modelo.atividadeController.buttonSalvar.disableProperty().set(true);
                
                buttonSalvar.disableProperty().set(false);
                textAreaObs.disableProperty().set(false);
                textAreaObs.clear();
                
                if(Modelo.getInstance().protocolo.getObs() != null){
                    textAreaObs.setText(Modelo.getInstance().protocolo.getObs());
                }
            }
        }
    }
    
    @FXML
    private void buttonExcluir(ActionEvent evt){
        if (tableProtocolo.getSelectionModel().isEmpty()) {
            Modelo.getInstance().showAlertErro("Selecione um protocolo.");
        }
        else if(Verify.hasEvent(tableProtocolo.getSelectionModel().getSelectedItem().getProtocolo())){
            if(Modelo.getInstance().showAlertConfirm("Excluir um Protocolo também irá excluir suas Atividade e seus"
                    + " Eventos cadastrados. Deseja continuar?")){

                DaoProtocolo daoProtocolo = new DaoProtocolo();
                daoProtocolo.removeProtocolo(tableProtocolo.getSelectionModel().getSelectedItem().getId());

                iniciaTabela();
                Modelo.atividadeController.iniciaTabela();
                Modelo.getMainController().atualizaCalendario();
            }
        }
        else{
            DaoProtocolo daoProtocolo = new DaoProtocolo();
            daoProtocolo.removeProtocolo(tableProtocolo.getSelectionModel().getSelectedItem().getId());

            iniciaTabela();
            Modelo.atividadeController.iniciaTabela();
            Modelo.getMainController().atualizaCalendario();
        }
    }

    @FXML
    private void buttonAddProtocolo(ActionEvent evt){
        try {
            AnchorPane popupRoot = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Gerenciador/Protocolo/AddProtocolo/AddProtocoloFXML.fxml"));
            JFXPopup popup = new JFXPopup(popupRoot);

            double centerX = (rootPane.getPrefWidth()/2 - popupRoot.getPrefWidth()/2);
            double centerY = tableProtocolo.getLayoutY() + 1;
            
            Modelo.getInstance().popup = popup;
            SnackbarModel.pane = rootPane;
            
            popup.show(rootPane, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, centerX, centerY);
            
        } catch (Exception e) {
            Modelo.getInstance().showAlertErro(e.getMessage());
        }
//        try {
//            Stage stage = new Stage();
//            new AddProtocoloLoader().start(stage);
//        } 
//        catch (Exception e) {
//             System.out.println(e);
//        }
    }
    
    @FXML
    private void buttonSalvarObsP(ActionEvent event) {
        Protocolo proto = Modelo.getInstance().protocolo;
        if(proto.getObs() == null){
            updateObs(Modelo.getInstance().protocolo);
        }
        else if(!proto.getObs().equals(textAreaObs.getText())){
            updateObs(proto);
        }
        else{
            Modelo.getInstance().showAlertErro("Não houve nenhuma alteração no campo Observação.");
            
        }
    }
    
    private void updateObs(Protocolo proto){
        proto.setObs(textAreaObs.getText());
        DaoProtocolo daoP = new DaoProtocolo();
        daoP.atualizaProtocolo(proto);
    }
    
    private ObservableList<ProtocoloProperty> listaTodos() {
        DaoProtocolo daoProtocolo = new DaoProtocolo();
        
        ObservableList<ProtocoloProperty> lista = FXCollections.observableArrayList();
        daoProtocolo.getListProtocolo().forEach(protocolo ->{
            lista.add(new ProtocoloProperty(protocolo));
        });
        
        return lista;
    }
   
    public void iniciaTabela() {
//        colunaID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnDescr.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        columnDescr.setSortType(TableColumn.SortType.ASCENDING);
        
        tableProtocolo.setEditable(true);
        tableProtocolo.setItems(listaTodos());
        tableProtocolo.getSortOrder().add(columnDescr);
        tableProtocolo.sort();
        
        columnDescr.setCellFactory(TextFieldTableCell.forTableColumn());
        columnDescr.setOnEditCommit(e ->{
            if(e.getOldValue().trim().equals(e.getNewValue().trim())){
                e.consume();
            }
            else if(!e.getNewValue().trim().isEmpty()){
                ProtocoloProperty protocoloP = tableProtocolo.getSelectionModel().getSelectedItem();
                protocoloP.setDescricaoProperty(e.getNewValue().trim());
                if(!Verify.hasEqual(protocoloP.getProtocolo())){
                    DaoProtocolo daoP = new DaoProtocolo();
                    daoP.atualizaProtocolo(protocoloP.getProtocolo());
                    Modelo.getMainController().atualizaCalendario();
                }
                else{
                    Modelo.getInstance().showAlertErro("Já possui um Protocolo com o mesmo nome.");
                    iniciaTabela();
                }
            }
            else{
                Modelo.getInstance().showAlertErro("Descrição vazia.");
                iniciaTabela();
            }
        });
    }
    
    private void iniciaAtividadePane(){
        try {
            AnchorPane subPane = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Gerenciador/Atividade/AtividadeFXML.fxml"));
            paneAtividade.getChildren().add(subPane);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public boolean isTableSelected(){
        if(!tableProtocolo.getSelectionModel().isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Modelo.getInstance().setProcotoloController(this);
        iniciaTabela();
        iniciaAtividadePane();
        
        textAreaObs.disableProperty().set(true);
        buttonSalvar.disableProperty().set(true);
    }    
    
}
