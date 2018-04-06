package br.hela.doenca;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.hela.doenca.comandos.CriarDoenca;
import br.hela.doenca.comandos.EditarDoenca;

@Service
@Transactional
public class DoencaService {
	@Autowired
	private DoencaRepository doencaRepo;

	public Optional<DoencaId> executar(CriarDoenca comando) {
		Doenca nova = doencaRepo.save(new Doenca(comando));
		return Optional.of(nova.getIdDoenca());
	}

	public Optional<Doenca> encontrar(DoencaId id) {
		return doencaRepo.findById(id);
	}

	public Optional<List<Doenca>> encontrar() {
		return Optional.of(doencaRepo.findAll());
	}

	public Optional<String> deletar(DoencaId id) {
		doencaRepo.deleteById(id);
		return Optional.of("Doença -> " + id + " deletado com sucesso");
	}

	public Optional<DoencaId> alterar(EditarDoenca comando) {
		Optional<Doenca> optional = doencaRepo.findById(comando.getIdDoenca());
		if (optional.isPresent()) {
			Doenca doenca = optional.get();
			doenca.apply(comando);
			doencaRepo.save(doenca);
			return Optional.of(comando.getIdDoenca());
		}
		return Optional.empty();
	}
}
