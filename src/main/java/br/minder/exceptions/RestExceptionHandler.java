package br.minder.exceptions;

import java.nio.file.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import br.minder.exceptions.comandos.CriarErrorDetail;

@ControllerAdvice
public class RestExceptionHandler {
	@Autowired
	private ErrorDetailService service;

	@ExceptionHandler({ Exception.class })
	public @ResponseBody ResponseEntity<Object> allExceptions(Exception message) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String type = "Generic Error";
		String developerMessage = "Falha interna no servidor";
		CriarErrorDetail errorDetail = gerarErro(type, status.value(), message.getMessage(), developerMessage);
		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, status);
	}

	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler({ NullPointerException.class })
	public @ResponseBody ResponseEntity<Object> handleNullPointerException(NullPointerException message) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		String type = "Not found";
		String developerMessage = "Recurso não encontrado";
		CriarErrorDetail errorDetail = gerarErro(type, status.value(), message.getMessage(), developerMessage);
		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, status);
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler({ AccessDeniedException.class })
	public @ResponseBody ResponseEntity<Object> handleForbidden(Exception message) {
		HttpStatus status = HttpStatus.FORBIDDEN;
		String type = "Forbidden";
		String developerMessage = "Token incorreto";
		CriarErrorDetail errorDetail = gerarErro(type, status.value(), message.getMessage(), developerMessage);
		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, status);
	}

	private CriarErrorDetail gerarErro(String type, int status, String message, String developerMessage) {
		CriarErrorDetail errorDetail = new CriarErrorDetail();
		errorDetail.setType(type);
		errorDetail.setHttpStatus(status);
		errorDetail.setError(message);
		errorDetail.setDeveloperMessage(developerMessage);
		return errorDetail;
	}
}
