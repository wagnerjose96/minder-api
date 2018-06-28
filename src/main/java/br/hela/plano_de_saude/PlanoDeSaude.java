package br.hela.plano_de_saude;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.convenio.ConvenioId;
import br.hela.plano_de_saude.comandos.CriarPlanoDeSaude;
import br.hela.plano_de_saude.comandos.EditarPlanoDeSaude;
import br.hela.usuario.UsuarioId;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = { "numeroCartao", "habitacao", "territorio" })
public class PlanoDeSaude {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private PlanoDeSaudeId id;
	@AttributeOverride(name = "value", column = @Column(name = "id_convenio"))
	private ConvenioId idConvenio;
	private int numeroCartao;
	private String habitacao;
	private String territorio;
	@AttributeOverride(name = "value", column = @Column(name = "id_usuario"))
	private UsuarioId idUsuario;

	public PlanoDeSaude() {

	}

	public PlanoDeSaude(CriarPlanoDeSaude comando, UsuarioId id) {
		this.id = new PlanoDeSaudeId();
		this.idConvenio = comando.getIdConvenio();
		this.numeroCartao = comando.getNumeroCartao();
		this.habitacao = comando.getHabitacao();
		this.territorio = comando.getTerritorio();
		this.idUsuario = id;
	}

	public void apply(EditarPlanoDeSaude comando) {
		this.id = comando.getId();
		this.idConvenio = comando.getIdConvenio();
		this.numeroCartao = comando.getNumeroCartao();
		this.habitacao = comando.getHabitacao();
		this.territorio = comando.getTerritorio();
	}
}
