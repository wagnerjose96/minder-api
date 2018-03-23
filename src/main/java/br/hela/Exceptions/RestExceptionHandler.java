package br.hela.Exceptions;

import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import javassist.tools.web.BadHttpRequest;

@ControllerAdvice
public class RestExceptionHandler{
	
	@ResponseStatus(code=HttpStatus.REQUEST_TIMEOUT)
	@ExceptionHandler({ TimeoutException.class })
	public @ResponseBody ResponseEntity<?> requestTimeout(TimeoutException message) {

		ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setHttpStatus(HttpStatus.REQUEST_TIMEOUT.value());
        errorDetail.setType("Tempo de resposta excedido");
        errorDetail.setError(message.getMessage());
        errorDetail.setDeveloperMessage(message.getClass().getName());
		return new ResponseEntity<>(errorDetail,null, HttpStatus.REQUEST_TIMEOUT);
	}
	
	@ResponseStatus(code=HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ExceptionHandler({ UnsupportedMediaTypeStatusException.class })
	public @ResponseBody ResponseEntity<?> unsupportedMediaType(UnsupportedMediaTypeStatusException message) {

		ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setHttpStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        errorDetail.setType("Mídia não suportada");
        errorDetail.setError(message.getMessage());
        errorDetail.setDeveloperMessage(message.getClass().getName());
		return new ResponseEntity<>(errorDetail,null, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}
	
	@ResponseStatus(code=HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler({ MethodNotAllowedException.class })
	public @ResponseBody ResponseEntity<?> methodNotAllowed(MethodNotAllowedException message) {

		ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setHttpStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        errorDetail.setType("Método não permitido");
        errorDetail.setError(message.getMessage());
        errorDetail.setDeveloperMessage(message.getClass().getName());
		return new ResponseEntity<>(errorDetail,null, HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	@ResponseStatus(code=HttpStatus.NOT_FOUND)
	@ExceptionHandler({ NullPointerException.class })
	public @ResponseBody ResponseEntity<?> NullPointerException(NullPointerException message) {

		ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setHttpStatus(HttpStatus.NOT_FOUND.value());
        errorDetail.setType("Recurso não encontrado");
        errorDetail.setError(message.getMessage());
        errorDetail.setDeveloperMessage(message.getClass().getName());
		return new ResponseEntity<>(errorDetail,null, HttpStatus.NOT_FOUND);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ SQLException.class })
	public @ResponseBody ResponseEntity<?> handleSQLException(SQLException message) {

		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorDetail.setType("Erro ao executar comando interno");
        errorDetail.setError(message.getMessage());
        errorDetail.setDeveloperMessage(message.getClass().getName());
		return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ BadHttpRequest.class })
	public @ResponseBody ResponseEntity<?> handleBadHttpRequest(BadHttpRequest message) {

		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setHttpStatus(HttpStatus.BAD_REQUEST.value());
		errorDetail.setType("Erro ao realizar a requisição");
        errorDetail.setError(message.getMessage());
        errorDetail.setDeveloperMessage(message.getClass().getName());
		return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
	}

}
