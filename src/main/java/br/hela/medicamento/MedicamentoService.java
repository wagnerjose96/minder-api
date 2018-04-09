package br.hela.medicamento;

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
		return medicamentoRepo.findById(id);
	}

	public Optional<List<Medicamento>> encontrar() {
		return Optional.of(medicamentoRepo.findAll());
	}

	public Optional<String> deletar(MedicamentoId id) {
		medicamentoRepo.deleteById(id);
		return Optional.of("Usuário -> " + id + ": deletado com sucesso");
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
