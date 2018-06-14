package br.hela.sexo;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.sexo.comandos.CriarSexo;
import br.hela.sexo.comandos.EditarSexo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = { "sexo" })
public class Sexo {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id_sexo"))
	@Setter(AccessLevel.NONE)
	private SexoId idSexo;
	private String sexo;

	public Sexo() {
	}

	public Sexo(CriarSexo comando) {
		this.idSexo = new SexoId();
		this.sexo = comando.getSexo();
	}

	public void apply(EditarSexo comando) {
		this.idSexo = comando.getIdSexo();
		this.sexo = comando.getSexo();
	}
}
