package br.hela.Exceptions;

public class ErrorDetail {

	private String type;
	private String error;
	private String developerMessage;
	private int HttpStatus;

	public ErrorDetail() {
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
		return HttpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		HttpStatus = httpStatus;
	}

}
