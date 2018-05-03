package br.hela.tipoSanguineo;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import br.hela.tipoSanguineo.comandos.CriarTipoSanguineo;
import br.hela.tipoSanguineo.comandos.EditarTipoSanguineo;

@Entity
@Audited
public class TipoSanguineo {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id_tipo_sanguineo"))
	private TipoSanguineoId tipoSanguineoId;
	private String tipoSangue;
	private String rh;
	
	public TipoSanguineo() {
		super();
	}

	public TipoSanguineo(CriarTipoSanguineo comando) {
		this.tipoSanguineoId = new TipoSanguineoId();
		this.tipoSangue = comando.getTipoSangue();
		this.rh = comando.getRh();
	}
	
	public void apply(EditarTipoSanguineo comando) {
		this.tipoSanguineoId = comando.getTipoSanguineoId();
		this.tipoSangue = comando.getTipoSangue();
		this.rh = comando.getRh();
	}

	public String getTipoSangue() {
		return tipoSangue;
	}

	public void setTipoSangue(String tipoSangue) {
		this.tipoSangue = tipoSangue;
	}

	public String getRh() {
		return rh;
	}

	public void setRh(String rh) {
		this.rh = rh;
	}

	public TipoSanguineoId getTipoSanguineoId() {
		return tipoSanguineoId;
	}
	
}
