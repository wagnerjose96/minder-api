package br.hela.contato.contato_emergencia;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import br.hela.contato.ContatoId;
import br.hela.emergencia.EmergenciaId;
import lombok.Data;

@Entity
@Data
public class Contato_Emergencia {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private Contato_Emergencia_Id id;
	@AttributeOverride(name = "value", column = @Column(name = "id_contato"))
	private ContatoId idContato;
	@AttributeOverride(name = "value", column = @Column(name = "id_emergencia"))
	private EmergenciaId idEmergencia;

	public Contato_Emergencia() {
		this.id = new Contato_Emergencia_Id();
	}
}