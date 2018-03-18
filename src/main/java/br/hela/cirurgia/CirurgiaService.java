package br.hela.cirurgia;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.hela.cirurgia.comandos.CriarCirurgia;

@Service
@Transactional
public class CirurgiaService {
	@Autowired
	private CirurgiaRepository cirurgiaRepo;
	
	public Optional<CirurgiaId> executar(CriarCirurgia comando) {
		Cirurgia nova = cirurgiaRepo.save(new Cirurgia(comando));
		return Optional.of(nova.getId());
	}
	
	public Optional<CirurgiaId> alterar(Cirurgia nova) {
		cirurgiaRepo.save(nova);
		return Optional.of(nova.getId());
	}
	
	public Optional<Cirurgia> encontrar(CirurgiaId id) {
		return cirurgiaRepo.findById(id);
	}

	public List<Cirurgia> encontrar() {
		return cirurgiaRepo.findAll();
	}

	public void deletar(CirurgiaId id) {
		cirurgiaRepo.deleteById(id);
	}
	
}
