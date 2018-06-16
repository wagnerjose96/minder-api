package br.hela.usuarioAdm.comandos;

import br.hela.usuarioAdm.UsuarioAdm;
import br.hela.usuarioAdm.UsuarioAdmId;
import lombok.Data;

@Data
public class BuscarUsuarioAdm {
	private UsuarioAdmId id;
	private String username;
	private String senha;

	public BuscarUsuarioAdm() {
	}

	public BuscarUsuarioAdm(UsuarioAdm comandos) {
		 this.id = comandos.getId();
		 this.username = comandos.getNome_usuario();
		 this.senha = comandos.getSenha();
	}
}
