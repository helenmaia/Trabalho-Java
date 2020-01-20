/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author helen
 */
public class Professor {
    private int idProf;
    private String nome;
    private String registro;
    private String dataNascimento;
    private String email;
    private Connection conexao;
     
    public Professor(Connection conexao) {
        this.conexao = conexao;
    }
    
    public Professor() {
        
    }
    

    public int getIdProf() {
        return idProf;
    }

    public void setIdProf(int idProf) {
        this.idProf = idProf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Connection getConexao() {
        return conexao;
    }

    public void setConexao(Connection conexao) {
        this.conexao = conexao;
    }
    
    public ResultSet listarProfessores() throws SQLException{
        String sql = "select "
                + "id, "
                + "nome, "
                + "email, "
                + "registro,"
                + "dataNascimento "
                + "from prof";
        PreparedStatement req = this.conexao.prepareStatement(sql);
        return req.executeQuery();  
    }
    
    public void inserirProfessor() throws SQLException{
        String sql = "insert into prof(nome, email, registro, dataNascimento) values(?,?,?,?)";
        try (PreparedStatement req = this.conexao.prepareStatement(sql)) {
            req.setString(1, this.nome);
            req.setString(2, this.email);
            req.setString(3, this.registro);
            req.setString(4, this.dataNascimento);         
            req.executeUpdate();
        }
    }
    
    public void atualizarProfessor() throws SQLException{
        String sql = "update prof set nome=?, email=?, registro=?, dataNascimento=? where id =?";
        try (PreparedStatement req = this.conexao.prepareStatement(sql)) {
            req.setString(1, this.nome);
            req.setString(2, this.email);
            req.setString(3, this.registro);
            req.setString(4, this.dataNascimento);
            req.setInt(5, this.idProf);
            req.executeUpdate();
        }   
    }   
    
    public void excluirProfessor(int idProf) throws SQLException{
        String sql = "delete from prof where id =?";
        try (PreparedStatement req = this.conexao.prepareStatement(sql)) {
            req.setInt(1, idProf);
            req.executeUpdate();
        }
        
    }
    
}
