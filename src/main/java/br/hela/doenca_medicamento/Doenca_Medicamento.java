package br.hela.doenca_medicamento;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;

import org.hibernate.envers.Audited;

import br.hela.BaseId;
import br.hela.doenca.DoencaId;
import br.hela.medicamento.MedicamentoId;

@Audited
public class Doenca_Medicamento {
	@EmbeddedId
	@AttributeOverride(name="value", column=@Column(name="id"))
	private Doenca_Medicamento_Id id;
	@EmbeddedId
	@AttributeOverride(name="value", column=@Column(name="id_doenca"))
	private DoencaId idDoenca;
	@EmbeddedId
	@AttributeOverride(name="value", column=@Column(name="id_medicamento"))
	private Doenca_Medicamento_Id idMedicamento;
	
	public Doenca_Medicamento() {
		super();
	}

	public Doenca_Medicamento_Id getId() {
		return id;
	}

	public void setId(Doenca_Medicamento_Id id) {
		this.id = id;
	}

	public DoencaId getIdDoenca() {
		return idDoenca;
	}

	public void setIdDoenca(DoencaId idDoenca) {
		this.idDoenca = idDoenca;
	}

	public Doenca_Medicamento_Id getIdMedicamento() {
		return idMedicamento;
	}

	public void setIdMedicamento(Doenca_Medicamento_Id idMedicamento) {
		this.idMedicamento = idMedicamento;
	}
		
	

}
