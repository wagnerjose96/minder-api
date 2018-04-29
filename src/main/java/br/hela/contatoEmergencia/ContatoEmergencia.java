package br.hela.contatoEmergencia;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;

import br.hela.contatoEmergencia.comandos.CriarContatoEmergencia;

@Entity
@Audited
public class ContatoEmergencia {
	@EmbeddedId
	@AttributeOverride(name="value", column=@Column(name="id"))
	private ContatoEmergenciaId idContatoEmergencia;
	private String nomeContato;
	
	public ContatoEmergencia() {
	}

	public ContatoEmergencia(CriarContatoEmergencia comando) {
		this.idContatoEmergencia = new ContatoEmergenciaId();
		this.nomeContato = comando.getNomeContato();
	}

	public ContatoEmergenciaId getIdContatoEmergencia() {
		return idContatoEmergencia;
	}

	public String getNomeContato() {
		return nomeContato;
	}

	public void setNomeContato(String nomeContato) {
		this.nomeContato = nomeContato;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idContatoEmergencia == null) ? 0 : idContatoEmergencia.hashCode());
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
		ContatoEmergencia other = (ContatoEmergencia) obj;
		if (idContatoEmergencia == null) {
			if (other.idContatoEmergencia != null)
				return false;
		} else if (!idContatoEmergencia.equals(other.idContatoEmergencia))
			return false;
		return true;
	}

}
