package br.hela.alergia.comandos;

import java.util.Date;
import br.hela.alergia.AlergiaId;

public class EditarAlergia {
	private AlergiaId idAlergia;
	private String tipoAlergia;
	private String localAfetado;
	private Date dataDescoberta;
	private String efeitos;
	private String medicamento;
	
	public AlergiaId getIdAlergia() {
		return idAlergia;
	}
	public void setIdAlergia(AlergiaId idAlergia) {
		this.idAlergia = idAlergia;
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
	public void setEfeitos(String efeitos) {
		this.efeitos = efeitos;
	}
	public String getMedicamento() {
		return medicamento;
	}
	public void setMedicamento(String medicamento) {
		this.medicamento = medicamento;
	}

}
