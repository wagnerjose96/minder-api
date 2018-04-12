package br.hela.exceptions;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.exceptions.comandos.CriarErrorDetail;

@Entity
@Audited
public class ErrorDetail {

	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id_error_detail"))
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

	public ErrorDetailId getIdErrorDetail() {
		return idErrorDetail;
	}

	public void setIdErrorDetail(ErrorDetailId idErrorDetail) {
		this.idErrorDetail = idErrorDetail;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idErrorDetail == null) ? 0 : idErrorDetail.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ErrorDetail other = (ErrorDetail) obj;
		if (idErrorDetail == null) {
			if (other.idErrorDetail != null)
				return false;
		} else if (!idErrorDetail.equals(other.idErrorDetail))
			return false;
		return true;
	}

}
