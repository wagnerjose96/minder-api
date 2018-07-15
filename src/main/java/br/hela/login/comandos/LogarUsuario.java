package br.hela.login.comandos;

import lombok.Data;

@Data
public class LogarUsuario {
	private String identificador;
	private String senha;

	public LogarUsuario() {

	}
	
	public LogarUsuario(String identificador, String senha) {
		this.identificador = identificador;
		this.senha = senha;
	}

}
