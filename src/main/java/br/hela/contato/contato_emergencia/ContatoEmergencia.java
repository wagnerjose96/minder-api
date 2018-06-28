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
public class ContatoEmergencia {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private ContatoEmergenciaId id;
	@AttributeOverride(name = "value", column = @Column(name = "id_contato"))
	private ContatoId idContato;
	@AttributeOverride(name = "value", column = @Column(name = "id_emergencia"))
	private EmergenciaId idEmergencia;

	public ContatoEmergencia() {
		this.id = new ContatoEmergenciaId();
	}
}