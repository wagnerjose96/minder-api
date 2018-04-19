package br.hela.contatoEmergencia.comandos;

import java.util.ArrayList;
import java.util.List;

import br.hela.contatoEmergencia.ContatoEmergenciaId;

public class EditarContatoEmergencia {
	private ContatoEmergenciaId idContatoEmergencia;
	private String nomeContato;
	private Boolean contatoPrincipal;
	private List<String> telefones = new ArrayList<>();

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
	
	public List<String> getMedicamentos() {
		return telefones;
	}

	public void setMedicamentos(List<String> telefones) {
		this.telefones = telefones;
	}

}
