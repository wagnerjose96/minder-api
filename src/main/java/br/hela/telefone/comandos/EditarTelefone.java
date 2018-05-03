package br.hela.telefone.comandos;

import br.hela.telefone.TelefoneId;

public class EditarTelefone {
	private TelefoneId idTelefone;
	private int ddd;
	private int numero;

	public EditarTelefone() {
	}

	public TelefoneId getIdTelefone() {
		return idTelefone;
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
