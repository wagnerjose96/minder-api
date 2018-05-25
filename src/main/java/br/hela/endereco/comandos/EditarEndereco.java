package br.hela.endereco.comandos;

import br.hela.endereco.EnderecoId;
import lombok.Data;

@Data
public class EditarEndereco {
	private EnderecoId id;
	private String rua;
	private String bairro;
	private String cidade;
	private String estado;
	private String complemento;
	private int numero;
}
