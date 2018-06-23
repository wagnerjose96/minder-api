package br.hela.alergia.alergia_medicamento;

import javax.persistence.Embeddable;
import br.hela.BaseId;

@Embeddable
public class AlergiaMedicamentoId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public AlergiaMedicamentoId() {
		super();
	}

	public AlergiaMedicamentoId(String value) {
		super(value);
	}
}
