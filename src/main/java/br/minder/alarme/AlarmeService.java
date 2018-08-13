package br.minder.alarme;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.minder.alarme.Alarme;
import br.minder.alarme.AlarmeId;
import br.minder.alarme.comandos.BuscarAlarme;
import br.minder.alarme.comandos.CriarAlarme;
import br.minder.alarme.comandos.EditarAlarme;
import br.minder.medicamento.Medicamento;
import br.minder.medicamento.MedicamentoRepository;
import br.minder.medicamento.comandos.BuscarMedicamento;
import br.minder.usuario.UsuarioId;

@Service
@Transactional
public class AlarmeService {
	@Autowired
	private AlarmeRepository repo;

	@Autowired
	private MedicamentoRepository medRepo;

	public Optional<AlarmeId> salvar(CriarAlarme comando, UsuarioId id) {
		if (comando.getDataInicio() != null && comando.getDataFim() != null && comando.getIdMedicamento() != null
				&& comando.getQuantidade() != null && comando.getDescricao() != null) {
			Alarme novo = repo.save(new Alarme(comando, id));
			return Optional.of(novo.getId());
		}
		return Optional.empty();
	}

	public Optional<BuscarAlarme> encontrar(AlarmeId alarmeId, UsuarioId usuarioId) {
		Optional<Alarme> result = repo.findById(alarmeId);
		if (result.isPresent() && result.get().getIdUsuario().toString().equals(usuarioId.toString())) {
			BuscarAlarme resultado = new BuscarAlarme(result.get());
			Optional<Medicamento> medicamento = medRepo.findById(result.get().getIdMedicamento());
			if (medicamento.isPresent())
				resultado.setMedicamento(new BuscarMedicamento(medicamento.get()));

			return Optional.of(resultado);
		}
		return Optional.empty();
	}

	public Optional<Page<BuscarAlarme>> encontrar(Pageable pageable, UsuarioId id) {
		List<BuscarAlarme> resultados = new ArrayList<>();
		List<Alarme> alarmes = repo.findAll();
		if (!alarmes.isEmpty()) {
			for (Alarme alarme : alarmes) {
				if (alarme.getIdUsuario().toString().equals(id.toString())) {
					BuscarAlarme nova = new BuscarAlarme(alarme);
					Optional<Medicamento> medicamento = medRepo.findById(alarme.getIdMedicamento());
					if (medicamento.isPresent())
						nova.setMedicamento(new BuscarMedicamento(medicamento.get()));
					resultados.add(nova);
				}
			}
			@SuppressWarnings("deprecation")
			Page<BuscarAlarme> page = new PageImpl<>(resultados,
					new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()),
					resultados.size());
			return Optional.of(page);
		}
		return Optional.empty();
	}

	public Optional<AlarmeId> alterar(EditarAlarme comando) {
		Optional<Alarme> optional = repo.findById(comando.getId());
		if (comando.getDataInicio() != null && comando.getDataFim() != null && comando.getIdMedicamento() != null
				&& comando.getQuantidade() != null && comando.getDescricao() != null && optional.isPresent()) {
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