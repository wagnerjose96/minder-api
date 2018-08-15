package br.minder.plano_de_saude;

import java.text.ParseException;
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
import br.minder.convenio.Convenio;
import br.minder.convenio.ConvenioRepository;
import br.minder.convenio.comandos.BuscarConvenio;
import br.minder.plano_de_saude.comandos.BuscarPlanoDeSaude;
import br.minder.plano_de_saude.comandos.CriarPlanoDeSaude;
import br.minder.plano_de_saude.comandos.EditarPlanoDeSaude;
import br.minder.usuario.UsuarioId;

@Service
@Transactional
public class PlanoDeSaudeService {

	@Autowired
	private PlanoDeSaudeRepository repo;

	@Autowired
	private ConvenioRepository convRepo;

	public Optional<String> deletar(PlanoDeSaudeId id, UsuarioId usuarioId) {
		if (repo.findById(id.toString(), usuarioId.toString()) != null) {
			repo.deleteById(id);
			return Optional.of("Plano de saúde ===> " + id + ": deletado com sucesso");
		}
		return Optional.empty();
	}

	public Optional<PlanoDeSaudeId> salvar(CriarPlanoDeSaude comando, UsuarioId id) throws ParseException {
		if (comando.getHabitacao() != null && comando.getIdConvenio() != null && comando.getNumeroCartao() != null
				&& comando.getTerritorio() != null) {
			PlanoDeSaude novo = repo.save(new PlanoDeSaude(comando, id));
			return Optional.of(novo.getId());
		}
		return Optional.empty();
	}

	public Optional<BuscarPlanoDeSaude> encontrar(PlanoDeSaudeId planoId, UsuarioId id) {
		Optional<PlanoDeSaude> plano = repo.findById(planoId);
		if (plano.isPresent() && id.toString().equals(plano.get().getIdUsuario().toString())) {
			BuscarPlanoDeSaude resultado = new BuscarPlanoDeSaude(plano.get());
			Convenio convenio = convRepo.findById(plano.get().getIdConvenio().toString());
			if (convenio != null) {
				resultado.setConvenio(new BuscarConvenio(convenio));
				return Optional.of(resultado);
			}
		}
		return Optional.empty();
	}

	public Optional<Page<BuscarPlanoDeSaude>> encontrar(Pageable pageable, UsuarioId id) {
		List<BuscarPlanoDeSaude> rsPlanos = new ArrayList<>();
		Page<PlanoDeSaude> planos = repo.findAll(pageable, id.toString());
		if (planos.hasContent()) {
			for (PlanoDeSaude plano : planos) {
				Convenio convenio = convRepo.findById(plano.getIdConvenio().toString());
				BuscarPlanoDeSaude nova = new BuscarPlanoDeSaude(plano);
				if (convenio != null) {
					nova.setConvenio(new BuscarConvenio(convenio));
					rsPlanos.add(nova);
				}
			}
			Page<BuscarPlanoDeSaude> page = new PageImpl<>(rsPlanos,
					PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()),
					rsPlanos.size());
			return Optional.of(page);
		}
		return Optional.empty();
	}

	public Optional<PlanoDeSaudeId> alterar(EditarPlanoDeSaude comando) throws ParseException {
		Optional<PlanoDeSaude> optional = repo.findById(comando.getId());
		if (comando.getHabitacao() != null && comando.getIdConvenio() != null && comando.getNumeroCartao() != null
				&& comando.getTerritorio() != null && optional.isPresent()) {
			PlanoDeSaude plano = optional.get();
			plano.apply(comando);
			repo.save(plano);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

}
