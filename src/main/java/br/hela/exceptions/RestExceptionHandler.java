package br.hela.exceptions;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
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

		CriarErrorDetail errorDetail = new CriarErrorDetail();
		errorDetail.setType("Generic Error");
		errorDetail.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorDetail.setError(message.getMessage());
		errorDetail.setDeveloperMessage("Erro vindo da exceção generica - Verificar console");
		// errorDetail.setDeveloperMessage(message.getClass().getName());

		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, null, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ResponseStatus(code = HttpStatus.REQUEST_TIMEOUT)
	@ExceptionHandler({ TimeoutException.class })
	public @ResponseBody ResponseEntity<?> requestTimeout(TimeoutException message) {

		CriarErrorDetail errorDetail = new CriarErrorDetail();
		errorDetail.setType("Request Timeout");
		errorDetail.setHttpStatus(HttpStatus.REQUEST_TIMEOUT.value());
		errorDetail.setError(message.getMessage());
		errorDetail.setDeveloperMessage(message.getClass().getName());

		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, null, HttpStatus.REQUEST_TIMEOUT);
	}

	@ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ExceptionHandler({ UnsupportedMediaTypeStatusException.class })
	public @ResponseBody ResponseEntity<?> unsupportedMediaType(UnsupportedMediaTypeStatusException message) {

		CriarErrorDetail errorDetail = new CriarErrorDetail();
		errorDetail.setType("Unsupported media type");
		errorDetail.setHttpStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
		errorDetail.setError(message.getMessage());
		errorDetail.setDeveloperMessage(message.getClass().getName());

		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, null, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler({ MethodNotAllowedException.class })
	public @ResponseBody ResponseEntity<?> methodNotAllowed(MethodNotAllowedException message) {

		CriarErrorDetail errorDetail = new CriarErrorDetail();
		errorDetail.setType("Método não implementado");
		errorDetail.setHttpStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
		errorDetail.setError(message.getMessage());
		errorDetail.setDeveloperMessage(message.getClass().getName());

		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, null, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler({ NullPointerException.class })
	public @ResponseBody ResponseEntity<?> handleNullPointerException(NullPointerException message) {

		CriarErrorDetail errorDetail = new CriarErrorDetail();
		errorDetail.setType("Not found");
		errorDetail.setHttpStatus(HttpStatus.NOT_FOUND.value());
		errorDetail.setError(message.getMessage());
		errorDetail.setDeveloperMessage(message.getClass().getName());

		// Optional<MedicamentoContinuoId> optionalMedicamentoContinuoId =
		// service.salvar(comando);
		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ SQLException.class })
	public @ResponseBody ResponseEntity<?> handleSQLException(InternalError message) {

		CriarErrorDetail errorDetail = new CriarErrorDetail();
		errorDetail.setType("Internal Server Error");
		errorDetail.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorDetail.setError(message.getMessage());
		errorDetail.setDeveloperMessage(message.getClass().getName());

		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler({ AccessDeniedException.class })
	public @ResponseBody ResponseEntity<?> handleForbidden(Exception message) {

		CriarErrorDetail errorDetail = new CriarErrorDetail();
		errorDetail.setType("Forbidden");
		errorDetail.setHttpStatus(HttpStatus.FORBIDDEN.value());
		errorDetail.setError(message.getMessage());
		errorDetail.setDeveloperMessage(message.getClass().getName());

		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, HttpStatus.FORBIDDEN);
	}

	@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
	@ExceptionHandler({ NotYetImplementedException.class })
	public @ResponseBody ResponseEntity<?> handleNotImplemented(NotYetImplementedException message) {

		CriarErrorDetail errorDetail = new CriarErrorDetail();
		errorDetail.setType("Not implemented");
		errorDetail.setHttpStatus(HttpStatus.NOT_IMPLEMENTED.value());
		errorDetail.setError(message.getMessage());
		errorDetail.setDeveloperMessage(message.getClass().getName());

		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, HttpStatus.NOT_IMPLEMENTED);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ BadHttpRequest.class })
	public @ResponseBody ResponseEntity<?> handleBadHttpRequest(BadHttpRequest message) {

		CriarErrorDetail errorDetail = new CriarErrorDetail();
		errorDetail.setType("Bad request");
		errorDetail.setHttpStatus(HttpStatus.BAD_REQUEST.value());
		errorDetail.setError(message.getMessage());
		errorDetail.setDeveloperMessage(message.getClass().getName());

		service.salvar(errorDetail);
		return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
	}
}
