package br.hela.contatoEmergencia;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.hela.contatoEmergencia.comandos.BuscarContatoEmergencia;
import br.hela.contatoEmergencia.comandos.CriarContatoEmergencia;
import br.hela.contatoEmergencia.comandos.EditarContatoEmergencia;

@Service
@Transactional
public class ContatoEmergenciaService {
	@Autowired
	private ContatoEmergenciaRepository contatoEmergenciaRepo;

	public Optional<ContatoEmergenciaId> executar(CriarContatoEmergencia comando) {
		ContatoEmergencia nova = contatoEmergenciaRepo.save(new ContatoEmergencia(comando));
		return Optional.of(nova.getIdContatoEmergencia());
	}

	public Optional<ContatoEmergenciaId> salvar(CriarContatoEmergencia comando) throws NullPointerException {
		ContatoEmergencia novo = contatoEmergenciaRepo.save(new ContatoEmergencia(comando));
		return Optional.of(novo.getIdContatoEmergencia());
	}

	public Optional<BuscarContatoEmergencia> encontrar(ContatoEmergenciaId contatoEmergenciaId) throws Exception {
		BuscarContatoEmergencia contatoEmergencia = new BuscarContatoEmergencia(
				contatoEmergenciaRepo.findById(contatoEmergenciaId).get());
		return Optional.of(contatoEmergencia);
	}

	public Optional<List<ContatoEmergencia>> encontrar() throws Exception {
		List<ContatoEmergencia> contatoEmergencias = contatoEmergenciaRepo.findAll();
		return Optional.of(contatoEmergencias);
	}

	public Optional<ContatoEmergenciaId> alterar(EditarContatoEmergencia comando) {
		Optional<ContatoEmergencia> optional = contatoEmergenciaRepo.findById(comando.getIdContatoEmergencia());
		if (optional.isPresent()) {
			ContatoEmergencia cirurgia = optional.get();
			cirurgia.apply(comando);
			contatoEmergenciaRepo.save(cirurgia);
			return Optional.of(comando.getIdContatoEmergencia());
		}
		return Optional.empty();
	}

}
