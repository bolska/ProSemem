package BancoDeDados;

import Classes.Atividade;
import Model.Modelo;
import Model.SnackbarModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;


public class DaoAtividade {
    //Atividade
    private final String INSERT_ATIVIDADE = "INSERT INTO ATIVIDADE (ATIV_ID, ATIV_DESCRICAO, ATIV_INTERVALO, ATIV_TIPO, ATIV_OBS, PROTO_ID) VALUES (0,?,?,?,?,?)";
    private final String UPDATE_DESCR_PRINCIPAL_ATIVIDADE = "UPDATE ATIVIDADE SET ATIV_DESCRICAO=? WHERE ATIV_ID=?";
    private final String UPDATE_ATIVIDADE = "UPDATE ATIVIDADE SET ATIV_DESCRICAO=?, ATIV_TIPO=?, ATIV_INTERVALO=?, ATIV_OBS=? WHERE ATIV_ID=?";
    private final String DELETE_ATIVIDADE = "DELETE FROM ATIVIDADE WHERE ATIV_ID=?";
    private final String DELETE_BY_PROTOCOLOID_ATIVIDADE = "DELETE FROM ATIVIDADE WHERE PROTO_ID=?";
    private final String LIST_ATIVIDADE = "SELECT * FROM ATIVIDADE WHERE PROTO_ID=?";
    private final String LISTBYID_ATIVIDADE = "SELECT * FROM ATIVIDADE WHERE ATIV_ID=?";
    private final String LISTBYDESCRICAO_ATIVIDADE = "SELECT * FROM ATIVIDADE WHERE ATIV_DESCRICAO=?";
    private final String ATIVIDADE_PRINCIPAL_PROTOCOLO = "SELECT * FROM ATIVIDADE WHERE PROTO_ID=? and ATIV_TIPO='Principal'";
    
    //Inicio metodos de Atividade
    public void inserirAtividade(Atividade atividade) {
        if(atividade != null) {
            Connection conn = null;
            
            try {
                conn = Conexao.getConexao();
                PreparedStatement pstm;
                pstm = conn.prepareStatement(INSERT_ATIVIDADE);
                
                pstm.setString(1, atividade.getDescricao());
                pstm.setInt(2, atividade.getIntervalo());
                pstm.setString(3, atividade.getTipo());
                pstm.setString(4, atividade.getObs());
                pstm.setInt(5, atividade.getProtocoloId());
                
                SnackbarModel.showSnackbar(SnackbarModel.ATIVIDADE_ADD);
                
                pstm.execute();
                Conexao.fechaConexao(conn, pstm);
                
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Mensagem de Erro");
                alert.setContentText("Não foi possível inserir Atividade: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível inserir Atividade, parâmetro vazio");
            alert.showAndWait();
        }
    }
    
    public void atualizaAtividade(Atividade atividade) {
        if(atividade != null) {
            Connection conn = null;

            try {
                conn = Conexao.getConexao();
                PreparedStatement pstm = conn.prepareStatement(UPDATE_ATIVIDADE);

                pstm.setString(1, atividade.getDescricao());
                pstm.setString(2, atividade.getTipo());
                pstm.setInt(3, atividade.getIntervalo());
                pstm.setString(4, atividade.getObs());
                pstm.setInt(5, atividade.getId());
                
                SnackbarModel.showSnackbar(SnackbarModel.ATIVIDADE_UPD);
                
                pstm.execute();
                Conexao.fechaConexao(conn);

            } catch(Exception e) {
                Modelo.getInstance().showAlertErro("Não foi possível atualizar Atividade: " + e.getMessage());
            }

        } 
        else {
            Modelo.getInstance().showAlertErro("Não foi possível atualizar Atividade, parâmetro vazio");
        }
    }
    
    public void atualizaAtividadeDescrPrincipal(Atividade atividade) {
        if(atividade != null) {
            Connection conn = null;

            try {
                conn = Conexao.getConexao();
                PreparedStatement pstm;
                pstm = conn.prepareStatement(UPDATE_DESCR_PRINCIPAL_ATIVIDADE);

                pstm.setString(1, atividade.getDescricao());
                pstm.setInt(2, atividade.getId());
                
                SnackbarModel.showSnackbar(SnackbarModel.ATIVIDADE_UPD);
                
                pstm.execute();
                Conexao.fechaConexao(conn);

            } catch(Exception e) {
                Modelo.getInstance().showAlertErro("Não foi possível atualizar Atividade: " + e.getMessage());
            }

        } else {
            Modelo.getInstance().showAlertErro("Não foi possível atualizar Atividade, parâmetro vazio");
        }
    }
      
    public void removeAtividade(int id) {
        Connection conn = null;
        PreparedStatement pstm = null;
        
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(DELETE_ATIVIDADE);

            pstm.setInt(1, id);

            SnackbarModel.showSnackbar(SnackbarModel.ATIVIDADE_DEL);
            
            pstm.execute();
            Conexao.fechaConexao(conn, pstm);

        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível excluir a Atividade: " +e.getMessage());
        }
    }
    
    public void removeAtividadeByProtocolo(int protocoloId) {
        Connection conn = null;
        PreparedStatement pstm = null;
        
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(DELETE_BY_PROTOCOLOID_ATIVIDADE);

            pstm.setInt(1, protocoloId);

            pstm.execute();
            Conexao.fechaConexao(conn, pstm);

        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível excluir as Atividades: " +e.getMessage());
        }
    }
    
    public ObservableList<Atividade> getListaAtividadeByProtocoloId(int idProtocolo) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ObservableList<Atividade> listaAtividade = FXCollections.observableArrayList();

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_ATIVIDADE);
            pstm.setInt(1, idProtocolo);
            rs = pstm.executeQuery();

            while(rs.next()) {
                Atividade atividade = new Atividade();

                atividade.setId(rs.getInt("ATIV_ID"));
                atividade.setDescricao(rs.getString("ATIV_DESCRICAO"));
                atividade.setIntervalo(rs.getInt("ATIV_INTERVALO"));
                atividade.setTipo(rs.getString("ATIV_TIPO"));
                atividade.setObs(rs.getString("ATIV_OBS"));
                atividade.setProtocoloId(rs.getInt("PROTO_ID"));
                
                listaAtividade.add(atividade);
            }
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar Atividade: " +e.getMessage());
            alert.showAndWait();
        }

        return listaAtividade;
    }
    
    public Atividade getAtividadeById(int atividadeId) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Atividade atividade = new Atividade();

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LISTBYID_ATIVIDADE);
            pstm.setInt(1, atividadeId);
            rs = pstm.executeQuery();

            while(rs.next()) {
                atividade.setId(rs.getInt("ATIV_ID"));
                atividade.setDescricao(rs.getString("ATIV_DESCRICAO"));
                atividade.setIntervalo(rs.getInt("ATIV_INTERVALO"));
                atividade.setTipo(rs.getString("ATIV_TIPO"));
                atividade.setObs(rs.getString("ATIV_OBS"));
                atividade.setProtocoloId(rs.getInt("PROTO_ID"));
            }

            Conexao.fechaConexao(conn, pstm, rs);
            
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar Atividade: " + e.getMessage());
            alert.showAndWait();
        }

        return atividade;
    }      
    
    public Atividade getAtividadePrincipalProtocolo(int PROTO_ID) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Atividade atividade = new Atividade();
        
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(ATIVIDADE_PRINCIPAL_PROTOCOLO);
            pstm.setInt(1, PROTO_ID);
            rs = pstm.executeQuery();

            while(rs.next()) {
                atividade.setId(rs.getInt("ATIV_ID"));
                atividade.setDescricao(rs.getString("ATIV_DESCRICAO"));
                atividade.setIntervalo(rs.getInt("ATIV_INTERVALO"));
                atividade.setTipo(rs.getString("ATIV_TIPO"));
                atividade.setObs(rs.getString("ATIV_OBS"));
                atividade.setProtocoloId(PROTO_ID);
            }

            Conexao.fechaConexao(conn, pstm, rs);
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mensagem de Erro");
            alert.setContentText("Não foi possível lista Atividade: " +e.getMessage());
            alert.showAndWait();
        }

        return atividade;
    }      
    //Fim métodos de Atividade
}
