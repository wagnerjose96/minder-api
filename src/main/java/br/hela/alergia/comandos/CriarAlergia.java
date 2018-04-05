package br.hela.alergia.comandos;

import java.util.Date;

import br.hela.medicamento.MedicamentoId;

public class CriarAlergia {
	private String tipoAlergia;
	private String localAfetado;
	private Date dataDescoberta;
	private String efeitos;
	private MedicamentoId idMedicamento;
	
	public CriarAlergia() {
	}

	public String getTipoAlergia() {
		return tipoAlergia;
	}

	public void setTipoAlergia(String tipoAlergia) {
		this.tipoAlergia = tipoAlergia;
	}
	
	public String getLocalAfetado() {
		return localAfetado;
	}

	public void setLocalAfetado(String localAfetado) {
		this.localAfetado = localAfetado;
	}

	public Date getDataDescoberta() {
		return dataDescoberta;
	}

	public void setDataDescoberta(Date dataDescoberta) {
		this.dataDescoberta = dataDescoberta;
	}
	
	public String getEfeitos() {
		return efeitos;
	}

	public void setEfeito(String efeitos) {
		this.efeitos = efeitos;
	}

	public MedicamentoId getIdMedicamento() {
		return idMedicamento;
	}

	public void setIdMedicamento(MedicamentoId medicamento) {
		this.idMedicamento = medicamento;
	}

	
}