package AddEvento;

import BancoDeDados.DaoAtividade;
import BancoDeDados.DaoCompromisso;
import BancoDeDados.DaoEvento;
import BancoDeDados.DaoFazenda;
import BancoDeDados.DaoProtocolo;
import BancoDeDados.DaoSessao;
import Classes.Atividade;
import Classes.Compromisso;
import Classes.Protocolo;
import Model.Modelo;
import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import Classes.Fazenda;
import Classes.Sessao;
import RegrasNegocio.Verify;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;

public class AddEventoController implements Initializable {

    @FXML private Label labelDataSelecionada;
    
    @FXML private JFXComboBox comboBoxProtocolo;
    
    @FXML private JFXComboBox comboBoxFazenda;
    
    @FXML private JFXTextField textFieldSessao;
    
    @FXML private JFXTextField textFieldAvulso;
    
    @FXML private JFXColorPicker colorPicker;
    
    @FXML private JFXColorPicker colorPickerAvulso;

    @FXML private JFXColorPicker colorPickerAvulso;

    @FXML private TableView<Atividade> tableAtividade;
    
    @FXML private TableColumn<Atividade, Integer> columnID;
    
    @FXML private TableColumn<Atividade, String> columnDescr;
    
    @FXML private TableColumn<Atividade, Integer> columnIntervalo;
    
    @FXML private TableColumn<Atividade, String> columnTipo;

    @FXML
    private void buttonAddAvulso(ActionEvent evt){
        if(!textFieldAvulso.getText().trim().isEmpty()){
            DaoCompromisso daoC = new DaoCompromisso();
            DaoEvento daoE = new DaoEvento();
            
            Compromisso compromisso = new Compromisso();
            compromisso.setDescricao(textFieldAvulso.getText().trim());
            compromisso.setCor(colorPickerAvulso.getValue().toString());
            compromisso.setTipo("A");
            
            /*
            *    Se já tiver um compromisso do tipo avulso com a mesma descrição, não insere de novo no banco de dados
            *    Apenas insere um novo evento com esse compromisso
            */
            if(Verify.hasEqual(compromisso)){
                compromisso = daoC.getCompromissoByTipoDescricao(compromisso);
                
                if(!compromisso.getCor().equals(colorPickerAvulso.getValue().toString())){
                    compromisso.setCor(colorPickerAvulso.getValue().toString());
                    daoC.updateCompromissoCor(compromisso);
                }
            }
            else{
                daoC.insertCompromisso(compromisso);
                compromisso = daoC.getCompromissoByTipoDescricao(compromisso); //Para retornar o id
            }

            int ano = Modelo.getInstance().eventoAnoSelecionado;
            int mes = Modelo.getInstance().eventoMesSelecionado;
            int dia = Modelo.getInstance().eventoDiaSelecionado;
        
            LocalDate localDate = LocalDate.of(ano, mes, dia);
            Date sqlDate = Date.valueOf(localDate);
            
            daoE.inserirEvento(compromisso, sqlDate);
            
            Modelo.getMainController().atualizaCalendario();
            closePopup();
        }
        else{
            Modelo.getInstance().popup.setAutoHide(false);
            Modelo.getInstance().showAlertErro("Campo Vazio.");
            Modelo.getInstance().popup.setAutoHide(true);
        }
    }
    
    @FXML
    private void botaoSalvarEventoProtocolo(MouseEvent evt){
        boolean verifyFields = true;
        
        Protocolo protocolo = (Protocolo) comboBoxProtocolo.getSelectionModel().getSelectedItem();
        Fazenda fazenda = (Fazenda) comboBoxFazenda.getSelectionModel().getSelectedItem();
        
        if(protocolo == null){
            verifyFields = false;
            comboBoxProtocolo.setStyle("-fx-border-color: #DD1D36");
        }
        else{
            comboBoxProtocolo.setStyle("-fx-border: transparent");
        }    
        
        if(fazenda == null){
            verifyFields = false;
            comboBoxFazenda.setStyle("-fx-border-color: #DD1D36");
        }
        else{
            comboBoxFazenda.setStyle("-fx-border: transparent");
        }

        if(verifyFields){
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
        DaoFazenda daoF = new DaoFazenda();
        
        Sessao sessao = new Sessao();
        
        Fazenda fazenda = Modelo.getInstance().fazenda;
        fazenda.setCor(colorPicker.getValue().toString());
        daoF.updateFazenda(fazenda);
        
        sessao.setProtocoloId(Modelo.getInstance().protocolo.getId());
        sessao.setFazendaId(fazenda.getId());
        sessao.setId(textFieldSessao.getText());
        sessao.setDataAbertura(sqlDate);
        sessao.setCor(colorPicker.getValue().toString());
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
        ObservableList<Protocolo> listaProtocolo = daoProtocolo.getListProtocolo();
                
        comboBoxProtocolo.setItems(listaProtocolo);
        
        //Configurações do comboBox para por o objeto Protocolo em vez de apenas sua Descrição
        comboBoxProtocolo.setCellFactory((comboBox) -> {
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

        comboBoxProtocolo.setConverter(new StringConverter<Protocolo>() {
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
        comboBoxProtocolo.setOnAction((event) -> {
            Protocolo protocoloSelecionado = (Protocolo) comboBoxProtocolo.getSelectionModel().getSelectedItem();
            carregaTabelaAtividade(protocoloSelecionado);
        });
    }
    
    private void startComboFazenda(){
        DaoFazenda daoF = new DaoFazenda();
        ObservableList<Fazenda> listFazenda = daoF.getListFazenda();
        
        comboBoxFazenda.setItems(listFazenda);
        
        comboBoxFazenda.setCellFactory((comboBox) -> {
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

        comboBoxFazenda.setConverter(new StringConverter<Fazenda>() {
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
        
        comboBoxFazenda.setOnAction( (e) -> {
            Fazenda fazenda = (Fazenda) comboBoxFazenda.getSelectionModel().getSelectedItem();
            
            if(fazenda.getCor() != null){
                colorPicker.setValue(Color.web(fazenda.getCor()));
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
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnDescr.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        columnIntervalo.setCellValueFactory(new PropertyValueFactory<>("intervalo"));
        columnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));   
    }
    
    private void carregaTabelaAtividade(Protocolo protocolo){
        DaoAtividade daoAtividade = new DaoAtividade();
        ObservableList<Atividade> listAtividade = daoAtividade.getListaAtividadeByProtocoloId(protocolo.getId());
        
        tableAtividade.setItems(listAtividade);
        tableAtividade.getSortOrder().add(columnIntervalo);
        tableAtividade.sort();
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

