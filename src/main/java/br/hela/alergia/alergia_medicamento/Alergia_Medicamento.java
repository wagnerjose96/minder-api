package br.hela.alergia.alergia_medicamento;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import br.hela.alergia.AlergiaId;
import br.hela.medicamento.MedicamentoId;

@Entity
public class Alergia_Medicamento {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private Alergia_Medicamento_Id id;
	@AttributeOverride(name = "value", column = @Column(name = "id_alergia"))
	private AlergiaId idAlergia;
	@AttributeOverride(name = "value", column = @Column(name = "id_medicamento"))
	private MedicamentoId idMedicamento;

	public Alergia_Medicamento() {
		this.id = new Alergia_Medicamento_Id();
	}
	/*
	public Alergia_Medicamento(MedicamentoId medicamentos, AlergiaId idAlergia) {
		this.id = new Alergia_Medicamento_Id();
		this.idAlergia = idAlergia;
		this.idMedicamento = medicamentos;
	}*/

	public Alergia_Medicamento_Id getId() {
		return id;
	}

	public AlergiaId getIdAlergia() {
		return idAlergia;
	}

	public void setIdAlergia(AlergiaId idAlergia) {
		this.idAlergia = idAlergia;
	}

	public MedicamentoId getIdMedicamento() {
		return idMedicamento;
	}

	public void setIdMedicamento(MedicamentoId idMedicamento) {
		this.idMedicamento = idMedicamento;
	}
}
