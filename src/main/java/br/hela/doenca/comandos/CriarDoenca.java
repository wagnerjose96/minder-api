package br.hela.doenca.comandos;

import java.util.Date;
import java.util.List;

import br.hela.medicamento.MedicamentoId;

public class CriarDoenca {
	private String nomeDoenca;
	private Date dataDescoberta;
	private MedicamentoId medicamento;
	
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

	public MedicamentoId getMedicamento() {
		return medicamento;
	}

	public void setMedicamento(MedicamentoId medicamento) {
		this.medicamento = medicamento;
	}

	
}
