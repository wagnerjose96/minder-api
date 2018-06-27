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
@EqualsAndHashCode(exclude = { "genero" })
public class Sexo {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id_sexo"))
	@Setter(AccessLevel.NONE)
	private SexoId idGenero;
	@Column(name = "sexo")
	private String genero;

	public Sexo() {
	}

	public Sexo(CriarSexo comando) {
		this.idGenero = new SexoId();
		this.genero = comando.getSexo();
	}

	public void apply(EditarSexo comando) {
		this.idGenero = comando.getId();
		this.genero = comando.getSexo();
	}
}
