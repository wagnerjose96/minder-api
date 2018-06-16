package br.hela.contato.comandos;

import br.hela.contato.Contato;
import br.hela.contato.ContatoId;
import lombok.Data;

@Data
public class BuscarContato {
	private ContatoId id;
	private String nome;
	private int ddd;
	private int numero;
	
	public BuscarContato() {
	}
	
	public BuscarContato(Contato comandos) {
		this.id = comandos.getId();
		this.nome = comandos.getNome();
		this.ddd = comandos.getDdd();
		this.numero = comandos.getNumero();
	}
}
