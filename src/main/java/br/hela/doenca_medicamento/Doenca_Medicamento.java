package br.hela.doenca_medicamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.FetchType;

import org.hibernate.envers.Audited;

import br.hela.doenca.DoencaId;
import br.hela.medicamento.MedicamentoId;


@Audited
public class Doenca_Medicamento {
	@EmbeddedId
	@AttributeOverride(name="value", column=@Column(name="id"))
	private Doenca_Medicamento_Id id;
	@AttributeOverride(name="value", column=@Column(name="id_doenca"))
	private DoencaId idDoenca;
	@AttributeOverride(name="value", column=@Column(name="id_medicamento"))
	@ElementCollection(fetch=FetchType.EAGER)
	private List<MedicamentoId> idMedicamento = new ArrayList<MedicamentoId>();
	
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

	public List<MedicamentoId> getIdMedicamento() {
		return idMedicamento;
	}

	public void setIdMedicamento(List<MedicamentoId> idMedicamento) {
		this.idMedicamento = idMedicamento;
	}
		
	

}
