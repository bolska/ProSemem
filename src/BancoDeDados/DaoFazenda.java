/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDeDados;

import Classes.Fazenda;
import Model.Modelo;
import Model.SnackbarModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author user
 */
public class DaoFazenda {
    private final String INSERT_FAZENDA = "INSERT INTO FAZENDA (FAZEN_ID, FAZEN_NOME, FAZEND_SIGLA, FAZEN_DESCRICAO, ENCAR_ID) VALUES (0,?,?,?,?)";
    private final String UPDATE_FAZENDA = "UPDATE FAZENDA SET FAZEN_NOME=?, FAZEN_SIGLA=?, FAZEN_DESCRICAO=? WHERE FAZEN_ID=?";
    private final String DELETE_FAZENDA = "DELETE FROM FAZENDA WHERE FAZEN_ID=?";
    private final String LIST_FAZENDA = "SELECT * FROM FAZENDA";
    private final String LIST_FAZENDA_BY_ID = "SELECT * FROM FAZENDA WHERE FAZEN_ID=?";
    
    public void insertFazenda(Fazenda fazenda) {
        if(fazenda != null) {
            Connection conn = null;
            try {
                conn = Conexao.getConexao();
                
                PreparedStatement pstm = conn.prepareStatement(INSERT_FAZENDA);
                pstm.setString(1, fazenda.getNome());
                pstm.setString(2, fazenda.getSigla());
                pstm.setString(3, fazenda.getDescricao());
                pstm.setInt(4, fazenda.getEncarregadoId());
                
                
                pstm.execute();
                Conexao.fechaConexao(conn, pstm);
                
                SnackbarModel.showSnackbar(SnackbarModel.FAZENDA_ADD);
            } 
            catch (Exception e) {
                Modelo.getInstance().showAlertErro("Não foi possível inserir Fazenda: \n" + e.getMessage());
            }
        } 
        else {
            Modelo.getInstance().showAlertErro("Não foi possível inserir Fazenda, parâmetro vazio");
        }
    }
    
    public void updateFazenda(Fazenda fazenda) {
        if(fazenda != null) {
            Connection conn = null;

            try {
                conn = Conexao.getConexao();
                PreparedStatement pstm;
                pstm = conn.prepareStatement(UPDATE_FAZENDA);

                pstm.setString(1, fazenda.getNome());
                pstm.setString(2, fazenda.getSigla());
                pstm.setString(3, fazenda.getDescricao());
                pstm.setInt(4, fazenda.getId());
                
                pstm.execute();
                Conexao.fechaConexao(conn);
                
                SnackbarModel.showSnackbar(SnackbarModel.FAZENDA_UPD);

            } catch(Exception e) {
                Modelo.getInstance().showAlertErro("Não foi possível atualizar Fazenda: \n" + e.getMessage());
            }

        } 
        else {
            Modelo.getInstance().showAlertErro("Não foi possível atualizar Fazenda, parâmetro vazio");
        }
    }
    
    public void deleteFazenda(int id) {
        Connection conn = null;
        
        try {
            conn = Conexao.getConexao();
            PreparedStatement pstm;            
            pstm = conn.prepareStatement(DELETE_FAZENDA);

            pstm.setInt(1, id);

            pstm.execute();
            Conexao.fechaConexao(conn, pstm);
            
            SnackbarModel.showSnackbar(SnackbarModel.FAZENDA_DEL);

        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível excluir Fazenda: \n" +e.getMessage());
        }
    }
    
    public ObservableList<Fazenda> getListFazenda() {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ObservableList<Fazenda> listFazenda = FXCollections.observableArrayList();

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_FAZENDA);
            rs = pstm.executeQuery();

            while(rs.next()) {
                Fazenda fazenda = new Fazenda();

                fazenda.setId(rs.getInt("FAZEN_ID"));
                fazenda.setNome(rs.getString("FAZEN_NOME"));
                fazenda.setSigla(rs.getString("FAZEN_SIGLA"));
                fazenda.setDescricao(rs.getString("FAZEN_DESCRICAO"));
                fazenda.setEncarregadoId(rs.getInt("ENCAR_ID"));
                listFazenda.add(fazenda);
            }
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível listar Fazenda: \n" + e.getMessage());
        }

        return listFazenda;
    }
    
    public Fazenda getFazendaById(int fazendaId) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Fazenda fazenda = new Fazenda();
        
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_FAZENDA_BY_ID);
            pstm.setInt(1, fazendaId);
            rs = pstm.executeQuery();

            while(rs.next()) {
                fazenda.setId(rs.getInt("FAZEN_ID"));
                fazenda.setNome(rs.getString("FAZEN_NOME"));
                fazenda.setSigla(rs.getString("FAZEN_SIGLA"));
                fazenda.setDescricao(rs.getString("FAZEN_DESCRICAO"));
                fazenda.setEncarregadoId(rs.getInt("ENCAR_ID"));
            }
            Conexao.fechaConexao(conn, pstm, rs);
        } 
        catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível listar Fazenda: \n" + e.getMessage());
        }
        return fazenda;
    }
}
