package br.hela.alergia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.hela.alergia.Alergia;
import br.hela.alergia.AlergiaId;
import br.hela.alergia.alergia_medicamento.Alergia_Medicamento;
import br.hela.alergia.alergia_medicamento.Alergia_Medicamento_Service;
import br.hela.alergia.comandos.CriarAlergia;
import br.hela.alergia.comandos.EditarAlergia;
import br.hela.medicamento.MedicamentoId;
import br.hela.medicamento.MedicamentoService;

@Service
@Transactional
public class AlergiaService {
	@Autowired
	private AlergiaRepository repo;
	
	@Autowired
	private Alergia_Medicamento_Service service;
	
	@Autowired
	private MedicamentoService medicamentoService;

	public Optional<AlergiaId> salvar(CriarAlergia comando) throws NullPointerException {
		Alergia novo = repo.save(new Alergia(comando));
		for (MedicamentoId id_medicamento : comando.getId_medicamentos()) {
			if(verificaMedicamentoExistente(id_medicamento)) {
				Alergia_Medicamento alergiaMedicamento = new Alergia_Medicamento();
				alergiaMedicamento.setIdAlergia(novo.getIdAlergia());
				alergiaMedicamento.setIdMedicamento(id_medicamento);
				service.salvar(alergiaMedicamento);
			}
		}
		return Optional.of(novo.getIdAlergia());
	}

	
	public Optional<Alergia> encontrar(AlergiaId id) {
		return repo.findById(id);
	}

	public Optional<List<Alergia>> encontrar() {
		return Optional.of(repo.findAll());
		/*"SELECT * FROM ALERGIA INNER JOIN ALERGIA_MEDICAMENTO"
        + "ON ALERGIA.ID = ALERGIA_MEDICAMENTO.ID_ALERGIA"*/
	}

	public Optional<String> deletar(AlergiaId id) {
		repo.deleteById(id);
		return Optional.of("Alergia -> " + id + ": deletada com sucesso");
	}

	public Optional<AlergiaId> alterar(EditarAlergia comando) {
		Optional<Alergia> optional = repo.findById(comando.getIdAlergia());
		if (optional.isPresent()) {
			Alergia alergia = optional.get();
			alergia.apply(comando);
			repo.save(alergia);
			return Optional.of(comando.getIdAlergia());
		}
		return Optional.empty();
	}
	
	private boolean verificaMedicamentoExistente(MedicamentoId id) {
		if (!medicamentoService.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}
}
