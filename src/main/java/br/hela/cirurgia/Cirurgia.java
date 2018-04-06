package br.hela.cirurgia;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import br.hela.cirurgia.comandos.CriarCirurgia;
import br.hela.cirurgia.comandos.EditarCirurgia;
import io.swagger.annotations.ApiModelProperty;

import org.hibernate.envers.Audited;


@Entity
@Audited
public class Cirurgia {
	@EmbeddedId
	@AttributeOverride(name="value", column=@Column(name="id"))
	@ApiModelProperty(required=true)
	private CirurgiaId idCirurgia;
	@ApiModelProperty(required=true)
	private String tipoCirurgia;
	@ApiModelProperty(required=true)
	private Date dataCirurgia;
	@ApiModelProperty(required=true)
	private String clinicaResponsavel;
	@ApiModelProperty(required=true)
	private String medicoResponsavel;
	@ApiModelProperty(required=true)
	private String medicamentoConsumido;

	public Cirurgia() {
	}

	public Cirurgia(CriarCirurgia comando) {
		this.idCirurgia = new CirurgiaId();
		this.tipoCirurgia = comando.getTipoCirurgia();
		this.dataCirurgia = comando.getDataCirurgia();
		this.clinicaResponsavel = comando.getClinicaResponsavel();
		this.medicoResponsavel = comando.getMedicoResponsavel();
		this.medicamentoConsumido = comando.getMedicamentoConsumido();
	}
	
	public void apply(EditarCirurgia comando) {
		this.tipoCirurgia = comando.getTipoCirurgia();
		this.dataCirurgia = comando.getDataCirurgia();
		this.clinicaResponsavel = comando.getClinicaResponsavel();
		this.medicoResponsavel = comando.getMedicoResponsavel();
		this.medicamentoConsumido = comando.getMedicamentoConsumido();
	}

	public CirurgiaId getIdCirurgia() {
		return idCirurgia;
	}

	public String getTipoCirurgia() {
		return tipoCirurgia;
	}

	public void setTipoCirurgia(String tipoCirurgia) {
		this.tipoCirurgia = tipoCirurgia;
	}
	
	public Date getDataCirurgia() {
		return dataCirurgia;
	}

	public void setDataCirurgia(Date dataCirurgia) {
		this.dataCirurgia = dataCirurgia;
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
	
	public String getMedicamentoConsumido() {
		return medicamentoConsumido;
	}

	public void setMedicamentoConsumido(String medicamentoConsumido) {
		this.medicamentoConsumido = medicamentoConsumido;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idCirurgia == null) ? 0 : idCirurgia.hashCode());
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
		if (idCirurgia == null) {
			if (other.idCirurgia != null)
				return false;
		} else if (!idCirurgia.equals(other.idCirurgia))
			return false;
		return true;
	}

}
