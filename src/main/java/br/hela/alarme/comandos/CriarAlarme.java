package br.hela.alarme.comandos;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;

import br.hela.medicamento.MedicamentoId;

public class CriarAlarme {
	private Date dataInicio;
	private Date dataFim;
	private String quantidade;
	private String descricao;
	private int periodicidade;
	@AttributeOverride(name = "value", column = @Column(name = "id_medicamento"))
	private MedicamentoId idMedicamento;

	public CriarAlarme() {
	}

	public Date getDataInicio() {
		return this.dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return this.dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getQuantidade() {
		return this.quantidade;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getPeriodicidade() {
		return this.periodicidade;
	}

	public void setPeriodicidade(int periodicidade) {
		this.periodicidade = periodicidade;
	}

	public MedicamentoId getIdMedicamento() {
		return this.idMedicamento;
	}

	public void setIdMedicamento(MedicamentoId idMedicamento) {
		this.idMedicamento = idMedicamento;
	}

}
