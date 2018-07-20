package br.minder.telefone.comandos;

import br.minder.telefone.TelefoneId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditarTelefone {
	private TelefoneId id;
	private int ddd;
	private int numero;
}
