package br.minder.endereco.comandos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarEndereco {
	private String rua;
	private String bairro;
	private String cidade;
	private String estado;
	private String complemento;
	private String numero;
	
	public CriarEndereco() {
		// default constructor
	}

}
