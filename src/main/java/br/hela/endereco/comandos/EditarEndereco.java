package br.hela.endereco.comandos;

import br.hela.endereco.EnderecoId;

public class EditarEndereco {
	private EnderecoId id;
	private String rua;
	private String bairro;
	private String cidade;
	private String estado;
	private String complemento;
	private int numero;

	public EditarEndereco() {
	}

	public EnderecoId getId() {
		return id;
	}

	public void setId(EnderecoId id) {
		this.id = id;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

}
