package br.hela.alarme.comandos;

import java.util.Date;

import br.hela.alarme.AlarmeId;
import br.hela.medicamento.MedicamentoId;

public class EditarAlarme {
	private AlarmeId id;
	private Date dataInicio;
	private Date dataFim;
	private int quantidade;
	private String descricao;
	private int periodicidade;
	private MedicamentoId idMedicamento;

	public EditarAlarme() {
	}

	public AlarmeId getId() {
		return id;
	}

	public void setId(AlarmeId id) {
		this.id = id;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getPeriodicidade() {
		return periodicidade;
	}

	public void setPeriodicidade(int periodicidade) {
		this.periodicidade = periodicidade;
	}

	public MedicamentoId getIdMedicamento() {
		return idMedicamento;
	}

	public void setIdMedicamento(MedicamentoId idMedicamento) {
		this.idMedicamento = idMedicamento;
	}

}
