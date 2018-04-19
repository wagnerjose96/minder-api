package br.hela.cirurgia.cirurgia_medicamento;

import javax.persistence.Embeddable;
import br.hela.BaseId;

@Embeddable
public class Cirurgia_Medicamento_Id extends BaseId {
	private static final long serialVersionUID = 1L;

	public Cirurgia_Medicamento_Id() {
		super();
	}

	public Cirurgia_Medicamento_Id(String value) {
		super(value);
	}
}
