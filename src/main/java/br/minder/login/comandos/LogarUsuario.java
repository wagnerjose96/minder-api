package br.minder.login.comandos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogarUsuario {
	private String identificador;
	private String senha;

	public LogarUsuario() {
		// default constructor
	}

}
