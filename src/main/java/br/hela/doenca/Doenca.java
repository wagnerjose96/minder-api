package br.hela.doenca;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import br.hela.doenca.comandos.CriarDoenca;
import br.hela.doenca_medicamento.Doenca_Medicamento;
import br.hela.doenca_medicamento.Doenca_Medicamento_Id;
import br.hela.medicamento.MedicamentoId;

@Entity
@Audited
public class Doenca {
	@EmbeddedId
	@AttributeOverride(name="value", column=@Column(name="id_doenca"))
	private DoencaId idDoenca;
	private String nomeDoenca;
	private Date dataDescoberta;
	@AttributeOverride(name="value", column=@Column(name="id_medicamento"))
	private MedicamentoId idMedicamento;
	
	public Doenca() {
		
	}

	public Doenca (CriarDoenca comandos) {
		this.idDoenca = new DoencaId();
		this.nomeDoenca = comandos.getNomeDoenca();
		this.dataDescoberta = comandos.getDataDescoberta();
		this.idMedicamento = comandos.getIdMedicamento();
	}

	public DoencaId getIdDoenca() {
		return idDoenca;
	}

	public String getNomeDoenca() {
		return nomeDoenca;
	}

	public Date getDataDescoberta() {
		return dataDescoberta;
	}

	public MedicamentoId getMedicamento() {
		return idMedicamento;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idDoenca == null) ? 0 : idDoenca.hashCode());
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
		Doenca other = (Doenca) obj;
		if (idDoenca == null) {
			if (other.idDoenca != null)
				return false;
		} else if (!idDoenca.equals(other.idDoenca))
			return false;
		return true;
	}
}
