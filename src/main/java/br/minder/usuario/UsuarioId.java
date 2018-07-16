package br.minder.usuario;

import javax.persistence.Embeddable;

import br.minder.base_id.BaseId;

@Embeddable
public class UsuarioId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public UsuarioId() {
		super();
	}

	public UsuarioId(String value) {
		super(value);
	}

}
