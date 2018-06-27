package br.hela.alarme;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.alarme.comandos.CriarAlarme;
import br.hela.alarme.comandos.EditarAlarme;
import br.hela.medicamento.MedicamentoId;
import br.hela.usuario.UsuarioId;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@EqualsAndHashCode(exclude={"idMedicamento", "dataInicio", "dataFim", "quantidade", "descricao", "periodicidade", "idUsuario"})
@Data
public class Alarme {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE) 
	private AlarmeId id;
	@AttributeOverride(name = "value", column = @Column(name = "id_medicamento"))
	private MedicamentoId idMedicamento;
	@AttributeOverride(name = "value", column = @Column(name = "id_usuario"))
	private UsuarioId idUsuario;
	private Date dataInicio;
	private Date dataFim;
	private String quantidade;
	private String descricao;
	private int periodicidade;

	public Alarme() {
	}

	public Alarme(CriarAlarme comando, UsuarioId id) {
		this.id = new AlarmeId();
		this.idMedicamento = comando.getIdMedicamento();
		this.dataInicio = comando.getDataInicio();
		this.dataFim = comando.getDataFim();
		this.quantidade = comando.getQuantidade();
		this.descricao = comando.getDescricao();
		this.periodicidade = comando.getPeriodicidade();
		this.idUsuario = id;
	}

	public void apply(EditarAlarme comando) {
		this.id = comando.getId();
		this.idMedicamento = comando.getIdMedicamento();
		this.dataInicio = comando.getDataInicio();
		this.dataFim = comando.getDataFim();
		this.quantidade = comando.getQuantidade();
		this.descricao = comando.getDescricao();
		this.periodicidade = comando.getPeriodicidade();
	}

}
