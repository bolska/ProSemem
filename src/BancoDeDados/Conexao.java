/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDeDados;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 *
 * @author user
 */
public class Conexao {
    //local
    private static final String USUARIO = "root";
    private static final String SENHA = "0888501088";
    private static final String DATABASE = "mydb";
    private static final String DRIVER_CONEXAO = "com.mysql.jdbc.Driver";
    private static final String STR_CONEXAO = "jdbc:mysql://localhost:3306/";
    
    //Nuvem
//    private static final String USUARIO = "sql10223376";
//    private static final String SENHA = "SG8Tkqsnj6";
//    private static final String DATABASE = "sql10223376";
//    private static final String DRIVER_CONEXAO = "com.mysql.jdbc.Driver";
//    private static final String STR_CONEXAO = "jdbc:mysql://sql10.freemysqlhosting.net:3306/";
    
    public static Connection getConexao() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        
        
        try {
            Class.forName(DRIVER_CONEXAO);
            conn = DriverManager.getConnection(STR_CONEXAO + DATABASE, USUARIO, SENHA);
            
            return conn;
            
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException(
                                "Driver MySql não foi encontrado " + e.getMessage());
            
        } catch (SQLException e) {
            throw new SQLException(
                                "Erro ao conectar " + "com a base de dados" + e.getMessage());
        }
        
    }
    
    public static void fechaConexao(Connection conn) {
        try {
            if(conn != null) {
                conn.close();
            }
            
        } catch(Exception e) {
            System.out.println("Não foi possivel fechar a conexão com o banco de dados " + e.getLocalizedMessage());
        }
    }
    
    public static void fechaConexao(Connection conn, PreparedStatement stmt) {
        try {
            if(conn != null) {
                fechaConexao(conn);
            }
            if(stmt != null) {
                stmt.close();
            }
            
        } catch (Exception e) {
            System.out.println("Não foi possivel fechar o statement " + e.getMessage());
        }
    }
    
    public static void fechaConexao(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if(conn != null || stmt != null) {
                fechaConexao(conn, stmt);
            }
            if(rs != null) {
                rs.close();
            }
            
        } catch(Exception e) {
            System.out.println("Não foi possivel fechar o ResultSet " + e.getMessage());
        }
    }
}
