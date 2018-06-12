package br.hela.medicamento;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.hela.medicamento.comandos.CriarMedicamento;
import br.hela.medicamento.comandos.EditarMedicamento;

@Service
@Transactional
public class MedicamentoService {
	@Autowired
	private MedicamentoRepository medicamentoRepo;

	public Optional<MedicamentoId> salvar(CriarMedicamento comando) {
		Medicamento novo = medicamentoRepo.save(new Medicamento(comando));
		return Optional.of(novo.getIdMedicamento());
	}

	public Optional<Medicamento> encontrar(MedicamentoId id) {
		Medicamento medicamento = medicamentoRepo.findById(id).get();
		if (medicamento.getAtivo() == 1) {
			return Optional.of(medicamento);
		}
		return Optional.empty();
	}

	public Optional<List<Medicamento>> encontrar() {
		List<Medicamento> resultados = new ArrayList<>();
		List<Medicamento> medicamentos = medicamentoRepo.findAll();
		for (Medicamento medicamento : medicamentos) {
			if (medicamento.getAtivo() == 1) {
				resultados.add(medicamento);
			}
		}
		return Optional.of(resultados);
	}

	public Optional<String> deletar(MedicamentoId id) {
		Medicamento medicamento = medicamentoRepo.findById(id).get();
		medicamento.setAtivo(0);
		medicamentoRepo.save(medicamento);
		return Optional.of("Medicamento -> " + id + ": deletado com sucesso");
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