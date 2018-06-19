package br.hela.contato.comandos;

import br.hela.contato.Contato;
import br.hela.contato.ContatoId;
import br.hela.telefone.comandos.BuscarTelefone;
import lombok.Data;

@Data
public class BuscarContato {
	private ContatoId id;
	private String nome;
	private BuscarTelefone telefone;
	
	public BuscarContato() {
	}
	
	public BuscarContato(Contato comandos) {
		this.id = comandos.getId();
		this.nome = comandos.getNome();
	}
}
