package br.minder.alergia.alergia_medicamento;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import br.minder.alergia.AlergiaId;
import br.minder.medicamento.MedicamentoId;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

	public AlergiaMedicamento(AlergiaMedicamento alergia) {
		this.id = alergia.getId();
		this.idAlergia = alergia.getIdAlergia();
		this.idMedicamento = alergia.getIdMedicamento();
	}

}
