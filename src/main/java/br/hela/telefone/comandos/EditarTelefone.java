package br.hela.telefone.comandos;

import br.hela.telefone.Telefone;
import br.hela.telefone.TelefoneId;

public class EditarTelefone {
	private TelefoneId telefoneId;
	private int ddd;
	private int numero;

	public EditarTelefone() {

	}

	public EditarTelefone(Telefone comandos) {
		this.telefoneId = comandos.getTelefoneId();
		this.ddd = comandos.getDdd();
		this.numero = comandos.getNumero();
	}

	public TelefoneId getTelefoneId() {
		return telefoneId;
	}

	public int getDdd() {
		return ddd;
	}

	public void setDdd(int ddd) {
		this.ddd = ddd;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

}
