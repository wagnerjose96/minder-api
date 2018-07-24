package br.minder.sangue.comandos;

import lombok.Data;

@Data
public class CriarSangue {
	private String tipoSanguineo;

	public CriarSangue() {

	}

	public CriarSangue(String sangue) {
		this.tipoSanguineo = sangue;
	}
}
