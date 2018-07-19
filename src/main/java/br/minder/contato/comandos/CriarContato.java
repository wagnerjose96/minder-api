package br.minder.contato.comandos;

import br.minder.telefone.comandos.CriarTelefone;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarContato {
	private String nome;
	private CriarTelefone telefone;
}
