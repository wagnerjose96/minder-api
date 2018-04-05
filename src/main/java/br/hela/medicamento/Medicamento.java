package br.hela.medicamento;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import br.hela.medicamento.comandos.CriarMedicamento;
import br.hela.medicamentoContinuo.MedicamentoContinuo;

@Entity
@Audited
public class Medicamento {
	
	@EmbeddedId
	@AttributeOverride(name="value", column=@Column(name="id_medicamento"))
	private MedicamentoId idMedicamento;
	private String nomeMedicamento;
	private String composicao;
	
	
	public Medicamento() {
		
	}
	
	public Medicamento(CriarMedicamento comando) {
		this.idMedicamento = new MedicamentoId();
		this.nomeMedicamento = comando.getNomeMedicamento();
		this.composicao = comando.getComposicao();			
		}

	public MedicamentoId getIdMedicamento() {
		return idMedicamento;
	}

	public void setIdMedicamento(MedicamentoId idMedicamento) {
		this.idMedicamento = idMedicamento;
	}

	public String getNomeMedicamento() {
		return nomeMedicamento;
	}

	public void setNomeMedicamento(String nomeMedicamento) {
		this.nomeMedicamento = nomeMedicamento;
	}

	public String getComposicao() {
		return composicao;
	}

	public void setComposicao(String composicao) {
		this.composicao = composicao;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idMedicamento == null) ? 0 : idMedicamento.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Medicamento other = (Medicamento) obj;
		if ( idMedicamento == null) {
			if (other.idMedicamento != null)
				return false;
		} else if (!idMedicamento.equals(other.idMedicamento))
			return false;
		return true;
	}

}
