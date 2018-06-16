package br.hela.exceptions;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/exceptions")
public class ErrorDetailController {
	@Autowired
	private ErrorDetailService service;

	@GetMapping
	public ResponseEntity<List<ErrorDetail>> getErrorDetail() {
		Optional<List<ErrorDetail>> optionalError = service.encontrar();
		return ResponseEntity.ok(optionalError.get());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ErrorDetail> getErrorDetailPorId(@PathVariable ErrorDetailId id) throws NullPointerException {
		Optional<ErrorDetail> optionalError = service.encontrar(id);
		if (verificaErrorExistente(id)) {
			return ResponseEntity.ok(optionalError.get());
		}
		throw new NullPointerException("A Exceção buscada não existe no banco de dados");
	}

	private boolean verificaErrorExistente(ErrorDetailId id) {
		if (!service.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}
}
