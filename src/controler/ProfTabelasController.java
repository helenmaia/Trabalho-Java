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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import model.Professor;

/**
 * FXML Controller class
 *
 * @author helen
 */
public class ProfTabelasController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button btnAdicionar;

    @FXML
    private TextField campoDataNascimento;

    @FXML
    private TableColumn<Professor, String> colNascimentop;

    @FXML
    private Label lbId;

    @FXML
    private Button btnEdit;

    @FXML
    private TableColumn<Professor, Integer> coIdp;

    @FXML
    private TableColumn<Professor, String> colRegistro;

    @FXML
    private TableColumn<Professor, String> colEmailp;

    @FXML
    private AnchorPane painelP;

    @FXML
    private Button btnCancel;

    @FXML
    private TableColumn<Professor, String> colProf;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<Professor> tableProf;

    @FXML
    private Button btnDelete;

    @FXML
    private TextField campoEmail;

    @FXML
    private TextField campoNome;

    @FXML
    private TextField campoRegistro;

    @FXML
    private Label lblVoltar;

    @FXML
    public void atualizaTabela() {
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
        campoRegistro.clear();
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
        if (!campoNome.getText().equals("") || !campoRegistro.getText().equals("") || !campoEmail.getText().equals("") || !campoDataNascimento.getText().equals("")) {
            if (bd.getStatusConexao()) {
                Professor prof = new Professor(bd.getConexao());
                prof.setNome(campoNome.getText());
                prof.setRegistro(campoRegistro.getText());
                prof.setEmail(campoEmail.getText());
                prof.setDataNascimento(campoDataNascimento.getText());

                if (lbId.getText() == null) {
                    try {
                        prof.inserirProfessor();
                    } catch (SQLException e) {
                        falha("Erro", "Não foi possível adicionar o  professor " + prof.getNome() + "", null);
                    }
                } else {
                    try {
                        prof.setIdProf(Integer.parseInt(lbId.getText()));
                        prof.atualizarProfessor();
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
        if (tableProf.getSelectionModel().getSelectedItem() != null) {
            BancoDados bd = new BancoDados();
            Professor prof = tableProf.getSelectionModel().getSelectedItem();
            prof.setConexao(bd.getConexao());
            if (verificar("Deseja apagar?", "Tens certeza que quer apagar " + prof.getNome())) {
                try {
                    prof.excluirProfessor(prof.getIdProf());
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
    public boolean verificar(String titulo, String mensagem) {
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
        campoRegistro.setEditable(mostra);
        campoEmail.setEditable(mostra);
        campoDataNascimento.setEditable(mostra);
        btnSave.setDisable(!mostra);
        btnCancel.setDisable(!mostra);
    }

    @FXML
    void modificar(MouseEvent event) {
        if (tableProf.getSelectionModel().getSelectedItem() != null) {
            editMode(true);
            op(false);
            Professor prof = tableProf.getSelectionModel().getSelectedItem();
            lbId.setText(Integer.toString(prof.getIdProf()));
            campoRegistro.setText(prof.getRegistro());
            campoNome.setText(prof.getNome());
            campoEmail.setText(prof.getEmail());
            campoDataNascimento.setText(prof.getDataNascimento());
        } else {
            falha("Erro", "Selecione uma linha", "editar");
        }
    }

    @FXML
    public void apagarCampo() {
        campoRegistro.clear();
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        coIdp.setCellValueFactory(new PropertyValueFactory("id"));
        colProf.setCellValueFactory(new PropertyValueFactory("nome"));
        colRegistro.setCellValueFactory(new PropertyValueFactory("registro"));
        colEmailp.setCellValueFactory(new PropertyValueFactory("email"));
        colNascimentop.setCellValueFactory(new PropertyValueFactory("dataNascimento"));
        lbId.setText(null);
        editMode(false);
        atualizaTabela();
    }

}
