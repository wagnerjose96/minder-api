package br.minder.sexo.comandos;

import lombok.Data;

@Data
public class CriarSexo {
	private String genero;

	public CriarSexo() {

	}

	public CriarSexo(String sexo) {
		this.genero = sexo;
	}
}
