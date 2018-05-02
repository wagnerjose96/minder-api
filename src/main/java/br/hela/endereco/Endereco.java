package br.hela.endereco;


import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import br.hela.endereco.comandos.CriarEndereco;
import br.hela.endereco.comandos.EditarEndereco;

@Entity
@Audited
public class Endereco {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private EnderecoId id;
	private String rua;
	private String bairro;
	private String cidade;
	private String estado;
	private String complemento;
	private int numero;

	public Endereco() {
	}
	
	public void apply(EditarEndereco comando) {
		this.id =  comando.getId();
		this.rua = comando.getRua();
		this.bairro = comando.getBairro();
		this.cidade = comando.getCidade();;
		this.estado = comando.getEstado();;
		this.complemento = comando.getComplemento();;
		this.numero = comando.getNumero();;
	}

	public Endereco(CriarEndereco comando) {
		this.id =  new EnderecoId();
		this.rua = comando.getRua();
		this.bairro = comando.getBairro();
		this.cidade = comando.getCidade();;
		this.estado = comando.getEstado();;
		this.complemento = comando.getComplemento();;
		this.numero = comando.getNumero();;
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
