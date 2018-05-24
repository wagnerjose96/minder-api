package br.hela.contato.comandos;

import br.hela.contato.ContatoId;
import br.hela.telefone.comandos.EditarTelefone;

public class EditarContato {
	private ContatoId id;
	private String nome;
	private EditarTelefone telefone;

	public EditarContato() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public EditarTelefone getTelefone() {
		return telefone;
	}

	public void setTelefone(EditarTelefone telefone) {
		this.telefone = telefone;
	}

	public ContatoId getId() {
		return id;
	}

}
