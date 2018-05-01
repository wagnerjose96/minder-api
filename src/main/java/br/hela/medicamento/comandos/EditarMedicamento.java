package br.hela.medicamento.comandos;

import br.hela.medicamento.MedicamentoId;

public class EditarMedicamento {
	private MedicamentoId idMedicamento;
	private String nomeMedicamento;
	private String composicao;
	public int ativo;

	public void setAtivo(int ativo) {
		this.ativo = ativo;
	}

	public int getAtivo() {
		return ativo;
	}

	public EditarMedicamento() {
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
