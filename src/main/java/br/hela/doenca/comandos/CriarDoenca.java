package br.hela.doenca.comandos;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;


public class CriarDoenca {
	@ApiModelProperty(required=true)
	private String nomeDoenca;
	@ApiModelProperty(required=true)
	private Date dataDescoberta;
	@ApiModelProperty(required=true)
	private String medicamento;
	
	public CriarDoenca() {
	}

	public String getNomeDoenca() {
		return nomeDoenca;
	}

	public void setNomeDoenca(String nomeDoenca) {
		this.nomeDoenca = nomeDoenca;
	}

	public Date getDataDescoberta() {
		return dataDescoberta;
	}

	public void setDataDescoberta(Date dataDescoberta) {
		this.dataDescoberta = dataDescoberta;
	}

	public String getMedicamento() {
		return medicamento;
	}

	public void setMedicamento(String medicamento) {
		this.medicamento = medicamento;
	}

	
}
