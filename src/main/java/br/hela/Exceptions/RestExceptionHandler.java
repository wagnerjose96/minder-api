package br.hela.Exceptions;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javassist.tools.web.BadHttpRequest;

@ControllerAdvice
public class RestExceptionHandler{
	
	@ResponseStatus(code=HttpStatus.NOT_FOUND)
	@ExceptionHandler({ NullPointerException.class })
	public @ResponseBody ResponseEntity<?> NullPointerException(NullPointerException message) {

		ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setHttpStatus(HttpStatus.NOT_FOUND.value());
        errorDetail.setType("Recurso não encontrado.");
        errorDetail.setError("Usuário não registrado no sistema.");
        errorDetail.setDeveloperMessage(message.getClass().getName());
		return new ResponseEntity<>(errorDetail,null, HttpStatus.NOT_FOUND);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ SQLException.class })
	public @ResponseBody ResponseEntity<?> handleSQLException(SQLException message) {

		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorDetail.setType("Erro ao executar comando interno.");
		errorDetail.setError("Comando SQL inválido.");
        errorDetail.setDeveloperMessage(message.getClass().getName());
		return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ BadHttpRequest.class })
	public @ResponseBody ResponseEntity<?> handleBadHttpRequest(BadHttpRequest message) {

		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setHttpStatus(HttpStatus.BAD_REQUEST.value());
		errorDetail.setType("Erro ao realizar a requisição.");
		errorDetail.setError("Comando inválido");
        errorDetail.setDeveloperMessage(message.getClass().getName());
		return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
	}

}
