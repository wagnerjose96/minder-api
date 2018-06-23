package br.hela.usuario_adm.comandos;

import br.hela.usuario_adm.UsuarioAdm;
import br.hela.usuario_adm.UsuarioAdmId;
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
		 this.username = comandos.getNomeUsuario();
		 this.senha = comandos.getSenha();
	}
}
