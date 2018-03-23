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
	
	public Optional<MedicamentoContinuoId> salvar(CriarMedicamentoContinuo comando) {
		MedicamentoContinuo novo = medicamentoContinuoRepo.save(new MedicamentoContinuo(comando));
		return Optional.of(novo.getIdMedicamentoContinuo());
	}
	
	public Optional<MedicamentoContinuo> encontrar(MedicamentoContinuoId id) {
		return medicamentoContinuoRepo.findById(id);
	}

	public Optional<List<MedicamentoContinuo>> encontrar() {
		return Optional.of(medicamentoContinuoRepo.findAll());
	}

	public Optional<String> deletar(MedicamentoContinuoId id) {
		medicamentoContinuoRepo.deleteById(id);
		return Optional.of("Usuário -> " + id + ": deletado com sucesso");
	}
	
	public Optional<MedicamentoContinuoId> alterar(MedicamentoContinuo comando) {
		medicamentoContinuoRepo.save(comando);
		return Optional.of(comando.getIdMedicamentoContinuo());
	}
	
}	