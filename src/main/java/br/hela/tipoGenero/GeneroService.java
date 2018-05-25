package br.hela.tipoGenero;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.hela.tipoGenero.comandos.CriarGenero;
import br.hela.tipoGenero.comandos.EditarGenero;

@Service
@Transactional
public class GeneroService {
	@Autowired
	GeneroRepository repo;
	
	public Optional<List<Genero>> encontrarTudo(){
		return Optional.of(repo.findAll());
	}
	
	public Optional<Genero> encontrarPorId(GeneroId id){
		return repo.findById(id);
	}
	
	public Optional<GeneroId> salvar(CriarGenero comando){
		Genero novo = new Genero(comando);
		repo.save(novo);
		return Optional.of(novo.getId());
	}

	public Optional<GeneroId> alterar(EditarGenero comando) {
		Optional<Genero> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			Genero sexo = optional.get();
			sexo.apply(comando);
			repo.save(sexo);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}
}

