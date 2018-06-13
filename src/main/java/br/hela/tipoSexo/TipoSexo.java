package br.hela.tipoSexo;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;

import br.hela.tipoSexo.comandos.CriarTipoSexo;
import br.hela.tipoSexo.comandos.EditarTipoSexo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = {"sexo"})
public class TipoSexo {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private TipoSexoId id;
	private String sexo;
	
	public TipoSexo() {
		super();
	}

	public TipoSexo(CriarTipoSexo comando) {
		this.id = new TipoSexoId();
		this.sexo = comando.getSexo();
	}
	
	public void apply(EditarTipoSexo comando) {
		this.id = comando.getId();
		this.sexo = comando.getSexo();
	}
}
