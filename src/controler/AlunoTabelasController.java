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
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Aluno;
/**
 * FXML Controller class
 *
 * @author helen
 */
public class AlunoTabelasController implements Initializable {
    
      @FXML
    private AnchorPane painelP;
    
     @FXML
    private Button btnAdicionar;

    @FXML
    private TextField campoDataNascimento;

    @FXML
    private TableColumn<Aluno, String> colNascimento;

    @FXML
    private Label lbId;

    @FXML
    private Button btnEdit;

    @FXML
    private TableColumn<Aluno, Integer> coId;

    @FXML
    private TableColumn<Aluno, String> colEmail;

    @FXML
    private TableColumn<Aluno, String> colMatricula;

    @FXML
    private TableColumn<Aluno, String> colAluno;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<Aluno> tableAluno;

    @FXML
    private Button btnDelete;

    @FXML
    private TextField campoEmail;

    @FXML
    private TextField campoNome;

    @FXML
    private TextField campoMatricula;
    
    @FXML
    private Label lblVoltar;

    @FXML
    public void atualizaTabela() {
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
    void principal(MouseEvent event) {
         try {
            AnchorPane novoPainel = FXMLLoader.load(this.getClass().getResource("/view/Principal.fxml"));
            painelP.getChildren().setAll(novoPainel);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    
    @FXML
    void cancelar(MouseEvent event) {
        campoMatricula.clear();
        campoNome.clear();
        campoEmail.clear();
        campoDataNascimento.clear();
        lbId.setText(null);
        editMode(false);
        op(true);
    }

    @FXML
    void salvar(MouseEvent event) {
           BancoDados bd = new BancoDados();
        if (!campoNome.getText().equals("") || !campoMatricula.getText().equals("") || !campoEmail.getText().equals("") || !campoDataNascimento.getText().equals("")) {
            if (bd.getStatusConexao()) {
                Aluno aluno = new Aluno(bd.getConexao());
                aluno.setNome(campoNome.getText());
                aluno.setMatricula(campoMatricula.getText());
                aluno.setEmail(campoEmail.getText());
                aluno.setDataNascimento(campoDataNascimento.getText());

                if (lbId.getText() == null) {
                    try {
                        aluno.inserirAluno();
                    } catch (SQLException e) {
                        falha("Erro", "Não foi possível adicionar o  aluno " + aluno.getNome() + "", null);
                    }
                } else {
                    try {
                        aluno.setIdAluno(Integer.parseInt(lbId.getText()));
                        aluno.atualizarAluno();
                    } catch (NumberFormatException | SQLException e) {
                    }
                }
            }
            editMode(false);
            op(true);
            apagarCampo();
            atualizaTabela();
        } else {
            falha("Erro", "Preencha todos os campos!", null);
        }
    }

    @FXML
    void deletar(MouseEvent event) {
            if (tableAluno.getSelectionModel().getSelectedItem() != null) {
            BancoDados bd = new BancoDados();
            Aluno aluno = tableAluno.getSelectionModel().getSelectedItem();
            aluno.setConexao(bd.getConexao());
            if (verificar("Deseja apagar?", "Tens certeza que quer apagar " + aluno.getNome())) {
                try {
                    aluno.excluirAluno(aluno.getIdAluno());
                    atualizaTabela();
                } catch (SQLException e) {
                    falha("Falha ao apagar", "Não foi possivel excluir o registro", null);
                }
            }
            bd.encerraConexao();
        } else {
            falha("Falha", "Escolha um resgitro", "excluir");
        }
    }
    @FXML
     public boolean verificar(String titulo, String mensagem){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(mensagem);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(ButtonType.YES);
        alert.getButtonTypes().add(ButtonType.NO);
        Optional<ButtonType> option = alert.showAndWait();
        return option.get() == ButtonType.YES;
    }

    @FXML
    public void falha(String tit, String msg, String op) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(tit);
        a.setHeaderText(msg);
        if (op != null) {
            a.setContentText("Escolha uma linha para " + op + " um documento.");
        }
        a.showAndWait();
    }
    
     @FXML
    public void op(boolean mostra) {
        btnAdicionar.setDisable(!mostra);
        btnEdit.setDisable(!mostra);
        btnDelete.setDisable(!mostra);
    }

    @FXML
    public void editMode(boolean mostra) {
        campoNome.setEditable(mostra);
        campoMatricula.setEditable(mostra);
        campoEmail.setEditable(mostra);
        campoDataNascimento.setEditable(mostra);
        btnSave.setDisable(!mostra);
        btnCancel.setDisable(!mostra);
    }

    @FXML
    void modificar(MouseEvent event) {
         if (tableAluno.getSelectionModel().getSelectedItem() != null) {
            editMode(true);
            op(false);
            Aluno aluno = tableAluno.getSelectionModel().getSelectedItem();
            lbId.setText(Integer.toString(aluno.getIdAluno()));
            campoMatricula.setText(aluno.getMatricula());
            campoNome.setText(aluno.getNome());
            campoEmail.setText(aluno.getEmail());
            campoDataNascimento.setText(aluno.getDataNascimento());
        } else {
            falha("Erro", "Selecione uma linha", "editar");
        }
    }
        
    @FXML
    public void apagarCampo() {
        campoMatricula.clear();
        campoNome.clear();
        campoEmail.clear();
        campoDataNascimento.clear();
        lbId.setText(null);
    }

    
    @FXML
    void add(MouseEvent event) {
        editMode(true);
        op(false);
    }


    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        coId.setCellValueFactory(new PropertyValueFactory("id"));
        colAluno.setCellValueFactory(new PropertyValueFactory("nome"));
        colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colNascimento.setCellValueFactory(new PropertyValueFactory("dataNascimento"));
        lbId.setText(null);
        editMode(false);
        atualizaTabela();
       
    }    
    
}
