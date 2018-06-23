package br.hela.usuarioAdm.comandos;

import br.hela.usuarioAdm.UsuarioAdmId;
import lombok.Data;

@Data
public class EditarUsuarioAdm {
	private UsuarioAdmId id;
	private String username;
	private String senha;

}
