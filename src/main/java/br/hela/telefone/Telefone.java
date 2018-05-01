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
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private TelefoneId telefoneId;
	private int ddd;
	private int numero;

	public Telefone() {

	}

	public Telefone(CriarTelefone comandos) {
		this.telefoneId = new TelefoneId();
		this.ddd = comandos.getDdd();
		this.numero = comandos.getNumero();
	}

	public void aplly(EditarTelefone comandos) {
		this.telefoneId = comandos.getTelefoneId();
		this.ddd = comandos.getDdd();
		this.numero = comandos.getNumero();
	}

	public TelefoneId getTelefoneId() {
		return telefoneId;
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

}
