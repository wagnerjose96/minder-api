package br.minder.login.comandos;

import br.minder.usuario.UsuarioId;
import lombok.Data;

@Data
public class IdentificarUsuario {
	private UsuarioId id;
	private String identificador;
	private String senha;
}