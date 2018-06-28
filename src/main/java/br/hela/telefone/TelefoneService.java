package br.hela.telefone;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.hela.telefone.comandos.BuscarTelefone;
import br.hela.telefone.comandos.CriarTelefone;
import br.hela.telefone.comandos.EditarTelefone;

@Service
@Transactional
public class TelefoneService {
	@Autowired
	private TelefoneRepository repo;

	public Optional<TelefoneId> salvar(CriarTelefone comando) {
		Telefone novo = repo.save(new Telefone(comando));
		return Optional.of(novo.getId());
	}

	public Optional<BuscarTelefone> encontrar(TelefoneId id) {
		Optional<Telefone> plano = repo.findById(id);
		if (plano.isPresent()) {
			BuscarTelefone resultado = new BuscarTelefone(plano.get());
			return Optional.of(resultado);
		}
		return Optional.empty();
	}

	public Optional<List<BuscarTelefone>> encontrar() {
		List<BuscarTelefone> resultados = new ArrayList<>();
		List<Telefone> telefones = repo.findAll();
		for (Telefone telefone : telefones) {
			BuscarTelefone tel = new BuscarTelefone(telefone);
			resultados.add(tel);
		}
		return Optional.of(resultados);
	}

	public Optional<TelefoneId> alterar(EditarTelefone comando) {
		Optional<Telefone> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			Telefone tel = optional.get();
			tel.apply(comando);
			repo.save(tel);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

	public Optional<String> deletar(TelefoneId idTelefone) {
		repo.deleteById(idTelefone);
		return Optional.of("Telefone " + idTelefone + " deletado com sucesso");
	}
}