package br.minder.emergencia;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;

import br.minder.emergencia.comandos.CriarEmergencia;
import br.minder.emergencia.comandos.EditarEmergencia;
import br.minder.usuario.UsuarioId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Audited
@Getter
public class Emergencia {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id_emergencia"))
	@Setter(AccessLevel.NONE)
	private EmergenciaId idEmergencia;
	@AttributeOverride(name = "value", column = @Column(name = "id_usuario"))
	private UsuarioId idUsuario;
	private int ataqueConvulsivos;
	private String problemasCardiacos;
	private int doadorDeOrgaos;

	public Emergencia() {
	}

	public Emergencia(CriarEmergencia comando, UsuarioId id) {
		this.idEmergencia = new EmergenciaId();
		this.ataqueConvulsivos = comando.getAtaqueConvulsivos();
		this.problemasCardiacos = comando.getProblemasCardiacos();
		this.doadorDeOrgaos = comando.getDoadorDeOrgaos();
		this.idUsuario = id;
	}

	public void apply(EditarEmergencia comando) {
		this.idEmergencia = comando.getId();
		this.ataqueConvulsivos = comando.getAtaqueConvulsivos();
		this.problemasCardiacos = comando.getProblemasCardiacos();
		this.doadorDeOrgaos = comando.getDoadorDeOrgaos();
	}
}
