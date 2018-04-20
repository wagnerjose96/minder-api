package br.hela.contatoEmergencia.comandos;

import java.util.ArrayList;
import java.util.List;

import br.hela.contatoEmergencia.ContatoEmergenciaId;
import br.hela.telefone.TelefoneId;

public class EditarContatoEmergencia {
	private ContatoEmergenciaId idContatoEmergencia;
	private String nomeContato;
	private Boolean contatoPrincipal;
	private List<TelefoneId> id_telefones = new ArrayList<TelefoneId>();

	public EditarContatoEmergencia() {
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

	public Boolean getContatoPrincipal() {
		return contatoPrincipal;
	}

	public void setContatoPrincipal(Boolean contatoPrincipal) {
		this.contatoPrincipal = contatoPrincipal;
	}
	
	public List<TelefoneId> getId_telefones() {
		return id_telefones;
	}

	public void setId_telefones(List<TelefoneId> id_telefones) {
		this.id_telefones = id_telefones;
	}
}
