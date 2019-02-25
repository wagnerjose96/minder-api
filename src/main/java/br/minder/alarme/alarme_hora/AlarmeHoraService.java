package br.minder.alarme.alarme_hora;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.minder.alarme.AlarmeId;
import br.minder.alarme.alarme_hora.comandos.CriarAlarmeHora;
import br.minder.usuario.UsuarioId;

@Service
@Transactional
public class AlarmeHoraService {
	@Autowired
	private AlarmeHoraRepository repo;

	public Optional<AlarmeHoraId> salvar(CriarAlarmeHora comando, UsuarioId id) {
		if (comando.getDataAlarme() != null && comando.getIdAlarme() != null) {
			AlarmeHora novo = repo.save(new AlarmeHora(comando, id));
			return Optional.of(novo.getId());
		}
		return Optional.empty();
	}

	public Optional<List<AlarmeHora>> encontrar(AlarmeId alarmeId, UsuarioId usuarioId) {
		List<AlarmeHora> result = repo.findById(usuarioId.toString(), alarmeId.toString());
		if (!result.isEmpty()) {
			return Optional.of(result);
		}
		return Optional.empty();
	}

	public Optional<List<AlarmeHora>> encontrar(UsuarioId id) {
		List<AlarmeHora> alarmes = repo.findAll(id.toString());
		if (!alarmes.isEmpty()) {
			return Optional.of(alarmes);
		}
		return Optional.empty();
	}	
}