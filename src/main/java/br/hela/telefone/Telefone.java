package br.hela.telefone;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.telefone.comandos.CriarTelefone;
import br.hela.telefone.comandos.EditarTelefone;

@Entity
@Audited
public class Telefone {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id_telefone"))
	private TelefoneId idTelefone;
	private int ddd;
	private int numero;

	public Telefone() {
	}

	public Telefone(CriarTelefone comando) {
		this.idTelefone = new TelefoneId();
		this.ddd = comando.getDdd();
		this.numero = comando.getNumero();
	}

	public void apply(EditarTelefone comando) {
		this.idTelefone = comando.getIdTelefone();
		this.ddd = comando.getDdd();
		this.numero = comando.getNumero();
	}

	public TelefoneId getIdTelefone() {
		return idTelefone;
	}

	public void setIdTelefone(TelefoneId idTelefone) {
		this.idTelefone = idTelefone;
	}

	public int getDdd() {
		return ddd;
	}

	public void setDdd(int ddd) {
		this.ddd = ddd;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idTelefone == null) ? 0 : idTelefone.hashCode());
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
		Telefone other = (Telefone) obj;
		if (idTelefone == null) {
			if (other.idTelefone != null)
				return false;
		} else if (!idTelefone.equals(other.idTelefone))
			return false;
		return true;
	}

}
