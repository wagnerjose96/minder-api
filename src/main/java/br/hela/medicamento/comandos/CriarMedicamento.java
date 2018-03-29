package br.hela.medicamento.comandos;

import br.hela.medicamento.MedicamentoId;

public class CriarMedicamento {
	
	private MedicamentoId idMedicamento;
	private String nomeMedicamento;
	private String composicao;
	
	public CriarMedicamento() {
		
	}

	public MedicamentoId getIdMedicamento() {
		return idMedicamento;
	}

	public void setIdMedicamento(MedicamentoId idMedicamento) {
		this.idMedicamento = idMedicamento;
	}

	public String getNomeMedicamento() {
		return nomeMedicamento;
	}

	public void setNomeMedicamento(String nomeMedicamento) {
		this.nomeMedicamento = nomeMedicamento;
	}

	public String getComposicao() {
		return composicao;
	}

	public void setComposicao(String composicao) {
		this.composicao = composicao;
	}
	
	

}
