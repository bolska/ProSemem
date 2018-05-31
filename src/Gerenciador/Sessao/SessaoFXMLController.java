/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gerenciador.Sessao;

import BancoDeDados.DaoSessao;
import Classes.SessaoProperty;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author user
 */
public class SessaoFXMLController implements Initializable {

    @FXML
    private TableView<SessaoProperty> tableSessaoAber;
    
    @FXML
    private TableView<SessaoProperty> tableSessaoEnc;
    
    @FXML
    private TableColumn<SessaoProperty, String> columnIdAber;
    
    @FXML
    private TableColumn<SessaoProperty, String> columnNameAber;
    
    @FXML
    private TableColumn<SessaoProperty, String> columnIdEnc;
    
    @FXML
    private TableColumn<SessaoProperty, String> columnNameEnc;
    
    private ObservableList<SessaoProperty> listAllAberta() {
        DaoSessao daoS = new DaoSessao();
        
        ObservableList<SessaoProperty> listSessao = FXCollections.observableArrayList();
        
        daoS.getListSessaoByEstado("A").forEach(sessao -> {
            listSessao.add(new SessaoProperty(sessao));
        });
        
        return listSessao;
    }
    
    private ObservableList<SessaoProperty> listAllEncerrada() {
        DaoSessao daoS = new DaoSessao();
        
        ObservableList<SessaoProperty> listSessao = FXCollections.observableArrayList();
        
        daoS.getListSessaoByEstado("E").forEach(sessao -> {
            listSessao.add(new SessaoProperty(sessao));
        });

        return listSessao;
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
