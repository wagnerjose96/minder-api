package br.minder.usuario_adm.comandos;

import br.minder.usuario_adm.UsuarioAdmId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditarUsuarioAdm {
	private UsuarioAdmId id;
	private String nome;
	private String senha;

}
