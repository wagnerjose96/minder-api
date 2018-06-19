package br.hela.contato.comandos;

import br.hela.contato.ContatoId;
import br.hela.telefone.comandos.EditarTelefone;
import lombok.Data;

@Data
public class EditarContato {
	private ContatoId id;
	private String nome;
	private EditarTelefone telefone;
}
