package br.hela.contato.contato_telefone;

import javax.persistence.Embeddable;
import br.hela.BaseId;

@Embeddable
public class Contato_Telefone_Id extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public Contato_Telefone_Id() {
		super();
	}

	public Contato_Telefone_Id(String value) {
		super(value);
	}
}
