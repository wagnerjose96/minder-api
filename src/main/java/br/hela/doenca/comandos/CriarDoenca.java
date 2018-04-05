package br.hela.doenca.comandos;

import java.util.Date;
import java.util.List;

import br.hela.doenca_medicamento.Doenca_Medicamento;
import br.hela.doenca_medicamento.Doenca_Medicamento_Id;
import br.hela.medicamento.MedicamentoId;

public class CriarDoenca {
	private String nomeDoenca;
	private Date dataDescoberta;
	private MedicamentoId idMedicamento;
	
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

	public MedicamentoId getIdMedicamento() {
		return idMedicamento;
	}

	public void setIdMedicamento(MedicamentoId idMedicamento) {
		this.idMedicamento = idMedicamento;
	}

	
}
