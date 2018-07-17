package br.minder.telefone.comandos;

import br.minder.telefone.Telefone;
import br.minder.telefone.TelefoneId;
import lombok.Data;

@Data
public class BuscarTelefone {
	private TelefoneId id;
	private int ddd;
	private int numero;

	public BuscarTelefone(Telefone telefone) {
		this.id = telefone.getId();
		this.ddd = telefone.getDdd();
		this.numero = telefone.getNumero();
	}

	public BuscarTelefone() {
	}
}
