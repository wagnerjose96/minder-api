package br.hela.emergencia;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

		public Optional<Emergencia> encontrar(EmergenciaId id) {
			return repo.findById(id);
		}

		public Optional<List<Emergencia>> encontrar() {
			return Optional.of(repo.findAll());
		}

		public Optional<String> deletar(EmergenciaId id) {
			repo.deleteById(id);
			return Optional.of("Emergência -> " + id + ": deletada com sucesso");
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

		public void deletarTodos() {
			repo.deleteAll();
		}

	
}
