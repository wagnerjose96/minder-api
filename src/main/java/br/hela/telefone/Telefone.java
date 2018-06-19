package br.hela.telefone;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.telefone.comandos.CriarTelefone;
import br.hela.telefone.comandos.EditarTelefone;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = { "ddd", "numero" })
public class Telefone {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private TelefoneId id;
	private int ddd;
	private int numero;

	public Telefone() {
	}

	public Telefone(CriarTelefone comando) {
		this.id = new TelefoneId();
		this.ddd = comando.getDdd();
		this.numero = comando.getNumero();
	}

	public void apply(EditarTelefone comando) {
		this.id = comando.getId();
		this.ddd = comando.getDdd();
		this.numero = comando.getNumero();
	}
}
