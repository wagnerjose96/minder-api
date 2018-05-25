package br.hela.endereco.comandos;

import lombok.Data;

@Data
public class CriarEndereco {
	private String rua;
	private String bairro;
	private String cidade;
	private String estado;
	private String complemento;
	private int numero;
}
