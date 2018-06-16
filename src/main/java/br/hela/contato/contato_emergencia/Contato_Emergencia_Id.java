package br.hela.contato.contato_emergencia;

import javax.persistence.Embeddable;
import br.hela.BaseId;

@Embeddable
public class Contato_Emergencia_Id extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public Contato_Emergencia_Id() {
		super();
	}

	public Contato_Emergencia_Id(String value) {
		super(value);
	}
}