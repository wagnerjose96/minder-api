package br.hela.alergia.comandos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import br.hela.medicamento.MedicamentoId;

public class CriarAlergia {
	private String tipoAlergia;
	private String localAfetado;
	private Date dataDescoberta;
	private String efeitos;
	private List<MedicamentoId> id_medicamentos = new ArrayList<MedicamentoId>();

	public CriarAlergia() {
	}

	public String getTipoAlergia() {
		return this.tipoAlergia;
	}

	public void setTipoAlergia(String tipoAlergia) {
		this.tipoAlergia = tipoAlergia;
	}

	public String getLocalAfetado() {
		return this.localAfetado;
	}

	public void setLocalAfetado(String localAfetado) {
		this.localAfetado = localAfetado;
	}

	public Date getDataDescoberta() {
		return this.dataDescoberta;
	}

	public void setDataDescoberta(Date dataDescoberta) {
		this.dataDescoberta = dataDescoberta;
	}

	public String getEfeitos() {
		return this.efeitos;
	}

	public void setEfeitos(String efeitos) {
		this.efeitos = efeitos;
	}

	public List<MedicamentoId> getId_medicamentos() {
		return this.id_medicamentos;
	}

	public void setId_medicamentos(List<MedicamentoId> id_medicamentos) {
		this.id_medicamentos.addAll(id_medicamentos);
	}

}
