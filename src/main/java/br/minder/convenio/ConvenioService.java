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
import br.minder.conversor.TermoDeBusca;

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
		Convenio convenio = convenioRepo.findById(id.toString());
		if (convenio != null) {
			BuscarConvenio resultado = new BuscarConvenio(convenio);
			return Optional.of(resultado);
		}
		return Optional.empty();
	}

	public Optional<Page<BuscarConvenio>> encontrar(Pageable pageable, String searchTerm) {
		List<BuscarConvenio> resultados = new ArrayList<>();
		Page<Convenio> convenios = convenioRepo.findAll(pageable);
		if (convenios.hasContent()) {
			for (Convenio convenio : convenios) {
				if (convenio.getAtivo() == 1 && TermoDeBusca.searchTerm(convenio.getNome(), searchTerm)) {
					BuscarConvenio nova = new BuscarConvenio(convenio);
					resultados.add(nova);
				}
			}
			Page<BuscarConvenio> page = new PageImpl<>(resultados,
					PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()),
					resultados.size());
			return Optional.of(page);
		}
		return Optional.empty();
	}

	public Optional<String> deletar(ConvenioId id) {
		Convenio convenio = convenioRepo.findById(id.toString());
		if (convenio != null) {
			convenio.setAtivo(0);
			convenioRepo.save(convenio);
			return Optional.of("Convênio ===> " + id + ": deletado com sucesso");
		}
		return Optional.empty();
	}

	public Optional<ConvenioId> alterar(EditarConvenio comando) {
		Convenio optional = convenioRepo.findById(comando.getId().toString());
		if (optional != null && comando.getNome() != null) {
			Convenio conv = optional;
			conv.apply(comando);
			convenioRepo.save(conv);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

}
