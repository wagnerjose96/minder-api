package br.hela.medicamentoContinuo.comandos;

import java.util.Date;

public class CriarMedicamentoContinuo {
	
	private int idMedicamento; //Será trocado pelo ID da classe medicamento
	private String tipoMedicamento;
	private int quantidadeDeConsumo; 
	private Date intervaloDeConsumo;
	private Date dataConsumo;
	
	
	public CriarMedicamentoContinuo() {
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


	public Date getIntervaloDeConsumo() {
		return intervaloDeConsumo;
	}


	public void setIntervaloDeConsumo(Date intervaloDeConsumo) {
		this.intervaloDeConsumo = intervaloDeConsumo;
	}


	public Date getDataConsumo() {
		return dataConsumo;
	}


	public void setDataConsumo(Date dataConsumo) {
		this.dataConsumo = dataConsumo;
	}

	
}
