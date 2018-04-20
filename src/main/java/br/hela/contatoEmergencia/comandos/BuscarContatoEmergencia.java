package br.hela.contatoEmergencia.comandos;

import java.util.ArrayList;
import java.util.List;

import br.hela.contatoEmergencia.ContatoEmergencia;
import br.hela.contatoEmergencia.ContatoEmergenciaId;
import br.hela.telefone.Telefone;

public class BuscarContatoEmergencia {
	private ContatoEmergenciaId idContatoEmergencia;
	private String nomeContato;
	private Boolean contatoPrincipal;
	private List<Telefone> telefones = new ArrayList<>();
	
	public BuscarContatoEmergencia(ContatoEmergencia comando) {
		this.idContatoEmergencia = comando.getIdContatoEmergencia();
		this.nomeContato = comando.getNomeContato();
		this.contatoPrincipal = comando.getContatoPrincipal();
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
	
	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

}
