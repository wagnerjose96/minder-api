package br.hela.tipoSanguineo;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.tipoSanguineo.comandos.CriarTipoSanguineo;
import br.hela.tipoSanguineo.comandos.EditarTipoSanguineo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = { "tipoSangue", "rh"})
public class TipoSanguineo {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id_tipo_sanguineo"))
	@Setter(AccessLevel.NONE)
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
}
