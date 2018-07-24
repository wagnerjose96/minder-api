package br.minder.esqueci_senha.comandos;

import br.minder.usuario.UsuarioId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GerarSenha {
	private UsuarioId id;
	private String senha;
	private String email;
	private int ativo;
}
