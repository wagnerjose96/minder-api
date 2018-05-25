package br.hela.telefone.comandos;

import br.hela.telefone.TelefoneId;
import lombok.Data;

@Data
public class BuscarTelefone {
	private TelefoneId idTelefone;
	private int ddd;
	private int numero;
	private int ativo;
}
