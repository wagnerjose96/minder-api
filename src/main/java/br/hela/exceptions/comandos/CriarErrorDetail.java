package br.hela.exceptions.comandos;

import lombok.Data;

@Data
public class CriarErrorDetail {
	private String type;
	private String error;
	private String developerMessage;
	private int httpStatus;
}
