package br.hela.emergencia;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.emergencia.comandos.CriarEmergencia;
import br.hela.emergencia.comandos.EditarEmergencia;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = { "ataqueConvulsivos", "problemasCardiacos", "doadorDeOrgaos"})
public class Emergencia {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id_emergencia"))
	@Setter(AccessLevel.NONE)
	private EmergenciaId idEmergencia;
	private int ataqueConvulsivos;
	private String problemasCardiacos;
	public int doadorDeOrgaos;

	public Emergencia() {
	}

	public Emergencia(CriarEmergencia comando) {
		this.idEmergencia = new EmergenciaId();
		this.ataqueConvulsivos = comando.getAtaqueConvulsivos();
		this.problemasCardiacos = comando.getProblemasCardiacos();
		this.doadorDeOrgaos = comando.getDoadorDeOrgaos();
	}

	public void apply(EditarEmergencia comando) {
		this.idEmergencia = comando.getId();
		this.ataqueConvulsivos = comando.getAtaqueConvulsivos();
		this.problemasCardiacos = comando.getProblemasCardiacos();
		this.doadorDeOrgaos = comando.getDoadorDeOrgaos();
	}
}
