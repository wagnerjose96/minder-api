package br.minder.alergia.alergia_medicamento;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import br.minder.alergia.AlergiaId;
import br.minder.medicamento.MedicamentoId;
import lombok.Data;

@Entity
@Data
public class AlergiaMedicamento {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private AlergiaMedicamentoId id;
	@AttributeOverride(name = "value", column = @Column(name = "id_alergia"))
	private AlergiaId idAlergia;
	@AttributeOverride(name = "value", column = @Column(name = "id_medicamento"))
	private MedicamentoId idMedicamento;

	public AlergiaMedicamento() {
		this.id = new AlergiaMedicamentoId();
	}
}
