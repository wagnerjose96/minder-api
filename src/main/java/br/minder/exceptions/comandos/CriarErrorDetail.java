package br.minder.exceptions.comandos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarErrorDetail {
	private String type;
	private String error;
	private String developerMessage;
	private int httpStatus;
}
