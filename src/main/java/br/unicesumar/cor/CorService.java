package br.unicesumar.cor;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.unicesumar.cor.comandos.CriarCor;

@Service
@Transactional
public class CorService {
	@Autowired
	private CorRepository repo;
	
	public Optional<CorId> executar(CriarCor comando) {
		Cor nova = repo.save(new Cor(comando));
		return Optional.of(nova.getId());
	}
	
	public Optional<Cor> encontrar(CorId id) {
		return repo.findById(id);
	}

	public List<Cor> encontrar() {
		return repo.findAll();
	}

	public void deletar(CorId id) {
		repo.deleteById(id);
	}
	
	

}
