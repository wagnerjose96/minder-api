package br.minder.alergia.alergia_medicamento;

import javax.persistence.Embeddable;

import br.minder.base_id.BaseId;

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
