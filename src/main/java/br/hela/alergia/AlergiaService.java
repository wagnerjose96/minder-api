package br.hela.alergia;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.hela.alergia.Alergia;
import br.hela.alergia.AlergiaId;
import br.hela.alergia.comandos.CriarAlergia;
import br.hela.alergia.comandos.EditarAlergia;

@Service
@Transactional
public class AlergiaService {
	@Autowired
	private AlergiaRepository repo;

	public Optional<AlergiaId> salvar(CriarAlergia comando) {
		Alergia novo = repo.save(new Alergia(comando));
		return Optional.of(novo.getIdAlergia());
	}

	public Optional<Alergia> encontrar(AlergiaId id) {
		return repo.findById(id);
	}

	public Optional<List<Alergia>> encontrar() {
		return Optional.of(repo.findAll());
	}

	public Optional<String> deletar(AlergiaId id) {
		repo.deleteById(id);
		return Optional.of("Alergia -> " + id + ": deletada com sucesso");
	}

	public Optional<AlergiaId> alterar(EditarAlergia comando) {
		Optional<Alergia> optional = repo.findById(comando.getIdAlergia());
		if (optional.isPresent()) {
			Alergia alergia = optional.get();
			alergia.apply(comando);
			repo.save(alergia);
			return Optional.of(comando.getIdAlergia());
		}
		return Optional.empty();
	}
}
