package br.hela.emergencia.emergencia_usuario;

import javax.persistence.Embeddable;
import br.hela.BaseId;

@Embeddable
public class Emergencia_Usuario_Id extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public Emergencia_Usuario_Id() {
		super();
	}

	public Emergencia_Usuario_Id(String value) {
		super(value);
	}
}