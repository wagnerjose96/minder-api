package br.hela.alarme;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import br.hela.alarme.comandos.CriarAlarme;
import br.hela.alarme.comandos.EditarAlarme;

@Entity
@Audited
public class Alarme {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private AlarmeId id;
	private Date dataInicio;
	private Date dataFim;
	private String quantidade;
	private String descricao;
	private int periodicidade;

	public Alarme() {
	}

	public Alarme(CriarAlarme comando) {
		this.id = new AlarmeId();
		this.dataInicio = comando.getDataInicio();
		this.dataFim = comando.getDataFim();
		this.quantidade = comando.getQuantidade();
		this.descricao = comando.getDescricao();
		this.periodicidade = comando.getPeriodicidade();
	}

	public void apply(EditarAlarme comando) {
		this.id = comando.getId();
		this.dataInicio = comando.getDataInicio();
		this.dataFim = comando.getDataFim();
		this.quantidade = comando.getQuantidade();
		this.descricao = comando.getDescricao();
		this.periodicidade = comando.getPeriodicidade();
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

	public String getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(String quantidade) {
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

	public AlarmeId getId() {
		return id;
	}

}
