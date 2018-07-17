package br.minder.contato.contato_emergencia;

import javax.persistence.Embeddable;

import br.minder.base_id.BaseId;

@Embeddable
public class ContatoEmergenciaId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public ContatoEmergenciaId() {
		super();
	}

	public ContatoEmergenciaId(String value) {
		super(value);
	}
}