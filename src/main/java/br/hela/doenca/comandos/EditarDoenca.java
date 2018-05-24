package br.hela.doenca.comandos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import br.hela.doenca.DoencaId;
import br.hela.medicamento.MedicamentoId;

public class EditarDoenca {
	private DoencaId idDoenca;
	private String nomeDoenca;
	private Date dataDescoberta;
	private List<MedicamentoId> id_medicamentos = new ArrayList<MedicamentoId>();

	public EditarDoenca() {
	}

	public DoencaId getIdDoenca() {
		return idDoenca;
	}

	public void setIdDoenca(DoencaId idDoenca) {
		this.idDoenca = idDoenca;
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

	public List<MedicamentoId> getId_medicamentos() {
		return id_medicamentos;
	}

	public void setId_medicamentos(List<MedicamentoId> id_medicamentos) {
		this.id_medicamentos = id_medicamentos;
	}

}
