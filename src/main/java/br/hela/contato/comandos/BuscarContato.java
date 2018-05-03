package br.hela.contato.comandos;

import br.hela.contato.Contato;
import br.hela.contato.ContatoId;
import br.hela.telefone.Telefone;

public class BuscarContato {
	private ContatoId id;
	private String nome;
	private Telefone telefone;

	public BuscarContato() {
	}

	public BuscarContato(Contato comando) {
		this.id = comando.getId();
		this.nome = comando.getNome();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public ContatoId getId() {
		return id;
	}

}
