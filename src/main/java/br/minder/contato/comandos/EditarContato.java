package br.minder.contato.comandos;

import br.minder.contato.ContatoId;
import br.minder.telefone.comandos.EditarTelefone;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditarContato {
	private ContatoId id;
	private String nome;
	private EditarTelefone telefone;
}
