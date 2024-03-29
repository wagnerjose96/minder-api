package br.minder.contato;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.minder.contato.comandos.CriarContato;
import br.minder.contato.comandos.EditarContato;
import br.minder.telefone.TelefoneId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Audited
@Getter
public class Contato {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private ContatoId id;
	private String nome;
	@AttributeOverride(name = "value", column = @Column(name = "id_telefone"))
	@Setter
	private TelefoneId idTelefone;

	public Contato() {
	}

	public Contato(CriarContato comandos) {
		this.id = new ContatoId();
		this.nome = comandos.getNome();
	}

	public void apply(EditarContato comandos) {
		this.id = comandos.getId();
		this.nome = comandos.getNome();
	}
}
