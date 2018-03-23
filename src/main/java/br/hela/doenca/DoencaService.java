package br.hela.doenca;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.hela.doenca.comandos.CriarDoenca;

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
	
	public Optional<DoencaId> alterar(Doenca comando) {
			doencaRepo.save(comando);
			return Optional.of(comando.getIdDoenca());
	}

}
