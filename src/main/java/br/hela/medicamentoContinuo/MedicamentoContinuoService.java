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
	private MedicamentoContinuoRepository medicamentoContinuoRepo;
	
	public Optional<MedicamentoContinuoId> executar(CriarMedicamentoContinuo comando) {
		MedicamentoContinuo nova = medicamentoContinuoRepo.save(new MedicamentoContinuo(comando));
		return Optional.of(nova.getIdMedicamentoContinuo());
	}
	
	public Optional<MedicamentoContinuo> encontrar(MedicamentoContinuoId id) {
		return medicamentoContinuoRepo.findById(id);
	}

	public List<MedicamentoContinuo> encontrar() {
		return medicamentoContinuoRepo.findAll();
	}

	public void deletar(MedicamentoContinuoId id) {
		medicamentoContinuoRepo.deleteById(id);
	}
	
	public Optional<MedicamentoContinuoId> alterar(MedicamentoContinuo comando) {
		medicamentoContinuoRepo.save(comando);
		return Optional.of(comando.getIdMedicamentoContinuo());
	}
	
}	