/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler;

import banco.BancoDados;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Aluno;
import model.Professor;

/**
 * FXML Controller class
 *
 * @author helen
 */
public class PrincipalController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    
    @FXML
    private TableColumn<Aluno, String> colNascimento;

    @FXML
    private AnchorPane painelP;

    @FXML
    private TableColumn<Professor, String> colNascimentop;

    @FXML
    private TableColumn<Aluno, Integer> coId;

    @FXML
    private Button alun;

    @FXML
    private TableColumn<Professor, String> colRegistro;

    @FXML
    private Button prof;

    @FXML
    private TableColumn<Aluno, String> colEmail;

    @FXML
    private TableColumn<Professor, Integer> coIdp;

    @FXML
    private TableColumn<Aluno, String> colMatricula;

    @FXML
    private TableView<Professor> tableProf;

    @FXML
    private TableColumn<Aluno, String> colAluno;

    @FXML
    private TableColumn<Professor, String> colEmailp;

    @FXML
    private TableColumn<Professor, String> colProf;

    @FXML
    private TableView<Aluno> tableAluno;

    @FXML
    void dirProf(MouseEvent event) {
             try {
            AnchorPane novoPainel = FXMLLoader.load(this.getClass().getResource("/view/ProfTabelas.fxml"));
            painelP.getChildren().setAll(novoPainel);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void dirAlun(MouseEvent event) {
          try {
            AnchorPane novoPainel = FXMLLoader.load(this.getClass().getResource("/view/AlunoTabela.fxml"));
            painelP.getChildren().setAll(novoPainel);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
       @FXML
    public void atualizaAluno() {
        ObservableList<Aluno> listaAlunos = FXCollections.observableArrayList();
        BancoDados bd = new BancoDados();
        Aluno aluno = new Aluno(bd.getConexao());
        try {
            ResultSet listaResult = aluno.listarAlunos();
            while (listaResult.next()) {
                Aluno ax = new Aluno(bd.getConexao());
                ax.setIdAluno(listaResult.getInt("id"));
                ax.setMatricula(listaResult.getString("matricula"));
                ax.setNome(listaResult.getString("nome"));
                ax.setEmail(listaResult.getString("email"));
                ax.setDataNascimento(listaResult.getString("dataNascimento"));
                listaAlunos.add(ax);
            }
            bd.encerraConexao();
        } catch (SQLException ex) {
        }

        tableAluno.setItems(listaAlunos);
    }
    @FXML
    public void atualizaProfessor() {
        ObservableList<Professor> listaProfessores = FXCollections.observableArrayList();
        BancoDados bd = new BancoDados();
        Professor prof = new Professor(bd.getConexao());
        try {
            ResultSet listaResult = prof.listarProfessores();
            while (listaResult.next()) {
                Professor p = new Professor(bd.getConexao());
                p.setIdProf(listaResult.getInt("id"));
                p.setRegistro(listaResult.getString("registro"));
                p.setNome(listaResult.getString("nome"));
                p.setEmail(listaResult.getString("email"));
                p.setDataNascimento(listaResult.getString("dataNascimento"));
                listaProfessores.add(p);
            }
            bd.encerraConexao();
        } catch (SQLException ex) {
        }

        tableProf.setItems(listaProfessores);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        coId.setCellValueFactory(new PropertyValueFactory("id"));
        colAluno.setCellValueFactory(new PropertyValueFactory("nome"));
        colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colNascimento.setCellValueFactory(new PropertyValueFactory("dataNascimento"));
        
        coIdp.setCellValueFactory(new PropertyValueFactory("id"));
        colProf.setCellValueFactory(new PropertyValueFactory("nome"));
        colRegistro.setCellValueFactory(new PropertyValueFactory("registro"));
        colEmailp.setCellValueFactory(new PropertyValueFactory("email"));
        colNascimentop.setCellValueFactory(new PropertyValueFactory("dataNascimento"));
        atualizaAluno();
        atualizaProfessor();
        
        
    }    
    
}
