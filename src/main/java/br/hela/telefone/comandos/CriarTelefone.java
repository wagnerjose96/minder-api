package br.hela.telefone.comandos;

import br.hela.telefone.Telefone;

public class CriarTelefone {
	private int ddd;
	private int numero;
	
	public CriarTelefone() {
	
	}
	
	public CriarTelefone(Telefone telefone) {
		this.ddd = telefone.getDdd();
		this.numero = telefone.getNumero();
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
