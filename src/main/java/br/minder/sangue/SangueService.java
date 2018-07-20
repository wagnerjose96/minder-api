package br.minder.sangue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.minder.sangue.comandos.BuscarSangue;
import br.minder.sangue.comandos.CriarSangue;
import br.minder.sangue.comandos.EditarSangue;

@Service
@Transactional
public class SangueService {
	@Autowired
	private SangueRepository repo;

	public Optional<SangueId> salvar(CriarSangue comando) {
		Sangue novo = repo.save(new Sangue(comando));
		return Optional.of(novo.getIdSangue());
	}

	public Optional<BuscarSangue> encontrar(SangueId id) {
		Optional<Sangue> sangue = repo.findById(id);
		if (sangue.isPresent()) {
			BuscarSangue resultado = new BuscarSangue(sangue.get());
			return Optional.of(resultado);
		}
		return Optional.empty();
	}

	public Optional<List<BuscarSangue>> encontrar() {
		List<Sangue> sangues = repo.findAll();
		List<BuscarSangue> resultados = new ArrayList<>();
		if (!sangues.isEmpty()) {
			for (Sangue sangue : sangues) {
				BuscarSangue nova = new BuscarSangue(sangue);
				resultados.add(nova);
			}
			return Optional.of(resultados);
		}
		return Optional.empty();
	}

	public Optional<SangueId> alterar(EditarSangue comando) {
		Optional<Sangue> optional = repo.findById(comando.getIdSangue());
		if (optional.isPresent()) {
			Sangue sangue = optional.get();
			sangue.apply(comando);
			repo.save(sangue);
			return Optional.of(comando.getIdSangue());
		}
		return Optional.empty();
	}
}