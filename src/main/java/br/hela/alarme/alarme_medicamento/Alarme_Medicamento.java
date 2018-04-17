package br.hela.alarme.alarme_medicamento;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import br.hela.alarme.AlarmeId;
import br.hela.medicamento.MedicamentoId;

@Entity
public class Alarme_Medicamento {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private Alarme_Medicamento_Id id;
	@AttributeOverride(name = "value", column = @Column(name = "id_alarme"))
	private AlarmeId idAlarme;
	@AttributeOverride(name = "value", column = @Column(name = "id_medicamento"))
	private MedicamentoId idMedicamento;

	public Alarme_Medicamento() {
		this.id = new Alarme_Medicamento_Id();
	}

	public Alarme_Medicamento_Id getId() {
		return id;
	}

	public AlarmeId getIdAlarme() {
		return idAlarme;
	}

	public void setIdAlarme(AlarmeId idAlarme) {
		this.idAlarme = idAlarme;
	}

	public MedicamentoId getIdMedicamento() {
		return idMedicamento;
	}

	public void setIdMedicamento(MedicamentoId idMedicamento) {
		this.idMedicamento = idMedicamento;
	}
}
