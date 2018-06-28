package br.hela.cirurgia.cirurgia_medicamento;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import br.hela.cirurgia.CirurgiaId;
import br.hela.medicamento.MedicamentoId;
import lombok.Data;

@Entity
@Data
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
