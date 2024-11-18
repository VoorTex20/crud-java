package ConnectionFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class MySQLConnection {
	
	private int id;
	private String Nome;
	private Date data_aquisicao;
	
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return Nome;
	}

	public void setNome(String nome) {
		Nome = nome;
	}

	public Date getData_aquisicao() {
		return data_aquisicao;
	}

	public void setData_aquisicao(Date data_aquisicao) {
		this.data_aquisicao = data_aquisicao;
	}

	public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/db_estoque"; // Substitua pelo seu URL
        String user = "root"; // Substitua pelo seu usuário
        String password = "joaovini20"; // Substitua pela sua senha

        try {
            // Estabelecendo a conexão
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Conexão bem-sucedida!");

            // Aqui você pode executar suas operações no banco

            // Fechando a conexão
            connection.close();
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados:");
            e.printStackTrace();
        }
    }
}
