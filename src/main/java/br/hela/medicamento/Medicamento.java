package br.hela.medicamento;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.medicamento.comandos.CriarMedicamento;
import br.hela.medicamento.comandos.EditarMedicamento;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = { "nomeMedicamento", "composicao", "ativo"})
public class Medicamento {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id_medicamento"))
	@Setter(AccessLevel.NONE)
	private MedicamentoId idMedicamento;
	private String nomeMedicamento;
	private String composicao;
	private int ativo;

	public Medicamento() {
	}

	public Medicamento(CriarMedicamento comando) {
		this.idMedicamento = new MedicamentoId();
		this.nomeMedicamento = comando.getNomeMedicamento();
		this.composicao = comando.getComposicao();
		this.ativo = comando.getAtivo();
	}

	public void apply(EditarMedicamento comando) {
		this.idMedicamento = comando.getIdMedicamento();
		this.nomeMedicamento = comando.getNomeMedicamento();
		this.composicao = comando.getComposicao();
		this.ativo = comando.getAtivo();
	}
}
