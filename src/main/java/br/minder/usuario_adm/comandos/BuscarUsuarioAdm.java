package br.minder.usuario_adm.comandos;

import br.minder.usuario_adm.UsuarioAdm;
import br.minder.usuario_adm.UsuarioAdmId;
import lombok.Getter;

@Getter
public class BuscarUsuarioAdm {
	private UsuarioAdmId id;
	private String nome;
	private String senha;

	public BuscarUsuarioAdm(UsuarioAdm comandos) {
		 this.id = comandos.getId();
		 this.nome = comandos.getNomeUsuario();
		 this.senha = comandos.getSenha();
	}
}
