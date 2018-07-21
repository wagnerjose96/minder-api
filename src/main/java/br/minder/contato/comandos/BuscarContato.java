package br.minder.contato.comandos;

import br.minder.contato.Contato;
import br.minder.contato.ContatoId;
import br.minder.telefone.comandos.BuscarTelefone;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BuscarContato {
	private ContatoId id;
	private String nome;
	private BuscarTelefone telefone;
	
	public BuscarContato(Contato comandos) {
		this.id = comandos.getId();
		this.nome = comandos.getNome();
	}

	public BuscarContato() {
	}
}
