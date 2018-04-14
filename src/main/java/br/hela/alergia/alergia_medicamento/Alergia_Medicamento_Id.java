package br.hela.alergia.alergia_medicamento;

import javax.persistence.Embeddable;
import br.hela.BaseId;

@Embeddable
public class Alergia_Medicamento_Id extends BaseId {
	private static final long serialVersionUID = 1L;

	public Alergia_Medicamento_Id() {
		super();
	}

	public Alergia_Medicamento_Id(String value) {
		super(value);
	}
}
