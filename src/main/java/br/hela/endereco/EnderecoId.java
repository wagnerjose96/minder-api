package br.hela.endereco;

import javax.persistence.Embeddable;
import br.hela.BaseId;

@Embeddable
public class EnderecoId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public EnderecoId() {
		super();
	}

	public EnderecoId(String value) {
		super(value);
	}
}
