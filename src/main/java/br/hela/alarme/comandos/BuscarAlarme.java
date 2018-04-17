package br.hela.alarme.comandos;

import java.util.Date;

import br.hela.alarme.Alarme;
import br.hela.alarme.AlarmeId;
import br.hela.medicamento.Medicamento;

public class BuscarAlarme {
	private AlarmeId id;
	private Date dataInicio;
	private Date dataFim;
	private int quantidade;
	private String descricao;
	private int periodicidade;
	private Medicamento medicamento;

	public BuscarAlarme(Alarme comandos) {
		this.id = comandos.getId();
		this.dataInicio = comandos.getDataInicio();
		this.dataFim = comandos.getDataFim();
		this.quantidade = comandos.getQuantidade();
		this.descricao = comandos.getDescricao();
		this.periodicidade = comandos.getPeriodicidade();
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

	public Medicamento getMedicamento() {
		return medicamento;
	}

	public void setMedicamento(Medicamento medicamento) {
		this.medicamento = medicamento;
	}

}
