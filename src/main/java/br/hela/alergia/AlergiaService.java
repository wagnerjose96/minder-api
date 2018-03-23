package br.hela.alergia;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.hela.alergia.comandos.CriarAlergia;
import br.hela.alergia.comandos.EditarAlergia;

@Service
@Transactional
public class AlergiaService {
	@Autowired
	private AlergiaRepository alergiaRepo;
	
	public Optional<AlergiaId> salvar(CriarAlergia comando) {
		Alergia nova = alergiaRepo.save(new Alergia(comando));
		return Optional.of(nova.getIdAlergia());
	}
	
	public Optional<Alergia> encontrar(AlergiaId id) {
		return alergiaRepo.findById(id);
	}

	public Optional<List<Alergia>> encontrar() {
		return Optional.of(alergiaRepo.findAll());
	}

	public Optional<String> deletar(AlergiaId id) {
		alergiaRepo.deleteById(id);
		return Optional.of("Alergia -> " + id + ": deletada com sucesso");
	}
	
	public Optional<AlergiaId> alterar(EditarAlergia comando) {
		Optional<Alergia> optional = alergiaRepo.findById(comando.getIdAlergia());
		if (optional.isPresent()) {
			Alergia alergia = optional.get();
			alergia.apply(comando);
			alergiaRepo.save(alergia);
			return Optional.of(comando.getIdAlergia());
		}
		return Optional.empty();
	}

}
