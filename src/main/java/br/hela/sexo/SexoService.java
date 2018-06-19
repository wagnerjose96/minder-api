package br.hela.sexo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.hela.sexo.comandos.BuscarSexo;
import br.hela.sexo.comandos.CriarSexo;
import br.hela.sexo.comandos.EditarSexo;

@Service
@Transactional
public class SexoService {
	@Autowired
	private SexoRepository repo;

	public Optional<SexoId> salvar(CriarSexo comando) {
		Sexo novo = repo.save(new Sexo(comando));
		return Optional.of(novo.getIdSexo());
	}

	public Optional<BuscarSexo> encontrar(SexoId id) {
		Sexo genero = repo.findById(id).get();
		BuscarSexo resultado = new BuscarSexo();
		resultado = new BuscarSexo(genero);
		return Optional.of(resultado);
	}

	public Optional<List<BuscarSexo>> encontrar() {
		List<BuscarSexo> resultados = new ArrayList<>();
		List<Sexo> generos = repo.findAll();
		for (Sexo genero : generos) {
			BuscarSexo sexo = new BuscarSexo(genero);
			resultados.add(sexo);
		}
		return Optional.of(resultados);
	}

	public Optional<SexoId> alterar(EditarSexo comando) {
		Optional<Sexo> optional = repo.findById(comando.getIdSexo());
		if (optional.isPresent()) {
			Sexo genero = optional.get();
			genero.apply(comando);
			repo.save(genero);
			return Optional.of(comando.getIdSexo());
		}
		return Optional.empty();
	}
}