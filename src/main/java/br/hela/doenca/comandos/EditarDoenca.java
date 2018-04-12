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
	private List<MedicamentoId> idMedicamento = new ArrayList<>();

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

	public List<MedicamentoId> getIdMedicamento() {
		return idMedicamento;
	}

	public void setIdMedicamento(List<MedicamentoId> idMedicamento) {
		this.idMedicamento = idMedicamento;
	}

	

}
