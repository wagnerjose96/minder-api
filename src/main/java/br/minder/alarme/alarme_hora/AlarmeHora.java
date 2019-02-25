package br.minder.alarme.alarme_hora;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;

import br.minder.alarme.AlarmeId;
import br.minder.alarme.alarme_hora.comandos.CriarAlarmeHora;
import br.minder.usuario.UsuarioId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Audited
@Getter
public class AlarmeHora {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private AlarmeHoraId id;
	@AttributeOverride(name = "value", column = @Column(name = "id_alarme"))
	private AlarmeId idAlarme;
	@AttributeOverride(name = "value", column = @Column(name = "id_usuario"))
	private UsuarioId idUsuario;
	private Date dataAlarme;

	public AlarmeHora() {
	}

	public AlarmeHora(CriarAlarmeHora comando, UsuarioId id) {
		this.id = new AlarmeHoraId();
		this.dataAlarme = comando.getDataAlarme();
		this.idUsuario = id;		
		this.idAlarme = comando.getIdAlarme();
	}

}
