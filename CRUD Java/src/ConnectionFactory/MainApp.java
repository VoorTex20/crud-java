package ConnectionFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/db_estoque"; // Substitua pelo seu URL
        String user = "root"; // Substitua pelo seu usuário
        String password = "joaovini20"; // Substitua pela sua senha

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Conexão bem-sucedida!");

            // Criar ProdutoDAO com a conexão aberta
            ProdutoDAO produtoDAO = new ProdutoDAO(connection);

            // Menu para operações CRUD
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("\nEscolha uma operação:");
                System.out.println("1 - Criar Produto");
                System.out.println("2 - Ler Produtos");
                System.out.println("3 - Atualizar Produto");
                System.out.println("4 - Deletar Produto");
                System.out.println("5 - Sair");

                int escolha = scanner.nextInt();
                scanner.nextLine(); // Consumir a nova linha após o número

                switch (escolha) {
                    case 1:
                        createProduct(produtoDAO, scanner);
                        break;
                    case 2:
                        readProducts(produtoDAO);
                        break;
                    case 3:
                        updateProduct(produtoDAO, scanner);
                        break;
                    case 4:
                        deleteProduct(produtoDAO, scanner);
                        break;
                    case 5:
                        running = false;
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            }

            System.out.println("Conexão fechada com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao conectar ou operar no banco de dados:");
            e.printStackTrace();
        }
    }

    // Métodos para as operações CRUD
    private static void createProduct(ProdutoDAO produtoDAO, Scanner scanner) throws SQLException {
        System.out.println("Digite o nome do produto:");
        String nome = scanner.nextLine();
        Date dataAquisicao = new Date(); // Aqui estou usando a data atual
        Produto novoProduto = new Produto(0, nome, dataAquisicao);
        produtoDAO.create(novoProduto);
    }

    private static void readProducts(ProdutoDAO produtoDAO) throws SQLException {
        List<Produto> produtos = produtoDAO.read();
        for (Produto produto : produtos) {
            System.out.println("ID: " + produto.getId() + ", Nome: " + produto.getNome() + ", Data de Aquisição: " + produto.getDataAquisicao());
        }
    }

    private static void updateProduct(ProdutoDAO produtoDAO, Scanner scanner) throws SQLException {
        System.out.println("Digite o ID do produto a ser atualizado:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha

        Produto produtoExistente = produtoDAO.findById(id);
        if (produtoExistente == null) {
            System.out.println("Produto com ID " + id + " não encontrado.");
            return;
        }

        System.out.println("Digite o novo nome do produto:");
        String novoNome = scanner.nextLine();
        produtoExistente.setNome(novoNome);
        produtoDAO.update(produtoExistente);
        System.out.println("Produto atualizado com sucesso!");
    }

    private static void deleteProduct(ProdutoDAO produtoDAO, Scanner scanner) throws SQLException {
        System.out.println("Digite o ID do produto a ser deletado:");
        int id = scanner.nextInt();
        produtoDAO.delete(id);
    }
}
