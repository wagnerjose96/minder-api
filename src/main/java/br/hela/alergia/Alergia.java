package br.hela.alergia;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import com.wordnik.swagger.annotations.ApiModelProperty;

import br.hela.alergia.comandos.CriarAlergia;
import br.hela.alergia.comandos.EditarAlergia;

@Entity
@Audited
public class Alergia {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@ApiModelProperty(required = true)
	private AlergiaId id;
	@ApiModelProperty(required = true)
	private String tipo_alergia;
	@ApiModelProperty(required = true)
	private Date data_descoberta;
	@ApiModelProperty(required = true)
	private String medicamento;
	@ApiModelProperty(required = true)
	private String local_afetado;
	@ApiModelProperty(required = true)
	private String efeitos;
	
	public Alergia() {
	}
	
	public Alergia(CriarAlergia comando){
		this.id = new AlergiaId();
		this.tipo_alergia = comando.getTipo_alergia();
		this.data_descoberta = comando.getData_descoberta();
		this.medicamento = comando.getMedicamento();
		this.local_afetado = comando.getLocal_afetado();
		this.efeitos = comando.getEfeitos();
	}
	
	public void apply(EditarAlergia comando) {
		this.tipo_alergia = comando.getTipo_alergia();
		this.data_descoberta = comando.getData_descoberta();
		this.medicamento = comando.getMedicamento();
		this.local_afetado = comando.getLocal_afetado();
		this.efeitos = comando.getEfeitos();
	}

	public String getTipo_alergia() {
		return tipo_alergia;
	}

	public void setTipo_alergia(String tipo_alergia) {
		this.tipo_alergia = tipo_alergia;
	}

	public Date getData_descoberta() {
		return data_descoberta;
	}

	public void setData_descoberta(Date data_descoberta) {
		this.data_descoberta = data_descoberta;
	}

	public String getMedicamento() {
		return medicamento;
	}

	public void setMedicamento(String medicamento) {
		this.medicamento = medicamento;
	}

	public String getLocal_afetado() {
		return local_afetado;
	}

	public void setLocal_afetado(String local_afetado) {
		this.local_afetado = local_afetado;
	}

	public String getEfeitos() {
		return efeitos;
	}

	public void setEfeitos(String efeitos) {
		this.efeitos = efeitos;
	}

	public AlergiaId getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Alergia other = (Alergia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
