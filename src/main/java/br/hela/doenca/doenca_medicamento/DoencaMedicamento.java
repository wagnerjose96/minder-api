package br.hela.doenca.doenca_medicamento;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import br.hela.doenca.DoencaId;
import br.hela.medicamento.MedicamentoId;
import lombok.Data;

@Entity
@Data
public class Doenca_Medicamento {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private Doenca_Medicamento_Id id;
	@AttributeOverride(name = "value", column = @Column(name = "id_doenca"))
	private DoencaId idDoenca;
	@AttributeOverride(name = "value", column = @Column(name = "id_medicamento"))
	private MedicamentoId idMedicamento;

	public Doenca_Medicamento() {
		this.id = new Doenca_Medicamento_Id();
	}
}