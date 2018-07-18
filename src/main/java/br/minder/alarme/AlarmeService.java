package br.minder.alarme;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.minder.alarme.Alarme;
import br.minder.alarme.AlarmeId;
import br.minder.alarme.comandos.BuscarAlarme;
import br.minder.alarme.comandos.CriarAlarme;
import br.minder.alarme.comandos.EditarAlarme;
import br.minder.medicamento.MedicamentoService;
import br.minder.medicamento.comandos.BuscarMedicamento;
import br.minder.usuario.UsuarioId;

@Service
@Transactional
public class AlarmeService {
	@Autowired
	private AlarmeRepository repo;

	@Autowired
	private MedicamentoService medService;

	public Optional<AlarmeId> salvar(CriarAlarme comando, UsuarioId id) {
		if(comando.getDataInicio() != null && comando.getDataFim() != null) {
			Alarme novo = repo.save(new Alarme(comando, id));
			return Optional.of(novo.getId());
		}
		return Optional.empty();
	}

	public Optional<BuscarAlarme> encontrar(AlarmeId alarmeId) {
		Optional<Alarme> result = repo.findById(alarmeId);
		if (result.isPresent()) {
			BuscarAlarme resultado = new BuscarAlarme(result.get());
			Optional<BuscarMedicamento> medicamento = medService.encontrar(result.get().getIdMedicamento());
			if (medicamento.isPresent())
				resultado.setMedicamento(medicamento.get());

			return Optional.of(resultado);
		}
		return Optional.empty();
	}

	public Optional<List<BuscarAlarme>> encontrar(UsuarioId id) {
		List<BuscarAlarme> resultados = new ArrayList<>();
		List<Alarme> alarmes = repo.findAll();
		for (Alarme alarme : alarmes) {
			if(alarme.getIdUsuario().toString().equals(id.toString())) {
				BuscarAlarme nova = new BuscarAlarme(alarme);
				Optional<BuscarMedicamento> medicamento = medService.encontrar(alarme.getIdMedicamento());
				if (medicamento.isPresent())
					nova.setMedicamento(medicamento.get());
				resultados.add(nova);
			}
		}
		return Optional.of(resultados);
	}

	public Optional<AlarmeId> alterar(EditarAlarme comando) {
		Optional<Alarme> optional = repo.findById(comando.getId());
		if (optional.isPresent() && comando.getDataFim() != null && comando.getDataInicio() != null) {
			Alarme alarme = optional.get();
			alarme.apply(comando);
			repo.save(alarme);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

	public Optional<String> deletar(AlarmeId id) {
		if (repo.findById(id).isPresent()) {
			repo.deleteById(id);
			return Optional.of("Alarme ===> " + id + ": deletado com sucesso");
		}
		return Optional.empty();
	}
}
