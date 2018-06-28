package br.hela.doenca;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.doenca.comandos.CriarDoenca;
import br.hela.doenca.comandos.EditarDoenca;
import br.hela.usuario.UsuarioId;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = { "nomeDoenca", "dataDescoberta" })
public class Doenca {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id_doenca"))
	@Setter(AccessLevel.NONE)
	private DoencaId idDoenca;
	private String nomeDoenca;
	private Date dataDescoberta;
	@AttributeOverride(name = "value", column = @Column(name = "id_usuario"))
	private UsuarioId idUsuario;

	public Doenca() {
	}

	public Doenca(CriarDoenca comandos, UsuarioId id) {
		this.idDoenca = new DoencaId();
		this.nomeDoenca = comandos.getNomeDoenca();
		this.dataDescoberta = comandos.getDataDescoberta();
		this.idUsuario = id;
	}

	public void apply(EditarDoenca comandos) {
		this.idDoenca = comandos.getIdDoenca();
		this.nomeDoenca = comandos.getNomeDoenca();
		this.dataDescoberta = comandos.getDataDescoberta();
	}
}
