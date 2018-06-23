package br.hela.usuario_adm.comandos;

import br.hela.usuario_adm.UsuarioAdmId;
import lombok.Data;

@Data
public class EditarUsuarioAdm {
	private UsuarioAdmId id;
	private String username;
	private String senha;

}
