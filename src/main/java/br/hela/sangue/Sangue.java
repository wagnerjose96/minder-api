package br.hela.sangue;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.sangue.comandos.CriarSangue;
import br.hela.sangue.comandos.EditarSangue;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = { "sangue" })
public class Sangue {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id_sangue"))
	@Setter(AccessLevel.NONE)
	private SangueId idSangue;
	private String sangue;

	public Sangue() {
	}

	public Sangue(CriarSangue comando) {
		this.idSangue = new SangueId();
		this.sangue = comando.getSangue();
	}

	public void apply(EditarSangue comando) {
		this.idSangue = comando.getIdSangue();
		this.sangue = comando.getSangue();
	}
}
