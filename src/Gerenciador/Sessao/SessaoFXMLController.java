package Gerenciador.Sessao;

import BancoDeDados.DaoSessao;
import Classes.Sessao;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SessaoFXMLController implements Initializable {

    @FXML
    private TableView<Sessao> tableSessaoAber;
    
    @FXML
    private TableView<Sessao> tableSessaoEnc;
    
    @FXML
    private TableColumn<Sessao, String> columnIdAber;
    
    @FXML
    private TableColumn<Sessao, String> columnNameAber;
    
    @FXML
    private TableColumn<Sessao, String> columnIdEnc;
    
    @FXML
    private TableColumn<Sessao, String> columnNameEnc;
    
    private ObservableList<Sessao> listAllAberta() {
        DaoSessao daoS = new DaoSessao();
        return daoS.getListSessaoByEstado("A");
    }
    
    private ObservableList<Sessao> listAllEncerrada() {
        DaoSessao daoS = new DaoSessao();
        return daoS.getListSessaoByEstado("E");
    }
    
    public void startTableAberta() {
        
        columnIdAber.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNameAber.setCellValueFactory(new PropertyValueFactory<>("protocoloDescricao"));
        columnIdAber.setSortType(TableColumn.SortType.ASCENDING);
        
        tableSessaoAber.setItems(listAllAberta());
        tableSessaoAber.getSortOrder().add(columnIdAber);
        tableSessaoAber.sort();
    }
    
    public void startTableEncerrada() {
        
        columnIdEnc.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNameEnc.setCellValueFactory(new PropertyValueFactory<>("protocoloDescricao"));
        columnIdEnc.setSortType(TableColumn.SortType.ASCENDING);
        
        tableSessaoEnc.setItems(listAllEncerrada());
        tableSessaoEnc.getSortOrder().add(columnIdEnc);
        tableSessaoEnc.sort();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startTableAberta();
        startTableEncerrada();
    }    
    
}
