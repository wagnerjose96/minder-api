package br.hela.convenio.comandos;

import br.hela.convenio.ConvenioId;

public class EditarConvenio {
	private ConvenioId id;
	private String nome;
	private int ativo;
	
	public EditarConvenio() {
		
	}

	public ConvenioId getId() {
		return id;
	}

	public void setId(ConvenioId id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getAtivo() {
		return ativo;
	}

	public void setAtivo(int ativo) {
		this.ativo = ativo;
	}
}
