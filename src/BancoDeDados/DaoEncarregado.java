/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDeDados;

import Classes.Encarregado;
import Model.Modelo;
import Model.SnackbarModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 *
 * @author Bolska
 */
public class DaoEncarregado {
    private final String INSERT_ENCARREGADO = "INSERT INTO ENCARREGADO (ENCAR_ID, ENCAR_NOME) VALUES (0,?)";
    private final String UPDATE_ENCARREGADO = "UPDATE ENCARREGADO SET ENCAR_NOME=? WHERE ENCAR_ID=?";
    private final String DELETE_ENCARREGADO = "DELETE FROM ENCARREGADO WHERE ENCAR_ID=?";
    private final String LIST_ENCARREGADO = "SELECT * FROM ENCARREGADO";
    private final String LIST_BY_ID_ENCARREGADO = "SELECT * FROM ENCARREGADO WHERE ENCAR_ID=?";
    private final String LIST_BY_NOME_ENCARREGADO = "SELECT * FROM ENCARREGADO WHERE ENCAR_NOME=?";

    
    public void insertEncarregado(Encarregado encarregado) {
        if(encarregado != null) {
            Connection conn = null;
            
            try {
                conn = Conexao.getConexao();
                PreparedStatement pstm;
                pstm = conn.prepareStatement(INSERT_ENCARREGADO);
                
                pstm.setString(1, encarregado.getNome());
                
                pstm.execute();
                Conexao.fechaConexao(conn, pstm);
                
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Mensagem de Erro");
                alert.setContentText("Não foi possível inserir Encarregado: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível inserir Encarregado, parâmetro vazio");
            alert.showAndWait();
        }
    }
    
    public void updateEncarregado(Encarregado encarregado) {
        if(encarregado != null) {
            Connection conn = null;

            try {
                conn = Conexao.getConexao();
                PreparedStatement pstm = conn.prepareStatement(UPDATE_ENCARREGADO);

                pstm.setString(1, encarregado.getNome());
                pstm.setInt(2, encarregado.getId());
                
                SnackbarModel.showSnackbar(SnackbarModel.ENCARREGADO_UPD);
                
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
    
    public ObservableList<Encarregado> getListEncarregado() {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ObservableList<Encarregado> listEncarregado = FXCollections.observableArrayList();
            
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_ENCARREGADO);
            rs = pstm.executeQuery();

            while(rs.next()) {
                Encarregado encarregado = new Encarregado();
                encarregado.setId(rs.getInt("ENCAR_ID"));
                encarregado.setNome(rs.getString("ENCAR_NOME"));
                
                listEncarregado.add(encarregado);
            }
            
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar Encarregado: " + e.getMessage());
            alert.showAndWait();
        }

        return listEncarregado;
    }
    
    public Encarregado getEncarregadoById(int encarregadoId) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Encarregado encarregado = new Encarregado();
            
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_BY_ID_ENCARREGADO);
            pstm.setInt(1, encarregadoId);
            rs = pstm.executeQuery();

            while(rs.next()) {
                encarregado.setId(rs.getInt("ENCAR_ID"));
                encarregado.setNome(rs.getString("ENCAR_NOME"));
            }
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar Encarregado: " + e.getMessage());
            alert.showAndWait();
        }

        return encarregado;
    }
    
    public Encarregado getEncarregadoByNome(String encarregadoNome){
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Encarregado encarregado = new Encarregado();
            
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(LIST_BY_NOME_ENCARREGADO);
            pstm.setString(1, encarregadoNome);
            rs = pstm.executeQuery();

            while(rs.next()) {
                encarregado.setId(rs.getInt("ENCAR_ID"));
                encarregado.setNome(rs.getString("ENCAR_NOME"));
            }
            Conexao.fechaConexao(conn, pstm, rs);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Mensagem de Erro");
            alert.setContentText("Não foi possível listar Encarregado: " + e.getMessage());
            alert.showAndWait();
        }

        return encarregado;
    }
    
    public void removeEncarregado(Encarregado encarregado) {
        Connection conn = null;
        PreparedStatement pstm = null;
        
        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(DELETE_ENCARREGADO);

            pstm.setInt(1, encarregado.getId());
            
            SnackbarModel.showSnackbar(SnackbarModel.ENCARREGADO_DEL);
            
            pstm.execute();
            Conexao.fechaConexao(conn, pstm);

        } catch(Exception e) {
            Modelo.getInstance().showAlertErro("Não foi possível excluir o Encarregado: " +e.getMessage());
        }
    }
}
