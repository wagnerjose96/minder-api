package br.hela.contato.comandos;

import br.hela.contato.ContatoId;
import lombok.Data;

@Data
public class EditarContato {
	private ContatoId id;
	private String nome;
	private int ddd;
	private int numero;
}
