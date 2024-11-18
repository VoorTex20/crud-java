package ConnectionFactory;

import java.util.Date;

public class Produto {
    private int id; // ou `Integer`, dependendo do seu uso
    private String nome;
    private Date dataAquisicao;

    // Construtor para criar um novo Produto
    public Produto(String nome) {
        this.nome = nome;
        this.dataAquisicao = new Date(); // Define a data de aquisição como a data atual
    }

    // Outros construtores, se necessário
    public Produto(int id, String nome, Date dataAquisicao) {
        this.id = id;
        this.nome = nome;
        this.dataAquisicao = dataAquisicao;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataAquisicao() {
        return dataAquisicao;
    }

    public void setDataAquisicao(Date dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }
}
