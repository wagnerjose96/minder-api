package br.hela.contato.contato_telefone;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import br.hela.contato.ContatoId;
import br.hela.telefone.TelefoneId;
import lombok.Data;

@Entity
@Data
public class Contato_Telefone {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private Contato_Telefone_Id id;
	@AttributeOverride(name = "value", column = @Column(name = "id_contato"))
	private ContatoId idContato;
	@AttributeOverride(name = "value", column = @Column(name = "id_telefone"))
	private TelefoneId idTelefone;
	
	public Contato_Telefone() {
		this.id = new Contato_Telefone_Id();
	}
}
