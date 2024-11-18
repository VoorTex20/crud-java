package ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private Connection connection;

    public ProdutoDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE
    public void create(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (nome, data_aquisicao) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, produto.getNome());
            statement.setDate(2, new java.sql.Date(produto.getDataAquisicao().getTime())); // Converte para java.sql.Date
            statement.executeUpdate();
        }
    }


    // READ
    public List<Produto> read() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produto produto = new Produto(0, rs.getString("nome"), rs.getDate("data_aquisicao"));
                produto.setId(rs.getInt("id_produto")); // Alterado para id_produto
                produtos.add(produto);
            }
        }
        return produtos;
    }

    // UPDATE
    public void update(Produto produto) throws SQLException {
        String sql = "UPDATE produtos SET nome = ?, data_aquisicao = ? WHERE id_produto = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, produto.getNome());
            statement.setDate(2, new java.sql.Date(produto.getDataAquisicao().getTime())); // Atualize a data de aquisição
            statement.setInt(3, produto.getId()); // Use a chave primária correta
            statement.executeUpdate();
        }
    }

    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id_produto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Produto deletado com sucesso!");
        }
    }

    // FIND BY ID
    public Produto findById(int id) throws SQLException {
        String sql = "SELECT * FROM produtos WHERE id_produto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Produto produto = new Produto(id, rs.getString("nome"), rs.getDate("data_aquisicao"));
                produto.setId(rs.getInt("id_produto")); // Alterado para id_produto
                return produto;
            }
        }
        return null; // Retorna null se não encontrado
    }
}
