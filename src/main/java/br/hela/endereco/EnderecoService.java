package br.hela.endereco;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.hela.endereco.comandos.BuscarEndereco;
import br.hela.endereco.comandos.CriarEndereco;
import br.hela.endereco.comandos.EditarEndereco;

@Service
@Transactional
public class EnderecoService {
	@Autowired
	private EnderecoRepository repo;

	public Optional<EnderecoId> salvar(CriarEndereco comando) {
		Endereco nova = repo.save(new Endereco(comando));
		return Optional.of(nova.getId());
	}

	public Optional<BuscarEndereco> encontrar(EnderecoId id) {
		Endereco endereco = repo.findById(id).get();
		BuscarEndereco resultado = new BuscarEndereco(endereco);
		return Optional.of(resultado);
	}

	public Optional<List<BuscarEndereco>> encontrar() {
		List<Endereco> enderecos = repo.findAll();
		List<BuscarEndereco> resultados = new ArrayList<>();
		for (Endereco endereco : enderecos) {
			BuscarEndereco nova = new BuscarEndereco(endereco);
			resultados.add(nova);
		}
		return Optional.of(resultados);
	}

	public Optional<String> deletar(EnderecoId id) {
		repo.deleteById(id);
		return Optional.of("Endereço -> " + id + ": deletado com sucesso");
	}

	public Optional<EnderecoId> alterar(EditarEndereco comando) {
		Optional<Endereco> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			Endereco Endereco = optional.get();
			Endereco.apply(comando);
			repo.save(Endereco);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

}
