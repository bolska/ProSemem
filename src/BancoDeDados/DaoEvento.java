package BancoDeDados;

import Classes.Atividade;
import Classes.Compromisso;
import Classes.Evento;
import Classes.Protocolo;
import Classes.Sessao;
import Model.Modelo;
import Model.SnackbarModel;
import RegrasNegocio.Verify;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 *
 * @author Bolska
 */
public class DaoEvento {
    
    private final String INSERT_EVENTO_SESSAO = "INSERT INTO EVENTO (EVT_ID, EVT_DATA, SES_ID, PROTO_ID, ATIV_ID, EVT_CONFIRMADO) VALUES (0,?,?,?,?,?)";
    private final String INSERT_EVENTO_COMPROMISSO = "INSERT INTO EVENTO (EVT_ID, EVT_DATA, COMP_ID) VALUES (0,?,?)";
    private final String UPDATE_DATE_EVENTO = "UPDATE EVENTO SET EVT_DATA=?, EVT_CONFIRMADO=? WHERE EVT_ID=?";
    private final String UPDATE_CONFIRMADO_EVENTO = "UPDATE EVENTO SET EVT_CONFIRMADO=? WHERE EVT_ID=?";
    private final String DELETE_EVENTO_BY_ID = "DELETE FROM EVENTO WHERE EVT_ID=?";
    private final String DELETE_EVENTOS_BY_SESSAO_ID = "DELETE FROM EVENTO WHERE SES_ID=?";
    private final String DELETE_EVENTO_BY_ATIVIDADE_ID = "DELETE FROM EVENTO WHERE ATIV_ID=?";
    private final String DELETE_COMPROMISSO_EVENTO = "DELETE FROM EVENTO WHERE COMP_ID=? and EVT_DATA=?";
    private final String DELETE_EVENTO_COMPROMISSO_BY_ID = "DELETE FROM EVENTO WHERE COMP_ID=?";
    private final String COUNT_EVENTO_BY_DATE = "select count(EVT_ID) from EVENTO where EVT_DATA=?";
    private final String LIST_EVENTO_FOR_CALENDARIO = "SELECT * FROM EVENTO WHERE EVT_DATA >= ? AND EVT_DATA <= ?";
    private final String LISTBYDATA_AND_ATIVIDADE_EVENTO = "SELECT * FROM EVENTO WHERE EVT_DATA=? AND ATIV_ID=?";
    private final String LIST_EVENTO_BY_ID = "SELECT * FROM EVENTO WHERE EVT_ID=?";
    private final String LIST_EVENTO_BY_PROTOCOLO_ID = "SELECT * FROM EVENTO WHERE PROTO_ID=?";
    private final String LIST_EVENTO_BY_ATIVIDADE_ID = "SELECT * FROM EVENTO WHERE ATIV_ID=?";
    private final String LIST_EVENTO_BY_SESSAO_ID = "SELECT * FROM EVENTO WHERE SES_ID=?";
    private final String LIST_EVENTO_BY_CONFIRMADO = "select * from EVENTO where EVT_CONFIRMADO = 0 and ATIV_ID != 0 and EVT_DATA > ?";
    private final String LIST_EVENTO_BY_DATE = "SELECT * FROM EVENTO WHERE EVT_DATA=?";
    
    
    public void insertEvento(Sessao sessao){
        if(sessao != null){
            Connection conn = null;
            PreparedStatement pstm = null;

            try{
                conn = Conexao.getConexao();
                
                DaoAtividade daoAtiv = new DaoAtividade();
                ObservableList<Atividade> listAtiv = daoAtiv.getListaAtividadeByProtocoloId(sessao.getProtocoloId());

                for(int i = 0; i <listAtiv.size(); i++){
                    Atividade atv = listAtiv.get(i);
                    LocalDate dataAtiv = sessao.getDataAbertura().toLocalDate().plusDays(atv.getIntervalo());
                    Date sqlData = Date.valueOf(dataAtiv);
                    
                    pstm = conn.prepareStatement(INSERT_EVENTO_SESSAO);
                    pstm.setDate(1, sqlData);
                    pstm.setString(2, sessao.getId());
                    pstm.setInt(3, sessao.getProtocoloId());
                    pstm.setInt(4, atv.getId());

                    if(Verify.hasEventOnDate(sqlData) || Verify.isSunday(sqlData)){
                        pstm.setInt(5, 0);
                    }
                    else{
                        pstm.setInt(5, 1);
                    }
                    
                    pstm.execute();
                    pstm = null;
                }
                
                Conexao.fechaConexao(conn, pstm);    
            }
            catch(Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Mensagem de Erro");
                alert.setContentText("Não foi possível inserir Evento: " + e.getMessage());
                alert.showAndWait();
            }   
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível inserir Evento, parâmetro vazio.");
            alert.showAndWait();
        } 
    }
    
    //Evento Compromisso
    public void inserirEvento(Compromisso compromisso, Date date){
        if(compromisso != null && date != null){
            Connection conn = null;
            PreparedStatement pstm = null;

            try{
                conn = Conexao.getConexao();
                
                pstm = conn.prepareStatement(INSERT_EVENTO_COMPROMISSO);
                pstm.setDate(1, date);
                pstm.setInt(2, compromisso.getId());
                
                SnackbarModel.showSnackbar(SnackbarModel.EVENTO_ADD);
                
                pstm.execute();
                Conexao.fechaConexao(conn, pstm);
                
            }
            catch(Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Mensagem de Erro");
                alert.setContentText("Não foi possível inserir Evento: " + e.getMessage());
                alert.showAndWait();
            }   
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível inserir Evento, parâmetro vazio.");
            alert.showAndWait();
        } 
    }
    
    public void updateSessaoEvento(Sessao sessao){
        if(sessao != null){
            DaoAtividade daoA = new DaoAtividade();
            
            ObservableList<Evento> listEvento = getListaEventoBySessaoId(sessao.getId());
            
            for(int i = listEvento.size() -1; i >= 0; i--){
                Evento evento = listEvento.get(i);
                Atividade atividade = daoA.getAtividadeById(evento.getAtividadeId());

                LocalDate newDate = sessao.getDataAbertura().toLocalDate().plusDays(atividade.getIntervalo());
                Date sqlData = Date.valueOf(newDate);
                
                evento.setData(sqlData);
                updateDateEvento(evento);
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível inserir Evento, parâmetro vazio.");
            alert.showAndWait();
        } 
    }
    
    public void updateDateEvento(Evento evento){
        if(evento != null){
            Connection conn = null;
            PreparedStatement pstm = null;

            try{
                conn = Conexao.getConexao();
                
                pstm = conn.prepareStatement(UPDATE_DATE_EVENTO);
                pstm.setDate(1, evento.getData());
                pstm.setInt(3, evento.getId());
                
                if(Verify.hasEventOnDate(evento.getData()) || Verify.isSunday(evento.getData())){
                    pstm.setInt(2, 0);
                }
                else{
                    pstm.setInt(2, 1);
                }
                
                SnackbarModel.showSnackbar(SnackbarModel.EVENTO_UPD);
                
                pstm.execute();
                Conexao.fechaConexao(conn, pstm);
            }
            catch(Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Mensagem de Erro");
                alert.setContentText("Não foi possível inserir Evento: " + e.getMessage());
                alert.showAndWait();
            }   
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível inserir Evento, parâmetro vazio.");
            alert.showAndWait();
        } 
    }
    
    public void updateConfirmadoEvento(Evento evento){
        if(evento != null){
            Connection conn = null;
            PreparedStatement pstm = null;

            try{
                conn = Conexao.getConexao();
                
                pstm = conn.prepareStatement(UPDATE_CONFIRMADO_EVENTO);
                pstm.setInt(1, evento.getConfirmado());
                pstm.setInt(2, evento.getId());
                
                SnackbarModel.showSnackbar(SnackbarModel.EVENTO_UPD);
                
                pstm.execute();
                Conexao.fechaConexao(conn, pstm);
            }
            catch(Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Mensagem de Erro");
                alert.setContentText("Não foi possível inserir Evento: " + e.getMessage());
                alert.showAndWait();
            }   
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível inserir Evento, parâmetro vazio.");
            alert.showAndWait();
        } 
    }
    
    //Remove todos os Eventos-Atividades do Protocolo pelo id do Protocolo
    public void removeEventoAtividadeByProtocoloId(int id){
        Connection conn = null;
        PreparedStatement pstm = null;
        DaoAtividade daoAtividade = new DaoAtividade();
        
        List<Atividade> listaAtividade = daoAtividade.getListaAtividadeByProtocoloId(id);
        
        try {
            conn = Conexao.getConexao(); 
            
            for(int i = 0; i < listaAtividade.size(); i++){
                removeEventoAtividadeById(listaAtividade.get(i).getId());
            }
            
            
            Conexao.fechaConexao(conn, pstm);

        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível excluir o Evento: " +e.getMessage());
        }
    }
    
    //Remove o Evento-Atividade pelo id da Atividade
    public void removeEventoAtividadeById(int id){
        Connection conn = null;
        PreparedStatement pstm = null; 
        
        try {
            conn = Conexao.getConexao(); 

            pstm = conn.prepareStatement(DELETE_EVENTO_BY_ATIVIDADE_ID);
            pstm.setInt(1, id);
            pstm.execute();

            Conexao.fechaConexao(conn, pstm);

        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível excluir o Evento: " +e.getMessage());
        }
    }
    
    public void removeEventoById(int id){
        Connection conn = null;
        PreparedStatement pstm = null; 
        
        try {
            conn = Conexao.getConexao(); 

            pstm = conn.prepareStatement(DELETE_EVENTO_BY_ID);
            pstm.setInt(1, id);
            pstm.execute();

            Conexao.fechaConexao(conn, pstm);

        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível excluir o Evento: " +e.getMessage());
        }
    }
    
    public void removeEventosBySessaoId(String id){
        Connection conn = null;
        PreparedStatement pstm = null; 
        
        try {
            conn = Conexao.getConexao(); 

            pstm = conn.prepareStatement(DELETE_EVENTOS_BY_SESSAO_ID);
            pstm.setString(1, id);
            pstm.execute();

            Conexao.fechaConexao(conn, pstm);

        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível excluir o Sessão: " +e.getMessage());
        }
    }
    
    public void removeEventoCompromisso(int id, Date date){
        Connection conn = null;
        PreparedStatement pstm = null; 
        
        try {
            conn = Conexao.getConexao(); 

            pstm = conn.prepareStatement(DELETE_COMPROMISSO_EVENTO);
            pstm.setInt(1, id);
            pstm.setDate(2, date);
            pstm.execute();

            SnackbarModel.showSnackbar(SnackbarModel.EVENTO_DEL);
            
            Conexao.fechaConexao(conn, pstm);

        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível excluir o Evento-Compromisso: " +e.getMessage());
        }
    }
    
    public void removeEventoCompromisso(Compromisso compromisso){
        Connection conn = null;
        PreparedStatement pstm = null; 
        
        try {
            conn = Conexao.getConexao(); 

            pstm = conn.prepareStatement(DELETE_EVENTO_COMPROMISSO_BY_ID);
            pstm.setInt(1, compromisso.getId());
            pstm.execute();
            
            SnackbarModel.showSnackbar(SnackbarModel.EVENTO_DEL);
            
            Conexao.fechaConexao(conn, pstm);

        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível excluir o Evento-Compromisso: " +e.getMessage());
        }
    }
    
    public ObservableList<Evento> getListEventoForCalendario() {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ObservableList<Evento> listaEvento = FXCollections.observableArrayList();

        LocalDate dataAtual = Modelo.getInstance().dataAtual;
        LocalDate dataMesAnterior = LocalDate.now();
        LocalDate dataMesPosterior = LocalDate.now();
        
        if(dataAtual.getMonthValue() - 1 == 0){
            dataMesAnterior = LocalDate.of(dataAtual.getYear() - 1, 12, 1);
            dataMesPosterior = LocalDate.of(dataAtual.getYear(), dataAtual.getMonthValue() + 1, 20);
        }
        else if(dataAtual.getMonthValue() + 1 == 13){
            dataMesAnterior = LocalDate.of(dataAtual.getYear(), dataAtual.getMonthValue() - 1, 1);
            dataMesPosterior = LocalDate.of(dataAtual.getYear() + 1, 1, 20);
        }
        else{
            dataMesAnterior = LocalDate.of(dataAtual.getYear(), dataAtual.getMonthValue() - 1, 1);
            dataMesPosterior = LocalDate.of(dataAtual.getYear(), dataAtual.getMonthValue() + 1, 20);
        }
        
        
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_EVENTO_FOR_CALENDARIO);
            
            pstm.setDate(1, Date.valueOf(dataMesAnterior));
            pstm.setDate(2, Date.valueOf(dataMesPosterior));
            
            rs = pstm.executeQuery();

            while(rs.next()) {
                Evento evento = new Evento();

                evento.setId(rs.getInt("EVT_ID"));
                evento.setData(rs.getDate("EVT_DATA"));
                evento.setConfirmado(rs.getInt("EVT_CONFIRMADO"));
                evento.setBloqueado(rs.getInt("EVT_BLOQUEADO"));
                evento.setAtividadeId(rs.getInt("ATIV_ID"));
                evento.setCompromissoId(rs.getInt("COMP_ID"));
                evento.setSessaoId(rs.getString("SES_ID"));
                evento.setProtocoloId(rs.getInt("PROTO_ID"));
                
                listaEvento.add(evento);
            }
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar todos Eventos: " + e.getMessage());
            alert.showAndWait();
        }

        return listaEvento;
    }
    
    public ObservableList<Evento> getListEventoForCalendarioAno() {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ObservableList<Evento> listaEvento = FXCollections.observableArrayList();

        LocalDate dataAtual = Modelo.getInstance().dataAtualAno;
        LocalDate dataPrimeiroDia = LocalDate.now();
        LocalDate dataUltimoDia = LocalDate.now();
        
        dataPrimeiroDia = LocalDate.of(dataAtual.getYear(), 1, 1);
        dataUltimoDia = LocalDate.of(dataAtual.getYear(), 12, 31);
        
        
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_EVENTO_FOR_CALENDARIO);
            
            pstm.setDate(1, Date.valueOf(dataPrimeiroDia));
            pstm.setDate(2, Date.valueOf(dataUltimoDia));
            
            rs = pstm.executeQuery();

            while(rs.next()) {
                Evento evento = new Evento();

                evento.setId(rs.getInt("EVT_ID"));
                evento.setData(rs.getDate("EVT_DATA"));
                evento.setConfirmado(rs.getInt("EVT_CONFIRMADO"));
                evento.setBloqueado(rs.getInt("EVT_BLOQUEADO"));
                evento.setAtividadeId(rs.getInt("ATIV_ID"));
                evento.setCompromissoId(rs.getInt("COMP_ID"));
                evento.setSessaoId(rs.getString("SES_ID"));
                evento.setProtocoloId(rs.getInt("PROTO_ID"));
                
                listaEvento.add(evento);
            }
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar todos Eventos: " + e.getMessage());
            alert.showAndWait();
        }

        return listaEvento;
    }
    
    public ObservableList<Evento> getListaEventoByProtocoloId(int protocoloId) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ObservableList<Evento> listaEvento = FXCollections.observableArrayList();

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_EVENTO_BY_PROTOCOLO_ID);
            pstm.setInt(1, protocoloId);
            rs = pstm.executeQuery();

            while(rs.next()) {
                Evento evento = new Evento();

                evento.setId(rs.getInt("EVT_ID"));
                evento.setData(rs.getDate("EVT_DATA"));
                evento.setConfirmado(rs.getInt("EVT_CONFIRMADO"));
                evento.setBloqueado(rs.getInt("EVT_BLOQUEADO"));
                evento.setAtividadeId(rs.getInt("ATIV_ID"));
                evento.setCompromissoId(rs.getInt("COMP_ID"));
                evento.setSessaoId(rs.getString("SES_ID"));
                evento.setProtocoloId(rs.getInt("PROTO_ID"));
                
                listaEvento.add(evento);
            }
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar todos Eventos: " + e.getMessage());
            alert.showAndWait();
        }

        return listaEvento;
    }
    
    public ObservableList<Evento> getListaEventoByAtividadeId(int atividadeId) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ObservableList<Evento> listaEvento = FXCollections.observableArrayList();

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_EVENTO_BY_ATIVIDADE_ID);
            pstm.setInt(1, atividadeId);
            rs = pstm.executeQuery();

            while(rs.next()) {
                Evento evento = new Evento();

                evento.setId(rs.getInt("EVT_ID"));
                evento.setData(rs.getDate("EVT_DATA"));
                evento.setConfirmado(rs.getInt("EVT_CONFIRMADO"));
                evento.setBloqueado(rs.getInt("EVT_BLOQUEADO"));
                evento.setAtividadeId(rs.getInt("ATIV_ID"));
                evento.setCompromissoId(rs.getInt("COMP_ID"));
                evento.setSessaoId(rs.getString("SES_ID"));
                evento.setProtocoloId(rs.getInt("PROTO_ID"));
                
                listaEvento.add(evento);
            }
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar todos Eventos: " + e.getMessage());
            alert.showAndWait();
        }

        return listaEvento;
    }
    
    public ObservableList<Evento> getListaEventoBySessaoId(String sessaoId) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ObservableList<Evento> listaEvento = FXCollections.observableArrayList();

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_EVENTO_BY_SESSAO_ID);
            pstm.setString(1, sessaoId);
            rs = pstm.executeQuery();

            while(rs.next()) {
                Evento evento = new Evento();

                evento.setId(rs.getInt("EVT_ID"));
                evento.setData(rs.getDate("EVT_DATA"));
                evento.setConfirmado(rs.getInt("EVT_CONFIRMADO"));
                evento.setBloqueado(rs.getInt("EVT_BLOQUEADO"));
                evento.setAtividadeId(rs.getInt("ATIV_ID"));
                evento.setCompromissoId(rs.getInt("COMP_ID"));
                evento.setSessaoId(rs.getString("SES_ID"));
                evento.setProtocoloId(rs.getInt("PROTO_ID"));
                
                listaEvento.add(evento);
            }
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar todos Eventos: " + e.getMessage());
            alert.showAndWait();
        }

        return listaEvento;
    }
    
    public Evento getEventoById(int eventoId) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Evento evento = new Evento();
        
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_EVENTO_BY_ID);
            pstm.setInt(1, eventoId);
            rs = pstm.executeQuery();

            while(rs.next()) {
                evento.setId(rs.getInt("EVT_ID"));
                evento.setData(rs.getDate("EVT_DATA"));
                evento.setConfirmado(rs.getInt("EVT_CONFIRMADO"));
                evento.setBloqueado(rs.getInt("EVT_BLOQUEADO"));
                evento.setAtividadeId(rs.getInt("ATIV_ID"));
                evento.setCompromissoId(rs.getInt("COMP_ID"));
                evento.setSessaoId(rs.getString("SES_ID"));
                evento.setProtocoloId(rs.getInt("PROTO_ID"));
            }
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar Evento: " + e.getMessage());
            alert.showAndWait();
        }

        return evento;
    }
    
     public int getCountEventoByDate(Date date) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(COUNT_EVENTO_BY_DATE);
            pstm.setDate(1, date);
            rs = pstm.executeQuery();

            while(rs.next()) {
                count = rs.getInt("count(EVT_ID)");
            }
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar Evento: " + e.getMessage());
            alert.showAndWait();
        }

        return count;
    }
    
    public ObservableList<Evento> getListEventoNotConfirmed(Date data) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ObservableList<Evento> listEvento = FXCollections.observableArrayList();
        
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_EVENTO_BY_CONFIRMADO);
            pstm.setDate(1, data);
            rs = pstm.executeQuery();

            while(rs.next()) {
                Evento evento = new Evento();
                evento.setId(rs.getInt("EVT_ID"));
                evento.setData(rs.getDate("EVT_DATA"));
                evento.setConfirmado(rs.getInt("EVT_CONFIRMADO"));
                evento.setBloqueado(rs.getInt("EVT_BLOQUEADO"));
                evento.setAtividadeId(rs.getInt("ATIV_ID"));
                evento.setCompromissoId(rs.getInt("COMP_ID"));
                evento.setSessaoId(rs.getString("SES_ID"));
                evento.setProtocoloId(rs.getInt("PROTO_ID"));
                
                listEvento.add(evento);
            }
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar Evento: " + e.getMessage());
            alert.showAndWait();
        }

        return listEvento;
    } 
     
    public List<Evento> listByDataAndAtividadeEvento(Protocolo protocolo, Date date){
        
            Connection conn = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            
            List<Evento> listEvento = new ArrayList();

            try {
                conn = Conexao.getConexao();
                
                DaoAtividade daoAtividade = new DaoAtividade();
                List<Atividade> listAtividade = daoAtividade.getListaAtividadeByProtocoloId(protocolo.getId());

                for(int i = 0; i <listAtividade.size(); i++){
                    Atividade atv = listAtividade.get(i);
                    LocalDate localData = date.toLocalDate().plusDays(atv.getIntervalo());
                    Date sqlData = Date.valueOf(localData);

                    pstm = conn.prepareStatement(LISTBYDATA_AND_ATIVIDADE_EVENTO);
                    pstm.setDate(1, sqlData);
                    pstm.setInt(2, atv.getId());
                    rs = pstm.executeQuery();
                    
                    while(rs.next()) {
                        Evento evento = new Evento();
                        
                        evento.setId(rs.getInt("EVT_ID"));
                        evento.setData(rs.getDate("EVT_DATA"));
                        evento.setConfirmado(rs.getInt("EVT_CONFIRMADO"));
                        evento.setBloqueado(rs.getInt("EVT_BLOQUEADO"));
                        evento.setAtividadeId(rs.getInt("ATIV_ID"));
                        evento.setCompromissoId(rs.getInt("COMP_ID"));
                        evento.setSessaoId(rs.getString("SES_ID"));
                        evento.setProtocoloId(rs.getInt("PROTO_ID"));
                        
                        listEvento.add(evento);
                    }
          
                    pstm = null;
                    rs = null;
                }
                
                Conexao.fechaConexao(conn, pstm, rs);
                
            } catch(Exception e) {
                Modelo.getInstance().showAlertErro("Não foi possível listar os eventos para sessão: \n" + e.getMessage());
            }   
        
        return listEvento;
    }
    
    public ObservableList<Evento> listByData(Date date){
        
            Connection conn = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            
            ObservableList<Evento> listEvento = FXCollections.observableArrayList();

            try {
                conn = Conexao.getConexao();

                pstm = conn.prepareStatement(LIST_EVENTO_BY_DATE);
                pstm.setDate(1, date);
                rs = pstm.executeQuery();

                while(rs.next()) {
                    Evento evento = new Evento();

                    evento.setId(rs.getInt("EVT_ID"));
                    evento.setData(rs.getDate("EVT_DATA"));
                    evento.setConfirmado(rs.getInt("EVT_CONFIRMADO"));
                    evento.setBloqueado(rs.getInt("EVT_BLOQUEADO"));
                    evento.setAtividadeId(rs.getInt("ATIV_ID"));
                    evento.setCompromissoId(rs.getInt("COMP_ID"));
                    evento.setSessaoId(rs.getString("SES_ID"));
                    evento.setProtocoloId(rs.getInt("PROTO_ID"));

                    listEvento.add(evento);
                }

                pstm = null;
                rs = null;
                
                
                Conexao.fechaConexao(conn, pstm, rs);
                
            } catch(Exception e) {
                Modelo.getInstance().showAlertErro("Não foi possível listar os eventos para esta data: \n" + e.getMessage());
            }   
        
        return listEvento;
    }
}
