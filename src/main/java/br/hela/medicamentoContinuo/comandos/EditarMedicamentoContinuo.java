package br.hela.medicamentoContinuo.comandos;

import java.util.Date;

import br.hela.medicamentoContinuo.MedicamentoContinuoId;

public class EditarMedicamentoContinuo {
	private MedicamentoContinuoId idMedicamentoContinuo;
	private int idMedicamento; // Será trocado pelo ID da classe medicamento
	private String tipoMedicamento;
	private int quantidadeDeConsumo;
	private int intervaloDeConsumo;
	private Date dataConsumo;

	public EditarMedicamentoContinuo() {
	}

	public MedicamentoContinuoId getIdMedicamentoContinuo() {
		return idMedicamentoContinuo;
	}

	public void setIdMedicamentoContinuo(MedicamentoContinuoId idMedicamentoContinuo) {
		this.idMedicamentoContinuo = idMedicamentoContinuo;
	}

	public int getIdMedicamento() {
		return idMedicamento;
	}

	public void setIdMedicamento(int idMedicamento) {
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