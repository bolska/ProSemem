package BancoDeDados;

import Classes.Protocolo;
import Model.Modelo;
import Model.SnackbarModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class DaoProtocolo {
    
    private final String INSERT_PROTOCOLO = "INSERT INTO PROTOCOLO (PROTO_ID, PROTO_DESCRICAO) VALUES (0,?)";
    private final String UPDATE_PROTOCOLO = "UPDATE PROTOCOLO SET PROTO_DESCRICAO=?, PROTO_OBS =? WHERE PROTO_ID=?";
    private final String DELETE_PROTOCOLO = "DELETE FROM PROTOCOLO WHERE PROTO_ID=?";
    private final String DELETE_atividadePROTOCOLO = "DELETE FROM ATIVIDADE WHERE PROTO_ID=?";
    private final String LIST_PROTOCOLO = "SELECT * FROM PROTOCOLO";
    private final String LISTBYID_PROTOCOLO = "SELECT * FROM PROTOCOLO WHERE PROTO_ID=?";
    private final String LISTBYDESCR_PROTOCOLO = "SELECT * FROM PROTOCOLO WHERE PROTO_DESCRICAO=?";
    
    public void inserirProtocolo(Protocolo protocolo) {
        if(protocolo != null) {
            Connection conn = null;
            try {
                conn = Conexao.getConexao();
                
                PreparedStatement pstmP = conn.prepareStatement(INSERT_PROTOCOLO);
                pstmP.setString(1, protocolo.getDescricao());
                
                SnackbarModel.showSnackbar(SnackbarModel.PROTOCOLO_ADD);
                
                pstmP.execute();
                Conexao.fechaConexao(conn, pstmP);
                
            } 
            catch (Exception e) {
                Modelo.getInstance().showAlertErro("Não foi possível inserir Protocolo: " + e.getMessage());
            }
        } 
        else {
            Modelo.getInstance().showAlertErro("Não foi possível inserir Protocolo, parâmetro vazio");
        }
    }
    
    public void atualizaProtocolo(Protocolo protocolo) {
        if(protocolo != null) {
            Connection conn = null;

            try {
                conn = Conexao.getConexao();
                PreparedStatement pstm;
                pstm = conn.prepareStatement(UPDATE_PROTOCOLO);

                pstm.setString(1, protocolo.getDescricao());
                pstm.setString(2, protocolo.getObs());
                pstm.setInt(3, protocolo.getId());
                
                SnackbarModel.showSnackbar(SnackbarModel.PROTOCOLO_UPD);
                
                pstm.execute();
                Conexao.fechaConexao(conn);

            } catch(Exception e) {
                Modelo.getInstance().showAlertErro("Não foi possível atualizar Protocolo: " + e.getMessage());
            }

        } 
        else {
            Modelo.getInstance().showAlertErro("Não foi possível atualizar Protocolo, parâmetro vazio");
        }
    }
       
    public void removeProtocolo(int id) {
        Connection conn = null;
        
        DaoEvento daoEvento = new DaoEvento();
        DaoAtividade daoAtividade = new DaoAtividade();
        
        daoEvento.removeEventoAtividadeByProtocoloId(id);
        daoAtividade.removeAtividadeByProtocolo(id);
        
        try {
            conn = Conexao.getConexao();
            PreparedStatement pstm;            
            pstm = conn.prepareStatement(DELETE_PROTOCOLO);

            pstm.setInt(1, id);
            
            SnackbarModel.showSnackbar(SnackbarModel.PROTOCOLO_DEL);
            
            pstm.execute();
            Conexao.fechaConexao(conn, pstm);

        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível excluir o Protocolo: " +e.getMessage());
        }
    }

    public ObservableList<Protocolo> getListProtocolo() {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ObservableList<Protocolo> listaProtocolo = FXCollections.observableArrayList();

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_PROTOCOLO);
            rs = pstm.executeQuery();

            while(rs.next()) {
                Protocolo protocolo = new Protocolo();

                protocolo.setId(rs.getInt("PROTO_ID"));
                protocolo.setDescricao(rs.getString("PROTO_DESCRICAO"));
                protocolo.setObs(rs.getString("PROTO_OBS"));
                
                listaProtocolo.add(protocolo);
            }
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar Protocolo: " + e.getMessage());
            alert.showAndWait();
        }
        return listaProtocolo;
    }

    public Protocolo getProtocoloById(int id) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Protocolo protocolo = new Protocolo();

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LISTBYID_PROTOCOLO);
            pstm.setInt(1, id);
            rs = pstm.executeQuery();

            while(rs.next()) {
                protocolo.setId(rs.getInt("PROTO_ID"));
                protocolo.setDescricao(rs.getString("PROTO_DESCRICAO"));
                protocolo.setObs(rs.getString("PROTO_OBS"));
            }

            Conexao.fechaConexao(conn, pstm, rs);
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar Protocolo: " + e.getMessage());
            alert.showAndWait();
        }

        return protocolo;
    }
    
    public Protocolo getProtocoloByDescri(String descricao) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Protocolo protocolo = new Protocolo();

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LISTBYDESCR_PROTOCOLO);
            pstm.setString(1, descricao);
            rs = pstm.executeQuery();

            while(rs.next()) {
                protocolo.setId(rs.getInt("PROTO_ID"));
                protocolo.setDescricao(rs.getString("PROTO_DESCRICAO"));
                protocolo.setObs(rs.getString("PROTO_OBS"));
            }

            Conexao.fechaConexao(conn, pstm, rs);
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar Protocolo: " + e.getMessage());
            alert.showAndWait();
        }

        return protocolo;
    }
    //Fim metodos de Protocolo
}
