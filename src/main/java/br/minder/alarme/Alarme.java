package br.minder.alarme;

import java.sql.Time;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.minder.alarme.comandos.CriarAlarme;
import br.minder.alarme.comandos.EditarAlarme;
import br.minder.medicamento.MedicamentoId;
import br.minder.usuario.UsuarioId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Audited
@Getter
public class Alarme {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private AlarmeId id;
	@AttributeOverride(name = "value", column = @Column(name = "id_usuario"))
	private UsuarioId idUsuario;
	private Date dataInicio;
	private Date dataFim;
	private String quantidade;
	private String descricao;
	private int periodicidade;
	@AttributeOverride(name = "value", column = @Column(name = "id_medicamento"))
	private MedicamentoId idMedicamento;
	private Time horaPrimeiraDose;
	private Time horaUltimaDose;

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
		this.horaPrimeiraDose = comando.getHoraPrimeiraDose();
		this.horaUltimaDose = comando.getHoraPrimeiraDose();
	}

	public void apply(EditarAlarme comando) {
		this.id = comando.getId();
		this.idMedicamento = comando.getIdMedicamento();
		this.dataInicio = comando.getDataInicio();
		this.dataFim = comando.getDataFim();
		this.quantidade = comando.getQuantidade();
		this.descricao = comando.getDescricao();
		this.periodicidade = comando.getPeriodicidade();
		this.horaPrimeiraDose = comando.getHoraPrimeiraDose();
		this.horaUltimaDose = comando.getHoraUltimaDose();

	}

}
