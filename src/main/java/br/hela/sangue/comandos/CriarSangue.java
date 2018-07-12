package br.hela.sangue.comandos;

import lombok.Data;

@Data
public class CriarSangue {
	private String sangue;

	public CriarSangue() {

	}

	public CriarSangue(String sangue) {
		this.sangue = sangue;
	}
}
