package br.minder.sexo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.minder.sexo.comandos.BuscarSexo;
import br.minder.sexo.comandos.CriarSexo;
import br.minder.sexo.comandos.EditarSexo;

@Service
@Transactional
public class SexoService {
	@Autowired
	private SexoRepository repo;

	public Optional<SexoId> salvar(CriarSexo comando) {
		if (comando.getGenero() != null) {
			Sexo novo = repo.save(new Sexo(comando));
			return Optional.of(novo.getIdGenero());
		}
		return Optional.empty();
	}

	public Optional<BuscarSexo> encontrar(SexoId id) {
		Optional<Sexo> genero = repo.findById(id);
		if (genero.isPresent()) {
			BuscarSexo resultado = new BuscarSexo(genero.get());
			return Optional.of(resultado);
		}
		return Optional.empty();
	}

	public Optional<List<BuscarSexo>> encontrar() {
		List<BuscarSexo> resultados = new ArrayList<>();
		List<Sexo> generos = repo.findAll();
		if (!generos.isEmpty()) {
			for (Sexo genero : generos) {
				BuscarSexo sexo = new BuscarSexo(genero);
				resultados.add(sexo);
			}
			return Optional.of(resultados);
		}
		return Optional.empty();
	}

	public Optional<SexoId> alterar(EditarSexo comando) {
		Optional<Sexo> optional = repo.findById(comando.getId());
		if (comando.getGenero() != null && optional.isPresent()) {
			Sexo genero = optional.get();
			genero.apply(comando);
			repo.save(genero);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}
}