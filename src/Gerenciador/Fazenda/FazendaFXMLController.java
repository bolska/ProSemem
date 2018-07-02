package Gerenciador.Fazenda;

import BancoDeDados.DaoEncarregado;
import BancoDeDados.DaoFazenda;
import Classes.Encarregado;
import Classes.Fazenda;
import Model.Modelo;
import Model.SnackbarModel;
import RegrasNegocio.Verify;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;



public class FazendaFXMLController implements Initializable {

    @FXML
    private TableView<Fazenda> tableFazenda;
    
    @FXML
    private TableColumn<Fazenda, String> columnNome;
    
    @FXML
    private TableColumn<Fazenda, String> columnSigla;
    
    @FXML
    private TableColumn<Fazenda, String> columnEncarregado;
    
    @FXML
    private JFXTextArea textAreaDescricao;
    
    @FXML
    private AnchorPane rootPane;
    
    @FXML
    private void addButtonFazenda(ActionEvent evt) {
        try {
            AnchorPane popupRoot = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Gerenciador/Fazenda/AddFazenda/AddFazendaFXML.fxml"));
            JFXPopup popup = new JFXPopup(popupRoot);

            double centerX = rootPane.getPrefWidth()/2 - popupRoot.getPrefWidth()/2;
            double centerY = rootPane.getPrefHeight()/2 - popupRoot.getPrefHeight()/2 - 45;
            
            Modelo.getInstance().popup = popup;
            SnackbarModel.pane = rootPane;
            
            popup.show(rootPane, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, centerX, centerY);
            
        } catch (Exception e) {
            Modelo.getInstance().showAlertErro(e.getMessage());
        }
//        try {
//            Stage stage = new Stage();
//            new AddFazendaLoader().start(stage);
//        } catch(Exception e) {
//            System.out.println(e);
//        }
    }
    
    @FXML
    private void delButtonFazenda(ActionEvent evt) {
        if(tableFazenda.getSelectionModel().isEmpty()) {
            Modelo.getInstance().showAlertErro("Selecione uma fazenda");
            
        } else if(Modelo.getInstance().showAlertConfirm("Ao prosseguir irá excluir uma fazenda.\n"
                + "Deseja continuar?")){
            
            DaoFazenda daoF = new DaoFazenda();
            daoF.deleteFazenda(tableFazenda.getSelectionModel().getSelectedItem().getId());
            startTable();
            
        }
    }
    
    @FXML
    private void editableTextArea(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER")){
            Fazenda fazenda = tableFazenda.getSelectionModel().getSelectedItem();
            fazenda.setDescricao(textAreaDescricao.getText());
                
            DaoFazenda daoF = new DaoFazenda();
            daoF.updateFazenda(fazenda);
                
            textAreaDescricao.setEditable(false);//Pode mudar a forma de edição
        }
    }
    
    @FXML
    private void tableClickedEvent(MouseEvent evt) {
        if(evt.getClickCount() == 1 && !evt.isConsumed()) {
            Fazenda fazenda = tableFazenda.getSelectionModel().getSelectedItem();
            textAreaDescricao.setText(fazenda.getDescricao());
            textAreaDescricao.setEditable(true);
        }
    }
    
    private ObservableList<Fazenda> listAll() {
        DaoFazenda daoFazenda = new DaoFazenda();
        DaoEncarregado daoE = new DaoEncarregado();
        
        ObservableList<Fazenda> listFazenda = daoFazenda.getListFazenda();
        
        listFazenda.forEach(fazenda -> {
            Encarregado encarregado = daoE.getEncarregadoById(fazenda.getEncarregadoId());
            fazenda.setEncarregadoNome(encarregado.getNome());
        });

        return listFazenda;
    }
    
    public void startTable() {
        
        columnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        columnNome.setSortType(TableColumn.SortType.ASCENDING);
        
        columnEncarregado.setCellValueFactory(new PropertyValueFactory<>("encarregadoNome"));
        
        columnSigla.setCellValueFactory(new PropertyValueFactory<>("sigla"));
        
        tableFazenda.setItems(listAll());
        tableFazenda.setEditable(true);
        tableFazenda.getSortOrder().add(columnNome);
        tableFazenda.sort();
        
        
        columnNome.setCellFactory(TextFieldTableCell.forTableColumn());
        columnNome.setOnEditCommit(e -> {
            if(e.getOldValue().trim().equals(e.getNewValue().trim())){
                e.consume();
            }
            else if(!e.getNewValue().trim().isEmpty()){
                Fazenda fazenda = tableFazenda.getSelectionModel().getSelectedItem();
                fazenda.setNome(e.getNewValue().trim());
                
                if(!Verify.hasEqual(fazenda)){
                    DaoFazenda daoF = new DaoFazenda();
                    daoF.updateFazenda(fazenda);
                }
                else{
                    Modelo.getInstance().showAlertErro("Já possui uma Fazenda com o mesmo nome.");
                    startTable();
                }
            }
            else{
                Modelo.getInstance().showAlertErro("Nome vazio.");
                startTable();
            }
        });
        
        columnEncarregado.setCellFactory(TextFieldTableCell.forTableColumn());
        columnEncarregado.setOnEditCommit(e -> {
            if(e.getOldValue().trim().equals(e.getNewValue().trim())){
                e.consume();
            }
            else if(!e.getNewValue().trim().isEmpty()){
                Fazenda fazenda = tableFazenda.getSelectionModel().getSelectedItem();
                
                DaoEncarregado daoEncarregado = new DaoEncarregado();
                Encarregado encarregado = daoEncarregado.getEncarregadoById(fazenda.getEncarregadoId());
                encarregado.setNome(e.getNewValue().trim());
                
                if(!Verify.hasEqual(encarregado)){
                    DaoEncarregado daoE = new DaoEncarregado();
                    daoE.updateEncarregado(encarregado);
                }
                else{
                    Modelo.getInstance().showAlertErro("Já possui um Encarreagdo com o mesmo nome.");
                    startTable();
                }
            }
            else{
                Modelo.getInstance().showAlertErro("Nome vazio.");
                startTable();
            }
        });
        
        columnSigla.setCellFactory(TextFieldTableCell.forTableColumn());
        columnSigla.setOnEditCommit(e -> {
            if(e.getOldValue().trim().equals(e.getNewValue().trim())){
                e.consume();
            }
            else if(!e.getNewValue().trim().isEmpty()){
                Fazenda fazenda = tableFazenda.getSelectionModel().getSelectedItem();
                fazenda.setSigla(e.getNewValue().trim());
                
                if(!Verify.hasEqual(fazenda)){
                    DaoFazenda daoF = new DaoFazenda();
                    daoF.updateFazenda(fazenda);
                    Modelo.getMainController().atualizaCalendario();
                }
                else{
                    Modelo.getInstance().showAlertErro("Já possui uma Sigla igual.");
                    startTable();
                }
            }
            else{
                Modelo.getInstance().showAlertErro("Sigla vazia.");
                startTable();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Modelo.getInstance().setFazendaController(this);
        startTable();
        
        textAreaDescricao.setEditable(false);
    }    
    
}
