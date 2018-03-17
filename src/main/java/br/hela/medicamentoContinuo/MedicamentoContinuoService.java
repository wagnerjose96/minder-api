package br.hela.medicamentoContinuo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.hela.medicamentoContinuo.comandos.CriarMedicamentoContinuo;

@Service
@Transactional
public class MedicamentoContinuoService {
	
	@Autowired
	private MedicamentoContinuoRepository repo;
	
	public Optional<MedicamentoContinuoId> executar(CriarMedicamentoContinuo comando) {
		MedicamentoContinuo nova = repo.save(new MedicamentoContinuo(comando));
		return Optional.of(nova.getIdMedicamentoContinuo());
	}
	
	public Optional<MedicamentoContinuo> encontrar(MedicamentoContinuoId id) {
		return repo.findById(id);
	}

	public List<MedicamentoContinuo> encontrar() {
		return repo.findAll();
	}

	public void deletar(MedicamentoContinuoId id) {
		repo.deleteById(id);
	}
}	