package br.hela.contato;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.contato.comandos.CriarContato;
import br.hela.contato.comandos.EditarContato;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = { "nome", "ddd", "numero"})
public class Contato {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private ContatoId id;
	private String nome;
	private int ddd;
	private int numero;

	public Contato() {
	}

	public Contato(CriarContato comandos) {
		this.id = new ContatoId();
		this.nome = comandos.getNome();
		this.ddd = comandos.getDdd();
		this.numero = comandos.getNumero();
	}

	public void apply(EditarContato comandos) {
		this.id = comandos.getId();
		this.nome = comandos.getNome();
		this.ddd = comandos.getDdd();
		this.numero = comandos.getNumero();
	}
}
