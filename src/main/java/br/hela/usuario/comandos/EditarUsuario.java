package br.hela.usuario.comandos;

import java.util.Date;

import br.hela.usuario.UsuarioId;

public class EditarUsuario {
	private UsuarioId id;
	private String nome_completo;
	private String senha;
	private String tipo_sangue;
	private String endereco;
	private int telefone;
	private Date data_nascimento;
	private String sexo;
	private String imagem_usuario;

	public EditarUsuario() {
	}

	public UsuarioId getId() {
		return id;
	}

	public void setId(UsuarioId id) {
		this.id = id;
	}

	public String getNome_completo() {
		return nome_completo;
	}

	public void setNome_completo(String nome_completo) {
		this.nome_completo = nome_completo;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getTipo_sangue() {
		return tipo_sangue;
	}

	public void setTipo_sangue(String tipo_sangue) {
		this.tipo_sangue = tipo_sangue;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public int getTelefone() {
		return telefone;
	}

	public void setTelefone(int telefone) {
		this.telefone = telefone;
	}

	public Date getData_nascimento() {
		return data_nascimento;
	}

	public void setData_nascimento(Date data_nascimento) {
		this.data_nascimento = data_nascimento;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getImagem_usuario() {
		return imagem_usuario;
	}

	public void setImagem_usuario(String imagem_usuario) {
		this.imagem_usuario = imagem_usuario;
	}

}
