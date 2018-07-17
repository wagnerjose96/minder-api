package br.minder.contato.comandos;

import br.minder.telefone.comandos.CriarTelefone;
import lombok.Data;

@Data
public class CriarContato {
	private String nome;
	private CriarTelefone telefone;
}
