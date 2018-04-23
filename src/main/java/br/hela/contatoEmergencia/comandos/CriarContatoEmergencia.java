package br.hela.contatoEmergencia.comandos;

import br.hela.telefone.comandos.CriarTelefone;

public class CriarContatoEmergencia {
	private String nomeContato;
	private CriarTelefone telefone;

	public CriarContatoEmergencia() {
	}

	public String getNomeContato() {
		return nomeContato;
	}

	public void setNomeContato(String nomeContato) {
		this.nomeContato = nomeContato;
	}

	public CriarTelefone getTelefone() {
		return telefone;
	}

	public void setTelefone(CriarTelefone telefone) {
		this.telefone = telefone;
	}

}
