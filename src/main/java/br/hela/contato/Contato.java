package br.hela.contato;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.contato.comandos.CriarContato;
import br.hela.contato.comandos.EditarContato;

@Entity
@Audited
public class Contato {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private ContatoId id;
	private String nome;

	public Contato() {
	}

	public Contato(CriarContato comandos) {
		this.id = new ContatoId();
		this.nome = comandos.getNome();
	}

	public void apply(EditarContato comando) {
		this.id = comando.getId();
		this.nome = comando.getNome();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ContatoId getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contato other = (Contato) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
