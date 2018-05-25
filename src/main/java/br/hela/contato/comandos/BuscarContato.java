package br.hela.contato.comandos;

import br.hela.contato.Contato;
import br.hela.contato.ContatoId;
import br.hela.telefone.Telefone;
import lombok.Data;

@Data
public class BuscarContato {
	private ContatoId id;
	private String nome;
	private Telefone telefone;

	public BuscarContato(Contato comando) {
		this.id = comando.getId();
		this.nome = comando.getNome();
	}
}
