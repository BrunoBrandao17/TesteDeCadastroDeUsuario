package org.example.usuario;

public class Usuario {

    private String nome;
    private String login;
    private String senha;
    private String email;
    private NivelAcesso nivelAcesso;

    private int tentativasInvalidas = 0;
    private boolean bloqueado = false;

    // Construtor completo especificando o nível de acesso
    public Usuario(String nome, String login, String senha, String email, NivelAcesso nivelAcesso) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.nivelAcesso = nivelAcesso;
    }

    // Construtor alternativo que atribui o nível CLIENTE por padrão
    public Usuario(String nome, String login, String senha, String email) {
        this(nome, login, senha, email, NivelAcesso.CLIENTE);
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }

    public NivelAcesso getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(NivelAcesso nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }

    public int getTentativasInvalidas() {
        return tentativasInvalidas;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void registrarTentativaInvalida() {
        this.tentativasInvalidas++;
        if (this.tentativasInvalidas >= 3) {
            this.bloqueado = true;
        }
    }

    public void resetarTentativas() {
        this.tentativasInvalidas = 0;
    }
}