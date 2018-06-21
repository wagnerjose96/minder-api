package br.hela.alarme;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.hela.alarme.Alarme;
import br.hela.alarme.AlarmeId;
import br.hela.alarme.comandos.BuscarAlarme;
import br.hela.alarme.comandos.CriarAlarme;
import br.hela.alarme.comandos.EditarAlarme;
import br.hela.medicamento.MedicamentoService;
import br.hela.medicamento.comandos.BuscarMedicamento;

@Service
@Transactional
public class AlarmeService {
	@Autowired
	private AlarmeRepository repo;

	@Autowired
	private MedicamentoService medService;

	public Optional<AlarmeId> salvar(CriarAlarme comando) {
		Alarme novo = repo.save(new Alarme(comando));
		return Optional.of(novo.getId());
	}

	public Optional<BuscarAlarme> encontrar(AlarmeId alarmeId) {
		Alarme alarme = repo.findById(alarmeId).get();
		BuscarAlarme resultado = new BuscarAlarme(alarme);
		Optional<BuscarMedicamento> medicamento = medService.encontrar(alarme.getIdMedicamento());
		resultado.setMedicamento(medicamento.get());
		return Optional.of(resultado);
	}

	public Optional<List<BuscarAlarme>> encontrar() {
		List<BuscarAlarme> resultados = new ArrayList<>();
		List<Alarme> alarmes = repo.findAll();
		for (Alarme alarme : alarmes) {
			BuscarAlarme nova = new BuscarAlarme(alarme);
			Optional<BuscarMedicamento> medicamento = medService.encontrar(alarme.getIdMedicamento());
			nova.setMedicamento(medicamento.get());
			resultados.add(nova);
		}
		return Optional.of(resultados);
	}

	public Optional<AlarmeId> alterar(EditarAlarme comando) {
		Optional<Alarme> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
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
			return Optional.of("Alarme " + id + " deletado com sucesso");
		}
		return Optional.empty();
	}
}
