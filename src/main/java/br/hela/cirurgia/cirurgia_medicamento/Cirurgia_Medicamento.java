package br.hela.cirurgia.cirurgia_medicamento;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import br.hela.cirurgia.CirurgiaId;
import br.hela.medicamento.MedicamentoId;

@Entity
public class Cirurgia_Medicamento {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private Cirurgia_Medicamento_Id id;
	@AttributeOverride(name = "value", column = @Column(name = "id_cirurgia"))
	private CirurgiaId idCirurgia;
	@AttributeOverride(name = "value", column = @Column(name = "id_medicamento"))
	private MedicamentoId idMedicamento;

	public Cirurgia_Medicamento() {
		this.id = new Cirurgia_Medicamento_Id();
	}

	public Cirurgia_Medicamento_Id getId() {
		return id;
	}

	public CirurgiaId getIdCirurgia() {
		return idCirurgia;
	}

	public void setIdCirurgia(CirurgiaId idCirurgia) {
		this.idCirurgia = idCirurgia;
	}

	public MedicamentoId getIdMedicamento() {
		return idMedicamento;
	}

	public void setIdMedicamento(MedicamentoId idMedicamento) {
		this.idMedicamento = idMedicamento;
	}
}
