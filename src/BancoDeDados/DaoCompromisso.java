/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDeDados;

import Classes.Compromisso;
import Model.Modelo;
import Model.SnackbarModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;


public class DaoCompromisso {
   
    private Pane rootPane;
    
    private final String INSERT_COMPROMISSO = "INSERT INTO COMPROMISSO (COMP_ID, COMP_DESCRICAO, COMP_TIPO, COMP_COR) VALUES (0,?,?,?)";
    private final String UPDATE_COMPROMISSO = "UPDATE COMPROMISSO SET COMP_DESCRICAO=?, COMP_COR =? WHERE COMP_ID=?";
    private final String UPDATE_COR_COMPROMISSO = "UPDATE COMPROMISSO SET COMP_COR=? WHERE COMP_ID=?";
    private final String UPDATE_DESCR_COMPROMISSO = "UPDATE COMPROMISSO SET COMP_DESCRICAO=? WHERE COMP_ID=?";
    private final String DELETE_COMPROMISSO = "DELETE FROM COMPROMISSO WHERE COMP_ID=?";
    private final String LIST_COMPROMISSO = "SELECT * FROM COMPROMISSO";
    private final String LIST_COMPROMISSO_VETERINARIO = "SELECT * FROM COMPROMISSO WHERE COMP_TIPO = 'V'";
    private final String LIST_COMPROMISSO_AVULSO = "SELECT * FROM COMPROMISSO WHERE COMP_TIPO = 'A'";
    private final String LIST_COMPROMISSO_BY_TIPO_DESCRICAO = "SELECT * FROM COMPROMISSO WHERE COMP_DESCRICAO=? AND COMP_TIPO = ?";
    private final String LIST_BY_ID_COMPROMISSO = "SELECT * FROM COMPROMISSO WHERE COMP_ID=?";

    
    public void insertCompromisso(Compromisso compromisso) {
        if(compromisso != null) {
            Connection conn = null;
            
            try {
                conn = Conexao.getConexao();
                PreparedStatement pstm;
                pstm = conn.prepareStatement(INSERT_COMPROMISSO);
                
                pstm.setString(1, compromisso.getDescricao());
                pstm.setString(2, compromisso.getTipo());
                pstm.setString(3, compromisso.getCor());
                
                SnackbarModel.showSnackbar(SnackbarModel.COMPROMISSO_ADD);
                
                pstm.execute();
                Conexao.fechaConexao(conn, pstm);
                
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Mensagem de Erro");
                alert.setContentText("Não foi possível inserir Compromisso: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível inserir Compromisso, parâmetro vazio");
            alert.showAndWait();
        }
    }
    
    public void updateCompromisso(Compromisso compromisso) {
        if(compromisso != null) {
            Connection conn = null;

            try {
                conn = Conexao.getConexao();
                PreparedStatement pstm = conn.prepareStatement(UPDATE_COMPROMISSO);

                pstm.setString(1, compromisso.getDescricao());
                pstm.setString(2, compromisso.getCor());
                pstm.setInt(3, compromisso.getId());
                
                SnackbarModel.showSnackbar(SnackbarModel.COMPROMISSO_UPD);
                
                pstm.execute();
                Conexao.fechaConexao(conn);

            } catch(Exception e) {
                Modelo.getInstance().showAlertErro("Não foi possível atualizar Compromisso: " + e.getMessage());
            }

        } 
        else {
            Modelo.getInstance().showAlertErro("Não foi possível atualizar Compromisso, parâmetro vazio");
        }
    }
    
    public void updateCompromissoCor(Compromisso compromisso) {
        if(compromisso != null) {
            Connection conn = null;

            try {
                conn = Conexao.getConexao();
                PreparedStatement pstm = conn.prepareStatement(UPDATE_COR_COMPROMISSO);

                pstm.setString(1, compromisso.getCor());
                pstm.setInt(2, compromisso.getId());
                
                SnackbarModel.showSnackbar(SnackbarModel.COMPROMISSO_UPD);
                
                pstm.execute();
                Conexao.fechaConexao(conn);

            } catch(Exception e) {
                Modelo.getInstance().showAlertErro("Não foi possível atualizar Compromisso: " + e.getMessage());
            }

        } 
        else {
            Modelo.getInstance().showAlertErro("Não foi possível atualizar Compromisso, parâmetro vazio");
        }
    }
    
    public void updateCompromissoDescr(Compromisso compromisso) {
        if(compromisso != null) {
            Connection conn = null;

            try {
                conn = Conexao.getConexao();
                PreparedStatement pstm = conn.prepareStatement(UPDATE_DESCR_COMPROMISSO);

                pstm.setString(1, compromisso.getDescricao());
                pstm.setInt(2, compromisso.getId());
                
                SnackbarModel.showSnackbar(SnackbarModel.COMPROMISSO_UPD);
                
                pstm.execute();
                Conexao.fechaConexao(conn);

            } catch(Exception e) {
                Modelo.getInstance().showAlertErro("Não foi possível atualizar Compromisso: " + e.getMessage());
            }

        } 
        else {
            Modelo.getInstance().showAlertErro("Não foi possível atualizar Compromisso, parâmetro vazio");
        }
    }
    
    public ObservableList<Compromisso> getListCompromisso() {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ObservableList<Compromisso> listCompromisso = FXCollections.observableArrayList();
            
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_COMPROMISSO);
            rs = pstm.executeQuery();

            while(rs.next()) {
                Compromisso compromisso = new Compromisso();
                compromisso.setId(rs.getInt("COMP_ID"));
                compromisso.setDescricao(rs.getString("COMP_DESCRICAO"));
                compromisso.setTipo(rs.getString("COMP_TIPO"));
                compromisso.setCor(rs.getString("COMP_COR"));
                
                listCompromisso.add(compromisso);
            }
            
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar Compromisso: " + e.getMessage());
            alert.showAndWait();
        }

        return listCompromisso;
    }
    
    public ObservableList<Compromisso> getListCompromissoVeterinario() {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ObservableList<Compromisso> listCompromisso = FXCollections.observableArrayList();
            
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_COMPROMISSO_VETERINARIO);
            rs = pstm.executeQuery();

            while(rs.next()) {
                Compromisso compromisso = new Compromisso();
                compromisso.setId(rs.getInt("COMP_ID"));
                compromisso.setDescricao(rs.getString("COMP_DESCRICAO"));
                compromisso.setTipo(rs.getString("COMP_TIPO"));
                compromisso.setCor(rs.getString("COMP_COR"));
                
                listCompromisso.add(compromisso);
            }
            
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar Compromisso: " + e.getMessage());
            alert.showAndWait();
        }

        return listCompromisso;
    }
    
    public ObservableList<Compromisso> getListCompromissoAvulso() {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ObservableList<Compromisso> listCompromisso = FXCollections.observableArrayList();
            
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_COMPROMISSO_AVULSO);
            rs = pstm.executeQuery();

            while(rs.next()) {
                Compromisso compromisso = new Compromisso();
                compromisso.setId(rs.getInt("COMP_ID"));
                compromisso.setDescricao(rs.getString("COMP_DESCRICAO"));
                compromisso.setTipo(rs.getString("COMP_TIPO"));
                compromisso.setCor(rs.getString("COMP_COR"));
                
                listCompromisso.add(compromisso);
            }
            
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar Compromisso: " + e.getMessage());
            alert.showAndWait();
        }

        return listCompromisso;
    }
    
    public Compromisso getCompromissoById(int idCompromisso) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Compromisso compromisso = new Compromisso();
            
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_BY_ID_COMPROMISSO);
            pstm.setInt(1, idCompromisso);
            rs = pstm.executeQuery();

            while(rs.next()) {
                compromisso.setId(rs.getInt("COMP_ID"));
                compromisso.setDescricao(rs.getString("COMP_DESCRICAO"));
                compromisso.setTipo(rs.getString("COMP_TIPO"));
                compromisso.setCor(rs.getString("COMP_COR"));
            }
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar Compromisso: " + e.getMessage());
            alert.showAndWait();
        }

        return compromisso;
    }
    
    public Compromisso getCompromissoByTipoDescricao(Compromisso compromisso) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Compromisso comp = new Compromisso();
            
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_COMPROMISSO_BY_TIPO_DESCRICAO);
            pstm.setString(1, compromisso.getDescricao());
            pstm.setString(2, compromisso.getTipo());
            rs = pstm.executeQuery();

            while(rs.next()) {
                comp.setId(rs.getInt("COMP_ID"));
                comp.setDescricao(rs.getString("COMP_DESCRICAO"));
                comp.setTipo(rs.getString("COMP_TIPO"));
                comp.setCor(rs.getString("COMP_COR"));
            }
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar Compromisso: " + e.getMessage());
            alert.showAndWait();
        }

        return comp;
    }
    
    public void removeCompromisso(Compromisso compromisso) {
        Connection conn = null;
        PreparedStatement pstm = null;
        
        DaoEvento daoEvento = new DaoEvento();
        daoEvento.removeEventoCompromisso(compromisso);
        
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(DELETE_COMPROMISSO);

            pstm.setInt(1, compromisso.getId());
            
            SnackbarModel.showSnackbar(SnackbarModel.COMPROMISSO_DEL);
            
            pstm.execute();
            Conexao.fechaConexao(conn, pstm);

        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível excluir o Compromisso: " +e.getMessage());
        }
    }
}

