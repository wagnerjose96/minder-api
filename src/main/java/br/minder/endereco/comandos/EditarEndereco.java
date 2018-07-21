package br.minder.endereco.comandos;

import br.minder.endereco.EnderecoId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditarEndereco {
	private EnderecoId id;
	private String rua;
	private String bairro;
	private String cidade;
	private String estado;
	private String complemento;
	private String numero;
	
	public EditarEndereco() {
		// default constructor
	}
}
