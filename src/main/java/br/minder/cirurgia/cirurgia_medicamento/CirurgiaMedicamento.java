package br.minder.cirurgia.cirurgia_medicamento;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import br.minder.cirurgia.CirurgiaId;
import br.minder.medicamento.MedicamentoId;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CirurgiaMedicamento {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private CirurgiaMedicamentoId id;
	@AttributeOverride(name = "value", column = @Column(name = "id_cirurgia"))
	private CirurgiaId idCirurgia;
	@AttributeOverride(name = "value", column = @Column(name = "id_medicamento"))
	private MedicamentoId idMedicamento;

	public CirurgiaMedicamento() {
		this.id = new CirurgiaMedicamentoId();
	}
}
