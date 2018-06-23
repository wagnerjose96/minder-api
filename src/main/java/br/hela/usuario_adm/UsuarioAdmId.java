package br.hela.usuario_adm;

import javax.persistence.Embeddable;

import br.hela.BaseId;

@Embeddable
public class UsuarioAdmId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public UsuarioAdmId() {
		super();
	}

	public UsuarioAdmId(String value) {
		super(value);
	}

}

