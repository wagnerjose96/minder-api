package br.minder.alergia;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.minder.alergia.comandos.CriarAlergia;
import br.minder.alergia.comandos.EditarAlergia;
import br.minder.usuario.UsuarioId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Audited
@Getter
public class Alergia {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private AlergiaId idAlergia;
	private String tipoAlergia;
	private String localAfetado;
	private Date dataDescoberta;
	private String efeitos;
	@AttributeOverride(name = "value", column = @Column(name = "id_usuario"))
	private UsuarioId idUsuario;

	public Alergia() {
	}

	public Alergia(CriarAlergia comandos, UsuarioId id) {
		this.idAlergia = new AlergiaId();
		this.tipoAlergia = comandos.getTipoAlergia();
		this.localAfetado = comandos.getLocalAfetado();
		this.dataDescoberta = comandos.getDataDescoberta();
		this.efeitos = comandos.getEfeitos();
		this.idUsuario = id;
	}

	public void apply(EditarAlergia comando) {
		this.idAlergia = comando.getIdAlergia();
		this.tipoAlergia = comando.getTipoAlergia();
		this.localAfetado = comando.getLocalAfetado();
		this.dataDescoberta = comando.getDataDescoberta();
		this.efeitos = comando.getEfeitos();
	}

}
