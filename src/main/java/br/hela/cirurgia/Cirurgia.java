package br.hela.cirurgia;

import java.sql.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import br.hela.cirurgia.comandos.CriarCirurgia;

@Entity
public class Cirurgia {
	@EmbeddedId
	@AttributeOverride(name="value", column=@Column(name="id"))
	private CirurgiaId id;
	private String tipoCirurgia;
	private Date data;
	private String clinicaResponsavel;
	private String medicoResponsavel;

	public Cirurgia() {
	}

	public Cirurgia(CriarCirurgia comando) {
		this.id = new CirurgiaId();
		this.setTipoCirurgia(comando.getTipoCirurgia());
		this.data = comando.getData();
		this.clinicaResponsavel = comando.getClinicaResponsavel();
		this.medicoResponsavel = comando.getMedicoResponsavel();
	}

	public CirurgiaId getId() {
		return id;
	}

	public String getTipoCirurgia() {
		return tipoCirurgia;
	}

	public void setTipoCirurgia(String tipoCirurgia) {
		this.tipoCirurgia = tipoCirurgia;
	}
	
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	public String getClinicaResponsavel() {
		return clinicaResponsavel;
	}

	public void setClinicaResponsavel(String clinicaResponsavel) {
		this.clinicaResponsavel = clinicaResponsavel;
	}
	
	public String getMedicoResponsavel() {
		return medicoResponsavel;
	}

	public void setMedicoResponsavel(String medicoResponsavel) {
		this.medicoResponsavel = medicoResponsavel;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Cirurgia other = (Cirurgia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
