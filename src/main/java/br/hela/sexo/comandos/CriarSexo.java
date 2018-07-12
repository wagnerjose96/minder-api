package br.hela.sexo.comandos;

import lombok.Data;

@Data
public class CriarSexo {
	private String sexo;

	public CriarSexo() {

	}

	public CriarSexo(String sexo) {
		this.sexo = sexo;
	}
}
