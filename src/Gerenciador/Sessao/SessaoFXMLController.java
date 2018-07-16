package Gerenciador.Sessao;

import BancoDeDados.DaoSessao;
import Classes.Sessao;

import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

public class SessaoFXMLController implements Initializable {

    @FXML private TableView<Sessao> tableSessaoAber;
    
    @FXML private TableColumn<Sessao, String> columnIdAber;
    
    @FXML private TableColumn<Sessao, String> columnNameAber;
    
    @FXML private TableColumn<Sessao, String> columnFazendaAber;
    
    @FXML private TableColumn<Sessao, Date> columnDataAber;
    
    @FXML private TableView<Sessao> tableSessaoEnc;
    
    @FXML private TableColumn<Sessao, String> columnIdEnc;
    
    @FXML private TableColumn<Sessao, String> columnNameEnc;
    
    @FXML private TableColumn<Sessao, String> columnFazendaEnc;
    
    @FXML private TableColumn<Sessao, Date> columnDataEnc;
    
    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
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
        columnIdAber.setSortType(TableColumn.SortType.ASCENDING);
        
        columnNameAber.setCellValueFactory(new PropertyValueFactory<>("protocoloDescricao"));
        columnFazendaAber.setCellValueFactory(new PropertyValueFactory<>("fazendaNome"));
        columnDataAber.setCellValueFactory(new PropertyValueFactory<>("dataAbertura"));
        
        columnDataAber.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Date>() {
            @Override
            public String toString(Date object) {
                if(object == null){
                    return "";
                }
                else{
                    return dateFormat.format(object);
                }
            }

            @Override
            public Date fromString(String string) {
                return Date.valueOf(string);
            }
        }));
        
        tableSessaoAber.setItems(listAllAberta());
        tableSessaoAber.getSortOrder().add(columnIdAber);
        tableSessaoAber.sort();
    }
    
    public void startTableEncerrada() {
        
        columnIdEnc.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnIdEnc.setSortType(TableColumn.SortType.ASCENDING);
        
        columnNameEnc.setCellValueFactory(new PropertyValueFactory<>("protocoloDescricao"));
        columnFazendaEnc.setCellValueFactory(new PropertyValueFactory<>("fazendaNome"));
        
        columnDataEnc.setCellValueFactory(new PropertyValueFactory<>("dataEncerramento"));
        columnDataEnc.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Date>() {
            @Override
            public String toString(Date object) {
                if(object == null){
                    return "";
                }
                else{
                    return dateFormat.format(object);
                }
            }

            @Override
            public Date fromString(String string) {
                return Date.valueOf(string);
            }
        }));
        
        tableSessaoEnc.setItems(listAllEncerrada());
        tableSessaoEnc.getSortOrder().add(columnIdEnc);
        tableSessaoEnc.setPlaceholder(new Label("Em Desenvolvimento"));
        tableSessaoEnc.sort();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startTableAberta();
        startTableEncerrada();
    }    
    
}
