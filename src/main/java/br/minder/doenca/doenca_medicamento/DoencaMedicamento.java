package br.minder.doenca.doenca_medicamento;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import br.minder.doenca.DoencaId;
import br.minder.medicamento.MedicamentoId;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DoencaMedicamento {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private DoencaMedicamentoId id;
	@AttributeOverride(name = "value", column = @Column(name = "id_doenca"))
	private DoencaId idDoenca;
	@AttributeOverride(name = "value", column = @Column(name = "id_medicamento"))
	private MedicamentoId idMedicamento;

	public DoencaMedicamento() {
		this.id = new DoencaMedicamentoId();
	}
}