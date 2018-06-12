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
@EqualsAndHashCode(exclude = { "ddd", "numero"})
public class Telefone {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id_telefone"))
	@Setter(AccessLevel.NONE)
	private TelefoneId idTelefone;
	private int ddd;
	private int numero;
	private int telefone;
	private int ativo;

	public Telefone() {
	}

	public Telefone(CriarTelefone comando) {
		this.idTelefone = new TelefoneId();
		this.ddd = comando.getDdd();
		this.numero = comando.getNumero();
		this.ativo = 1;
	}

	public void apply(EditarTelefone comando) {
		this.idTelefone = comando.getIdTelefone();
		this.ddd = comando.getDdd();
		this.numero = comando.getNumero();
	}
}
