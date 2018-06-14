package br.hela.emergencia;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.hela.emergencia.comandos.BuscarEmergencia;
import br.hela.emergencia.comandos.CriarEmergencia;
import br.hela.emergencia.comandos.EditarEmergencia;

@Service
@Transactional
public class EmergenciaService {
	@Autowired
	private EmergenciaRepository repo;

	public Optional<EmergenciaId> salvar(CriarEmergencia comando) {
		Emergencia novo = repo.save(new Emergencia(comando));
		return Optional.of(novo.getId());
	}

	public Optional<BuscarEmergencia> encontrar(EmergenciaId id) {
		Emergencia emergencia = repo.findById(id).get();
		BuscarEmergencia resultado = new BuscarEmergencia(emergencia);
		return Optional.of(resultado);
	}

	public Optional<List<BuscarEmergencia>> encontrar() {
		List<Emergencia> emergencias = repo.findAll();
		List<BuscarEmergencia> resultados = new ArrayList<>();
		for (Emergencia emergencia : emergencias) {
			BuscarEmergencia nova = new BuscarEmergencia(emergencia);
			resultados.add(nova);
		}
		return Optional.of(resultados);
	}

	public Optional<EmergenciaId> alterar(EditarEmergencia comando) {
		Optional<Emergencia> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			Emergencia emer = optional.get();
			emer.apply(comando);
			repo.save(emer);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

}
