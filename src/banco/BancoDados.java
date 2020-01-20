/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author helen
 */
public class BancoDados {
    private boolean statusConexao;
    private Connection conexao;
    private String mensagemErro;
    private String nomeDriver = "com.mysql.cj.jdbc.Driver";
    private String nomeServidor = "localhost";
    private String portaServidor = "3306";
    private String nomeUsuario = "root";
    private String senha = "vertrigo";
    private String nomeBanco = "escola";
    private String url;

    public BancoDados() {
        url = "jdbc:mysql://" + this.nomeServidor;
        url += ":" + this.portaServidor + "/"+ this.nomeBanco;
        url += "?autoReconnect=true&useSSL=false&useTimezone=true&serverTimezone=UTC";
        this.statusConexao = false;
        this.realizaConexao();
    }
    
    public boolean getStatusConexao() {
        return statusConexao;
    }

    public Connection getConexao() {
        return conexao;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }
    public void realizaConexao(){
        try{
            Class.forName(this.nomeDriver);
            //Cria a conexão
            this.conexao = DriverManager.getConnection(this.url, this.nomeUsuario, this.senha);
            this.statusConexao = true;
        }
        catch(ClassNotFoundException | SQLException ex){
            this.mensagemErro = ex.toString();
            this.statusConexao = false;
        }
    }
    public void encerraConexao(){
        try{
            this.conexao.close();
            this.statusConexao = false;
        }
        catch(SQLException ex){
            this.mensagemErro = "Não foi possível encerrar a conexão "+ex;
        }
    }
}
