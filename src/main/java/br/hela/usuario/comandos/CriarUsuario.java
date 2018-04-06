package br.hela.usuario.comandos;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class CriarUsuario {
	@ApiModelProperty(required=true)
	private String nome_usuario;
	@ApiModelProperty(required=true)
	private String email;
	@ApiModelProperty(required=true)
	private String senha;
	@ApiModelProperty(required=true)
	private String nome_completo;
	@ApiModelProperty(required=true)
	private String tipo_sangue;
	@ApiModelProperty(required=true)
	private String endereco;
	@ApiModelProperty(required=true)
	private int telefone;
	@ApiModelProperty(required=true)
	private Date data_nascimento;
	@ApiModelProperty(required=true)
	private String sexo;
	@ApiModelProperty(required=true)
	private String imagem_usuario;

	public CriarUsuario() {
	}

	public String getNome_usuario() {
		return nome_usuario;
	}

	public void setNome_usuario(String nome_usuario) {
		this.nome_usuario = nome_usuario;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome_completo() {
		return nome_completo;
	}

	public void setNome_completo(String nome_completo) {
		this.nome_completo = nome_completo;
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
