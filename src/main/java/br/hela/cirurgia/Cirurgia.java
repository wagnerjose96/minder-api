package br.hela.cirurgia;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import br.hela.cirurgia.comandos.CriarCirurgia;
import br.hela.cirurgia.comandos.EditarCirurgia;
import br.hela.medicamento.MedicamentoId;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class Cirurgia {
	@EmbeddedId
	@AttributeOverride(name="value", column=@Column(name="id_cirurgia"))
	private CirurgiaId idCirurgia;
	@AttributeOverride(name="value", column=@Column(name="id_medicamento"))
	private MedicamentoId idMedicamento;
	private String tipoCirurgia;
	private Date dataCirurgia;
	private String clinicaResponsavel;
	private String medicoResponsavel;
	

	public Cirurgia() {
	}

	public Cirurgia(CriarCirurgia comando) {
		this.idCirurgia = new CirurgiaId();
		this.idMedicamento = new MedicamentoId();
		this.tipoCirurgia = comando.getTipoCirurgia();
		this.dataCirurgia = comando.getDataCirurgia();
		this.clinicaResponsavel = comando.getClinicaResponsavel();
		this.medicoResponsavel = comando.getMedicoResponsavel();

	}

	public void apply(EditarCirurgia comando) {
		this.idCirurgia = comando.getIdCirurgia();
		this.tipoCirurgia = comando.getTipoCirurgia();
		this.dataCirurgia = comando.getDataCirurgia();
		this.clinicaResponsavel = comando.getClinicaResponsavel();
		this.medicoResponsavel = comando.getMedicoResponsavel();
		this.idMedicamento = comando.getIdMedicamento();
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
	
	public MedicamentoId getMedicamentoConsumido() {
		return idMedicamento;
	}

	public void setMedicamentoConsumido(MedicamentoId medicamentoConsumido) {
		this.idMedicamento = medicamentoConsumido;
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
