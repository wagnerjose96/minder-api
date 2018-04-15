package br.hela.alergia.comandos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import br.hela.alergia.AlergiaId;
import br.hela.alergia.alergia_medicamento.Alergia_Medicamento;

public class EditarAlergia {
	private AlergiaId idAlergia;
	private String tipoAlergia;
	private String localAfetado;
	private Date dataDescoberta;
	private String efeitos;
	private List<Alergia_Medicamento> medicamentos = new ArrayList<>();

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

	public List<Alergia_Medicamento> getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(List<Alergia_Medicamento> medicamentos) {
		this.medicamentos = medicamentos;
	}

}
