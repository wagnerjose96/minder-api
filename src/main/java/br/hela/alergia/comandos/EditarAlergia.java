package br.hela.alergia.comandos;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModelProperty;

import br.hela.alergia.AlergiaId;

public class EditarAlergia {
	@ApiModelProperty(required = true)
	private AlergiaId id;
	@ApiModelProperty(required = true)
	private String tipo_alergia;
	@ApiModelProperty(required = true)
	private Date data_descoberta;
	@ApiModelProperty(required = true)
	private String medicamento;
	@ApiModelProperty(required = true)
	private String local_afetado;
	@ApiModelProperty(required = true)
	private String efeitos;

	public EditarAlergia() {
	}

	public AlergiaId getId() {
		return id;
	}

	public void setId(AlergiaId id) {
		this.id = id;
	}

	public String getTipo_alergia() {
		return tipo_alergia;
	}

	public void setTipo_alergia(String tipo_alergia) {
		this.tipo_alergia = tipo_alergia;
	}

	public Date getData_descoberta() {
		return data_descoberta;
	}

	public void setData_descoberta(Date data_descoberta) {
		this.data_descoberta = data_descoberta;
	}

	public String getMedicamento() {
		return medicamento;
	}

	public void setMedicamento(String medicamento) {
		this.medicamento = medicamento;
	}

	public String getLocal_afetado() {
		return local_afetado;
	}

	public void setLocal_afetado(String local_afetado) {
		this.local_afetado = local_afetado;
	}

	public String getEfeitos() {
		return efeitos;
	}

	public void setEfeitos(String efeitos) {
		this.efeitos = efeitos;
	}

}
