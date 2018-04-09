package br.hela.alergia.comandos;

import java.util.Date;

import br.hela.alergia.AlergiaId;
import br.hela.medicamento.MedicamentoId;

public class EditarAlergia {
	private AlergiaId idAlergia;
	private String tipoAlergia;
	private String localAfetado;
	private Date dataDescoberta;
	private String efeitos;
	private MedicamentoId medicamento;

	public AlergiaId getIdAlergia() {
		return idAlergia;
	}

	public String getTipoAlergia() {
		return tipoAlergia;
	}

	public void setIdAlergia(AlergiaId idAlergia) {
		this.idAlergia = idAlergia;
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

	public void setEfeitos(String efeitos) {
		this.efeitos = efeitos;
	}

	public MedicamentoId getIdMedicamento() {
		return medicamento;
	}

	public void setMedicamento(MedicamentoId medicamento) {
		this.medicamento = medicamento;
	}

}
