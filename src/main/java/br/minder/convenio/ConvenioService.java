package br.minder.convenio;

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
import br.minder.convenio.ConvenioId;
import br.minder.convenio.ConvenioRepository;
import br.minder.convenio.comandos.BuscarConvenio;
import br.minder.convenio.comandos.CriarConvenio;
import br.minder.convenio.comandos.EditarConvenio;

@Service
@Transactional
public class ConvenioService {
	@Autowired
	private ConvenioRepository convenioRepo;

	public Optional<ConvenioId> salvar(CriarConvenio comando) {
		if (comando.getNome() != null) {
			Convenio novo = convenioRepo.save(new Convenio(comando));
			return Optional.of(novo.getId());
		}
		return Optional.empty();
	}

	public Optional<BuscarConvenio> encontrar(ConvenioId id) {
		Optional<Convenio> convenio = convenioRepo.findById(id);
		if (convenio.isPresent() && convenio.get().getAtivo() == 1) {
			BuscarConvenio resultado = new BuscarConvenio(convenio.get());
			return Optional.of(resultado);
		}
		return Optional.empty();
	}

	public Optional<Page<BuscarConvenio>> encontrar(Pageable pageable) {
		List<BuscarConvenio> resultados = new ArrayList<>();
		List<Convenio> convenios = convenioRepo.findAll();
		if (!convenios.isEmpty()) {
			for (Convenio convenio : convenios) {
				if (convenio.getAtivo() == 1) {
					BuscarConvenio nova = new BuscarConvenio(convenio);
					resultados.add(nova);
				}
			}
			@SuppressWarnings("deprecation")
			Page<BuscarConvenio> page = new PageImpl<>(resultados,
					new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()),
					resultados.size());
			return Optional.of(page);
		}
		return Optional.empty();
	}

	public Optional<String> deletar(ConvenioId id) {
		Optional<Convenio> convenio = convenioRepo.findById(id);
		if (convenio.isPresent()) {
			convenio.get().setAtivo(0);
			convenioRepo.save(convenio.get());
			return Optional.of("Convênio ===> " + id + ": deletado com sucesso");
		}
		return Optional.empty();
	}

	public Optional<ConvenioId> alterar(EditarConvenio comando) {
		Optional<Convenio> optional = convenioRepo.findById(comando.getId());
		if (optional.isPresent() && comando.getNome() != null) {
			Convenio conv = optional.get();
			conv.apply(comando);
			convenioRepo.save(conv);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

}
