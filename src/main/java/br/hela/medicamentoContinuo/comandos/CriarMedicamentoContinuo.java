package br.hela.medicamentoContinuo.comandos;

import java.util.Date;

import br.hela.medicamento.MedicamentoId;

public class CriarMedicamentoContinuo {
	private MedicamentoId idMedicamento;
	private String tipoMedicamento;
	private int quantidadeDeConsumo;
	private int intervaloDeConsumo;
	private Date dataConsumo;

	public CriarMedicamentoContinuo() {
	}

	public MedicamentoId getIdMedicamento() {
		return idMedicamento;
	}

	public void setIdMedicamento(MedicamentoId idMedicamento) {
		this.idMedicamento = idMedicamento;
	}

	public String getTipoMedicamento() {
		return tipoMedicamento;
	}

	public void setTipoMedicamento(String tipoMedicamento) {
		this.tipoMedicamento = tipoMedicamento;
	}

	public int getQuantidadeDeConsumo() {
		return quantidadeDeConsumo;
	}

	public void setQuantidadeDeConsumo(int quantidadeDeConsumo) {
		this.quantidadeDeConsumo = quantidadeDeConsumo;
	}

	public int getIntervaloDeConsumo() {
		return intervaloDeConsumo;
	}

	public void setIntervaloDeConsumo(int intervaloDeConsumo) {
		this.intervaloDeConsumo = intervaloDeConsumo;
	}

	public Date getDataConsumo() {
		return dataConsumo;
	}

	public void setDataConsumo(Date dataConsumo) {
		this.dataConsumo = dataConsumo;
	}

}
