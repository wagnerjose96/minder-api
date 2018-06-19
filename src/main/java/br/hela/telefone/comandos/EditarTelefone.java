package br.hela.telefone.comandos;

import br.hela.telefone.TelefoneId;
import lombok.Data;

@Data
public class EditarTelefone {
	private TelefoneId id;
	private int ddd;
	private int numero;
}
