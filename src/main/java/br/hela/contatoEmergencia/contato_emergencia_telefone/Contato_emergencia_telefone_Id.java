package br.hela.contatoEmergencia.contato_emergencia_telefone;

import javax.persistence.Embeddable;
import br.hela.BaseId;

@Embeddable
public class Contato_emergencia_telefone_Id extends BaseId {
	private static final long serialVersionUID = 1L;

	public Contato_emergencia_telefone_Id() {
		super();
	}

	public Contato_emergencia_telefone_Id(String value) {
		super(value);
	}
}
