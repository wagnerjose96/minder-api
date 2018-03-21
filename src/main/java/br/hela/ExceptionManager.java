package br.hela;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javassist.tools.web.BadHttpRequest;

@ControllerAdvice
public class ExceptionManager {

	@ResponseStatus(code=HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ SQLException.class })
	public @ResponseBody String handleSQLException(SQLException ex) {
		return ("==============> " + ex.getMessage());
	}
	
	@ResponseStatus(code=HttpStatus.NOT_FOUND)
	@ExceptionHandler({ NullPointerException.class })
	public @ResponseBody String handleNullPointerException(NullPointerException ex) {
		return (ex.getMessage());
	}
	
	@ResponseStatus(code=HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ NullPointerException.class })
	public @ResponseBody String handleadHttpRequest(BadHttpRequest ex) {
		return (ex.getMessage());
	}

}
