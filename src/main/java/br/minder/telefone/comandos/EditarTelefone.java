package br.minder.telefone.comandos;

import br.minder.telefone.TelefoneId;
import lombok.Data;

@Data
public class EditarTelefone {
	private TelefoneId id;
	private int ddd;
	private int numero;
}
