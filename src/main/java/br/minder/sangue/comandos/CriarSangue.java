package br.minder.sangue.comandos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarSangue {
	private String tipoSanguineo;

	public CriarSangue() {

	}

	public CriarSangue(String sangue) {
		this.tipoSanguineo = sangue;
	}
}
