package br.hela.alergia;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import br.hela.alergia.comandos.CriarAlergia;

@Entity
public class Alergia {
	@EmbeddedId
	@AttributeOverride(name="value", column=@Column(name="idAlergia"))
	private AlergiaId idAlergia;
	@Column
	private String tipoAlergia;
	@Column
	private String localAfetado;
	@Column
	private Date dataDescoberta;
	@Column
	private String efeitos;
	@Column
	private String medicamento;
	
	public Alergia() {
		
	}

	public Alergia (CriarAlergia comandos) {
		this.idAlergia = new AlergiaId();
		this.tipoAlergia = comandos.getTipoAlergia();
		this.localAfetado = comandos.getLocalAfetado();
		this.dataDescoberta = comandos.getDataDescoberta();
		this.efeitos = comandos.getEfeitos();
		this.medicamento = comandos.getMedicamento();
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

	public String getMedicamento() {
		return medicamento;
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
