package br.minder.medicamento;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.minder.medicamento.comandos.CriarMedicamento;
import br.minder.medicamento.comandos.EditarMedicamento;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Audited
@Getter
public class Medicamento {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id_medicamento"))
	@Setter(AccessLevel.NONE)
	private MedicamentoId idMedicamento;
	private String nomeMedicamento;
	private String composicao;
	@Setter
	private int ativo;

	public Medicamento() {
	}

	public Medicamento(CriarMedicamento comando) {
		this.idMedicamento = new MedicamentoId();
		this.nomeMedicamento = comando.getNomeMedicamento();
		this.composicao = comando.getComposicao();
		this.ativo = 1;
	}

	public void apply(EditarMedicamento comando) {
		this.idMedicamento = comando.getIdMedicamento();
		this.nomeMedicamento = comando.getNomeMedicamento();
		this.composicao = comando.getComposicao();
		this.ativo = comando.getAtivo();
	}

}
