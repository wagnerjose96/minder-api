package br.unicesumar.cor;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import br.unicesumar.cor.comandos.CriarCor;

@Entity
public class Cor {
	@EmbeddedId
	@AttributeOverride(name="value", column=@Column(name="id"))
	private CorId id;
	private String nome;

	public Cor() {
	}

	public Cor(CriarCor comando) {
		this.id = new CorId();
		this.nome = comando.getNome();
	}

	public CorId getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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
		Cor other = (Cor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
