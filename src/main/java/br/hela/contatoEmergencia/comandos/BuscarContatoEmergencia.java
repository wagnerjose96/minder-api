package br.hela.contatoEmergencia.comandos;

import br.hela.contatoEmergencia.ContatoEmergencia;
import br.hela.contatoEmergencia.ContatoEmergenciaId;
import br.hela.telefone.Telefone;

public class BuscarContatoEmergencia {
	private ContatoEmergenciaId idContatoEmergencia;
	private String nomeContato;
	private Telefone telefone;

	public BuscarContatoEmergencia(ContatoEmergencia comando) {
		this.idContatoEmergencia = comando.getIdContatoEmergencia();
		this.nomeContato = comando.getNomeContato();
	}

	public ContatoEmergenciaId getIdContatoEmergencia() {
		return idContatoEmergencia;
	}

	public String getNomeContato() {
		return nomeContato;
	}

	public void setNomeContato(String nomeContato) {
		this.nomeContato = nomeContato;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

}
