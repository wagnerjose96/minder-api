package br.hela.endereco;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.hela.endereco.comandos.CriarEndereco;
import br.hela.endereco.comandos.EditarEndereco;

@Service
@Transactional
public class EnderecoService {
	@Autowired
	private EnderecoRepository EnderecoRepo;
	
	public Optional<EnderecoId> salvar(CriarEndereco comando) {
		Endereco nova = EnderecoRepo.save(new Endereco(comando));
		return Optional.of(nova.getId());
	}
	
	public Optional<Endereco> encontrar(EnderecoId id) {
		return EnderecoRepo.findById(id);
	}

	public Optional<List<Endereco>> encontrar() {
		return Optional.of(EnderecoRepo.findAll());
	}

	public Optional<String> deletar(EnderecoId id) {
		EnderecoRepo.deleteById(id);
		return Optional.of("Endereco -> " + id + ": deletada com sucesso");
	}
	
	public Optional<EnderecoId> alterar(EditarEndereco comando) {
		Optional<Endereco> optional = EnderecoRepo.findById(comando.getId());
		if (optional.isPresent()) {
			Endereco Endereco = optional.get();
			Endereco.apply(comando);
			EnderecoRepo.save(Endereco);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

}

