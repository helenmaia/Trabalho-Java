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
public class Aluno {
    private int idAluno;
    private String matricula;
    private String nome;
    private String dataNascimento;
    private String email;
    private Connection conexao;
    
    public Aluno(Connection conexao) {
        this.conexao = conexao;
    }
    
    public Aluno() {
        
    }
    
    
    public int getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
    
    public ResultSet listarAlunos() throws SQLException{
        String sql = "select "
                + "id, "
                + "nome, "
                + "email, "
                + "matricula,"
                + "dataNascimento "
                + "from alunos";
        PreparedStatement req = this.conexao.prepareStatement(sql);
        return req.executeQuery();  
    }
    
    public void inserirAluno() throws SQLException{
        String sql = "insert into alunos(nome, email, matricula, dataNascimento) values(?,?,?,?)";
        try (PreparedStatement req = this.conexao.prepareStatement(sql)) {
            req.setString(1, this.nome);
            req.setString(2, this.email);
            req.setString(3, this.matricula);
            req.setString(4, this.dataNascimento);         
            req.executeUpdate();
        }
    }
    
    public void atualizarAluno() throws SQLException{
        String sql = "update alunos set nome=?, email=?, matricula=?, dataNascimento=? where id =?";
        try (PreparedStatement req = this.conexao.prepareStatement(sql)) {
            req.setString(1, this.nome);
            req.setString(2, this.email);
            req.setString(3, this.matricula);
            req.setString(4, this.dataNascimento);
            req.setInt(5, this.idAluno);
            req.executeUpdate();
        }   
    }   
    
    public void excluirAluno(int idAluno) throws SQLException{
        String sql = "delete from alunos where id =?";
        try (PreparedStatement req = this.conexao.prepareStatement(sql)) {
            req.setInt(1, idAluno);
            req.executeUpdate();
        }
        
    }
    
}
