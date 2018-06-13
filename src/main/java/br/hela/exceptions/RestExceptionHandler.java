package br.hela.exceptions;

import java.nio.file.AccessDeniedException;
import java.util.concurrent.TimeoutException;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import br.hela.exceptions.comandos.CriarErrorDetail;
import javassist.tools.web.BadHttpRequest;

@ControllerAdvice
public class RestExceptionHandler {
	@Autowired
	private ErrorDetailService service;

	@ExceptionHandler({ Exception.class })
	public @ResponseBody ResponseEntity<?> allExceptions(Exception message) throws Exception {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String type = "Generic Error";
		String developerMessage = "Falha interna no servidor";
		CriarErrorDetail errorDetail = gerarErro(type, status.value(), message.getMessage(), developerMessage);
		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, status);
	}

	@ResponseStatus(code = HttpStatus.REQUEST_TIMEOUT)
	@ExceptionHandler({ TimeoutException.class })
	public @ResponseBody ResponseEntity<?> requestTimeout(TimeoutException message) {
		HttpStatus status = HttpStatus.REQUEST_TIMEOUT;
		String type = "Request Timeout";
		String developerMessage = "Tempo de requisição esgotado";
		CriarErrorDetail errorDetail = gerarErro(type, status.value(), message.getMessage(),
				developerMessage);
		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, status);
	}

	@ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ExceptionHandler({ UnsupportedMediaTypeStatusException.class })
	public @ResponseBody ResponseEntity<?> unsupportedMediaType(UnsupportedMediaTypeStatusException message) {
		HttpStatus status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
		String type = "Unsupported media type";
		String developerMessage = "Mídia não suportada";
		CriarErrorDetail errorDetail = gerarErro(type, status.value(), message.getMessage(),
				developerMessage);
		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, status);
	}

	@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler({ MethodNotAllowedException.class })
	public @ResponseBody ResponseEntity<?> methodNotAllowed(MethodNotAllowedException message) {
		HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
		String type = "Method not allowed";
		String developerMessage = "Método não permitido";
		CriarErrorDetail errorDetail = gerarErro(type, status.value(), message.getMessage(),
				developerMessage);
		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, status);
	}

	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler({ NullPointerException.class })
	public @ResponseBody ResponseEntity<?> handleNullPointerException(NullPointerException message) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		String type = "Not found";
		String developerMessage = "Recurso não encontrado";
		CriarErrorDetail errorDetail = gerarErro(type, status.value(), message.getMessage(),
				developerMessage);
		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, status);
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler({ AccessDeniedException.class })
	public @ResponseBody ResponseEntity<?> handleForbidden(Exception message) {
		HttpStatus status = HttpStatus.FORBIDDEN;
		String type = "Forbidden";
		String developerMessage = "Token incorreto";
		CriarErrorDetail errorDetail = gerarErro(type, status.value(), message.getMessage(),
				developerMessage);
		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, status);
	}

	@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
	@ExceptionHandler({ NotYetImplementedException.class })
	public @ResponseBody ResponseEntity<?> handleNotImplemented(NotYetImplementedException message) {
		HttpStatus status = HttpStatus.NOT_IMPLEMENTED;
		String type = "Not implemented";
		String developerMessage = "Método não implementado";
		CriarErrorDetail errorDetail = gerarErro(type, status.value(), message.getMessage(),
				developerMessage);
		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, status);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ BadHttpRequest.class })
	public @ResponseBody ResponseEntity<?> handleBadHttpRequest(BadHttpRequest message) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String type = "Bad request";
		String developerMessage = "Requisição incorreta";
		CriarErrorDetail errorDetail = gerarErro(type, status.value(), message.getMessage(),
				developerMessage);
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
