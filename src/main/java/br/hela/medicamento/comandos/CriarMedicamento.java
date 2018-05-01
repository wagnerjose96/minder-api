package br.hela.medicamento.comandos;

public class CriarMedicamento {

	private String nomeMedicamento;
	private String composicao;
	private int ativo;

	public int getAtivo() {
		return ativo;
	}

	public void setAtivo(int ativo) {
		this.ativo = ativo;
	}

	public CriarMedicamento() {

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
