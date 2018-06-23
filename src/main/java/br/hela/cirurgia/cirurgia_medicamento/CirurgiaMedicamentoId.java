package br.hela.cirurgia.cirurgia_medicamento;

import javax.persistence.Embeddable;
import br.hela.BaseId;

@Embeddable
public class CirurgiaMedicamentoId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public CirurgiaMedicamentoId() {
		super();
	}

	public CirurgiaMedicamentoId(String value) {
		super(value);
	}
}
