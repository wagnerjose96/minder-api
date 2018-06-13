package br.hela.tipoSexo;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.hela.tipoSexo.comandos.CriarTipoSexo;
import br.hela.tipoSexo.comandos.EditarTipoSexo;

@Service
@Transactional
public class TipoSexoService {
	@Autowired
	TipoSexoRepository repo;

	public Optional<List<TipoSexo>> encontrarTudo() {
		return Optional.of(repo.findAll());
	}

	public Optional<TipoSexo> encontrarPorId(TipoSexoId id) {
		return repo.findById(id);
	}

	public Optional<TipoSexoId> salvar(CriarTipoSexo comando) {
		TipoSexo novo = new TipoSexo(comando);
		repo.save(novo);
		return Optional.of(novo.getId());
	}

	public Optional<TipoSexoId> alterar(EditarTipoSexo comando) {
		Optional<TipoSexo> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			TipoSexo sexo = optional.get();
			sexo.apply(comando);
			repo.save(sexo);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}
}
