package br.hela.emergencia;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.hela.emergencia.comandos.BuscarEmergencia;
import br.hela.emergencia.comandos.CriarEmergencia;
import br.hela.emergencia.comandos.EditarEmergencia;
//import br.hela.emergencia.emergencia_usuario.Emergencia_Usuario;
//import br.hela.emergencia.emergencia_usuario.Emergencia_Usuario_Service;
import br.hela.usuario.UsuarioId;

@Service
@Transactional
public class EmergenciaService {
	@Autowired
	private EmergenciaRepository repo;
//	@Autowired
//	private Emergencia_Usuario_Service servUser;

	public Optional<EmergenciaId> salvar(CriarEmergencia comando, UsuarioId id) {
		Emergencia novo = repo.save(new Emergencia(comando));
//		if (novo.getIdEmergencia() != null) {
//			Emergencia_Usuario emerUser = new Emergencia_Usuario();
//			emerUser.setIdEmergencia(novo.getIdEmergencia());
//			emerUser.setIdUsuario(id);
//			servUser.salvar(emerUser);
//		}
		return Optional.of(novo.getIdEmergencia());
	}

	public Optional<BuscarEmergencia> encontrar(EmergenciaId id) {
		Emergencia emergencia = repo.findById(id).get();
		BuscarEmergencia resultado = new BuscarEmergencia(emergencia);
		return Optional.of(resultado);
	}

	public Optional<List<BuscarEmergencia>> encontrar() {
		List<BuscarEmergencia> resultados = new ArrayList<>();
		List<Emergencia> emergencias = repo.findAll();
		for (Emergencia emergencia : emergencias) {
			BuscarEmergencia emer = new BuscarEmergencia(emergencia);
			resultados.add(emer);
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