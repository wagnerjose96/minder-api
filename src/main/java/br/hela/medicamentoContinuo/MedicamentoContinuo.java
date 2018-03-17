package br.hela.medicamentoContinuo;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import br.hela.medicamentoContinuo.comandos.CriarMedicamentoContinuo;

@Entity
public class MedicamentoContinuo {

	@EmbeddedId
	@AttributeOverride(name="value", column=@Column(name="id"))
	private MedicamentoContinuoId idMedicamentoContinuo; 
	private int idMedicamento; //Será trocado pelo ID da classe medicamento
	private String tipoMedicamento;
	private int quantidadeDeConsumo; 
	private Date intervaloDeConsumo;
	private Date dataConsumo;
	
	
	public MedicamentoContinuo() {
	}

	public MedicamentoContinuo(CriarMedicamentoContinuo comando) {
		this.idMedicamentoContinuo = new MedicamentoContinuoId();
		this.idMedicamento = comando.getIdMedicamento();
		this.tipoMedicamento = comando.getTipoMedicamento();
		this.quantidadeDeConsumo = comando.getQuantidadeDeConsumo();
		this.intervaloDeConsumo = comando.getIntervaloDeConsumo();
		this.dataConsumo = comando.getDataConsumo();
	}

	

	public MedicamentoContinuoId getIdMedicamentoContinuo() {
		return idMedicamentoContinuo;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idMedicamentoContinuo == null) ? 0 : idMedicamentoContinuo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MedicamentoContinuo other = (MedicamentoContinuo) obj;
		if ( idMedicamentoContinuo == null) {
			if (other.idMedicamentoContinuo != null)
				return false;
		} else if (!idMedicamentoContinuo.equals(other.idMedicamentoContinuo))
			return false;
		return true;
	}
	
}
