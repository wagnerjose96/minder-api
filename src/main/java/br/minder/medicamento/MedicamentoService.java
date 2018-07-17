package br.minder.medicamento;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.minder.medicamento.comandos.BuscarMedicamento;
import br.minder.medicamento.comandos.CriarMedicamento;
import br.minder.medicamento.comandos.EditarMedicamento;

@Service
@Transactional
public class MedicamentoService {
	@Autowired
	private MedicamentoRepository medicamentoRepo;

	public Optional<MedicamentoId> salvar(CriarMedicamento comando) {
		Medicamento novo = medicamentoRepo.save(new Medicamento(comando));
		return Optional.of(novo.getIdMedicamento());
	}

	public Optional<BuscarMedicamento> encontrar(MedicamentoId id) {
		Optional<Medicamento> medicamento = medicamentoRepo.findById(id);
		if (medicamento.isPresent() && medicamento.get().getAtivo() == 1) {
			BuscarMedicamento resultado = new BuscarMedicamento(medicamento.get());
			return Optional.of(resultado);
		}
		return Optional.empty();
	}

	public Optional<List<BuscarMedicamento>> encontrar() {
		List<BuscarMedicamento> resultados = new ArrayList<>();
		List<Medicamento> medicamentos = medicamentoRepo.findAll();
		for (Medicamento medicamento : medicamentos) {
			if (medicamento.getAtivo() == 1) {
				BuscarMedicamento med = new BuscarMedicamento(medicamento);
				resultados.add(med);
			}
		}
		return Optional.of(resultados);
	}

	public Optional<String> deletar(MedicamentoId id) {
		Optional<Medicamento> medicamento = medicamentoRepo.findById(id);
		if (medicamento.isPresent()) {
			medicamento.get().setAtivo(0);
			medicamentoRepo.save(medicamento.get());
			return Optional.of("Medicamento ===> " + id + ": deletado com sucesso");
		}
		return Optional.empty();
	}

	public Optional<MedicamentoId> alterar(EditarMedicamento comando) {
		Optional<Medicamento> optional = medicamentoRepo.findById(comando.getIdMedicamento());
		if (optional.isPresent()) {
			Medicamento med = optional.get();
			med.apply(comando);
			medicamentoRepo.save(med);
			return Optional.of(comando.getIdMedicamento());
		}
		return Optional.empty();
	}
}