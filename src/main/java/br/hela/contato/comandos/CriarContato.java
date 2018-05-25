package br.hela.contato.comandos;

import br.hela.telefone.comandos.CriarTelefone;
import lombok.Data;

@Data
public class CriarContato {
	private String nome;
	private CriarTelefone telefone;
}
