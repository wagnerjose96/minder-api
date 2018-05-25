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
@EqualsAndHashCode(exclude = { "doadorDeOrgaos", "ataqueConvulsivos", "problemasCardiacos"})
public class Emergencia {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private EmergenciaId id;
	private int doadorDeOrgaos;
	private int ataqueConvulsivos;
	private String problemasCardiacos;

	public Emergencia() {
	}

	public Emergencia(CriarEmergencia comandos) {
		this.id = new EmergenciaId();
		this.doadorDeOrgaos = comandos.getDoadorDeOrgaos();
		this.ataqueConvulsivos = comandos.getAtaqueConvulsivos();
		this.problemasCardiacos = comandos.getProblemasCardiacos();
	}

	public void apply(EditarEmergencia comandos) {
		this.id = comandos.getId();
		this.doadorDeOrgaos = comandos.getDoadorDeOrgaos();
		this.ataqueConvulsivos = comandos.getAtaqueConvulsivos();
		this.problemasCardiacos = comandos.getProblemasCardiacos();
	}

}
