/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AddEvento;

import BancoDeDados.DaoAtividade;
import BancoDeDados.DaoFazenda;
import BancoDeDados.DaoProtocolo;
import BancoDeDados.DaoSessao;
import Classes.AtividadeProperty;
import Classes.Protocolo;
import Model.Modelo;
import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import Classes.Fazenda;
import Classes.Sessao;
import RegrasNegocio.Verify;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;

/**
 * FXML Controller class
 *
 * @author Bolska
 */
public class AddEventoController implements Initializable {
    
    @FXML
    private AnchorPane root;

    @FXML
    private Label labelDataSelecionada;
    
    @FXML
    private JFXComboBox comboProtocolo;
    
    @FXML
    private JFXComboBox comboFazenda;
    
    @FXML
    private JFXTextField textFieldSessao;
    
    @FXML
    private JFXColorPicker jfxColorPicker;
    
    //Tabela Atividade
    @FXML
    private TableView<AtividadeProperty> tabelaAtividade;
    @FXML
    private TableColumn<AtividadeProperty, Integer> colunaID;
    @FXML
    private TableColumn<AtividadeProperty, String> colunaDescr;
    @FXML
    private TableColumn<AtividadeProperty, Integer> colunaIntervalo;
    @FXML
    private TableColumn<AtividadeProperty, String> colunaTipo;
    
    
    
    @FXML
    private void botaoSalvarEventoProtocolo(MouseEvent evt){
        boolean passou = true;
        
        Protocolo protocolo = (Protocolo) comboProtocolo.getSelectionModel().getSelectedItem();
        Fazenda fazenda = (Fazenda) comboFazenda.getSelectionModel().getSelectedItem();
        
        if(protocolo == null){
            passou = false;
            comboProtocolo.setStyle("-fx-border-color: #DD1D36");
        }
        else{
            comboProtocolo.setStyle("-fx-border: transparent");
        }    
        
        if(fazenda == null){
            passou = false;
            comboFazenda.setStyle("-fx-border-color: #DD1D36");
        }
        else{
            comboFazenda.setStyle("-fx-border: transparent");
        }

        if(passou){
            Modelo.getInstance().protocolo = protocolo;
            Modelo.getInstance().fazenda = fazenda;
            
            addSessao();
        }
    }
    
    @FXML
    private void closePopup(ActionEvent e) {
        Modelo.getInstance().popup.hide();
    }
    
    private void closePopup() {
        Modelo.getInstance().popup.hide();
    }
    
    private void addSessao() {
        int ano = Modelo.getInstance().eventoAnoSelecionado;
        int mes = Modelo.getInstance().eventoMesSelecionado;
        int dia = Modelo.getInstance().eventoDiaSelecionado;
        
        LocalDate localDate = LocalDate.of(ano, mes, dia);
        Date sqlDate = Date.valueOf(localDate);
        
        DaoSessao daoS = new DaoSessao();
        Sessao sessao = new Sessao();

        sessao.setProtocoloId(Modelo.getInstance().protocolo.getId());
        sessao.setFazendaId(Modelo.getInstance().fazenda.getId());
        sessao.setId(textFieldSessao.getText());
        sessao.setDataAbertura(sqlDate);
        sessao.setCor(jfxColorPicker.getValue().toString());
        sessao.setEstado("A");
        
        if(!Verify.hasEqual(sessao)){
            daoS.insertSessao(sessao);
            Modelo.getMainController().atualizaCalendario();
            Modelo.getInstance().updateLastSessaoId();
            
            closePopup();
        }
        else{
            Modelo.getInstance().popup.setAutoHide(false);
            Modelo.getInstance().showAlertErro("Já possui uma Sessão com esse código.");
            Modelo.getInstance().popup.setAutoHide(true);
        }
    }
    
    private void iniciaComboProtocolo(){
        DaoProtocolo daoProtocolo = new DaoProtocolo();
        ObservableList<Protocolo> listaProtocolo = FXCollections.observableArrayList();
                
        listaProtocolo = daoProtocolo.getListProtocolo();
        comboProtocolo.setItems(listaProtocolo);
        
        //Configurações do comboBox para por o objeto Protocolo em vez de apenas sua Descrição
        comboProtocolo.setCellFactory((comboBox) -> {
            return new ListCell<Protocolo>() {
                @Override
                protected void updateItem(Protocolo p, boolean empty) {
                    super.updateItem(p, empty);

                    if (p == null || empty) {
                        setText(null);
                    } else {
                        setText(p.getDescricao());
                    }
                }
            };
        });

        comboProtocolo.setConverter(new StringConverter<Protocolo>() {
            @Override
            public String toString(Protocolo p) {
                if (p == null) {
                    return null;
                } else {
                    return p.getDescricao();
                }
            }

            @Override
            public Protocolo fromString(String descricaoString) {
                return null; // No conversion fromString needed.
            }
        });
        
        //Evento para carregar a tabela de Atividades
        comboProtocolo.setOnAction((event) -> {
            Protocolo protocoloSelecionado = (Protocolo) comboProtocolo.getSelectionModel().getSelectedItem();
            carregaTabelaAtividade(protocoloSelecionado);
        });
    }
    
    private void startComboFazenda(){
        DaoFazenda daoF = new DaoFazenda();
        List<Fazenda> listFazenda = new ArrayList();
        
        listFazenda = daoF.getListFazenda();
        comboFazenda.setItems(FXCollections.observableArrayList(listFazenda));
        
        comboFazenda.setCellFactory((comboBox) -> {
            return new ListCell<Fazenda>() {
                @Override
                protected void updateItem(Fazenda f, boolean empty) {
                    super.updateItem(f, empty);

                    if (f == null || empty) {
                        setText(null);
                    } else {
                        setText(f.getNome());
                    }
                }
            };
        });

        comboFazenda.setConverter(new StringConverter<Fazenda>() {
            @Override
            public String toString(Fazenda f) {
                if (f == null) {
                    return null;
                } else {
                    return f.getNome();
                }
            }

            @Override
            public Fazenda fromString(String nomeString) {
                return null; // No conversion fromString needed.
            }
        });
    }
    
    private void carregaLabelSelecionada(){
        StringBuilder textoData = new StringBuilder();
        textoData.append(Modelo.getInstance().eventoDiaSelecionado);
        textoData.append(" - ");
        textoData.append(Modelo.getInstance().eventoMesSelecionado);
        textoData.append(" - ");
        textoData.append(Modelo.getInstance().eventoAnoSelecionado);

        labelDataSelecionada.setText(textoData.toString());
    }

    private void iniciaTabelaAtividade() {
        colunaID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaDescr.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaIntervalo.setCellValueFactory(new PropertyValueFactory<>("intervalo"));
        colunaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));   
    }
    
    private void carregaTabelaAtividade(Protocolo protocolo){
        DaoAtividade daoAtividade = new DaoAtividade();
        
        List<AtividadeProperty> lista = new ArrayList<>();
        daoAtividade.getListaAtividadeByProtocoloId(protocolo.getId()).forEach(atividade ->{
            lista.add(new AtividadeProperty(atividade));
        });
        
        ObservableList<AtividadeProperty> obLista = FXCollections.observableArrayList(lista);
        tabelaAtividade.setItems(FXCollections.observableArrayList(obLista));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregaLabelSelecionada();
        iniciaComboProtocolo();
        iniciaTabelaAtividade();
        startComboFazenda();
        textFieldSessao.setText(Modelo.getInstance().lastSessaoId);
    }    
    
}

