package br.hela.cirurgia.comandos;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModelProperty;

import br.hela.cirurgia.CirurgiaId;

public class EditarCirurgia {
	@ApiModelProperty(required = true)
	private CirurgiaId idCirurgia;
	@ApiModelProperty(required = true)
	private String tipoCirurgia;
	@ApiModelProperty(required = true)
	private Date dataCirurgia;
	@ApiModelProperty(required = true)
	private String clinicaResponsavel;
	@ApiModelProperty(required = true)
	private String medicoResponsavel;
	@ApiModelProperty(required = true)
	private String medicamentoConsumido;

	public EditarCirurgia() {
	}

	public CirurgiaId getIdCirurgia() {
		return idCirurgia;
	}

	public void setIdCirurgia(CirurgiaId idCirurgia) {
		this.idCirurgia = idCirurgia;
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

}
