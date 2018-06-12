package br.hela.telefone;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.hela.telefone.Telefone;
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
		Telefone telefone = telefoneRepo.findById(id).get();
		if (telefone.getAtivo() == 1) {
			return Optional.of(telefone);
		}
		return Optional.empty();
	}

	public Optional<List<Telefone>> encontrar() {
		List<Telefone> resultados = new ArrayList<>();
		List<Telefone> telefones = telefoneRepo.findAll();
		for (Telefone telefone : telefones) {
			if (telefone.getAtivo() == 1) {
				resultados.add(telefone);
			}
		}
		return Optional.of(resultados);	
	}

	public Optional<String> deletar(TelefoneId id) {
		Telefone telefone = telefoneRepo.findById(id).get();
		telefone.setAtivo(0);
		telefoneRepo.save(telefone);
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