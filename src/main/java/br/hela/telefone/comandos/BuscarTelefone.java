package br.hela.telefone.comandos;

import br.hela.telefone.Telefone;
import br.hela.telefone.TelefoneId;
import lombok.Data;

@Data
public class BuscarTelefone {
	private TelefoneId id;
	private int ddd;
	private int numero;

	public BuscarTelefone(Telefone plano) {
		this.id = plano.getId();
		this.ddd = plano.getDdd();
		this.numero = plano.getNumero();
	}

	public BuscarTelefone() {
	}
}
