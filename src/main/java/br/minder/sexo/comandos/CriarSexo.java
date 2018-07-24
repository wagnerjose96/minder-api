package br.minder.sexo.comandos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarSexo {
	private String genero;

	public CriarSexo() {

	}

	public CriarSexo(String sexo) {
		this.genero = sexo;
	}
}
