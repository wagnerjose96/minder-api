package br.hela.tipoGenero;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;

import br.hela.tipoGenero.comandos.CriarGenero;
import br.hela.tipoGenero.comandos.EditarGenero;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = {"genero"})
public class Genero {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private GeneroId id;
	private String genero;
	
	public Genero() {
		super();
	}

	public Genero(CriarGenero comando) {
		this.id = new GeneroId();
		this.genero = comando.getGenero();
	}
	
	public void apply(EditarGenero comando) {
		this.id = comando.getId();
		this.genero = comando.getGenero();
	}
}
