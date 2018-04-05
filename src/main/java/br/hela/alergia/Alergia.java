package br.hela.alergia;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import br.hela.alergia.comandos.CriarAlergia;
import br.hela.alergia.comandos.EditarAlergia;
import br.hela.medicamento.MedicamentoId;

@Entity
@Audited
public class Alergia {
	@EmbeddedId
	@AttributeOverride(name="value", column=@Column(name="id"))
	private AlergiaId idAlergia;
	private String tipoAlergia;
	private String localAfetado;
	private Date dataDescoberta;
	private String efeitos;
	@AttributeOverride(name="value", column=@Column(name="id_medicamento"))
	private MedicamentoId idMedicamento;
	
	public Alergia() {
		
	}

	public Alergia (CriarAlergia comandos) {
		this.idAlergia = new AlergiaId();
		this.tipoAlergia = comandos.getTipoAlergia();
		this.localAfetado = comandos.getLocalAfetado();
		this.dataDescoberta = comandos.getDataDescoberta();
		this.efeitos = comandos.getEfeitos();
		this.idMedicamento = comandos.getIdMedicamento();
	}
	
	public void apply(EditarAlergia comando) {
		this.idAlergia = comando.getIdAlergia();
		this.tipoAlergia = comando.getTipoAlergia();
		this.localAfetado = comando.getLocalAfetado();
		this.dataDescoberta = comando.getDataDescoberta();
		this.efeitos = comando.getEfeitos();
		this.idMedicamento = comando.getMedicamento();
	}

	public AlergiaId getIdAlergia() {
		return idAlergia;
	}

	public String getTipoAlergia() {
		return tipoAlergia;
	}
	
	public String getLocalAfetado() {
		return localAfetado;
	}

	public Date getDataDescoberta() {
		return dataDescoberta;
	}
	
	public String getEfeitos() {
		return efeitos;
	}

	public MedicamentoId getMedicamento() {
		return idMedicamento;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idAlergia == null) ? 0 : idAlergia.hashCode());
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
		Alergia other = (Alergia) obj;
		if (idAlergia == null) {
			if (other.idAlergia != null)
				return false;
		} else if (!idAlergia.equals(other.idAlergia))
			return false;
		return true;
	}
}
