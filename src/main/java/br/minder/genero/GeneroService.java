package br.minder.genero;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.minder.genero.comandos.BuscarGenero;
import br.minder.genero.comandos.CriarGenero;
import br.minder.genero.comandos.EditarGenero;

@Service
@Transactional
public class GeneroService {
	@Autowired
	private GeneroRepository repo;

	public Optional<GeneroId> salvar(CriarGenero comando) {
		if (comando.getGenero() != null) {
			Genero novo = repo.save(new Genero(comando));
			return Optional.of(novo.getIdGenero());
		}
		return Optional.empty();
	}

	public Optional<BuscarGenero> encontrar(GeneroId id) {
		Optional<Genero> genero = repo.findById(id);
		if (genero.isPresent()) {
			BuscarGenero resultado = new BuscarGenero(genero.get());
			return Optional.of(resultado);
		}
		return Optional.empty();
	}

	public Optional<List<BuscarGenero>> encontrar() {
		List<BuscarGenero> resultados = new ArrayList<>();
		List<Genero> generos = repo.findAll();
		if (!generos.isEmpty()) {
			for (Genero genero : generos) {
				BuscarGenero sexo = new BuscarGenero(genero);
				resultados.add(sexo);
			}
			return Optional.of(resultados);
		}
		return Optional.empty();
	}

	public Optional<GeneroId> alterar(EditarGenero comando) {
		Optional<Genero> optional = repo.findById(comando.getId());
		if (comando.getGenero() != null && optional.isPresent()) {
			Genero genero = optional.get();
			genero.apply(comando);
			repo.save(genero);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}
}