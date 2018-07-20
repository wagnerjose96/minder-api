package br.minder.exceptions;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.minder.exceptions.comandos.CriarErrorDetail;

@Service
@Transactional
public class ErrorDetailService {
	@Autowired
	private ErrorDetailRepository repo;

	public Optional<ErrorDetailId> salvar(CriarErrorDetail comando) {
		ErrorDetail novoError = repo.save(new ErrorDetail(comando));
		return Optional.of(novoError.getIdErrorDetail());
	}

	public Optional<List<ErrorDetail>> encontrar() {
		List<ErrorDetail> erros = repo.findAll();
		if (!erros.isEmpty())
			return Optional.of(erros);
		return Optional.empty();
	}

	public Optional<ErrorDetail> encontrar(ErrorDetailId id) {
		Optional<ErrorDetail> error = repo.findById(id);
		if (error.isPresent())
			return error;
		return Optional.empty();
	}

}
