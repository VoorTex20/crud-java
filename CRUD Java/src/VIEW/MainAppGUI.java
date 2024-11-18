package VIEW;

import ConnectionFactory.ProdutoDAO;
import ConnectionFactory.Produto;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class MainAppGUI extends Application {
    private Connection connection;
    private ProdutoDAO produtoDAO;

    @Override
    public void start(Stage primaryStage) {
        // Conexão com o banco de dados
        String url = "jdbc:mysql://localhost:3306/db_estoque";
        String user = "root";
        String password = "joaovini20";

        try {
            connection = DriverManager.getConnection(url, user, password);
            produtoDAO = new ProdutoDAO(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Criação do layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(5);
        grid.setHgap(5);

        // Campos de entrada
        TextField nomeField = new TextField();
        Button createButton = new Button("Criar Produto");
        Button readButton = new Button("Ler Produtos");
        Button updateButton = new Button("Atualizar Produto");
        Button deleteButton = new Button("Deletar Produto");

        // Adicionando os elementos ao grid
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(nomeField, 1, 0);
        grid.add(createButton, 0, 1);
        grid.add(readButton, 1, 1);
        grid.add(updateButton, 0, 2);
        grid.add(deleteButton, 1, 2);

        // Configurando ações dos botões
        createButton.setOnAction(e -> createProduct(nomeField.getText()));
        readButton.setOnAction(e -> readProducts());
        updateButton.setOnAction(e -> showUpdateWindow());
        deleteButton.setOnAction(e -> showDeleteWindow());

        // Configuração da cena e do estágio
        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setTitle("CRUD de Produtos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para exibir a janela de atualização
    private void showUpdateWindow() {
        Stage updateStage = new Stage();
        updateStage.initModality(Modality.APPLICATION_MODAL);
        updateStage.setTitle("Atualizar Produto");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(5);
        grid.setHgap(5);

        TextField idField = new TextField();
        TextField novoNomeField = new TextField();
        Button updateButton = new Button("Atualizar");

        grid.add(new Label("ID do Produto:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Novo Nome:"), 0, 1);
        grid.add(novoNomeField, 1, 1);
        grid.add(updateButton, 1, 2);

        updateButton.setOnAction(e -> {
            updateProduct(idField.getText(), novoNomeField.getText());
            updateStage.close();
        });

        Scene scene = new Scene(grid, 300, 150);
        updateStage.setScene(scene);
        updateStage.show();
    }

    // Método para exibir a janela de exclusão
    private void showDeleteWindow() {
        Stage deleteStage = new Stage();
        deleteStage.initModality(Modality.APPLICATION_MODAL);
        deleteStage.setTitle("Deletar Produto");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(5);
        grid.setHgap(5);

        TextField idField = new TextField();
        Button deleteButton = new Button("Deletar");

        grid.add(new Label("ID do Produto:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(deleteButton, 1, 1);

        deleteButton.setOnAction(e -> {
            deleteProduct(idField.getText());
            deleteStage.close();
        });

        Scene scene = new Scene(grid, 300, 100);
        deleteStage.setScene(scene);
        deleteStage.show();
    }

    // Métodos para operações CRUD
    private void createProduct(String nome) {
        try {
            // Verifica se o nome não está vazio
            if (nome.isEmpty()) {
                showAlert("O nome do produto é obrigatório.");
                return;
            }

            Produto novoProduto = new Produto(0, nome, new java.util.Date());
            produtoDAO.create(novoProduto);
            showAlert("Produto criado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erro ao criar produto.");
        }
    }

    private void readProducts() {
        try {
            List<Produto> produtos = produtoDAO.read();
            StringBuilder sb = new StringBuilder();
            for (Produto produto : produtos) {
                sb.append("ID: ").append(produto.getId()).append(", Nome: ").append(produto.getNome()).append("\n");
            }
            showAlert(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erro ao ler produtos.");
        }
    }

    private void updateProduct(String idStr, String novoNome) {
        try {
            // Verifica se o novo nome não está vazio
            if (novoNome.isEmpty()) {
                showAlert("O nome do produto é obrigatório.");
                return;
            }

            int id = Integer.parseInt(idStr);
            Produto produtoExistente = produtoDAO.findById(id);
            if (produtoExistente == null) {
                showAlert("Produto não encontrado.");
                return;
            }

            produtoExistente.setNome(novoNome);
            produtoDAO.update(produtoExistente);
            showAlert("Produto atualizado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erro ao atualizar produto.");
        } catch (NumberFormatException e) {
            showAlert("ID inválido.");
        }
    }

    private void deleteProduct(String idStr) {
        try {
            // Verifica se o ID foi informado
            if (idStr.isEmpty()) {
                showAlert("Por favor, insira um ID para deletar o produto.");
                return;
            }

            int id = Integer.parseInt(idStr);
            Produto produtoExistente = produtoDAO.findById(id);
            if (produtoExistente == null) {
                showAlert("Produto com ID " + id + " não encontrado.");
                return;
            }

            produtoDAO.delete(id);
            showAlert("Produto deletado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erro ao deletar produto.");
        } catch (NumberFormatException e) {
            showAlert("ID inválido.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
