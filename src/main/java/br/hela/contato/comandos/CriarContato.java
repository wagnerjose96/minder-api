package br.hela.contato.comandos;

import br.hela.telefone.comandos.CriarTelefone;

public class CriarContato {
	private String nome;
	private CriarTelefone telefone;

	public CriarContato() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public CriarTelefone getTelefone() {
		return telefone;
	}

	public void setTelefone(CriarTelefone telefone) {
		this.telefone = telefone;
	}

}
