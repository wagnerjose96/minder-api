package br.hela.alergia;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.alergia.comandos.CriarAlergia;
import br.hela.alergia.comandos.EditarAlergia;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@EqualsAndHashCode(exclude={"tipoAlergia", "localAfetado", "dataDescoberta", "efeitos"})
@Data
public class Alergia {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private AlergiaId idAlergia;
	private String tipoAlergia;
	private String localAfetado;
	private Date dataDescoberta;
	private String efeitos;

	public Alergia() {
	}
	
	public Alergia(CriarAlergia comandos) {
		this.idAlergia = new AlergiaId();
		this.tipoAlergia = comandos.getTipoAlergia();
		this.localAfetado = comandos.getLocalAfetado();
		this.dataDescoberta = comandos.getDataDescoberta();
		this.efeitos = comandos.getEfeitos();
	}

	public void apply(EditarAlergia comando) {
		this.idAlergia = comando.getIdAlergia();
		this.tipoAlergia = comando.getTipoAlergia();
		this.localAfetado = comando.getLocalAfetado();
		this.dataDescoberta = comando.getDataDescoberta();
		this.efeitos = comando.getEfeitos();
	}
}
