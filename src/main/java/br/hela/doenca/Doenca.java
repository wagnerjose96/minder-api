package br.hela.doenca;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import com.wordnik.swagger.annotations.ApiModelProperty;

import br.hela.doenca.comandos.CriarDoenca;
import br.hela.doenca.comandos.EditarDoenca;

@Entity
@Audited
public class Doenca {
	@EmbeddedId
	@AttributeOverride(name="value", column=@Column(name="idDoenca"))
	@ApiModelProperty(required = true)
	private DoencaId idDoenca;
	@ApiModelProperty(required = true)
	private String nomeDoenca;
	@ApiModelProperty(required = true)
	private Date dataDescoberta;
	@ApiModelProperty(required = true)
	private String medicamento;
	
	public Doenca() {
	}

	public Doenca (CriarDoenca comandos) {
		this.idDoenca = new DoencaId();
		this.nomeDoenca = comandos.getNomeDoenca();
		this.dataDescoberta = comandos.getDataDescoberta();
		this.medicamento = comandos.getMedicamento();
	}
	
	public void apply(EditarDoenca comandos) {
		this.nomeDoenca = comandos.getNomeDoenca();
		this.dataDescoberta = comandos.getDataDescoberta();
		this.medicamento = comandos.getMedicamento();
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

	public String getMedicamento() {
		return medicamento;
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
