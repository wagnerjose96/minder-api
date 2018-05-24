package br.hela.telefone;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.hela.telefone.comandos.CriarTelefone;
import br.hela.telefone.comandos.EditarTelefone;

@Service
@Transactional
public class TelefoneService {
	@Autowired
	private TelefoneRepository telefoneRepo;

	public Optional<TelefoneId> salvar(CriarTelefone comando) {
		Telefone novo = telefoneRepo.save(new Telefone(comando));
		return Optional.of(novo.getIdTelefone());
	}

	public Optional<Telefone> encontrar(TelefoneId id) {
		return telefoneRepo.findById(id);
	}

	public Optional<List<Telefone>> encontrar() {
		return Optional.of(telefoneRepo.findAll());
	}

	public Optional<String> deletar(TelefoneId id) {
		telefoneRepo.deleteById(id);
		return Optional.of("Telefone -> " + id + ": deletado com sucesso");
	}

	public Optional<TelefoneId> alterar(EditarTelefone comando) {
		Optional<Telefone> optional = telefoneRepo.findById(comando.getIdTelefone());
		if (optional.isPresent()) {
			Telefone tel = optional.get();
			tel.apply(comando);
			telefoneRepo.save(tel);
			return Optional.of(comando.getIdTelefone());
		}
		return Optional.empty();
	}
}