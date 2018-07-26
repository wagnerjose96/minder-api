package br.minder.genero;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;

import br.minder.genero.comandos.CriarGenero;
import br.minder.genero.comandos.EditarGenero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Audited
@Getter
public class Genero {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id_genero"))
	@Setter(AccessLevel.NONE)
	private GeneroId idGenero;
	@Column(name = "genero")
	private String tipoGenero;

	public Genero() {
	}

	public Genero(CriarGenero comando) {
		this.idGenero = new GeneroId();
		this.tipoGenero = comando.getGenero();
	}

	public void apply(EditarGenero comando) {
		this.idGenero = comando.getId();
		this.tipoGenero = comando.getGenero();
	}
}
