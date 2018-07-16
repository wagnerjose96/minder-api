package br.minder.endereco;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.minder.endereco.comandos.BuscarEndereco;
import br.minder.endereco.comandos.CriarEndereco;
import br.minder.endereco.comandos.EditarEndereco;

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
		Optional<Endereco> endereco = repo.findById(id);
		if (endereco.isPresent()) {
			BuscarEndereco resultado = new BuscarEndereco(endereco.get());
			return Optional.of(resultado);
		}
		return Optional.empty();
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

	public Optional<EnderecoId> alterar(EditarEndereco comando) {
		Optional<Endereco> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			Endereco endereco = optional.get();
			endereco.apply(comando);
			repo.save(endereco);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

}
