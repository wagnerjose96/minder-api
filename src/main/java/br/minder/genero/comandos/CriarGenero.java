package br.minder.genero.comandos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarGenero {
	private String genero;

	public CriarGenero() {

	}

	public CriarGenero(String sexo) {
		this.genero = sexo;
	}
}
