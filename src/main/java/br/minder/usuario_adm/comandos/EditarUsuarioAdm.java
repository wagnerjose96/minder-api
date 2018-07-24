package br.minder.usuario_adm.comandos;

import br.minder.usuario_adm.UsuarioAdmId;
import lombok.Data;

@Data
public class EditarUsuarioAdm {
	private UsuarioAdmId id;
	private String nome;
	private String senha;

}
