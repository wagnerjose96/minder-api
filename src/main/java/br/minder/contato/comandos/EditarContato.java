package br.minder.contato.comandos;

import br.minder.contato.ContatoId;
import br.minder.telefone.comandos.EditarTelefone;
import lombok.Data;

@Data
public class EditarContato {
	private ContatoId id;
	private String nome;
	private EditarTelefone telefone;
}
