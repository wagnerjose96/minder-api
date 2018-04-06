package br.hela.cirurgia.comandos;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;


public class CriarCirurgia {
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
	
	public CriarCirurgia() {
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

