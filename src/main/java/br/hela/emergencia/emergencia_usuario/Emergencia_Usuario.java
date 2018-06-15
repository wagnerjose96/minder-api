package br.hela.emergencia.emergencia_usuario;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import br.hela.emergencia.EmergenciaId;
import br.hela.usuario.UsuarioId;
import lombok.Data;

@Entity
@Data
public class Emergencia_Usuario {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private Emergencia_Usuario_Id id;
	@AttributeOverride(name = "value", column = @Column(name = "id_emergencia"))
	private EmergenciaId idEmergencia;
	@AttributeOverride(name = "value", column = @Column(name = "id_usuario"))
	private UsuarioId idUsuario;

	public Emergencia_Usuario() {
		this.id = new Emergencia_Usuario_Id();
	}
}