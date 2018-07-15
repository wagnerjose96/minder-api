package br.hela.login.comandos;

import br.hela.usuario.UsuarioId;
import lombok.Data;

@Data
public class IdentificarUsuario {
	private UsuarioId id;
	private String identificador;
	private String senha;
}