package br.minder.exceptions;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;

import br.minder.exceptions.comandos.CriarErrorDetail;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = { "type", "error", "developerMessage", "httpStatus"})
public class ErrorDetail {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id_error_detail"))
	@Setter(AccessLevel.NONE)
	private ErrorDetailId idErrorDetail;
	private String type;
	private String error;
	private String developerMessage;
	private int httpStatus;

	public ErrorDetail() {
	}

	public ErrorDetail(CriarErrorDetail comandos) {
		this.idErrorDetail = new ErrorDetailId();
		this.type = comandos.getType();
		this.error = comandos.getError();
		this.developerMessage = comandos.getDeveloperMessage();
		this.httpStatus = comandos.getHttpStatus();
	}
}
