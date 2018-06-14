/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDeDados;

import Classes.Sessao;
import Model.Modelo;
import Model.SnackbarModel;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author user
 */
public class DaoSessao {//MUDAR
    private final String INSERT_SESSAO = "INSERT INTO SESSAO (SES_ID, SES_ESTADO, FAZEN_ID, PROTO_ID, SES_DATA_ABERTURA, SES_COR) VALUES (?,?,?,?,?,?)";
    private final String UPDATE_SESSAO_SITUATION = "UPDATE SESSAO SET SES_ESTADO=? WHERE SES_ID=?";
    private final String UPDATE_SESSAO_DATE = "UPDATE SESSAO SET SES_DATA_ABERTURA=? WHERE SES_ID=?";
    private final String UPDATE_SESSAO_COR = "UPDATE SESSAO SET SES_COR=? WHERE SES_ID=?";
    private final String DELETE_SESSAO_BY_PROTOCOLO_ID = "DELETE FROM SESSAO WHERE PROTO_ID=?";
    private final String DELETE_SESSAO_BY_ID = "DELETE FROM SESSAO WHERE SES_ID=?";
    private final String LIST_SESSAO = "SELECT * FROM SESSAO";
    private final String LIST_SESSAO_BY_ESTADO = "SELECT * FROM SESSAO WHERE SES_ESTADO=?";
    private final String LIST_SESSAO_BY_ID = "SELECT * FROM SESSAO WHERE SES_ID=?";
    private final String LIST_SESSAO_BY_EVENTO_ID = "select * from SESSAO s, EVENTO e where e.EVT_ID = ? and e.SES_ID = s.SES_ID";
    private final String LIST_LAST_SESSAO_ID = "SELECT MAX(SES_ID) FROM SESSAO";
    
    public void insertSessao(Sessao sessao) {
        if(sessao != null) {
            Connection conn = null;
            PreparedStatement pstm = null;
            try {
                conn = Conexao.getConexao();

                pstm = conn.prepareStatement(INSERT_SESSAO);
                pstm.setString(1, sessao.getId());
                pstm.setString(2, sessao.getEstado());
                pstm.setInt(3, sessao.getFazendaId());
                pstm.setInt(4, sessao.getProtocoloId());
                pstm.setDate(5, sessao.getDataAbertura());
                pstm.setString(6, sessao.getCor());
                pstm.execute();

                //Gerar Eventos a partir de Ativididade de atrelar a sua Sessão
                DaoEvento daoEvt = new DaoEvento();
                daoEvt.insertEvento(sessao);

                Conexao.fechaConexao(conn, pstm);

                SnackbarModel.showSnackbar(SnackbarModel.SESSAO_ADD);
                
            } catch(Exception e) {
                Modelo.getInstance().showAlertErro("Não foi possível cadastrar Sessão: \n" + e.getMessage());
            }
        } else {
            Modelo.getInstance().showAlertErro("Não foi possível cadastrar Sessão, parâmetro nulo");
        }
    }
    
    public void updateSessaoSituation(Sessao sessao) {//When a Sessao needs to be update because ends or canceled
        if(sessao != null) {
            Connection conn = null;
            
            try {
                conn = Conexao.getConexao();
                PreparedStatement pstm = conn.prepareStatement(UPDATE_SESSAO_SITUATION);
                
                pstm.setString(1, sessao.getEstado());
                pstm.setString(2, sessao.getId());
                
                pstm.execute();
                Conexao.fechaConexao(conn);
                
                SnackbarModel.showSnackbar(SnackbarModel.SESSAO_UPD);
                
            } catch(Exception e) {
                Modelo.getInstance().showAlertErro("Não foi possível atualizar estado de Sessão: \n" + e.getMessage());
            }
        } else {
            Modelo.getInstance().showAlertErro("Não foi possível atualizar estado de Sessão, parâmetro vazio");
        }
    }
    
    public void updateSessaoDate(Sessao sessao) {
        if(sessao != null) {
            Connection conn = null;
            
            try {
                conn = Conexao.getConexao();
                PreparedStatement pstm = conn.prepareStatement(UPDATE_SESSAO_DATE);
                
                pstm.setDate(1, sessao.getDataAbertura());
                pstm.setString(2, sessao.getId());
                
                pstm.execute();
                Conexao.fechaConexao(conn);
                
                SnackbarModel.showSnackbar(SnackbarModel.SESSAO_UPD);
                
            } catch(Exception e) {
                Modelo.getInstance().showAlertErro("Não foi possível atualizar a data da Sessão: \n" + e.getMessage());
            }
        } else {
            Modelo.getInstance().showAlertErro("Não foi possível atualizar a data da Sessão, parâmetro vazio");
        }
    }
    
    public void updateSessaoCor(Sessao sessao) {
        if(sessao != null) {
            Connection conn = null;
            
            try {
                conn = Conexao.getConexao();
                PreparedStatement pstm = conn.prepareStatement(UPDATE_SESSAO_COR);
                
                pstm.setString(1, sessao.getCor());
                pstm.setString(2, sessao.getId());
                
                pstm.execute();
                Conexao.fechaConexao(conn);
                
                SnackbarModel.showSnackbar(SnackbarModel.SESSAO_UPD);
                
            } catch(Exception e) {
                Modelo.getInstance().showAlertErro("Não foi possível atualizar a cor da Sessão: \n" + e.getMessage());
            }
        } else {
            Modelo.getInstance().showAlertErro("Não foi possível atualizar a cor da Sessão, parâmetro vazio");
        }
    }
    
    public void removeSessaoByProtocolo(int id) {
        Connection conn = null;
        
        try {
            conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(DELETE_SESSAO_BY_PROTOCOLO_ID);
            
            pstm.setInt(1, id);
            
            pstm.execute();
            Conexao.fechaConexao(conn, pstm);
            
            SnackbarModel.showSnackbar(SnackbarModel.SESSAO_DEL);
            
        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível excluir Sessao: \n" + e.getMessage());
        }
    }
    
    public void removeSessaoById(String id) {
        Connection conn = null;
        DaoEvento daoE = new DaoEvento();
        
        daoE.removeEventosBySessaoId(id);
        
        try {
            conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(DELETE_SESSAO_BY_ID);
            
            pstm.setString(1, id);
            
            pstm.execute();
            Conexao.fechaConexao(conn, pstm);
            
            SnackbarModel.showSnackbar(SnackbarModel.SESSAO_DEL);
            
        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível excluir Sessao: \n" + e.getMessage());
        }
    }
    
    public ObservableList<Sessao> getListSessao() {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ObservableList<Sessao> listSessao = FXCollections.observableArrayList();
        
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_SESSAO);
            rs = pstm.executeQuery();
            
            while(rs.next()) {
                Sessao sessao = new Sessao();
                
                sessao.setId(rs.getString("SES_ID"));
                sessao.setEstado(rs.getString("SES_ESTADO"));
                sessao.setProtocoloId(rs.getInt("PROTO_ID"));
                sessao.setFazendaId(rs.getInt("FAZEN_ID"));
                sessao.setDataAbertura(rs.getDate("SES_DATA_ABERTURA"));
                sessao.setDataEncerramento(rs.getDate("SES_DATA_ENCERRAMENTO"));
                sessao.setCor(rs.getString("SES_COR"));
                
                listSessao.add(sessao);   
            }
            
            Conexao.fechaConexao(conn, pstm, rs);
        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível listar Sessões: \n" + e.getMessage());
        }
        
        return listSessao;
    }
    
    public ObservableList<Sessao> getListSessaoByEstado(String estado) { //A = Aberto, E = encerrado
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ObservableList<Sessao> listSessao = FXCollections.observableArrayList();
        
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_SESSAO_BY_ESTADO);
            pstm.setString(1, estado);
            rs = pstm.executeQuery();
            String id = null;
            
            while(rs.next()) {
                Sessao sessao = new Sessao();
                
                sessao.setId(rs.getString("SES_ID"));
                sessao.setEstado(rs.getString("SES_ESTADO"));
                sessao.setProtocoloId(rs.getInt("PROTO_ID"));
                sessao.setFazendaId(rs.getInt("FAZEN_ID"));
                sessao.setDataAbertura(rs.getDate("SES_DATA_ABERTURA"));
                sessao.setDataEncerramento(rs.getDate("SES_DATA_ENCERRAMENTO"));
                sessao.setCor(rs.getString("SES_COR"));
                
                listSessao.add(sessao);   
            }
            
            Conexao.fechaConexao(conn, pstm, rs);
        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível listar Sessões: \n" + e.getMessage());
        }
        
        return listSessao;
    }
    
    public Sessao getSessaoById(String sessaoId) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Sessao sessao = new Sessao();
        
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_SESSAO_BY_ID);
            pstm.setString(1, sessaoId);
            rs = pstm.executeQuery();
            
            while(rs.next()) {
                sessao.setId(rs.getString("SES_ID"));
                sessao.setEstado(rs.getString("SES_ESTADO"));
                sessao.setProtocoloId(rs.getInt("PROTO_ID"));
                sessao.setFazendaId(rs.getInt("FAZEN_ID"));
                sessao.setDataAbertura(rs.getDate("SES_DATA_ABERTURA"));
                sessao.setDataEncerramento(rs.getDate("SES_DATA_ENCERRAMENTO"));
                sessao.setCor(rs.getString("SES_COR"));
            }
            
            Conexao.fechaConexao(conn, pstm, rs);
        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível listar Sessões: \n" + e.getMessage());
        }
        
        return sessao;
    }
    
    public Sessao getSessaoByEventoId(int eventoId) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_SESSAO_BY_EVENTO_ID);
            pstm.setInt(1, eventoId);
            rs = pstm.executeQuery();
            Sessao sessao = new Sessao();
            
            while(rs.next()) {
                sessao.setId(rs.getString("SES_ID"));
                sessao.setEstado(rs.getString("SES_ESTADO"));
                sessao.setProtocoloId(rs.getInt("PROTO_ID"));
                sessao.setFazendaId(rs.getInt("FAZEN_ID"));
                sessao.setDataAbertura(rs.getDate("SES_DATA_ABERTURA"));
                sessao.setDataEncerramento(rs.getDate("SES_DATA_ENCERRAMENTO"));
                sessao.setCor(rs.getString("SES_COR"));
            }
            
            Conexao.fechaConexao(conn, pstm, rs);
            
            return sessao;
            
        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível listar Sessões no calendário: \n" + e.getMessage());
        }
        
        return null;
    }
    
    public String getLastSessaoId(){
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String lastSessaoId = "";
        
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_LAST_SESSAO_ID);
            rs = pstm.executeQuery();

            while(rs.next()) {
                lastSessaoId = rs.getString("MAX(SES_ID)");
            }
            
            Conexao.fechaConexao(conn, pstm, rs);
        } 
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return lastSessaoId;
    }
}
