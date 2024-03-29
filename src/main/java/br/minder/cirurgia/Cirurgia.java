package br.minder.cirurgia;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import br.minder.cirurgia.comandos.CriarCirurgia;
import br.minder.cirurgia.comandos.EditarCirurgia;
import br.minder.usuario.UsuarioId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Getter
public class Cirurgia {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private CirurgiaId idCirurgia;
	private String tipoCirurgia;
	private Date dataCirurgia;
	private String clinicaResponsavel;
	private String medicoResponsavel;
	@AttributeOverride(name = "value", column = @Column(name = "id_usuario"))
	private UsuarioId idUsuario;

	public Cirurgia() {
	}

	public Cirurgia(CriarCirurgia comando, UsuarioId id) {
		this.idCirurgia = new CirurgiaId();
		this.tipoCirurgia = comando.getTipoCirurgia();
		this.dataCirurgia = comando.getDataCirurgia();
		this.clinicaResponsavel = comando.getClinicaResponsavel();
		this.medicoResponsavel = comando.getMedicoResponsavel();
		this.idUsuario = id;
	}

	public void apply(EditarCirurgia comando) {
		this.idCirurgia = comando.getIdCirurgia();
		this.tipoCirurgia = comando.getTipoCirurgia();
		this.dataCirurgia = comando.getDataCirurgia();
		this.clinicaResponsavel = comando.getClinicaResponsavel();
		this.medicoResponsavel = comando.getMedicoResponsavel();
	}
}
