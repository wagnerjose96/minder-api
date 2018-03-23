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
		return Optional.of(nova.getIdCirurgia());
	}
	
	public Optional<Cirurgia> encontrar(CirurgiaId id) {
		return cirurgiaRepo.findById(id);
	}

	public Optional<List<Cirurgia>> encontrar() {
		return Optional.of(cirurgiaRepo.findAll());
	}

	public Optional<String> deletar(CirurgiaId id) {
		cirurgiaRepo.deleteById(id);
		return Optional.of("Cirurgia -> " + id + ": deletada com sucesso");
	}
	
	public Optional<CirurgiaId> alterar(Cirurgia comando) {
		cirurgiaRepo.save(comando);
		return Optional.of(comando.getIdCirurgia());
	}

}
